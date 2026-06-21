package com.myproject.jobportal.contact.service;

import com.myproject.jobportal.dto.ContactRequestDto;

public interface IContactService {

    boolean saveContact(ContactRequestDto contactRequestDto) ;
}
