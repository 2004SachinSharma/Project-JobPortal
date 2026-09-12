package com.myproject.jobportal.repository;

import com.myproject.jobportal.entity.JobPortalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {
}