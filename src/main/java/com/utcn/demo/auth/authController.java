package com.utcn.demo.auth;

import com.utcn.demo.employee.Employee;
import com.utcn.demo.employee.EmployeeRepository;
import com.utcn.demo.employee.EmployeeService;
import com.utcn.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class authController {

    private final EmployeeService employeeService;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping("/register")
    public Employee create(@RequestBody Employee employee){
        return employeeService.create(employee);
    }

    @PostMapping("/login")
    public ResponseEntity<Employee> login(final @RequestBody LoginDto loginDto) {
        Employee employee = employeeService.login(loginDto);
        String jwtToken = jwtTokenProvider.createAuthToken(employee.getUsername(),employee.getRole());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken).body(employee);
    }
}
