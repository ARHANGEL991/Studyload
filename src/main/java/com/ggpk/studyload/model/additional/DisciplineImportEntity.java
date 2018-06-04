package com.ggpk.studyload.model.additional;

import lombok.Data;

@Data
public class DisciplineImportEntity {


    //Discipline fields
    private String name;

    private boolean haveSubGroup;

    private String disciplineType;

    //Discipline group
    private int additionalControl;
    //Discipline First Term 

    private int firstTermLaboratoryPracticalExercises;       // laboratory Practical Exercises hours

    private int firstTermCourseDesign;       //Curse design hours

    private double firstTermWeeksInTerm;

    private boolean firstTermHasExam;

    private int firstTermTestsCount;

    private int firstTermConsultations;

    private double firstTermTotalHours;

    private double firstTermTotalHoursWithRemoval;

    private String firstTermExamValue;

    //Discipline Second Term

    private int secondTermLaboratoryPracticalExercises;       // laboratory Practical Exercises hours

    private int secondTermCourseDesign;       //Curse design hours

    private double secondTermWeeksInTerm;

    private boolean secondTermHasExam;

    private int secondTermTestsCount;

    private int secondTermConsultations;

    private double secondTermTotalHours;

    private double secondTermTotalHoursWithRemoval;

    private String secondTermExamValue;

    //Teacher field

    private String teacherName;

    //Group fields
    private String groupName;

    private int groupStudentCount;

    private String groupType;

    private String groupStudyForm;


}
