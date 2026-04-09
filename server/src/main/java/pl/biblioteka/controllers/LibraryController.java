// ...existing imports...
import pl.biblioteka.service.LibraryService;
// ...existing code...

@RestController
@RequestMapping("/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/loans")
    public List<LibraryResponse> getLibraryData() {
        return libraryService.getLibraryData();
    }

    @GetMapping("/name{id}")
    public String getById(@PathVariable("id") String id) {
        return libraryService.getById(id);
    }

    @PostMapping("/imiona")
    public ResponseEntity<String> linkNames(@RequestBody List<FirstName> incNames) {
        return libraryService.linkNames(incNames);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveLoans(@RequestBody List<ItemRequest> incomingLoans) {
        return libraryService.saveLoans(incomingLoans);
    }

    @PutMapping("/items/{id}")
    public boolean updateItem(@PathVariable long id, @RequestBody LibraryResponse updatedData) {
        return libraryService.updateItem(id, updatedData);
    }

    // ...other controller code...
}
