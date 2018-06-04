package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.*;
import com.ggpk.studyload.service.DisciplineService;
import com.ggpk.studyload.service.TeacherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DisciplineServiceImplTest {


    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private TeacherService teacherService;


    @Before
    public void setUp() throws Exception {
        Teacher teacher = new Teacher();
//        teacherService.save(teacher);
        AcademicYear academicYear = new AcademicYear();
        Group group = new Group();
        Discipline discipline = new Discipline();
        Discipline discipline1 = new Discipline();
        Discipline discipline2 = new Discipline();

        discipline.setGroup(group);
        discipline1.setGroup(group);
        discipline2.setGroup(group);
        discipline.setAcademicYear(academicYear);
        discipline.setFullGroup(new DisciplineGroup());
        discipline.getFullGroup().setTeacher(teacher);
        discipline1.setFullGroup(new DisciplineGroup());
        discipline1.getFullGroup().setTeacher(teacher);
        discipline2.setFullGroup(new DisciplineGroup());
        discipline2.getFullGroup().setTeacher(teacher);
        discipline1.setAcademicYear(academicYear);
        discipline2.setAcademicYear(academicYear);

        disciplineService.saveAll(Arrays.asList(discipline, discipline1, discipline2));

    }

    @Test
    public void delete() {


        List<Discipline> disciplines = disciplineService.getAll();
        disciplineService.delete(disciplines.get(disciplines.size() - 1));

        Optional<Discipline> discipline = disciplineService.getById(disciplines.get(disciplines.size() - 1).getId());

        assertFalse(discipline.isPresent());

    }
}