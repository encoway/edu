package com.encoway.edu.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Spring style initialization.
 */
public class DemoInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (servletContext.getAttribute("contextClass") != null) {
            return;
        }

        try (AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext()) {
            context.setServletContext(servletContext);
            context.register(DemoConfig.class);
            context.refresh();

            servletContext.addListener(new ContextLoaderListener(context));
        }
    }

    @Configuration
    public static class DemoConfig {

        @Bean
        @Lazy
        public EventDrivenUpdatesContextFactoryBean eduContextWrapper() {
            return new EventDrivenUpdatesContextFactoryBean();
        }

    }

}
