package com.task.university.service.impl;

import com.task.university.entity.Degree;
import com.task.university.entity.Department;
import com.task.university.entity.Lector;
import com.task.university.repository.LectorRepository;
import com.task.university.service.LectorService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LectorServiceImpl implements LectorService {
    private LectorRepository lectorRepository;

    @Autowired
    public LectorServiceImpl(LectorRepository lectorRepository) {
        this.lectorRepository = lectorRepository;
    }

    @Override
    public Lector createLector(Lector lector) {
        return lectorRepository.save(lector);
    }

    @Override
    public List<Lector> findByNameContainsOrSurnameContains(String partString) {
        return lectorRepository.findByNameContainsOrSurnameContains("%" + partString + "%");
    }

    @Override
    public Integer countMembersOfDepartmentWithDegree(Department department, Degree degree) {
        List<Lector> lectorList = lectorRepository.findByDepartmentsAndDegree(department, degree);
        return lectorList.size();
    }

    @Override
    public Double findAvgSalaryByDepartments(Department department) {
        return lectorRepository.findByDepartments(department)
                .stream()
                .mapToDouble(x -> x.getSalary())
                .average()
                .getAsDouble();
    }

    @Override
    public Integer findCountMembersByDepartments(Department department) {
        return lectorRepository.findByDepartments(department).size();
    }
}
