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

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;

/**
 * A {@link Map} mapping event names to {@link UIComponent} IDs.
 */
public class EventDrivenUpdatesMap extends AbstractMap<String, String> implements Serializable {

    /**
     * @since 1.5.4
     */
    private static final long serialVersionUID = 2716308243361339413L;

    private static final String DEFAULT_VALUE = "@none";

    private static final Pattern EVENT_ATTRIBUTE_SPLITTER = Pattern.compile("[ ,]");
    
    private static final char DEFAULT_VALUE_KEY_SPLITTER = '|';
    
    private static final char EVENT_LISTENER_DELIMITER = ' ';
    
    private final Map<String, Set<String>> delegate;

    /**
     * Initializes an {@link EventDrivenUpdatesMap} with an empty map delegate.
     */
    EventDrivenUpdatesMap() {
        this(new HashMap<String, Set<String>>());
    }

    /**
     * Initializes an {@link EventDrivenUpdatesMap} with the specified {@code delegate}.
     * @param delegate the map used as delegate
     */
    EventDrivenUpdatesMap(Map<String, Set<String>> delegate) {
        this.delegate = delegate;
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
            final String[] iter = parseEventsAndDefault((String) events);
            return get(iter[0], iter[1]);
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
        final Iterator<String> iter = getSeparate(events, defaultValue).iterator();
        StringBuilder builder = new StringBuilder();
        if (iter.hasNext()) {
            builder.append(iter.next());
            while (iter.hasNext()) {
                builder.append(EVENT_LISTENER_DELIMITER);
                builder.append(iter.next());
            }
        }
        return builder.toString();
    }
    
    /**
     * Returns a space separated list of component IDs of components registered for at least one the `events`.
     * 
     * @param events a comma/space separated list of event names
     * @param defaultValue will be returned if no component is registered for one of the `events`
     * @return a space separated list of fully qualified component IDs or `defaultValue`
     */
    public String get(String events, String defaultValue) {
        return get(parseEvents(events), defaultValue);
    }
    
    /**
     * Maps the the specified {@code ids} as listeners for {@code events}.
     * @param events the events triggering an update of the specified {@code ids}
     * @param ids the ids to be updated
     */
    public void add(String events, String...ids) {
        for (String event : EventDrivenUpdatesMap.parseEvents(events)) {
            Set<String> listenerIds = delegate.get(event);
            if (listenerIds == null) {
                listenerIds = new HashSet<>();
                delegate.put(event, listenerIds);
            }

            listenerIds.addAll(Arrays.asList(ids));
        }        
    }

    @Override
    public Set<Map.Entry<String, String>> entrySet() {
        throw new UnsupportedOperationException("this map is get-only");
    }

    @Override
    public String toString() {
        return EventDrivenUpdatesMap.class.getSimpleName() + "{delegate: " + delegate + "}";
    }

    Set<String> getSeparate(String events) {
        final String[] iter = parseEventsAndDefault(events);
        return getSeparate(iter[0], iter[1]);
    }

    Set<String> getSeparate(String events, String defaultValue) {
        return getSeparate(parseEvents(events), defaultValue);
    }

    Set<String> getSeparate(Iterable<String> events, String defaultValue) {
        final Set<String> ids = new HashSet<>();
    
        for (String event : events) {
            Set<String> listeners = delegate.get(event);
    
            if (listeners != null && !listeners.isEmpty()) {
                ids.addAll(listeners);
            }
        }
        
        if (ids.isEmpty()) {
            ids.add(defaultValue);
        }
        
        return ids;
    }
    
    static String[] parseEventsAndDefault(String events) {
        final int offset = events.indexOf(DEFAULT_VALUE_KEY_SPLITTER);
        if (offset >= 0) {
            return new String[] { 
                events.substring(0, offset), 
                events.substring(offset + 1) };
        }
        return new String[] { events, DEFAULT_VALUE };
    }

    static List<String> parseEvents(String events) {
        return Arrays.asList(EVENT_ATTRIBUTE_SPLITTER.split(events));
    }
    
}