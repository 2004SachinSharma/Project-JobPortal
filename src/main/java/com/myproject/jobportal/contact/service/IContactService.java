package com.myproject.jobportal.contact.service;

import com.myproject.jobportal.dto.CompanyDto;
import com.myproject.jobportal.dto.ContactRequestDto;

public interface IContactService {

    void saveContact(ContactRequestDto companyDto) ;
}
