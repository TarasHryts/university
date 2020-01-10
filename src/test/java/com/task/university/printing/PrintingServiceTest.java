package com.task.university.printing;

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
import com.task.university.service.impl.DegreeServiceImpl;
import com.task.university.service.impl.DepartmentServiceImpl;
import com.task.university.service.impl.LectorServiceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PrintingServiceTest {
    private DepartmentRepository departmentRepository;
    private DepartmentService departmentService;
    private DegreeRepository degreeRepository;
    private DegreeService degreeService;
    private LectorRepository lectorRepository;
    private LectorService lectorService;
    private PrintingService printingService;
    private Degree degree = new Degree();
    Department mathDepartment = new Department();
    Lector lector;
    List<Lector> lectorList = new ArrayList<>();
    List<Department> departmentList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        departmentRepository = mock(DepartmentRepository.class);
        degreeRepository = mock(DegreeRepository.class);
        lectorRepository = mock(LectorRepository.class);
        departmentService = new DepartmentServiceImpl(departmentRepository);
        degreeService = new DegreeServiceImpl(degreeRepository);
        lectorService = new LectorServiceImpl(lectorRepository);
        printingService = new PrintingService(departmentService, degreeService, lectorService);
        degree.setDegreeName(Degree.DegreeName.ASSISTANT);
        mathDepartment.setName("Math Department");
        for (int i = 0; i < Constants.BASIC_LECTORS; i++) {
            lector = new Lector();
            lector.setName("Somename" + i);
            lector.setSurname("Somesurname" + i);
            lector.setSalary(i * 100.0);
            lector.setDegree(degree);
            lector.setDepartments(Arrays.asList(mathDepartment));
            lectorList.add(lector);
        }
        mathDepartment.setHeadOfDepartment(lector);
        departmentList.add(mathDepartment);
    }

    @Test
    public void printDataAvgSalaryOk() {
        when(lectorRepository.findByDepartments(any())).thenReturn(lectorList);
        when(departmentRepository.findByName(any())).thenReturn(mathDepartment);
        Assert.assertEquals("The average salary of Math Department is 450.0",
                printingService.printData("Show the average salary for department 'Math Department'"));
    }

    @Test
    public void printDataAvgSalaryMistakeInDepartmentName() {
        when(lectorRepository.findByDepartments(any())).thenReturn(lectorList);
        when(departmentRepository.findByName(any())).thenReturn(mathDepartment);
        Assert.assertEquals("The average salary of Math Department is 450.0",
                printingService.printData("Show the average salary for department 'Mah epartment'"));
    }

    @Test
    public void printDataAvgSalaryBadDepartmentName() {
        when(lectorRepository.findByDepartments(any())).thenReturn(lectorList);
        when(departmentRepository.findByName(any())).thenReturn(null);
        Assert.assertEquals("Incorrect department name: 'Math'",
                printingService.printData("Show the average salary for department 'Math'"));
    }

    @Test
    public void printDataAddTestDataOk() {
        Assert.assertEquals("Test data added...",
                printingService.printData("Add test data"));
    }

    @Test
    public void printDataHeadOfDepartmentOk() {
        when(departmentRepository.findByName(any())).thenReturn(mathDepartment);
        Assert.assertEquals("Head of Math Department department is Somename9\n",
                printingService.printData("Who is head of department 'Math Department'"));
    }

    @Test
    public void printDataHeadOfDepartmentWrongNameOk() {
        when(departmentRepository.findByName(any())).thenReturn(mathDepartment);
        Assert.assertEquals("Head of Math Department department is Somename9\n",
                printingService.printData("Who is head of department 'Ma Department'"));
    }

    @Test
    public void printDataCountEmployeeOk() {
        when(departmentRepository.findByName(any())).thenReturn(mathDepartment);
        when(lectorRepository.findByDepartments(any())).thenReturn(lectorList);
        Assert.assertEquals("10",
                printingService.printData("Show count of employee for 'Math Department'"));
    }

    @Test
    public void printDataCountEmployeeWrongNameOk() {
        when(departmentRepository.findByName(any())).thenReturn(mathDepartment);
        when(lectorRepository.findByDepartments(any())).thenReturn(lectorList);
        Assert.assertEquals("10",
                printingService.printData("Show count of employee for 'Ma Department'"));
    }

    @Test
    public void printDataCountEmployeeBed() {
        when(departmentRepository.findByName(any())).thenReturn(null);
        Assert.assertEquals("Incorrect department name: 'Math'",
                printingService.printData("Show count of employee for 'Math'"));
    }

    @Test
    public void printDataSearchByNameOk() {
        when(lectorRepository.findByNameContainsOrSurnameContains(any())).thenReturn(lectorList);
        Assert.assertEquals("Somename0 Somesurname0, Somename1 Somesurname1, " +
                        "Somename2 Somesurname2, Somename3 Somesurname3, " +
                        "Somename4 Somesurname4, Somename5 Somesurname5, " +
                        "Somename6 Somesurname6, Somename7 Somesurname7, " +
                        "Somename8 Somesurname8, Somename9 Somesurname9",
                printingService.printData("Global search by 'ame'"));
    }

    @Test
    public void printDataSearchBySurnameOk() {
        when(lectorRepository.findByNameContainsOrSurnameContains(any())).thenReturn(lectorList);
        Assert.assertEquals("Somename0 Somesurname0, Somename1 Somesurname1, " +
                        "Somename2 Somesurname2, Somename3 Somesurname3, " +
                        "Somename4 Somesurname4, Somename5 Somesurname5, " +
                        "Somename6 Somesurname6, Somename7 Somesurname7, " +
                        "Somename8 Somesurname8, Somename9 Somesurname9",
                printingService.printData("Global search by 'sure'"));
    }

    @Test
    public void printDataIncorrectCommandOk() {
        when(departmentRepository.findByName(any())).thenReturn(null);
        Assert.assertEquals("Incorrect department name: 'Math'",
                printingService.printData("Show count of employee for 'Math'"));
    }
}