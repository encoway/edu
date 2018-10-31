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

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.inject.Inject;

import java.util.Map;

/**
 * {@link SystemEventListener} mapping event names to registered {@link UIComponent}'s IDs.
 */
@ApplicationScoped
public class EventDrivenUpdatesListener implements SystemEventListener, ComponentSystemEventListener {

    /**
     * Default value.
     */
    public static final String ATTRIBUTE_DEFAULT_NAME = "updateOn";
    public static final String MAP_NAME = "edu";

    private String attribute = ATTRIBUTE_DEFAULT_NAME;
    private String mapVariableName = MAP_NAME;

    @Inject
    private EventDrivenUpdatesMap map;

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setMapVariableName(String mapVariableName) {
        this.mapVariableName = mapVariableName;
    }

    @Override
    public void processEvent(ComponentSystemEvent componentEvent) {
        final UIComponent listener = componentEvent.getComponent();
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final String listenerId = Components.getFullyQualifiedComponentId(facesContext, listener);

        map.add((String) listener.getAttributes().get(attribute), listenerId);

        Map<String, Object> viewMap = facesContext.getViewRoot().getViewMap();
        if (viewMap.get(mapVariableName) == null) {
            viewMap.put(mapVariableName, map);
        }
    }

    @Override
    public boolean isListenerForSource(Object source) {
        if (source instanceof UIComponent) {
            final Object value = ((UIComponent) source).getAttributes().get(attribute);
            if (value instanceof String) {
                return !((String) value).isEmpty();
            }
        }
        return false;
    }

    @Override
    public void processEvent(SystemEvent event) {
        if (event instanceof ComponentSystemEvent) {
            processEvent((ComponentSystemEvent) event);
        }
    }

}
