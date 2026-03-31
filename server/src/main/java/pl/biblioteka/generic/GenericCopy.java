package pl.biblioteka.generic;

import pl.biblioteka.model.Item;
import pl.biblioteka.model.CopyStatus;

import java.util.Objects;
 
public class GenericCopy<T extends Item> {
    private long id;
    private long itemId;
    private T item;
    private CopyStatus status;

    public GenericCopy(long id, long itemId) {
        this.id = id;
        this.itemId = itemId;
        this.status = CopyStatus.AVAILABLE;
    }

    public GenericCopy(long id, T item) {
        this(id, item != null ? item.getId() : 0L);
        this.item = item;
    }

    public long getId() { return id; }
    public long getItemId() { return itemId; }
    public T getItem() { return item; }
    public void setItem(T item) {
        this.item = item;
        if (item != null) this.itemId = item.getId();
    }

    public CopyStatus getStatus() { return status; }
    public void setStatus(CopyStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "GenericCopy{" + "id=" + id + ", itemId=" + itemId + ", status=" + status + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericCopy)) return false;
        GenericCopy<?> copy = (GenericCopy<?>) o;
        return id == copy.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
