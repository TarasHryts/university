package com.task.university.service;

import com.task.university.entity.Department;
import java.util.List;

public interface DepartmentService {
    Department createDepartment(Department department);

    Department findByName(String name);

    Department update(Department department);

    List<Department> findAll();
}
