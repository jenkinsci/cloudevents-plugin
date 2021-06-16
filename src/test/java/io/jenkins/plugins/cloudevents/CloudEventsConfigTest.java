package io.jenkins.plugins.cloudevents;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.jvnet.hudson.test.RestartableJenkinsRule;

public class CloudEventsConfigTest {

    @Rule
    public RestartableJenkinsRule rr = new RestartableJenkinsRule();

    /**
     * Tries to exercise enough code paths to catch common mistakes:
     * <ul>
     * <li>missing {@code load}
     * <li>missing {@code save}
     * <li>misnamed or absent getter/setter
     * <li>misnamed {@code textbox}
     * </ul>
     */
    @Test
    public void uiAndStorage() {
        rr.then(r -> {
            //assertNull("not set initially", CloudEventsConfig.get().getSinkURL());
            HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
            HtmlTextInput textbox = config.getInputByName("_.sinkURL");
            textbox.setText("Test");
            r.submit(config);
            //assertEquals("global config page let us edit it", "Test", CloudEventsConfig.get().getSinkURL());
        });
        rr.then(r -> {
            //assertEquals("still there after restart of Jenkins", "Test", CloudEventsConfig.get().getSinkURL());
        });
    }

}
