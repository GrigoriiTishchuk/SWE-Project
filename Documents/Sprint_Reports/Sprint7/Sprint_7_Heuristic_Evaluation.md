# EduJournal - Sprint 7 Heuristic Evaluation

**Project:**: EduJournal - Teacher's Gradebook and Report Card System  
**Sprint:** 7 - Project Testing, Functional and Non-functional Testing  
**Evaluator:** Maria Kuznetsova  
**Date:** 19.11.2026  
**Status:** Complete

---

## 1. Overview

This report presents the heuristic evaluation of the EduJournal JavaFX desktop application.
The evaluation is based on **Nielsen's 10 Usability Heuristics**. Each finding is recorded with
the affected screen, the violated heuristic, a description of the problem, severity rating,
and a recommendation.

### Severity Scale

| Rating | Description |
|:---:|---|
| 0 | Not a usability problem |
| 1 | Cosmetic issue - fix only if time permits |
| 2 | Minor usability problem - low priority |
| 3 | Major usability problem - important to fix |
| 4 | Usability catastrophe - must fix before release |

---

## 2. Nielsen's 10 Heuristics - Reference

| # | Heuristic |
|:---:|---|
| H1 | Visibility of system status |
| H2 | Match between system and the real world |
| H3 | User control and freedom |
| H4 | Consistency and standards |
| H5 | Error prevention |
| H6 | Recognition rather than recall |
| H7 | Flexibility and efficiency of use |
| H8 | Aesthetic and minimalist design |
| H9 | Help users recognize, diagnose, and recover from errors |
| H10 | Help and documentation |

---

## 3. Screens Evaluated

| Screen | Role |
|---|---|
| Login Page | All users |
| Admin Dashboard | Admin |
| Admin - Student Management | Admin |
| Admin - Teacher Management | Admin |
| Admin - Group Management | Admin |
| Teacher Dashboard | Teacher |
| Teacher Gradebook - Assessments Tab | Teacher |
| Teacher Gradebook - Grades Tab | Teacher |
| Teacher Gradebook - Notes Tab | Teacher |
| Student Dashboard | Student |
| Course Report Page | Teacher / Admin |
| Student Report Page | Teacher / Admin / Student |
| Own Profile Page | All users |

---

## 4. Findings

| ID | Screen | Heuristic | Description | Severity | Recommendation | Status |
|---|---|:---:|---|:---:|---|---|
| HE-01 | Login Page | H10 | No app description or context visible - a first-time user has no explanation of what EduJournal is or why they are logging in | 1 | Add a short tagline or subtitle below the app title (e.g. "Teacher's Gradebook and Report Card System") | Open |
| HE-02 | Login Page | H7 | Pressing Enter on the username field does not move focus to the password field - user must click with the mouse | 1 | Set the username field's action to transfer focus to the password field on Enter | Open |
| HE-03 | Login Page | H7 | Pressing Enter on the password field does not submit the form - user must click the Sign In button with the mouse | 1 | Bind Enter key on the password field to trigger the login action | Open |
| HE-04 | Login Page | H5 | Forgot Password allows any user to reset any other user's password without identity verification - no current user credentials or confirmation required | 3 | Require the current user to confirm their identity (e.g. current password or admin approval) before a password change is accepted | Open |
| HE-05 | Admin - Student / Teacher / Course / Group Management | H1 | No confirmation message is shown after adding, editing, or deleting a record - the table updates immediately so the change is visible, but there is no explicit success notification | 1 | Optionally add a brief status message (e.g. "Student saved") for extra clarity | Open |
| HE-06 | Course Report Page | H1 | After exporting a gradebook to PDF or CSV, no confirmation or file path is shown - user does not know if the export succeeded or where the file was saved | 2 | Display a success message with the saved file location after export completes | Open |
| HE-07 | Teacher Gradebook - Grades Tab | H1 | Final grade column shows "-" with no explanation when a student is missing one or more assessment grades - user does not know why the grade is absent or what action is required | 1 | Show a tooltip on "-" cells explaining that the final grade appears once all assessment grades are filled in | Fixed |
| HE-08 | Teacher Gradebook - Assessments Tab | H3 | Editing an existing assessment does not apply a position change - the position field is shown and pre-filled in the edit form but its value is silently ignored on save | 2 | Read the position field value during update and move the assessment to the selected position in the list | Fixed |
| HE-09 | Own Profile Page | H3 | No Cancel button is available while editing profile fields - if a user starts editing and wants to discard changes they must manually retype all original values | 1 | Add a Cancel button that restores original field values and exits edit mode without saving | Fixed |
| HE-10 | Student Report Page | H1 | Final grade column shows "-" with no explanation when assessment grades are incomplete - student does not know what action is required to see the grade | 1 | Show a tooltip on "-" cells explaining that the final grade appears once all assessment grades are filled in | Fixed |

---

## 5. Summary by Heuristic

| Heuristic | Findings Count | Highest Severity |
|---|:---:|:---:|
| H1 - Visibility of system status | 5 | 2 |
| H2 - Match with the real world | 0 | - |
| H3 - User control and freedom | 2 | 1 |
| H4 - Consistency and standards | 0 | - |
| H5 - Error prevention | 1 | 3 |
| H6 - Recognition rather than recall | 0 | - |
| H7 - Flexibility and efficiency | 2 | 1 |
| H8 - Aesthetic and minimalist design | 0 | - |
| H9 - Error recovery | 0 | - |
| H10 - Help and documentation | 1 | 1 |

---

## 6. Overall Assessment

**Total findings:** 10
**Critical issues (severity 4):** 0
**Major issues (severity 3):** 1 (HE-04 - Forgot Password without identity verification)
**Minor/cosmetic issues (1–2):** 9

**General conclusion:** The application is functionally stable and covers all core use cases. The majority of usability issues are cosmetic or minor workflow gaps (missing keyboard shortcuts, absent feedback messages, missing cancel actions). One major security concern was identified around the password reset flow. Four issues found during evaluation (HE-07, HE-08, HE-09, HE-10) were fixed immediately during the sprint.

---

## 7. Positive Observations

- **Role-based visual identity** - each role (Admin, Teacher, Student) has a distinct background and color scheme, making it immediately clear which role context the user is in.
- **Consistent sidebar navigation** - all pages share the same sidebar layout per role; users always know where they are and how to navigate.
- **Multilingual support** - the application supports four languages (English, Russian, Turkish, Arabic) switchable at any time from the top bar without requiring a logout.
- **Destructive action protection** - deleting an assessment that already has grades triggers a two-step confirmation dialog, preventing accidental data loss.
- **Searchable quick actions** - the dashboard quick-action panel has a live search filter, allowing fast access to any action without scrolling.
- **Export to PDF and CSV** - course and student reports can be exported to both formats directly from the report pages, making it easy to share or archive data outside the application.
- **Automatic final grade calculation** - the system calculates the weighted final grade automatically once all assessment scores are entered, reducing manual work and calculation errors.
- **All information in one place** - student grades, assessments, group membership, course details, and reports are all accessible within the application without switching between external tools.
- **Editable user profile** - all users can view and update their personal information (name, email, phone, date of birth for students) directly from the Own Profile page.
- **Inline grade editing** - the gradebook allows teachers to edit grades in place within the table without navigating to a separate page.
- **Comprehensive dashboard statistics** - the dashboard provides an at-a-glance overview of key metrics (total students, courses, groups, assessments) alongside a grade distribution chart, giving each role a meaningful summary of their data.

---

## 8. Recommendations Summary

Prioritized by severity - open issues only.

| Priority | ID | Severity | Recommendation |
|:---:|---|:---:|---|
| 1 | HE-04 | 3 | Add identity verification to the Forgot Password flow - require current credentials or admin approval before accepting a password change |
| 2 | HE-06 | 2 | Show a success notification with the saved file path after CSV or PDF export completes |
| 3 | HE-02 | 1 | Move focus to the password field when Enter is pressed in the username field |
| 4 | HE-03 | 1 | Submit the login form when Enter is pressed in the password field |
| 5 | HE-05 | 1 | Show a brief success message after adding, editing, or deleting a student, teacher, course, or group |
| 6 | HE-01 | 1 | Add a short tagline below the app title on the login page to explain what EduJournal is |

> HE-07, HE-08, HE-09, HE-10 were identified and fixed during this sprint.
