package com.encoway.edu.controller;

import com.encoway.edu.model.DemoModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

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

    public void updateStringModel(AjaxBehaviorEvent event) {
        stringModel.setValue(stringModel.getValue() + "+");
    }

    public void updateIntModel(AjaxBehaviorEvent event) {
        intModel.setValue(intModel.getValue() + 1);
    }

    public void reset(AjaxBehaviorEvent event) {
        stringModel.reset();
        intModel.reset();
    }

}
