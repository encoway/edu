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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.google.common.base.Strings;

public class EventDrivenUpdatesConfig {

    static class ViewMapProvider implements EventDrivenUpdatesMapProvider {

        private final String mapName;

        public ViewMapProvider(String mapName) {
            this.mapName = mapName;
        }

        @Override
        public String getName() {
            return mapName;
        }

        @Override
        public EventDrivenUpdatesMap getMap(FacesContext facesContext) {
            Map<String, Object> viewMap = facesContext.getViewRoot().getViewMap();
            EventDrivenUpdatesMap eventListenerMap = (EventDrivenUpdatesMap) viewMap.get(mapName);
            if (eventListenerMap == null) {
                eventListenerMap = new EventDrivenUpdatesMap();
                viewMap.put(mapName, eventListenerMap);
            }
            return eventListenerMap;
        }
    }

    /**
     * Default value.
     */
    public static final String EVENTS_ATTRIBUTE_DEFAULT_NAME = "updateOn";
    /**
     * Name of the `context-param` to override the listener attribute,
     * defaults to {@value EventDrivenUpdatesConfig#EVENTS_ATTRIBUTE_DEFAULT_NAME}.
     */
    public static final String EVENTS_ATTRIBUTE_PARAM = EventDrivenUpdatesConfig.PARAM_PREFIX + ".ATTRIBUTE_NAME";
    /**
     * Name of the `context-param` to override the EL variable name through which the EDU map is accessible,
     * defaults to {@value EVENT_LISTENER_MAP_DEFAULT_NAME}.
     */
    public static final String EVENT_LISTENER_MAP_PARAM = EventDrivenUpdatesConfig.PARAM_PREFIX + ".MAP_NAME";
    /**
     * Default value.
     */
    public static final String EVENT_LISTENER_MAP_DEFAULT_NAME = "edu";
    
    private final EventDrivenUpdatesListener listener;
    private final EventDrivenUpdatesMapResolver resolver;
    private final EventDrivenUpdatesMapProvider provider;
    
    /**
     * Prefix used for `context-param` definitions.
     */
    static final String PARAM_PREFIX = "com.encoway.edu";

    public EventDrivenUpdatesConfig() {
        this(EventDrivenUpdatesConfig.getParameter(EventDrivenUpdatesConfig.EVENTS_ATTRIBUTE_PARAM, EventDrivenUpdatesConfig.EVENTS_ATTRIBUTE_DEFAULT_NAME),
             EventDrivenUpdatesConfig.getParameter(EventDrivenUpdatesConfig.EVENT_LISTENER_MAP_PARAM, EventDrivenUpdatesConfig.EVENT_LISTENER_MAP_DEFAULT_NAME));
    }

    public EventDrivenUpdatesConfig(String attributeName, final String mapName) {
        this(attributeName, new ViewMapProvider(mapName));
    }
    
    public EventDrivenUpdatesConfig(String attributeName, EventDrivenUpdatesMapProvider provider) {
        this(new EventDrivenUpdatesListener(attributeName, provider), new EventDrivenUpdatesMapResolver(provider), provider);
    }

    public EventDrivenUpdatesConfig(EventDrivenUpdatesListener listener, EventDrivenUpdatesMapResolver resolver, EventDrivenUpdatesMapProvider provider) {
        this.listener = listener;
        this.resolver = resolver;
        this.provider = provider;
    }

    public EventDrivenUpdatesListener getListener() {
        return listener;
    }

    public EventDrivenUpdatesMapResolver getResolver() {
        return resolver;
    }

    public EventDrivenUpdatesMapProvider getProvider() {
        return provider;
    }

    static String getParameter(String parameterName, String defaultValue) {
        return getParameter(FacesContext.getCurrentInstance().getExternalContext(), parameterName, defaultValue);
    }

    static String getParameter(ExternalContext externalContext, String parameterName, String defaultValue) {
        String value = externalContext.getInitParameter(parameterName);
        value = Strings.isNullOrEmpty(value) ? defaultValue : value;
        return value;
    }

    static String getParameter(ServletContext servletContext, String parameterName, String defaultValue) {
        String value = servletContext.getInitParameter(parameterName);
        value = Strings.isNullOrEmpty(value) ? defaultValue : value;
        return value;
    }

}
