# Sprint 6 – Sprint Planning Document

**Project:** Edujournal – Teacher's Gradebook and Report Card System  
**Sprint Duration:** 4.11.2026 – 17.11.2026  
**Team:** Bayram Erdogan, Grigorii Tishchuk, Olena Petrova, Maria Kuznetsova  
**Scrum Master:** Olena Petrova  

---

## 1. Sprint Goal

The primary goal of Sprint 6 is to extend localization support to the database layer while improving overall code quality, maintainability, and test readiness of the EduJournal application. The sprint focuses on database localization, statistical code review using SonarQube, code clean-up and refactoring, acceptance test planning, and updating architecture documentation.

---

## 2. Sprint Objectives

The main objectives of Sprint 5 are:

- Design and implement a localization strategy for multilingual database content.
- Extend the database structure and data access layer to support localized records.
- Verify correct UTF-8 encoding and locale-aware data retrieval.
- Update the project documentation and README with database localization details.
- Perform statistical code review using SonarQube and SonarScanner.
- Analyze project metrics including coverage, complexity, code duplication, and maintainability.
- Identify code smells, duplicated logic, and areas requiring refactoring.
- Refactor and simplify complex classes and methods.
- Improve code readability through documentation, JavaDoc comments, and coding standard compliance.
- Execute unit tests and end-to-end tests to ensure system stability after refactoring.
- Prepare a formal Acceptance Test Plan covering functional, usability, performance, and reliability testing.
- Update architecture documentation, including ER diagrams and UML models.
- Update GitHub repository documentation and project artefacts.

---

## 3. Sprint Backlog

The following Product Backlog Items are planned for Sprint 6:

| Product Backlog Item | Priority | Story Points |
|---|:--------:|:------------:|
| Database Localization Design and Implementation |   High   |      5       |
| Localization Data Model and Database Schema Updates |   High   |      3       |
| UTF-8 Validation and Multilingual Data Testing |   High   |~~      2       |
| Static Code Analysis using SonarQube and SonarScanner |   High   |      4       |
| Code Metrics Collection and Review Documentation |   High   |      3       |
| Refactoring and Code Clean-Up |   High   |      5       |
| JavaDoc and Documentation Improvements |  Medium  |      2       |
| Acceptance Test Planning and Design |  Medium  |      3       |
| ER Diagram and UML Documentation Update |   Medium   |      2       |
| GitHub README and Project Documentation Update |  Medium  |      2       |
| Product Backlog Update (Trello/Jira) |  Medium  |      1       |
| **Total** |          |  **32 SP**   |

---

## 4. Roles and Responsibilities

| Team Member | Responsibilities                                                                                                                                                      |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Olena Petrova**<br>*(Scrum Master)* | Sprint coordination, SonarQube analysis, static code review, code clean-up, acceptance test design, UI verification, architecture documentation, sprint reporting, backlog management. |
| **Maria Kuznetsova** | Database localization implementation, static code review, code clean-up, multilingual database testing, ER diagram updates.                                                                             |
| **Bayram Erdogan** | Static code review, code clean-up, testing and validation.                                                                                                                                                |
| **Grigorii Tishchuk** | Static code review, code clean-up, static code review, code clean-up.                                                                                                                                  |

---

## 5. Expected Deliverables

At the end of Sprint 6, the team expects to deliver:

- Database localization implementation supporting multilingual database content.
- Updated database model and localization documentation.
- SonarQube analysis results with screenshots and statistical metrics.
- Statistical Code Review Report.
- Refactored and standardized codebase following Java coding conventions.
- Updated JavaDoc documentation and code comments.
- Successful execution of all unit and end-to-end tests.
- Formal Acceptance Test Plan document.
- Updated ER diagrams and UML documentation stored in the /docs directory.
- Updated project README describing localization architecture and implementation.
- Updated Product Backlog and Sprint documentation.

---

## 6. Team Capacity and Assumptions

### Team Capacity

The estimated team capacity for Sprint 6 is:

**4 members × 3 hours/day × 8 working days = approximately 96 hours**

### Assumptions and Risks

- Database localization structure will remain compatible with the current application architecture.
- UTF-8 encoding will be correctly handled by the database, application, and reporting components.
- Refactoring activities will not introduce regressions due to comprehensive test coverage.
- SonarQube metrics will be available and reproducible across development environments.
- Existing architecture diagrams can be updated without significant redesign effort.

---

## 7. Definition of Done

A task will be considered **Done** when:

- [ ] Database localization has been implemented and documented.
- [ ] Multilingual database content is correctly displayed in supported languages.
- [ ] SonarQube analysis has been executed and documented.
- [ ] Identified code issues have been addressed through refactoring.
- [ ] JavaDoc comments and documentation have been updated where required.
- [ ] Unit tests and E2E tests pass successfully.
- [ ] Acceptance Test Plan has been completed.
- [ ] ER diagrams and UML documentation have been updated in /docs.
- [ ] Changes have been committed and pushed to GitHub.
- [ ] Trello/Jira backlog and project documentation have been updated.
