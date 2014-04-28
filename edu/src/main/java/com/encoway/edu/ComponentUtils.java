package com.encoway.edu;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

class ComponentUtils {
	
	/**
	 * Gibt die voll qualifizierte (absolute) ID des �bergeben {@code component} zur�ck.
	 * @param context ein {@link FacesContext}
	 * @param component {@link UIComponent} dessen ID ermittelt werden soll
	 * @return eine voll qualifizierte ID
	 */
	public static String getFullyQualifiedComponentId(FacesContext context, UIComponent component) {
		return getFullyQualifiedComponentId(context, component, true);
	}
	
	/**
	 * Gibt die voll qualifizierte (absolute) ID des �bergeben {@code component} zur�ck.
	 * @param context ein {@link FacesContext}
	 * @param component {@link UIComponent} dessen ID ermittelt werden soll
	 * @param absolute wenn {@code true} wird {@link UINamingContainer#getSeparatorChar(FacesContext)} der ID vorangestellt
	 * @return eine voll qualifizierte ID
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
