package biblioteka.model;

import java.util.Objects;

public class Book extends Item {
    private String autor;
    private String isbn;

    public Book() {
    }

    public Book(long id, String tytul, String autor, String isbn) {
        super(id, tytul);
        this.autor = autor;
        this.isbn = isbn;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", tytul='" + getTytul() + '\'' +
            ", autor='" + autor + '\'' +
            ", isbn='" + isbn + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Book book)) {
            return false;
        }
        return super.equals(object)
            && Objects.equals(autor, book.autor)
            && Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), autor, isbn);
    }
}