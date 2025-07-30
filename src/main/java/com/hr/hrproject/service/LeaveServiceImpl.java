package com.hr.hrproject.service;

import com.hr.hrproject.entity.Leave;
import com.hr.hrproject.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;

    @Override
    public List<Leave> getLeavesByUserId(Long userId) {
        return leaveRepository.findByUserId(userId);
    }

    @Override
    public void saveLeave(Leave leave) {
        leaveRepository.save(leave);
    }
}
