package com.myproject.jobportal.contact.service.impl;

import com.myproject.jobportal.constants.ApplicationConstants;
import com.myproject.jobportal.contact.service.IContactService;
import com.myproject.jobportal.dto.ContactRequestDto;
import com.myproject.jobportal.dto.ContactResponseDto;
import com.myproject.jobportal.entity.Contact;
import com.myproject.jobportal.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

private final ContactRepository contactRepository;

public boolean saveContact(ContactRequestDto contactRequestDto) {
	boolean result = false;

/*
       Contact contact = contactRepository.save(transformToEntity(contactRequestDto));
       if(contact != null) {
           return true;
       }
       return false;

        rather we can use
        return contactRepository.save(this.transformToEntity(contactRequestDto)) !=null;
        rather in professional flow we don't return boolean also, just save data; make return type void.
*/
	Contact contact = contactRepository.save(this.transformToEntity(contactRequestDto));
	System.out.println(contact);//Just for logging purpose, but this is not the professional one. Will explore one Log.info(0 later
	
	if (contact != null && contact.getId() != null) {
		result = true;
	}
	return result;
}


private Contact transformToEntity(ContactRequestDto contactRequestDto) {
	Contact contact = new Contact();

//        contact.setId(contactRequestDto.id());
//        contact.setName(contactRequestDto.name());
//        ...
	//This is nothing but a cumbersome process to manually initialize the entity object. But we can use an utility
	// method to direct copy the object sata into the corresponding same name fieds from one object to another object.
	//Let's leverage that here for copying dto fields' data to contact entity's fields
	
	BeanUtils.copyProperties(contactRequestDto, contact);
//        contact.setCreatedAt(Instant.now());
//        contact.setCreatedBy("System");
	//Because now JPA Auditing Automatically Handle this
	return contact;
	
}

@PreAuthorize("hasAuthority('ROLE_ADMIN')") //It's method level security.(Fine-Grained authorization)
public List<ContactResponseDto> getAllContactsByStatus() {
	List<Contact> contacts = contactRepository.getContactsByStatus(ApplicationConstants.MESSAGE_STATUS_NEW);
	
	List<ContactResponseDto> contactResponseDtoList = contacts.stream().map(this::transformToDto).collect(Collectors.toList());
	
	return contactResponseDtoList;
	
}

private ContactResponseDto transformToDto(Contact contact) {
	
	return new ContactResponseDto(contact.getId(), contact.getName(), contact.getEmail(), contact.getUserType(), contact.getSubject(), contact.getMessage(), contact.getStatus(), contact.getCreatedAt());
	
}

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public List<ContactResponseDto> getAllContactsByStatusWithSorting(String sortBy, String sortDirection) {
	
	Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	
	List<Contact> contacts = contactRepository.getContactsByStatus(ApplicationConstants.MESSAGE_STATUS_NEW, sort);
	
	return contacts.stream().map(this::transformToDto).collect(Collectors.toList());
	
}

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public Page<ContactResponseDto> getAllContactsByStatusWithSortingAndPagination(int pageNumber, int pageSize, String sortBy, String sortDirection) {
	
	Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	Pageable pageRequest = PageRequest.of(pageNumber,pageSize,sort);
	
	Page<Contact> contactPages = contactRepository.getContactsByStatus(ApplicationConstants.MESSAGE_STATUS_NEW, pageRequest );
	
	return contactPages.map(this::transformToDto);
	
}

@Override
public boolean changeContactStatustoClosed(Long id) {
	  Contact contact = contactRepository.findById(id).orElse(null);
      if(contact != null) {
		      contact.setStatus(ApplicationConstants.MESSAGE_STATUS_CLOSED);
		      contactRepository.save(contact);
	      }else {
	      return false;
      }
	  return true;
}

	
}


