package com.ggpk.studyload.service;

import com.ggpk.studyload.model.Teacher;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TeacherService extends IRepository<Teacher, Long> {

    Teacher getTeacherByName(String name);
}
