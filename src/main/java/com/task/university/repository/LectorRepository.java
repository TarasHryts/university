package com.task.university.repository;

import com.task.university.entity.Degree;
import com.task.university.entity.Department;
import com.task.university.entity.Lector;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LectorRepository extends JpaRepository<Lector, Long> {
    @Query(value = "FROM Lector le WHERE le.name LIKE ?1 OR le.surname LIKE ?1")
    List<Lector> findByNameContainsOrSurnameContains(String partString);

    List<Lector> findByDepartmentsAndDegree(Department department, Degree degree);

    List<Lector> findByDepartments(Department department);

}
