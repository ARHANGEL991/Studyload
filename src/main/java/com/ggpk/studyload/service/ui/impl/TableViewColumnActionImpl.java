package com.ggpk.studyload.service.ui.impl;

import com.ggpk.studyload.service.impl.LangProperties;
import com.ggpk.studyload.service.ui.TableViewColumnAction;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class TableViewColumnActionImpl implements TableViewColumnAction {

    private Hyperlink updateLink;
    private Hyperlink deleteLink;
    private Hyperlink hideLink;
    private Button detailLink;
    private ToggleButton onOff;
    private CheckBox checklist;
    private MessageSource messageSource;

    @Autowired
    public TableViewColumnActionImpl(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public Hyperlink getUpdateLink() {
        return updateLink;
    }

    public Hyperlink getDeleteLink() {
        return deleteLink;
    }

    public Hyperlink getHideLink() {
        return hideLink;
    }

    public Button getDetailLink() {
        return detailLink;
    }

    public ToggleButton getOnOff() {
        return onOff;
    }

    public CheckBox getChecklist() {
        return checklist;
    }

    public Node getSingleButtonTableModel(String textOfButton) {
        this.detailLink = new Button();
        this.detailLink.setPrefSize(70, 20);
        this.detailLink.setText(messageSource.getMessage(textOfButton, null, Locale.getDefault()));
        this.detailLink.setTextAlignment(TextAlignment.CENTER);
        return this.detailLink;
    }

    public Node getSingleHyperlinkTableModel(String textOfLink) {
        this.deleteLink = new Hyperlink();
        this.deleteLink.setText(messageSource.getMessage(textOfLink, null, Locale.getDefault()));
        return deleteLink;
    }

    public Node getMasterDetailTableModel() {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        this.deleteLink = new Hyperlink();
        this.deleteLink.setText(messageSource.getMessage(LangProperties.DELETE.getValue(), null, Locale.getDefault()));
        this.deleteLink.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT));
        this.deleteLink.setTextFill(Color.RED);

        this.detailLink = new Button();
        this.detailLink.setText(messageSource.getMessage(LangProperties.VIEW.getValue(), null, Locale.getDefault()));
        this.detailLink.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.FOLDER_OPEN_ALT));

        this.updateLink = new Hyperlink();
        this.updateLink.setText(messageSource.getMessage(LangProperties.UPDATE.getValue(), null, Locale.getDefault()));
        this.updateLink.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EDIT));
        this.updateLink.setTextFill(Color.YELLOWGREEN);

        box.getChildren().add(detailLink);
        box.getChildren().add(updateLink);
        box.getChildren().add(deleteLink);

        return box;
    }

    public Node getDefaultTableModel() {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        updateLink = new Hyperlink();
        updateLink.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.EDIT));
        updateLink.setText(messageSource.getMessage(LangProperties.UPDATE.getValue(), null, Locale.getDefault()));
        updateLink.setTextFill(Color.YELLOWGREEN);

        deleteLink = new Hyperlink();
        deleteLink.setText(messageSource.getMessage(LangProperties.DELETE.getValue(), null, Locale.getDefault()));
        deleteLink.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.TRASH_ALT));
        deleteLink.setTextFill(Color.RED);

        box.getChildren().add(updateLink);
        box.getChildren().add(deleteLink);
        return box;
    }

    public Node getDefaultHideTableModel() {
        HBox box = new HBox(5);
        box.setAlignment(Pos.CENTER);

        hideLink = new Hyperlink();
        hideLink.setText(messageSource.getMessage(LangProperties.HIDE.getValue(), null, Locale.getDefault()));
        hideLink.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.COMPRESS));
        hideLink.setTextFill(Color.BLUE);

        box.getChildren().add(hideLink);
        return box;
    }


}