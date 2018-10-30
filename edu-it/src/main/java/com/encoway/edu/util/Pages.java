package com.encoway.edu.util;

import com.google.common.io.Resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.ByteArrayOutputStream;

/**
 * Utility class providing page related helper methods.
 */
@Named
@ApplicationScoped
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
