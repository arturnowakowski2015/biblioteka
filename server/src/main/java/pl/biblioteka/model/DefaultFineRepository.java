package pl.biblioteka.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pl.biblioteka.model.fine.Fine;
import pl.biblioteka.model.fine.FineRepository;

public class DefaultFineRepository implements FineRepository{
	private  List<Fine>  fines = new ArrayList<>();
	public DefaultFineRepository() {}
	public Fine save(Fine fine) {
		this.fines.add(fine);
		return fine;
	}
	
	public Optional<Fine> findById(Long id){
		return this.fines.stream().filter(t->t.getId()==id).findFirst();
	}
	
	public List<Fine> findByLoanId(Long loanId){
		return this.fines.stream().filter(t->t.getLoanId()==loanId).toList();
	}
	
	public List<Fine> findOutstandingByUserId(Long userId){
		return this.fines.stream().filter(t->t.getUserId()==userId).toList();
	}
	
	public boolean existsForLoanIdAndPeriod(Long loanId, LocalDate periodFrom, LocalDate periodTo) {
	    return this.fines.stream()
	        .anyMatch(f -> f.getLoanId().equals(loanId) && 
	                       !f.getAssessedAt().toLocalDate().isBefore(periodFrom) && 
	                       !f.getAssessedAt().toLocalDate().isAfter(periodTo));
	}

}
 