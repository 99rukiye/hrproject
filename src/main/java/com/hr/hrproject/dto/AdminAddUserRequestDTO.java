package com.hr.hrproject.dto;

import lombok.Data;

@Data
public class AdminAddUserRequestDTO {
    private String fullName;
    private String email;
    private String password;
    private String role;
}
