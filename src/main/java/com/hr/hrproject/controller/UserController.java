package com.hr.hrproject.controller;

import com.hr.hrproject.dto.LeaveRequestDTO;
import com.hr.hrproject.dto.SalaryResponseDTO;
import com.hr.hrproject.entity.Leave;
import com.hr.hrproject.entity.Log;
import com.hr.hrproject.entity.User;
import com.hr.hrproject.security.JwtTokenProvider;
import com.hr.hrproject.service.LeaveService;
import com.hr.hrproject.service.LogService;
import com.hr.hrproject.service.SalaryService;
import com.hr.hrproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LeaveService leaveService;
    private final SalaryService salaryService; // ✅ bu satır eksikti
    private final JwtTokenProvider jwtTokenProvider;
    private final LogService logService;

    // Admin - Tüm kullanıcıları getir
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Admin - Sayfalı kullanıcı listesi
    @GetMapping("/paged")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<User>> getUsersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<User> users = userService.getAllUsers();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), users.size());
        Page<User> userPage = new PageImpl<>(users.subList(start, end), pageable, users.size());

        return ResponseEntity.ok(userPage);
    }
    @GetMapping("/my-leaves")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Leave>> getMyLeaves(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userService.getByEmail(email).orElseThrow();

        List<Leave> leaves = leaveService.getLeavesByUserId(user.getId());
        return ResponseEntity.ok(leaves);
    }
    @GetMapping("/my-salaries")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SalaryResponseDTO>> getMySalaries(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userService.getByEmail(email).orElseThrow();

        List<SalaryResponseDTO> salaries = salaryService.getSalariesByUserId(user.getId()).stream()
                .map(salary -> {
                    SalaryResponseDTO dto = new SalaryResponseDTO();
                    dto.setAmount(salary.getAmount());
                    dto.setCurrency(salary.getCurrency());
                    dto.setEmployeeName(user.getFullName());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(salaries);
    }



    // Kullanıcı - İzin talebi gönderme
    @PostMapping("/leaves")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> requestLeave(
            @RequestBody LeaveRequestDTO dto,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userService.getByEmail(email).orElseThrow();

        Leave leave = Leave.builder()
                .leaveDate(dto.getLeaveDate())
                .reason(dto.getReason())
                .user(user)
                .build();

        leaveService.saveLeave(leave);

        // LOG
        Log log = Log.builder()
                .action("İzin talebi oluşturuldu: " + dto.getReason())
                .timestamp(LocalDateTime.now())
                .performedBy(user.getEmail())
                .user(user)
                .build();
        logService.save(log);

        return ResponseEntity.ok("İzin talebi başarıyla gönderildi.");
    }
}
