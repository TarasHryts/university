package com.task.university.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.task.university.entity.Degree;
import com.task.university.repository.DegreeRepository;
import com.task.university.service.DegreeService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DegreeServiceImplTest {
    private DegreeRepository degreeRepository;
    private DegreeService degreeService;
    private Degree degree;
    private List<Degree> degreeList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        degreeRepository = mock(DegreeRepository.class);
        degreeService = new DegreeServiceImpl(degreeRepository);
        degree = new Degree();
        degree.setDegreeName(Degree.DegreeName.ASSISTANT);
        degreeList.add(degree);

    }

    @Test
    public void createDegreeOk() {
        when(degreeRepository.save(degree)).thenReturn(degree);
        Assert.assertEquals(degree, degreeService.createDegree(degree));
        Assert.assertEquals(degree.getDegreeName(),
                degreeService.createDegree(degree).getDegreeName());
    }

    @Test
    public void findAllOk() {
        when(degreeRepository.findAll()).thenReturn(degreeList);
        Assert.assertEquals(degreeList.size(), degreeService.findAll().size());
        Assert.assertEquals(degreeList, degreeService.findAll());
    }
}