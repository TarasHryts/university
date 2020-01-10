package com.task.university.printing;

import com.task.university.constant.Constants;
import com.task.university.entity.Degree;
import com.task.university.entity.Department;
import com.task.university.entity.Lector;
import com.task.university.example.TestData;
import com.task.university.service.DegreeService;
import com.task.university.service.DepartmentService;
import com.task.university.service.LectorService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import net.ricecode.similarity.LevenshteinDistanceStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class PrintingService {
    private DepartmentService departmentService;
    private DegreeService degreeService;
    private LectorService lectorService;

    @Autowired
    public PrintingService(DepartmentService departmentService,
                           DegreeService degreeService,
                           LectorService lectorService) {
        this.departmentService = departmentService;
        this.degreeService = degreeService;
        this.lectorService = lectorService;
    }

    private String hintDepartmentName(String name) {
        SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
        List<String> departmentNamesList = departmentService.findAll()
                .stream()
                .map(x -> x.getName())
                .collect(Collectors.toList());
        String result = "";
        Double maxScore = 0.0;
        for (String departmentName : departmentNamesList) {
            if ((service.score(departmentName, name) > Constants.NAME_HINT_CONST)
                    && (service.score(departmentName, name) > maxScore)) {
                result = departmentName;
                maxScore = service.score(departmentName, name);
            }
        }
        if (maxScore.equals(1.0)) {
            return result;
        }
        if (maxScore.equals(0.0)) {
            return name;
        }
        System.out.println(String.format("Do you mean '%s'? Press 'y' key to confirm.", result));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String readDecision = null;
        try {
            readDecision = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ("y".equalsIgnoreCase(readDecision)) {
            return result;
        }
        return name;
    }

    public String printData(String command) {
        if (command.toUpperCase().contains(Constants.HEAD_OF_DEPARTMENT.toUpperCase())) {
            String departmentName = hintDepartmentName(command.split("'")[1]);
            Department department = departmentService.findByName(departmentName);
            if (department == null) {
                return String.format("Incorrect department name: '%s'", departmentName);
            }
            return String.format("Head of %s department is %s\n",
                    department.getName(),
                    department.getHeadOfDepartment().getName());
        }
        if (command.toUpperCase().contains(Constants.SHOW_AVG_SALARY.toUpperCase())) {
            String departmentName = hintDepartmentName(command.split("'")[1]);
            Department department = departmentService.findByName(departmentName);
            if (department == null) {
                return String.format("Incorrect department name: '%s'", departmentName);
            }
            Double avgSalary = lectorService.findAvgSalaryByDepartments(department);
            return String.format("The average salary of %s is %s",
                    department.getName(),
                    avgSalary);
        }
        if (command.toUpperCase().contains(Constants.SHOW_COUNT_EMPLOYEE.toUpperCase())) {
            String departmentName = hintDepartmentName(command.split("'")[1]);
            Department department = departmentService.findByName(departmentName);
            if (department == null) {
                return String.format("Incorrect department name: '%s'", departmentName);
            }
            return lectorService.findCountMembersByDepartments(department).toString();
        }
        if (command.toUpperCase().contains(Constants.SHOW_STATISTIC.toUpperCase())) {
            String departmentName = hintDepartmentName(command.split("'")[1]);
            Department department = departmentService.findByName(departmentName);
            if (department == null) {
                return String.format("Incorrect department name: '%s'", departmentName);
            }
            List<Degree> degreeList = degreeService.findAll();
            String stringList = "";
            for (Degree degree : degreeList) {
                Integer countMembers = lectorService
                        .countMembersOfDepartmentWithDegree(department, degree);
                stringList += (String.format("%s - %d\n",
                        degree.getDegreeName().getName(),
                        countMembers));
            }
            return stringList;
        }
        if (command.toUpperCase().contains(Constants.GLOBAL_SEARCH.toUpperCase())) {
            boolean notFirst = false;
            String partName = command.split(" ")[3];
            List<Lector> lectorList = lectorService.findByNameContainsOrSurnameContains(partName);
            String result = "";
            for (Lector lector : lectorList) {
                if (notFirst) {
                    result += ", " + lector.getName() + " " + lector.getSurname();
                } else {
                    result += lector.getName() + " " + lector.getSurname();
                    notFirst = true;
                }
            }
            return result;
        }
        if (command.toUpperCase().contains(Constants.HELP.toUpperCase())) {
            String result = "Example of commands:\n" +
                    "Who is head of department 'Literature Department'\n" +
                    "Show the average salary for department 'Literature Department'\n" +
                    "Show count of employee for 'Literature Department'\n" +
                    "Show 'Literature Department' statistic\n" +
                    "Global search by ame\n" +
                    "add test data";
            List<Department> departmentList = departmentService.findAll();
            String departmentsToPrint = "";
            if (!departmentList.isEmpty()) {
                departmentsToPrint = "\nList of departments:\n";
                for (Department department : departmentList) {
                    departmentsToPrint += String.format("%s\n", department.getName());
                }
            }
            return result + departmentsToPrint;
        }
        if (command.toUpperCase().contains(Constants.ADD_TEST_DATA.toUpperCase())) {
            new TestData(departmentService, degreeService, lectorService).addSomeTestData();
            return "Test data added...";
        }
        return "Wrong command. Use command 'help'";
    }

}
