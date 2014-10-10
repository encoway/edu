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

/**
 * {@link ELResolver} for a {@link EventDrivenUpdatesMap}.
 */
public class EventDrivenUpdatesMapResolver extends ELResolver {
    
    private final EventDrivenUpdatesMapProvider mapProvider;
    
    public EventDrivenUpdatesMapResolver(EventDrivenUpdatesMapProvider mapProvider) {
        this.mapProvider = mapProvider;
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        if (isResolvable(base, property)) {
            context.setPropertyResolved(true);
            return mapProvider.getMap(null);
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
        return EventDrivenUpdatesMap.class;
    }

    private boolean isResolvable(Object base, Object property) {
        return base == null && mapProvider.getName().equals(property);
    }

}