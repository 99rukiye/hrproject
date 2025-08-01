package com.hr.hrproject.dto;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private String fullName;
    private String email;
    private String role;
}
