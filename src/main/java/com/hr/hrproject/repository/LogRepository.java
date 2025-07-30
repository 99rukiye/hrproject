package com.hr.hrproject.repository;

import com.hr.hrproject.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}

