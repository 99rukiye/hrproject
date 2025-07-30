package com.hr.hrproject.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDTO {
    private LocalDate leaveDate;
    private String reason;
}
