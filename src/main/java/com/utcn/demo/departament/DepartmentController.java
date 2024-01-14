package com.utcn.demo.departament;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/getAll")
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public Department createDepartment(@RequestBody final Department department) {
        return departmentService.create(department);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{departmentId}")
    public Department editDepartment(@RequestBody final DepartmentEditDto departmentEditDto, @PathVariable final Integer departmentId) {
        return departmentService.editDepartment(departmentEditDto, departmentId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{departmentId}")
    public void deleteDepartment(@PathVariable final Integer departmentId) {
        departmentService.deleteDepartment(departmentId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getByEmployeeNumber/{employeeThreshold}")
    public List<Department> getAllByNumber(@PathVariable int employeeThreshold) {
        return departmentService.findAllDepartmentsWithMoreEmployeesNumberThanSpecified(employeeThreshold);
    }
}
