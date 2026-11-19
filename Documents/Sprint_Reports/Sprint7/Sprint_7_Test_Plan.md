# EduJournal - Sprint 7 Test Plan

**Project:** EduJournal - Teacher's Gradebook and Report Card System  
**Sprint:** 7 - Functional and Non-functional Testing  
**Team:** Lucky7  
**Test plan owner:** Scrum Master / QA coordinator  
**Status:** Planned  
**Related documentation:** [Features](../../Features.md), [Database Test](../../Diagrams/DatabaseTest.md), [UI Localization](../../Localization/UI_Localization.md), [DB Localization](../../Localization/DB_Localization.md)

## 1. Objective

The objective of this test plan is to verify that the final EduJournal release is functional, usable, secure, localized, and deployable. Testing will confirm that the implemented features meet the product vision and acceptance criteria, that role permissions protect data, that grade and report calculations are correct, and that the application remains stable in the supported local and Docker environments.

The plan covers the Sprint 7 requirements for:

- functional testing of administrator, teacher, and student workflows;
- regression testing after code cleanup and localization changes;
- unit, integration, and end-to-end testing;
- usability and heuristic evaluation;
- performance and basic security checks;
- CI/CD, Docker build, JaCoCo coverage, and SonarQube quality analysis;
- evidence collection for the Sprint 7 report, UAT, and defect log.

This document defines the tests to execute. Actual pass/fail results, screenshots, and resolved defects must be recorded in the Sprint 7 review report and the issue tracker.

## 1.1 Relationship to the previous Acceptance Test Plan

The Acceptance Test Plan dated 17.11.2026 is compatible with this document and is used as the UAT/acceptance view of the Sprint 7 tests. Its acceptance areas are covered by the functional catalogue below, while this plan additionally defines unit, DAO/integration, security, CI/CD, static-analysis, deployment, heuristic, and evidence requirements.

The following implementation-specific interpretation keeps the acceptance plan aligned with the repository:

| Acceptance-plan item | Sprint 7 test-plan treatment |
|---|---|
| User registration (US-01 / FT-01) | The repository implements administrator-created student and teacher accounts, not a public self-registration flow. Test F-05 covers the implemented account-management workflow. Do not report public registration as implemented unless the feature is added. |
| Profile management (US-03 / FT-03) | Supported by `OwnProfilePage`: test F-21 verifies viewing and saving the editable profile fields allowed for each role. |
| Grade calculation example (FT-10) | The repository documents a 1-5 weighted grade scale. The acceptance example using 80, 90, and 100 and expecting 92 must be converted to the project's grade scale or marked as not applicable; do not use it as evidence for the current implementation without Product Owner approval. |
| Performance limits (AC-14 / PT-01--PT-03) | The 3-second, 2-second, and 5-second values are acceptance targets from the previous plan, not measured repository results. Record them as proposed thresholds and obtain Product Owner confirmation before declaring them passed. Existing JMH benchmarks currently provide the measurement mechanism. |
| One-hour reliability (AC-15 / PT-04) | Add the manual soak test R-01 below. It is not replaced by the existing unit or JMH tests. |

## 2. System under test and scope

### 2.1 In scope

1. **Authentication and authorization**
   - Login with valid and invalid credentials.
   - Input validation and error messages.
   - Password hashing and session handling.
   - Role-based navigation and teacher/student/admin access restrictions.
2. **Administrator workflows**
   - Create, edit, delete, search, and filter student and teacher accounts.
   - Create and manage academic groups and student membership.
   - Create and manage courses, teachers, groups, and dates.
   - Dashboard statistics and student report-card access.
3. **Teacher workflows**
   - View assigned courses and students.
   - Add and manage assessments and weights.
   - Enter and update grades.
   - Calculate final grades and display incomplete grades correctly.
   - Export gradebooks to CSV and PDF.
4. **Student workflows**
   - View enrolled courses, credits, averages, assessments, and report cards.
   - Confirm that student data is read-only.
5. **Localization**
   - English, Russian, Turkish, and Arabic UI text.
   - UTF-8 data, translated database labels, locale persistence, and Arabic RTL layout.
6. **Quality attributes**
   - Performance benchmarks for login, lists, data retrieval, course, assessment, and grade creation.
   - Basic security checks for authentication, authorization, input validation, SQL injection, and password storage.
   - Usability, accessibility, error handling, and visual consistency.
   - Build reproducibility, Docker packaging, CI test execution, JaCoCo, and SonarQube.

### 2.2 Out of scope

- Production-scale availability, disaster recovery, and network load testing.
- Penetration testing beyond the basic security checks listed in this plan.
- Testing unsupported operating systems or database engines.
- Testing features that are not implemented or documented in the current repository.

## 3. Test approach

| Test level/type | Main purpose | Repository evidence or tool |
|---|---|---|
| Unit | Verify isolated services, utilities, entities, mappers, validators, and grade calculations | JUnit 5, Mockito, `src/test/java/com/edujournal/backend`, `utils`, `entity`, and `model` |
| Integration / DAO | Verify persistence, CRUD operations, localization data, and database relationships | JUnit 5, MariaDB, `src/test/java/com/edujournal/dao` |
| End-to-end | Verify complete user journeys through the application | TestFX, `src/test/java/com/edujournal/e2e` |
| Regression | Confirm existing behavior after cleanup and localization changes | Full non-E2E Maven test suite plus selected E2E scenarios |
| Performance | Measure response time and stability for common operations | JMH tests under `src/test/java/com/edujournal/performance`, `mvn clean verify -Pperformance` |
| Security | Verify authentication, authorization, validation, password handling, session isolation, and injection resistance | Security tests under `src/test/java/com/edujournal/security` |
| Localization and usability | Check translated content, UTF-8 rendering, RTL layout, labels, forms, and user feedback | Manual exploratory testing in all four locales and heuristic checklist |
| Build and deployment | Verify CI, coverage, static analysis, and container packaging | Jenkins, Maven, JaCoCo, SonarQube, Docker |

Automated tests are the primary regression check. Manual testing is required for JavaFX rendering, localization layout, accessibility, PDF/CSV output, heuristic evaluation, and Docker GUI startup.

## 4. Test resources

### 4.1 People and responsibilities

| Role | Responsibility |
|---|---|
| Scrum Master / QA coordinator | Maintain this plan, coordinate execution, review evidence, maintain the defect log, and report sprint quality status |
| Developer responsible for a feature | Fix assigned defects, add or update automated tests, and provide technical evidence |
| Tester / reviewer | Execute independent functional, exploratory, usability, and regression scenarios |
| Product Owner / UAT participant | Confirm that acceptance criteria and user workflows meet the original requirements |
| All team members | Review results, update Trello cards, and ensure their assigned in-class tasks are documented |

### 4.2 Test data

- A clean `edujournal` MariaDB database initialized by `DatabaseInitializer`.
- Default admin account created by the application initializer.
- Dedicated test accounts for one administrator, one teacher, and at least two students.
- At least one academic group, one course, multiple assessments with different weights, and students with complete and incomplete grades.
- Test values containing Turkish characters (`ğ`, `ş`, `ı`), Cyrillic text, Arabic text, boundary grade values, empty values, duplicate values, and invalid dates.
- No real personal data or production credentials may be used.

### 4.3 Test environment

| Component | Required setup |
|---|---|
| Operating system | Team development OS or Jenkins agent; record OS and version with each test run |
| Java | JDK 21 runtime/build environment |
| Build | Maven 3.x, UTF-8 source encoding |
| Database | MariaDB on port 3306, database `edujournal`, configured through `.env` |
| UI | JavaFX 21; a working display for TestFX and manual GUI tests |
| CI/CD | Jenkins with Maven, Docker, JUnit result publishing, JaCoCo, and SonarQube access |
| Container | Docker engine; MariaDB container available before launching the GUI image |
| Browsers | Not applicable; EduJournal is a JavaFX desktop application |

Before testing, verify the database connection using the documented database test procedure and confirm that test credentials are available. Secrets must be supplied through environment variables or Jenkins credentials and must not be committed.

## 5. Entry and exit criteria

### 5.1 Entry criteria

- The candidate branch builds successfully with Maven.
- The database and test data are available.
- The target build contains the current Sprint 6 localization and cleanup changes.
- Test accounts, display configuration, and required Jenkins/Docker/SonarQube credentials are available.
- Test cases and expected results have been reviewed by at least one team member.

### 5.2 Exit criteria

Testing may be closed when:

- all planned critical and high-priority functional scenarios have passed, or have an approved documented workaround;
- no open Critical or High defect blocks login, role isolation, data persistence, grade calculation, report generation, or localization;
- all failed tests have a linked issue, severity, owner, evidence, and retest result;
- the Maven test suite and Jenkins build are successful;
- JaCoCo and SonarQube reports are generated and the quality gate is A, or at minimum B as required by the course;
- Docker image build and application startup have been verified;
- UAT, heuristic evaluation, and test evidence are attached to the Sprint 7 report;
- Trello cards, GitHub documentation, and the defect log reflect the final status.

## 6. Functional test catalogue

The following catalogue is the minimum execution set. Testers should add a linked test case when exploratory testing finds a new risk.

| ID | Area | Scenario | Expected result | Priority |
|---|---|---|---|---|
| F-01 | Authentication | Login with valid admin, teacher, and student credentials | Each user reaches only the correct dashboard and role navigation | Critical |
| F-02 | Authentication | Login with invalid, empty, and malformed input | Login is rejected with a clear localized message; application remains stable | High |
| F-03 | Authorization | Attempt to open another role's functions or modify read-only student data | Access is denied and no unauthorized data changes occur | Critical |
| F-04 | Session | Log out, switch user, and restart the application | Previous session data is not exposed to the next user | High |
| F-05 | Admin users | Add, edit, delete, search, and filter student/teacher accounts | Valid changes persist; invalid or duplicate data is rejected | High |
| F-06 | Admin groups | Create a group and assign/remove students | Membership is persisted and displayed consistently | High |
| F-07 | Admin courses | Create a course with valid and invalid dates, teacher, and group | Valid course is saved; invalid required fields and dates are rejected | High |
| F-08 | Admin dashboard | Open dashboard after data changes | Student, teacher, course, and group counts are accurate | Medium |
| F-09 | Teacher course view | Open assigned course and student list | Only assigned course data is shown and loads without errors | Critical |
| F-10 | Assessments | Add, edit, and remove assessment types, weights, and names | Valid assessment data persists and is localized correctly | High |
| F-11 | Grades | Enter and update grades at lower, upper, decimal, and invalid boundaries | Valid grades are saved; invalid values are rejected with feedback | Critical |
| F-12 | Grade calculation | Calculate a weighted grade from multiple assessments | Result matches the defined weighted-average rule on a 1-5 scale | Critical |
| F-13 | Incomplete grades | Leave one required grade empty | Final grade shows the documented dash/incomplete state, not a misleading number | Critical |
| F-14 | Teacher reports | Export a course gradebook to CSV and PDF | Files are generated, readable, correctly labeled, and contain the displayed data | High |
| F-15 | Student dashboard | View enrolled courses, credits, and average | Values match persisted enrollment and grade data | High |
| F-16 | Student report | View assessment details and final grades | Report is complete, read-only, and consistent with teacher data | Critical |
| F-17 | Localization | Switch between English, Russian, Turkish, and Arabic without restart | Labels, buttons, tables, dialogs, and messages update to the selected locale | High |
| F-18 | RTL and encoding | Use Arabic UI and enter/read Unicode data in all supported scripts | Arabic layout is RTL; all supported characters render and persist without corruption | High |
| F-19 | Locale persistence | Change preferred locale, log out, and log in again | The user's selected locale is restored | Medium |
| F-20 | Regression | Repeat representative admin, teacher, and student journeys after fixes | Previously passing functionality remains passing | Critical |
| F-21 | Profile | Open own profile, edit permitted fields, save, and reopen the page | Allowed changes persist and read-only fields remain protected according to role | High |

For each executed case, record: date, tester, build/commit, preconditions, actual result, pass/fail status, screenshot or log path, and linked issue if failed.

## 7. Non-functional test tasks

### 7.1 Usability and heuristic evaluation

Evaluate the final UI using the ten Nielsen heuristics: visibility of system status, match with the real world, user control, consistency, error prevention, recognition rather than recall, flexibility, minimalist design, error recovery, and help/documentation. Review login, each role dashboard, forms, tables, dialogs, language selector, and exports.

For every finding record the screen, heuristic, evidence, severity (0-4), recommendation, owner, and retest status. Also check keyboard focus, readable contrast, clear required fields, non-truncated translated text, meaningful error messages, and usable Arabic RTL alignment.

### 7.2 Performance and stability

Run the existing JMH profile for login, teacher/student lists, student data, course creation, assessment creation, and grade creation:

```bash
mvn clean verify -Pperformance
```

Save the generated files in `performance-results/`. Record the machine, database state, warm-up/measurement configuration, average score, and any outliers. Compare results with the previous baseline where available. Investigate regressions, repeated database calls, UI freezes, and failures under a representative dataset; do not claim a performance pass without recorded measurements.

For UAT, the previous Acceptance Test Plan proposes these targets: student list (1,000 records) within 3 seconds, grade search within 2 seconds, and report generation within 5 seconds. Execute these only with a documented 1,000-record/large-dataset fixture and record the Product Owner's approval of the thresholds. The current repository's existing performance fixtures are not, by themselves, evidence that the 1,000-record targets have been met.

### 7.2.1 Reliability and data integrity acceptance tests

| ID | Scenario | Expected result | Evidence |
|---|---|---|---|
| R-01 | Use normal administrator, teacher, and student workflows continuously for one hour | No crash, freeze, unexpected error, or session/data corruption occurs | Start/end time, environment, test data, logs, and observations |
| R-02 | Perform repeated create, update, delete, assignment, and grade operations, then reload affected views | Records remain consistent and no unrelated records are changed | Before/after screenshots, database check, and linked test cases |

These are manual acceptance checks and must not be marked passed from unit-test results alone.

### 7.3 Security

Execute the repository security tests and manually verify:

- passwords are never stored or logged in plain text;
- invalid credentials do not reveal whether a username exists;
- teacher, student, and admin permissions are enforced server/service-side as well as in the UI;
- input validation rejects empty, malformed, out-of-range, and injection-like values;
- SQL injection payloads do not alter queries or bypass login;
- logout and user switching clear the prior session;
- test credentials and database secrets are absent from committed files and reports.

Security findings must be linked to a defect and retested after remediation.

### 7.4 CI/CD, static analysis, and deployment

1. Run the normal Maven test/build command and confirm JUnit XML results are produced.
2. Run the Jenkins pipeline and verify checkout, environment setup, tests, coverage publication, Docker build, and artifact publication.
3. Run JaCoCo and archive the generated report.
4. Run SonarQube analysis for the same commit and archive the dashboard screenshot/report. Confirm the quality gate is A or, at minimum, B for all reported grades.
5. Build the Docker image and start it with a configured MariaDB container and display. Verify that the JavaFX application launches and can complete a smoke workflow.
6. Record commit SHA, Jenkins build number, Docker image tag, test counts, coverage, SonarQube result, and deployment result in the Sprint 7 report.

## 8. Defect and issue tracking

All failures are logged in Trello or GitHub Issues and summarized in the Sprint 7 report. Use the following minimum fields:

| Field | Required value |
|---|---|
| Defect ID | Unique ID, for example `S7-BUG-001` |
| Date and reporter | Date, tester, and build/commit |
| Area and test ID | Feature area and linked F/U/NF test case |
| Summary and steps | Short reproducible description and numbered steps |
| Expected / actual result | Observable difference |
| Severity | Critical, High, Medium, or Low |
| Priority and owner | Fix order and responsible team member |
| Evidence | Screenshot, log, exported file, or report link |
| Status | New, Triaged, In Progress, Fixed, Ready for Retest, Verified, or Reopened |
| Resolution | Fix description, commit, and retest result |

Severity guidance: **Critical** means data loss, unauthorized access, unusable login, or incorrect grades/reports; **High** means a major workflow is blocked or data is not persisted; **Medium** means a workaround exists; **Low** means cosmetic or minor usability impact.

## 9. Traceability and evidence

| Requirement area | Evidence to attach |
|---|---|
| Functional requirements | Completed functional test table, automated test report, screenshots, and defect links |
| Localization | Screenshots for all four locales, including Arabic RTL and Unicode data |
| Performance | `performance-results` outputs and environment/baseline notes |
| Security | Security test results and remediation/retest links |
| Usability | Heuristic evaluation table with severity and recommendations |
| CI/CD | Jenkins build URL/number, JUnit results, JaCoCo report, SonarQube screenshot |
| Deployment | Docker build log, image tag, startup/smoke-test evidence |
| UAT | Signed or named acceptance scenarios, result, feedback, and Product Owner decision |

## 10. Execution log

Complete this table during the sprint rather than filling it with planned results.

| Execution date | Build/commit | Test scope | Tester | Passed | Failed | Blocked | Evidence/link |
|---|---|---|---|---:|---:|---:|---|
| TBD | TBD | Unit and integration | TBD | - | - | - | - |
| TBD | TBD | E2E and functional smoke | TBD | - | - | - | - |
| TBD | TBD | Localization and heuristic evaluation | TBD | - | - | - | - |
| TBD | TBD | Performance and security | TBD | - | - | - | - |
| TBD | TBD | Jenkins, SonarQube, and Docker | TBD | - | - | - | - |

## 11. Risks and mitigation

| Risk | Impact | Mitigation |
|---|---|---|
| MariaDB or environment configuration differs between machines | Tests fail for environmental reasons | Use the documented `.env` setup, isolated test data, and record environment versions |
| JavaFX/TestFX requires a working display | E2E tests cannot start | Run GUI tests on a configured desktop/Jenkins agent and keep backend tests display-independent |
| Localization text is truncated or incorrectly aligned | Usability and acceptance failure | Test every supported locale on every major screen, especially Arabic RTL |
| Incomplete grade data produces misleading reports | Incorrect academic results | Include complete and incomplete datasets and verify the documented dash behavior |
| CI and Docker credentials are unavailable | Pipeline/deployment cannot be verified | Request credentials before execution; never place secrets in the repository |
| A fix causes a regression in another role | Cross-role data or permission failure | Re-run the regression smoke set after every High/Critical fix |

## 12. Approval

| Role | Name | Date | Approval |
|---|---|---|---|
| Test plan owner / Scrum Master | Bayram Erdogan | TBD | Pending |
| Product Owner / lecturer | TBD | TBD | Pending |
| Technical reviewer | TBD | TBD | Pending |
