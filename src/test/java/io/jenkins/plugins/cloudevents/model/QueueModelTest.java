package io.jenkins.plugins.cloudevents.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class QueueModelTest {

    QueueModel queueModel;

    @Before
    public void setQueueModel() {
        queueModel = new QueueModel();
    }

    @Test
    public void testInitialQueueModel() {
        assertEquals(queueModel.getCiUrl(), "");
        assertEquals(queueModel.getDisplayName(), "");
        assertEquals(queueModel.getEntryTime(), new Date());
        assertEquals(queueModel.getExitTime(), new Date());
        assertEquals(queueModel.getStartedBy(), "");
        assertEquals(queueModel.getJenkinsQueueId(), 0);
        assertEquals(queueModel.getStatus(), "");
        assertEquals(queueModel.getDuration(), 0);
        assertTrue(queueModel.getQueueCauses().isEmpty());
    }

    @Test
    public void testCIUrl() {
        String testValue = "test";
        queueModel.setCiUrl(testValue);
        assertEquals(testValue, queueModel.getCiUrl());
    }

    @Test
    public void testDisplayName() {
        String testValue = "test";
        queueModel.setDisplayName(testValue);
        assertEquals(testValue, queueModel.getDisplayName());
    }

    @Test
    public void testEntryTime() {
        Date testValue = new Date();
        queueModel.setEntryTime(testValue);
        assertEquals(testValue, queueModel.getEntryTime());
    }

    @Test
    public void testExitTime() {
        Date testValue = new Date();
        queueModel.setExitTime(testValue);
        assertEquals(testValue, queueModel.getExitTime());
    }

    @Test
    public void testStartedBy() {
        String testValue = "test";
        queueModel.setStartedBy(testValue);
        assertEquals(testValue, queueModel.getStartedBy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJenkinsQueueId_NotAllowed() {
        int testValue = 0;
        queueModel.setJenkinsQueueId(testValue);
        assertNull(queueModel.getJenkinsQueueId());
    }

    @Test
    public void testJenkinsQueueId() {
        int testValue = 1;
        queueModel.setJenkinsQueueId(testValue);
        assertEquals(testValue, queueModel.getJenkinsQueueId());
    }

    @Test
    public void testStatus() {
        String testValue = "test";
        queueModel.setStatus(testValue);
        assertEquals(testValue, queueModel.getStatus());
    }

    @Test
    public void testDuration() {
        long testValue = 0;
        queueModel.setDuration(testValue);
        assertEquals(testValue, queueModel.getDuration());
    }

    @Test
    public void testQueueCauses() {
        List<QueueCauseModel> testValue = new ArrayList<>();
        queueModel.setQueueCauses(testValue);
        assertEquals(testValue, queueModel.getQueueCauses());
    }
}
