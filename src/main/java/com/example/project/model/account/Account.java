package com.example.project.model.account;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import static com.example.project.consts.TableConsts.ACCOUNT_TABLE_NAME;

@Data
@Entity
@Table(name = ACCOUNT_TABLE_NAME)
@RequiredArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String password;
    private String roles;
}
