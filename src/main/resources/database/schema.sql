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
    notes TEXT,
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

-- adds preferred_locale to databases created before this sprint
ALTER TABLE users ADD COLUMN IF NOT EXISTS preferred_locale VARCHAR(10) DEFAULT 'en_US';

-- adds notes column to courses for existing databases
ALTER TABLE courses ADD COLUMN IF NOT EXISTS notes TEXT;

CREATE TABLE IF NOT EXISTS assessment_types (
    type_key VARCHAR(30) PRIMARY KEY
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT IGNORE INTO assessment_types (type_key) VALUES
('INCLASSTASK'), ('HOMETASK'), ('PROJECT'), ('LAB'),
('EXAM1'), ('EXAM2'), ('EXAM3'), ('EXAM4'), ('FINALEXAM');

CREATE TABLE IF NOT EXISTS assessment_type_translation (
    type_key VARCHAR(30) NOT NULL,
    lang     VARCHAR(10) NOT NULL,
    name     VARCHAR(100) NOT NULL,
    PRIMARY KEY (type_key, lang),
    FOREIGN KEY (type_key) REFERENCES assessment_types(type_key)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS roles (
    role_key VARCHAR(30) PRIMARY KEY
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT IGNORE INTO roles (role_key) VALUES
('ADMINISTRATOR'), ('TEACHER'), ('STUDENT');

CREATE TABLE IF NOT EXISTS role_translation (
    role_key VARCHAR(30) NOT NULL,
    lang     VARCHAR(10) NOT NULL,
    label    VARCHAR(50) NOT NULL,
    PRIMARY KEY (role_key, lang),
    FOREIGN KEY (role_key) REFERENCES roles(role_key)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT IGNORE INTO assessment_type_translation (type_key, lang, name) VALUES
('INCLASSTASK', 'en_US', 'In-Class Task'),
('INCLASSTASK', 'ru_RU', 'Аудиторное задание'),
('INCLASSTASK', 'tr_TR', 'Sınıf İçi Görev'),
('INCLASSTASK', 'ar_SA', 'مهمة صفية'),
('HOMETASK',   'en_US', 'Homework'),
('HOMETASK',   'ru_RU', 'Домашнее задание'),
('HOMETASK',   'tr_TR', 'Ev Ödevi'),
('HOMETASK',   'ar_SA', 'واجب منزلي'),
('PROJECT',    'en_US', 'Project'),
('PROJECT',    'ru_RU', 'Проект'),
('PROJECT',    'tr_TR', 'Proje'),
('PROJECT',    'ar_SA', 'مشروع'),
('LAB',        'en_US', 'Lab Work'),
('LAB',        'ru_RU', 'Лабораторная работа'),
('LAB',        'tr_TR', 'Laboratuvar Çalışması'),
('LAB',        'ar_SA', 'عمل مختبري'),
('EXAM1',      'en_US', 'Exam 1'),
('EXAM1',      'ru_RU', 'Экзамен 1'),
('EXAM1',      'tr_TR', 'Sınav 1'),
('EXAM1',      'ar_SA', 'امتحان 1'),
('EXAM2',      'en_US', 'Exam 2'),
('EXAM2',      'ru_RU', 'Экзамен 2'),
('EXAM2',      'tr_TR', 'Sınav 2'),
('EXAM2',      'ar_SA', 'امتحان 2'),
('EXAM3',      'en_US', 'Exam 3'),
('EXAM3',      'ru_RU', 'Экзамен 3'),
('EXAM3',      'tr_TR', 'Sınav 3'),
('EXAM3',      'ar_SA', 'امتحان 3'),
('EXAM4',      'en_US', 'Exam 4'),
('EXAM4',      'ru_RU', 'Экзамен 4'),
('EXAM4',      'tr_TR', 'Sınav 4'),
('EXAM4',      'ar_SA', 'امتحان 4'),
('FINALEXAM',  'en_US', 'Final Exam'),
('FINALEXAM',  'ru_RU', 'Итоговый экзамен'),
('FINALEXAM',  'tr_TR', 'Final Sınavı'),
('FINALEXAM',  'ar_SA', 'الامتحان النهائي');

INSERT IGNORE INTO role_translation (role_key, lang, label) VALUES
('ADMINISTRATOR', 'en_US', 'Administrator'),
('ADMINISTRATOR', 'ru_RU', 'Администратор'),
('ADMINISTRATOR', 'tr_TR', 'Yönetici'),
('ADMINISTRATOR', 'ar_SA', 'مسؤول'),
('TEACHER',       'en_US', 'Teacher'),
('TEACHER',       'ru_RU', 'Преподаватель'),
('TEACHER',       'tr_TR', 'Öğretmen'),
('TEACHER',       'ar_SA', 'معلم'),
('STUDENT',       'en_US', 'Student'),
('STUDENT',       'ru_RU', 'Студент'),
('STUDENT',       'tr_TR', 'Öğrenci'),
('STUDENT',       'ar_SA', 'طالب');
