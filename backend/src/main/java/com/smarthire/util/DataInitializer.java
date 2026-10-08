package com.smarthire.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.smarthire.entity.*;
import com.smarthire.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final SkillRepository skillRepository;
    private final EducationRepository educationRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final ResumeRepository resumeRepository;
    private final InterviewRepository interviewRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CompanyRepository companyRepository, RecruiterProfileRepository recruiterProfileRepository, CandidateProfileRepository candidateProfileRepository, SkillRepository skillRepository, EducationRepository educationRepository, WorkExperienceRepository workExperienceRepository, JobRepository jobRepository, ApplicationRepository applicationRepository, ResumeRepository resumeRepository, InterviewRepository interviewRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.skillRepository = skillRepository;
        this.educationRepository = educationRepository;
        this.workExperienceRepository = workExperienceRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.resumeRepository = resumeRepository;
        this.interviewRepository = interviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${smarthire.upload.resumes-dir:uploads/resumes}")
    private String resumesDir;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping initial data seed.");
            return;
        }

        log.info("Starting initial seed data generation...");

        // Ensure resume upload directory exists
        try {
            Path path = Paths.get(resumesDir);
            Files.createDirectories(path);
        } catch (Exception e) {
            log.warn("Could not create initial resumes directory: {}", e.getMessage());
        }

        // 1. Create Skills
        Skill javaSkill = skillRepository.save(Skill.builder().name("Java").build());
        Skill springBootSkill = skillRepository.save(Skill.builder().name("Spring Boot").build());
        Skill restApiSkill = skillRepository.save(Skill.builder().name("REST API").build());
        Skill postgresSkill = skillRepository.save(Skill.builder().name("PostgreSQL").build());
        Skill reactSkill = skillRepository.save(Skill.builder().name("React").build());
        Skill typeScriptSkill = skillRepository.save(Skill.builder().name("TypeScript").build());
        Skill gitSkill = skillRepository.save(Skill.builder().name("Git").build());
        Skill dockerSkill = skillRepository.save(Skill.builder().name("Docker").build());

        // 2. Create Platform Administrator
        User admin = userRepository.save(User.builder()
                .fullName("System Administrator")
                .email("admin@smarthire.com")
                .password(passwordEncoder.encode("Admin@123"))
                .role(Role.ROLE_ADMIN)
                .enabled(true)
                .build());

        // 3. Create Companies
        Company techNova = companyRepository.save(Company.builder()
                .name("TechNova Solutions")
                .description("Leading provider of enterprise cloud software and distributed systems.")
                .website("https://technova.example.com")
                .industry("Information Technology & Services")
                .location("Hyderabad, India")
                .logoUrl(null)
                .verified(true) // Verified by admin
                .build());

        Company apexLabs = companyRepository.save(Company.builder()
                .name("Apex Global Labs")
                .description("Next-gen AI and data solutions startup.")
                .website("https://apexlabs.example.com")
                .industry("Artificial Intelligence")
                .location("Bengaluru, India")
                .logoUrl(null)
                .verified(false) // Pending verification to demonstrate workflow
                .build());

        // 4. Create Recruiters
        User recruiterUser1 = userRepository.save(User.builder()
                .fullName("Sarah Jenkins")
                .email("recruiter@technova.com")
                .password(passwordEncoder.encode("Recruiter@123"))
                .role(Role.ROLE_RECRUITER)
                .enabled(true)
                .build());

        recruiterProfileRepository.save(RecruiterProfile.builder()
                .user(recruiterUser1)
                .company(techNova)
                .positionTitle("Senior Talent Acquisition Specialist")
                .phone("+91 98765 43210")
                .build());

        User recruiterUser2 = userRepository.save(User.builder()
                .fullName("Marcus Vance")
                .email("recruiter@apexlabs.com")
                .password(passwordEncoder.encode("Recruiter@123"))
                .role(Role.ROLE_RECRUITER)
                .enabled(true)
                .build());

        recruiterProfileRepository.save(RecruiterProfile.builder()
                .user(recruiterUser2)
                .company(apexLabs)
                .positionTitle("Technical Recruiter")
                .phone("+91 91234 56789")
                .build());

        // 5. Create Candidate
        User candidateUser = userRepository.save(User.builder()
                .fullName("John Doe")
                .email("john.doe@example.com")
                .password(passwordEncoder.encode("Candidate@123"))
                .role(Role.ROLE_CANDIDATE)
                .enabled(true)
                .build());

        Set<Skill> candidateSkills = new HashSet<>(List.of(javaSkill, springBootSkill, restApiSkill, postgresSkill, gitSkill));

        CandidateProfile candidateProfile = CandidateProfile.builder()
                .user(candidateUser)
                .phone("+91 99887 76655")
                .location("Hyderabad, India")
                .professionalSummary("Enthusiastic Java & Spring Boot backend developer with experience in relational database design, RESTful APIs, and clean code principles.")
                .githubUrl("https://github.com/johndoe")
                .linkedinUrl("https://linkedin.com/in/johndoe")
                .portfolioUrl("https://johndoe.dev")
                .yearsOfExperience(2)
                .skills(candidateSkills)
                .build();

        CandidateProfile savedProfile = candidateProfileRepository.save(candidateProfile);

        // Add Candidate Education
        educationRepository.save(Education.builder()
                .candidateProfile(savedProfile)
                .institution("National Institute of Technology")
                .degree("Bachelor of Technology")
                .fieldOfStudy("Computer Science & Engineering")
                .startDate(LocalDate.of(2020, 8, 1))
                .endDate(LocalDate.of(2024, 5, 30))
                .grade("8.8 CGPA")
                .build());

        // Add Candidate Work Experience
        workExperienceRepository.save(WorkExperience.builder()
                .candidateProfile(savedProfile)
                .companyName("CloudCraft Software")
                .jobTitle("Junior Java Developer")
                .location("Hyderabad, India")
                .startDate(LocalDate.of(2024, 7, 1))
                .isCurrent(true)
                .description("Developed RESTful web services using Spring Boot and PostgreSQL, improved query performance, and implemented unit tests with JUnit 5.")
                .build());

        // Create a dummy resume file on disk
        String sampleResumeFile = "sample_resume_john_doe.pdf";
        try {
            Path targetFile = Paths.get(resumesDir, sampleResumeFile);
            if (!Files.exists(targetFile)) {
                Files.writeString(targetFile, "%PDF-1.4 - SmartHire Sample Candidate Resume Content for John Doe");
            }
        } catch (Exception e) {
            log.warn("Sample resume file generation error: {}", e.getMessage());
        }

        Resume sampleResume = resumeRepository.save(Resume.builder()
                .candidateProfile(savedProfile)
                .originalFileName("John_Doe_Resume_2026.pdf")
                .storedFileName(sampleResumeFile)
                .filePath(sampleResumeFile)
                .contentType("application/pdf")
                .fileSize(1024L)
                .version(1)
                .isPrimary(true)
                .build());

        // 6. Create Job Postings for TechNova Solutions (Verified Company)
        Job job1 = jobRepository.save(Job.builder()
                .company(techNova)
                .recruiter(recruiterUser1)
                .title("Java Backend Developer")
                .description("We are looking for a skilled Java Backend Developer to build scalable backend services using Spring Boot, Hibernate, and PostgreSQL. You will design REST APIs, write automated tests, and collaborate with frontend engineers.")
                .requiredSkills(new HashSet<>(List.of(javaSkill, springBootSkill, restApiSkill, postgresSkill, gitSkill)))
                .minExperience(1)
                .maxExperience(3)
                .minSalary(600000.0)
                .maxSalary(1000000.0)
                .currency("INR")
                .location("Hyderabad, India")
                .employmentType(EmploymentType.FULL_TIME)
                .workMode(WorkMode.HYBRID)
                .openings(3)
                .applicationDeadline(LocalDate.now().plusMonths(2))
                .status(JobStatus.PUBLISHED)
                .build());

        Job job2 = jobRepository.save(Job.builder()
                .company(techNova)
                .recruiter(recruiterUser1)
                .title("Full-Stack Software Engineer")
                .description("Join our core product team building high-performance web applications with React, TypeScript, Java, and Spring Boot. Experience with cloud deployments and Docker is a major plus.")
                .requiredSkills(new HashSet<>(List.of(javaSkill, springBootSkill, reactSkill, typeScriptSkill, dockerSkill)))
                .minExperience(2)
                .maxExperience(5)
                .minSalary(900000.0)
                .maxSalary(1500000.0)
                .currency("INR")
                .location("Hyderabad, India")
                .employmentType(EmploymentType.FULL_TIME)
                .workMode(WorkMode.REMOTE)
                .openings(2)
                .applicationDeadline(LocalDate.now().plusMonths(1))
                .status(JobStatus.PUBLISHED)
                .build());

        // 7. Create an active Application with Shortlisted status & Scheduled Interview
        Application app1 = applicationRepository.save(Application.builder()
                .candidate(candidateUser)
                .job(job1)
                .resume(sampleResume)
                .coverLetter("I am passionate about building resilient backend microservices and modern Spring Boot applications.")
                .status(ApplicationStatus.INTERVIEW_SCHEDULED)
                .recruiterNotes("Strong core Java skills and sound knowledge of relational schemas.")
                .build());

        // 8. Create an Interview
        interviewRepository.save(Interview.builder()
                .application(app1)
                .interviewer(recruiterUser1)
                .interviewType(InterviewType.TECHNICAL)
                .interviewDate(LocalDate.now().plusDays(5))
                .startTime(LocalTime.of(14, 0))
                .endTime(LocalTime.of(15, 0))
                .meetingLink("https://meet.google.com/abc-defg-hij")
                .location("Google Meet")
                .notes("Technical round covering Spring Boot, JPA concurrency, and SQL queries.")
                .status(InterviewStatus.SCHEDULED)
                .build());

        log.info("Initial data seed completed successfully!");
        log.info("Admin user: admin@smarthire.com / Admin@123");
        log.info("Recruiter user (TechNova): recruiter@technova.com / Recruiter@123");
        log.info("Recruiter user (Apex Labs - Pending): recruiter@apexlabs.com / Recruiter@123");
        log.info("Candidate user: john.doe@example.com / Candidate@123");
    }
}
