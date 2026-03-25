package biblioteka.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Katalog {
    private List<Item> set;
    private List<Copy> setOfCopies;
    private String nazwa;
    private String opis;

    public Katalog() {
        this(new ArrayList<>(), new ArrayList<>(), "", "");
    }

    public Katalog(List<Item> set, List<Copy> setOfCopies, String nazwa, String opis) {
        this.set = set;
        this.setOfCopies = setOfCopies;
        this.nazwa = nazwa;
        this.opis = opis;
    }

    public void registerItem(Item book, User user) {
        if (user == null || !user.isAdmin()) {
            return;
        }

        boolean exists = set.stream()
            .anyMatch(item -> item.getTytul() != null && item.getTytul().equals(book.getTytul()));

        if (!exists) {
            set.add(book);
            long nextCopyId = setOfCopies.stream()
                .mapToLong(Copy::getId)
                .max()
                .orElse(0L) + 1L;
            setOfCopies.add(new Copy(nextCopyId, book.getId()));
        }
    }

    public Optional<Item> getItem(long id) {
        return set.stream().filter(item -> item.getId() == id).findFirst();
    }

    public List<Item> getAllItems() {
        return set;
    }

    public List<Copy> getAllOfCopies() {
        return setOfCopies;
    }

    public List<Item> getSet() {
        return set;
    }

    public void setSet(List<Item> set) {
        this.set = set;
    }

    public List<Copy> getSetOfCopies() {
        return setOfCopies;
    }

    public void setSetOfCopies(List<Copy> setOfCopies) {
        this.setOfCopies = setOfCopies;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public String toString() {
        return "Katalog{" +
            "set=" + set +
            ", setOfCopies=" + setOfCopies +
            ", nazwa='" + nazwa + '\'' +
            ", opis='" + opis + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Katalog katalog)) {
            return false;
        }
        return Objects.equals(set, katalog.set)
            && Objects.equals(setOfCopies, katalog.setOfCopies)
            && Objects.equals(nazwa, katalog.nazwa)
            && Objects.equals(opis, katalog.opis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(set, setOfCopies, nazwa, opis);
    }
}