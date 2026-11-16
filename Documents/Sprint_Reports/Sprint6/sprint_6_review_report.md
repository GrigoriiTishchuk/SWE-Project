# Software Engineering Project 2 - Sprint 6 Review Report

## Sprint 6 Review – Edujournal (Database Localization, Code Review, Refactoring and Acceptance Planning)

- **Project:** Edujournal – Teacher's Gradebook and Report Card System
- **Sprint Duration:** 4.11.2026 – 17.11.2026
- **Team Members:** Bayram Erdogan, Grigorii Tishchuk, Olena Petrova, Maria Kuznetsova
- **Scrum Master:** Olena Petrova

---

## 1. Sprint 6 Goal

The goal of Sprint 6 was to extend localization support into the database layer while improving overall code quality, maintainability, and project readiness for acceptance. The sprint focused on database localization, static code analysis, code refactoring, acceptance test planning, and updating software architecture documentation.

---

## 2. Sprint Outcome Summary

- Sprint 6 was successfully completed and all planned backlog items were delivered.
- Database localization support was designed and implemented to enable retrieval of localized content according to the active language setting.
- UTF-8 database configuration and multilingual data handling were verified across all supported languages.
- Static code analysis was performed using SonarQube and SonarScanner.
- Statistical code review metrics such as code coverage, complexity, duplicate code, and maintainability indicators were collected and analyzed.
- Identified code quality issues were addressed through refactoring and code clean-up activities.
- JavaDoc documentation and inline comments were added and improved throughout the codebase.
- Unit tests and end-to-end (E2E) tests were executed and updated after refactoring activities to ensure application stability.
- A formal Acceptance Test Plan was prepared, covering functional, usability, performance, and reliability requirements.
- Architecture documentation, including ER diagrams and UML models, was updated and committed to the GitHub repository.
- A total of 32 Story Points planned for the sprint were completed.

---

## 3. Completed User Stories / Tasks

### Database Localization 

- Designed and implemented a localization approach for multilingual database content.
- Updated data access components to support locale-dependent information retrieval.
- Verified UTF-8 encoding compatibility across database records and application layers.
- Validated successful retrieval and display of localized data in English, Russian, Turkish, and Arabic.

### Statistical Code Review

- Performed static code analysis using SonarQube and SonarScanner.
- Collected and reviewed metrics related to:
  - Code coverage
  - Cyclomatic complexity
  - Maintainability
  - Duplicate code
  - Code smells
- Documented findings and identified areas requiring refactoring

### Code Clean-Up and Refactoring

- Refactored complex classes and methods to improve readability and maintainability.
- Removed duplicated logic and redundant code structures.
- Simplified conditional expressions and extracted reusable constants.
- Improved naming consistency throughout the codebase.
- Added and updated JavaDoc documentation for classes and methods.
- Re-ran automated tests to verify that no regressions were introduced.

### Acceptance Test Planning

- Defined acceptance criteria based on system requirements and previous sprint deliverables.
- Designed acceptance test scenarios covering:
  - Functional requirements
  - Usability requirements
  - Reliability requirements
  - Performance requirements
- Created a test coverage matrix connecting requirements to planned acceptance tests.

### Architecture Documentation

- Updated ER diagrams to reflect localization-related database structures.
- Updated UML diagrams to reflect architectural changes introduced during localization and refactoring.
- Added updated design artefacts to the project documentation directory.

### Project Management and Documentation

- Updated Product and Sprint Backlogs in Trello.
- Updated project documentation and GitHub repository contents.
- Added localization and architecture documentation to the README and documentation folders.

---

## 4. Sprint 6 Demo Summary

During the Sprint 6 Review, the team demonstrated:

- Database-driven localization functionality.
- Retrieval and display of multilingual content based on the current application locale.
- Successful execution of SonarQube analysis and interpretation of quality metrics.
- Refactored code structure with improved maintainability and documentation.
- Successful execution of unit and end-to-end tests after refactoring.
- Updated ER diagrams and UML documentation.
- Acceptance Test Plan prepared for project acceptance activities.

---

## 5. What Went Well

- The database localization strategy was integrated successfully with the existing localization architecture.
- SonarQube analysis provided clear visibility into code quality and maintainability concerns.
- Refactoring activities improved code readability without introducing regressions.
- Automated testing helped validate system stability after code modifications.
- Team collaboration allowed parallel work on localization, testing, documentation, and architecture updates.

---

## 6. What Could Be Improved

- Static analysis identified several UI-related classes with lower coverage levels that may benefit from additional automated testing.
- Some legacy components required additional effort to meet updated documentation and code quality standards.
- Future development could further increase automated coverage of JavaFX view components.

---

## 7. Daily Scrum (Stand-up Meetings)

The team conducted regular stand-up meetings to coordinate database localization implementation, code review activities, refactoring progress, testing efforts, and documentation updates. Sprint progress was tracked through Trello and GitHub repositories.

---

## 8. Team Contributions

| Team Member Name | Assigned Tasks                                                                                                                                                                         | Time Spent (hrs) | In-class tasks |
| :--- |:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| :---: | :---: |
| **Olena Petrova**<br>*(Scrum Master)* | Sprint coordination, SonarQube analysis, static code review, code clean-up, acceptance test design, UI verification, architecture documentation, sprint reporting, backlog management. | 18 h 15 min | Submitted |
| **Maria Kuznetsova** | Database localization implementation, static code review, code clean-up, multilingual database testing, ER diagram updates.        | 25 h 30 min | Submitted |
| **Bayram Erdogan** | Static code review, code clean-up, testing and validation.                                                                                                  | 10 h 30 min | Submitted |
| **Grigorii Tishchuk** | Static code review, code clean-up, static code review, code clean-up.                                                                                                | 11 h 30 min | Submitted |
## Submission Summary

| Deliverable | Status |
| :--- | :--- |
| **Sprint Report (GitHub)** | Submitted |
| **Database Localization Implementation** | Submitted |
| **Statistical Code Review Report** | Submitted |
| **Refactored Codebase** | Submitted |
| **Acceptance Test Plan** | Submitted |
| **Architecture Documentation (ER & UML)** | Submitted |
| **Product/Sprint Backlog Update (Trello)** | Submitted |
| **GitHub Repository Update** | Submitted |