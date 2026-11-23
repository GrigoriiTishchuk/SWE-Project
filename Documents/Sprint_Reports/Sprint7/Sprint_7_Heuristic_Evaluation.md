# EduJournal — Sprint 7 Heuristic Evaluation

**Project:** EduJournal - Teacher's Gradebook and Report Card System
**Sprint:** 7 - Functional and Non-functional Testing
**Evaluator:** Maria Kuznetsova
**Date:** 19.11.2026
**Status:** In Progress

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
| 1 | Cosmetic issue — fix only if time permits |
| 2 | Minor usability problem — low priority |
| 3 | Major usability problem — important to fix |
| 4 | Usability catastrophe — must fix before release |

---

## 2. Nielsen's 10 Heuristics — Reference

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
| Admin — Student Management | Admin |
| Admin — Teacher Management | Admin |
| Admin — Group Management | Admin |
| Teacher Dashboard | Teacher |
| Teacher Gradebook — Assessments Tab | Teacher |
| Teacher Gradebook — Grades Tab | Teacher |
| Teacher Gradebook — Notes Tab | Teacher |
| Student Dashboard | Student |
| Course Report Page | Teacher / Admin |
| Student Report Page | Teacher / Admin / Student |
| Own Profile Page | All users |

---

## 4. Findings

| ID | Screen | Heuristic | Description | Severity | Recommendation | Status |
|---|---|:---:|---|:---:|---|---|
| HE-01 | Login Page | H10 | No app description or context visible — a first-time user has no explanation of what EduJournal is or why they are logging in | 1 | Add a short tagline or subtitle below the app title (e.g. "Teacher's Gradebook and Report Card System") | Open |
| HE-02 | Login Page | H7 | Pressing Enter on the username field does not move focus to the password field — user must click with the mouse | 1 | Set the username field's action to transfer focus to the password field on Enter | Open |
| HE-03 | Login Page | H7 | Pressing Enter on the password field does not submit the form — user must click the Sign In button with the mouse | 1 | Bind Enter key on the password field to trigger the login action | Open |
| HE-04 | Login Page | H5 | Forgot Password allows any user to reset any other user's password without identity verification — no current user credentials or confirmation required | 3 | Require the current user to confirm their identity (e.g. current password or admin approval) before a password change is accepted | Open |
| HE-05 | Admin — Student / Teacher / Course / Group Management | H1 | No confirmation message is shown after adding, editing, or deleting a record — the table updates immediately so the change is visible, but there is no explicit success notification | 1 | Optionally add a brief status message (e.g. "Student saved") for extra clarity | Open |
| HE-06 | Course Report Page | H1 | After exporting a gradebook to PDF or CSV, no confirmation or file path is shown — user does not know if the export succeeded or where the file was saved | 2 | Display a success message with the saved file location after export completes | Open |

---

## 5. Summary by Heuristic

| Heuristic | Findings Count | Highest Severity |
|---|:---:|:---:|
| H1 — Visibility of system status | | |
| H2 — Match with the real world | | |
| H3 — User control and freedom | | |
| H4 — Consistency and standards | | |
| H5 — Error prevention | | |
| H6 — Recognition rather than recall | | |
| H7 — Flexibility and efficiency | | |
| H8 — Aesthetic and minimalist design | | |
| H9 — Error recovery | | |
| H10 — Help and documentation | | |

---

## 6. Overall Assessment

> *(To be completed after evaluation)*

**Total findings:** TBD
**Critical issues (severity 4):** TBD
**Major issues (severity 3):** TBD
**Minor/cosmetic issues (1–2):** TBD

**General conclusion:**

---

## 7. Positive Observations

> *(Record things that work well — areas where the app meets usability expectations)*

---

## 8. Recommendations Summary

> *(Prioritized list of the most important fixes based on severity)*
