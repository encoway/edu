Event Driven Updates
====================

Event Driven Updates (EDU) helps decoupling components of AJAX heavy JSF UIs.

EDU does so by allowing components to request being re-rendered/updated based on an **event**.
An event in terms of EDU is just a name through which it can be referenced by a triggering component, for example, *configuration-changed*.

## Motivation

Given there is a panel showing the time of the last change of a configuration interface
`<h:outputText id="lastChangeText" value="#{config.lastChangeDate}" />`.
Whenever the configuration is changed through some other component, for instance, `configSelect`
it has to know all the components requiring an update and trigger it:

```xhtml
<h:selectOneMenu id="configSelect">
    <f:ajax event="change" render=":fully:qualified:id:of:lastChangeText :yet:another:component" />
</h:form>
```

Composed component IDs like `:fully:qualified:id:of:lastChangeText` are fragile and
might break if the parent components are restructured.

EDU decouples trigger (observable) and receiver (observer) using events.

## Usage

```xhtml
<h:form id="someForm">
    <h:panelGroup id="somePanel" layout="block" updateOn="something-changed">
        ...
    </h:panelGroup>
</h:form>
```

Now any component triggering `something-changed` can obtain a list of component IDs
of components registered for `something-changed` from the `edu` map:

```xhtml
<h:commandLink>
    <f:ajax render="#{edu['something-changed']}" />
</h:commandLink>
```

### Chaining Events

It is possible to register one component for multiple events:

```xhtml
<h:outputText id="someText" layout="block" updateOn="something-changed some-global-change" />
```

Likewise a component can "trigger" multiple events:

```xhtml
<h:commandLink>
    <f:ajax render="#{edu['something-changed something-else-changed']}" />
</h:commandLink>
```

### Default Value

In case there's no component registered for an event the EDU map returns a default value: `@none`.
This value can be overridden on a per-trigger basis:

```xhtml
<h:commandLink>
    <f:ajax render="#{edu['some-event-no-one-cares-about|:default:component:id']}" />
</h:commandLink>
```

This would result in the component with the ID `:default:component:id` to be updated.
The default value can be everything that would be valid in place of the EL expression (`#{edu['...']}`)
including special identifiers such as `@this`, `@all` etc.

#### Programmatic

It is possible to trigger component updates from java code. This is done through the com.encoway.edu.EventDrivenUpdatesContext. An instance of which may be obtained via injection or a static helper method `EventDrivenUpdatesContext.getCurrentInstance()`. The `EventDrivenUpdatesContext` is registered under the name `eduContext` by default. This may be overridden, however (see section Configuration). 

The following snippet demonstrates the programmatic usage of EDU: 

```java
@SessionScoped
@Named("controller")
class Controller {

    @Inject
    private EventDrivenUpdatesContext eduContext;

    public void update() {
        eduContext.update("something-changed something-else-changed");
    }
    
}
```

```xhtml
<h:commandLink value="Update" action="#{controller.update}">
    <f:ajax />
</h:commandLink>
```

## Configuration

In general EDU just works out of the box and does not need any configuration.
However, if either the attribute `updateOn` or any of the implicit bean names, e.g. `edu` is already in use,
these defaults may be overridden. To change the attribute `updateOn` inject the `EventDrivenUpdatesListener`
and call `setAttribute` or `setMapVariableName`, respectively. 

## License

EDU is licensed under Apache 2.0.