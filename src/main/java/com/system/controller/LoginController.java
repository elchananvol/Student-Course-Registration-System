package com.system.controller;


import com.system.model.Admin;
import com.system.model.LoginRequest;
import com.system.model.LoginResponse;
import com.system.model.Student;
import com.system.repository.AdminRepository;
import com.system.repository.StudentRepository;
import com.system.security.JwtUtil;
import com.system.exceptions.UserExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping({"/admin"})
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest request) {

        Optional<Admin> adminOpt = adminRepository.findByEmail(request.getEmail());
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();

            if (BCrypt.checkpw(request.getPassword(), admin.getPasswordHash())) {
                String token = jwtUtil.generateToken(admin.getId(), LoginResponse.RoleEnum.ADMIN);
                logger.debug("Generated JWT token for admin with ID: {}", admin.getId());
                return ResponseEntity.ok(new LoginResponse(token, LoginResponse.RoleEnum.ADMIN));
            }
        }
        logger.debug("Failed login attempt for admin: {}", request);
        throw new UserExceptions.UnauthorizedAccess();
    }

    @PostMapping({"/student"})
    public ResponseEntity<?> studentLogin(@RequestBody LoginRequest request) {

        Optional<Student> studentOpt = studentRepository.findByEmail(request.getEmail());
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (BCrypt.checkpw(request.getPassword(), student.getPasswordHash())) {
                String token = jwtUtil.generateToken(student.getId(), LoginResponse.RoleEnum.STUDENT);
                logger.debug("Generated JWT token for student with ID: {}", student.getId());
                return ResponseEntity.ok(new LoginResponse(token, LoginResponse.RoleEnum.STUDENT));
            }
        }
        logger.debug("Failed login attempt for student: {}", request);
        throw new UserExceptions.UnauthorizedAccess();
    }
}

