# EduJournal – Teacher's Gradebook and Report Card System

A Java-based desktop application that helps teachers manage student academic records - recording marks, calculating averages and weighted grades, and generating report cards - instead of doing it manually on paper or in scattered tools. Teachers enter and manage grades, admins manage courses and accounts, and students can view (but not edit) their own grades and report cards.

Course: Software Engineering Project 1 (SEP1) · Team: **Lucky7**

---

## Product Vision

### Vision Statement

Our goal is to make managing student grades and academic performance easier for teachers. We want to create a reliable tool that handles grade calculations and report card creation automatically. In this way, teachers can spend less time on manual tasks and calculations and more time focusing on their students and their learning.

### Goals

- Deliver a fully functional product within the project timeline
- Reduce the time teachers spend calculating grades manually
- Reduce mistakes when calculating student averages and weighted averages
- Make it easier for teachers to enter and manage student grades
- Allow teachers to create accurate report cards more easily
- Provide a simple and user-friendly interface for managing student records
- Keep student and grade information organized in a reliable database

### Key Features

- Add and manage student information
- Enter grades for assignments, exams, and projects
- Automatically calculate student averages
- Calculate weighted averages for different types of assessments
- Store and manage student and grade information
- Create report cards based on student grades and performance
- Display student grades and academic results in an organized way
- Provide feedback / comments

### Definition of Success

The project will be successful if the gradebook system is completed on time, meets requirements, calculates grades accurately, and offers a reliable, user-friendly solution for teachers.

---

## Technologies Used

#### Development
- Java Development Kit (JDK)
- JavaFX - desktop user interface
- IntelliJ IDEA - IDE
- Maven - build management

#### Database
- MariaDB - data storage
- JPA (Hibernate) - data access

#### Testing
- JUnit - unit testing

#### DevOps
- Jenkins - CI/CD
- Docker - containerization (MariaDB)
- Kubernetes - orchestration (deploying the MariaDB container)

#### Other Tools
- GitHub - version control
- Trello - sprint planning and task management
- Discord - team communication
- jBCrypt - password hashing

### Why We Chose These Technologies

- **JavaFX** - the whole team codes in one language, no switching between frontend and backend languages.
- **MariaDB** - a reliable relational database, good fit for structured data like students, grades, and courses.
- **JPA (Hibernate)** - lets us work with Java classes instead of writing raw SQL by hand, which means less repetitive code and fewer manual query mistakes.
- **Docker** - packages MariaDB the same way for every team member, so nobody has database setup problems on their own machine.
- **Kubernetes** - deploys that Docker container;
- **Jenkins** - automates building and testing the project on every change, catching mistakes earlier.
- **JUnit** - lets us test grade calculations and other logic automatically, instead of checking everything by hand.
- **Git / GitHub** - standard, reliable version control; lets the whole team work on the code without overwriting each other's work.
- **Trello** - simple visual board for tracking sprint tasks and progress.
- **Discord** - our team's main channel for daily communication and quick questions.
- **jBCrypt** - securely hashes passwords so raw passwords are never stored in the database.
- **Maven** - manages our project's dependencies and build process automatically.

---

## Project Plan & Sprint Structure

The project follows an Agile Scrum methodology over 8 weeks, divided into 4 sprints of 2 weeks each. Trello is used to plan tasks, track progress, and manage sprint activities.

| Sprint | Focus |
|---|---|
| Sprint 1 | Requirement and Planning |
| Sprint 2 | Design and Core Development |
| Sprint 3 | Feature Implementation and Testing |
| Sprint 4 | Finalization and Presentation |

---

## Sprint 1 – Requirement and Planning

Focus: understanding the project and planning the work for upcoming
sprints.

- Project requirements analyzed and gathered
- User stories and product backlog created in Trello
- Project vision defined
- Project plan created
- Key UI design elements created
- Database structure designed

🔗 [Sprint 1 Planning](Documents/Sprint_Reports/Sprint1/Sprint_1_Planning_report.md)
🔗 [Sprint 1 Review](Documents/Sprint_Reports/Sprint1/Sprint_1_Review_Report.md)

---

## Sprint 2 – Design and Core Development (Planned)

- Finalize database schema
- Create MariaDB tables
- Develop JavaFX user interface
- Set up JPA entities and connect to MariaDB

---

## Sprint 3 – Feature Implementation and Testing (Planned)

- Implement student and grade management features
- Complete grade calculation logic
- Implement report card generation
- Perform unit testing using JUnit

---

## Sprint 4 – Finalization and Presentation (Planned)

- Integrate final features
- Fix bugs and stabilize the system
- Complete project documentation
- Prepare and deliver final project presentation

---

## How to Run the Project

### Prerequisites
- Java 21
- Git
- Maven
- MariaDB
- Docker (optional)

### Steps

**1.** Clone the repository
```bash
  git clone https://github.com/GrigoriiTishchuk/SWE-Project.git
  cd SWE-Project
```

**2.** Set up the database

Make sure MariaDB is installed and running.

Create a database:
```sql
CREATE DATABASE your_database_name;
USE your_database_name;
```

```md
> Replace `your_database_name` with the database name used in your configuration.
> ⚠️ Make sure the database name matches the one in your configuration file.
> Update your database credentials in the configuration file (e.g., application.properties).
```

Run the application using Maven

```bash
  mvn javafx:run
```
 OR

Run the application using Docker

```bash
  
```
---

## Testing Instructions

Unit tests are written with JUnit and can be run with Maven from the project root:

```bash
mvn test
```

---

## Repository Structure

```
/Documents → Documentation and reports  
/src       → Source code and tests
```

---

## Authors

- Olena Petrova
- Bayram Erdogan
- Grigorii Tishchuk
- Maria Kuznetsova

Course name and semester:

- Software Engineering Project TX00EY27-3012
- Semester 3 & 4, 2026