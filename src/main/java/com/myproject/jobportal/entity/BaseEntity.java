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
    @Column(name = "created_at", nullable = false, updatable = false) //updatable = false & insertable by def = true, ensures that on update the created_at must not update.But here insertable = true, Means the created_at be updated at the time of creation only for one time
    @CreatedDate//Will fetch created by from System
    private Instant createdAt;

    @CreatedBy //Will fetch created by from AuditAwareImpl
    @Column(name = "created_by", nullable = false, length = 20, updatable = false)
    private String createdBy;

    @LastModifiedDate//Will fetch LastModifiedDate from System
    @Column(name = "updated_at" , insertable = false) //here by def updatable = true, but I have explicitly made insertable = false, which together ensures that on update the updated_at must update each time on that particular row. But not at the time of insert/first time data insertion.
    private Instant updatedAt;

    @LastModifiedBy //Will fetch LastModifiedBy from AuditAwareImpl
    @Column(name = "updated_by", length = 20, insertable=false)
    private String updatedBy;

    //From the last two columns I have removed nullable = true, as it is redundant being there. But contrary to it, if columns needs to be not null, then putting nullable = false becomes meaningful and mandatory.
    
    /**
     *  * 2. WHY REMOVE 'nullable = true' FROM @Column?
     *  *    - Because in JPA, columns are nullable by default (nullable() default true).
     *  *    - If you delete 'nullable = true', Hibernate still treats the column as nullable.
     *  *    - Leaving it out keeps your annotation lines short, readable, and professional.
     *  *
     *  * 💡 RULE: Only write an attribute if you are CHANGING the default behavior
     *  *    (like writing 'nullable = false' or 'insertable = false'). Otherwise, delete it!
     *  */

//    Reponsibility of this auto-fetching the data is of @EntityListeners(AuditingEntityListener.class)
//    @MappedSuperclass: Says"Yeh class khud koi database table nahi hai.Lekin iske andar jitne bhi fields hain (jaise createdAt, createdBy), unhe un sabhi child tables (jaise Contact, Company) ke andar columns ke roop mein copy-paste kar do jo is class ko extends karte hain."
}
