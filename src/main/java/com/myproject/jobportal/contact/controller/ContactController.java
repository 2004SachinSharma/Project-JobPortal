package com.myproject.jobportal.contact.controller;

import com.myproject.jobportal.contact.service.impl.ContactServiceImpl;
import com.myproject.jobportal.dto.ContactRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    public final ContactServiceImpl contactService;

    @PostMapping(version = "1.0")
    public ResponseEntity<String> saveContact(@RequestBody ContactRequestDto contactdto) {

        contactService.saveContact(contactdto);

        return ResponseEntity.status(HttpStatus.CREATED).body("Contact ban gya bhai");
    }

}
