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

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.Application;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.inject.Inject;

/**
 * EDU configuration.
 */
@ApplicationScoped
public class EventDrivenUpdates implements SystemEventListener {

    @Inject
    private EventDrivenUpdatesListener listener;

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PostConstructApplicationEvent) {
            Application application = ((PostConstructApplicationEvent) event).getApplication();
            application.subscribeToEvent(PostAddToViewEvent.class, listener);
        }
    }

    @Override
    public boolean isListenerForSource(Object source) {
        return source instanceof Application;
    }

}
