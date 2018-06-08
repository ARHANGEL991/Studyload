package com.ggpk.studyload.controller;

import com.ggpk.studyload.model.Discipline;
import com.ggpk.studyload.model.Group;
import com.ggpk.studyload.model.Teacher;
import com.ggpk.studyload.service.DisciplineService;
import com.ggpk.studyload.ui.masterdata.ProofReaderAddView;
import de.roskenet.jfxsupport.test.GuiTest;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProofReaderAddViewControllerTest extends GuiTest {

    @Autowired
    private DisciplineService disciplineService;

    @Autowired
    private ProofReaderAddViewController proofReaderAddViewController;

    @PostConstruct
    public void constructView() throws Exception {
        init(ProofReaderAddView.class);
    }


    @Test
    public void addDisciplineTest() {
        double totalHours = 12;
        double totalHoursWithRemoval = 11;
        double weeksInTerm = 1;
        int additionalControl = 2;
        Teacher teacher;
        Group group;
        String discpName = "Физкультура";


        ChoiceBox<Teacher> teacherChoiceBox = find("#choiseTeacherName");
        ChoiceBox<Group> groupChoiceBox = find("#choiseGroupName");
        TextField disciplineNameTextField = find("#txtDisciplineName");
        Spinner<Double> spinnerTotalHours = find("#spinnerTotalHours");

        Spinner<Double> spinnerTotalHoursWithRemoval = find("#spinnerTotalHoursWithRemoval");

        Spinner<Double> spinnerWeeksInTerm = find("#spinnerWeeksInTerm");
        Spinner<Integer> spinnerAdditionalControl = find("#spinnerAdditionalControl");


        disciplineNameTextField.setText(discpName);


        spinnerTotalHours.getEditor().setText(Double.toString(totalHours));
        spinnerTotalHoursWithRemoval.getEditor().setText(Double.toString(totalHoursWithRemoval));
        spinnerWeeksInTerm.getEditor().setText(Double.toString(weeksInTerm));
        spinnerAdditionalControl.getEditor().setText(Integer.toString(additionalControl));


        teacherChoiceBox.getSelectionModel().selectFirst();
        groupChoiceBox.getSelectionModel().selectFirst();

        teacher = teacherChoiceBox.getValue();
        group = groupChoiceBox.getValue();
        clickOn("#btnSave");

        Discipline actual = disciplineService.getDisciplinesByGroup(group).stream()
                .filter(discipline -> discipline.getFullGroup().getTeacher() == teacher)
                .collect(Collectors.toList()).get(0);


        assertEquals(actual.getFullGroup().getFirstTerm().getTotalHoursWithRemoval(), totalHoursWithRemoval, 0.01);
        assertEquals(actual.getFullGroup().getFirstTerm().getTotalHours(), totalHours, 0.01);
        assertEquals(actual.getFullGroup().getFirstTerm().getWeeksInTerm(), weeksInTerm, 0.01);


    }
}