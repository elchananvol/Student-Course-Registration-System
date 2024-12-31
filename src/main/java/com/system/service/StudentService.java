package com.system.service;

import com.system.exceptions.UserExceptions;
import com.system.model.*;
import com.system.repository.AdminRepository;
import com.system.repository.StudentRepository;
import com.system.security.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordGenerator passwordGenerator;

    @Transactional
    public StudentResponse createStudent(StudentCreationRequest studentCreationRequest) {
        validateUniqueEmail(studentCreationRequest.getEmail(),null);
        Student student = Student.builder().name(studentCreationRequest.getName()).email(studentCreationRequest.getEmail()).build();
        String generatedPassword = passwordGenerator.generatePassword();
        String encryptedPassword = passwordGenerator.encryptPassword(generatedPassword);
        student.setPasswordHash(encryptedPassword);
        student = studentRepository.save(student);
        StudentResponse response = mapToStudentResponse(student);
        response.setPassword(generatedPassword);
        return response;

    }
    @Transactional
    public StudentResponse updateStudent(Integer studentId, StudentUpdateRequest updateRequest) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserExceptions.NotFound("Student not found with id: " + studentId));
        if (updateRequest.getName() != null) {
            student.setName(updateRequest.getName());
        }

        if (updateRequest.getEmail() != null) {
            validateUniqueEmail(updateRequest.getEmail(),studentId);
            student.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getPassword() != null) {
            student.setPasswordHash(passwordGenerator.encryptPassword(updateRequest.getPassword()));
        }
        Student updatedStudent = studentRepository.save(student);
        StudentResponse response = mapToStudentResponse(updatedStudent);
        if (updateRequest.getPassword() != null) {
            response.setPassword(updateRequest.getPassword());
        }
        return response;
    }

    @Transactional
    public void deleteStudent(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserExceptions.NotFound("Student not found with id: " + studentId));
        studentRepository.delete(student);
    }


    public StudentResponse getStudentById(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new UserExceptions.NotFound("Student not found with id: " + studentId));

        return mapToStudentResponse(student);
    }

    public List<StudentResponse> getAllStudentsWithCourses() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::mapToStudentResponse).toList();
    }

    private StudentResponse mapToStudentResponse(Student student) {
        StudentResponse studentResponse =  new StudentResponse(student.getId(),student.getName(),student.getEmail(),student.getPasswordHash());
        studentResponse.setEnrolledCourseIds(student.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toList()));
        return studentResponse;

    }

    public Set<Integer> getRegisteredCourses(Integer studentId) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new UserExceptions.NotFound("Student not found");
        }
        Student student = studentOpt.get();
        return student.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
    }

    private void validateUniqueEmail(String email, Integer studentId){
        studentRepository.findByEmail(email)
                .filter(s -> !s.getId().equals(studentId))
                .ifPresent(s -> {
                    throw new UserExceptions.StudentRegistration("Email already in use");
                });

        // Check if the email is already used by an admin
        adminRepository.findByEmail(email).ifPresent(s -> {
            throw new UserExceptions.StudentRegistration("Email already in use");
        });

    }
}