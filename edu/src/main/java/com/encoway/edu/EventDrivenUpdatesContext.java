/**
 * Copyright (C) 2014 encoway GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.encoway.edu;

import java.util.Map;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 * EDU context of the a JSF application.
 */
public class EventDrivenUpdatesContext {
    
    /**
     * Name of the `context-param` to override the context bean name,
     * defaults to {@value #EVNT_CONTEXT_DEFAULT_NAME}.
     */
    public static final String EVENT_CONTEXT_PARAM = Configuration.PARAM_PREFIX + ".CONTEXT_NAME";
    
    /**
     * Default value.
     */
    public static final String EVNT_CONTEXT_DEFAULT_NAME = "eduContext";

    private final EventListenerMapELResolver resolver;
    
    /**
     * Returns the instance of {@link EventDrivenUpdatesContext} associated with the current {@link Application}.
     * @return the context, may not be {@code null}
     * @throws IllegalStateException if the context has not been initialized
     * @throws IllegalStateException if a bean of another type than {@link EventDrivenUpdatesContext} is found
     */
    public static EventDrivenUpdatesContext getCurrentInstance() {
        final Map<String, Object> applicationMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
        final String name = Configuration.getParameter(EVENT_CONTEXT_PARAM, EVNT_CONTEXT_DEFAULT_NAME);
        final Object bean = applicationMap.get(name);
        
        if (bean == null) {
            throw new IllegalStateException(
                    "no EDU context has been initialized/it cannot be found under the name " + name);
        }
        
        if (!(bean instanceof EventDrivenUpdatesContext)) {
            throw new IllegalStateException(
                    "unexpected bean found unter name " + name + ": " + bean + " expected an instance of " + EventDrivenUpdatesContext.class);
        }
        
        return (EventDrivenUpdatesContext) bean;
    }

    /**
     * Updates all components registered for one or more of the {@code events}.
     * @param events events expression, see {@link com.encoway.edu package documentation}
     */
    public void update(String events) {
        update(FacesContext.getCurrentInstance(), events);
    }
    
    /**
     * Updates all components registered for one or more of the {@code events}.
     * @param facesContext the JSF context to be used for lookups
     * @param events events expression, see {@link com.encoway.edu package documentation}
     */
    public void update(FacesContext facesContext, String events) {
        final EventListenerMap map = resolver.getEventListenerMap(facesContext);
        Components.render(facesContext, map.getSeparate(events));
    }

    /**
     * Initializes a {@link EventDrivenUpdatesContext}.
     * @param resolver EL resolver to be used
     */
    EventDrivenUpdatesContext(EventListenerMapELResolver resolver) {
        this.resolver = resolver;
    }

    static void initialize(EventListenerMapELResolver resolver) {
        final Map<String, Object> applicationMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
        final String name = Configuration.getParameter(EVENT_CONTEXT_PARAM, EVNT_CONTEXT_DEFAULT_NAME);
        final Object bean = applicationMap.get(name);
        if (bean == null) {
            applicationMap.put(name, new EventDrivenUpdatesContext(resolver));
        } else if (!(bean instanceof EventDrivenUpdatesContext)) {
            throw new IllegalStateException("the bean name " + name + " is already in use: " + bean);
        } else {
            throw new IllegalStateException(EventDrivenUpdatesContext.class + " bean has already been set under the name " + name);
        }
    }

}
