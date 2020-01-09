package com.task.university.service;

import com.task.university.entity.Degree;
import com.task.university.entity.Department;
import com.task.university.entity.Lector;
import java.util.List;

public interface LectorService {
    Lector createLector(Lector lector);

    List<Lector> findByNameContainsOrSurnameContains(String partString);

    Integer countMembersOfDepartmentWithDegree(Department department, Degree degree);

    Double findAvgSalaryByDepartments(Department department);

    Integer findCountMembersByDepartments(Department department);
}
