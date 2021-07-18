package io.jenkins.plugins.cloudevents;

import hudson.util.FormValidation;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.RestartableJenkinsRule;

import static org.junit.Assert.*;


public class CloudEventsGlobalConfigTest {


    @Rule
    public RestartableJenkinsRule jenkinsRule = new RestartableJenkinsRule();

    @Test
    public void doCheckInitialGlobalConfig() {
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
    public void doCheckEvents() {
        jenkinsRule.then(step -> {
            CloudEventsGlobalConfig cloudEventsGlobalConfig = CloudEventsGlobalConfig.get();

            cloudEventsGlobalConfig.setCreated(true);
            assertTrue(cloudEventsGlobalConfig.isCreated());
            assertTrue(cloudEventsGlobalConfig.getEvents().contains("created"));
            cloudEventsGlobalConfig.setCreated(false);
            assertFalse(cloudEventsGlobalConfig.isCreated());
            assertFalse(cloudEventsGlobalConfig.getEvents().contains("created"));

            cloudEventsGlobalConfig.setUpdated(true);
            assertTrue(cloudEventsGlobalConfig.isUpdated());
            assertTrue(cloudEventsGlobalConfig.getEvents().contains("updated"));
            cloudEventsGlobalConfig.setUpdated(false);
            assertFalse(cloudEventsGlobalConfig.isUpdated());
            assertFalse(cloudEventsGlobalConfig.getEvents().contains("updated"));

            cloudEventsGlobalConfig.setEnteredWaiting(true);
            assertTrue(cloudEventsGlobalConfig.isEnteredWaiting());
            assertTrue(cloudEventsGlobalConfig.getEvents().contains("entered_waiting"));
            cloudEventsGlobalConfig.setEnteredWaiting(false);
            assertFalse(cloudEventsGlobalConfig.isEnteredWaiting());
            assertFalse(cloudEventsGlobalConfig.getEvents().contains("entered_waiting"));

            cloudEventsGlobalConfig.setLeft(true);
            assertTrue(cloudEventsGlobalConfig.isLeft());
            assertTrue(cloudEventsGlobalConfig.getEvents().contains("left"));
            cloudEventsGlobalConfig.setLeft(false);
            assertFalse(cloudEventsGlobalConfig.isLeft());
            assertFalse(cloudEventsGlobalConfig.getEvents().contains("left"));

            cloudEventsGlobalConfig.setOnline(true);
            assertTrue(cloudEventsGlobalConfig.isOnline());
            assertTrue(cloudEventsGlobalConfig.getEvents().contains("online"));
            cloudEventsGlobalConfig.setOnline(false);
            assertFalse(cloudEventsGlobalConfig.isOnline());
            assertFalse(cloudEventsGlobalConfig.getEvents().contains("online"));

            cloudEventsGlobalConfig.setOffline(true);
            assertTrue(cloudEventsGlobalConfig.isOffline());
            assertTrue(cloudEventsGlobalConfig.getEvents().contains("offline"));
            cloudEventsGlobalConfig.setOffline(false);
            assertFalse(cloudEventsGlobalConfig.isOffline());
            assertFalse(cloudEventsGlobalConfig.getEvents().contains("offline"));



        });
    }

    @Test
    public void doCheckSinkType() {
        jenkinsRule.then(step -> {
            CloudEventsGlobalConfig cloudEventsGlobalConfig = CloudEventsGlobalConfig.get();
            cloudEventsGlobalConfig.setSinkType("other");
            assertEquals(cloudEventsGlobalConfig.getSinkType(), "other");
        });
    }


    @Test
    public void doCheckSinkURL() {
        jenkinsRule.then(step -> {
            CloudEventsGlobalConfig cloudEventsGlobalConfig = CloudEventsGlobalConfig.get();

            cloudEventsGlobalConfig.setSinkURL("");
            assertEquals(cloudEventsGlobalConfig.getSinkURL(), "");
            assertEquals(cloudEventsGlobalConfig.doCheckSinkURL("").kind, FormValidation.Kind.ERROR);

            cloudEventsGlobalConfig.setSinkURL(null);
            assertEquals(cloudEventsGlobalConfig.getSinkURL(), null);
            assertEquals(cloudEventsGlobalConfig.doCheckSinkURL(null).kind, FormValidation.Kind.ERROR);

            cloudEventsGlobalConfig.setSinkURL("tcp://my-company.com/events");
            assertEquals(cloudEventsGlobalConfig.getSinkURL(), "tcp://my-company.com/events");
            assertEquals(cloudEventsGlobalConfig.doCheckSinkURL("tcp://my-company.com/events").kind, FormValidation.Kind.ERROR);

            cloudEventsGlobalConfig.setSinkURL("http://my-company.com/events");
            assertEquals(cloudEventsGlobalConfig.getSinkURL(), "http://my-company.com/events");
            assertEquals(cloudEventsGlobalConfig.doCheckSinkURL("http://my-company.com/events").kind, FormValidation.Kind.OK);

            cloudEventsGlobalConfig.setSinkURL("https://my-company.com/events");
            assertEquals(cloudEventsGlobalConfig.getSinkURL(), "https://my-company.com/events");
            assertEquals(cloudEventsGlobalConfig.doCheckSinkURL("https://my-company.com/events").kind, FormValidation.Kind.OK);
        });
    }

}
