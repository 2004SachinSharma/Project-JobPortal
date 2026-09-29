package com.myproject.jobportal.contact.service;

import com.myproject.jobportal.dto.ContactRequestDto;
import com.myproject.jobportal.dto.ContactResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IContactService {

    boolean saveContact(ContactRequestDto contactRequestDto) ;
    List<ContactResponseDto> getAllContactsByStatus();
    List<ContactResponseDto> getAllContactsByStatusWithSorting(String sortBy, String sortDirection);
    Page<ContactResponseDto> getAllContactsByStatusWithSortingAndPagination(int pageNumber, int pageSize, String sortBy, String sortDirection);
    boolean changeContactStatustoClosed(Long id);
}
