package com.myproject.jobportal.repository;

import com.myproject.jobportal.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {
Optional<JobPortalUser> readUserByEmailOrMobileNumber(String email, String mobileNumber);
Optional<JobPortalUser> findByEmail(String email);
}