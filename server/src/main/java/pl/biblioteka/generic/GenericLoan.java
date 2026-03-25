package pl.biblioteka.generic;

import pl.biblioteka.model.User;

import java.time.LocalDateTime;

public class GenericLoan<T extends pl.biblioteka.model.Item> {
    private final long id;
    private final User whoHas;
    private final GenericCopy<T> whatHas;
    private LocalDateTime from;
    public void setFrom(LocalDateTime from) {
		this.from = from;
	}
	private LocalDateTime end;

    public GenericLoan(long id, User whoHas, GenericCopy<T> whatHas, LocalDateTime from) {
        this.id = id;
        this.whoHas = whoHas;
        this.whatHas = whatHas;
        this.from = from;
    }

    public long getId() { return id; }
    public User getWhoHas() { return whoHas; }
    public GenericCopy<T> getWhatHas() { return whatHas; }
    public LocalDateTime getFrom() { return from; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }
}
