package com.example.project.model.audit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.sql.Types;
import java.time.LocalDateTime;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "audit")
public class AuditLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String requestType;
    @Column(columnDefinition = "TEXT")
    private String requestParam;
    @Column(columnDefinition = "TEXT")
    private String responseParam;
    private String createdBy;
    private String createdByRole;
    private LocalDateTime createdDate;
}
