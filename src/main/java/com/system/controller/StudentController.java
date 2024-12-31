package com.system.controller;

import com.system.exceptions.UserExceptions;
import com.system.model.*;
import com.system.repository.*;
import com.system.service.StudentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private EntityManager entityManager;


    @Transactional
    @PostMapping("/courses")
    public ResponseEntity<?> registerCourse(@Valid @RequestBody CourseRegistrationRequest request, Authentication authentication) {
        logger.info("Attempting to register student with ID: {} for course with ID: {}", request.getStudentId(), request.getCourseId());
        authenticateStudent(request.getStudentId(), authentication);

        Student student = entityManager.find(Student.class, request.getStudentId(), LockModeType.PESSIMISTIC_WRITE);
        Course course = entityManager.find(Course.class, request.getCourseId(), LockModeType.PESSIMISTIC_WRITE);

        if (student == null || course == null) {
            throw new UserExceptions.InvalidInput("Invalid input: student or course not found");
        }

        if (student.getCourses().contains(course)) {
            throw new UserExceptions.CourseRegistration("The student is already enrolled in this course");
        }
        if (student.getCourses().size() >= 2) {
            throw new UserExceptions.CourseRegistration("The student can't enroll in more than 2 courses");
        }
        if (course.getStudents().size() >= 30) {
            throw new UserExceptions.CourseRegistration("Course is already full");
        }
        student.getCourses().add(course);
        course.getStudents().add(student);

        logger.info("Registration successful for student (ID: {}) to course (ID: {})", request.getStudentId(), request.getCourseId());
        return ResponseEntity.ok("Registration to the course was successful.");
    }

    @Transactional
    @DeleteMapping("/courses")
    public ResponseEntity<?> cancelCourseRegistration(@Valid @RequestBody CourseRegistrationRequest request, Authentication authentication) {
        authenticateStudent(request.getStudentId(), authentication);

        logger.info("Attempting to cancel registration for student with ID: {} from course with ID: {}", request.getStudentId(), request.getCourseId());

        Student student = entityManager.find(Student.class, request.getStudentId(), LockModeType.PESSIMISTIC_WRITE);
        Course course = entityManager.find(Course.class, request.getCourseId(), LockModeType.PESSIMISTIC_WRITE);

        if (student == null || course == null) {
            throw new UserExceptions.InvalidInput("Invalid input: student or course not found");
        }
        if (!student.getCourses().contains(course)) {
            throw new UserExceptions.InvalidInput(("The student is not enrolled in this course."));
        }

        student.getCourses().remove(course);
        course.getStudents().remove(student);
        logger.info("Successfully canceled registration for student (ID: {}) from course (ID: {})", request.getStudentId(), request.getCourseId());
        return ResponseEntity.ok("Registration was successfully canceled.");
    }

    @GetMapping("/courses")
    public ResponseEntity<?> getRegisteredCourses(@RequestParam Integer studentId, Authentication authentication) {
        logger.debug("Received request to get registered courses for student with ID: {}", studentId);
        authenticateStudent(studentId, authentication);
        Set<Integer> courses = studentService.getRegisteredCourses(studentId);
        return ResponseEntity.ok(courses);
    }


    private void authenticateStudent(Integer studentId, Authentication authentication) {
        Integer studentIdFromToken = Integer.valueOf((String) authentication.getPrincipal());
        if (!studentIdFromToken.equals(studentId)) {
            logger.debug("Wrong authentication for student: {}", studentId);
            throw new UserExceptions.UnauthorizedAccess();
        }
    }

}