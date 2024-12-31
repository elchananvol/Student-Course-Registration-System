package com.system.controller;

import com.system.model.*;
import com.system.service.StudentService;
import com.system.service.CourseService;

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


    @PostMapping("/courses")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseCreationRequest courseCreationRequest) {
        CourseResponse courseResponse = courseService.createCourse(courseCreationRequest);
        return ResponseEntity.ok(courseResponse);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCoursesWithStudents();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable("id") Integer courseId) {
        CourseResponse courseResponse = courseService.getCourseById(courseId);
        return ResponseEntity.ok(courseResponse);
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable("id") Integer courseId,
            @Valid @RequestBody CourseUpdateRequest updateRequest) {
        CourseResponse updatedCourse = courseService.updateCourse(courseId, updateRequest);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("id") Integer courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/students")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentCreationRequest studentCreationRequest) {
        StudentResponse studentResponse = studentService.createStudent(studentCreationRequest);
        return ResponseEntity.ok(studentResponse);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudentsWithCourses();
        return ResponseEntity.ok(students);
    }


    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable("id") Integer studentId) {
        StudentResponse studentResponse = studentService.getStudentById(studentId);
        return ResponseEntity.ok(studentResponse);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable("id") Integer studentId,
            @Valid @RequestBody StudentUpdateRequest updateRequest) {
        StudentResponse updatedStudent = studentService.updateStudent(studentId, updateRequest);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Integer studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

}
