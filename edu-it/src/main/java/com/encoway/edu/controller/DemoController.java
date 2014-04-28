package com.encoway.edu.controller;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.encoway.edu.model.DemoModel;

@ApplicationScoped
@ManagedBean(name="demo")
public class DemoController {
	
	private DemoModel<String> stringModel = new DemoModel<>("EDU", "Old Value");
	
	public DemoModel<String> getStringModel() {
		return stringModel;
	}

}
