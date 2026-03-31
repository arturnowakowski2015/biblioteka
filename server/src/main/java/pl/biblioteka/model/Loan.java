package pl.biblioteka.model;

import java.util.Objects;

public class Loan {
    private long id;
    private User whoHas;
    private Copy whatHas;

    public Loan() {
    }

    public Loan(long id, User whoHas, Copy whatHas) {
        this.id = id;
        this.whoHas = whoHas;
        this.whatHas = whatHas;
    } 

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getWhoHas() {
        return whoHas;
    }

    public void setWhoHas(User whoHas) {
        this.whoHas = whoHas;
    }

    public Copy getWhatHas() {
        return whatHas;
    }

    public void setWhatHas(Copy whatHas) {
        this.whatHas = whatHas;
    }

    @Override
    public String toString() {
        return "Loans{" +
            "id=" + id +
            ", whoHas=" + whoHas +
            ", whatHas=" + whatHas +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Loan loans)) {
            return false;
        }
        return id == loans.id
            && Objects.equals(whoHas, loans.whoHas)
            && Objects.equals(whatHas, loans.whatHas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, whoHas, whatHas);
    }
}