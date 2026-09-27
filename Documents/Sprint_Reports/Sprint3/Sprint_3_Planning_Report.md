# Sprint 3 – Sprint Planning Document

**Project:** Edujournal – Teacher's Gradebook and Report Card System  
**Sprint Duration:** 15.09.2026 – 27.09.2026  
**Team:** Bayram Erdogan, Grigorii Tishchuk, Olena Petrova, Maria Kuznetsova  
**Scrum Master:** Maria Kuznetsova

---

## 1. Sprint Goal

The main goal of Sprint 3 is to extend the functional prototype of **Edujournal**, establish a CI/CD pipeline with Jenkins, enhance automated testing with code coverage, and create a local Docker image of the application.

During this sprint, the team will build on the technical foundation from Sprint 2 by implementing the remaining core features, integrating Jenkins to automate the build and test processes, and ensuring that all key functionality is testable and ready for a functional review.

---

## 2. Sprint Objectives

The main objectives of Sprint 3 are:

- Implement user authentication and authorization with role-based navigation.
- Add data validation and input sanitization to prevent security vulnerabilities.
- Implement enhanced search and filtering functionality across admin views.
- Develop a reporting and analytics dashboard for administrators.
- Implement student report card generation with per-course grades and average grade.
- Implement grade calculation with weighted assessments (1–5 scale).
- Add CSV and PDF export for gradebook and course reports.
- Establish a Jenkins CI/CD pipeline with stages: checkout, build, unit tests, JaCoCo coverage.
- Expand the unit test suite to cover new and existing features (JUnit 5, TestFX E2E tests).
- Continue using JaCoCo for code coverage analysis and publish the HTML report.
- Build and test a local Docker image of the project.
- Update project documentation to reflect new features and functionalities.
- Keep GitHub and Trello updated throughout the sprint.
- Prepare the application for the Sprint Review demonstration.

---

## 3. Sprint Backlog

The following Product Backlog Items are planned for Sprint 3:

| Product Backlog Item | Priority | Story Points |
|---|:---:|:---:|
| User Authentication with BCrypt & Input Validation | High | 5 |
| Role-based Navigation (Admin / Teacher / Student) | High | 3 |
| Admin Dashboard with Statistics | High | 3 |
| Student Report Card Page | High | 5 |
| Grade Calculation Logic (Weighted, 1–5 scale) | High | 5 |
| Search & Filter Functionality in Admin Views | Medium | 3 |
| CSV and PDF Export for Gradebook | Medium | 3 |
| Jenkins CI/CD Pipeline Setup | High | 5 |
| Unit Test Suite Extension (JUnit 5 + TestFX) | High | 5 |
| JaCoCo Code Coverage Integration with Jenkins | High | 3 |
| Local Docker Image Build and Test | High | 3 |
| Update Documentation | Medium | 2 |
| GitHub and Trello Maintenance | High | 1 |
| **Total** | | **46 SP** |

---

## 4. Roles and Responsibilities

| Team Member | Responsibilities                                                                                                         |
|---|--------------------------------------------------------------------------------------------------------------------------|
| **Maria Kuznetsova**<br>*(Scrum Master)* | Sprint coordination; Trello and meetings; PR reviews; UI, frontend-backend integration; testing the code, documentation. |
| **Bayram Erdogan** | Backend and Testing.                                                                                                     |
| **Grigorii Tishchuk** | Jenkins CI/CD pipeline; Docker image; JaCoCo.                                                                            |
| **Olena Petrova** | Backend, Frontend & Integration.                                                                                         |

---

## 5. Expected Deliverables

At the end of Sprint 3, the team expects to deliver:

- Working user authentication and authorization with BCrypt password hashing.
- Role-based navigation routing to the correct dashboard for each user role.
- Admin dashboard showing key statistics (students, teachers, courses, groups).
- Fully functional student report card page with per-course grades, final grade, and average grade.
- Grade calculation logic supporting weighted assessments.
- Search and filter functionality across admin student, teacher, course, and group views.
- CSV and PDF export for gradebook reports.
- Configured Jenkins CI/CD pipeline running on every commit to main.
- Expanded JUnit 5 unit tests and TestFX end-to-end tests.
- JaCoCo HTML code coverage report published by the Jenkins pipeline.
- Local Docker image of the application built and tested.
- Updated project documentation and Features.md.
- Updated GitHub repository and Trello Sprint 3 board.
- Sprint Review demonstration materials.

---

## 6. Team Capacity and Assumptions

### Team Capacity

The estimated team capacity for Sprint 3 is:

**4 members × 3 hours/day × 8 working days = approximately 96 hours**

### Assumptions and Risks

- Jenkins setup may require additional configuration time depending on environment differences.
- Docker image may require adjustments for GUI-based JavaFX application display.
- E2E test stability depends on shared test state; isolation between tests may need extra effort.
- Academic workload from other courses may reduce available hours during the sprint.

---

## 7. Definition of Done

A task will be considered **Done** when:

- [ ] The planned functionality has been implemented and meets the backlog requirements.
- [ ] The code compiles and runs successfully.
- [ ] Unit tests are implemented and pass for the relevant functionality.
- [ ] JaCoCo generates a valid code coverage report where applicable.
- [ ] The Jenkins pipeline completes all stages without errors.
- [ ] The Docker image builds and runs successfully on the local machine.
- [ ] The work has been reviewed by at least one other team member.
- [ ] The implementation is committed and pushed to GitHub.
- [ ] The corresponding Trello task is updated.
- [ ] Required documentation is updated.
- [ ] The functionality is ready to be demonstrated during the Sprint Review.

---

## 8. Sprint Review Preparation

Before the Sprint Review, the team will ensure that:

- [ ] Authentication and role-based navigation can be demonstrated.
- [ ] The admin dashboard with statistics can be shown.
- [ ] The student report card can be demonstrated.
- [ ] Grade calculation with weighted assessments can be explained.
- [ ] The Jenkins CI/CD pipeline can be demonstrated live.
- [ ] JaCoCo code coverage results can be presented.
- [ ] The local Docker image can be demonstrated.
- [ ] Unit tests and E2E tests can be executed and shown.
- [ ] GitHub contains the latest project changes.
- [ ] Trello reflects the final status of Sprint 3 tasks.
- [ ] Each team member is prepared to explain their individual contribution.

---

## 9. Sprint 3 Success Criteria

Sprint 3 will be considered successful if the team has extended the functional prototype with critical features, established a working CI/CD pipeline, and produced a testable local Docker image:

**Authentication → Features → Testing → CI/CD Pipeline → Docker Image**
