package com.encoway.edu.controller;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.encoway.edu.EventDrivenUpdatesContext;
import com.encoway.edu.model.DemoModel;
import com.encoway.edu.model.IntegerModel;
import com.encoway.edu.model.StringModel;

/**
 * Demo bean holding some values and providing logic for manipulating them.
 */
@Named("demo")
@SessionScoped
public class DemoController implements Serializable {

    /**
     * @since 1.5.7
     */
    private static final long serialVersionUID = -3977106738089852396L;

    private static final String INITIAL_STRING_VALUE = "Old Value";

    private static final int INITIAL_INT_VALUE = 0;
    private static final int UPDATE_INT_VALUE = 1;

    private StringModel stringModel = new StringModel("String", INITIAL_STRING_VALUE);

    private IntegerModel intModel = new IntegerModel("Integer", INITIAL_INT_VALUE);

    @Inject
    private EventDrivenUpdatesContext eduContext;

    public DemoModel<String> getStringModel() {
        return stringModel;
    }

    public DemoModel<Integer> getIntModel() {
        return intModel;
    }

    public void setEduContext(EventDrivenUpdatesContext eduContext) {
        this.eduContext = eduContext;
    }

    public EventDrivenUpdatesContext getEduContext() {
        return eduContext;
    }

    /**
     * Changes the {@link #stringModel}.
     * 
     * @param event the reason for the update
     */
    public void updateStringModel(AjaxBehaviorEvent event) {
        stringModel.setValue(stringModel.getValue() + createUpdateString());
        eduContext.update("string-model-changed");
    }

    /**
     * Changes the {@link #intModel}.
     * 
     * @param event the reason for the update
     */
    public void updateIntModel(AjaxBehaviorEvent event) {
        intModel.setValue(intModel.getValue() + UPDATE_INT_VALUE);
        eduContext.update("int-model-changed");
    }

    /**
     * Resets both {@link #stringModel} and {@link #intModel}.
     */
    public void reset() {
        stringModel.reset();
        intModel.reset();
        eduContext.update("string-model-changed int-model-changed");
    }

    private String createUpdateString() {
        return " (upd. " + new SimpleDateFormat("hh:mm").format(new Date()) + ")";
    }
}
