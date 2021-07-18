package io.jenkins.plugins.cloudevents.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ScmStateTest {
    ScmState scmState;

    @Before
    public void setScmState(){
        scmState = new ScmState();
    }

    @Test
    public void testInitialScmState(){
        assertNull(scmState.getUrl());
        assertNull(scmState.getBranch());
        assertNull(scmState.getCommit());
    }

    @Test
    public void testUrl(){
        String testValue = "test";
        scmState.setUrl(testValue);
        assertEquals(testValue, scmState.getUrl());
    }

    @Test
    public void testBranch(){
        String testValue = "test";
        scmState.setBranch(testValue);
        assertEquals(testValue, scmState.getBranch());
    }

    @Test
    public void testCommit(){
        String testValue = "test";
        scmState.setCommit(testValue);
        assertEquals(testValue, scmState.getCommit());
    }
}
