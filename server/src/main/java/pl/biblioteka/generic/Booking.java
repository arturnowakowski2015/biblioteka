package pl.biblioteka.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import pl.biblioteka.model.Item;
import pl.biblioteka.model.User;

public class Booking<T extends Item> {
	private Map<GenericCopy<? extends Item>, List<User>> bookingCopy = new ConcurrentHashMap<>();
	
	public boolean addCopyToBooking(GenericCopy<? extends Item> copy, User user) {
	    Optional<GenericCopy<? extends Item>> existingCopy = bookingCopy.keySet().stream()
	            .filter(t -> t.getId() == copy.getId())
	            .findFirst();

	    if (existingCopy.isPresent()) {
 	        bookingCopy.get(existingCopy.get()).add(user);
	        return false;  
	    } else {
 	        List<User> newUserList = new ArrayList<>();
	        newUserList.add(user);
	        bookingCopy.put(copy, newUserList);
	        return true; 
	    }
	}
	
	public boolean removeBookedCopyByUser(GenericCopy<T> copy,User user) {
	    return  bookingCopy.keySet().stream()
	            .filter(t -> t.getId() == copy.getId())
	            .findFirst()
	            .map(key->{ 
	            	 bookingCopy.get(key).removeIf(t->t.getId()==user.getId());
	            	 if(bookingCopy.get(key).isEmpty())
	            		 bookingCopy.remove(key);
	            	 return true;
	            		})
	            .orElse(false);
	}
	
//	- Opis: użytkownik może przedłużyć termin wypożyczenia (jednokrotnie lub wielokrotnie, zależnie od reguł).
 
	
	
}




 