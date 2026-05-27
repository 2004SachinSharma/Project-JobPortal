package com.myproject.jobportal.contact.service.impl;

import com.myproject.jobportal.contact.service.IContactService;
import com.myproject.jobportal.dto.ContactRequestDto;

import com.myproject.jobportal.entity.Contact;
import com.myproject.jobportal.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;

    public void saveContact(ContactRequestDto contactRequestDto){

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
    }


    private Contact transformToEntity(ContactRequestDto contactRequestDto){
        Contact contact = new Contact();

//        contact.setId(contactRequestDto.id());
//        contact.setName(contactRequestDto.name());
//        ...
    //This is nothing but a cumbersome process to manually initialize the entity object. But we can use an utility
    // method to direct copy the object sata into the corresponding same name fieds from one object to another object.
    //Let's leverage that here for copying dto fields' data to contact entity's fields

        BeanUtils.copyProperties(contactRequestDto,contact);
        contact.setCreatedAt(Instant.now());
        contact.setCreatedBy("System");
        return contact;

    }
}
