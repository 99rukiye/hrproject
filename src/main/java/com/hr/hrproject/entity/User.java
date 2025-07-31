package com.hr.hrproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(name = "account_locked")
    private boolean accountLocked;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked = true;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int loginAttempts;

    private boolean isBlocked = false;

    @Column(name = "failed_login_attempts", nullable = false, columnDefinition = "int default 0")
    private int failedLoginAttempts;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
}

