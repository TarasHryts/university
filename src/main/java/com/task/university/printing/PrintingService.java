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
import java.util.ArrayList;
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

    private String hintCommand(String command) {
        SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
        StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
        List<String> commandsList = new ArrayList<>();
        commandsList.add(Constants.HEAD_OF_DEPARTMENT);
        commandsList.add(Constants.SHOW_COUNT_EMPLOYEE);
        commandsList.add(Constants.SHOW_AVG_SALARY);
        commandsList.add(Constants.SHOW_STATISTIC);
        commandsList.add(Constants.GLOBAL_SEARCH);
        commandsList.add(Constants.HELP);
        commandsList.add(Constants.ADD_TEST_DATA);

        String result = "";
        Double maxScore = 0.0;
        for (String constCommand : commandsList) {
            if ((service.score(constCommand, command.trim()) > Constants.NAME_HINT_CONST)
                    && (service.score(constCommand, command.trim()) > maxScore)) {
                result = constCommand;
                maxScore = service.score(constCommand, command.trim());
            }
        }
        if (maxScore.equals(1.0)) {
            return result;
        }
        if (maxScore.equals(0.0)) {
            return command;
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
        return command;
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

    public String printData(String inputCommand) {
        String command = hintCommand(inputCommand.split("'")[0]);
        if (command.toUpperCase().contains(Constants.HEAD_OF_DEPARTMENT.toUpperCase())) {
            String departmentName = hintDepartmentName(inputCommand.split("'")[1]);
            return findHeadOfDepartment(departmentName);
        }
        if (command.toUpperCase().contains(Constants.SHOW_AVG_SALARY.toUpperCase())) {
            String departmentName = hintDepartmentName(inputCommand.split("'")[1]);
            return findAvgSalary(departmentName);
        }
        if (command.toUpperCase().contains(Constants.SHOW_COUNT_EMPLOYEE.toUpperCase())) {
            String departmentName = hintDepartmentName(inputCommand.split("'")[1]);
            return countEmployee(departmentName);
        }
        if (command.toUpperCase().contains(Constants.SHOW_STATISTIC.toUpperCase())) {
            String departmentName = hintDepartmentName(inputCommand.split("'")[1]);
            return showStatistic(departmentName);
        }
        if (command.toUpperCase().contains(Constants.GLOBAL_SEARCH.toUpperCase())) {
            String partName = inputCommand.split("'")[1];
            return findByPartName(partName);
        }
        if (command.toUpperCase().contains(Constants.HELP.toUpperCase())) {
            return showHelpMenu();
        }
        if (command.toUpperCase().contains(Constants.ADD_TEST_DATA.toUpperCase())) {
            return addTestData();
        }
        return "Wrong command. Use command 'help'";
    }

    private String findHeadOfDepartment(String departmentName) {
        Department department = departmentService.findByName(departmentName);
        if (department == null) {
            return String.format("Incorrect department name: '%s'", departmentName);
        }
        return String.format("Head of %s department is %s\n",
                department.getName(),
                department.getHeadOfDepartment().getName());
    }
    private String findAvgSalary(String departmentName) {
        Department department = departmentService.findByName(departmentName);
        if (department == null) {
            return String.format("Incorrect department name: '%s'", departmentName);
        }
        Double avgSalary = lectorService.findAvgSalaryByDepartments(department);
        return String.format("The average salary of %s is %s",
                department.getName(),
                avgSalary);
    }
    private String countEmployee(String  departmentName) {
        Department department = departmentService.findByName(departmentName);
        if (department == null) {
            return String.format("Incorrect department name: '%s'", departmentName);
        }
        return lectorService.findCountMembersByDepartments(department).toString();
    }
    private String showStatistic(String departmentName) {
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
    private String findByPartName(String partName) {
        List<Lector> lectorList = lectorService.findByNameContainsOrSurnameContains(partName);
        String result = "";
        boolean notFirst = false;
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
    private String showHelpMenu() {
        String result = "Example of commands:\n" +
                "Who is head of department 'Literature Department'\n" +
                "Show the average salary for department 'Literature Department'\n" +
                "Show count of employee for 'Literature Department'\n" +
                "Show 'Literature Department' statistic\n" +
                "Global search by 'ame'\n" +
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
    private String addTestData() {
        new TestData(departmentService, degreeService, lectorService).addSomeTestData();
        return "Test data added...";
    }

}
