package com.system.controller;

import com.system.model.Student;
import com.system.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public Student login(@RequestParam String specialKey) {
        return studentService.login(specialKey)
                .orElseThrow(() -> new RuntimeException("Invalid special key"));
    }

    @PostMapping("/{studentId}/courses/{courseId}/register")
    public String registerForCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        studentService.registerForCourse(studentId, courseId);
        return "Registered successfully";
    }

    // Other endpoints for canceling registration, etc.
}