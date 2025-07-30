package com.hr.hrproject.dto;

import lombok.Data;

@Data
public class SalaryResponseDTO {
    private Double amount;
    private String currency;
    private String employeeName;
}

