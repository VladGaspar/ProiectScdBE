package com.utcn.demo.departament;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    default Department getById(Integer departmentId) {
        return findById(departmentId).orElseThrow(() -> new EntityNotFoundException("Cannot find department by id: " + departmentId));
    }

    @Query("SELECT d, COUNT(e) FROM Department d JOIN Employee e ON d.departmentId = e.department.departmentId GROUP BY d HAVING COUNT(e) > :employeeThreshold")
    List<Department> findAllDepartmentsWithMoreEmployeesNumberThanSpecified(@Param("employeeThreshold") int employeeThreshold);
}
