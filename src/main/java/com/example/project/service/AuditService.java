package com.example.project.service;

import com.example.project.model.audit.AuditLog;

import java.util.List;

public interface AuditService {
    void save(AuditLog auditLog);
    List<AuditLog> getAllAuditLogs();
}
