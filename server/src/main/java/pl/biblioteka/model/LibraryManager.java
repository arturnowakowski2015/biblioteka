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
 
    private final Map<Class<? extends Item>, GenericKatalog<? extends Item>> catalogs = new ConcurrentHashMap<>();
 
    private final Map<Long, GenericCopy<? extends Item>> copiesById = new ConcurrentHashMap<>();
 
    private final List<GenericLoan<? extends Item>> genericLoans = new ArrayList<>();
    private List<GenericLoan<? extends Item>> loanHistory = new ArrayList<>(); 
    private List<User> users = new ArrayList<>();  
    private List<Loan> loans;
    

    public static synchronized LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }
 
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
 
    public Optional<GenericCopy<? extends Item>> findGenericCopyById(long copyId) {
        return Optional.ofNullable(copiesById.get(copyId));
    }
    
    public Map<Long, GenericCopy<? extends Item>> getAllOfCopies(){
    	return this.copiesById;
    }
 
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
 
        return Optional.of(gLoan);
    }
 
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
 
    
    public List<GenericLoan<? extends Item>> getLoansHistory(){
    	return this.genericLoans;
    }
    
     
    
    
    public boolean updateCopyStatus(long copyId, CopyStatus newStatus) {
        synchronized (this) { 
            GenericCopy<?> copy = copiesById.get(copyId);
            if (copy == null) {
                return false;  
            }

            CopyStatus oldStatus = copy.getStatus();
            if (oldStatus == newStatus) {
                return true;  
            }

           
            copy.setStatus(newStatus);
 
            Item item = copy.getItem();
            if (item != null) {
                Class<? extends Item> clazz = item.getClass();
                GenericKatalog<?> katalog = (GenericKatalog<?>) catalogs.get(clazz);
                if (katalog != null) {
                    katalog.getCopy(copyId).ifPresent(c -> c.setStatus(newStatus));
                }
            } else { 
                for (GenericKatalog<? extends Item> k : catalogs.values()) {
                    k.getCopy(copyId).ifPresent(c -> c.setStatus(newStatus));
                }
            }
 
            Optional<GenericLoan<? extends Item>> loanOpt = genericLoans.stream()
                .filter(l -> l.getWhatHas() != null && l.getWhatHas().getId() == copyId)
                .findFirst();

            if (newStatus == CopyStatus.AVAILABLE) { 
                if (loanOpt.isPresent()) {
                    GenericLoan<?> loan = loanOpt.get();
                    loan.setEnd(LocalDateTime.now());
                    genericLoans.remove(loan);
 
                }
            } else if (newStatus == CopyStatus.BORROWED) { 
            }
 
            return true;
        }
    }

    
    
    public boolean update(long loanId, String newStatus) {
        return genericLoans.stream()
                .filter(loan -> loan.getId() == loanId)
                .findFirst()
                .map(loan -> { 
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
                .orElse(false);  
        
    }
    
    
    
    
    public boolean updateItem(long id, LibraryResponse ir) {
        Optional<GenericLoan<? extends Item>> idd = this.genericLoans.stream().filter(t -> t.getId() == id).findFirst();
         System.out.println(">   n   "+idd.get().getId()) ;        
         
        if (idd.isPresent()) {
            long ids = idd.get().getWhatHas().getId();        }
        return true;
    } 

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