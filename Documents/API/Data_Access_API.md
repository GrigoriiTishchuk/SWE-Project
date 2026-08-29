# Data Access (API)

JavaFX connects to the MariaDB database directly using **JDBC** (`java.sql` / MariaDB Connector/J).
The desktop application and the data access logic run in the same process.

## Architecture

```
JavaFX UI  →  DAO layer (Java classes)  →  JDBC (MariaDB Connector/J)  →  MariaDB
```

- **UI layer** — JavaFX views/controllers (screens, buttons, forms)
- **DAO layer** — plain Java classes that hold SQL and business rules
  (e.g. `GradeDAO`, `StudentDAO`), keeping SQL out of the UI code
- **JDBC** — Java's built-in database API (`Connection`, `PreparedStatement`,
  `ResultSet`) used inside the DAO layer to talk to MariaDB

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

- Passwords are hashed with jBCrypt before being stored via
  `UserDAO.createUser` / `authenticate`; the raw password is never stored.
- All SQL uses `PreparedStatement` (not string-concatenated queries) to
  prevent SQL injection.