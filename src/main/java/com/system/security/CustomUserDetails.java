package com.system.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.*;
import java.util.*;

public class CustomUserDetails implements UserDetails {

    private String email;
    private String role;
    private Integer studentId;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String email, String role, Integer studentId) {
        this.email = email;
        this.role = role;
        this.studentId = studentId;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    public Integer getStudentId() {
        return studentId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Return null for password since it's not needed here
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // These methods can return true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
