package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.Group;
import com.ggpk.studyload.repository.DisciplineRepository;
import com.ggpk.studyload.service.DisciplineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineRepository repository;


    @Autowired
    public DisciplineServiceImpl(DisciplineRepository repository) {
        this.repository = repository;
    }


    public <S extends Discipline> S save(S entity) {
        return repository.save(entity);
    }

    public <S extends Discipline> List<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    public Optional<Discipline> getById(Long id) {
        return repository.findById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public List<Discipline> getAll() {
        return repository.findAll();
    }

    public long count() {
        return repository.count();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void delete(Discipline entity) {
        repository.delete(entity);
    }

    public void deleteAll(Iterable<? extends Discipline> entities) {
        repository.deleteAll(entities);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Discipline> getDisciplinesByGroup(Group group) {
        return repository.findByGroup(group);
    }

    public List<Discipline> getDisciplinesByGroupName(String groupName) {
        return repository.findByGroupName(groupName);
    }

    public List<Discipline> getDisciplinesByTeacherName(String teacherName) {
        return repository.findByFullGroupTeacherName(teacherName);
    }


    public List<Discipline> getDisciplinesByGroupNameLike(String groupName) {
        return repository.findByGroupNameLike("%" + groupName + "%");
    }

    public List<Discipline> getDisciplinesByTeacherNameLike(String teacherName) {
        return repository.findByFullGroupTeacherNameLike("%" + teacherName + "%");
    }

    public List<String> getAllDisciplineNames() {
        return repository.getAllDisciplineNames();
    }
}
