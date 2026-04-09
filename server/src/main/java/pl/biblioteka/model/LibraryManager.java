package pl.biblioteka.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
import pl.biblioteka.generic.Booking;

public class LibraryManager {
    private static LibraryManager instance;
 
    private final Map<Class<? extends Item>, GenericKatalog<? extends Item>> catalogs = new ConcurrentHashMap<>();
  
    private final Map<Long, GenericCopy<? extends Item>> copiesById = new ConcurrentHashMap<>();
  
    private final List<GenericLoan<? extends Item>> genericLoans = new ArrayList<>();
    private List<GenericLoan<? extends Item>> loanHistory = new ArrayList<>(); 
    private List<User> users = new ArrayList<>();  
     
     private final Booking<? extends Item> booking = new Booking<>();
    
    
    

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
  
    public boolean bookCopy(long copyId) {
        GenericCopy<? extends Item> copy = copiesById.get(copyId);
        if (copy == null) return false;  

        // Szukamy aktywnego wypożyczenia dla tej kopii
        Optional<GenericLoan<? extends Item>> loan = this.genericLoans.stream()
                .filter(t -> t.getWhatHas().getId() == copyId)
                .findFirst();
        if (loan.isPresent()) {
            // Jeśli jest wypożyczona, dodajemy rezerwację dla osoby, która ją aktualnie ma
            // Uwaga: copyReservation przyjmuje List<User>, więc używamy computeIfAbsent
            this.booking.addCopyToBooking(loan.get().getWhatHas(),loan.get().getWhoHas());
            return true;
        }

        return false;
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
 
    private int countActiveLoansForUser(long userId) {
        return (int) genericLoans.stream()
            .filter(l -> l.getWhoHas() != null && l.getWhoHas().getId() == userId && l.getEnd() == null)
            .count();
    }
    public Optional<GenericLoan<? extends Item>> borrowGenericCopy(long loanId, long userId, long copyId) {
        Optional<User> userOpt = users.stream().filter(user -> user.getId() == userId).findFirst();
        GenericCopy<? extends Item> copy = copiesById.get(copyId);

        if (userOpt.isEmpty() || copy == null) {
            return Optional.empty();
        }

        User user = userOpt.get();

        // synchronizacja per-user, żeby uniknąć wyścigów wypożyczeń dla tego samego użytkownika
        synchronized (user) {
            // policz aktywne wypożyczenia
            int active = countActiveLoansForUser(userId);
            int limit = user.getMaxActiveLoans(); // albo stała DEFAULT_MAX jeśli preferujesz globalny limit

            if (active >= limit) {
                // limit osiągnięty — odrzucamy wypożyczenie
                return Optional.empty();
            }

            if (copy.getStatus() != CopyStatus.AVAILABLE) {
                return Optional.empty();
            }

            copy.setStatus(CopyStatus.BORROWED);
            GenericLoan<? extends Item> gLoan = new GenericLoan<>(loanId, user, copy, LocalDateTime.now());
            genericLoans.add(gLoan);
            user.increaseMaxActiveLoans();
            // opcjonalnie: jeśli chcesz, utrzymuj też listę w User (jeśli ją dodasz)
            // user.addLoan(gLoan);

            return Optional.of(gLoan);
        }
    }

 
    public boolean returnGenericCopy(long copyId) {
        Optional<GenericLoan<? extends Item>> loanOpt = genericLoans.stream()
            .filter(loan -> loan.getWhatHas() != null && loan.getWhatHas().getId() == copyId)
            .findFirst();

        if (loanOpt.isEmpty()) {
            return false;
        }
//
        GenericLoan<? extends Item> loan = loanOpt.get();
        loan.getWhatHas().setStatus(CopyStatus.AVAILABLE);
        genericLoans.remove(loan);
        
         
        Optional<User> user = this.users.stream().filter(t->t.getId() == loan.getWhoHas().getId()).findFirst();
        user.ifPresent(t->t.decreaseMaxActiveLoans());
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

    public boolean renewBooking(long loanid) {
        Optional<GenericLoan<? extends Item>> loan = this.genericLoans.stream().filter(t -> t.getId() == loanid).findFirst();
        if(loan.isPresent()) {
        	loan.get().renew();
        	return true;
        }
        return false;
    }
    public Optional<List<GenericLoan<? extends Item>>> checkOverDues() {
        List<GenericLoan<? extends Item>> overdues = this.genericLoans.stream()
                .filter(t -> t.getOverDue()==true)
                .toList();   
        return overdues.isEmpty() ? Optional.empty() : Optional.of(overdues);
    }
 
     

 
 
	 
 
  
}