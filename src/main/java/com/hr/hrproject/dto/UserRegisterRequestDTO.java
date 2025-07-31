package com.hr.hrproject.dto;

import lombok.Data;

@Data
public class UserRegisterRequestDTO {
    private String fullName;
    private String email;
    private String password;
    private String role;
}

