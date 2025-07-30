package com.hr.hrproject.service;

import com.hr.hrproject.entity.Admin;
import com.hr.hrproject.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public void save(Admin admin) {
        adminRepository.save(admin);
    }
}
