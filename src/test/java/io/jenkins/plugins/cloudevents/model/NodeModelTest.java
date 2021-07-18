package io.jenkins.plugins.cloudevents.model;

import hudson.model.Computer;
import hudson.slaves.OfflineCause;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NodeModelTest {
    NodeModel nodeModel;

    @Before
    public void setNodeModel() {
        nodeModel = new NodeModel();
    }

    @Test
    public void testInitialNodeModel() {
        assertEquals(nodeModel.getNumExecutors(), 0);
        assertNull(nodeModel.getOfflineCause());
        assertNull(nodeModel.getNodeName());
        assertNull(nodeModel.getCachedHostName());
        assertEquals(nodeModel.getConnectTime(), 0);
        assertNull(nodeModel.getStatus());
        assertNull(nodeModel.getTerminatedBy());
    }

    @Test
    public void testNumExecutors() {
        int testValue = 0;
        nodeModel.setNumExecutors(testValue);
        assertEquals(testValue, nodeModel.getNumExecutors());
    }

    @Test
    public void testOfflineCause() {
        OfflineCause testValue = new OfflineCause() {
            @Override
            public long getTimestamp() {
                return super.getTimestamp();
            }
        };
        nodeModel.setOfflineCause(testValue);
        assertEquals(testValue, nodeModel.getOfflineCause());
    }

    @Test
    public void testNodeName() {
        String testValue = "test";
        nodeModel.setNodeName(testValue);
        assertEquals(testValue, nodeModel.getNodeName());
    }

    @Test
    public void testCachedHostName() {
        String testValue = "test";
        nodeModel.setCachedHostName(testValue);
        assertEquals(testValue, nodeModel.getCachedHostName());
    }

    @Test
    public void testTerminatedBy() {
        List<Computer.TerminationRequest> testValue = new ArrayList<>();
        nodeModel.setTerminatedBy(testValue);
        assertEquals(testValue, nodeModel.getTerminatedBy());
    }

    @Test
    public void testConnectTime() {
        long testValue = 0;
        nodeModel.setConnectTime(testValue);
        assertEquals(testValue, nodeModel.getConnectTime());
    }

    @Test
    public void testStatus() {
        String testValue = "test";
        nodeModel.setStatus(testValue);
        assertEquals(testValue, nodeModel.getStatus());
    }


}
