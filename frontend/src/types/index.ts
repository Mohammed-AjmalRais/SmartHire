export type Role = 'ROLE_CANDIDATE' | 'ROLE_RECRUITER' | 'ROLE_ADMIN';

export type EmploymentType = 'FULL_TIME' | 'PART_TIME' | 'CONTRACT' | 'INTERNSHIP';
export type WorkMode = 'REMOTE' | 'HYBRID' | 'ON_SITE';
export type JobStatus = 'DRAFT' | 'PUBLISHED' | 'CLOSED';
export type ApplicationStatus = 'APPLIED' | 'UNDER_REVIEW' | 'SHORTLISTED' | 'INTERVIEW_SCHEDULED' | 'SELECTED' | 'REJECTED' | 'WITHDRAWN';
export type InterviewType = 'TECHNICAL' | 'HR' | 'MANAGERIAL';
export type InterviewStatus = 'SCHEDULED' | 'COMPLETED' | 'CANCELLED';

export interface User {
  id: number;
  email: string;
  fullName: string;
  role: Role;
  enabled: boolean;
  companyId?: number;
  companyName?: string;
  companyVerified?: boolean;
  createdAt: string;
}

export interface Company {
  id: number;
  name: string;
  description?: string;
  website?: string;
  industry?: string;
  location?: string;
  logoUrl?: string;
  verified: boolean;
  activeJobsCount: number;
  createdAt: string;
}

export interface Skill {
  id?: number;
  name: string;
}

export interface Education {
  id?: number;
  institution: string;
  degree: string;
  fieldOfStudy?: string;
  startDate?: string;
  endDate?: string;
  grade?: string;
}

export interface WorkExperience {
  id?: number;
  companyName: string;
  jobTitle: string;
  location?: string;
  startDate?: string;
  endDate?: string;
  isCurrent?: boolean;
  description?: string;
}

export interface Resume {
  id: number;
  originalFileName: string;
  storedFileName: string;
  contentType: string;
  fileSize: number;
  version: number;
  isPrimary: boolean;
  uploadedAt: string;
}

export interface CandidateProfile {
  id: number;
  userId: number;
  fullName: string;
  email: string;
  phone?: string;
  location?: string;
  professionalSummary?: string;
  githubUrl?: string;
  linkedinUrl?: string;
  portfolioUrl?: string;
  yearsOfExperience?: number;
  skills: string[];
  educations: Education[];
  workExperiences: WorkExperience[];
  resumes: Resume[];
  primaryResume?: Resume;
}

export interface Job {
  id: number;
  companyId: number;
  companyName: string;
  companyLogoUrl?: string;
  companyIndustry?: string;
  companyVerified: boolean;
  recruiterId: number;
  recruiterName: string;
  title: string;
  description: string;
  requiredSkills: string[];
  minExperience?: number;
  maxExperience?: number;
  minSalary?: number;
  maxSalary?: number;
  currency: string;
  location: string;
  employmentType: EmploymentType;
  workMode: WorkMode;
  openings: number;
  applicationDeadline: string;
  status: JobStatus;
  totalApplicationsCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface Interview {
  id: number;
  applicationId: number;
  jobId: number;
  jobTitle: string;
  companyName: string;
  candidateId: number;
  candidateName: string;
  interviewerId: number;
  interviewerName: string;
  interviewType: InterviewType;
  interviewDate: string;
  startTime: string;
  endTime: string;
  meetingLink?: string;
  location?: string;
  notes?: string;
  status: InterviewStatus;
  createdAt: string;
}

export interface Application {
  id: number;
  candidateId: number;
  candidateName: string;
  candidateEmail: string;
  candidatePhone?: string;
  jobId: number;
  jobTitle: string;
  companyId: number;
  companyName: string;
  resumeId?: number;
  resumeFileName?: string;
  coverLetter?: string;
  status: ApplicationStatus;
  recruiterNotes?: string;
  rejectionReason?: string;
  interviews?: Interview[];
  appliedAt: string;
  updatedAt: string;
}

export interface NotificationItem {
  id: number;
  title: string;
  message: string;
  isRead: boolean;
  link?: string;
  createdAt: string;
}

export interface AdminStats {
  totalUsers: number;
  totalCandidates: number;
  totalRecruiters: number;
  totalCompanies: number;
  pendingCompanies: number;
  verifiedCompanies: number;
  activeJobs: number;
  totalApplications: number;
  shortlistedApplications: number;
  scheduledInterviews: number;
  selectedCandidates: number;
}

export interface RecruiterStats {
  activeJobs: number;
  totalApplications: number;
  underReviewApplications: number;
  shortlistedApplications: number;
  scheduledInterviews: number;
  selectedCandidates: number;
  rejectedCandidates: number;
}

export interface ApiResponse<T> {
  timestamp: string;
  success: boolean;
  message?: string;
  data: T;
}

export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}
