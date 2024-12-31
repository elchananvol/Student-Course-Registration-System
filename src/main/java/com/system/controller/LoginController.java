package com.system.controller;


import com.system.model.Admin;
import com.system.model.LoginRequest;
import com.system.model.LoginResponse;
import com.system.model.Student;
import com.system.repository.AdminRepository;
import com.system.repository.StudentRepository;
import com.system.security.JwtUtil;
import com.system.util.Exceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {


    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping({"/login", "/login/"})
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Try to authenticate as Admin
        Optional<Admin> adminOpt = adminRepository.findByEmail(request.getEmail());
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();

            if (BCrypt.checkpw(request.getPassword(), admin.getPasswordHash())) {
                String token = jwtUtil.generateToken(admin.getEmail(), LoginResponse.RoleEnum.ADMIN, null);
                return ResponseEntity.ok(new LoginResponse(token, LoginResponse.RoleEnum.ADMIN));
            } else {
                throw new Exceptions.UnauthorizedAccess();
            }
        }

        // Try to authenticate as Student
        Optional<Student> studentOpt = studentRepository.findByEmail(request.getEmail());
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (BCrypt.checkpw(request.getPassword(), student.getPasswordHash())) {
                String token = jwtUtil.generateToken(student.getEmail(), LoginResponse.RoleEnum.STUDENT, student.getId());
                return ResponseEntity.ok(new LoginResponse(token, LoginResponse.RoleEnum.STUDENT));
            } else {
                throw new Exceptions.UnauthorizedAccess();
            }
        }
        throw new Exceptions.UnauthorizedAccess();

    }
}

