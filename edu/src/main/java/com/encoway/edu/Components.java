/**
 * Copyright (C) 2014 encoway GmbH
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.encoway.edu;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

/**
 * Component utilities.
 */
class Components {

    /**
     * Returns the fully qualified (absolute) ID of {@code component}.
     * 
     * @param context a {@link FacesContext}
     * @param component {@link UIComponent} to return the ID for
     * @return a fully qualified ID
     */
    public static String getFullyQualifiedComponentId(FacesContext context, UIComponent component) {
        return getFullyQualifiedComponentId(context, component, true);
    }

    /**
     * Returns the fully qualified (absolute) ID of {@code component}.
     * 
     * @param context a {@link FacesContext}
     * @param component {@link UIComponent} to return the ID for
     * @param absolute if {@code true} {@link UINamingContainer#getSeparatorChar(FacesContext)} is prepended (to indicate an absolute path)
     * @return a fully qualified ID
     */
    public static String getFullyQualifiedComponentId(FacesContext context, UIComponent component, boolean absolute) {
        if (component == null) {
            return null;
        }

        char separatorChar = UINamingContainer.getSeparatorChar(context);

        String fqid = component.getId();
        while (component.getParent() != null) {
            component = component.getParent();
            if (component instanceof NamingContainer) {
                StringBuilder builder = new StringBuilder(fqid.length() + 1 + component.getId().length());
                builder.append(component.getId()).append(separatorChar).append(fqid);
                fqid = builder.toString();
            }
        }
        return absolute ? separatorChar + fqid : fqid;
    }

    public static String getFullyQualifiedComponentId(UIComponent component) {
        return getFullyQualifiedComponentId(FacesContext.getCurrentInstance(), component);
    }

}
