package com.ggpk.studyload.repository;

import com.ggpk.studyload.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByName(String name);
}
