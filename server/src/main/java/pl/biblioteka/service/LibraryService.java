package pl.biblioteka.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import pl.biblioteka.dto.FirstName;
import pl.biblioteka.dto.ItemRequest;
import pl.biblioteka.dto.LibraryResponse;

public interface LibraryService {
    List<LibraryResponse> getLibraryData();
    String getById(String id);
    ResponseEntity<String> linkNames(List<FirstName> incNames);
    ResponseEntity<String> saveLoans(List<ItemRequest> incomingLoans);
    boolean updateItem(long id, LibraryResponse updatedData);
}
