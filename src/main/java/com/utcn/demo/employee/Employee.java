package com.utcn.demo.employee;

import com.utcn.demo.departament.Department;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    private Integer managerId;
    private String email;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
}
