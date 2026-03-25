package pl.biblioteka.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pl.biblioteka.dto.ItemRequest;
import pl.biblioteka.dto.LibraryResponse;
import pl.biblioteka.generic.GenericKatalog;
import pl.biblioteka.generic.GenericCopy;
import pl.biblioteka.generic.GenericLoan;

public class LibraryManager {
    private static LibraryManager instance;

    // New generic catalogs map (option C)
    private final Map<Class<? extends Item>, GenericKatalog<? extends Item>> catalogs = new ConcurrentHashMap<>();
    // Global index of copies by id for fast lookup
    private final Map<Long, GenericCopy<? extends Item>> copiesById = new ConcurrentHashMap<>();
    // Generic loans list
    private final List<GenericLoan<? extends Item>> genericLoans = new ArrayList<>();
    private List<GenericLoan<? extends Item>> loanHistory = new ArrayList<>();
    // --- old non-generic fields (kept for compatibility, usage commented out) ---
    private List<User> users = new ArrayList<>(); // Inicjalizacja!
    private List<Loan> loans;
    

    public static synchronized LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    // Generic accessor: returns a GenericKatalog for the requested subclass of Item (lazy init)
    @SuppressWarnings("unchecked")
    public <T extends Item> GenericKatalog<T> getKatalog(Class<T> clazz) {
        return (GenericKatalog<T>) catalogs.computeIfAbsent(clazz, c -> new GenericKatalog<>(c.getSimpleName(), ""));
    }
 

    public List<User> getUsers() {
        return users;
    }
 

    public void addUser(User user) {
        users.add(user);
    }

    // New: register item into appropriate generic catalog and create a GenericCopy
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends Item> GenericCopy<T> registerItemInCatalog(Class<T> clazz,T item, User user) {
        if (item == null) return null;
        GenericKatalog<T> k = getKatalog(clazz);
        k.addItem(item);
        long nextId = copiesById.keySet().stream().mapToLong(Long::longValue).max().orElse(0L) + 1L;
        GenericCopy<T> copy = new GenericCopy<>(nextId, item);
        k.addCopy(copy);
        copiesById.put(nextId, copy);
        return copy;
    }

    // New: find generic copy quickly
    public Optional<GenericCopy<? extends Item>> findGenericCopyById(long copyId) {
        return Optional.ofNullable(copiesById.get(copyId));
    }
    
    public Map<Long, GenericCopy<? extends Item>> getAllOfCopies(){
    	return this.copiesById;
    }

    // New: borrow generic copy (returns a GenericLoan)
    public Optional<GenericLoan<? extends Item>> borrowGenericCopy(long loanId, long userId, long copyId) {
        Optional<User> userOpt = users.stream().filter(user -> user.getId() == userId).findFirst();
        GenericCopy<? extends Item> copy = copiesById.get(copyId);

        if (userOpt.isEmpty() || copy == null) {
            return Optional.empty();
        }

        if (copy.getStatus() != CopyStatus.AVAILABLE) {
            return Optional.empty();
        }

        copy.setStatus(CopyStatus.BORROWED);
        GenericLoan<? extends Item> gLoan = new GenericLoan<>(loanId, userOpt.get(), copy, LocalDateTime.now());
        genericLoans.add(gLoan);
        // maintain legacy history for compatibility
 
        return Optional.of(gLoan);
    }

    // New: return generic copy
    public boolean returnGenericCopy(long copyId) {
        Optional<GenericLoan<? extends Item>> loanOpt = genericLoans.stream()
            .filter(loan -> loan.getWhatHas() != null && loan.getWhatHas().getId() == copyId)
            .findFirst();

        if (loanOpt.isEmpty()) {
            return false;
        }

        GenericLoan<? extends Item> loan = loanOpt.get();
        loan.getWhatHas().setStatus(CopyStatus.AVAILABLE);
        genericLoans.remove(loan);
 

        return true;
    }
 
    // --- Legacy API: old methods are preserved here but their implementations are commented out
    // This keeps source history and allows rollback if needed.

    /*
    public void addItem(Item item, User user) {
        katalog.registerItem(item, user);
    }
    */

    /*
    public Optional<Loan> borrowCopy(long loanId, long userId, long copyId) {
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
        Loan loan = new Loan(loanId, userOpt.get(), copy);
        loans.add(loan);
        historyLoans.add(new LoansHistory(loanId, copy.getId(), userId, LocalDateTime.now()));

        return Optional.of(loan);
    }
    */

    /*
    public boolean returnCopy(long copyId) {
        Optional<Loan> loanOpt = loans.stream()
            .filter(loan -> loan.getWhatHas() != null && loan.getWhatHas().getId() == copyId)
            .findFirst();

        if (loanOpt.isEmpty()) {
            return false;
        }

        Loan loan = loanOpt.get();
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
    */
    public List<GenericLoan<? extends Item>> getLoansHistory(){
    	return this.genericLoans;
    }
    
    
 // nie ma takiej kopii
 // nic do zrobienia
 // 1) Zaktualizuj globalną kopię
    // 2) Zaktualizuj kopię w generycznym katalogu (jeśli można go znaleźć)
 // Przykład (pseudokod dostosowany do Twoich pól w LibraryManager)

    
    
    
    public boolean updateCopyStatus(long copyId, CopyStatus newStatus) {
        synchronized (this) { // prosty lock; możesz użyć bardziej fine-grained locków
            GenericCopy<?> copy = copiesById.get(copyId);
            if (copy == null) {
                return false; // nie ma takiej kopii
            }

            CopyStatus oldStatus = copy.getStatus();
            if (oldStatus == newStatus) {
                return true; // nic do zrobienia
            }

            // 1) Zaktualizuj globalną kopię
            copy.setStatus(newStatus);

            // 2) Zaktualizuj kopię w generycznym katalogu (jeśli można go znaleźć)
            Item item = copy.getItem();
            if (item != null) {
                Class<? extends Item> clazz = item.getClass();
                GenericKatalog<?> katalog = (GenericKatalog<?>) catalogs.get(clazz);
                if (katalog != null) {
                    katalog.getCopy(copyId).ifPresent(c -> c.setStatus(newStatus));
                }
            } else {
                // jeśli brak referencji item, przeszukaj katalogi po copyId (opsjonalne, kosztowne)
                for (GenericKatalog<? extends Item> k : catalogs.values()) {
                    k.getCopy(copyId).ifPresent(c -> c.setStatus(newStatus));
                }
            }

            // 3) Zaktualizuj aktywne pożyczki i historię:
            Optional<GenericLoan<? extends Item>> loanOpt = genericLoans.stream()
                .filter(l -> l.getWhatHas() != null && l.getWhatHas().getId() == copyId)
                .findFirst();

            if (newStatus == CopyStatus.AVAILABLE) {
                // zwrot: zakończ pożyczkę i uzupełnij historię
                if (loanOpt.isPresent()) {
                    GenericLoan<?> loan = loanOpt.get();
                    loan.setEnd(LocalDateTime.now());
                    genericLoans.remove(loan);
 
                }
            } else if (newStatus == CopyStatus.BORROWED) {
                // wypożyczenie: tutaj zwykle tworzymy loan w innej metodzie (wymaga usera)
                // jeśli istnieje pożyczka - sprawdź spójność; inaczej pozostaw bez tworzenia loan
            }
 
            return true;
        }
    }

    
    
    public boolean update(long loanId, String newStatus) {
        return genericLoans.stream()
                .filter(loan -> loan.getId() == loanId)
                .findFirst()
                .map(loan -> {
                    // Tutaj logika aktualizacji, np.:
                    // loan.setStatus(newStatus); 
                    long copyId = loan.getWhatHas().getId();
                    GenericCopy<? extends Item> copy = copiesById.get(copyId);
                    copy.setStatus(CopyStatus.valueOf(newStatus));
                   // 
                    GenericKatalog<?> cat = this.catalogs.get(loan.getWhatHas().getItem().getClass());
                    cat.getCopy(copyId).ifPresent(t->t.setStatus(CopyStatus.valueOf(newStatus)));
                    if(CopyStatus.valueOf(newStatus)==CopyStatus.AVAILABLE) {
                    	loan.setEnd(LocalDateTime.now());
                      	loan.setEnd(null);
                    	genericLoans.remove(loan);	
                    }
                    if(CopyStatus.valueOf(newStatus)==CopyStatus.BORROWED) {
                    	loan.setFrom(LocalDateTime.now());
  
                    	loanHistory.add(loan);
                    }
                    return true;
                })
                .orElse(false); // Zwraca false, jeśli nie znaleziono pożyczki
        
    }
    
    
    
    
    public boolean updateItem(long id, LibraryResponse ir) {
        Optional<GenericLoan<? extends Item>> idd = this.genericLoans.stream().filter(t -> t.getId() == id).findFirst();
         System.out.println(">   n   "+idd.get().getId()) ;        
         
        if (idd.isPresent()) {
            long ids = idd.get().getWhatHas().getId();        }
        return true;
    }
    // Existing updateItem kept as-is (works with old katalog). You can migrate it to generic variant if needed.
//    public boolean updateItem(long id,LibraryResponse ir) {
//        System.out.println("/////  " + katalog.getAllItems() + "\n" + this.historyLoans.toString() + "///" + id);
//        Optional<LoansHistory> idd = this.getHistoryLoans().stream().filter(t -> t.getId() == id).findFirst();
//        if (idd.isPresent()) {
//            long ids = idd.get().getBaseId();
//            Optional<Copy> res = katalog.getAllOfCopies().stream().filter(t -> t.getId() == ids).findFirst();
//            if (res.isPresent()) {
//                long it = res.get().getItemId();
//                Optional<Item> itemOpt = katalog.getItem(it);
//                if (itemOpt.isPresent()) {
//                    Item item = itemOpt.get();
//                    // Update title if provided
//                    if (ir.getTytul() != null && !ir.getTytul().isBlank()) {
//                        item.setTytul(ir.getTytul());
//                    }
//                    // Update author generically
//                    if (ir.getAuthor() != null) {
//                        item.setAuthor(ir.getAuthor());
//                    }
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

	@Override
	public int hashCode() {
		return Objects.hash(catalogs, copiesById, genericLoans, loanHistory, loans, users);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LibraryManager other = (LibraryManager) obj;
		return Objects.equals(catalogs, other.catalogs) && Objects.equals(copiesById, other.copiesById)
				&& Objects.equals(genericLoans, other.genericLoans) && Objects.equals(loanHistory, other.loanHistory)
				&& Objects.equals(loans, other.loans) && Objects.equals(users, other.users);
	}

	@Override
	public String toString() {
		return "LibraryManager [catalogs=" + catalogs + ", copiesById=" + copiesById + ", genericLoans=" + genericLoans
				+ ", loanHistory=" + loanHistory + ", users=" + users + ", loans=" + loans + "]";
	}

	 
 
  
}