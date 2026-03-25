package pl.biblioteka.model;

import java.time.LocalDate;
import java.util.Objects;

public class NewsPaper extends Item {
    private int numerWydania;
    private LocalDate dataPublikacji;

    public NewsPaper() {
    }

    public NewsPaper(long id, String tytul, int numerWydania, LocalDate dataPublikacji) {
        super(id, tytul);
        this.numerWydania = numerWydania;
        this.dataPublikacji = dataPublikacji;
    }

    public int getNumerWydania() {
        return numerWydania;
    }

    public void setNumerWydania(int numerWydania) {
        this.numerWydania = numerWydania;
    }

    public LocalDate getDataPublikacji() {
        return dataPublikacji;
    }

    public void setDataPublikacji(LocalDate dataPublikacji) {
        this.dataPublikacji = dataPublikacji;
    }

    @Override
    public String toString() {
        return "NewsPaper{" +
            "id=" + getId() +
            ", tytul='" + getTytul() + '\'' +
            ", numerWydania=" + numerWydania +
            ", dataPublikacji=" + dataPublikacji +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof NewsPaper newsPaper)) {
            return false;
        }
        return super.equals(object)
            && numerWydania == newsPaper.numerWydania
            && Objects.equals(dataPublikacji, newsPaper.dataPublikacji);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), numerWydania, dataPublikacji);
    }
}