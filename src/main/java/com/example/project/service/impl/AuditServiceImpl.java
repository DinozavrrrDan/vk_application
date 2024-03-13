package com.example.project.service.impl;

import com.example.project.model.audit.AuditLog;
import com.example.project.repository.AuditRepository;
import com.example.project.service.AuditService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;

    @Override
    public void save(AuditLog auditLog) {
        auditRepository.save(auditLog);
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        return auditRepository.findAll();
    }
}
