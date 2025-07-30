package com.hr.hrproject.service;

import com.hr.hrproject.entity.Log;

import java.util.List;

public interface LogService {
    void save(Log log);
    List<Log> getAllLogs();
}
