package com.encoway.edu.model;

/**
 * A simple model.
 * @param <V> value type
 */
public class DemoModel<V> {

    private String name;

    private V initialValue;

    private V value;

    public DemoModel(String name, V value) {
        this.name = name;
        this.value = this.initialValue = value;
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

    /**
     * Resets the value to the initial value.
     */
    public void reset() {
        setValue(initialValue);
    }

}
