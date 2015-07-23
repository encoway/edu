package com.encoway.edu.spring;

import com.encoway.edu.EventDrivenUpdatesContext;

import javax.el.ELContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.FactoryBean;

/**
 * {@link FactoryBean} for {@link EventDrivenUpdatesContext}.
 * Expects a JSF managed bean named {@link #setManagedBeanName(String)}.
 */
public class EventDrivenUpdatesContextFactoryBean implements FactoryBean<EventDrivenUpdatesContext> {

    private String managedBeanName = "eduContext";

    public void setManagedBeanName(String managedBeanName) {
        this.managedBeanName = managedBeanName;
    }

    @Override
    public EventDrivenUpdatesContext getObject() throws Exception {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        return (EventDrivenUpdatesContext) elContext.getELResolver().getValue(elContext, null, managedBeanName);
    }

    @Override
    public Class<?> getObjectType() {
        return EventDrivenUpdatesContext.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}