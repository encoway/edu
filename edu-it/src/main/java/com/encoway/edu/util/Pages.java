package com.encoway.edu.util;

import java.io.ByteArrayOutputStream;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import com.google.common.io.Resources;

/**
 * Utility class providing page related helper methods.
 */
@ApplicationScoped
@ManagedBean(name = "pages")
public class Pages {

    /**
     * Returns the contents of the a file specified by {@code path}.
     * @param path path to a file
     * @return the contents of the specified file
     * @throws Exception if anything goes wrong
     */
    public String getFileContents(String path) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Resources.copy(Pages.class.getResource(path), outputStream);
        return new String(outputStream.toByteArray(), "UTF-8");
    }

}
