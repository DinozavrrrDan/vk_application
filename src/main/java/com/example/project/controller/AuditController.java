package com.example.project.controller;

import com.example.project.model.account.Account;
import com.example.project.model.audit.AuditLog;
import com.example.project.service.AuditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuditController {
    private final AuditService auditService;

    @GetMapping("/audit")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<AuditLog>> getAuditLogs() {
        return new ResponseEntity<>(auditService.getAllAuditLogs(), HttpStatus.OK);
    }
}
