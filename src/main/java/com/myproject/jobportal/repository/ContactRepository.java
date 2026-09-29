package com.myproject.jobportal.repository;

import com.myproject.jobportal.dto.ContactResponseDto;
import com.myproject.jobportal.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

 public List<Contact> getContactsByStatus(String status);
 public List<Contact> getContactsByStatus(String status, Sort sort);
 public Page<Contact> getContactsByStatus(String status, Pageable pageRequest);
}