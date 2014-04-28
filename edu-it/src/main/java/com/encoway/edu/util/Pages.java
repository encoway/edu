package com.encoway.edu.util;

import java.io.StringWriter;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.commons.io.IOUtils;

@ApplicationScoped
@ManagedBean(name = "pages")
public class Pages {
	
	public String getFileContents(String path) throws Exception {
		final StringWriter output = new StringWriter();
		IOUtils.copy(Pages.class.getResourceAsStream(path), output);
		return output.toString();
	}

}
