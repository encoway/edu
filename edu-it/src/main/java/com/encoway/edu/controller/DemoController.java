package com.encoway.edu.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import com.encoway.edu.model.DemoModel;

@SessionScoped
@ManagedBean(name="demo")
public class DemoController {
	
	private DemoModel<String> stringModel = new DemoModel<>("String", "Old Value");
	
	private DemoModel<Integer> intModel = new DemoModel<>("Integer", 0);
	
	public DemoModel<String> getStringModel() {
		return stringModel;
	}
	
	public DemoModel<Integer> getIntModel() {
		return intModel;
	}
	
	public void reset(ActionEvent event) {
		stringModel.reset();
		intModel.reset();
	}

}
