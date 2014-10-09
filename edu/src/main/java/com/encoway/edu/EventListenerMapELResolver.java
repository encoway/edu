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

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.FacesContext;

/**
 * {@link ELResolver} for a {@link EventListenerMap}.
 */
class EventListenerMapELResolver extends ELResolver {
    
    /**
     * Name of the `context-param` to override the EL variable name through which the EDU map is accessible,
     * defaults to {@value #EVENT_LISTENER_MAP_DEFAULT_NAME}.
     */
    public static final String EVENT_LISTENER_MAP_PARAM = Configuration.PARAM_PREFIX + ".MAP_NAME";

    /**
     * Default value.
     */
    public static final String EVENT_LISTENER_MAP_DEFAULT_NAME = "edu";

    private final String mapName;
    
    EventListenerMapELResolver() {
        this(getMapName());
    }

    EventListenerMapELResolver(String mapName) {
        this.mapName = mapName;
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (isResolvable(base, property)) {
            context.setPropertyResolved(true);
            return getEventListenerMap(FacesContext.getCurrentInstance());
        }
        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return Map.class;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        if (isResolvable(base, property)) {
            throw new UnsupportedOperationException("this resolver is read-only");
        }
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return EventListenerMap.class;
    }

    /**
     * Returns an {@link EventListenerMap} for the specified {@code facesContext}.
     * A new one will be created on demand.
     * @param facesContext a JSF context
     * @return an {@link EventListenerMap}
     */
    public EventListenerMap getEventListenerMap(FacesContext facesContext) {
        Map<String, Object> viewMap = facesContext.getViewRoot().getViewMap();
        EventListenerMap eventListenerMap = (EventListenerMap) viewMap.get(mapName);
        if (eventListenerMap == null) {
            eventListenerMap = new EventListenerMap();
            viewMap.put(mapName, eventListenerMap);
        }
        return eventListenerMap;
    }

    static String getMapName() {
        return Configuration.getParameter(EVENT_LISTENER_MAP_PARAM, EVENT_LISTENER_MAP_DEFAULT_NAME);
    }

    private boolean isResolvable(Object base, Object property) {
        return base == null && mapName.equals(property);
    }

}