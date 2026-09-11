# Sprint 2 – Sprint Planning Document

**Project:** Edujournal – Teacher's Gradebook and Report Card System  
**Sprint Duration:** 01.09.2026 – 15.09.2026  
**Team:** Bayram Erdogan, Grigorii Tishchuk, Olena Petrova, Maria Kuznetsova  
**Scrum Master:** Bayram Erdogan

---

## 1. Sprint Goal

The main goal of Sprint 2 is to establish the technical foundation of the **Edujournal – Teacher's Gradebook and Report Card System**.

During this sprint, the team will transform the initial designs and product backlog created in Sprint 1 into the first working parts of the application. The main focus will be on implementing the MariaDB database, developing the initial JavaFX user interface, configuring the Maven project, introducing unit testing with JUnit, and configuring JaCoCo for code coverage reporting.

The sprint will also ensure that the project's GitHub repository and Trello board are kept up to date and that the team is prepared for the Sprint Review.

---

## 2. Sprint Objectives

The main objectives of Sprint 2 are:

- Implement the relational database based on the ERD created in Sprint 1.
- Create the initial database tables, relationships, and constraints using MariaDB.
- Test basic CRUD operations with the implemented database.
- Begin developing the JavaFX user interface based on the Figma designs.
- Implement initial screens for the main user roles: **Administrator, Teacher, and Student**.
- Configure the project as a Maven-based Java project.
- Add and manage required project dependencies through `pom.xml`.
- Integrate JUnit for unit testing.
- Write unit tests for selected core application functions.
- Integrate JaCoCo and generate a code coverage report.
- Continue refining the software architecture and data model where necessary.
- Keep GitHub and Trello updated throughout the sprint.
- Prepare the application and individual contributions for the Sprint Review.

---

## 3. Sprint Backlog

The following Product Backlog Items are planned for Sprint 2:

| Product Backlog Item | Priority | Story Points |
|---|:---:|:---:|
| Implement MariaDB Database Schema | High | 5 |
| Create Database Tables and Relationships | High | 5 |
| Implement Basic CRUD Operations | High | 5 |
| Develop Initial JavaFX UI | High | 5 |
| Implement Login / Role Selection Screen | High | 3 |
| Develop Teacher Grade Management UI | High | 5 |
| Develop Student Report Card UI | Medium | 5 |
| Configure Maven Project | High | 2 |
| Integrate JUnit Unit Testing | High | 3 |
| Configure JaCoCo Code Coverage | High | 3 |
| Update Documentation | Medium | 2 |
| GitHub and Trello Maintenance | High | 1 |
| **Total** | | **44 SP** |

---

## 4. Roles and Responsibilities

| Team Member | Responsibilities |
|---|-|
| **Bayram Erdogan**<br>*(Scrum Master)* | Manage Sprint 2 planning, facilitate team coordination, maintain the Trello backlog, monitor GitHub activity, implement database/JPA-related tasks, and coordinate Sprint Review preparation. |
| **Grigorii Tishchuk** | |
| **Olena Petrova** | |
| **Maria Kuznetsova** | |

---

## 5. Expected Deliverables

At the end of Sprint 2, the team expects to deliver:

- A working MariaDB relational database.
- Implemented database tables and relationships.
- Basic CRUD operations connected to the application.
- Initial JavaFX application screens.
- Basic navigation between initial screens.
- Initial teacher grade management functionality.
- Initial student report card functionality.
- Configured Maven project and `pom.xml`.
- JUnit unit testing infrastructure.
- Initial unit tests for core functionality.
- Configured JaCoCo code coverage reporting.
- Generated JaCoCo HTML report.
- Updated GitHub repository.
- Updated Trello Sprint 2 board.
- Updated project documentation.
- Sprint Review demonstration materials.

---

## 6. Team Capacity and Assumptions

### Team Capacity

The estimated team capacity for Sprint 2 is:

**4 members × 3 hours/day × 8 working days = approximately 96 hours**

The capacity is an estimation and may vary depending on development progress, meetings, academic workload, and other project activities.

### Assumptions and Risks

- The team may need additional time to study and configure JaCoCo correctly.
- Some database or JavaFX requirements may require further refinement during implementation.
- The sprint backlog may be adjusted based on development progress and feedback.

---

## 7. Definition of Done

A task will be considered **Done** when:

- [ ] The planned functionality or deliverable has been implemented.
- [ ] The implementation meets the requirements of the corresponding backlog item.
- [ ] The code compiles and runs successfully.
- [ ] Database operations work correctly where applicable.
- [ ] UI screens load and provide the planned basic interaction.
- [ ] Unit tests are implemented for the relevant functionality.
- [ ] Unit tests pass successfully.
- [ ] JaCoCo generates a valid code coverage report where applicable.
- [ ] The work has been reviewed by at least one other team member.
- [ ] The implementation is committed and pushed to GitHub.
- [ ] The corresponding Trello task is updated.
- [ ] Required documentation is updated.
- [ ] The functionality is ready to be demonstrated during the Sprint Review.

---

## 8. Sprint Review Preparation

Before the Sprint Review, the team will ensure that:

- [ ] The MariaDB database can be demonstrated.
- [ ] Database tables and relationships can be explained.
- [ ] CRUD operations can be demonstrated.
- [ ] Initial JavaFX screens can be demonstrated.
- [ ] Basic application navigation can be shown.
- [ ] Unit tests can be executed and demonstrated.
- [ ] Maven configuration and dependency management can be explained.
- [ ] JaCoCo coverage results can be demonstrated.
- [ ] GitHub contains the latest project changes.
- [ ] Trello reflects the final status of Sprint 2 tasks.
- [ ] Each team member is prepared to explain their individual contribution.

---

## 9. Sprint 2 Success Criteria

Sprint 2 will be considered successful if the team has established the basic technical foundation of the application and can demonstrate:

**Database → Application → UI → Testing → Code Coverage**
