# Software Engineering Project 1 - Sprint 3 Review Report

## Sprint 3 Review – Edujournal

- **Project:** Edujournal – Teacher's Gradebook and Report Card System
- **Sprint Duration:** 15.09.2026 – 27.09.2026
- **Team Members:** Bayram Erdogan, Grigorii Tishchuk, Olena Petrova, Maria Kuznetsova
- **Scrum Master:** Maria Kuznetsova

---

## Sprint 3 Goal

The main goal of Sprint 3 was to extend the functional prototype of **Edujournal**, establish a Jenkins CI/CD pipeline, enhance automated testing with code coverage reporting, and create a local Docker image of the application.

The sprint focused on implementing the remaining core features from the product backlog, integrating DevOps tooling, and ensuring the application was ready for a functional review. The main areas of work were:

- Feature implementation: authentication, grade management, report card, admin dashboard
- Jenkins CI/CD pipeline integration
- Unit and end-to-end test suite expansion
- JaCoCo code coverage reporting
- Local Docker image build and test
- Documentation update

---

## Completed User Stories / Tasks

### 1. Extend Functional Prototype

The team implemented the remaining and more complex features from the product backlog.

**Implemented features:**

- **User authentication and authorization:** Secure login with username/password validation, BCrypt password hashing via jBCrypt, role-based navigation routing each user to the correct dashboard (Administrator, Teacher, Student). Reset password functionality added.
- **Data validation and input sanitization:** Input validation on login and user creation forms to prevent invalid or empty submissions.
- **Enhanced search and filtering:** Search and filter functionality implemented across admin views for students, teachers, courses, and academic groups.
- **Reporting and analytics dashboard for administrators:** Admin dashboard displays key statistics — total students, teachers, courses, and academic groups. Individual student report cards accessible from the student list.
- **Student report card:** Per-course grade tables showing all assessments and entered grades, a Final Grade column calculated from weighted assessments, and an overall average grade displayed at the top of the report.
- **Grade calculation logic:** Weighted grade calculation (scale 1–5) based on assessment types and weights; dash (—) shown when grades are incomplete.
- **CSV and PDF export:** Gradebook and course reports can be exported in CSV and PDF formats.

**Core functionality validation:**

- End-to-end testing of user workflows was performed using TestFX.
- Unit tests executed for grade calculation logic and service layer.
- Authentication flow tested for both valid and invalid credential cases.

**Bug fixes:**

- Fixed wrong TopBar title on student report page.
- Fixed repeated database lookup inside assessment loop in student report service.
- Removed unused imports across multiple controller and service classes.
- Fixed hardcoded `localhost` in `DatabaseInitializer` — replaced with `DB_HOST` from environment configuration.

---

### 2. Jenkins CI/CD Integration

A Jenkins CI/CD pipeline was established to automate the build, test, and coverage reporting processes.

**Pipeline stages configured:**

- **Code Checkout:** Pipeline retrieves the latest code from the Git repository.
- **Build:** Project compiled using Maven.
- **Unit Tests:** JUnit tests executed automatically on every commit to the main branch.
- **Code Coverage:** JaCoCo code coverage report generated and published after each build.
- **Environment Configuration:** `.env` file created automatically in the pipeline with required database variables.

The pipeline runs on every commit to the main branch, ensuring early detection of build failures and test regressions.

**JaCoCo Public Report:** https://users.metropolia.fi/~grigorit/devops/jacoco/

---

### 3. Automated Unit & Coverage Testing

The test suite was extended to cover new and existing features.

**Test suite additions:**

- End-to-end tests using TestFX covering: admin login, student creation, teacher creation, academic group creation, course creation, and a full creation workflow.
- Authentication E2E tests for valid and invalid login scenarios.
- Teacher and student E2E tests for viewing courses and report cards.
- Unit tests written for grade calculation edge cases and boundary conditions.

**JaCoCo integration:**

- JaCoCo code coverage analysis continued and integrated into the Jenkins pipeline.
- The pipeline automatically generates and publishes the JaCoCo HTML report after each build.

**Note on test coverage:** While the test suite was significantly extended, some E2E tests required additional adjustments due to test state isolation issues and date format handling. These were identified and partially resolved during the sprint. Full stability of the E2E suite is planned for Sprint 4.

---

### 4. Functional Review Readiness

The application was prepared for a functional review, with all main features implemented and testable.

- All core user workflows function end-to-end.
- All three user roles (Administrator, Teacher, Student) have working dashboards and role-specific functionality.
- Backend and frontend are fully integrated.
- Documentation updated in `Documents/Features.md` and `README.md`.

---

### 5. Docker Image

A local Docker image of the application was created and tested.

- Docker image built using Docker Desktop.
- MariaDB containerized and connected to the application via environment configuration.
- `DB_HOST` externalized through `.env` and `.env.docker` configuration files, allowing the application to connect to both local and containerized databases.
- Image tested locally with JavaFX GUI display via X11 forwarding.

---

### 6. Project Management

- GitHub repository updated with all sprint changes, commits, and pull requests.
- Trello Sprint 3 board maintained throughout the sprint.
- Sprint Review preparation completed.

---

## Sprint 3 Demo Summary

During the Sprint 3 Review, the team will demonstrate:

- User authentication with role-based navigation.
- Admin dashboard with statistics and student report card access.
- Teacher gradebook with grade entry and weighted calculation.
- Student dashboard and report card page.
- Jenkins CI/CD pipeline — build, test, and coverage report stages.
- JaCoCo code coverage report.
- Local Docker image running the application.
- E2E test execution.
- GitHub repository and Trello board progress.

---

## What Went Well

- The Jenkins CI/CD pipeline was successfully configured and integrates smoothly with the Maven build and JUnit tests.
- Authentication with BCrypt was implemented securely and tested end-to-end.
- The student report card was implemented with reusable grade calculation logic shared across the dashboard and report page, avoiding code duplication.
- Average grade calculation was centralized in `StudentReportService`, making it reusable across the student dashboard and report card.
- The team successfully created and tested a local Docker image, and externalized database host configuration to support both local and container environments.
- The test suite was significantly extended with both unit and E2E tests.

---

## What Could Be Improved

- **E2E test stability:** End-to-end tests required extra debugging due to shared application state between test methods and date format handling differences across environments. Full stability will be addressed in Sprint 4.
- **Performance optimization:** Database queries and caching mechanisms were not fully optimized within the sprint timeframe. This is planned for Sprint 4.
- **Security testing:** While basic input validation was implemented, a more thorough security review (e.g., SQL injection, XSS hardening) was not completed due to time constraints. The team worked hard to implement the available mitigations.
- **Docker deployment:** Full deployment automation (e.g., Docker Compose for combined app + database) was not completed in this sprint. Currently the image runs locally with manual steps.
- **Notification configuration for Jenkins:** Email notification for build results was identified as optional and was not configured in this sprint.

---

## Daily Scrum (Stand-up Meetings)

The team used regular stand-ups to monitor progress and coordinate tasks.

Daily Scrum discussions covered:

- Progress on authentication and feature implementation.
- Jenkins pipeline configuration challenges.
- Docker image build and display issues.
- Test suite failures and fixes.
- GitHub updates and pull request reviews.
- Trello task progress.
- Sprint Review preparation.

---

## Team Contributions

| Team Member | Tasks                                                                                                  | Hours Spent | In-class tasks |
|---|--------------------------------------------------------------------------------------------------------|---|---|
| **Bayram Erdogan** | Backend and Testing.                                                                                   | 40 h 10 min | Submitted |
| **Grigorii Tishchuk** | Login and CI/CD setup. | 21 h | Submitted |
| **Olena Petrova** | Backend, Frontend & Integration.                                                                       | 64 h 40 min | Submitted |
| **Maria Kuznetsova**<br>*(Scrum Master)* | Sprint coordination; Trello and meetings; PR reviews; UI, frontend-backend integration; documentation. | 54 h | Submitted |

---

## Next Sprint Focus – Sprint 4

The following areas are planned for Sprint 4:

- Finalize and polish all UI views.
- Resolve remaining E2E test stability issues.
- Complete performance optimization and database query improvements.
- Expand Docker deployment with Docker Compose.
- Conduct a more thorough security review.
- Prepare final project documentation.
- Deliver final project presentation.
