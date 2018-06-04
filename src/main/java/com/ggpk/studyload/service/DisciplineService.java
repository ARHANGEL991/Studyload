package com.ggpk.studyload.service;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.Group;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface DisciplineService extends IRepository<Discipline, Long> {


    List<Discipline> getDisciplinesByGroup(Group group);

    List<Discipline> getDisciplinesByGroupName(String groupName);

    List<Discipline> getDisciplinesByGroupNameLike(String groupName);

    List<Discipline> getDisciplinesByTeacherName(String teacherName);

    List<Discipline> getDisciplinesByTeacherNameLike(String teacherName);
}
