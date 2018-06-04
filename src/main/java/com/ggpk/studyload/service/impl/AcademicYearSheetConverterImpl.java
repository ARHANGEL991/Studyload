package com.ggpk.studyload.service.impl;

import com.ggpk.studyload.model.*;
import com.ggpk.studyload.model.additional.AcademicYearImportEntity;
import com.ggpk.studyload.model.additional.DisciplineImportEntity;
import com.ggpk.studyload.model.enums.GroupStudyForm;
import com.ggpk.studyload.service.AcademicPlanReader;
import com.ggpk.studyload.service.AcademicYearSheetConverter;
import com.ggpk.studyload.service.GroupService;
import com.ggpk.studyload.service.TeacherService;
import com.ggpk.studyload.util.EmptyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class AcademicYearSheetConverterImpl implements AcademicYearSheetConverter {


    private final TeacherService teacherService;

    private final GroupService groupService;

    private final AcademicPlanReader planReader;


    @Autowired
    public AcademicYearSheetConverterImpl(TeacherService teacherService, GroupService groupService, AcademicPlanReader planReader) {


        this.teacherService = teacherService;
        this.groupService = groupService;
        this.planReader = planReader;
    }


    @Transactional
    public AcademicYear convertImportAcademicEntity(AcademicYearImportEntity academicYearImportEntity) {

        AcademicYear academicYear = new AcademicYear();

        Group group = new Group();
        Teacher teacher = new Teacher();

        group.setName("");  //initialize to avoid NPE
        teacher.setName("");


        log.info("Start creating POJO academic year");
        for (DisciplineImportEntity row : academicYearImportEntity.getDisciplines()) {

            if (row.getGroupName() != null && !row.getGroupName().trim().equals("ИТОГО") && row.getName() != null) {    //skip all sum rows and row when group or = null

                if (!teacher.getName().equals(row.getTeacherName()))
                    teacher = getOrCreateTeacher(row.getTeacherName());

                if (!group.getName().equals(row.getGroupName()))
                    group = getOrCreateGroup(academicYear,
                            row.getGroupName(),
                            row.getGroupType(),
                            row.getGroupStudentCount(),
                            row.getGroupStudyForm());


                DisciplineTerm firstTerm = new DisciplineTerm();
                firstTerm.setConsultations(row.getFirstTermConsultations());
                firstTerm.setCourseDesign(row.getFirstTermCourseDesign());
                firstTerm.setLaboratoryPracticalExercises(row.getFirstTermLaboratoryPracticalExercises());
                firstTerm.setTestsCount(row.getFirstTermTestsCount());
                firstTerm.setWeeksInTerm(row.getFirstTermWeeksInTerm());
                firstTerm.setTotalHours(row.getFirstTermTotalHours());
                firstTerm.setTotalHoursWithRemoval(row.getFirstTermTotalHoursWithRemoval());
                if (row.getFirstTermExamValue() != null)
                    firstTerm.setHasExam(true);

                if (firstTerm.equals(EmptyEntity.EMPTY_TERM)) {
                    firstTerm = null;
                }


                DisciplineTerm secondTerm = new DisciplineTerm();
                secondTerm.setConsultations(row.getSecondTermConsultations());
                secondTerm.setCourseDesign(row.getSecondTermCourseDesign());
                secondTerm.setLaboratoryPracticalExercises(row.getSecondTermLaboratoryPracticalExercises());
                secondTerm.setTestsCount(row.getSecondTermTestsCount());
                secondTerm.setWeeksInTerm(row.getSecondTermWeeksInTerm());
                secondTerm.setTotalHours(row.getSecondTermTotalHours());
                secondTerm.setTotalHoursWithRemoval(row.getSecondTermTotalHoursWithRemoval());
                if (row.getSecondTermExamValue() != null)
                    secondTerm.setHasExam(true);

                if (secondTerm.equals(EmptyEntity.EMPTY_TERM)) {
                    secondTerm = null;
                }

                DisciplineGroup disciplineGroup = new DisciplineGroup();

                disciplineGroup.setAdditionalControl(row.getAdditionalControl());
                disciplineGroup.setFirstTerm(firstTerm);
                disciplineGroup.setSecondTerm(secondTerm);
                disciplineGroup.setStudentCount(row.getGroupStudentCount());
                disciplineGroup.setTeacher(teacher);

                Discipline discipline = new Discipline();
                discipline.setFullGroup(disciplineGroup);
                discipline.setDisciplineType(row.getDisciplineType());
                discipline.setAcademicYear(academicYear);
                discipline.setFullGroup(disciplineGroup);
                discipline.setGroup(group);
                discipline.setName(row.getName());


                academicYear.getDisciplines().add(discipline);
            }
        }

        return academicYear;
    }

    /**
     * Search or create new teacher if group in row isn't the same
     * To reduce database queries
     *
     * @param teacherName current data row
     * @return found or create teacher
     */
    private Teacher getOrCreateTeacher(String teacherName) {
        Teacher teacher;

        log.debug("Search teacher in DB");
        teacher = teacherService.getTeacherByName(teacherName);

        if (teacher == null) {
            teacher = new Teacher();
            teacher.setName(teacherName);
            log.debug("Save teacher in DB");
            teacherService.save(teacher);
        }
        return teacher;
    }

    /**
     * Search or create new group if group in row isn't the same
     * To reduce database queries
     *
     * @param academicYear    to set group.academicYear if create new group
     * @param searchGroupName search group name
     * @param newGroupType    group type for new group
     * @param newStudentCount student count for new group
     * @return found or create group
     */
    private Group getOrCreateGroup(AcademicYear academicYear, String searchGroupName, String newGroupType, int newStudentCount, String groupStudyForm) {

        Group group;

        log.debug("Search group in DB");
        group = groupService.getGroupByName(searchGroupName);


        if (group == null) {
            group = new Group();
            group.setGroupType(newGroupType);
            group.setStudentCount(newStudentCount);
            group.setName(searchGroupName);
            group.setCurrentAcademicYear(academicYear);
            if (groupStudyForm != null && groupStudyForm.equalsIgnoreCase(GroupStudyForm.EXTRAMURAL.getValue()))
                group.setGroupStudyForm(GroupStudyForm.fromString(groupStudyForm));
            else {
                group.setGroupStudyForm(GroupStudyForm.FULL_TIME);
            }
            log.debug("Save group in DB");
            groupService.save(group);

        }
        return group;
    }


}
