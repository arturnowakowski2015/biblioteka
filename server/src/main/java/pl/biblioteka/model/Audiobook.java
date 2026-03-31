package pl.biblioteka.model;

import java.util.Objects;

public class Audiobook extends Item {
    private int timeLength;
    private String lector;

    public Audiobook() {
    }

    public Audiobook(long id, String tytul, int timeLength, String lector) {
        super(id, tytul);
        this.timeLength = timeLength;
        this.lector = lector;
    }
 
    public int getTimeLength() {
        return timeLength;
    }
//
    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public String getLector() {
        return lector;
    }

    public void setLector(String lector) {
        this.lector = lector;
    }

    @Override
    public String getAuthor() {
        return lector;
    }

    @Override
    public void setAuthor(String author) {
        this.lector = author;
    }

    @Override
    public String toString() {
        return "Audiobook{" +
            "id=" + getId() +
            ", tytul='" + getTytul() + '\'' +
            ", timeLength=" + timeLength +
            ", lector='" + lector + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Audiobook audiobook)) {
            return false;
        }
        return super.equals(object)
            && timeLength == audiobook.timeLength
            && Objects.equals(lector, audiobook.lector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timeLength, lector);
    }
}