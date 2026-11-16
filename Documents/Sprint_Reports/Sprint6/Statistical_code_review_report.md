# Statistical Code Review Report

## Software Engineering Project 1 – Sprint 6

**Project:** EduJournal – Teacher's Gradebook and Report Card System  
**Sprint:** Sprint 6 – Database Localization, Code Review, Code Clean-Up, and Acceptance Testing  
**Team Members:** Bayram Erdogan, Grigorii Tishchuk, Olena Petrova, Maria Kuznetsova  
**Date:** November 2026

---

# 1. Introduction

The purpose of this code review was to assess the overall quality, maintainability, reliability, and test coverage of the EduJournal project. A comprehensive statistical analysis was performed using automated code quality tools. The review focused on identifying code smells, duplicated logic, maintainability issues, potential defects, and opportunities for refactoring.

The review process included:

- Static code analysis using SonarQube and SonarScanner
- Coverage analysis using JaCoCo
- Refactoring of duplicate and complex code
- Documentation improvements
- Verification through automated testing

---

# 2. Analysis Tools

The following tools were used during the review process:

| Tool | Purpose |
|--------|----------|
| SonarQube | Static code analysis and quality metrics |
| SonarScanner | Project analysis and reporting |
| JaCoCo | Test coverage measurement |
| Maven Surefire | Automated test execution |
| JUnit 5 | Unit and integration testing |

## 2.1 Code Formatting and Standards Enforcement

In addition to static analysis and refactoring activities, automated code formatting was applied to improve consistency across the entire codebase.

### Tool Used

- Google Java Format

### Objectives

Google Java Format was used to:

- Enforce a consistent coding style throughout the project.
- Standardize indentation, spacing, and line wrapping.
- Improve code readability and maintainability.
- Reduce formatting-related review comments.
- Support adherence to Java coding conventions and best practices.

### Applied Scope

The formatter was applied to actively maintained source code during the refactoring phase, including:

- Controllers
- Services
- DAO classes
- Utility classes
- Localization components

### Benefits Achieved

The adoption of a unified formatting standard resulted in:

- Consistent code appearance across all modules.
- Easier navigation and understanding of source code.
- Simplified code reviews and merge operations.
- Improved maintainability and long-term project consistency.

Formatting standardization complemented the refactoring activities performed during Sprint 6 and contributed to the overall improvement of code quality.

---

# 3. Quality Metrics Summary

## SonarQube Results

| Metric | Result |
|----------|----------|
| Security Rating | A |
| Reliability Rating | A |
| Maintainability Rating | A |
| Vulnerability Issues | 0 |
| Reliability Issues | 0 |
| Maintainability Issues | 0 |
| Coverage | 92.5% |
| Duplications | 2.4% |
| Quality Gate | Passed |

### SonarQube Dashboard

![SonarQube Dashboard](screenshots/sonar-dashboard.png)

Figure 1. SonarQube quality metrics overview.

---

## JaCoCo Coverage

Coverage analysis was performed after executing the complete unit and end-to-end test suite.

### Coverage Summary

| Metric | Result |
|----------|----------|
| Instruction Coverage | 94% |
| Branch Coverage | 87% |

### JaCoCo Coverage Report

![JaCoCo Coverage Report](screenshots/jacoco-report.png)

Figure 2. JaCoCo code coverage statistics.

---

# 4. Key Findings

The static analysis identified several categories of issues requiring improvement:

## 4.1 Code Duplication

Duplicated logic was found in multiple controllers:

- Error dialog creation
- ComboBox localization rendering
- ScrollPane initialization
- Card view rendering

These issues increased maintenance effort and violated the DRY (Don't Repeat Yourself) principle.

### Resolution

Common functionality was moved into reusable methods within the shared BaseController class.

---

## 4.2 Documentation Deficiencies

Several classes and methods lacked sufficient JavaDoc documentation.

### Resolution

JavaDoc documentation was added for:

- Controller classes
- Helper methods
- Utility classes
- Constructors
- Navigation and localization methods

---

## 4.3 Localization Issues

Hard-coded interface strings were discovered in several controllers and utility classes.

### Resolution

Hard-coded values were replaced with localization resource keys and integrated into the existing localization framework.

---

## 4.4 Persistence Layer Issues

SonarQube identified critical issues related to JPA entity management.

### Resolution

The return values of EntityManager.merge() were properly assigned and reused.

Example:

`grade = entityManager.merge(grade);`

instead of

`entityManager.merge(grade);`

This prevents usage of detached entities and ensures consistency of persistence operations.

---

# 5. Refactoring Activities

## 5.1 BaseController Refactoring

### Improvements

- Converted UI component fields to final where appropriate.
- Removed unused fields and redundant code.
- Created reusable createScrollPane() method.
- Extracted createCardList() method.
- Added shared showError() method.
- Added reusable createLocalizedCell() method.
- Added comprehensive JavaDoc documentation.
- Centralized null-safe string handling logic.

### Benefits

- Reduced code duplication.
- Improved maintainability.
- Simplified future UI development.

---

## 5.2 AdminStudentController

### Improvements

- Fixed username filtering defect.
- Added phone number filtering support.
- Extended global search to email and phone fields.
- Removed redundant variables and imports.
- Reused BaseController error handling.
- Added JavaDoc documentation.

---

## 5.3 AdminTeacherController

### Improvements

- Added JavaDoc documentation.
- Reused centralized error handling.
- Reused localized ComboBox rendering.
- Improved teacher search functionality.
- Simplified null handling.

---

## 5.4 AdminGroupController

### Improvements

- Reused shared localization support.
- Centralized error handling.
- Removed redundant code.
- Added JavaDoc documentation.
- Reduced duplicated student rendering logic.

---

## 5.5 CourseController

### Improvements

- Reused localization infrastructure.
- Reused centralized error dialogs.
- Removed hard-coded strings.
- Simplified null handling.
- Added JavaDoc documentation.

---

## 5.6 StudentReportController

### Improvements

- Added JavaDoc documentation.
- Replaced hard-coded messages.
- Improved localization support.
- Extracted focused helper methods.
- Improved maintainability.

---

# 6. DAO Layer Improvements

## Critical Issues Fixed

SonarQube reported multiple critical issues involving incorrect use of EntityManager.merge().

Affected classes:

- AcademicGroupDAO
- EnrollmentDAO
- StudentDAO
- CourseDAO
- AssessmentsDAO
- UserDAO
- GradesDAO

### Result

All affected update operations now correctly use managed entity instances returned by merge().

---

## Duplicate Literal Reduction

Repeated query parameter names were extracted into constants.

Example:

```java
private static final String PARAM_STUDENT_ID = "studentId";
private static final String PARAM_COURSE_ID = "courseId";
```

This improved maintainability and reduced the risk of future refactoring errors.

---

# 7. Utility Layer Improvements

## GradeNormalizer

### Improvements

- Added private constructor.
- Converted normalizer() into a static method.
- Simplified usage throughout the application.

---

## InputValidator

### Improvements

- Added private constructor.
- Enforced utility-class pattern.

---

## Password Security Improvement

The previous password generation mechanism relied on
`Integer.toHexString(password.hashCode())`.

This was replaced with
`BCrypt.hashpw(...)`.

Resulting in:

- Improved security
- Strong password hashing
- Better compliance with authentication best practices

---

# 8. DatabaseInitializer Refactoring

Several SonarQube findings were resolved:

### Improvements

- System.out.println replaced with Logger.
- Added private constructor.
- Extracted helper methods.
- Replaced generic Exception handling with SQLException.
- Improved exception semantics using IllegalStateException.
- Documented unavoidable dynamic SQL usage.

---

# 9. Test Validation

After all refactoring activities:

- Unit tests were executed successfully.
- Integration tests completed successfully.
- End-to-End tests completed successfully.
- No regressions were detected.

### Test Execution Result

![Build Success](screenshots/build-success.png)

Figure 3. Maven test execution results after refactoring.

---

# 10. Conclusions

The statistical code review successfully identified maintainability issues, duplicated logic, documentation gaps, and persistence-layer defects. Through systematic refactoring and code clean-up, the project achieved:

- Security Rating A
- Reliability Rating A
- Maintainability Rating A
- 92.5% Test Coverage
- 2.4% Code Duplication
- Successful Quality Gate status

In addition to structural refactoring and defect resolution, the team adopted Google Java Format to enforce a consistent coding style across the project. This improved readability, simplified code reviews, and increased overall maintainability.

The resulting codebase is significantly more maintainable, better documented, more secure, and easier to extend. The improvements completed during Sprint 6 increased overall software quality and prepared the project for acceptance testing and future development.