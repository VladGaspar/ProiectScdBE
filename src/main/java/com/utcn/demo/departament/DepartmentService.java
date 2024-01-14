package com.utcn.demo.departament;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {


    private final DepartmentRepository departmentRepository;

    @Transactional
    public Department create(Department department){
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

    public void deleteDepartment(Integer departmentId) {
        Department department = departmentRepository.getById(departmentId);
        departmentRepository.delete(department);
    }

    public Department editDepartment(DepartmentEditDto departmentEditDto, Integer departmentId) {
        Department department = departmentRepository.getById(departmentId);
        department.setDescription(departmentEditDto.getDescription());
        department.setManagerId(departmentEditDto.getManagerId());

       return departmentRepository.save(department);
    }

    public List<Department> findAllDepartmentsWithMoreEmployeesNumberThanSpecified(int employeeThreshold){
       return departmentRepository.findAllDepartmentsWithMoreEmployeesNumberThanSpecified(employeeThreshold);
    }

}
