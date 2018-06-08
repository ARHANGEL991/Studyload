package com.ggpk.studyload.service.ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

public interface TableViewColumnAction {

    Hyperlink getUpdateLink();

    Hyperlink getDeleteLink();

    Hyperlink getHideLink();

    Button getDetailLink();

    Node getSingleHyperlinkTableModel(String textOfLink);

    Node getMasterDetailTableModel();

    Node getDefaultTableModel();

    Node getSingleButtonTableModel(String textOfButton);

    Node getDefaultHideTableModel();

}
