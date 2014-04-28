package com.encoway.edu.model;

public class DemoModel<V> {
	
	private String name;
	
	private V value;

	public DemoModel(String name, V value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
