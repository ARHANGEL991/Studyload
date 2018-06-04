package com.ggpk.studyload.model.additional;


import com.ggpk.studyload.model.Group;
import com.ggpk.studyload.model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisciplineExportEntity {

    private Group group;
    private double[] month;
    private Teacher teacher;
    private String name;


}
