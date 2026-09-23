/*
 Users
 */
INSERT INTO users (
    username,
    password_hash,
    first_name,
    last_name,
    role
)
SELECT
    'admin',
    '$2a$10$dT96PvmFO9u5rV.Qo9FMle5Fr5HnVDKpsUm3S4KMadh8ygRYL5/aO',
    'Admin',
    'User',
    'ADMINISTRATOR'
    WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'admin'
);

INSERT INTO users (
    username,
    password_hash,
    first_name,
    last_name,
    role
)
SELECT
    'teacher1',
    '$2a$10$dT96PvmFO9u5rV.Qo9FMle5Fr5HnVDKpsUm3S4KMadh8ygRYL5/aO',
    'Anna',
    'Korhonen',
    'TEACHER'
    WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'teacher1'
);

INSERT INTO users (
    username,
    password_hash,
    first_name,
    last_name,
    role
)
SELECT
    'teacher2',
    '$2a$10$dT96PvmFO9u5rV.Qo9FMle5Fr5HnVDKpsUm3S4KMadh8ygRYL5/aO',
    'Mika',
    'Virtanen',
    'TEACHER'
    WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'teacher2'
);

INSERT INTO users (
    username,
    password_hash,
    first_name,
    last_name,
    role
)
SELECT
    'teacher3',
    '$2a$10$dT96PvmFO9u5rV.Qo9FMle5Fr5HnVDKpsUm3S4KMadh8ygRYL5/aO',
    'Sofia',
    'Niemi',
    'TEACHER'
    WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'teacher3'
);


/*
 Students
 */

-- Student 1
INSERT INTO users (
    username,
    password_hash,
    first_name,
    last_name,
    role
)
SELECT
    'student1',
    '$2a$10$dT96PvmFO9u5rV.Qo9FMle5Fr5HnVDKpsUm3S4KMadh8ygRYL5/aO',
    'Heikki',
    'Heikkinen',
    'STUDENT'
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'student1'
);

INSERT INTO students (
    student_number,
    date_of_birth,
    user_id
)
SELECT
    'S001',
    '2005-04-15',
    u.id
FROM users u
WHERE u.username = 'student1'
  AND NOT EXISTS (
    SELECT 1
    FROM students
    WHERE student_number = 'S001'
);


-- Student 2
INSERT INTO users (
    username,
    password_hash,
    first_name,
    last_name,
    role
)
SELECT
    'student2',
    '$2a$10$dT96PvmFO9u5rV.Qo9FMle5Fr5HnVDKpsUm3S4KMadh8ygRYL5/aO',
    'Anna',
    'Laine',
    'STUDENT'
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'student2'
);

INSERT INTO students (
    student_number,
    date_of_birth,
    user_id
)
SELECT
    'S002',
    '2006-02-20',
    u.id
FROM users u
WHERE u.username = 'student2'
  AND NOT EXISTS (
    SELECT 1
    FROM students
    WHERE student_number = 'S002'
);


-- Student 3
INSERT INTO users (
    username,
    password_hash,
    first_name,
    last_name,
    role
)
SELECT
    'student3',
    '$2a$10$dT96PvmFO9u5rV.Qo9FMle5Fr5HnVDKpsUm3S4KMadh8ygRYL5/aO',
    'Matti',
    'Nieminen',
    'STUDENT'
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'student3'
);

INSERT INTO students (
    student_number,
    date_of_birth,
    user_id
)
SELECT
    'S003',
    '2005-09-12',
    u.id
FROM users u
WHERE u.username = 'student3'
  AND NOT EXISTS (
    SELECT 1
    FROM students
    WHERE student_number = 'S003'
);


-- Student 4
INSERT INTO users (
    username,
    password_hash,
    first_name,
    last_name,
    role
)
SELECT
    'student4',
    '$2a$10$dT96PvmFO9u5rV.Qo9FMle5Fr5HnVDKpsUm3S4KMadh8ygRYL5/aO',
    'Sofia',
    'Virtanen',
    'STUDENT'
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE username = 'student4'
);

INSERT INTO students (
    student_number,
    date_of_birth,
    user_id
)
SELECT
    'S004',
    '2006-06-05',
    u.id
FROM users u
WHERE u.username = 'student4'
  AND NOT EXISTS (
    SELECT 1
    FROM students
    WHERE student_number = 'S004'
);


/*
 Academic Groups
 */

INSERT INTO academic_groups (
    name
)
SELECT
    'TVT25K-0'
    WHERE NOT EXISTS (
    SELECT 1
    FROM academic_groups
    WHERE name = 'TVT25K-0'
);

INSERT INTO academic_groups (
    name
)
SELECT
    'Group 2'
    WHERE NOT EXISTS (
    SELECT 1
    FROM academic_groups
    WHERE name = 'Group 2'
);

INSERT INTO academic_groups (
    name
)
SELECT
    'Group 3'
    WHERE NOT EXISTS (
    SELECT 1
    FROM academic_groups
    WHERE name = 'Group 3'
);


/*
 Courses
 */

INSERT INTO courses (
    code,
    name,
    user_id,
    academic_group_id
)
SELECT
    'SE01',
    'Software Engineering',
    u.id,
    ag.id
FROM users u
         JOIN academic_groups ag
              ON ag.name = 'TVT25K-0'
WHERE u.username = 'teacher1'
  AND NOT EXISTS (
    SELECT 1
    FROM courses
    WHERE code = 'SE01'
);

INSERT INTO courses (
    code,
    name,
    user_id,
    academic_group_id
)
SELECT
    'DB01',
    'Database Systems',
    u.id,
    ag.id
FROM users u
         JOIN academic_groups ag
              ON ag.name = 'Group 2'
WHERE u.username = 'teacher2'
  AND NOT EXISTS (
    SELECT 1
    FROM courses
    WHERE code = 'DB01'
);

INSERT INTO courses (
    code,
    name,
    user_id,
    academic_group_id
)
SELECT
    'PR01',
    'Programming',
    u.id,
    ag.id
FROM users u
         JOIN academic_groups ag
              ON ag.name = 'Group 3'
WHERE u.username = 'teacher3'
  AND NOT EXISTS (
    SELECT 1
    FROM courses
    WHERE code = 'PR01'
);

INSERT INTO courses (
    code,
    name,
    user_id,
    academic_group_id
)
SELECT
    'MA01',
    'Mathematics',
    u.id,
    ag.id
FROM users u
         JOIN academic_groups ag
              ON ag.name = 'Group 3'
WHERE u.username = 'teacher1'
  AND NOT EXISTS (
    SELECT 1
    FROM courses
    WHERE code = 'MA01'
);


/*
 Enrollments
 */

INSERT INTO enrollments (
    student_id,
    course_id,
    academic_group_id,
    status
)
SELECT
    s.id,
    c.id,
    ag.id,
    'ENROLLED'
FROM students s
         JOIN courses c
              ON c.code = 'SE01'
         JOIN academic_groups ag
              ON ag.name = 'TVT25K-0'
WHERE s.student_number = 'S001'
  AND NOT EXISTS (
    SELECT 1
    FROM enrollments e
    WHERE e.student_id = s.id
      AND e.course_id = c.id
);

INSERT INTO enrollments (
    student_id,
    course_id,
    academic_group_id,
    status
)
SELECT
    s.id,
    c.id,
    ag.id,
    'ENROLLED'
FROM students s
         JOIN courses c
              ON c.code = 'DB01'
         JOIN academic_groups ag
              ON ag.name = 'Group 2'
WHERE s.student_number = 'S002'
  AND NOT EXISTS (
    SELECT 1
    FROM enrollments e
    WHERE e.student_id = s.id
      AND e.course_id = c.id
);

INSERT INTO enrollments (
    student_id,
    course_id,
    academic_group_id,
    status
)
SELECT
    s.id,
    c.id,
    ag.id,
    'ENROLLED'
FROM students s
         JOIN courses c
              ON c.code = 'PR01'
         JOIN academic_groups ag
              ON ag.name = 'Group 3'
WHERE s.student_number = 'S003'
  AND NOT EXISTS (
    SELECT 1
    FROM enrollments e
    WHERE e.student_id = s.id
      AND e.course_id = c.id
);

INSERT INTO enrollments (
    student_id,
    course_id,
    academic_group_id,
    status
)
SELECT
    s.id,
    c.id,
    ag.id,
    'ENROLLED'
FROM students s
         JOIN courses c
              ON c.code = 'MA01'
         JOIN academic_groups ag
              ON ag.name = 'Group 3'
WHERE s.student_number = 'S004'
  AND NOT EXISTS (
    SELECT 1
    FROM enrollments e
    WHERE e.student_id = s.id
      AND e.course_id = c.id
);


/*
 Assessments
 */

INSERT INTO assessments (
    course_id,
    title,
    type,
    max_score,
    weight,
    due_date
)
SELECT
    c.id,
    'Midterm Exam',
    'EXAM1',
    100,
    30,
    '2026-10-15'
FROM courses c
WHERE c.code = 'SE01'
  AND NOT EXISTS (
    SELECT 1
    FROM assessments a
    WHERE a.course_id = c.id
      AND a.title = 'Midterm Exam'
);

INSERT INTO assessments (
    course_id,
    title,
    type,
    max_score,
    weight,
    due_date
)
SELECT
    c.id,
    'Database Assignment',
    'HOMETASK',
    100,
    30,
    '2026-10-20'
FROM courses c
WHERE c.code = 'DB01'
  AND NOT EXISTS (
    SELECT 1
    FROM assessments a
    WHERE a.course_id = c.id
      AND a.title = 'Database Assignment'
);

INSERT INTO assessments (
    course_id,
    title,
    type,
    max_score,
    weight,
    due_date
)
SELECT
    c.id,
    'Programming Project',
    'PROJECT',
    100,
    40,
    '2026-11-01'
FROM courses c
WHERE c.code = 'PR01'
  AND NOT EXISTS (
    SELECT 1
    FROM assessments a
    WHERE a.course_id = c.id
      AND a.title = 'Programming Project'
);

INSERT INTO assessments (
    course_id,
    title,
    type,
    max_score,
    weight,
    due_date
)
SELECT
    c.id,
    'Mathematics Exam',
    'EXAM2',
    100,
    30,
    '2026-10-25'
FROM courses c
WHERE c.code = 'MA01'
  AND NOT EXISTS (
    SELECT 1
    FROM assessments a
    WHERE a.course_id = c.id
      AND a.title = 'Mathematics Exam'
);


/*
 Grades
 */

INSERT INTO grades (
    enrollment_id,
    assessment_id,
    score,
    comment
)
SELECT
    e.id,
    a.id,
    85,
    'Good performance'
FROM enrollments e
         JOIN students s
              ON e.student_id = s.id
         JOIN courses c
              ON e.course_id = c.id
         JOIN assessments a
              ON a.course_id = c.id
WHERE s.student_number = 'S001'
  AND c.code = 'SE01'
  AND a.title = 'Midterm Exam'
  AND NOT EXISTS (
    SELECT 1
    FROM grades g
    WHERE g.enrollment_id = e.id
      AND g.assessment_id = a.id
);


INSERT INTO grades (
    enrollment_id,
    assessment_id,
    score,
    comment
)
SELECT
    e.id,
    a.id,
    90,
    'Very good work'
FROM enrollments e
         JOIN students s
              ON e.student_id = s.id
         JOIN courses c
              ON e.course_id = c.id
         JOIN assessments a
              ON a.course_id = c.id
WHERE s.student_number = 'S002'
  AND c.code = 'DB01'
  AND a.title = 'Database Assignment'
  AND NOT EXISTS (
    SELECT 1
    FROM grades g
    WHERE g.enrollment_id = e.id
      AND g.assessment_id = a.id
);


INSERT INTO grades (
    enrollment_id,
    assessment_id,
    score,
    comment
)
SELECT
    e.id,
    a.id,
    88,
    'Good programming skills'
FROM enrollments e
         JOIN students s
              ON e.student_id = s.id
         JOIN courses c
              ON e.course_id = c.id
         JOIN assessments a
              ON a.course_id = c.id
WHERE s.student_number = 'S003'
  AND c.code = 'PR01'
  AND a.title = 'Programming Project'
  AND NOT EXISTS (
    SELECT 1
    FROM grades g
    WHERE g.enrollment_id = e.id
      AND g.assessment_id = a.id
);


INSERT INTO grades (
    enrollment_id,
    assessment_id,
    score,
    comment
)
SELECT
    e.id,
    a.id,
    92,
    'Excellent result'
FROM enrollments e
         JOIN students s
              ON e.student_id = s.id
         JOIN courses c
              ON e.course_id = c.id
         JOIN assessments a
              ON a.course_id = c.id
WHERE s.student_number = 'S004'
  AND c.code = 'MA01'
  AND a.title = 'Mathematics Exam'
  AND NOT EXISTS (
    SELECT 1
    FROM grades g
    WHERE g.enrollment_id = e.id
      AND g.assessment_id = a.id
);