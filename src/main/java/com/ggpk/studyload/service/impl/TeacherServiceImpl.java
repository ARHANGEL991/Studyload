package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.Teacher;
import com.ggpk.studyload.repository.TeacherRepository;
import com.ggpk.studyload.service.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository repository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository repository) {
        this.repository = repository;

    }


    public <S extends Teacher> S save(S entity) {
        return repository.save(entity);
    }

    public <S extends Teacher> List<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    public Optional<Teacher> getById(Long id) {
        return repository.findById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public List<Teacher> getAll() {
        return repository.findAll();
    }

    public long count() {
        return repository.count();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);

    }

    public void delete(Teacher entity) {
        repository.delete(entity);
    }

    public void deleteAll(Iterable<? extends Teacher> entities) {
        repository.deleteAll(entities);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Teacher getTeacherByName(String name) {
        return repository.findByName(name);
    }

}
