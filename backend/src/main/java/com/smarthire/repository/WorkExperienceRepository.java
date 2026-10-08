package com.smarthire.repository;

import com.smarthire.entity.CandidateProfile;
import com.smarthire.entity.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {
    List<WorkExperience> findByCandidateProfile(CandidateProfile candidateProfile);
}
