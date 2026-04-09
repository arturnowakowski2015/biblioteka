package pl.biblioteka.model.fine;

import java.time.LocalDate;
import java.time.LocalDateTime;

import pl.biblioteka.generic.GenericLoan;

public class FineAssessorImplementation implements FineAssessor {
	public long computeFineAmount(GenericLoan<?> loan, LocalDateTime now, FinePolicy policy) {
		if(now.plusDays(policy.getGraceDays())
	}
}


//private int graceDays; //when not to count
//private long centsPerDay;
//private long capCents;//max for fine
//private boolean perDay;	                       
!f.getAssessedAt().toLocalDate().isBefore(periodFrom) && 
!f.getAssessedAt().toLocalDate().isAfter(periodTo));

//Fine
//private Long id;
//
//private Long loanId;
//
//private Long userId;
//
///** Amount in cents (to avoid floating point) */
//private long amountCents;
//
//private LocalDateTime assessedAt;
//
///** Optional period this fine covers (useful for per-day fines) */
//private LocalDate periodFrom;
//
//private LocalDate periodTo;
//
//private boolean paid;
//
//private LocalDateTime paidAt;
//
//private String reason;