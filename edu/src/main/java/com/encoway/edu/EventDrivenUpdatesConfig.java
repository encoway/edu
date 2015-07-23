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

/**
 * EDU configuration.
 */
public class EventDrivenUpdatesConfig {

    /**
     * Default value.
     */
    public static final String EVENTS_ATTRIBUTE_DEFAULT_NAME = "updateOn";
    /**
     * Name of the `context-param` to override the listener attribute,
     * defaults to {@value #EVENTS_ATTRIBUTE_DEFAULT_NAME}.
     */
    public static final String EVENTS_ATTRIBUTE_PARAM = EventDrivenUpdatesConfig.PARAM_PREFIX + ".ATTRIBUTE_NAME";
    /**
     * Name of the `context-param` to override the EL variable name through which the EDU map is accessible,
     * defaults to {@value #EVENT_LISTENER_MAP_DEFAULT_NAME}.
     */
    public static final String EVENT_LISTENER_MAP_PARAM = EventDrivenUpdatesConfig.PARAM_PREFIX + ".MAP_NAME";
    /**
     * Default value.
     */
    public static final String EVENT_LISTENER_MAP_DEFAULT_NAME = "edu";

    /**
     * Prefix used for `context-param` definitions.
     */
    static final String PARAM_PREFIX = "com.encoway.edu";

    private final EventDrivenUpdatesListener listener;
    private final EventDrivenUpdatesMapResolver resolver;
    private final EventDrivenUpdatesMapProvider provider;

    /**
     * Initializes an {@link EventDrivenUpdatesConfig} based on the external context.
     * 
     * @see EventDrivenUpdatesConfig#EventDrivenUpdatesConfig(ExternalContext)
     */
    public EventDrivenUpdatesConfig() {
        this(FacesContext.getCurrentInstance().getExternalContext());
    }

    /**
     * Initializes an {@link EventDrivenUpdatesConfig} based on {@code externalContext}.
     * 
     * @param externalContext the external context
     */
    public EventDrivenUpdatesConfig(ExternalContext externalContext) {
        this(InitParamProvider.create(externalContext));
    }
    
    /**
     * Initializes an {@link EventDrivenUpdatesConfig} based on {@code servletContext}.
     * 
     * @param servletContext the servlet context
     */
    public EventDrivenUpdatesConfig(ServletContext servletContext) {
        this(InitParamProvider.create(servletContext));
    }
    
    EventDrivenUpdatesConfig(InitParamProvider provider) {
        this(
                provider.get(EVENTS_ATTRIBUTE_PARAM, EVENTS_ATTRIBUTE_DEFAULT_NAME),
                provider.get(EVENT_LISTENER_MAP_PARAM, EVENT_LISTENER_MAP_DEFAULT_NAME));
    }

    /**
     * Initializes an {@link EventDrivenUpdatesConfig} using the specified {@code attributeName} an {@code mapName}.
     * @param attributeName name of the JSF component attribute holding the events a component 'listens' for 
     * @param mapName name of the {@link EventDrivenUpdatesMap} exposed by {@link EventDrivenUpdatesMapResolver}
     */
    public EventDrivenUpdatesConfig(String attributeName, final String mapName) {
        this(attributeName, new ViewMapProvider(mapName));
    }

    /**
     * Initializes an {@link EventDrivenUpdatesConfig} using the specified {@code attributeName} an {@code provider}.
     * @param attributeName name of the JSF component attribute holding the events a component 'listens' for
     * @param provider the provider exposing the current {@link EventDrivenUpdatesMap}
     */
    public EventDrivenUpdatesConfig(String attributeName, EventDrivenUpdatesMapProvider provider) {
        this(new EventDrivenUpdatesListener(attributeName, provider), new EventDrivenUpdatesMapResolver(provider), provider);
    }

    /**
     * Initializes an {@link EventDrivenUpdatesConfig} using the specified {@code listener} an {@code resolver} and {@code provider}.
     * @param listener JSF component listener responsible for the event-listener-mapping
     * @param resolver EL resolver for the {@link EventDrivenUpdatesMap}
     * @param provider the provider exposing the current {@link EventDrivenUpdatesMap} 
     */
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
     * Interface for obtaining init parameters.
     */
    abstract static class InitParamProvider {
        
        abstract String get(String key, String defaultValue);
        
        static InitParamProvider create(final ServletContext context) {
            return new InitParamProvider() {

                @Override
                String get(String parameterName, String defaultValue) {
                    String value = context.getInitParameter(parameterName);
                    return value == null || value.isEmpty() ? defaultValue : value;
                }
                
            };
        }
        
        static InitParamProvider create(final ExternalContext context) {
            return new InitParamProvider() {

                @Override
                String get(String parameterName, String defaultValue) {
                    String value = context.getInitParameter(parameterName);
                    return value == null || value.isEmpty() ? defaultValue : value;
                }
                
            };
        }
        
    }

}
