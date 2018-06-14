package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.AcademicYear;
import com.ggpk.studyload.service.AcademicYearService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AcademicYearServiceImplTest {


    @Autowired
    private AcademicYearService academicYearService;


    private AcademicYear academicYear;
    private AcademicYear academicYear2;
    private List<AcademicYear> academicYears;

    @Before
    public void setUp() throws Exception {
        academicYear = new AcademicYear();
        academicYear.setStartYear(Year.parse("2017"));
        academicYear.setEndYear(Year.parse("2018"));
        academicYear2 = new AcademicYear();
        academicYear2.setStartYear(Year.parse("2018"));
        academicYear2.setEndYear(Year.parse("2019"));
        academicYears = new ArrayList<>();
        academicYears.add(academicYear);
        academicYears.add(academicYear2);
        academicYearService.deleteAll();

    }

    @Test
    public void save() {
        academicYearService.save(academicYear);
        assertTrue(academicYearService.existsById(academicYear.getId()));
    }

    @Test
    public void saveAll() {
        academicYearService.saveAll(academicYears);
        assertTrue(academicYearService.existsById(academicYear.getId()));
        assertTrue(academicYearService.existsById(academicYear2.getId()));
    }

    @Test
    public void getById() {

        academicYearService.saveAll(academicYears);
        Optional<AcademicYear> byId = academicYearService.getById(academicYear.getId());
        assertTrue(byId.isPresent());
        assertEquals(academicYear, byId.get());

    }

    @Test
    public void existsById() {

        academicYearService.saveAll(academicYears);
        assertTrue(academicYearService.existsById(academicYear.getId()));
        assertTrue(academicYearService.existsById(academicYear2.getId()));
    }

    @Test
    public void getAll() {
        academicYearService.saveAll(academicYears);
        List<AcademicYear> academicYearList = academicYearService.getAll();
        assertEquals(Arrays.asList(academicYear, academicYear2), academicYearList);
    }

    @Test
    public void count() {
        academicYearService.saveAll(academicYears);
        assertEquals(academicYears.size(), academicYearService.count());
    }

    @Test
    public void deleteById() {
        academicYearService.saveAll(academicYears);
        academicYearService.deleteById(academicYears.get(0).getId());
        assertEquals(academicYears.size() - 1, academicYearService.count());
    }

}