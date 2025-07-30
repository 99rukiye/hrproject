package com.hr.hrproject.service;

import com.hr.hrproject.entity.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmins();
    void save(Admin admin);
}
