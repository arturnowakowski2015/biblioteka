package pl.biblioteka.model.fine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Business API for assessing and managing fines. Implementations contain the
 * logic for computing fines and persisting them (via FineRepository).
 */
public interface FineService {
    /**
     * Compute and persist any fines that should be assessed for the given loan at the provided time.
     * Returns an Optional containing the created FineEntity (or empty if nothing was created).
     */
    Optional<Fine> assessFineForLoan(Long loanId, LocalDateTime now);

    List<Fine> getOutstandingFinesForUser(Long userId);

    /**
     * Mark a fine (or portion) as paid. Implementation decides semantics (partial payments or full).
     */
    void markFinePaid(Long fineId, long amountCents, LocalDateTime paidAt);

    List<Fine> getFinesForLoan(Long loanId);

    long getOutstandingTotalForUser(Long userId);
}
