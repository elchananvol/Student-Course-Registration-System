package com.system.controller;

import com.system.model.Course;
import com.system.model.CourseRegistrationRequest;
import com.system.model.Student;
import com.system.repository.*;
import com.system.security.CustomUserDetails;
import com.system.util.Exceptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager entityManager; // Inject EntityManager

    @Transactional
    @PostMapping("/courses")
    public ResponseEntity<?> registerCourse(@Valid @RequestBody CourseRegistrationRequest request, Authentication authentication) {
        authenticateStudent(request.getStudentId(), authentication);

        Student student = entityManager.find(Student.class, request.getStudentId(), LockModeType.PESSIMISTIC_WRITE);
        Course course = entityManager.find(Course.class, request.getCourseId(), LockModeType.PESSIMISTIC_WRITE);

        if (student == null || course == null) {
            throw new Exceptions.InvalidInput("Student or Course not found");
        }

        if (student.getCourses().contains(course)) {
            throw new Exceptions.CourseRegistration("The student is already enrolled in this course.");
        }
        if (student.getCourses().size() >= 2) {
            throw new Exceptions.CourseRegistration("You can not register to more than 2 courses");
        }
        if (course.getStudents().size() >= 30) {
            throw new Exceptions.CourseRegistration("The course is already full.");
        }
        student.getCourses().add(course);
        course.getStudents().add(student);

        // Save changes
        studentRepository.save(student);
        courseRepository.save(course);

        return ResponseEntity.ok("Registration to the course was successful.");
    }
    @Transactional
    @DeleteMapping("/courses")
    public ResponseEntity<?> cancelCourseRegistration(@Valid @RequestBody CourseRegistrationRequest request, Authentication authentication) {
        authenticateStudent(request.getStudentId(), authentication);

        // Fetch student and course with pessimistic lock
        Student student = entityManager.find(Student.class, request.getStudentId(), LockModeType.PESSIMISTIC_WRITE);
        Course course = entityManager.find(Course.class, request.getCourseId(), LockModeType.PESSIMISTIC_WRITE);

        if (student == null || course == null) {
            throw new Exceptions.InvalidInput("Student or Course not found");
        }

        // Check if student is enrolled in this course
        if (!student.getCourses().contains(course)) {
            throw new Exceptions.InvalidInput(("The student is not enrolled in this course."));
        }

        student.getCourses().remove(course);
        course.getStudents().remove(student);

        studentRepository.save(student);
        courseRepository.save(course);

        return ResponseEntity.ok("Registration was successfully canceled.");
    }

    // New method to get registered courses
    @GetMapping("/courses")
    public ResponseEntity<?> getRegisteredCourses(@RequestParam Integer studentId, Authentication authentication) {
        authenticateStudent(studentId, authentication);

        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new Exceptions.NotFound("Student not found");
        }
        Student student = studentOpt.get();
        Set<Integer> courses = student.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(courses);
    }


    private void authenticateStudent(Integer studentId, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer studentIdFromToken = userDetails.getStudentId();
        if (!studentIdFromToken.equals(studentId)) {
            throw new Exceptions.UnauthorizedAccess();
        }
    }

}