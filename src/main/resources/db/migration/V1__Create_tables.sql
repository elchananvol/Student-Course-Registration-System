-- Create Admin Table
CREATE TABLE admins (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL
);

-- Create Student Table
CREATE TABLE students (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          special_key VARCHAR(255) NOT NULL UNIQUE
);

-- Create Course Table
CREATE TABLE courses (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT
);

-- Create Student_Course Join Table
CREATE TABLE registration_student_courses (
                                 student_id BIGINT NOT NULL,
                                 course_id BIGINT NOT NULL,
                                 PRIMARY KEY (student_id, course_id),
                                 FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                                 FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);