package biblioteka.model;

import java.util.Objects;

public abstract class Item {
    private long id;
    private String tytul;

    protected Item() {
    }

    protected Item(long id, String tytul) {
        this.id = id;
        this.tytul = tytul;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "id=" + id +
            ", tytul='" + tytul + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Item item = (Item) object;
        return id == item.id && Objects.equals(tytul, item.tytul);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tytul, getClass());
    }
}