package pl.biblioteka.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class LoansHistory {
    private long id;
    private long baseId;
    private long userId;
    private LocalDateTime from;
    private LocalDateTime end;

    public LoansHistory() {
    }
 
    public LoansHistory(long id, long baseId, long userId, LocalDateTime from) {
        this.id = id;
        this.baseId = baseId;
        this.userId = userId;
        this.from = from;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBaseId() {
        return baseId;
    }

    public void setBaseId(long baseId) {
        this.baseId = baseId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "LoansHistory{" +
            "id=" + id +
            ", baseId=" + baseId +
            ", userId=" + userId +
            ", from=" + from +
            ", end=" + end +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof LoansHistory that)) {
            return false;
        }
        return id == that.id
            && baseId == that.baseId
            && userId == that.userId
            && Objects.equals(from, that.from)
            && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, baseId, userId, from, end);
    }
}