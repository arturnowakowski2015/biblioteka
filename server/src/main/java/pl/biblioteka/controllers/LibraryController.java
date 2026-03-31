package pl.biblioteka.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import pl.biblioteka.dto.ItemRequest;
import pl.biblioteka.dto.LibraryResponse;
import pl.biblioteka.model.Auth;
import pl.biblioteka.model.Book;
import pl.biblioteka.model.Copy;
import pl.biblioteka.model.CopyStatus;
import pl.biblioteka.model.Item;
import pl.biblioteka.model.LibraryManager;
import pl.biblioteka.model.Loan;
 import pl.biblioteka.model.User;
import pl.biblioteka.model.UserRole;
import pl.biblioteka.model.Audiobook;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import org.springframework.web.bind.annotation.RequestMethod; 

@CrossOrigin(
	    origins = "http://localhost:5173", 
	    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
	    allowedHeaders = "*"
	)
@RestController
@RequestMapping("/library")
public class LibraryController {
 
	
	@GetMapping("/loans")     // Endpoint: /library/loans
    public List<LibraryResponse> getLibraryData() {
        LibraryManager m = LibraryManager.getInstance();
       List<LibraryResponse> loans = m.getLoansHistory().stream()
    		    .map(t -> {
    	 
    		    	
    		         String status = m.getAllOfCopies().values().stream()
    		        		    .filter(copy -> copy.getId() == t.getWhatHas().getId()) 
    		            .findFirst()
    		            .map(copy -> copy.getStatus().toString())  
    		            .orElse("UNKNOWN");  

    		       
    		        return new LibraryResponse(
    		            t.getId(),
    		             
    		            m.getKatalog(t.getWhatHas().getItem().getClass()).getAllItems().stream()
    		                .filter(it -> it.getId()== t.getId())
    		                .findFirst()
    		                .map(it -> it.getAuthor()
    		                )
    		                .orElse("nieznany autor"),
    		                
    		                m.getKatalog(t.getWhatHas().getItem().getClass()).getAllItems().stream()
    		                .filter(it -> it.getId()== t.getId())
    		                .findFirst()
    		                .map(it ->  it.getTytul()
    		                 )
    		                .orElse("nieznany tytul"),
    		                 
    		            status,
    		            t.getFrom().toString(), 
    		            t.getEnd() != null ? t.getEnd().toString() : "ACTIVE"
    		        );
    		    })
    		    .toList();
			    				   
   
        return loans;
    }

	
	long f =1004l;
 	@PostMapping("/save")
	public ResponseEntity<String> saveLoans(@RequestBody List<ItemRequest> incomingLoans) {
 		incomingLoans.forEach(t->System.out.println(">>>>>>>>>>>>>   "+t.toString()));
    try {
	        
	        LibraryManager m = LibraryManager.getInstance();
	        User admin = new User(1L, "Jan", "Nowak", "jan.nowak@biblioteka.pl", UserRole.ADMIN);
	        Auth auth = new Auth();
	        auth.login(admin);
	        m.addUser(admin);
	        
	        int i = 0;
	        long id=3;
	        for (ItemRequest loan : incomingLoans) {
	            i++;
	            System.out.println("Próba dodania nr " + i + ": " + loan.getTitle());
	            
	            try {
	                User user = auth.getCurrentUser();
	                if (user == null) {
 	                    break;  
	                }
	                
	                m.registerItemInCatalog(Book.class,new Book(f,loan.getTitle() ,loan.getAuthor() , loan.getIsbn()), user);
	                m.borrowGenericCopy(f++, admin.getId(), ++id);
	            } catch (Throwable t) {
	                System.out.println("KATASTROFA przy " + i + ": " + t.getMessage());
	                t.printStackTrace();  
	            }
	        }
	        										
	        
 	        return ResponseEntity.ok("Zapisano " +  " rekordów");
 	    } catch (Exception e) {
         return ResponseEntity.status(500).body("Błąd zapisu: " + e.getMessage());
 	    }
	}
 	
 	
  	@PutMapping("/items/{id}")  
    public  boolean updateItem(
        @PathVariable long id,            
        @RequestBody LibraryResponse updatedData  
    ) {
 
            LibraryManager m = LibraryManager.getInstance();
            
             boolean isUpdated = m.update(id, updatedData.getStatus());
			 return isUpdated;

       
    }
 	
 	
	
}