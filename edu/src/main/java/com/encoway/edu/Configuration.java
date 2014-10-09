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

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.google.common.base.Strings;

/**
 * Utility class for reading configuration information.
 */
class Configuration {
    
    /**
     * Prefix used for `context-param` definitions.
     */
    static final String PARAM_PREFIX = "com.encoway.edu";
    
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
