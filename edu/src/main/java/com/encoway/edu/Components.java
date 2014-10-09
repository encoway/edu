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

import java.util.Collection;
import java.util.Set;

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
    static String getFullyQualifiedComponentId(FacesContext context, UIComponent component) {
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
    static String getFullyQualifiedComponentId(FacesContext context, UIComponent component, boolean absolute) {
        if (component == null) {
            return null;
        }

        char separatorChar = getSeparatorChar(context);

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

    static char getSeparatorChar(FacesContext context) {
        return UINamingContainer.getSeparatorChar(context);
    }

    /**
     * Returns the fully qualified (absolute) ID of {@code component}.
     * @param component {@link UIComponent} to return the ID for
     * @return a fully qualified ID
     */
    static String getFullyQualifiedComponentId(UIComponent component) {
        return getFullyQualifiedComponentId(FacesContext.getCurrentInstance(), component);
    }

    /**
     * Adds the {@code ids} to the collection of IDs to be rendered.
     * Any leading {@link UINamingContainer#getSeparatorChar(FacesContext) separator char} will be stripped.
     * @param context a {@link FacesContext}
     * @param ids collection of fully qualified IDs
     */
    static void render(FacesContext context, final Set<String> ids) {
        if (ids.isEmpty()) {
            return;
        }
        final Collection<String> renderIds = context.getPartialViewContext().getRenderIds();
        final char separatorChar = getSeparatorChar(context);
        for (String id : ids) {
            renderIds.add(id.substring(id.indexOf(separatorChar) + 1));
        }
    }

}
