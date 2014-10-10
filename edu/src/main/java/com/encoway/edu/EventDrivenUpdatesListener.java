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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import com.google.common.base.Strings;

/**
 * {@link SystemEventListener} mapping event names to registered {@link UIComponent}'s IDs.
 */
public class EventDrivenUpdatesListener implements SystemEventListener, ComponentSystemEventListener {

    private final EventDrivenUpdatesMapProvider provider;

    private final String attribute;

    /**
     * Initializes a {@link EventDrivenUpdatesListener} with the specified {@code attribute} name and {@code provider}.
     * @param attribute name of the attribute to look for
     * @param provider EL provider to be used
     */
    public EventDrivenUpdatesListener(String attribute, EventDrivenUpdatesMapProvider provider) {
        this.attribute = attribute;
        this.provider = provider;
    }

    @Override
    public void processEvent(ComponentSystemEvent componentEvent) {
        final UIComponent listener = componentEvent.getComponent();
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final String listenerId = Components.getFullyQualifiedComponentId(facesContext, listener);
        provider.getMap(facesContext).add((String) listener.getAttributes().get(attribute), listenerId);
    }

    @Override
    public boolean isListenerForSource(Object source) {
        if (source instanceof UIComponent) {
            final Object value = ((UIComponent) source).getAttributes().get(attribute);
            if (value instanceof String) {
                return !Strings.isNullOrEmpty((String) value);
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
