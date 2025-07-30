package com.hr.hrproject.service;

import com.hr.hrproject.entity.Log;
import com.hr.hrproject.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public void save(Log log) {
        logRepository.save(log);
    }

    @Override
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }
}

