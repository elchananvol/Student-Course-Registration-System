package com.system.controller;

import com.system.model.*;
import com.system.service.StudentService;
import com.system.service.CourseService;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;


    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/courses")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseCreationRequest courseCreationRequest) {
        logger.info("Creating a new course with request data: {}", courseCreationRequest);
        CourseResponse courseResponse = courseService.createCourse(courseCreationRequest);
        logger.info("Course created successfully with ID: {}", courseResponse.getId());
        return ResponseEntity.ok(courseResponse);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        logger.debug("Fetching all courses with student details");
        List<CourseResponse> courses = courseService.getAllCoursesWithStudents();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable("id") Integer courseId) {
        logger.debug("Fetching course details for course ID: {}", courseId);
        CourseResponse courseResponse = courseService.getCourseById(courseId);
        return ResponseEntity.ok(courseResponse);
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable("id") Integer courseId,
            @Valid @RequestBody CourseUpdateRequest updateRequest) {
        logger.info("Updating course with ID: {} using request: {}", courseId, updateRequest);
        CourseResponse updatedCourse = courseService.updateCourse(courseId, updateRequest);
        logger.debug("Course updated successfully with ID: {}", updatedCourse.getId());
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("id") Integer courseId) {
        logger.info("Deleting course with ID: {}", courseId);
        courseService.deleteCourse(courseId);
        logger.info("Course with ID: {} deleted successfully ", courseId);
        return ResponseEntity.noContent().build();
    }

// Student Controllers

    @PostMapping("/students")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentCreationRequest studentCreationRequest) {
        logger.info("Creating a new student with request data: {}", studentCreationRequest);
        StudentResponse studentResponse = studentService.createStudent(studentCreationRequest);
        logger.info("Student created successfully with ID: {}", studentResponse.getId());
        return ResponseEntity.ok(studentResponse);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        logger.info("Fetching all students with course details");
        List<StudentResponse> students = studentService.getAllStudentsWithCourses();
        logger.info("Retrieved {} students", students.size());
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable("id") Integer studentId) {
        logger.debug("Fetching student details for student ID: {}", studentId);
        StudentResponse studentResponse = studentService.getStudentById(studentId);
        logger.debug("Retrieved student details for student ID: {}. ", studentId);
        return ResponseEntity.ok(studentResponse);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable("id") Integer studentId,
            @Valid @RequestBody StudentUpdateRequest updateRequest) {

        logger.info("Updating student with ID: {}", studentId);
        StudentResponse updatedStudent = studentService.updateStudent(studentId, updateRequest);
        logger.info("Student updated successfully with ID: {}", updatedStudent.getId());
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Integer studentId) {
        logger.info("Deleting student with ID: {}", studentId);
        studentService.deleteStudent(studentId);
        logger.info("Student deleted successfully with ID: {}", studentId);
        return ResponseEntity.noContent().build();
    }

}
