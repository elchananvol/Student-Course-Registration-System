-- Create Admin Table
CREATE TABLE admins (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL,
                        UNIQUE INDEX idx_admin_email (email)
);

-- Create Student Table
CREATE TABLE students (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          UNIQUE INDEX idx_students_email (email)
);

-- Create Course Table
CREATE TABLE courses (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT
);

-- Create Student_Course Join Table
CREATE TABLE registration_student_courses (
                                 student_id INT NOT NULL,
                                 course_id INT NOT NULL,
                                 PRIMARY KEY (student_id, course_id),
                                 FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                                 FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
                                 INDEX idx_student_id (student_id),
                                 INDEX idx_course_id (course_id)
#                                  CONSTRAINT Student_Course_Limit CHECK (
#                                  (SELECT COUNT(*) FROM registration_student_courses WHERE student_id = Student_Course.student_id) <= 2),
#                                  CONSTRAINT Course_Student_Limit CHECK (
#                                  (SELECT COUNT(*) FROM registration_student_courses WHERE course_id = registration_student_courses.course_id) <= 30)
);