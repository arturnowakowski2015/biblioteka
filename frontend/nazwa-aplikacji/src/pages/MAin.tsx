import { useState } from "react";
import { Katalog } from "../../../src/model/katalog.tsx";
import { Items } from "../../../src/model/Item.tsx";
import { User, type UserStatus } from "../../../src/model/User.tsx";
import type { NewBookData } from "../../../src/model/Auth";

const initialLibrary: Katalog = new Katalog([], [], "Biblioteka", "Opis biblioteki");

const Main = () => {
    const [user, setUser] = useState<User | null>(null);
    const [library, setLibrary] = useState<Katalog>(initialLibrary);
    const [bookDraft, setBookDraft] = useState<NewBookData>({
        tytul: "",
        autor: "",
        rok: new Date().getFullYear(),
        opis: "",
    });

    const isAdmin = user?.status === "admin";

    const login = (status: UserStatus) => {
        const loggedUser = new User(
            1,
            status === "admin" ? "Admin" : "Użytkownik",
            "",
            `${status}@example.com`,
            status,
        );

        setUser(loggedUser);
    };

    const logout = () => setUser(null);

    const handleAddBook = () => {
        if (!isAdmin) return;

        const nextId = library.Set.length ? Math.max(...library.Set.map((b: Items) => b.id)) + 1 : 1;
        const newBook = new Items(nextId, bookDraft.tytul, bookDraft.autor, bookDraft.rok, bookDraft.opis);

        setLibrary((prev: Katalog) =>
            new Katalog([...prev.Set, newBook], prev.Setofcopies, prev.nazwa, prev.opis, prev.user),
        );

        setBookDraft({ tytul: "", autor: "", rok: new Date().getFullYear(), opis: "" });
    };

    return (
        <div style={{ padding: 18, fontFamily: "system-ui, sans-serif" }}>
            <h1>Biblioteka</h1>

            <section style={{ marginBottom: 24 }}>
                <h2>Autoryzacja</h2>
                {!user ? (
                    <div>
                        <p>Wybierz rolę i zaloguj się:</p>
                        <button onClick={() => login("admin")}>Zaloguj się jako admin</button>{" "}
                        <button onClick={() => login("user")}>Zaloguj się jako użytkownik</button>
                    </div>
                ) : (
                    <div>
                        <p>
                            Jesteś zalogowany jako: <strong>{user.name}</strong> ({user.status})
                        </p>
                        <button onClick={logout}>Wyloguj</button>
                    </div>
                )}
            </section>

            <section style={{ marginBottom: 24 }}>
                <h2>Lista książek</h2>
                {library.Set.length === 0 ? (
                    <p>Brak książek w zbiorze.</p>
                ) : (
                    <ul>
                        {library.Set.map((item: Items) => (
                            <li key={item.id}>
                                <strong>{item.tytul}</strong> — {item.autor} ({item.rok})
                                {item.opis ? ` — ${item.opis}` : ""}
                            </li>
                        ))}
                    </ul>
                )}
            </section>

            {isAdmin && (
                <section style={{ marginBottom: 24 }}>
                    <h2>Dodaj nową książkę</h2>
                    <div style={{ display: "grid", gap: 8, maxWidth: 400 }}>
                        <label>
                            Tytuł
                            <input
                                value={bookDraft.tytul}
                                onChange={(e) => setBookDraft((prev) => ({ ...prev, tytul: e.target.value }))}
                            />
                        </label>
                        <label>
                            Autor
                            <input
                                value={bookDraft.autor}
                                onChange={(e) => setBookDraft((prev) => ({ ...prev, autor: e.target.value }))}
                            />
                        </label>
                        <label>
                            Rok
                            <input
                                type="number"
                                value={bookDraft.rok}
                                onChange={(e) => setBookDraft((prev) => ({ ...prev, rok: Number(e.target.value) }))}
                            />
                        </label>
                        <label>
                            Opis
                            <textarea
                                value={bookDraft.opis}
                                onChange={(e) => setBookDraft((prev) => ({ ...prev, opis: e.target.value }))}
                            />
                        </label>
                        <button onClick={handleAddBook} disabled={!bookDraft.tytul || !bookDraft.autor}>
                            Dodaj książkę
                        </button>
                    </div>
                </section>
            )}
        </div>
    );
};

export default Main;
