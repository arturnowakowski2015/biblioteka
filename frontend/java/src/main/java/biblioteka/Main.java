package biblioteka;

import biblioteka.model.Auth;
import biblioteka.model.Book;
import biblioteka.model.LibraryManager;
import biblioteka.model.User;
import biblioteka.model.UserRole;

public class Main {
    public static void main(String[] args) {
        LibraryManager manager = LibraryManager.getInstance();

        User admin = new User(1L, "Jan", "Nowak", "jan.nowak@biblioteka.pl", UserRole.ADMIN);
        Auth auth = new Auth();
        auth.login(admin);
        manager.addUser(admin);

        Book book = new Book(1001L, "Czysty kod", "Robert C. Martin", "9780132350884");
        manager.addItem(book, auth.getCurrentUser());

        manager.borrowCopy(5001L, admin.getId(), 1L);

        System.out.println("Zalogowany użytkownik: " + auth.getCurrentUser());
        System.out.println("Katalog: " + manager.getKatalog());
        System.out.println("Aktywne wypożyczenia: " + manager.getLoans());
        System.out.println("Historia wypożyczeń: " + manager.getHistoryLoans());
    }
}