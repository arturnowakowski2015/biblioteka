package pl.biblioteka.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import pl.biblioteka.dto.FirstName;
import pl.biblioteka.dto.ItemRequest;
import pl.biblioteka.dto.LibraryResponse;
import pl.biblioteka.model.Auth;
import pl.biblioteka.model.Book;
import pl.biblioteka.model.LibraryManager;
import pl.biblioteka.model.User;
import pl.biblioteka.model.UserRole;

@Service
public class LibraryServiceImpl implements LibraryService {

    @Override
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
                                    .filter(it -> it.getId() == t.getId())
                                    .findFirst()
                                    .map(it -> it.getAuthor())
                                    .orElse("nieznany autor"),
                            m.getKatalog(t.getWhatHas().getItem().getClass()).getAllItems().stream()
                                    .filter(it -> it.getId() == t.getId())
                                    .findFirst()
                                    .map(it -> it.getTytul())
                                    .orElse("nieznany tytul"),
                            status,
                            t.getFrom().toString(),
                            t.getEnd() != null ? t.getEnd().toString() : "ACTIVE");
                })
                .toList();

        return loans;
    }

    @Override
    public String getById(String id) {
        // Oryginalny controller nie miał zaimplementowanej logiki - pozostawiam placeholder.
        LibraryManager m = LibraryManager.getInstance();
        return "Pobrano dane dla ID: " + id;
    }

    @Override
    public ResponseEntity<String> linkNames(List<FirstName> incNames) {
        HashMap<String, String> ilosc = new HashMap<>();
        try {
            if (incNames == null || incNames.isEmpty()) {
                return ResponseEntity.badRequest().body("Brak danych wejściowych");
            }

            if (!incNames.get(0).getOn().equalsIgnoreCase("-") || !incNames.get(0).getOna().equalsIgnoreCase("-")) {
                ilosc.put(incNames.get(0).getOn(), incNames.get(0).getOna());

                StringBuilder wynik = new StringBuilder();
                ilosc.forEach((t, k) -> {
                    wynik.append("key: ").append(t).append(", value: ").append(k).append("\n");
                });

                return ResponseEntity.ok("Zapisano \n" + wynik);
            } else
                return ResponseEntity.ok("wiecej nie mozna");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd zapisu: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> saveLoans(List<ItemRequest> incomingLoans) {
        if (incomingLoans == null || incomingLoans.isEmpty()) {
            return ResponseEntity.badRequest().body("Brak rekordów do zapisania");
        }

        incomingLoans.forEach(t -> System.out.println(">>>>>>>>>>>>>   " + t.toString()));
        try {
            LibraryManager m = LibraryManager.getInstance();
            User admin = new User(1L, "Jan", "Nowak", "jan.nowak@biblioteka.pl", UserRole.ADMIN);
            Auth auth = new Auth();
            auth.login(admin);
            m.addUser(admin);

            int i = 0;
            long f = 1004L;
            long id = 3;
            for (ItemRequest loan : incomingLoans) {
                i++;
                System.out.println("Próba dodania nr " + i + ": " + loan.getTitle());

                try {
                    User user = auth.getCurrentUser();
                    if (user == null) {
                        break;
                    }

                    m.registerItemInCatalog(Book.class, new Book(f, loan.getTitle(), loan.getAuthor(), loan.getIsbn()), user);
                    m.borrowGenericCopy(f++, admin.getId(), ++id);
                } catch (Throwable t) {
                    System.out.println("KATASTROFA przy " + i + ": " + t.getMessage());
                    t.printStackTrace();
                }
            }

            return ResponseEntity.ok("Zapisano rekordów");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd zapisu: " + e.getMessage());
        }
    }

    @Override
    public boolean updateItem(long id, LibraryResponse updatedData) {
        LibraryManager m = LibraryManager.getInstance();
        boolean isUpdated = m.update(id, updatedData.getStatus());
        return isUpdated;
    }
}
