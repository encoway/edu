/**
 * Event Driven Updates (EDU) helps decoupling components of AJAX heavy JSF UIs.
 * The approach is known as <a href="http://en.wikipedia.org/w/index.php?title=Observer_pattern&oldid=605721456">ovserver pattern</a>.
 * EDU provides a convenient way to put it to use in a JSF component tree.
 * 
 * <p><strong>Motivation</strong></p>
 * 
 * Given there is a a panel showing the time of the last change of a configuration interface
 * <code>&lt;h:outputText id="<em>lastChangeText</em>" value="#{config.lastChangeDate}" /&gt;</code>.
 * Whenever the configuration is changed through some other component, e.g. <code>configSelect</code>
 * it has to know all the components requiring an update and trigger it:
 * 
 * <pre>
 * &lt;h:selectOneMenu id="configSelect"&gt;
 *     &lt;f:ajax event="change" <strong>render</strong>=":fully:qualified:id:of:<em>lastChangeText</em> :yet:another:component" /&gt;
 * &lt;/h:form&gt;
 * </pre>
 * 
 * Composed component IDs like <code>:fully:qualified:id:of:<em>lastChangeText</em></code> are fragile and
 * break e.g. if the parent components are restructured.
 * 
 * <p>EDU takes the responsibility from the trigger to know which component needs to be updated.</p>
 * 
 * <p><strong>Usage</strong></p>
 *
 * <pre>
 * &lt;h:form id="someForm"&gt;
 *     &lt;h:panelGroup id="somePanel" layout="block" <strong>updateOn</strong>="<em>something-changed</em>"&gt;
 *         &hellip;
 *     &lt;/h:panelGroup&gt;
 * &lt;/h:form&gt;
 * </pre>
 * 
 * Now any component triggering '<code><em>something-changed</em></code>' can obtain a list of component IDs
 * of components registered for '<code><em>something-changed</em></code>' from the <code>edu</code> map:
 * 
 * <pre>
 * &lt;h:commandLink&gt;
 *     &lt;f:ajax render="#{<strong>edu</strong>['<em>something-changed</em> something-else-changed']}" /&gt;
 * &lt;/h:commandLink&gt;
 * </pre>
 * 
 * <p><strong>Chaining Events</strong></p>
 * 
 * It is possible to register one component for multiple events:
 * 
 * <pre>
 * &lt;h:outputText id="someText" layout="block" <strong>updateOn</strong>="<em>something-changed some-global-change</em>" /&gt;
 * </pre>
 * 
 * Likewise a component can "trigger" multiple events:
 * 
 * <pre>
 * &lt;h:commandLink&gt;
 *     &lt;f:ajax render="#{<strong>edu</strong>['<em>something-changed something-else-changed</em>']}" /&gt;
 * &lt;/h:commandLink&gt;
 * </pre>
 * 
 * <p><strong>Default Value</strong></p>
 * 
 * In case there's no component registered for an event the EDU map returns a default value: <code>@none</code>.
 * This value can be overridden on a trigger basis:
 * 
 * <pre>
 * &lt;h:commandLink&gt;
 *     &lt;f:ajax render="#{<strong>edu</strong>['some-event-no-one-cares-about<em>|:default:component:id</em>']}" /&gt;
 * &lt;/h:commandLink&gt;
 * </pre>
 * 
 * This would result in the component with the ID <code><em>:default:component:id</em></code> to be updated.
 * The default value can be everything that would be valid in place of the EL expression (<code>#{edu['&hellip;']}</code>)
 * including special identifiers such as <code>@this</code>, <code>@all</code> etc.
 * 
 * <p><strong>Configuration</strong></p>
 * 
 * The following things may be configured through <code>context-param</code>s in a <code>web.xml</code>
 * 
 * <dl>
 * <dt>{@value com.encoway.edu.EventDrivenUpdatesListener#EVENTS_ATTRIBUTE_CONTEXT_PARAM}</dt>
 * <dd>Overrides the attribute used in components to register for events<br>
 * <dt>{@value com.encoway.edu.EventDrivenUpdatesListener.EventListenerMapELResolver#EVENT_LISTENER_MAP_CONTEXT_PARAM}</dt>
 * <dd>Overrides the name of the EDU map<br>
 * </dl>
 * 
 * @see com.encoway.edu.EventDrivenUpdatesListener
 * @see com.encoway.edu.EventDrivenUpdatesListener.EventListenerMap#get(Object)
 */
package com.encoway.edu;