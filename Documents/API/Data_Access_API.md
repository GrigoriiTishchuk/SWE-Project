# Data Access API

JavaFX connects to the MariaDB database using **JPA** (Java Persistence API) with **Hibernate**.
The desktop application and the data access logic run in the same process.

## Architecture

```
JavaFX UI  →  DTO layer (+ Mapper layer)  →  Service layer  →  DAO layer  →  MariaDB
```

- **UI layer** — JavaFX views and controllers (screens, buttons, forms)
- **DTO layer** — data transfer objects (e.g. `UserDTO`, `StudentDTO`) passed between UI and services; Mappers convert entities to DTOs
- **Service layer** — business logic (e.g. `AuthService`, `CourseGradeService`, `StudentReportService`)
- **DAO layer** — database queries via JPA/Hibernate (e.g. `UserDAO`, `GradesDAO`)
- **JPA / Hibernate** — maps Java entities to MariaDB tables using `EntityManager` and JPQL
- **MariaDB** — relational database storing all application data

---

## DAO Layer

### UserDAO
- `findById(Integer userId)` → `User`
- `findByUsername(String username)` → `User`
- `findByRole(Role role)` → `List<User>`
- `findAll()` → `List<User>`
- `findAllTeachers()` → `List<User>`
- `save(User user)`
- `update(User user)`
- `delete(User user)`

### StudentDAO
- `findById(Integer id)` → `Student`
- `findAll()` → `List<Student>`
- `findByUserId(Integer userId)` → `Student`
- `findByStudentNumber(String studentNumber)` → `Student`
- `save(Student student)`
- `update(Student student)`
- `delete(Integer id)`

### AcademicGroupDAO
- `findById(Integer id)` → `AcademicGroup`
- `findAll()` → `List<AcademicGroup>`
- `findByName(String name)` → `AcademicGroup`
- `findStudentIdsByGroupId(Integer groupId)` → `List<Integer>`
- `addStudentToGroup(Integer studentId, Integer groupId)`
- `removeStudentFromGroup(Integer studentId, Integer groupId)`
- `save(AcademicGroup group)`
- `update(AcademicGroup group)`
- `delete(Integer id)`

### CourseDAO
- `findById(Integer id)` → `Course`
- `findAll()` → `List<Course>`
- `findByName(String name)` → `Course`
- `findByCode(String code)` → `Course`
- `existsByCode(String code)` → `boolean`
- `findByUserId(Integer userId)` → `List<Course>`
- `findByGroupId(Integer groupId)` → `List<Course>`
- `save(Course course)`
- `update(Course course)`
- `delete(Integer id)`

### EnrollmentDAO
- `findById(Integer id)` → `Enrollment`
- `findAll()` → `List<Enrollment>`
- `findByStudentId(Integer studentId)` → `List<Enrollment>`
- `findByCourseId(Integer courseId)` → `List<Enrollment>`
- `findByAcademicGroupId(Integer academicGroupId)` → `List<Enrollment>`
- `findByCourseAndGroup(int courseId, int groupId)` → `List<Enrollment>`
- `findByStudentAndCourse(Integer studentId, Integer courseId)` → `Enrollment`
- `save(Enrollment enrollment)`
- `update(Enrollment enrollment)`
- `delete(Integer id)`
- `deleteByStudentAndAcademicGroup(Integer studentId, Integer academicGroupId)`
- `deleteByCourseId(Integer courseId)`

### AssessmentsDAO
- `findById(Integer id)` → `Assessments`
- `findAll()` → `List<Assessments>`
- `findByCourseId(Integer courseId)` → `List<Assessments>`
- `save(Assessments assessment)`
- `update(Assessments assessment)`
- `delete(Assessments assessment)`

### GradesDAO
- `findById(int id)` → `Grades`
- `findByEnrollment(int enrollmentId)` → `List<Grades>`
- `findByAssessment(int assessmentId)` → `List<Grades>`
- `findByEnrollmentAssessment(int enrollmentId, int assessmentId)` → `Grades`
- `save(Grades grade)`
- `update(Grades grade)`
- `deleteById(int id)`

---

## Service Layer

### AuthService
- `login(String username, String password)` → `boolean` — validates credentials with BCrypt; sets `UserSession` on success
- `resetPassword(String username, String newPassword)` → `boolean`
- `logout()` — clears `UserSession`

### CourseGradeService
- `calculateFinalGrade(List<Assessments> assessments, List<Grades> grades)` → `double` — returns weighted percentage score across all assessments

### StudentReportService
- `buildReport(Integer studentId)` → `List<StudentReportDTO>` — returns per-course assessment and grade data for the student report card
- `computeAverageGrade(Integer studentId)` → `String` — returns average final grade across all enrolled courses (e.g. `"3.5"` or `"—"` if no grades)

---

## Grade Calculation

Final course grades use a weighted percentage converted to a 1–5 scale:

| Percentage | Grade |
|:---:|:---:|
| ≥ 83% | 5 |
| ≥ 72% | 4 |
| ≥ 62% | 3 |
| ≥ 50% | 2 |
| ≥ 40% | 1 |
| < 40% | 0 |

---

## Notes

- Passwords are hashed with **jBCrypt** before storage; raw passwords are never saved
- JPQL with bound parameters is used throughout the DAO layer to prevent SQL injection
- `UserSession` (singleton) caches the logged-in user's `UserDTO` in memory — no DB fetch needed for the current user's name or role during a session
