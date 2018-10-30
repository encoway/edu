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

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.Assert.assertThat;

/**
 * Unit test for {@link EventDrivenUpdatesMap}.
 */
public class EventDrivenUpdatesMapTest {

    private static final Map<String, Set<String>> DEFAULT_DELEGATE = new HashMap<String, Set<String>>() {

        private static final long serialVersionUID = 1L;

        {
            put("event-a", asSet(":comp_1"));
            put("event-b", asSet(":comp_1", ":comp_2:comp_2.1"));
        }
    };

    static Set<String> asSet(String... strings) {
        final HashSet<String> set = new HashSet<>();
        set.addAll(Arrays.asList(strings));
        return set;
    }

    @Test
    public void add() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        map.add("event-d event-e", ":comp_1.1", ":comp_1.2");
        assertThat("unexpected ID", map.getSeparate("event-d"), containsInAnyOrder(":comp_1.1", ":comp_1.2"));
        assertThat("unexpected ID", map.getSeparate("event-e"), containsInAnyOrder(":comp_1.1", ":comp_1.2"));
        assertThat("unexpected ID", map.getSeparate("event-d event-e"), containsInAnyOrder(":comp_1.1", ":comp_1.2"));
        assertThat("unexpected ID", map.getSeparate("event-d,event-e"), containsInAnyOrder(":comp_1.1", ":comp_1.2"));
        assertThat("unexpected ID", map.getSeparate("event-d, event-e"), containsInAnyOrder(":comp_1.1", ":comp_1.2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFailsForObjectKey() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        map.get(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFailsForNullKey() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        map.get(null);
    }

    @Test
    public void getSupportsIterableKey() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        final String ids = map.get(DEFAULT_DELEGATE.keySet());
        for (String id : DEFAULT_DELEGATE.get("event-b")) {
            assertThat("missing ID", ids, containsString(id));
        }
    }

    @Test
    public void getSupportsStringKey() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        final String ids = map.get("event-a");
        assertThat("unexpected ID", ids, is(DEFAULT_DELEGATE.get("event-a").iterator().next()));
    }

    @Test
    public void getSupportsStringKeyWithMultipleEvents() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        final String ids = map.get("event-a event-b,event-c");
        for (String id : DEFAULT_DELEGATE.get("event-b")) {
            assertThat("missing ID", ids, containsString(id));
        }
    }

    @Test
    public void getSupportsStringKeyWithDefaultValue() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        final String ids = map.get("event-c|test");
        assertThat("expected default value", ids, is("test"));
    }

    @Test
    public void getSeparateSupportsStringKey() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        final Set<String> actualIds = map.getSeparate("event-a");
        final Set<String> expectedIds = DEFAULT_DELEGATE.get("event-a");
        assertThat("unexpected ID", actualIds, contains(asArray(expectedIds, String.class)));
    }

    @Test
    public void getSeparateSupportsStringKeyWithMultipleEvents() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        final Set<String> actualIds = map.getSeparate("event-a event-b, event-c");
        final Set<String> expectedIds = DEFAULT_DELEGATE.get("event-b");
        assertThat(actualIds, containsInAnyOrder(asArray(expectedIds, String.class)));
    }

    @Test
    public void getSeparateSupportsStringKeyWithDefaultValue() {
        final EventDrivenUpdatesMap map = new EventDrivenUpdatesMap(DEFAULT_DELEGATE);
        final Set<String> ids = map.getSeparate("event-c|test");
        assertThat("expected default value", ids, contains("test"));
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] asArray(Collection<T> list, Class<T> type) {
        T[] a = (T[]) java.lang.reflect.Array.newInstance(type, list.size());
        return list.toArray(a);
    }

}
