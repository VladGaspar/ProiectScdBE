package com.utcn.demo.employee;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer>{
    default Employee getById(Integer employeeId) {
        return findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Cannot find employee by id: " + employeeId));
    }
    @Query("SELECT e FROM Employee e WHERE e.managerId = :managerId")
    Employee findEmployeeByManagerId(@Param("managerId") Integer managerId);

    Optional<Employee> findByUsername(String username);

    @Query("SELECT e FROM Employee e WHERE e.department.departmentId = :departmentId")
    List<Employee> findAllEmployeesWithTheSameDepartment(@Param("departmentId") int departmentId);
}
