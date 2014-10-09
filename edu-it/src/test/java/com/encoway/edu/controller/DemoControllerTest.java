package com.encoway.edu.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import com.encoway.edu.EventDrivenUpdatesContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.event.AjaxBehaviorEvent;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link DemoController}.
 */
public class DemoControllerTest {

    private static final int UPDATED_INT_VALUE = 1;
    private static final String UPDATED_STRING_VALUE = " (upd. " + new SimpleDateFormat("hh:mm").format(new Date()) + ")";

    private EventDrivenUpdatesContext context;
    private DemoController controller;

    @Before
    public void setUp() {
        controller = new DemoController();
        context = mock(EventDrivenUpdatesContext.class);
        controller.setEduContext(context);
    }

    @Test
    public void testUpdateStringModel() throws Exception {
        final String initialStringValue = controller.getStringModel().getValue();
        final int initialIntValue = controller.getIntModel().getValue();

        controller.updateStringModel(mock(AjaxBehaviorEvent.class));
        assertThat(controller.getStringModel().getValue(), is(initialStringValue + UPDATED_STRING_VALUE));
        assertThat(controller.getIntModel().getValue(), is(initialIntValue));

        controller.reset();
        assertThat(controller.getStringModel().getValue(), is(initialStringValue));
    }

    @Test
    public void testUpdateIntModel() throws Exception {
        final String initialStringValue = controller.getStringModel().getValue();
        final int initialIntValue = controller.getIntModel().getValue();

        controller.updateIntModel(mock(AjaxBehaviorEvent.class));
        assertThat(controller.getStringModel().getValue(), is(initialStringValue));
        assertThat(controller.getIntModel().getValue(), is(initialIntValue + UPDATED_INT_VALUE));

        controller.reset();
        assertThat(controller.getIntModel().getValue(), is(initialIntValue));
    }

}
