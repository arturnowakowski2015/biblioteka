package pl.biblioteka.model.fine;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Abstraction for persisting and querying Fine records.
 * Implementations may be in-memory or JPA-backed.
 */
public interface FineRepository {
	Fine save(Fine fine);
    Optional<Fine> findById(Long id);
    List<Fine> findByLoanId(Long loanId);
    List<Fine> findOutstandingByUserId(Long userId);

    /**
     * Check if a fine already exists for a loan covering the given period.
     * Useful for idempotency when assessing per-day fines.
     */
    boolean existsForLoanIdAndPeriod(Long loanId, LocalDate periodFrom, LocalDate periodTo);
}
