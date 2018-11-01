package com.ggpk.studyload.repository;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {


    List<Discipline> findByGroup(Group group);

    List<Discipline> findByGroupName(String groupName);

    List<Discipline> findByGroupNameLike(String groupName);

    List<Discipline> findByFullGroupTeacherName(String teacherName);

    List<Discipline> findByFullGroupTeacherNameLike(String teacherName);

    @Query("select d.name from Discipline d")
    List<String> getAllDisciplineNames();

}
