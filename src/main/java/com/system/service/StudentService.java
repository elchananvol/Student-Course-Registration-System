package com.system.service;

import com.system.model.*;
import com.system.repository.AdminRepository;
import com.system.repository.StudentRepository;
import com.system.security.PasswordGenerator;
import com.system.util.Exceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public StudentResponse createStudent(StudentCreationRequest studentCreationRequest) {
        validateUniqueEmail(studentCreationRequest.getEmail(),null);
        Student student = Student.builder().name(studentCreationRequest.getName()).email(studentCreationRequest.getEmail()).build();
        String generatedPassword = PasswordGenerator.generatePassword();
        String encryptedPassword = passwordEncoder.encode(generatedPassword);
        student.setPasswordHash(encryptedPassword);
        student = studentRepository.save(student);
        student.setPasswordHash(generatedPassword);

        return mapToStudentResponse(student);
    }
    @Transactional
    public StudentResponse updateStudent(Integer studentId, StudentUpdateRequest updateRequest) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new Exceptions.NotFound("Student not found with id: " + studentId));

        // Update only the fields that are not null
        if (updateRequest.getName() != null) {
            student.setName(updateRequest.getName());
        }

        if (updateRequest.getEmail() != null) {
            validateUniqueEmail(updateRequest.getEmail(),studentId);
            student.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null) {
            student.setPasswordHash(passwordEncoder.encode(updateRequest.getPassword()));
        }
        Student updatedStudent = studentRepository.save(student);

        if (updateRequest.getPassword() != null) {
            updatedStudent.setPasswordHash(updateRequest.getPassword());
        }
        return mapToStudentResponse(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new Exceptions.NotFound("Student not found with id: " + studentId));

        // Remove the student from all enrolled courses
        student.getCourses().forEach(course -> course.getStudents().remove(student));

        studentRepository.delete(student);
    }


    public StudentResponse getStudentById(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new Exceptions.NotFound("Student not found with id: " + studentId));

        return mapToStudentResponse(student);
    }

    public List<StudentResponse> getAllStudentsWithCourses() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::mapToStudentResponse).toList();
    }

    private StudentResponse mapToStudentResponse(Student student) {
        StudentResponse studentResponse =  new StudentResponse(student.getId(),student.getName(),student.getEmail(),student.getPasswordHash());
        studentResponse.setCoursesId(student.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toList()));
        return studentResponse;

    }
    private void validateUniqueEmail(String email, Integer studentId){
        studentRepository.findByEmail(email)
                .filter(s -> !s.getId().equals(studentId))
                .ifPresent(s -> {
                    throw new Exceptions.StudentRegistration("Email already in use");
                });

        // Check if the email is already used by an admin
        adminRepository.findByEmail(email).ifPresent(s -> {
            throw new Exceptions.StudentRegistration("Email already in use");
        });

    }
}