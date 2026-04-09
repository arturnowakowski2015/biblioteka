package pl.biblioteka.model.fine;

 import java.time.LocalDate;
import java.time.LocalDateTime;

 
public class Fine {
    private Long id;

    private Long loanId;

    private Long userId;

    /** Amount in cents (to avoid floating point) */
    private long amountCents;

    private LocalDateTime assessedAt;

    /** Optional period this fine covers (useful for per-day fines) */
    private LocalDate periodFrom;

    private LocalDate periodTo;

    private boolean paid;

    private LocalDateTime paidAt;

    private String reason;

    public Fine() {}
     public Fine(Long loanId,Long userId,long amountCents,LocalDateTime assessedAt,LocalDate periodFrom,LocalDate periodTo) {
    	this.id=this.getId()+1;
    	this.loanId=loanId;
    	this.userId=userId;
    	this.amountCents=amountCents;
    	this.assessedAt=assessedAt;
    	this.periodFrom=periodFrom;
    	this.periodTo=periodTo;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public long getAmountCents() { return amountCents; }
    public void setAmountCents(long amountCents) { this.amountCents = amountCents; }

    public LocalDateTime getAssessedAt() { return assessedAt; }
    public void setAssessedAt(LocalDateTime assessedAt) { this.assessedAt = assessedAt; }

    public LocalDate getPeriodFrom() { return periodFrom; }
    public void setPeriodFrom(LocalDate periodFrom) { this.periodFrom = periodFrom; }

    public LocalDate getPeriodTo() { return periodTo; }
    public void setPeriodTo(LocalDate periodTo) { this.periodTo = periodTo; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
