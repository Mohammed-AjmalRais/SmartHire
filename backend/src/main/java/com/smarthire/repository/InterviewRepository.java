package com.smarthire.repository;

import com.smarthire.entity.Interview;
import com.smarthire.entity.InterviewStatus;
import com.smarthire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    @Query("SELECT i FROM Interview i WHERE i.application.candidate = :candidate ORDER BY i.interviewDate ASC, i.startTime ASC")
    List<Interview> findByCandidate(@Param("candidate") User candidate);

    @Query("SELECT i FROM Interview i WHERE i.application.job.company.id = :companyId ORDER BY i.interviewDate ASC, i.startTime ASC")
    List<Interview> findByCompanyId(@Param("companyId") Long companyId);

    List<Interview> findByInterviewerOrderByInterviewDateAscStartTimeAsc(User interviewer);

    List<Interview> findByApplicationId(Long applicationId);

    @Query("SELECT COUNT(i) > 0 FROM Interview i " +
           "WHERE i.interviewer = :interviewer " +
           "AND i.interviewDate = :date " +
           "AND i.status = :status " +
           "AND (:excludeId IS NULL OR i.id <> :excludeId) " +
           "AND ((i.startTime < :endTime AND i.endTime > :startTime))")
    boolean existsConflictingInterview(
            @Param("interviewer") User interviewer,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("status") InterviewStatus status,
            @Param("excludeId") Long excludeId
    );

    long countByStatus(InterviewStatus status);

    @Query("SELECT COUNT(i) FROM Interview i WHERE i.application.job.company.id = :companyId")
    long countByCompanyId(@Param("companyId") Long companyId);
}
