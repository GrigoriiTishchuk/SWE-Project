# EduJournal – Feature Documentation

**Project:** EduJournal – Teacher's Gradebook and Report Card System
**Team:** Lucky7

---

## Overview

EduJournal is a Java desktop application for managing student academic records. It supports three user roles — Administrator, Teacher, and Student — each with role-specific views and permissions.

---

## Authentication & Security

- Username and password login with input validation
- Passwords hashed with jBCrypt (no plain-text storage)
- Role-based navigation: each role opens its own dashboard after login
- Error messages shown on invalid credentials

---

## Administrator Features

### User Management
- Add, edit, and delete student and teacher accounts
- Accounts are created with an auto-generated username, email, and temporary password
- View full student and teacher lists with search and filter

### Academic Group Management
- Create and manage academic groups
- Assign and remove students from groups

### Course Management
- Create courses with name, code, teacher, academic group, and start/end dates
- View all courses with search and filter

### Reports & Dashboard
- Admin dashboard shows total counts: students, teachers, courses, groups
- View individual student report cards from the student list

---

## Teacher Features

### Gradebook
- View assigned courses and student lists
- Add assessments per course (assignments, exams, projects, with weights)
- Enter and update grades per student per assessment
- Automatic weighted grade calculation (1–5 scale)
- View final grade per course per student

### Reports
- View per-course grade tables
- Export gradebook to CSV or PDF

### Dashboard
- Displays the number of active courses assigned to the teacher

---

## Student Features

### Dashboard
- Shows enrolled courses, total credits, and average grade across all courses

### Report Card
- Per-course table showing all assessments and entered grades
- Final grade column computed automatically from assessment weights
- Average grade shown across all enrolled courses

---

## Grade Calculation

- Final course grade calculated as a weighted average of assessment grades (scale 1–5)
- A dash (—) is shown if not all grades have been entered
- Average grade across courses is shown on dashboard and report card

---

## CI/CD & DevOps

### Jenkins Pipeline
- Automated pipeline: checkout → Maven build → JUnit tests → JaCoCo coverage report
- Runs on every push to ensure build stability

### Docker
- Local Docker image of the application available for testing
- MariaDB containerized with Docker for consistent setup across environments

### Testing
- JUnit 5 unit tests for backend services and grade logic
- TestFX end-to-end tests covering login, student/teacher/group/course creation
- JaCoCo code coverage reports generated on each CI run

---

## Technologies

| Layer | Technology |
|---|---|
| Language | Java 21 |
| UI | JavaFX |
| Database | MariaDB |
| Data Access | JPA (Hibernate) |
| Security | jBCrypt |
| Build | Maven |
| Testing | JUnit 5, TestFX, JaCoCo |
| CI/CD | Jenkins |
| Containers | Docker |
| Version Control | GitHub |
