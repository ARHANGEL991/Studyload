package com.ggpk.studyload.repository;

import com.ggpk.studyload.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByName(String name);
}
