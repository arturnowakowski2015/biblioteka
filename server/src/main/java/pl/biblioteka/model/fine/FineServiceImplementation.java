package pl.biblioteka.model.fine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import pl.biblioteka.generic.GenericLoan;
import pl.biblioteka.model.fine.Fine;
public class FineServiceImplementation {
    private final FineAssessor fineAssessor;  
    private final FineRepository fineRepository;  

    // MOMENT WSTRZYKIWANIA: Konstruktor
    public FineServiceImplementation(FineAssessor fineAssessor, FineRepository fineRepository) {
        this.fineAssessor = fineAssessor;
        this.fineRepository = fineRepository;
    }
    
	public Optional<Fine> assessFineForLoan(Long loanId, LocalDateTime now, GenericLoan<?> loan){


		 List<Fine> finse =  this.fineRepository.findByLoanId(loanId);
		 
		 long daysBetween =  ChronoUnit.DAYS.between(now, loan.getExpectedReturnTime());
		 long totalAmount = fine.stream().mapToLong(t->t.getAmountCents()).sum();
		 if(daysBetween<0) {
			 FinePolicy fpol= new FinePolicy(now, totalAmount/yetTo);
			 long amount = this.fineAssessor.computeFineAmount(loan,  now, fpol);

		
			if (amount > 0) { 
			    LocalDate periodFrom = fine.getFrom() != null ? fine.getFrom().toLocalDate() : null;
			    LocalDate periodTo = fine.getEnd() != null ? fine.getEnd().toLocalDate() : null;
	
			    Fine fine1 = new Fine(
			        loanId, 
			        (Long) fine.getWhoHas().getId(), 
			        amount, 
			        now, 
			        periodFrom, 
			        periodTo
			    );
			    
			    // ZAPIS I ZWRÓCENIE WYNIKU
			    return Optional.of(this.fineRepository.save(fine1));
			}
		 }
		    return Optional.empty();
	}
}

//public interface FineService {
//    /**
//     * Compute and persist any fines that should be assessed for the given loan at the provided time.
//     * Returns an Optional containing the created FineEntity (or empty if nothing was created).
//     */
//    Optional<Fine> assessFineForLoan(Long loanId, LocalDateTime now);
//
//    List<Fine> getOutstandingFinesForUser(Long userId);
//
//    /**
//     * Mark a fine (or portion) as paid. Implementation decides semantics (partial payments or full).
//     */
//    void markFinePaid(Long fineId, long amountCents, LocalDateTime paidAt);
//
//    List<Fine> getFinesForLoan(Long loanId);
//
//    long getOutstandingTotalForUser(Long userId);