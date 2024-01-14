package com.utcn.demo.employee;

import lombok.Data;

@Data
public class EmployeeUpdateDto {
    private String name;
    private Integer departmentId;
    private Integer managerId;
    private String email;
}
