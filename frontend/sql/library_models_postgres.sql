-- PostgreSQL schema generated from models:
-- Audiobook, Book, Copy, Item, Katalog, LibraryManager, Loans, LoansHistory, NewsPaper, User
-- Date: 2026-03-19

BEGIN;

-- 1) Enum-like constraints
CREATE TABLE IF NOT EXISTS user_statuses (
  value VARCHAR(20) PRIMARY KEY
);

INSERT INTO user_statuses(value)
VALUES ('admin'), ('user')
ON CONFLICT (value) DO NOTHING;

CREATE TABLE IF NOT EXISTS copy_statuses (
  value VARCHAR(20) PRIMARY KEY
);

INSERT INTO copy_statuses(value)
VALUES ('available'), ('borrowed'), ('reserved')
ON CONFLICT (value) DO NOTHING;

-- 2) Users (User)
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  surname VARCHAR(120) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  status VARCHAR(20) NOT NULL REFERENCES user_statuses(value),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 3) Catalogs (Katalog)
CREATE TABLE IF NOT EXISTS catalogs (
  id BIGSERIAL PRIMARY KEY,
  nazwa VARCHAR(200) NOT NULL,
  opis TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 4) Items base table (Item)
CREATE TABLE IF NOT EXISTS items (
  id BIGINT PRIMARY KEY,
  catalog_id BIGINT NOT NULL REFERENCES catalogs(id) ON DELETE CASCADE,
  item_type VARCHAR(20) NOT NULL CHECK (item_type IN ('book', 'audiobook', 'newspaper')),
  tytul VARCHAR(300) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (catalog_id, tytul)
);

-- 5) Item specializations
CREATE TABLE IF NOT EXISTS books (
  item_id BIGINT PRIMARY KEY REFERENCES items(id) ON DELETE CASCADE,
  autor VARCHAR(200) NOT NULL,
  isbn VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS audiobooks (
  item_id BIGINT PRIMARY KEY REFERENCES items(id) ON DELETE CASCADE,
  time_length_minutes INT NOT NULL CHECK (time_length_minutes > 0),
  lector VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS newspapers (
  item_id BIGINT PRIMARY KEY REFERENCES items(id) ON DELETE CASCADE,
  numer_wydania INT NOT NULL,
  data_publikacji DATE NOT NULL,
  UNIQUE (numer_wydania, data_publikacji)
);

-- 6) Copies (Copy / Katalog.Setofcopies)
CREATE TABLE IF NOT EXISTS copies (
  id BIGINT PRIMARY KEY,
  item_id BIGINT NOT NULL REFERENCES items(id) ON DELETE CASCADE,
  status VARCHAR(20) NOT NULL DEFAULT 'available' REFERENCES copy_statuses(value),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_copies_item_id ON copies(item_id);
CREATE INDEX IF NOT EXISTS idx_copies_status ON copies(status);

-- 7) Active loans (Loans)
CREATE TABLE IF NOT EXISTS loans (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id),
  copy_id BIGINT NOT NULL REFERENCES copies(id),
  from_ts TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  due_ts TIMESTAMPTZ,
  -- one copy can have at most one active loan
  UNIQUE (copy_id)
);

CREATE INDEX IF NOT EXISTS idx_loans_user_id ON loans(user_id);

-- 8) Loan history (LoansHistory)
CREATE TABLE IF NOT EXISTS loan_history (
  id BIGINT PRIMARY KEY,
  base_id BIGINT NOT NULL REFERENCES items(id),
  user_id BIGINT NOT NULL REFERENCES users(id),
  from_ts TIMESTAMPTZ NOT NULL,
  end_ts TIMESTAMPTZ,
  CHECK (end_ts IS NULL OR end_ts >= from_ts)
);

CREATE INDEX IF NOT EXISTS idx_loan_history_base_id ON loan_history(base_id);
CREATE INDEX IF NOT EXISTS idx_loan_history_user_id ON loan_history(user_id);

-- 9) Optional helper views similar to LibraryManager arrays
CREATE OR REPLACE VIEW v_library_snapshot AS
SELECT
  c.id AS catalog_id,
  c.nazwa,
  c.opis,
  COUNT(DISTINCT i.id) AS items_count,
  COUNT(DISTINCT cp.id) AS copies_count,
  COUNT(DISTINCT l.id) AS active_loans_count
FROM catalogs c
LEFT JOIN items i ON i.catalog_id = c.id
LEFT JOIN copies cp ON cp.item_id = i.id
LEFT JOIN loans l ON l.copy_id = cp.id
GROUP BY c.id, c.nazwa, c.opis;

COMMIT;

-- Example seed and usage:
-- INSERT INTO catalogs (nazwa, opis) VALUES ('Biblioteka', 'Glowny katalog') RETURNING id;
-- INSERT INTO users (id, name, surname, email, status) VALUES (1, 'Jan', 'Nowak', 'jan@lib.local', 'admin');
-- INSERT INTO items (id, catalog_id, item_type, tytul) VALUES (1001, 1, 'book', 'Clean Code');
-- INSERT INTO books (item_id, autor, isbn) VALUES (1001, 'Robert C. Martin', '9780132350884');
-- INSERT INTO copies (id, item_id, status) VALUES (5001, 1001, 'available');
-- INSERT INTO loans (id, user_id, copy_id) VALUES (9001, 1, 5001);
-- INSERT INTO loan_history (id, base_id, user_id, from_ts) VALUES (10001, 1001, 1, NOW());
