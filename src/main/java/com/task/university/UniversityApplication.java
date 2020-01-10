package com.task.university;

import static java.lang.System.exit;

import com.task.university.constant.Constants;
import com.task.university.entity.Degree;
import com.task.university.entity.Department;
import com.task.university.entity.Lector;
import com.task.university.printing.PrintingService;
import com.task.university.service.DegreeService;
import com.task.university.service.DepartmentService;
import com.task.university.service.LectorService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.print.PrintService;
import net.ricecode.similarity.LevenshteinDistanceStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
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
        PrintingService printingService =
                new PrintingService(departmentService, degreeService, lectorService);
        String string = "";
        while (!string.equalsIgnoreCase("exit")) {
            string = bufferedReader.readLine();
            try {
                System.out.println(printingService.printData(string));
            } catch (Throwable e) {
                System.out.println("incorret data enter correct please");
            }
        }
        exit(0);
    }

    @Bean
    CommandLineRunner init(DegreeService degreeService,
                           DepartmentService departmentService,
                           LectorService lectorService) {
        return args -> {
        };
    }



}