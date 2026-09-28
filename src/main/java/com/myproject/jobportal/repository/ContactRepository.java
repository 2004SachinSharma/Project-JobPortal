package com.myproject.jobportal.repository;

import com.myproject.jobportal.dto.ContactResponseDto;
import com.myproject.jobportal.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

// public List<Contact> getContactsByStatus(String status);
 public List<Contact> getContactsByStatusOrderById(String status);
}