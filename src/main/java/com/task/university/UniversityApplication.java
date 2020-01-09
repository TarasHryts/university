package com.task.university;

import static java.lang.System.exit;

import com.task.university.constant.Constants;
import com.task.university.entity.Degree;
import com.task.university.entity.Department;
import com.task.university.entity.Lector;
import com.task.university.service.DegreeService;
import com.task.university.service.DepartmentService;
import com.task.university.service.LectorService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UniversityApplication implements CommandLineRunner {
    private DegreeService degreeService;
    private DepartmentService departmentService;
    private LectorService lectorService;

    @Autowired
    public UniversityApplication(DegreeService degreeService,
                                 DepartmentService departmentService,
                                 LectorService lectorService) {
        this.degreeService = degreeService;
        this.departmentService = departmentService;
        this.lectorService = lectorService;
    }

    public static void main(String[] args) throws IOException {
        SpringApplication app = new SpringApplication(UniversityApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws IOException {
        System.out.println("Use command 'help' for add test data and review all commands");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String string = "";
        while (!string.equalsIgnoreCase("exit")) {
            string = bufferedReader.readLine();
            printData(string);
        }
        exit(0);
    }

    @Bean
    CommandLineRunner init(DegreeService degreeService,
                           DepartmentService departmentService,
                           LectorService lectorService) {
        return args -> {};
    }

    private void printData(String command) {
        if (command.toUpperCase().contains(Constants.HEAD_OF_DEPARTMENT1.toUpperCase())) {
            String departmentName = command.split("'")[1];
            Department department = departmentService.findByName(departmentName);
            System.out.println(String.format("Head of %s department is %s\n",
                    department.getName(),
                    department.getHeadOfDepartment().getName()));
        } else if (command.toUpperCase().contains(Constants.SHOW_AVG_SALARY3.toUpperCase())) {
            String departmentName = command.split("'")[1];
            Department department = departmentService.findByName(departmentName);
            Double avgSalary = lectorService.findAvgSalaryByDepartments(department);
            System.out.println(String.format("The average salary of %s is %s",
                    department.getName(),
                    avgSalary));
        } else if (command.toUpperCase().contains(Constants.SHOW_COUNT_EMPLOYEE4.toUpperCase())) {
            String departmentName = command.split("'")[1];
            Department department = departmentService.findByName(departmentName);
            Integer countMembers = lectorService.findCountMembersByDepartments(department);
            System.out.println(countMembers);
        } else if (command.toUpperCase().contains(Constants.SHOW_STATISTIC2.toUpperCase())) {
            String departmentName = command.split("'")[1];
            Department department = departmentService.findByName(departmentName);
            List<Degree> degreeList = degreeService.findAll();
            for (Degree degree : degreeList) {
                Integer countMembers = lectorService
                        .countMembersOfDepartmentWithDegree(department, degree);
                System.out.println(String.format("%s - %d",
                        degree.getDegreeName().getName(),
                        countMembers));
            }
        } else if (command.toUpperCase().contains(Constants.GLOBAL_SEARCH5.toUpperCase())) {
            boolean first = false;
            String partName = command.split(" ")[3];
            List<Lector> lectorList = lectorService.findByNameContainsOrSurnameContains(partName);
            for (Lector lector : lectorList) {
                if (first) {
                    System.out.print(", " + lector.getName() + " " + lector.getSurname());
                } else {
                    System.out.print(lector.getName() + " " + lector.getSurname());
                    first = true;
                }
            }
        } else if (command.toUpperCase().contains(Constants.HELP.toUpperCase())) {
            System.out.println("Exaple of commands:");
            System.out.println("Who is head of department 'Literature Department'\n" +
                    "Show the average salary for department 'Literature Department'\n" +
                    "Show count of employee for 'Literature Department'\n" +
                    "Show 'Literature Department' statistic\n" +
                    "Global search by ame\n" +
                    "add test data");
            List<Department> departmentList = departmentService.findAll();
            if (!departmentList.isEmpty()) {
                System.out.println("\nList of departments:");
                for (Department department : departmentList) {
                    System.out.println(department.getName());
                }
            }
        } else if (command.toUpperCase().contains(Constants.ADD_TEST_DATA.toUpperCase())) {
            addSomeTestData();
        } else {
            System.out.println("Wrong command. Use command 'help'");
        }
    }

    private void addSomeTestData() {
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
            lector.setDepartments(Arrays.asList(philosophyDepartment));
            lector = lectorService.createLector(lector);
        }
        philosophyDepartment.setHeadOfDepartment(lector);
        departmentService.update(philosophyDepartment);

        System.out.println("Test data added...");
    }
}