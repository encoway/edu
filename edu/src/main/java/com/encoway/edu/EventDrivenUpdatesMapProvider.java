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

import javax.faces.context.FacesContext;

/**
 * Interface representing the key-value-pair of a property/variable name 
 * and the {@link EventDrivenUpdatesMap} associated with it. 
 */
public interface EventDrivenUpdatesMapProvider {
    
    /**
     * Returns the name through which the map can be resolved.
     * @return a valid property name
     */
    String getName();
    
    /**
     * Returns the event listener map for the specified {@code context}.
     * @param context a JSF context
     * @return a map, cannot be {@code null}
     */
    EventDrivenUpdatesMap getMap(FacesContext context);
    
}