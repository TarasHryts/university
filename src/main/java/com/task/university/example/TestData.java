package com.task.university.example;

import com.task.university.constant.Constants;
import com.task.university.entity.Degree;
import com.task.university.entity.Department;
import com.task.university.entity.Lector;
import com.task.university.service.DegreeService;
import com.task.university.service.DepartmentService;
import com.task.university.service.LectorService;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;

public class TestData {
    private DepartmentService departmentService;
    private DegreeService degreeService;
    private LectorService lectorService;

    @Autowired
    public TestData(DepartmentService departmentService,
                    DegreeService degreeService,
                    LectorService lectorService) {
        this.departmentService = departmentService;
        this.degreeService = degreeService;
        this.lectorService = lectorService;
    }

    public void addSomeTestData() {
        Degree degree = new Degree();
        degree.setDegreeName(Degree.DegreeName.ASSISTANT);
        degreeService.createDegree(degree);
        Degree associatedDegree = new Degree();
        associatedDegree.setDegreeName(Degree.DegreeName.ASSOCIATE_PROFESSOR);
        degreeService.createDegree(associatedDegree);
        Degree professorDegree = new Degree();
        professorDegree.setDegreeName(Degree.DegreeName.PROFESSOR);
        degreeService.createDegree(professorDegree);

        Department mathDepartment = new Department();
        mathDepartment.setName("Math Department");
        departmentService.createDepartment(mathDepartment);

        Lector lector = null;
        for (int i = 0; i < Constants.BASIC_LECTORS; i++) {
            lector = new Lector();
            lector.setName("Somename" + i);
            lector.setSurname("Somesurname" + i);
            lector.setSalary((i + 1) * 100.0);
            lector.setDegree(degree);
            lector.setDepartments(Arrays.asList(mathDepartment));
            lector = lectorService.createLector(lector);
        }
        mathDepartment.setHeadOfDepartment(lector);
        departmentService.update(mathDepartment);

        Department literatureDepartment = new Department();
        literatureDepartment.setName("Literature Department");
        departmentService.createDepartment(literatureDepartment);

        for (int i = 0; i < Constants.BASIC_LECTORS; i++) {
            lector = new Lector();
            lector.setName("Othername" + i);
            lector.setSurname("Othersurname" + i);
            lector.setSalary((i + 1) * 123.0);
            lector.setDegree(professorDegree);
            lector.setDepartments(Arrays.asList(literatureDepartment));
            lector = lectorService.createLector(lector);
        }
        literatureDepartment.setHeadOfDepartment(lector);
        departmentService.update(literatureDepartment);

        Department philosophyDepartment = new Department();
        philosophyDepartment.setName("Philosophy Department");
        departmentService.createDepartment(philosophyDepartment);

        for (int i = 0; i < Constants.BASIC_LECTORS; i++) {
            lector = new Lector();
            lector.setName("Differname" + i);
            lector.setSurname("Differsurname" + i);
            lector.setSalary((i + 1) * 88.0);
            lector.setDegree(associatedDegree);
            lector.setDepartments(Arrays.asList(philosophyDepartment, mathDepartment));
            lector = lectorService.createLector(lector);
        }
        philosophyDepartment.setHeadOfDepartment(lector);
        departmentService.update(philosophyDepartment);
    }
}
