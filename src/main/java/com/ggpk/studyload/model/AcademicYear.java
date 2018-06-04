package com.ggpk.studyload.model;

import com.ggpk.studyload.model.interfaces.IAcademicYear;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "academic_year")
@Data
@NoArgsConstructor
public class AcademicYear extends BaseEntity implements IAcademicYear {


    private Year startYear;
    private Year endYear;

    @OneToMany(mappedBy = "academicYear", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Discipline> disciplines = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AcademicYear that = (AcademicYear) o;

        if (startYear != null ? !startYear.equals(that.startYear) : that.startYear != null) return false;
        return endYear != null ? endYear.equals(that.endYear) : that.endYear == null;
    }

    @Override
    public int hashCode() {
        int result = startYear != null ? startYear.hashCode() : 0;
        result = 31 * result + (endYear != null ? endYear.hashCode() : 0);
        return result;
    }
}
