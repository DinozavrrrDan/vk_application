package com.example.project;

import com.example.project.controller.AuditController;
import com.example.project.controller.UserController;
import com.example.project.model.audit.AuditLog;
import com.example.project.model.user.User;
import com.example.project.service.AuditService;
import com.example.project.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuditController.class)
public class AuditControllerTests {
    @MockBean
    private AuditService auditService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    public void testGetAuditLogs_Success() throws Exception {
        List<AuditLog> auditLogs = new ArrayList<>();
        auditLogs.add(getTestAuditLog());
        auditLogs.add(getTestAuditLog());

        when(auditService.getAllAuditLogs()).thenReturn(auditLogs);

        mockMvc.perform(get("/api/audit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)));
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
