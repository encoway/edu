package com.encoway.edu.controller;

import com.encoway.edu.EventDrivenUpdatesContext;
import com.encoway.edu.model.DemoModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

/**
 * Demo bean holding some values and providing logic for manipulating them.
 */
@SessionScoped
@ManagedBean(name = "demo")
public class DemoController implements Serializable {

    /**
     * @since 1.5.7
     */
    private static final long serialVersionUID = -3977106738089852396L;

    private static final String INITIAL_STRING_VALUE = "Old Value";

    private static final int INITIAL_INT_VALUE = 0;
    private static final int UPDATE_INT_VALUE = 1;

    private DemoModel<String> stringModel = new DemoModel<>("String", INITIAL_STRING_VALUE);

    private DemoModel<Integer> intModel = new DemoModel<>("Integer", INITIAL_INT_VALUE);

    @ManagedProperty("#{eduContext}")
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
    }

    /**
     * Changes the {@link #intModel}.
     * 
     * @param event the reason for the update
     */
    public void updateIntModel(AjaxBehaviorEvent event) {
        intModel.setValue(intModel.getValue() + UPDATE_INT_VALUE);
    }

    /**
     * Resets both {@link #stringModel} and {@link #intModel}.
     * 
     * @param event the reason for the reset
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
