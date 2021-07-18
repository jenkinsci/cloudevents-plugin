package io.jenkins.plugins.cloudevents.model;

import io.jenkins.plugins.cloudevents.Stage;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BuildModelTest {

    private BuildModel buildModel;

    @Before
    public void setBuildModel() {
        buildModel = new BuildModel();
    }

    @Test
    public void testInitialBuildModel() {
        assertNull(buildModel.getFullUrl());
        assertEquals(buildModel.getNumber(), 0);
        assertEquals(buildModel.getQueueId(), 0);
        assertEquals(buildModel.getTimestamp(),0);
        assertEquals(buildModel.getDuration(), 0);
        assertNull(buildModel.getPhase());
        assertNull(buildModel.getStatus());
        assertNull(buildModel.getUrl());
        assertNull(buildModel.getDisplayName());
        assertNull(buildModel.getParameters());
        assertNull(buildModel.getScmState());
    }

    @Test
    public void testFullUrl() {
        String testValue = "test";
        buildModel.setFullUrl(testValue);
        assertEquals(testValue, buildModel.getFullUrl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNumber_NotAllowed() {
        int testValue = 0;
        buildModel.setNumber(testValue);
        assertNull(buildModel.getNumber());
    }

    @Test
    public void testNumber() {
        int testValue = 1;
        buildModel.setNumber(testValue);
        assertEquals(testValue, buildModel.getNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueueId_NotAllowed() {
        int testValue = 0;
        buildModel.setQueueId(testValue);
        assertNull(buildModel.getQueueId());
    }

    @Test
    public void testQueueId() {
        int testValue = 1;
        buildModel.setQueueId(testValue);
        assertEquals(testValue, buildModel.getQueueId());
    }

    @Test
    public void testTimestamp() {
        long testValue = 0;
        buildModel.setTimestamp(testValue);
        assertEquals(testValue, buildModel.getTimestamp());
    }

    @Test
    public void testDuration() {
        long testValue = 0;
        buildModel.setDuration(testValue);
        assertEquals(testValue, buildModel.getDuration());
    }

    @Test
    public void testPhase() {
        Stage testValue = Stage.STARTED;
        buildModel.setPhase(testValue);
        assertEquals(testValue, buildModel.getPhase());
    }

    @Test
    public void testStatus() {
        String testValue = "test";
        buildModel.setStatus(testValue);
        assertEquals(testValue, buildModel.getStatus());
    }

    @Test
    public void testUrl() {
        String testValue = "test";
        buildModel.setUrl(testValue);
        assertEquals(testValue, buildModel.getUrl());
    }

    @Test
    public void testDisplayName() {
        String testValue = "test";
        buildModel.setDisplayName(testValue);
        assertEquals(testValue, buildModel.getDisplayName());
    }

    @Test
    public void testParameters() {
        Map<String, String> testValue = new HashMap<>();
        buildModel.setParameters(testValue);
        assertEquals(testValue, buildModel.getParameters());
    }

    @Test
    public void testScmState() {
        ScmState testValue = new ScmState();
        buildModel.setScmState(testValue);
        assertEquals(testValue, buildModel.getScmState());
    }
}
