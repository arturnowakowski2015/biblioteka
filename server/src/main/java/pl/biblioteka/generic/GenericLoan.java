package pl.biblioteka.generic;

 import pl.biblioteka.model.User;

import java.time.LocalDateTime;

public class GenericLoan<T extends pl.biblioteka.model.Item> {
 
    private final long id;
    private boolean overdue; 
    private LocalDateTime expectedReturnTime;
    private final User whoHas;
    private final GenericCopy<T> whatHas;
    private LocalDateTime from;
    public void setFrom(LocalDateTime from) {
		this.from = from;
	}
	private LocalDateTime end;
//
    public GenericLoan(long id, User whoHas, GenericCopy<T> whatHas, LocalDateTime from) {
    	this.overdue=false; 
    	this.expectedReturnTime=from.plusDays(30); 
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
    private void setExpectedReturnTime() {
    	this.expectedReturnTime.plusDays(30);
    }
  
    public LocalDateTime getExpectedReturnTime() {
    	return this.expectedReturnTime;
    }
 
    public boolean getOverDue() {
    	return this.overdue;
    }
    public void setOverDue() {
    	this.overdue=!this.overdue;
    }
 
 }
