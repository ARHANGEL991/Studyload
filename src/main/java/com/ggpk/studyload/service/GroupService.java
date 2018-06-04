package com.ggpk.studyload.service;

import com.ggpk.studyload.model.Group;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GroupService extends IRepository<Group, Long> {

    Group getGroupByName(String name);
}
