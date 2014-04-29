package com.encoway.edu.util;

import java.io.ByteArrayOutputStream;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.google.common.io.Resources;

@ApplicationScoped
@ManagedBean(name = "pages")
public class Pages {
	
	public String getFileContents(String path) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Resources.copy(Pages.class.getResource(path), outputStream);
		return new String(outputStream.toByteArray(), "UTF-8");
	}

}
