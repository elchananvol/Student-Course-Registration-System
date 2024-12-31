package com.system.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "admins", schema = "registration")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

}
