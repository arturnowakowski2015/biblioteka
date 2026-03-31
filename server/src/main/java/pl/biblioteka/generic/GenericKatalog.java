package pl.biblioteka.generic;

import pl.biblioteka.model.Item;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
 
public class GenericKatalog<T extends Item> {
    private final List<T> items = new ArrayList<>();
    private final List<GenericCopy<T>> copies = new ArrayList<>();
     private String nazwa = "";
    private String opis = "";

    public GenericKatalog() { }
    public GenericKatalog(String nazwa, String opis) { this.nazwa = nazwa; this.opis = opis; }

    public void addItem(T item) {
        if (item == null) return;
        if (items.stream().noneMatch(i -> i.getId() == item.getId())) {
            items.add(item);
        }
    }

    public void addCopy(GenericCopy<T> copy) {
        if (copy == null) return;
        copies.add(copy);
    }

    public Optional<T> getItem(long id) {
        return items.stream().filter(i -> i.getId() == id).findFirst();
    }

    public Optional<GenericCopy<T>> getCopy(long id) {
        return copies.stream().filter(c -> c.getId() == id).findFirst();
    }

    public List<T> findByPredicate(Predicate<? super T> pred) {
        return items.stream().filter(pred).collect(Collectors.toList());
    }

    public List<T> findByTitle(String title) {
        if (title == null) return Collections.emptyList();
        return items.stream()
            .filter(i -> title.equalsIgnoreCase(i.getTytul()))
            .collect(Collectors.toList());
    }

    public <S extends T> Optional<S> getItemAs(long id, Class<S> clazz) {
        return items.stream()
            .filter(clazz::isInstance)
            .map(clazz::cast)
            .filter(i -> i.getId() == id)
            .findFirst();
    }

    public <R> List<R> mapItems(Function<? super T, ? extends R> mapper) {
        return items.stream().map(mapper).collect(Collectors.toList());
    }

    public List<GenericCopy<T>> getAllCopies() { return Collections.unmodifiableList(copies); }
    public List<T> getAllItems() { return Collections.unmodifiableList(items); }

    public void resolveReferencesById() {
        Map<Long, T> map = items.stream().collect(Collectors.toMap(Item::getId, i -> i));
        copies.forEach(c -> {
            if (c.getItem() == null) {
                T ref = map.get(c.getItemId());
                if (ref != null) c.setItem(ref);
            }
        });
    }
}
