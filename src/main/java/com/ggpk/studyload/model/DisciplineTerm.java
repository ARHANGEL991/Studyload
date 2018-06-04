package com.ggpk.studyload.model;


import com.ggpk.studyload.model.interfaces.IDisciplineTerm;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


@Entity
@Table(name = "discipline_term")
@Data
public class DisciplineTerm extends BaseEntity implements IDisciplineTerm {


    private static final double EXAM_ACCEPTANCE_RATE = 0.25;
    private static final double TOTAL_HOURS_REMOVAL_RATE = 0.03;
    private static final double COURSE_ACCEPTANCE_RATE = 0.75;
    private static final double HOUSEWORK_TEST_ACCEPTANCE_RATE = 0.58;


    @Transient
    DecimalFormat df = new DecimalFormat(".##");


    @Transient
    DecimalFormatSymbols symbols = new DecimalFormatSymbols();

    private int laboratoryPracticalExercises;       // laboratory Practical Exercises hours

    private int courseDesign;       //Curse design hours

    private double weeksInTerm;

    private int testsCount;

    private int consultations;

    private double totalHours;

    private double totalHoursWithRemoval;

    private boolean hasExam;

    public DisciplineTerm() {
        symbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(symbols);
    }

    public double getExamHours(int studentCount) {

        return isHasExam() ? (studentCount * EXAM_ACCEPTANCE_RATE) : 0;
    }

//    public double getTotalHoursWithRemoval() {
//        return totalHours == laboratoryPracticalExercises ? totalHours : totalHours - totalHours * TOTAL_HOURS_REMOVAL_RATE;
//    }

    public double getAcceptanceOfCourseProjects(int studentCount) {
        double totalAcceptance = 0;

        if (courseDesign > 0)
            totalAcceptance = studentCount * COURSE_ACCEPTANCE_RATE;


        return totalAcceptance;
    }

    public double getHoursInWeek() {
        return Double.valueOf(df.format((totalHours == 0 ? totalHoursWithRemoval : totalHours) / (weeksInTerm == 0 ? 1 : weeksInTerm)));
    }

    public boolean isHasExam() {
        return hasExam;
    }

    public double getTotal(int studentCount, boolean isExtramural) {

        String format = df.format(getTotalHoursWithRemoval()
                + getAcceptanceOfCourseProjects(studentCount)
                + consultations
                + getExamHours(studentCount)
                + (isExtramural ? getHomeworkTest(studentCount) : 0));


        return Double.valueOf(format);
    }

    public double getHomeworkTest(int studentCount) {
        return testsCount * HOUSEWORK_TEST_ACCEPTANCE_RATE * studentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DisciplineTerm that = (DisciplineTerm) o;

        if (laboratoryPracticalExercises != that.laboratoryPracticalExercises) return false;
        if (courseDesign != that.courseDesign) return false;
        if (Double.compare(that.weeksInTerm, weeksInTerm) != 0) return false;
        if (testsCount != that.testsCount) return false;
        if (consultations != that.consultations) return false;
        if (totalHoursWithRemoval != that.totalHoursWithRemoval) return false;
        return Double.compare(that.totalHours, totalHours) == 0;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + laboratoryPracticalExercises;
        result = 31 * result + courseDesign;
        temp = Double.doubleToLongBits(weeksInTerm);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + testsCount;
        result = 31 * result + consultations;
        temp = Double.doubleToLongBits(totalHoursWithRemoval);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalHours);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
