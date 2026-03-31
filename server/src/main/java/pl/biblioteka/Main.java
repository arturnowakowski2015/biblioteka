package pl.biblioteka;

import pl.biblioteka.generic.GenericCopy;
import pl.biblioteka.generic.GenericLoan;
import pl.biblioteka.model.Auth;
import pl.biblioteka.model.Book;
import pl.biblioteka.model.Item;
import pl.biblioteka.model.LibraryManager;
import pl.biblioteka.model.User;
import pl.biblioteka.model.UserRole;

import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
 
@SpringBootApplication  
public class Main {
    public static void main(String[] args) {
    	  SpringApplication.run(Main.class, args);
        LibraryManager manager = LibraryManager.getInstance();

        User admin = new User(1L, "Jan", "Nowak", "jan.nowak@biblioteka.pl", UserRole.ADMIN);
        Auth auth = new Auth();
        auth.login(admin);
        manager.addUser(admin);
 
        manager.registerItemInCatalog(Book.class, new Book(1001L, "ffffffffffffff", "Robert C. Martin", "9780132350333"), auth.getCurrentUser());
        manager.registerItemInCatalog(Book.class, new Book(1002L, "cccccc", "Robert C. Martin", "9780132350884"), auth.getCurrentUser());
        manager.registerItemInCatalog(Book.class, new Book(1003L, "ddddd", "Robert C. Martin", "9780132350111"), auth.getCurrentUser());

 
        manager.borrowGenericCopy(1001L, admin.getId(), 1L);
        manager.borrowGenericCopy(1002L, admin.getId(), 2L);
        manager.borrowGenericCopy(1003L, admin.getId(), 3L);

        System.out.println("Zalogowany użytkownik: " + auth.getCurrentUser());
    }
}
