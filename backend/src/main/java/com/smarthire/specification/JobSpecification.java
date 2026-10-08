package com.smarthire.specification;

import com.smarthire.dto.JobFilterCriteria;
import com.smarthire.entity.Job;
import com.smarthire.entity.JobStatus;
import com.smarthire.entity.Skill;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobSpecification {

    public static Specification<Job> filterBy(JobFilterCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Status filter (default to PUBLISHED if not specified)
            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
            } else {
                predicates.add(cb.equal(root.get("status"), JobStatus.PUBLISHED));
            }

            // Keyword filter matching title or description or company name
            if (StringUtils.hasText(criteria.getKeyword())) {
                String pattern = "%" + criteria.getKeyword().toLowerCase().trim() + "%";
                Predicate titleLike = cb.like(cb.lower(root.get("title")), pattern);
                Predicate descLike = cb.like(cb.lower(root.get("description")), pattern);
                Predicate companyLike = cb.like(cb.lower(root.get("company").get("name")), pattern);
                predicates.add(cb.or(titleLike, descLike, companyLike));
            }

            // Location filter
            if (StringUtils.hasText(criteria.getLocation())) {
                String locPattern = "%" + criteria.getLocation().toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("location")), locPattern));
            }

            // Experience filter (checking if candidate experience is within min-max)
            if (criteria.getExperience() != null) {
                Predicate minExpValid = cb.or(
                        cb.isNull(root.get("minExperience")),
                        cb.lessThanOrEqualTo(root.get("minExperience"), criteria.getExperience())
                );
                Predicate maxExpValid = cb.or(
                        cb.isNull(root.get("maxExperience")),
                        cb.greaterThanOrEqualTo(root.get("maxExperience"), criteria.getExperience())
                );
                predicates.add(cb.and(minExpValid, maxExpValid));
            }

            // Employment Type filter
            if (criteria.getEmploymentType() != null) {
                predicates.add(cb.equal(root.get("employmentType"), criteria.getEmploymentType()));
            }

            // Work Mode filter
            if (criteria.getWorkMode() != null) {
                predicates.add(cb.equal(root.get("workMode"), criteria.getWorkMode()));
            }

            // Minimum Salary filter
            if (criteria.getMinSalary() != null) {
                predicates.add(cb.or(
                        cb.isNull(root.get("maxSalary")),
                        cb.greaterThanOrEqualTo(root.get("maxSalary"), criteria.getMinSalary())
                ));
            }

            // Maximum Salary filter
            if (criteria.getMaxSalary() != null) {
                predicates.add(cb.or(
                        cb.isNull(root.get("minSalary")),
                        cb.lessThanOrEqualTo(root.get("minSalary"), criteria.getMaxSalary())
                ));
            }

            // Company filter
            if (criteria.getCompanyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), criteria.getCompanyId()));
            }

            // Skill filter
            if (StringUtils.hasText(criteria.getSkill())) {
                Join<Job, Skill> skillsJoin = root.join("requiredSkills", JoinType.LEFT);
                predicates.add(cb.equal(cb.lower(skillsJoin.get("name")), criteria.getSkill().toLowerCase().trim()));
            }

            // Do not show expired jobs in public search
            predicates.add(cb.greaterThanOrEqualTo(root.get("applicationDeadline"), LocalDate.now()));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
