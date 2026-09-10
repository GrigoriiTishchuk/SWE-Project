INSERT INTO users (
    username,
    password_hash,
    first_name,
    last_name,
    role
)
SELECT
    'admin',
    'demo_password',
    'Admin',
    'User',
    'ADMIN'
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
    'demo_password',
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
    'demo_password',
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
    'demo_password',
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

INSERT INTO students (
    student_number,
    first_name,
    last_name,
    date_of_birth
)
SELECT
    'S001',
    'Heikki',
    'Heikkinen',
    '2005-04-15'
    WHERE NOT EXISTS (
    SELECT 1
    FROM students
    WHERE student_number = 'S001'
);

INSERT INTO students (
    student_number,
    first_name,
    last_name,
    date_of_birth
)
SELECT
    'S002',
    'Anna',
    'Laine',
    '2006-02-20'
    WHERE NOT EXISTS (
    SELECT 1
    FROM students
    WHERE student_number = 'S002'
);

INSERT INTO students (
    student_number,
    first_name,
    last_name,
    date_of_birth
)
SELECT
    'S003',
    'Matti',
    'Nieminen',
    '2005-09-12'
    WHERE NOT EXISTS (
    SELECT 1
    FROM students
    WHERE student_number = 'S003'
);

INSERT INTO students (
    student_number,
    first_name,
    last_name,
    date_of_birth
)
SELECT
    'S004',
    'Sofia',
    'Virtanen',
    '2006-06-05'
    WHERE NOT EXISTS (
    SELECT 1
    FROM students
    WHERE student_number = 'S004'
);

/*
 Courses
 */

INSERT INTO courses (
    code,
    name,
    user_id
)
SELECT
    'SE01',
    'Software Engineering',
    id
FROM users
WHERE username = 'teacher1'
  AND NOT EXISTS (
    SELECT 1
    FROM courses
    WHERE code = 'SE01'
);

INSERT INTO courses (
    code,
    name,
    user_id
)
SELECT
    'DB01',
    'Database Systems',
    id
FROM users
WHERE username = 'teacher2'
  AND NOT EXISTS (
    SELECT 1
    FROM courses
    WHERE code = 'DB01'
);

INSERT INTO courses (
    code,
    name,
    user_id
)
SELECT
    'PR01',
    'Programming',
    id
FROM users
WHERE username = 'teacher3'
  AND NOT EXISTS (
    SELECT 1
    FROM courses
    WHERE code = 'PR01'
);

INSERT INTO courses (
    code,
    name,
    user_id
)
SELECT
    'MA01',
    'Mathematics',
    id
FROM users
WHERE username = 'teacher1'
  AND NOT EXISTS (
    SELECT 1
    FROM courses
    WHERE code = 'MA01'
);

/*
 Academic Groups
 */

INSERT INTO academic_groups (
    name
)
SELECT
    'Software Engineering'
    WHERE NOT EXISTS (
    SELECT 1
    FROM academic_groups
    WHERE name = 'Software Engineering'
);

INSERT INTO academic_groups (
    name
)
SELECT
    'Database Systems'
    WHERE NOT EXISTS (
    SELECT 1
    FROM academic_groups
    WHERE name = 'Database Systems'
);

INSERT INTO academic_groups (
    name
)
SELECT
    'Programming'
    WHERE NOT EXISTS (
    SELECT 1
    FROM academic_groups
    WHERE name = 'Programming'
);

INSERT INTO academic_groups (
    name
)
SELECT
    'Mathematics'
    WHERE NOT EXISTS (
    SELECT 1
    FROM academic_groups
    WHERE name = 'Mathematics'
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
              ON ag.name = 'Software Engineering'
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
              ON ag.name = 'Database Systems'
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
              ON ag.name = 'Programming'
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
              ON ag.name = 'Mathematics'
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
    'EXAM',
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
    'ASSIGNMENT',
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
    'EXAM',
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
    student_id,
    assessment_id,
    score,
    comment
)
SELECT
    s.id,
    a.id,
    85,
    'Good performance'
FROM students s
         JOIN assessments a
              ON a.title = 'Midterm Exam'
         JOIN courses c
              ON c.id = a.course_id
WHERE s.student_number = 'S001'
  AND c.code = 'SE01'
  AND NOT EXISTS (
    SELECT 1
    FROM grades g
    WHERE g.student_id = s.id
      AND g.assessment_id = a.id
);

INSERT INTO grades (
    student_id,
    assessment_id,
    score,
    comment
)
SELECT
    s.id,
    a.id,
    90,
    'Very good work'
FROM students s
         JOIN assessments a
              ON a.title = 'Database Assignment'
         JOIN courses c
              ON c.id = a.course_id
WHERE s.student_number = 'S002'
  AND c.code = 'DB01'
  AND NOT EXISTS (
    SELECT 1
    FROM grades g
    WHERE g.student_id = s.id
      AND g.assessment_id = a.id
);

INSERT INTO grades (
    student_id,
    assessment_id,
    score,
    comment
)
SELECT
    s.id,
    a.id,
    88,
    'Good programming skills'
FROM students s
         JOIN assessments a
              ON a.title = 'Programming Project'
         JOIN courses c
              ON c.id = a.course_id
WHERE s.student_number = 'S003'
  AND c.code = 'PR01'
  AND NOT EXISTS (
    SELECT 1
    FROM grades g
    WHERE g.student_id = s.id
      AND g.assessment_id = a.id
);

INSERT INTO grades (
    student_id,
    assessment_id,
    score,
    comment
)
SELECT
    s.id,
    a.id,
    92,
    'Excellent result'
FROM students s
         JOIN assessments a
              ON a.title = 'Mathematics Exam'
         JOIN courses c
              ON c.id = a.course_id
WHERE s.student_number = 'S004'
  AND c.code = 'MA01'
  AND NOT EXISTS (
    SELECT 1
    FROM grades g
    WHERE g.student_id = s.id
      AND g.assessment_id = a.id
);

