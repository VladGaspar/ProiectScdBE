package com.utcn.demo.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/getAll")
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{employeetId}")
    public Employee editDepartment(@RequestBody final EmployeeUpdateDto employeeUpdateDto, @PathVariable final Integer employeetId){
        return employeeService.editEmployee(employeeUpdateDto,employeetId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{employeeId}")
    public void deleteEmployee(@PathVariable final Integer employeeId){
        employeeService.deleteEmployee(employeeId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getByDepartment/{departmentId}")
    public List<Employee> getWithoutManager(@PathVariable final int departmentId) {
       return employeeService.findAllEmployeesWithTheSameDepartment(departmentId);
    }
}
