package com.myproject.jobportal.contact.controller;

import com.myproject.jobportal.contact.service.IContactService;
import com.myproject.jobportal.dto.ContactRequestDto;
import com.myproject.jobportal.dto.ContactResponseDto;
import jakarta.validation.Valid;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

public final IContactService contactService;

@PostMapping(path = "/public", version = "1.0")
public ResponseEntity<String> saveContact(@RequestBody @Valid ContactRequestDto contactRequestDto) {
	
	boolean isSaved = contactService.saveContact(contactRequestDto);
	
	if (isSaved) {
		return ResponseEntity.status(HttpStatus.CREATED)
				       .body("Request processed successfully");
	} else {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				       .body("Request processing failed");
	}
}

@GetMapping(path = "/admin", version = "1.0")
public ResponseEntity<List<ContactResponseDto>> fetchOpenContacts() {
	List<ContactResponseDto> openContacts = contactService.getAllContactsByStatus();
	return ResponseEntity.ok().body(openContacts);
	
}

@GetMapping(path="/sorting/admin", version="1.0")
public ResponseEntity<List<ContactResponseDto>> fetchOpenContactsWithSorting(
		@RequestParam(defaultValue = "createdAt", name = "sortby") String sortBy,
		@RequestParam(defaultValue = "asc", name = "sortdir")String sortDir
) {
	return ResponseEntity.ok(contactService.getAllContactsByStatusWithSorting(sortBy, sortDir));
}

@GetMapping(path="/page/admin", version="1.0")
public ResponseEntity<Page<ContactResponseDto>> fetchOpenContactsWithSortingAndPagination(
		@RequestParam(defaultValue = "0") int pageNumber,
		@RequestParam(defaultValue = "3") int pageSize,
		@RequestParam(defaultValue = "createdAt") String sortBy,
		@RequestParam(defaultValue = "asc")String sortDir
) {
	return ResponseEntity.ok(contactService.getAllContactsByStatusWithSortingAndPagination(pageNumber,pageSize, sortBy,sortDir));
}

@PatchMapping(path="/{id}/status/admin", version="1.0")
public ResponseEntity<String> fetchOpenContactsWithSortingAndPagination(@PathVariable String id) {
	
	if(contactService.changeContactStatustoClosed(Long.valueOf(id))){
		return ResponseEntity.status(HttpStatus.OK)
				       .body("Contact message updated successfully.");
	}else
	{
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to updated contact message.");
	}
}

}
