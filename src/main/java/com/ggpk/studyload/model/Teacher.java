package com.ggpk.studyload.model;

import com.ggpk.studyload.model.interfaces.ITeacher;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "disciplines", callSuper = false)
public class Teacher extends BaseEntity implements ITeacher {


    private String name;

    //    @OneToMany(mappedBy = "teacher", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @OneToMany(mappedBy = "teacher", orphanRemoval = true)
    private List<DisciplineGroup> disciplines;


    public String getFullName() {
        return String.format("%s %s", getLastName(), getInitials());
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return "";
    }

    public String getInitials() {
        return "";
    }

}
