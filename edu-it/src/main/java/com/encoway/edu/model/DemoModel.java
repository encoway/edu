package com.encoway.edu.model;

import java.io.Serializable;

/**
 * A simple model.
 * 
 * @param <V> value type
 */
public class DemoModel<V extends Serializable> implements Serializable {

    /**
     * @since 1.5.7
     */
    private static final long serialVersionUID = 1659999273691357169L;

    private String name;

    private V initialValue;

    private V value;

    /**
     * Initializes a {@link DemoModel}.
     * 
     * @param name the name
     * @param value the value
     */
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
