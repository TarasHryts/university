package com.task.university.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.task.university.entity.Department;
import com.task.university.repository.DepartmentRepository;
import com.task.university.service.DepartmentService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DepartmentServiceImplTest {
    private DepartmentRepository departmentRepository;
    private DepartmentService departmentService;
    private Department mathDepartment;
    private List<Department> departmentList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        departmentRepository = mock(DepartmentRepository.class);
        departmentService = new DepartmentServiceImpl(departmentRepository);
        mathDepartment = new Department();
        mathDepartment.setName("Math Department");
        departmentList.add(mathDepartment);
    }

    @Test
    public void createDepartmentOk() {
        when(departmentRepository.save(mathDepartment)).thenReturn(mathDepartment);
        Assert.assertEquals(mathDepartment, departmentService.createDepartment(mathDepartment));
    }

    @Test
    public void findByNameOk() {
        when(departmentRepository.findByName("Math Department")).thenReturn(mathDepartment);
        Assert.assertEquals(mathDepartment, departmentService.findByName("Math Department"));
    }

    @Test
    public void updateOk() {
        mathDepartment.setName("new Department");
        when(departmentRepository.saveAndFlush(mathDepartment)).thenReturn(mathDepartment);
        Assert.assertEquals(mathDepartment, departmentService.update(mathDepartment));
    }

    @Test
    public void findAllOk() {
        when(departmentRepository.findAll()).thenReturn(departmentList);
        Assert.assertEquals(departmentList, departmentService.findAll());
    }
}