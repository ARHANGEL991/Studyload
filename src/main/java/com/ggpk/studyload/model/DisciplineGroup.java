package com.ggpk.studyload.model;

import com.ggpk.studyload.model.additional.YearDisciplineAccounting;
import com.ggpk.studyload.model.enums.GroupStudyForm;
import com.ggpk.studyload.model.interfaces.ISubGroup;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "discipline")
public class DisciplineGroup extends BaseEntity implements ISubGroup {


    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Teacher teacher;


    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.PERSIST)
    private DisciplineTerm firstTerm;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.PERSIST)
    private DisciplineTerm secondTerm;

    @OneToOne(mappedBy = "fullGroup", fetch = FetchType.EAGER, orphanRemoval = true)
    private Discipline discipline;

    private int additionalControl;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private YearDisciplineAccounting yearDisciplineAccounting;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private YearDisciplineAccounting yearDisciplineAccountingAdditionalControl;

    private int studentCount;

    public double getTotalSum() {
        double totalSum = 0;

        if (true) {
            if (firstTerm != null) {
                totalSum += firstTerm.getTotal(studentCount, GroupStudyForm.isExtramular(discipline.getGroup().getGroupStudyForm()));
            }

            if (secondTerm != null) {
                totalSum += secondTerm.getTotal(studentCount, GroupStudyForm.isExtramular(discipline.getGroup().getGroupStudyForm()));
            }
        }

        return totalSum;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DisciplineGroup that = (DisciplineGroup) o;
        return additionalControl == that.additionalControl &&
                studentCount == that.studentCount &&
                Objects.equals(teacher, that.teacher) &&
                Objects.equals(firstTerm, that.firstTerm) &&
                Objects.equals(secondTerm, that.secondTerm);
    }

    public int hashCode() {

        return Objects.hash(teacher, firstTerm, secondTerm, additionalControl, studentCount);
    }
}
