CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    phone VARCHAR(30),
    email VARCHAR(255),
    photo VARCHAR(255),
    preferred_locale VARCHAR(10) DEFAULT 'en_US'
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS academic_groups (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_number VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    user_id INT,
    academic_group_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (academic_group_id) REFERENCES academic_groups(id)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS courses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    user_id INT,
    academic_group_id INT,
    start_date DATE,
    end_date DATE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (academic_group_id) REFERENCES academic_groups(id)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS enrollments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    academic_group_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (course_id) REFERENCES courses(id),
    FOREIGN KEY (academic_group_id) REFERENCES academic_groups(id)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS assessments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    max_score DECIMAL(10,2) NOT NULL,
    weight DECIMAL(5,2) NOT NULL,
    due_date DATE,
    FOREIGN KEY (course_id) REFERENCES courses(id)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS grades (
    id INT PRIMARY KEY AUTO_INCREMENT,
    enrollment_id INT NOT NULL,
    assessment_id INT NOT NULL,
    score DECIMAL(10,2) NOT NULL,
    comment TEXT,
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(id),
    FOREIGN KEY (assessment_id) REFERENCES assessments(id)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS seed_log (id INT PRIMARY KEY) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Idempotent migration: adds preferred_locale to databases created before this sprint
ALTER TABLE users ADD COLUMN IF NOT EXISTS preferred_locale VARCHAR(10) DEFAULT 'en_US';
