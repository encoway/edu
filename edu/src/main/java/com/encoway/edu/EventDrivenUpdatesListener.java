/*
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
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

/**
 * {@link SystemEventListener} mapping event names to registered {@link UIComponent}'s IDs.
 */
public class EventDrivenUpdatesListener implements SystemEventListener, ComponentSystemEventListener {

    private static final String CONTEXT_PARAM_PREFIX = "com.encoway.edu";

    /**
     * Name of the `context-param` to override the listener attribute,
     * defaults to `updateOn`.
     */
    public static final String EVENTS_ATTRIBUTE_CONTEXT_PARAM = CONTEXT_PARAM_PREFIX + ".LISTENER_ATTRIBUTE";

    private static final char EVENT_LISTENER_DELIMITER = ' ';

    private static final String EVENTS_ATTRIBUTE_DEFAULT_NAME = "updateOn";

    private static final Splitter EVENT_ATTRIBUTE_SPLITTER = Splitter.on(Pattern.compile("[ ,]"));

    private static final Splitter DEFAULT_VALUE_KEY_SPLITTER = Splitter.on('|');

    private final EventListenerMapELResolver resolver;

    private final String eventsAttribute;

    public EventDrivenUpdatesListener() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final String eventsAttributeName = context.getExternalContext().getInitParameter(EVENTS_ATTRIBUTE_CONTEXT_PARAM);
        this.eventsAttribute = Strings.isNullOrEmpty(eventsAttributeName) ? EVENTS_ATTRIBUTE_DEFAULT_NAME : eventsAttributeName;
        this.resolver = new EventListenerMapELResolver(context);
        context.getApplication().addELResolver(resolver);
    }

    @Override
    public void processEvent(ComponentSystemEvent event) {
        final UIComponent listener = event.getComponent();
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final String listenerId = Components.getFullyQualifiedComponentId(facesContext, listener);
        final Map<String, Set<String>> eventListenerMap = resolver.getEventListenerMap(facesContext).getDelegate();
        final String eventNames = (String) listener.getAttributes().get(eventsAttribute);
        for (String eventName : EVENT_ATTRIBUTE_SPLITTER.split(eventNames)) {

            Set<String> listenerIds = eventListenerMap.get(eventName);
            if (listenerIds == null) {
                listenerIds = new HashSet<>();
                eventListenerMap.put(eventName, listenerIds);
            }

            listenerIds.add(listenerId);
        }
    }

    @Override
    public boolean isListenerForSource(Object source) {
        if (source instanceof UIComponent) {
            final Object value = ((UIComponent) source).getAttributes().get(EVENTS_ATTRIBUTE_DEFAULT_NAME);
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

    /**
     * A {@link Map} mapping event names to {@link UIComponent} IDs.
     */
    public static class EventListenerMap extends AbstractMap<String, String> implements Serializable {

        /**
         * @since 1.5.4
         */
        private static final long serialVersionUID = 2716308243361339413L;

        private static final String DEFAULT_VALUE = "@none";

        private final Map<String, Set<String>> delegate = new HashMap<>();

        EventListenerMap() {

        }

        Map<String, Set<String>> getDelegate() {
            return delegate;
        }

        /**
         * Returns a space separated list of component IDs of components associated with `events`.
         * If `events` is a {@link String} the following format is expected: `event-a[[,] event-b][|default-value]`
         * Where **`default-value`** is returned if no matching event is found and defaults to `@none`.
         * 
         * @return the list of component IDs associated with `events` or a default value (see above)
         * @param events either {@link String} (see above) or an {@link Iterable} of Strings
         * @throws IllegalArgumentException if `events` is not of the possible types
         * @see #get(String, String)
         * @see #get(Iterable, String)
         */
        @Override
        @SuppressWarnings("unchecked")
        public String get(Object events) throws IllegalArgumentException {
            if (events instanceof Iterable) {
                return get((Iterable<String>) events, DEFAULT_VALUE);
            } else if (events instanceof String) {
                final Iterator<String> iter = DEFAULT_VALUE_KEY_SPLITTER.split((String) events).iterator();
                return get(iter.next(), iter.hasNext() ? iter.next() : DEFAULT_VALUE);
            }
            throw new IllegalArgumentException("expected Iterable<String> or String but was " + events);
        }

        /**
         * Returns a space separated list of component IDs of components registered for at least one the `events`.
         * 
         * @param events a collection of events
         * @param defaultValue will be returned if no component is registered for one of the `events`
         * @return a space separated list of fully qualified component IDs or `defaultValue`
         */
        public String get(Iterable<String> events, String defaultValue) {
            if (events != null && !Iterables.isEmpty(events)) {
                final Set<String> ids = new HashSet<>();

                for (String eventName : events) {
                    Set<String> listenerIds = delegate.get(eventName);

                    if (listenerIds != null && !listenerIds.isEmpty()) {
                        ids.addAll(listenerIds);
                    }
                }

                if (!ids.isEmpty()) {
                    return Joiner.on(EVENT_LISTENER_DELIMITER).join(ids);
                }
            }
            return defaultValue;
        }

        /**
         * Returns a space separated list of component IDs of components registered for at least one the `events`.
         * 
         * @param events a comma/space separated list of event names
         * @param defaultValue will be returned if no component is registered for one of the `events`
         * @return a space separated list of fully qualified component IDs or `defaultValue`
         */
        public String get(String events, String defaultValue) {
            if (!Strings.isNullOrEmpty(events)) {
                return get(EVENT_ATTRIBUTE_SPLITTER.split(events), defaultValue);
            }

            return defaultValue;
        }

        @Override
        public Set<Map.Entry<String, String>> entrySet() {
            throw new UnsupportedOperationException("this map is get-only");
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("delegate", delegate).toString();
        }

    }

    /**
     * {@link ELResolver} for a {@link EventListenerMap}.
     */
    public static class EventListenerMapELResolver extends ELResolver {

        /**
         * Name of the `context-param` to override the EL variable name through which the EDU map is accessible,
         * defaults to `edu`.
         */
        public static final String EVENT_LISTENER_MAP_CONTEXT_PARAM = CONTEXT_PARAM_PREFIX + ".EVENT_LISTENER_MAP_NAME";

        private static final String EVENT_LISTENER_MAP_DEFAULT_NAME = "edu";

        private final String eventListenerMapName;

        EventListenerMapELResolver(FacesContext facesContext) {
            String initParameter = facesContext.getExternalContext().getInitParameter(EVENT_LISTENER_MAP_CONTEXT_PARAM);
            eventListenerMapName = Strings.isNullOrEmpty(initParameter) ? EVENT_LISTENER_MAP_DEFAULT_NAME : initParameter;
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

        public EventListenerMap getEventListenerMap(FacesContext facesContext) {
            Map<String, Object> viewMap = facesContext.getViewRoot().getViewMap();
            EventListenerMap eventListenerMap = (EventListenerMap) viewMap.get(eventListenerMapName);
            if (eventListenerMap == null) {
                eventListenerMap = new EventListenerMap();
                viewMap.put(eventListenerMapName, eventListenerMap);
            }
            return eventListenerMap;
        }

        private boolean isResolvable(Object base, Object property) {
            return base == null && eventListenerMapName.equals(property);
        }

    }

}
