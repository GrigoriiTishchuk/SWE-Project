# Data Access (API)

JavaFX connects to the MariaDB database using **JPA** (Java Persistence API) with **Hibernate**.
The desktop application and the data access logic run in the same process.

## Architecture

```
JavaFX UI  →  DAO layer (Java classes)  →  JPA (Hibernate)  →  MariaDB
```

- **UI layer** - JavaFX views/controllers (screens, buttons, forms)
- **DAO layer** - plain Java classes that hold queries and business rules
  (e.g. `GradeDAO`, `StudentDAO`), keeping database logic out of the UI code
- **JPA** - Java's persistence API (`Entity`, `EntityManager`, JPQL)
  used inside the DAO layer to talk to MariaDB via Hibernate

## Data access methods

Java method calls on DAO classes.

### UserDAO
- `getUserById(id)`
- `getAllUsers()`
- `createUser(user)`
- `updateUser(id, user)`
- `deleteUser(id)`
- `authenticate(username, password)`

### StudentDAO
- `getStudentById(id)`
- `getAllStudents()`
- `createStudent(student)`
- `updateStudent(id, student)`
- `deleteStudent(id)`
- `getEnrollmentsByStudent(studentId)`
- `getGradesByStudent(studentId)`
- `getReportCard(studentId)`

### AcademicGroupDAO
- `getGroupById(id)`
- `getAllGroups()`
- `createGroup(group)`
- `updateGroup(id, group)`
- `deleteGroup(id)`
- `getStudentsByGroup(groupId)`

### CourseDAO
- `getCourseById(id)`
- `getAllCourses()`
- `getCoursesByTeacher(teacherId)`
- `createCourse(course)`
- `updateCourse(id, course)`
- `deleteCourse(id)`
- `getEnrollmentsByCourse(courseId)`
- `getAssessmentsByCourse(courseId)`
- `getGroupReportCard(courseId)`

### EnrollmentDAO
- `getEnrollments(filters)`
- `enrollStudent(studentId, courseId, groupId)`
- `updateEnrollmentStatus(id, status)`
- `deleteEnrollment(id)`

### AssessmentDAO
- `getAssessmentById(id)`
- `getAssessmentsByCourse(courseId)`
- `createAssessment(assessment)`
- `updateAssessment(id, assessment)`
- `deleteAssessment(id)`
- `getGradesByAssessment(assessmentId)`

### GradeDAO
- `getGradeById(id)`
- `getGrades(filters)`
- `createGrade(grade)`
- `updateGrade(id, grade)`
- `deleteGrade(id)`
- `calculateWeightedScore(studentId, courseId)`

## Notes

- Passwords are hashed with jBCrypt before being stored via `UserDAO.createUser` / `authenticate`; the raw password is never stored.
- Queries use JPQL (not raw SQL) with bound parameters prevents SQL injection 