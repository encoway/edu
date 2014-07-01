package com.encoway.edu.controller;

import com.encoway.edu.model.DemoModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

/**
 * Demo bean holding some values and providing logic for manipulating them. 
 */
@SessionScoped
@ManagedBean(name = "demo")
public class DemoController {

    private DemoModel<String> stringModel = new DemoModel<>("String", "Old Value");

    private DemoModel<Integer> intModel = new DemoModel<>("Integer", 0);

    public DemoModel<String> getStringModel() {
        return stringModel;
    }

    public DemoModel<Integer> getIntModel() {
        return intModel;
    }

    /**
     * Changes the {@link #stringModel}.
     * @param event the reason for the update
     */
    public void updateStringModel(AjaxBehaviorEvent event) {
        stringModel.setValue(stringModel.getValue() + "+");
    }

    /**
     * Changes the {@link #intModel}.
     * @param event the reason for the update
     */
    public void updateIntModel(AjaxBehaviorEvent event) {
        intModel.setValue(intModel.getValue() + 1);
    }

    /**
     * Resets both {@link #stringModel} and {@link #intModel}.
     * @param event the reason for the reset
     */
    public void reset(AjaxBehaviorEvent event) {
        stringModel.reset();
        intModel.reset();
    }

}
