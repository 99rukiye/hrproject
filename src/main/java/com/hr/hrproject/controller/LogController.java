package com.hr.hrproject.controller;

import com.hr.hrproject.entity.Log;
import com.hr.hrproject.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Log>> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }
}
