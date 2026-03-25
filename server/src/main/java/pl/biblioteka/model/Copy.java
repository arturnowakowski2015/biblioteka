package pl.biblioteka.model;

import java.util.Objects;

public class Copy {
    private long id;
    private long itemId;
    private CopyStatus status = CopyStatus.AVAILABLE;

    public Copy() {
    }

    public Copy(long id, long itemId) {
        this.id = id;
        this.itemId = itemId;
    }

    public Copy(long id, long itemId, CopyStatus status) {
        this.id = id;
        this.itemId = itemId;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public CopyStatus getStatus() {
        return status;
    }

    public void setStatus(CopyStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Copy{" +
            "id=" + id +
            ", itemId=" + itemId +
            ", status=" + status +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Copy copy)) {
            return false;
        }
        return id == copy.id && itemId == copy.itemId && status == copy.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemId, status);
    }
}