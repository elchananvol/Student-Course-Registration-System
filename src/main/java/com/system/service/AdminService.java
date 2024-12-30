package com.system.service;

import com.system.model.Admin;
import com.system.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Optional<Admin> login(String email, String password) {
        return null;
//        return adminRepository.findByEmail(email)
//                .filter(admin -> admin.getPassword().equals(password));
    }
//    public Admin authenticate(Admin admin) {
//        if ("admin@example.com".equals(admin.getEmail()) && "password123".equals(admin.getPassword())) {
//            return new Admin(1L, "Admin Name", admin.getEmail());
//        }
//        return null;
//    }

    // Methods for CRUD operations on students and courses
}