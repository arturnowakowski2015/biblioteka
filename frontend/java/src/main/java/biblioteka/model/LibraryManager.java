package biblioteka.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LibraryManager {
    private static LibraryManager instance;

    private Katalog katalog;
    private List<User> users;
    private List<Loans> loans;
    private List<LoansHistory> historyLoans;

    private LibraryManager() {
        this.katalog = new Katalog(new ArrayList<>(), new ArrayList<>(), "nazwa", "pusty katalog");
        this.users = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.historyLoans = new ArrayList<>();
    }

    public static synchronized LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    public Katalog getKatalog() {
        return katalog;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Loans> getLoans() {
        return loans;
    }

    public List<LoansHistory> getHistoryLoans() {
        return historyLoans;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addItem(Item item, User user) {
        katalog.registerItem(item, user);
    }

    public Optional<Loans> borrowCopy(long loanId, long userId, long copyId) {
        Optional<User> userOpt = users.stream().filter(user -> user.getId() == userId).findFirst();
        Optional<Copy> copyOpt = katalog.getAllOfCopies().stream().filter(copy -> copy.getId() == copyId).findFirst();

        if (userOpt.isEmpty() || copyOpt.isEmpty()) {
            return Optional.empty();
        }

        Copy copy = copyOpt.get();
        if (copy.getStatus() != CopyStatus.AVAILABLE) {
            return Optional.empty();
        }

        copy.setStatus(CopyStatus.BORROWED);
        Loans loan = new Loans(loanId, userOpt.get(), copy);
        loans.add(loan);
        historyLoans.add(new LoansHistory(loanId, copy.getItemId(), userId, LocalDateTime.now()));

        return Optional.of(loan);
    }

    public boolean returnCopy(long copyId) {
        Optional<Loans> loanOpt = loans.stream()
            .filter(loan -> loan.getWhatHas() != null && loan.getWhatHas().getId() == copyId)
            .findFirst();

        if (loanOpt.isEmpty()) {
            return false;
        }

        Loans loan = loanOpt.get();
        loan.getWhatHas().setStatus(CopyStatus.AVAILABLE);
        loans.remove(loan);

        historyLoans.stream()
            .filter(history -> history.getBaseId() == loan.getWhatHas().getItemId()
                && history.getUserId() == loan.getWhoHas().getId()
                && history.getEnd() == null)
            .findFirst()
            .ifPresent(history -> history.setEnd(LocalDateTime.now()));

        return true;
    }

    @Override
    public String toString() {
        return "LibraryManager{" +
            "katalog=" + katalog +
            ", users=" + users +
            ", loans=" + loans +
            ", historyLoans=" + historyLoans +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof LibraryManager that)) {
            return false;
        }
        return Objects.equals(katalog, that.katalog)
            && Objects.equals(users, that.users)
            && Objects.equals(loans, that.loans)
            && Objects.equals(historyLoans, that.historyLoans);
    }

    @Override
    public int hashCode() {
        return Objects.hash(katalog, users, loans, historyLoans);
    }
}