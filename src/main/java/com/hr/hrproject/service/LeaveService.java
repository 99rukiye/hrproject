package com.hr.hrproject.service;

import com.hr.hrproject.entity.Leave;
import java.util.List;

public interface LeaveService {
    List<Leave> getLeavesByUserId(Long userId);
    void saveLeave(Leave leave);
}
