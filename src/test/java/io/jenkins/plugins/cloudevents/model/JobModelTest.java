package io.jenkins.plugins.cloudevents.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JobModelTest {

    private JobModel jobModel;

    @Before
    public void setJobModel(){
        jobModel = new JobModel();
    }

    @Test
    public void testInitialJobModel() {
        assertNull(jobModel.getName());
        assertNull(jobModel.getDisplayName());
        assertNull(jobModel.getUrl());
        assertNull(jobModel.getBuild());
        assertNull(jobModel.getUserId());
        assertNull(jobModel.getUserName());
        // assertNull(jobModel.getCreatedDate());
        // assertNull(jobModel.getUpdatedDate());
        assertNull(jobModel.getStatus());
        assertNull(jobModel.getConfigFile());
        assertNull(jobModel.getStage());
    }

    @Test
    public void testName(){
        String testValue = "test";
        jobModel.setName(testValue);
        assertEquals(testValue, jobModel.getName());
    }

    @Test
    public void testDisplayName(){
        String testValue = "test";
        jobModel.setDisplayName(testValue);
        assertEquals(testValue, jobModel.getDisplayName());
    }

    @Test
    public void testUrl(){
        String testValue = "test";
        jobModel.setUrl(testValue);
        assertEquals(testValue, jobModel.getUrl());
    }

    @Test
    public void testBuild(){
        BuildModel buildModel = new BuildModel();
        jobModel.setBuild(buildModel);
        assertEquals(buildModel, jobModel.getBuild());
    }

    @Test
    public void testUserId(){
        String testValue = "test";
        jobModel.setUserId(testValue);
        assertEquals(testValue, jobModel.getUserId());
    }

    @Test
    public void testUserName(){
        String testValue = "test";
        jobModel.setUserName(testValue);
        assertEquals(testValue, jobModel.getUserName());
    }

    @Test
    public void testCreatedDated(){
        Date testValue = new Date(0);
        jobModel.setCreatedDate(testValue);
        assertEquals(testValue, jobModel.getCreatedDate());
    }

    @Test
    public void testUpdatedDate(){
        Date testValue = new Date(0);
        jobModel.setUpdatedDate(testValue);
        assertEquals(testValue, jobModel.getUpdatedDate());
    }

    @Test
    public void testStatus(){
        String testValue = "test";
        jobModel.setStatus(testValue);
        assertEquals(testValue, jobModel.getStatus());
    }

    @Test
    public void testConfigFile(){
        String testValue = "test";
        jobModel.setConfigFile(testValue);
        assertEquals(testValue, jobModel.getConfigFile());
    }

    @Test
    public void testStage(){
        String testValue = "test";
        jobModel.setStage(testValue);
        assertEquals(testValue, jobModel.getStage());
    }


}
