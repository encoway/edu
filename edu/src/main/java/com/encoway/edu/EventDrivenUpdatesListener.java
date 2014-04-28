package com.encoway.edu;

import java.beans.FeatureDescriptor;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

/**
 * {@link SystemEventListener} der nach dem Hinzuf�gen eines {@link UIComponent} pr�ft, ob
 * dieses ein Attribut {@value #EVENTS_ATTRIBUTE_DEFAULT_NAME} hat. Ist dessen Wert, eine Komma/Leerzeichen separierte
 * Liste von Event-Namen, nicht leer, so wird in der Map names {@value EventMapELResolver#EVENT_LISTERNER_MAP_NAME} 
 * jeder Event-Name mit der ID des {@link UIComponent} verkn�pft.
 * 
 * <h2>Verwendung</h2>
 *
 * <pre title="Komponente 'someForm' registrieren">
 * &lt;h:form id="someForm" &gt;
 *     &lt;h:panelGroup id="somePanel" layout="block" &gt;
 *         &lt;f:attribute name={@value #EVENTS_ATTRIBUTE_DEFAULT_NAME} value="<strong>something-changed</strong>" /&gt;
 *     &lt;/h:panelGroup&gt;
 * &lt;/h:form&gt;
 * </pre>
 * 
 * In der Map {@value EventMapELResolver#EVENT_LISTERNER_MAP_NAME} steht nun die voll qualifizierte, d.h. absolute ID
 * von <code>somePanel</code>, <code>:someForm:somePanel</code>. 
 * 
 * <pre title="Registrierte Komponenten abrufen">
 * &lt;h:commandLink&gt;
 *     &lt;f:ajax render="#{customEventMap.get('<strong>something-changed</strong>')}" /&gt;
 * &lt;/h:commandLink&gt;
 * </pre>
 * 
 * @see EventDrivenUpdatesListener.EventMapELResolver.EventListenerMap#get(String, String)
 */
public class EventDrivenUpdatesListener implements SystemEventListener, ComponentSystemEventListener {
	
	private static final String INIT_PARAM_PREFIX = EventDrivenUpdatesListener.class.getPackage().getName();
	
	private static final String EVENTS_ATTRIBUTE_INIT_PARAM = INIT_PARAM_PREFIX + ".LISTENER_ATTRIBUTE";

	private static final char EVENT_LISTENER_DELIMITER = ' ';
	
	private static final String EVENTS_ATTRIBUTE_DEFAULT_NAME = "updateOn";
	
	private static final Splitter EVENT_ATTRIBUTE_SPLITTER = Splitter.on(Pattern.compile("[ ,]"));
	
	private static final Splitter DEFAULT_VALUE_KEY_SPLITTER = Splitter.on('|');
	
	private final EventMapELResolver resolver;
	
	private final String eventsAttribute;
	
	public EventDrivenUpdatesListener() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final String eventsAttributeName = context.getExternalContext().getInitParameter(EVENTS_ATTRIBUTE_INIT_PARAM);
		this.eventsAttribute = Strings.isNullOrEmpty(eventsAttributeName) ? EVENTS_ATTRIBUTE_DEFAULT_NAME : eventsAttributeName;
		Application application = context.getApplication();
		this.resolver = new EventMapELResolver(context);
		application.addELResolver(resolver);
	}
	
	@Override
	public void processEvent(ComponentSystemEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map<String, String> eventListenerMap = resolver.getEventListenerMap(facesContext).getDelegate();
		// relevante Informationen der zu registrierende Komponente: ID + Event Namen    
		UIComponent listener = event.getComponent();
		String eventNames = (String) listener.getAttributes().get(eventsAttribute);
		String listenerId = ComponentUtils.getFullyQualifiedComponentId(facesContext, listener);
		for (String eventName : EVENT_ATTRIBUTE_SPLITTER.split(eventNames)) {
			// bereits registrierte Komponenten-IDs auslesen
			String listenerIds = eventListenerMap.containsKey(eventName) ? eventListenerMap.get(eventName) : "";
			StringBuilder builder = new StringBuilder(listenerIds.length() + 1 + listenerId.length());
			if (listenerIds.length() > 0) {
				builder.append(listenerIds).append(EVENT_LISTENER_DELIMITER); 
			}
			builder.append(listenerId);
			eventListenerMap.put(eventName, builder.toString());
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
	 * {@link ELResolver} f�r eine {@link Map} die Event-Namen auf {@link UIComponent}-IDs abbildet.
	 * 
	 * <h2>Konfiguration</h2>
	 * Der Name der Variable �ber die Event-Map bereitgestellt wird l�sst sich durch einen Parameter
	 * im {@link javax.servlet.ServletContext} (web.xml) namens {@value #EVENT_LISTENER_MAP_PARAMETER} konfigurieren.
	 */
	public static final class EventMapELResolver extends ELResolver {
		
		public static final String EVENT_LISTENER_MAP_PARAMETER = INIT_PARAM_PREFIX + ".EVENT_LISTENER_MAP_NAME";
	
		private static final String EVENT_LISTENER_MAP_DEFAULT_NAME = "edu";
		
		private final String eventListenerMapName;
		
		private final boolean readOnly = true; 
	
		public EventMapELResolver(FacesContext facesContext) {
			String initParameter = facesContext.getExternalContext().getInitParameter(EVENT_LISTENER_MAP_PARAMETER);
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
			return readOnly;
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
		
		/**
		 * Schnittstelle f�r das Auslesen von Listenern f�r ein Event. 
		 */
		public static class EventListenerMap extends AbstractMap<String, String> {
			
			private final Map<String, String> delegate = new HashMap<>();
			
			Map<String, String> getDelegate() {
				return delegate;
			}
			
			public String get(Object key) {
				final Iterator<String> iter = DEFAULT_VALUE_KEY_SPLITTER.split((String) key).iterator();
				return get(iter.next(), iter.hasNext() ? iter.next() : "@none");
			}
			
			/**
			 * Gibt die IDs der Listener als String zur�ck.
			 * @param events Liste von Events, Trennzeichen siehe {@link EventDrivenUpdatesListener#EVENT_ATTRIBUTE_SPLITTER} 
			 * @param defaultValue Standardwert der ausgegeben werden soll, sofern keine Listener registriert sind, standardm��ig {@code @none}
			 * @return Liste von Listener IDs
			 */
			private String get(String events, String defaultValue) {
				if (!Strings.isNullOrEmpty(events)) {
					StringBuilder builder = new StringBuilder();
					for (String eventName : EVENT_ATTRIBUTE_SPLITTER.split(events)) {
						String listeners = delegate.get(eventName);
						
						if (!Strings.isNullOrEmpty(listeners)) {							
							if (builder.length() > 0) {
								builder.append(EVENT_LISTENER_DELIMITER);
							}
							builder.append(listeners);
						}
					}
					
					if (builder.length() > 0) {
						return builder.toString();
					}
				}
				return defaultValue;
			}

			@Override
			public Set<Map.Entry<String, String>> entrySet() {
				return delegate.entrySet();
			}
			
		}
		
	}

}
