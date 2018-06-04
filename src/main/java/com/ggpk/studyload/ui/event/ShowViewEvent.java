package com.ggpk.studyload.ui.event;

import de.felixroske.jfxsupport.AbstractFxmlView;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class ShowViewEvent<T extends AbstractFxmlView> extends ApplicationEvent {

    private T fxmlView;


    public ShowViewEvent(Object source, T fxmlView) {
        super(source);
        this.fxmlView = fxmlView;
    }


}
