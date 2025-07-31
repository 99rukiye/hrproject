package com.hr.hrproject.controller;

import com.hr.hrproject.dto.*;
import com.hr.hrproject.entity.Role;
import com.hr.hrproject.entity.User;
import com.hr.hrproject.repository.UserRepository;
import com.hr.hrproject.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegisterRequestDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email zaten kayıtlı!");
        }

        User user = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build();

        user.setAccountNonLocked(true);
        user.setBlocked(false);
        user.setFailedLoginAttempts(0);
        user.setLoginAttempts(0);
        user.setAccountLocked(false);

        userRepository.save(user);
        return ResponseEntity.ok("Kayıt başarılı.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Kullanıcı bulunamadı.");
        }

        User user = optionalUser.get();
        if (user.isBlocked()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hesabınız bloke edilmiştir.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (BadCredentialsException e) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= 3) {
                user.setBlocked(true);
            }
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hatalı giriş: " + attempts);
        }

        user.setFailedLoginAttempts(0); // sıfırla
        userRepository.save(user);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                java.util.Collections.singleton(() -> "ROLE_" + user.getRole())
        );

        String token = jwtTokenProvider.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponseDTO(token, user.getRole().name(), user.getFullName()));
    }
}
