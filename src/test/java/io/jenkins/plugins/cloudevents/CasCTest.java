package io.jenkins.plugins.cloudevents;

import hudson.ExtensionList;
import io.jenkins.plugins.casc.misc.ConfiguredWithCode;
import io.jenkins.plugins.casc.misc.JenkinsConfiguredWithCodeRule;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CasCTest {


    @ClassRule
    @ConfiguredWithCode("configuration-as-code.yaml")
    public static JenkinsConfiguredWithCodeRule j = new JenkinsConfiguredWithCodeRule();

    @Test
    public void testConfigYAML() {
        CloudEventsGlobalConfig cloudEventsGlobalConfig = ExtensionList.lookupSingleton(CloudEventsGlobalConfig.class);
        assertEquals("http", cloudEventsGlobalConfig.getSinkType());
        assertEquals("http://my-company.com", cloudEventsGlobalConfig.getSinkURL());
        assertTrue(cloudEventsGlobalConfig.isStarted());
        assertTrue(cloudEventsGlobalConfig.isCompleted());
        assertTrue(cloudEventsGlobalConfig.isFinalized());
        assertTrue(cloudEventsGlobalConfig.isFailed());
        assertTrue(cloudEventsGlobalConfig.isEnteredWaiting());
        assertTrue(cloudEventsGlobalConfig.isLeft());
        assertTrue(cloudEventsGlobalConfig.isCreated());
        assertTrue(cloudEventsGlobalConfig.isUpdated());
        assertTrue(cloudEventsGlobalConfig.isOnline());
        assertTrue(cloudEventsGlobalConfig.isOffline());
    }
}
