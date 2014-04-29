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
	 * @param context a {@link FacesContext}
	 * @param component {@link UIComponent} to return the ID for
	 * @return a fully qualified ID
	 */
	public static String getFullyQualifiedComponentId(FacesContext context, UIComponent component) {
		return getFullyQualifiedComponentId(context, component, true);
	}
	
	/**
	 * Returns the fully qualified (absolute) ID of {@code component}.
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
