package com.system.controller;

import com.system.model.Admin;
import com.system.model.Student;
import com.system.model.Course;
import com.system.service.AdminService;
import com.system.service.StudentService;
import com.system.service.CourseService;
import com.system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtUtil jwtUtil;

    // Admin Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        Admin authenticatedAdmin = adminService.authenticate(admin);
        if (authenticatedAdmin != null) {
            String token = jwtUtil.generateToken(authenticatedAdmin.getEmail());
            return ResponseEntity.ok(new LoginResponse(token, authenticatedAdmin.getId(), authenticatedAdmin.getName()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
        }
    }

    // Create Student
    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestHeader("Authorization") String token, @RequestBody @Valid Student student) {
        String adminEmail = jwtUtil.extractUsername(token.substring(7));
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    // Retrieve All Students
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents(@RequestHeader("Authorization") String token) {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // Retrieve Student by ID
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    // Update Student
    @PutMapping("/students/{id}")
    public ResponseEntity<?> updateStudent(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    }

    // Delete Student
    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Create Course
    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestHeader("Authorization") String token, @RequestBody @Valid Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    // Retrieve All Courses
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAllCourses(@RequestHeader("Authorization") String token) {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    // Retrieve Course by ID
    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    // Update Course
    @PutMapping("/courses/{id}")
    public ResponseEntity<?> updateCourse(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return ResponseEntity.ok(updatedCourse);
    }

    // Delete Course
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> deleteCourse(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Helper class for login response
    public static class LoginResponse {
        private String token;
        private Long adminId;
        private String name;

        public LoginResponse(String token, Long adminId, String name) {
            this.token = token;
            this.adminId = adminId;
            this.name = name;
        }

        public String getToken() {
            return token;
        }

        public Long getAdminId() {
            return adminId;
        }

        public String getName() {
            return name;
        }
    }
}
