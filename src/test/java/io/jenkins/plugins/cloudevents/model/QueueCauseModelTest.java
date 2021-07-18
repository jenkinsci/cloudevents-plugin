package io.jenkins.plugins.cloudevents.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueueCauseModelTest {

    QueueCauseModel queueCauseModel;

    @Before
    public void setQueueCauseModel() {
        queueCauseModel = new QueueCauseModel();
    }

    @Test
    public void testInitialQueueCauseModel() {
        assertEquals(queueCauseModel.getReasonForWaiting(), "");
        assertEquals(queueCauseModel.getType(), "");
    }

    @Test
    public void testReasonForWaiting() {
        String testValue = "test";
        queueCauseModel.setReasonForWaiting(testValue);
        assertEquals(testValue, queueCauseModel.getReasonForWaiting());
    }

    @Test
    public void testType() {
        String testValue = "test";
        queueCauseModel.setType(testValue);
        assertEquals(testValue, queueCauseModel.getType());
    }
}
