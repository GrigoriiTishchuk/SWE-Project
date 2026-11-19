# Sprint 7 - Sprint Planning Report

**Project:** EduJournal - Teacher's Gradebook and Report Card System  
**Sprint:** 7 - Functional and Non-functional Testing  
**Sprint duration:** 17.11.2026 – 1.12.2026                        
**Team:** Bayram Erdogan, Grigorii Tishchuk, Olena Petrova, Maria Kuznetsova  
**Scrum Master:** Bayram Erdogan  


## 1. Sprint goal

Validate that EduJournal is functional, usable, localized, secure, and ready for delivery. The sprint will use automated and manual testing to verify the existing administrator, teacher, and student workflows, grade calculations, report generation, database persistence, localization, and Docker deployment.

## 2. Sprint objectives

- Execute unit, DAO/integration, end-to-end, regression, security, and performance tests.
- Validate administrator, teacher, and student acceptance scenarios.
- Verify English, Russian, Turkish, and Arabic UI localization, including Arabic RTL layout and UTF-8 data.
- Perform heuristic usability evaluation and user acceptance testing.
- Record, prioritize, fix, and retest discovered defects.
- Run Maven and Jenkins builds, publish JUnit and JaCoCo evidence, and perform the required SonarQube quality analysis.
- Build and smoke-test the Docker image with the MariaDB environment.
- Update Trello, GitHub documentation, and the Sprint 7 review evidence.

## 3. Sprint backlog

| Backlog item | Priority | Deliverable |
|---|:---:|---|
| Prepare and execute the Sprint 7 test plan | High | Completed test cases and execution log |
| Functional and regression testing | Critical | Results for admin, teacher, student, authentication, grades, reports, and localization |
| Unit and integration test verification | High | Maven/JUnit results and linked defects |
| UAT and heuristic evaluation | High | Acceptance results and heuristic findings |
| Performance and reliability testing | Medium | JMH results, reliability observations, and data-integrity checks |
| Security testing | High | Security test results and remediation/retest evidence |
| Jenkins, JaCoCo, SonarQube, and Docker validation | High | Build, quality, coverage, and deployment evidence |
| Defect tracking and retesting | Critical | Updated Trello/GitHub issue records |
| Sprint 7 documentation and review report | High | Updated README and completed review report |

## 4. Roles and responsibilities

| Team member | Planned responsibility |
|---|---|
| **Bayram Erdogan** *(Scrum Master)* | Sprint coordination, test-plan ownership, defect tracking, acceptance coordination, and final documentation |
| **Grigorii Tishchuk** |  |
| **Olena Petrova** |  |
| **Maria Kuznetsova** |  |

Responsibilities may be adjusted in Trello while preserving ownership of reported defects and evidence.

## 5. Expected deliverables

- Completed Sprint 7 test execution log.
- Functional, UAT, heuristic, performance, security, and reliability results.
- Defect/issue log with severity, owner, evidence, fix, and retest status.
- Successful Maven/Jenkins evidence where the environment is available.
- JaCoCo and SonarQube reports/screenshots.
- Docker build and application smoke-test evidence.
- Updated README, Trello backlog, GitHub issues, and Sprint 7 review report.

## 6. Team Capacity and Assumptions

### Team Capacity

The Sprint 7 team capacity is **TBD**. Confirm the planned working days and hours with the team in Trello before the sprint review.

### Assumptions and Risks

- MariaDB, the configured test data, and the `.env` settings will be available for database and integration tests.
- A working JavaFX display will be available for TestFX, manual UI checks, and Docker GUI smoke testing.
- The four repository-supported locales (English, Russian, Turkish, and Arabic) will be available for localization testing; Japanese is not a supported project locale.
- UTF-8 data and localized database labels will remain readable and consistent across the supported locales.
- Jenkins, Docker, and SonarQube access may depend on the team's available infrastructure; unavailable activities will be recorded as blocked rather than reported as passed.
- Performance acceptance targets from the previous plan will only be assessed after the test dataset, measurement method, and Product Owner approval are documented.

## 7. Definition of done

- [ ] Planned critical and high-priority scenarios are executed.
- [ ] Every failed test has a linked issue and evidence.
- [ ] Critical and High defects are fixed or explicitly accepted by the Product Owner.
- [ ] Fixes are retested and regression checks are completed.
- [ ] Localization, Arabic RTL, UTF-8 persistence, and report outputs are checked.
- [ ] UAT and heuristic evaluation are documented.
- [ ] Performance and reliability results are measured rather than assumed.
- [ ] Maven/Jenkins, JaCoCo, SonarQube, and Docker evidence is attached where executed.
- [ ] Trello, GitHub documentation, and this sprint's review report are updated.
