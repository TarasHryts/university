package com.task.university.service;

import com.task.university.entity.Degree;
import java.util.List;

public interface DegreeService {
    Degree createDegree(Degree degree);

    List<Degree> findAll();
}
