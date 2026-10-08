package com.smarthire.repository;

import com.smarthire.entity.Company;
import com.smarthire.entity.Job;
import com.smarthire.entity.JobStatus;
import com.smarthire.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
    List<Job> findByCompany(Company company);
    Page<Job> findByCompany(Company company, Pageable pageable);
    List<Job> findByRecruiter(User recruiter);
    long countByStatus(JobStatus status);
    long countByCompany(Company company);
}
