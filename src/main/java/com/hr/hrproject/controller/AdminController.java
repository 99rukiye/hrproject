package com.hr.hrproject.controller;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.hr.hrproject.dto.SalaryResponseDTO;
import com.hr.hrproject.dto.UpdateUserRequestDTO;
import com.hr.hrproject.dto.AdminAddUserRequestDTO;
import com.hr.hrproject.entity.Leave;
import com.hr.hrproject.entity.Log;
import com.hr.hrproject.entity.Role;
import com.hr.hrproject.entity.Salary;
import com.hr.hrproject.entity.User;
import com.hr.hrproject.service.LeaveService;
import com.hr.hrproject.service.LogService;
import com.hr.hrproject.service.SalaryService;
import com.hr.hrproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LeaveService leaveService;
    private final SalaryService salaryService;
    private final UserService userService;
    private final LogService logService;
    private final PasswordEncoder passwordEncoder;


    // Admin - Belirli çalışanın izinleri
    @GetMapping("/user/{userId}/leaves")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Leave>> getUserLeaves(@PathVariable Long userId) {
        return ResponseEntity.ok(leaveService.getLeavesByUserId(userId));
    }

    // Admin - Belirli çalışanın maaş bilgileri
    @GetMapping("/user/{userId}/salary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SalaryResponseDTO>> getSalaryInfo(@PathVariable Long userId) {
        List<Salary> salaryList = salaryService.getSalariesByUserId(userId);
        List<SalaryResponseDTO> dtoList = salaryList.stream()
                .map(salary -> {
                    SalaryResponseDTO dto = new SalaryResponseDTO();
                    dto.setAmount(salary.getAmount());
                    dto.setCurrency(salary.getCurrency());
                    dto.setEmployeeName(salary.getUser().getFullName());
                    return dto;
                }).toList();
        return ResponseEntity.ok(dtoList);
    }

    // Admin - Maaş ekle
    @PostMapping("/user/{userId}/salary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addSalary(
            @PathVariable Long userId,
            @RequestBody SalaryResponseDTO dto
    ) {
        User user = userService.getById(userId).orElseThrow();

        Salary salary = Salary.builder()
                .amount(dto.getAmount())
                .currency(dto.getCurrency())
                .user(user)
                .build();

        salaryService.saveSalary(salary);

        Log log = Log.builder()
                .action("Maaş eklendi: " + dto.getAmount() + " " + dto.getCurrency())
                .timestamp(LocalDateTime.now())
                .performedBy("ADMIN")
                .user(user)
                .build();
        logService.save(log);

        return ResponseEntity.ok("Maaş bilgisi başarıyla kaydedildi.");
    }

    // Admin - Kullanıcı güncelle
    @PutMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequestDTO dto
    ) {
        User user = userService.getById(userId).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));

        userService.save(user);

        Log log = Log.builder()
                .action("Kullanıcı güncellendi: " + user.getEmail())
                .timestamp(LocalDateTime.now())
                .performedBy("ADMIN")
                .user(user)
                .build();
        logService.save(log);

        return ResponseEntity.ok("Kullanıcı güncellendi.");
    }

    // Admin - Kullanıcı sil
    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        User user = userService.getById(userId).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        userService.delete(userId);

        Log log = Log.builder()
                .action("Kullanıcı silindi: " + user.getEmail())
                .timestamp(LocalDateTime.now())
                .performedBy("ADMIN")
                .user(user)
                .build();
        logService.save(log);

        return ResponseEntity.ok("Kullanıcı silindi.");
    }

    // Admin - Yeni kullanıcı ekle
    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addUserByAdmin(@RequestBody AdminAddUserRequestDTO dto) {
        if (userService.getByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Bu email ile kayıtlı kullanıcı zaten var.");
        }

        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.valueOf(dto.getRole().toUpperCase()))
                .build();

        userService.save(user);

        Log log = Log.builder()
                .action("Admin tarafından yeni kullanıcı eklendi: " + user.getEmail())
                .timestamp(LocalDateTime.now())
                .performedBy("ADMIN")
                .user(user)
                .build();

        logService.save(log);

        return ResponseEntity.ok("Kullanıcı başarıyla eklendi.");
    }
}
