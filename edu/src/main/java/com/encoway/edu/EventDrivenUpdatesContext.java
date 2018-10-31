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
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import java.io.Serializable;

/**
 * EDU context of the a JSF application.
 */
@ViewScoped
public class EventDrivenUpdatesContext implements Serializable {

    @Inject
    private transient EventDrivenUpdatesMap map;

    /**
     * Updates all components registered for one or more of the {@code events}.
     * 
     * @param events events expression, see {@link com.encoway.edu package documentation}
     */
    public void update(String events) {
        update(FacesContext.getCurrentInstance(), events);
    }

    /**
     * Updates all components registered for one or more of the {@code events}.
     * 
     * @param facesContext the JSF context to be used for lookups
     * @param events events expression, see {@link com.encoway.edu package documentation}
     */
    public void update(FacesContext facesContext, String events) {
        Components.render(facesContext, map.getSeparate(events));
    }

}
