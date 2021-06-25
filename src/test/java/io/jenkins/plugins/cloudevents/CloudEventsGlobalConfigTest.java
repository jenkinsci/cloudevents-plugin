package io.jenkins.plugins.cloudevents;

import hudson.util.FormValidation;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.RestartableJenkinsRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


public class CloudEventsGlobalConfigTest {


    @Rule
    public RestartableJenkinsRule jenkinsRule = new RestartableJenkinsRule();

    @Test
    public void doCheckGlobalConfig() {
    jenkinsRule.then(step -> {
            CloudEventsGlobalConfig cloudEventsGlobalConfig = CloudEventsGlobalConfig.get();
            assertEquals(cloudEventsGlobalConfig.getSinkType(), "http");
            assertEquals(cloudEventsGlobalConfig.getSinkURL(), "");
            assertEquals(cloudEventsGlobalConfig.getEvents().size(), 0);

            assertTrue(cloudEventsGlobalConfig.isCreated());
            assertTrue(cloudEventsGlobalConfig.isUpdated());
            assertTrue(cloudEventsGlobalConfig.isEnteredWaiting());
            assertTrue(cloudEventsGlobalConfig.isLeft());
            assertTrue(cloudEventsGlobalConfig.isStarted());
            assertTrue(cloudEventsGlobalConfig.isCompleted());
            assertTrue(cloudEventsGlobalConfig.isFinalized());
            assertTrue(cloudEventsGlobalConfig.isFailed());


        });
    }

    @Test
    public void doCheckSinkURL() {
        jenkinsRule.then(step -> {
            CloudEventsGlobalConfig cloudEventsGlobalConfig = CloudEventsGlobalConfig.get();
            assertEquals(cloudEventsGlobalConfig.doCheckSinkURL("").kind, FormValidation.Kind.ERROR);
            assertEquals(cloudEventsGlobalConfig.doCheckSinkURL("tcp://my-company.com/events").kind, FormValidation.Kind.ERROR);
            assertEquals(cloudEventsGlobalConfig.doCheckSinkURL("http://my-company.com/events").kind, FormValidation.Kind.OK);
        });
    }

}
