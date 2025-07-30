package com.hr.hrproject.service;

import com.hr.hrproject.entity.Salary;
import java.util.List;

public interface SalaryService {
    List<Salary> getSalariesByUserId(Long userId);
    void saveSalary(Salary salary);
}

