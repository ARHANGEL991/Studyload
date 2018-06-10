package com.ggpk.studyload.model;


import com.ggpk.studyload.model.enums.DisciplineType;
import com.ggpk.studyload.model.interfaces.IDiscipline;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "academicYear")
@Builder
public class Discipline extends BaseEntity implements IDiscipline<AcademicYear, Group, DisciplineGroup> {


    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private AcademicYear academicYear;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private Group group;


    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private DisciplineGroup fullGroup;


    @Enumerated(value = EnumType.STRING)
    private DisciplineType disciplineType;

    public void setDisciplineType(String disciplineType) {
        this.disciplineType = DisciplineType.fromString(disciplineType);
    }

    public void setType(DisciplineType type) {
        this.disciplineType = type;
    }

    public boolean isSubGroup() {
        return fullGroup.getStudentCount() < group.getStudentCount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        Discipline that = (Discipline) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (academicYear != null ? !academicYear.equals(that.academicYear) : that.academicYear != null) return false;
        if (group != null ? !group.equals(that.group) : that.group != null) return false;
        if (fullGroup != null ? !fullGroup.equals(that.fullGroup) : that.fullGroup != null) return false;
        return disciplineType == that.disciplineType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (academicYear != null ? academicYear.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (fullGroup != null ? fullGroup.hashCode() : 0);
        result = 31 * result + (disciplineType != null ? disciplineType.hashCode() : 0);
        return result;
    }
}
