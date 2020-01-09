package com.task.university.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.task.university.constant.Constants;
import com.task.university.entity.Degree;
import com.task.university.entity.Department;
import com.task.university.entity.Lector;
import com.task.university.repository.DegreeRepository;
import com.task.university.repository.DepartmentRepository;
import com.task.university.repository.LectorRepository;
import com.task.university.service.DegreeService;
import com.task.university.service.DepartmentService;
import com.task.university.service.LectorService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LectorServiceImplTest {
    private DegreeRepository degreeRepository;
    private DegreeService degreeService;
    private DepartmentRepository departmentRepository;
    private DepartmentService departmentService;
    private LectorRepository lectorRepository;
    private LectorService lectorService;
    private Lector lector;
    private Degree degree;
    private Department mathDepartment;
    private List<Lector> lectorList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        lectorRepository = mock(LectorRepository.class);
        lectorService = new LectorServiceImpl(lectorRepository);
        degree = new Degree();
        degree.setDegreeName(Degree.DegreeName.ASSISTANT);
        mathDepartment = new Department();
        mathDepartment.setName("Math Department");
        for (int i = 0; i < Constants.BASIC_LECTORS; i++) {
            lector = new Lector();
            lector.setName("Somename" + i);
            lector.setSurname("Somesurname" + i);
            lector.setSalary((i + 1) * 100.0);
            lector.setDegree(degree);
            lector.setDepartments(Arrays.asList(mathDepartment));
            lectorList.add(lector);
        }
        lector = lectorList.get(1);
        mathDepartment.setHeadOfDepartment(lector);
    }

    @Test
    public void createLectorOk() {
        when(lectorRepository.save(lector)).thenReturn(lector);
        Assert.assertEquals(lector, lectorService.createLector(lector));
    }

    @Test
    public void findByNameContainsOrSurnameContainsOk() {
        when(lectorRepository.findByNameContainsOrSurnameContains(any())).thenReturn(lectorList);
        Assert.assertEquals(lectorList, lectorService.findByNameContainsOrSurnameContains(any()));
    }

    @Test
    public void countMembersOfDepartmentWithDegreeOk() {
        when(lectorRepository.findByDepartmentsAndDegree(any(), any())).thenReturn(lectorList);
        Assert.assertEquals(Optional.ofNullable(lectorList.size()),
                Optional.ofNullable(lectorService.countMembersOfDepartmentWithDegree(any(), any())));

    }

    @Test
    public void findAvgSalaryByDepartmentsOk() {
        Double avg = lectorList.stream().mapToDouble(x -> x.getSalary()).average().orElseThrow();
        when(lectorRepository.findByDepartments(any())).thenReturn(lectorList);
        Assert.assertEquals(avg, lectorService.findAvgSalaryByDepartments(any()));
    }

    @Test
    public void findCountMembersByDepartmentsOk() {
        Integer count = lectorList.size();
        when(lectorRepository.findByDepartments(any())).thenReturn(lectorList);
        Assert.assertEquals(count, lectorService.findCountMembersByDepartments(any()));
    }
}