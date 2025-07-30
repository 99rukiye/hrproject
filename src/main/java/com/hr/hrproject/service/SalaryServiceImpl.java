package com.hr.hrproject.service;

import com.hr.hrproject.entity.Salary;
import com.hr.hrproject.repository.SalaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryServiceImpl implements SalaryService {

    private final SalaryRepository salaryRepository;

    @Override
    public List<Salary> getSalariesByUserId(Long userId) {
        return salaryRepository.findByUserId(userId);
    }

    @Override
    public void saveSalary(Salary salary) {
        salaryRepository.save(salary);
    }
}
