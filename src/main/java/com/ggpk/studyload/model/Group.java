package com.ggpk.studyload.model;


import com.ggpk.studyload.model.enums.GroupStudyForm;
import com.ggpk.studyload.model.enums.GroupType;
import com.ggpk.studyload.model.interfaces.IGroup;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "academic_group")
@Data
@NoArgsConstructor
@ToString(exclude = {"currentAcademicYear", "disciplines"})
public class Group extends BaseEntity implements IGroup<AcademicYear, Discipline> {


    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<Discipline> disciplines;


    //    @ManyToOne
    @ManyToOne(cascade = CascadeType.PERSIST)
//    @ManyToOne(cascade = { CascadeType.REFRESH})
    private AcademicYear currentAcademicYear;

    @Column(unique = true)
    private String name;

    private int studentCount;

    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @Enumerated(EnumType.STRING)
    private GroupStudyForm groupStudyForm;


    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = GroupType.fromString(groupType);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return name != null ? name.equals(group.name) : group.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
