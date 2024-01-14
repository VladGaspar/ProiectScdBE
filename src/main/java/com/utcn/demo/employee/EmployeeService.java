package com.utcn.demo.employee;

import com.utcn.demo.auth.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AuthenticationManager authenticationManager;


    public Employee editEmployee(EmployeeUpdateDto employeeUpdateDto, Integer employeeId) {
        Employee employee = employeeRepository.getById(employeeId);

        employee.setName(employeeUpdateDto.getName());
        employee.setEmail(employee.getEmail());
        employee.setManagerId(employeeUpdateDto.getManagerId());

        return employeeRepository.save(employee);
    }

    public Employee create(Employee employee) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);

    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public void deleteEmployee(Integer employeeId) {
        Employee employee = employeeRepository.getById(employeeId);

        if(employee.getManagerId() == null) {
            Employee employeeByManagerId = employeeRepository.findEmployeeByManagerId(employeeId);
            employeeByManagerId.setManagerId(null);
            employeeRepository.save(employeeByManagerId);
        }

        employeeRepository.delete(employee);
    }

    public Employee login(LoginDto loginDto) {
        final Employee employee = employeeRepository.findByUsername(loginDto.getUsername()).orElseThrow();
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return employee;
    }

    public List<Employee> findAllEmployeesWithTheSameDepartment(int departmentId){
       return employeeRepository.findAllEmployeesWithTheSameDepartment(departmentId);
    }
}
