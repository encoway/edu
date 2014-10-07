package com.encoway.edu.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.event.AjaxBehaviorEvent;

import org.junit.Before;
import org.junit.Test;

public class DemoControllerTest {

    private static final int UPDATED_INT_VALUE = 1;
    private static final String UPDATED_STRING_VALUE = " (upd. " + new SimpleDateFormat("hh:mm").format(new Date()) + ")";
    private DemoController controller;
    private AjaxBehaviorEvent event;

    @Before
    public void setUp() {
        controller = new DemoController();
        event = mock(AjaxBehaviorEvent.class);
    }

    @Test
    public void testUpdateStringModel() throws Exception {
        final String initialStringValue = controller.getStringModel().getValue();
        final int initialIntValue = controller.getIntModel().getValue();

        controller.updateStringModel(event);
        assertThat(controller.getStringModel().getValue(), is(initialStringValue + UPDATED_STRING_VALUE));
        assertThat(controller.getIntModel().getValue(), is(initialIntValue));

        controller.reset(event);
        assertThat(controller.getStringModel().getValue(), is(initialStringValue));
    }

    @Test
    public void testUpdateIntModel() throws Exception {
        final String initialStringValue = controller.getStringModel().getValue();
        final int initialIntValue = controller.getIntModel().getValue();

        controller.updateIntModel(event);
        assertThat(controller.getStringModel().getValue(), is(initialStringValue));
        assertThat(controller.getIntModel().getValue(), is(initialIntValue + UPDATED_INT_VALUE));

        controller.reset(event);
        assertThat(controller.getIntModel().getValue(), is(initialIntValue));
    }

}
