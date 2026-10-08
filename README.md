# SmartHire – Full-Stack Job Recruitment & Applicant Tracking System (ATS)

**SmartHire** is an enterprise-grade, full-stack recruitment platform and applicant tracking system designed to streamline and digitize the end-to-end recruitment lifecycle across candidates, recruiters, and platform administrators. Built with **Spring Boot 3.3.5 (Java 21/25)** on the backend and **React 19 + TypeScript + Tailwind CSS** on the frontend.

---

## 🌟 Key Architecture & Highlights

- **Modular Monolith Architecture**: Clean separation of concerns across Domain Entities, Repositories, Service Business Logic, Controllers, DTOs, and Security Filters.
- **Role-Based Access Control (RBAC)**: Fine-grained security separating `ROLE_CANDIDATE`, `ROLE_RECRUITER`, and `ROLE_ADMIN` using Spring Security 6 and stateless JWT bearer tokens.
- **Relational Integrity & Business Logic Guardrails**:
  - Valid recruitment status state transitions (`APPLIED` ➔ `UNDER_REVIEW` ➔ `SHORTLISTED` ➔ `INTERVIEW_SCHEDULED` ➔ `SELECTED` / `REJECTED`).
  - Interview scheduling conflict detection: prevents overlapping interview slots for the same interviewer.
  - Recruiter authorization: recruiters can only view resumes of candidates who have actively applied to their company.
  - Company verification gate: recruiters cannot publish vacancies until their company is vetted and approved by a platform administrator.
- **Dynamic JPA Specification Search**: Multi-criteria job filtering by keyword, location, work mode (`REMOTE`, `HYBRID`, `ON_SITE`), employment type (`FULL_TIME`, `PART_TIME`, `CONTRACT`, `INTERNSHIP`), experience range, and salary.
- **Resume Management & File Handling**: Upload validation (PDF/DOCX <= 10MB), version history, and primary resume designation.
- **Dynamic PDF Reporting**: PDF dossier generation for candidate profiles and application summaries powered by OpenPDF.
- **Dual Database Profiles**:
  - `h2` (Default): File-backed persistent database (`./data/smarthiredb`) with automatic schema setup and H2 web console.
  - `postgres`: Production-ready PostgreSQL profile configured via `application-postgres.yml`.
- **Interactive OpenAPI / Swagger Documentation**: Full Swagger 3 UI at `/swagger-ui/index.html`.

---

## 👥 Demo Credentials

The database automatically seeds standard demonstration accounts on initial startup:

| Role | Email | Password | Details |
| :--- | :--- | :--- | :--- |
| **System Administrator** | `admin@smarthire.com` | `Admin@123` | Full access to platform metrics, company approvals, user account management |
| **Verified Recruiter** | `recruiter@technova.com` | `Recruiter@123` | Recruiter at TechNova Solutions (Approved); post jobs, manage applicants, schedule interviews |
| **Unverified Recruiter** | `recruiter@apexlabs.com` | `Recruiter@123` | Recruiter at Apex Global Labs (Pending Approval); demonstrates admin verification workflow |
| **Candidate** | `john.doe@example.com` | `Candidate@123` | Full candidate profile with education, experience, skills, applications, and scheduled interview |

> *Tip: The Login page contains one-click demo login buttons to instantly switch between these accounts.*

---

## 🚀 Getting Started

### Prerequisites
- **Java 21 or Java 25 JDK**
- **Apache Maven 3.8+**
- **Node.js 18+ & npm**

---

### 1. Backend Setup & Startup

Navigate to the `backend` folder:
```bash
cd backend
```

Run tests to verify the build:
```bash
mvn test
```

Start the Spring Boot server (defaults to `h2` profile on port `8080`):
```bash
mvn spring-boot:run
```

To run with PostgreSQL instead:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

The backend server will be live at:
- **API Base**: `http://localhost:8080/api`
- **Swagger Documentation**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON Spec**: `http://localhost:8080/v3/api-docs`
- **H2 Web Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/smarthiredb`, User: `sa`, Password: *empty*)

---

### 2. Frontend Setup & Startup

In a separate terminal, navigate to the `frontend` folder:
```bash
cd frontend
```

Install dependencies:
```bash
npm install
```

Start Vite development server:
```bash
npm run dev
```

The web application will be accessible at:
- **Frontend URL**: `http://localhost:5173/`

*(Vite is preconfigured with a proxy that transparently forwards `/api` requests to `http://localhost:8080`)*.

---

## 💼 Core User Workflows

### 1. Job Candidate Workflow
1. Browse public jobs or apply rich filters (Keywords, Location, Experience, Work Mode, Salary).
2. Create/edit professional profile (contact info, bio, skills, education, work experience, portfolio links).
3. Upload resume documents (PDF/DOCX) and designate a primary version.
4. Apply to openings with a single click and attach cover notes.
5. Track application status milestones in real-time (`Applied`, `Under Review`, `Shortlisted`, `Interview Scheduled`, `Selected`, `Rejected`).
6. View interview schedules with meeting links, interviewer contact, and agenda.
7. Download an official Candidate Profile PDF dossier.

### 2. Recruiter Workflow
1. Complete company profile details (industry, logo, website, description, office location).
2. Publish job vacancies with openings, salary range, experience criteria, and skill tags (once verified).
3. Review applicants for each vacancy: view matching scores, download submitted resumes, and write recruiter notes.
4. Advance applicant stages sequentially.
5. Schedule technical, HR, or managerial interviews with built-in time conflict prevention.
6. Make final hiring decisions (`Selected` or `Rejected` with feedback notes).
7. Download full Application Summary PDFs for HR audit records.

### 3. Administrator Workflow
1. Platform Governance Dashboard: monitor total users, candidates, recruiters, companies, active jobs, and interviews.
2. Company Verification Queue: review newly registered companies and approve or reject verification status.
3. User Account Moderation: view all registered users across roles, filter by status, and activate or deactivate accounts.

---

## 🛠️ Tech Stack Summary

- **Backend**:
  - Spring Boot 3.3.5
  - Spring Security 6 + JJWT 0.12.6
  - Spring Data JPA + Hibernate 6.5
  - OpenPDF 2.0.3 (PDF report generation)
  - SpringDoc OpenAPI 2.6.0 (Swagger 3)
  - H2 Database & PostgreSQL Driver
- **Frontend**:
  - React 19 + TypeScript
  - Vite 8
  - Tailwind CSS v4
  - Lucide React Icons
  - Axios HTTP Client with JWT interceptors
  - React Router DOM v7

---

## 📄 License
MIT License. Developed for enterprise recruitment automation.
