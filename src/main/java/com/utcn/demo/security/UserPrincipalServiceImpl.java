package com.utcn.demo.security;



import com.utcn.demo.employee.Employee;
import com.utcn.demo.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserPrincipalServiceImpl implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUsername(username).orElseThrow();

        return new UserPrincipal(employee);
}
}
