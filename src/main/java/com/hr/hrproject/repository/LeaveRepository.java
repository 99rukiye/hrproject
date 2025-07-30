package com.hr.hrproject.repository;

import com.hr.hrproject.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByUserId(Long userId);
}
