package com.encoway.edu.controller;

import com.encoway.edu.model.DemoModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

/**
 * Demo bean holding some values and providing logic for manipulating them.
 */
@SessionScoped
@ManagedBean(name = "demo")
public class DemoController {

    private static final String INITIAL_STRING_VALUE = "Old Value";

    private static final int INITIAL_INT_VALUE = 0;
    private static final int UPDATE_INT_VALUE = 1;

    private DemoModel<String> stringModel = new DemoModel<>("String", INITIAL_STRING_VALUE);

    private DemoModel<Integer> intModel = new DemoModel<>("Integer", INITIAL_INT_VALUE);

    public DemoModel<String> getStringModel() {
        return stringModel;
    }

    public DemoModel<Integer> getIntModel() {
        return intModel;
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
    public void reset(AjaxBehaviorEvent event) {
        stringModel.reset();
        intModel.reset();
    }

    private String createUpdateString() {
        return " (upd. " + new SimpleDateFormat("hh:mm").format(new Date()) + ")";
    }
}
