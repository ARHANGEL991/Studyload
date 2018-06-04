package com.ggpk.studyload.service.impl;


import com.ggpk.studyload.model.AcademicYear;
import com.ggpk.studyload.repository.AcademicYearRepository;
import com.ggpk.studyload.service.AcademicYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicYearServiceImpl implements AcademicYearService {

    private final AcademicYearRepository academicYearRepository;

    @Autowired
    public AcademicYearServiceImpl(AcademicYearRepository academicYearRepository) {
        this.academicYearRepository = academicYearRepository;
    }


    public <S extends AcademicYear> S save(S entity) {
        return academicYearRepository.save(entity);
    }

    public <S extends AcademicYear> List<S> saveAll(Iterable<S> entities) {
        return academicYearRepository.saveAll(entities);
    }

    public Optional<AcademicYear> getById(Long id) {
        return academicYearRepository.findById(id);
    }

    public boolean existsById(Long id) {
        return academicYearRepository.existsById(id);
    }

    public List<AcademicYear> getAll() {
        return academicYearRepository.findAll();
    }

    public long count() {
        return academicYearRepository.count();
    }

    public void deleteById(Long id) {
        academicYearRepository.deleteById(id);
    }

    public void delete(AcademicYear entity) {
        academicYearRepository.delete(entity);
    }

    public void deleteAll(Iterable<? extends AcademicYear> entities) {
        academicYearRepository.deleteAll();
    }

    public void deleteAll() {
        academicYearRepository.deleteAll();
    }
}
