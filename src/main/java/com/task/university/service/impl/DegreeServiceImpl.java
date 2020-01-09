package com.task.university.service.impl;

import com.task.university.entity.Degree;
import com.task.university.repository.DegreeRepository;
import com.task.university.service.DegreeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DegreeServiceImpl implements DegreeService {
    private DegreeRepository degreeRepository;

    @Autowired
    public DegreeServiceImpl(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
    }

    @Override
    public Degree createDegree(Degree degree) {
        return degreeRepository.save(degree);
    }

    @Override
    public List<Degree> findAll() {
        return degreeRepository.findAll();
    }
}
