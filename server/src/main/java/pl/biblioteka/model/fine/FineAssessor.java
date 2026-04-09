package pl.biblioteka.model.fine;

import pl.biblioteka.generic.GenericLoan;
import java.time.LocalDateTime;

/**
 * Strategy interface for computing how much fine should be assessed for a loan
 * at a given point in time according to a provided policy.
 *
 * Method should be pure (no persistence) — persistence is handled by FineService.
 */
public interface FineAssessor {
    /**
     * Compute the fine amount (in cents) for the given loan and policy.
     * Return calculated amount (>=0).
     */
    long computeFineAmount(GenericLoan<?> loan, LocalDateTime now, FinePolicy policy);
}
