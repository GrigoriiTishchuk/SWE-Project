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
- Figma - UI design and prototyping

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
- **Figma** - lets us design and prototype the user interface before coding, so we can plan the layout and flow of the application.

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

## Sprint 2 – Design and Core Development

- Designed and implemented database schema (MariaDB tables, CRUD operations)
- Developed initial JavaFX UI views referencing Figma designs
- Set up JPA entities and connected to MariaDB
- Wrote JUnit unit tests for key backend functions
- Configured JaCoCo code coverage reporting and published report

🔗 [Sprint 2 Planning](Documents/Sprint_Reports/Sprint2/Sprint_2_Planning_Report.md)
🔗 [Sprint 2 Review](Documents/Sprint_Reports/Sprint2/Sprint_2_Review_Report.md)
🔗 [JaCoCo Code Coverage Report](https://users.metropolia.fi/~grigorit/devops/jacoco/)

---

## Sprint 3 – CI/CD Integration, Feature Extension, Basic Docker Image, and Testing

- Implement student and grade management features
- Complete grade calculation logic
- Implement report card generation
- Perform unit testing using JUnit
- Integrated Jenkins CI/CD pipeline (checkout, Maven build, JUnit tests, JaCoCo coverage report)
- Created and tested a local Docker image of the project
- Extended unit test suite with JaCoCo code coverage reporting
- User authentication with input validation and jBCrypt password hashing
- Admin reporting and analytics dashboard
- CSV and PDF export for gradebook and course report

🔗 [Sprint 3 Planning](Documents/Sprint_Reports/Sprint3/Sprint_3_Planning_Report.md)
🔗 [Sprint 3 Review](Documents/Sprint_Reports/Sprint3/Sprint_3_Review_Report.md)
🔗 [JaCoCo Code Coverage Report](https://users.metropolia.fi/~grigorit/devops/jacoco/)

---

## Sprint 4 – Finalization and Presentation

- Final features integrated
- Bugs fixed and system stabilized
- Project documentation completed
- Final project presentation prepared
- GUI-enabled Docker image created for the JavaFX application

🔗 [Sprint 4 Planning](Documents/Sprint_Reports/Sprint4/Sprint_4_Planning_Report.md)
🔗 [Sprint 4 Review](Documents/Sprint_Reports/Sprint4/Sprint_4_Review_Report.md)
🔗 [JaCoCo Code Coverage Report](https://users.metropolia.fi/~grigorit/devops/jacoco/)

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

Database Setup

The project uses MariaDB for local database configuration.

Before running the application make sure MariaDB is installed and running.

For detailed database configuration, see [DatabaseConfiguration.md](Documents/Diagrams/DatabaseConfiguration.md).

For database initialization and connection testing, see [DatabaseTest.md](Documents/Diagrams/DatabaseTest.md).

Run the application using Maven:

```bash
  mvn javafx:run
```
 OR

### Run with Docker

Pull the Docker image from Docker Hub:

```bash
  docker pull gregtish/edujournal-frontend:latest
```

Run the container:

```bash
  docker run --rm -it 
  -e DISPLAY=host.docker.internal:0.0 
  gregtish/edujournal-frontend:latest
```

Docker Hub repository:

[DockerHub](https://hub.docker.com/repository/docker/gregtish/edujournal-frontend/general)

---

## Testing Instructions

The project uses JUnit 5 for unit testing, Mockito for mocking, JMH for performance testing, and JaCoCo for code coverage.

Run unit tests:

```bash
mvn test
```

Generate the JaCoCo code coverage report:

```bash
mvn clean verify
```

The coverage report is generated in `public_html/`.

Run performance tests:

```bash
mvn clean verify -Pperformance
```

Performance test results are saved in `performance-results/`.


## Documentation

- [Feature Documentation](Documents/Features.md) – full description of implemented features by role

---

## Repository Structure

```
/Documents → Documentation and reports
/performance-results → Performance test results
/public_html → JaCoCo code coverage report
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
- Academic Year 2026–2027