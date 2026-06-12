package com.myproject.jobportal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Column(name = "CREATED_AT", nullable = false, updatable = false) //updatable = false & insertable by def = true, ensures that on update the created_at must not update.But here insertable = true, Means the created_at be updated at the time of creation only for one time
    @CreatedDate//Will fetch created by from System
    private Instant createdAt;

    @CreatedBy //Will fetch created by from AuditAwareImpl
    @Column(name = "CREATED_BY", nullable = false, length = 20, updatable = false)
    private String createdBy;

    @LastModifiedDate//Will fetch LastModifiedDate from System
    @Column(name = "UPDATED_AT" , nullable = true , insertable = false) //here by def updatable = true, but I have explicitly made insertable = false, which together ensures that on update the updated_at must update each time on that particular row. But not at the time of insert/first time data insertion.
    private Instant updatedAt;

    @LastModifiedBy //Will fetch LastModifiedBy from AuditAwareImpl
    @Column(name = "UPDATED_BY", nullable = true, length = 20, insertable=false)
    private String updatedBy;

//    Reponsibility of this auto-fetching the data is of @EntityListeners(AuditingEntityListener.class)
//    @MappedSuperclass: Says"Yeh class khud koi database table nahi hai.Lekin iske andar jitne bhi fields hain (jaise createdAt, createdBy), unhe un sabhi child tables (jaise Contact, Company) ke andar columns ke roop mein copy-paste kar do jo is class ko extends karte hain."
}
