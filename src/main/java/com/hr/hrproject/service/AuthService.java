package com.hr.hrproject.service;

import com.hr.hrproject.dto.LoginRequestDTO;
import com.hr.hrproject.dto.UserRegisterRequestDTO;
import com.hr.hrproject.entity.Role;
import com.hr.hrproject.entity.User;
import com.hr.hrproject.repository.UserRepository;
import com.hr.hrproject.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String register(UserRegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Bu e-posta adresi zaten kayıtlı.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .loginAttempts(0)
                .accountLocked(false)
                .build();

        userRepository.save(user);
        return "Kayıt başarılı.";
    }

    public String login(LoginRequestDTO request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            throw new BadCredentialsException("Kullanıcı bulunamadı.");
        }

        User user = optionalUser.get();

        if (user.isAccountLocked()) {
            throw new RuntimeException("Hesabınız bloke edilmiştir. Lütfen yöneticiyle iletişime geçin.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            int attempts = user.getLoginAttempts() + 1;
            user.setLoginAttempts(attempts);

            if (attempts >= 3) {
                user.setAccountLocked(true);
            }

            userRepository.save(user);
            throw new BadCredentialsException("Şifre hatalı. Kalan deneme: " + Math.max(0, 3 - attempts));
        }

        // Başarılı giriş:
        user.setLoginAttempts(0);
        userRepository.save(user);

        return jwtTokenProvider.generateToken(user.getEmail());
    }
}
