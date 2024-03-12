package com.example.project;

import com.example.project.model.audit.AuditLog;
import com.example.project.repository.AuditRepository;
import com.example.project.service.AuditService;
import com.example.project.service.impl.AuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AuditServiceTests {
    private AuditService auditService;
    @Mock
    private AuditRepository auditRepository;

    @BeforeEach
    public void setup() {
        auditService = new AuditServiceImpl(auditRepository);
    }

    @Test
    public void testSaveAuditLog() {
        AuditLog auditLog = getTestAuditLog();
        auditService.save(auditLog);
        Mockito.verify(auditRepository, Mockito.times(1)).save(auditLog);
    }

    @Test
    public void testGetAllAuditLogs() {
        List<AuditLog> logs = new ArrayList<>();
        logs.add(getTestAuditLog());
        logs.add(getTestAuditLog());

        Mockito.when(auditRepository.findAll()).thenReturn(logs);

        List<AuditLog> retrievedLogs = auditService.getAllAuditLogs();

        assertEquals(logs.size(), retrievedLogs.size());
        assertEquals(logs, retrievedLogs);
    }
    private AuditLog getTestAuditLog(){
        AuditLog auditLog = new AuditLog();
        auditLog.setId(1L);
        auditLog.setCreatedByRole("created by role");
        auditLog.setResponseParam("response");
        auditLog.setCreatedBy("created by");
        auditLog.setRequestParam("request");
        return auditLog;
    }
}
