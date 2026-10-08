package com.smarthire.repository;

import com.smarthire.entity.Application;
import com.smarthire.entity.ApplicationStatus;
import com.smarthire.entity.Company;
import com.smarthire.entity.Job;
import com.smarthire.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateOrderByAppliedAtDesc(User candidate);
    Page<Application> findByCandidate(User candidate, Pageable pageable);

    List<Application> findByJob(Job job);
    Page<Application> findByJob(Job job, Pageable pageable);

    @Query("SELECT a FROM Application a WHERE a.job.company.id = :companyId ORDER BY a.appliedAt DESC")
    List<Application> findByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT a FROM Application a WHERE a.job.company.id = :companyId ORDER BY a.appliedAt DESC")
    Page<Application> findByCompanyId(@Param("companyId") Long companyId, Pageable pageable);

    Optional<Application> findByCandidateIdAndJobId(Long candidateId, Long jobId);
    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);

    boolean existsByCandidateIdAndJobCompanyId(Long candidateId, Long companyId);

    long countByStatus(ApplicationStatus status);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.company = :company")
    long countByJobCompany(@Param("company") Company company);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.company = :company AND a.status = :status")
    long countByJobCompanyAndStatus(@Param("company") Company company, @Param("status") ApplicationStatus status);
}
