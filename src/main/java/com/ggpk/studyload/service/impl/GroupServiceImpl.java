package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.Group;
import com.ggpk.studyload.repository.GroupRepository;
import com.ggpk.studyload.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {


    private final GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }


    public <S extends Group> S save(S entity) {
        return groupRepository.save(entity);
    }

    public <S extends Group> List<S> saveAll(Iterable<S> entities) {
        return groupRepository.saveAll(entities);
    }

    public Optional<Group> getById(Long id) {
        return groupRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return groupRepository.existsById(id);
    }

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public long count() {
        return groupRepository.count();
    }

    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }

    public void delete(Group entity) {
        groupRepository.delete(entity);
    }

    public void deleteAll(Iterable<? extends Group> entities) {
        groupRepository.deleteAll(entities);
    }

    public void deleteAll() {
        groupRepository.deleteAll();
    }

    public Group getGroupByName(String name) {
        return groupRepository.findByName(name);
    }
}
