package com.ggpk.studyload.controller;

import com.ggpk.studyload.model.*;
import com.ggpk.studyload.model.enums.DisciplineType;
import com.ggpk.studyload.service.DisciplineService;
import com.ggpk.studyload.service.GroupService;
import com.ggpk.studyload.service.TeacherService;
import com.ggpk.studyload.service.impl.LangProperties;
import com.ggpk.studyload.service.ui.notifications.DialogBalloon;
import com.ggpk.studyload.service.ui.notifications.ValidatorMessages;
import com.ggpk.studyload.ui.event.ShowViewEvent;
import com.ggpk.studyload.ui.masterdata.ProofReaderAddView;
import com.ggpk.studyload.ui.masterdata.ProofReaderView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;

import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class ProofReaderAddViewController implements FxInitializable {

    @FXML
    private TextField txtDisciplineName;

    @FXML
    private ChoiceBox<Group> choiseGroupName;

    @FXML
    private ComboBox<Teacher> choiseTeacherName;

    @FXML
    private Spinner<Double> spinnerTotalHours;

    @FXML
    private Spinner<Double> spinnerTotalHoursWithRemoval;

    @FXML
    private Spinner<Double> spinnerWeeksInTerm;

    @FXML
    private ToggleGroup isEdu;

    @FXML
    private RadioButton radioBtnProfEdu;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Spinner<Integer> spinnerAdditionalControl;

    @FXML
    private RadioButton radioBtnFirstTerm;


    private final ProofReaderAddView proofReaderAddView;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ProofReaderView proofReaderView;

    private final TeacherService teacherService;

    private final GroupService groupService;

    private final DisciplineService disciplineService;

    private ValidationSupport validationSupport;

    private final ValidatorMessages validatorMessages;

    private final MessageSource messageSource;

    private final DialogBalloon dialogBalloon;






    @Autowired
    public ProofReaderAddViewController(ProofReaderAddView proofReaderAddView, ApplicationEventPublisher applicationEventPublisher, ProofReaderView proofReaderView, TeacherService teacherService, GroupService groupService, DisciplineService disciplineService, ValidatorMessages validatorMessages, MessageSource messageSource, DialogBalloon dialogBalloon) {
        this.proofReaderAddView = proofReaderAddView;
        this.applicationEventPublisher = applicationEventPublisher;
        this.proofReaderView = proofReaderView;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.disciplineService = disciplineService;
        this.validatorMessages = validatorMessages;
        this.messageSource = messageSource;
        this.dialogBalloon = dialogBalloon;
    }


    @FXML
    public void doClose(ActionEvent event) {
        applicationEventPublisher.publishEvent(new ShowViewEvent<>(this, proofReaderView));
    }

    @FXML
    void doSave(ActionEvent event) {
        Discipline discipline = new Discipline();
        discipline.setName(txtDisciplineName.getText());
        discipline.setGroup(choiseGroupName.getValue());
        if (radioBtnProfEdu.isSelected()) {
            discipline.setType(DisciplineType.PROF_EDU_COMPONENT);
        } else {
            discipline.setType(DisciplineType.GENERAL_EDU_COMPONENT);
        }
        DisciplineGroup fullGroup = new DisciplineGroup();
        DisciplineTerm disciplineTerm = new DisciplineTerm();

        disciplineTerm.setTotalHours(spinnerTotalHours.getValue());
        disciplineTerm.setTotalHoursWithRemoval(spinnerTotalHoursWithRemoval.getValue());
        disciplineTerm.setTotalHoursWithRemoval(spinnerWeeksInTerm.getValue());

        fullGroup.setTeacher(choiseTeacherName.getValue());
        fullGroup.setDiscipline(discipline);

        fullGroup.setAdditionalControl(spinnerAdditionalControl.getValue());

        if (radioBtnFirstTerm.isSelected()) {
            fullGroup.setFirstTerm(disciplineTerm);
        } else {
            fullGroup.setSecondTerm(disciplineTerm);
        }
        discipline.setFullGroup(fullGroup);

        disciplineService.save(discipline);

        dialogBalloon.succeed(LangProperties.SUCESSED_SAVED.getValue());

    }

    public void initialize(URL location, ResourceBundle resources) {
        choiseTeacherName.getItems().addAll(teacherService.getAll());
        choiseGroupName.getItems().addAll(groupService.getAll());

        choiseTeacherName.setConverter(new StringConverter<Teacher>() {
            public String toString(Teacher object) {
                return object.getName();
            }

            public Teacher fromString(String string) {
                return null;
            }
        });
        choiseGroupName.setConverter(new StringConverter<Group>() {
            public String toString(Group object) {
                return object.getName();
            }

            public Group fromString(String string) {
                return null;
            }
        });

        spinnerTotalHours.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100000, 0));
        spinnerWeeksInTerm.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100000, 0));
        spinnerTotalHoursWithRemoval.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 100000, 0));
        spinnerAdditionalControl.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0));
//        messageSource.getMessage(LangProperties.LEVEL.getValue(),Locale.c);
    }

//    public void initValidation() {
//        validationSupport = new ValidationSupport();
//        validationSupport.invalidProperty().addListener((observable, oldValue, newValue) -> btnSave.setDisable(newValue));
//        validationSupport.registerValidator(txtType,
//                Validator.createEmptyValidator(
//                        validatorMessages.validatorNotSelected(messageSource.getMessage(LangProperties.LEVEL.getValue()))));
//        validationSupport.redecorate();
//        validationSupport.registerValidator(txtFullname, true, Validator.createEmptyValidator(
//                validatorMessages.validatorNotNull(messaLangProperties.ACCOUNT_FULLNAME)), Severity.ERROR
//        ));
//        validationSupport.registerValidator(txtUsername, (Control textfield, String value) -> ValidationResult.fromErrorIf(
//                textfield,
//                validatorMessages.validatorMinMax(lang.getSources(LangProperties.USERNAME), 3, 25),
//                value.trim().length() < 3 || value.trim().length() > 25 || value.trim().isEmpty()
//        ));
//        validationSupport.registerValidator(txtPassword, true,
//                Validator.createEmptyValidator(
//                        validatorMessages.validatorNotNull(lang.getSources(LangProperties.PASSWORD))));
//    }
}


