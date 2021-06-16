package io.jenkins.plugins.cloudevents;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import java.util.List;

/**
 * Jenkins global configuration.
 */
@Extension
public class CloudEventsConfig extends GlobalConfiguration {

    public static final String PROTOCOL_ERROR_MESSAGE = "Only http and https protocols are supported";
    private List<EndPoint> endpoints;

    public CloudEventsConfig() {
        load();
    }

    /**
     * @return the singleton instance
     */
    public static CloudEventsConfig get() {
        return GlobalConfiguration.all().get(CloudEventsConfig.class);
    }

    public List<EndPoint> getEndpoints() {
        return endpoints;
    }

    @DataBoundSetter
    public void setEndpoints(List<EndPoint> endpoints) {
        this.endpoints = endpoints;
        save();
    }

    public FormValidation doCheckSinkURL(@QueryParameter(value = "sinkURL") String sinkURL) {
        if (sinkURL == null || sinkURL.isEmpty()) {
            return FormValidation.error("Provide valid Sink URL. " +
                    "For ex: \"http://ci.mycompany.com/api/steps\"");
        }
        if (validateProtocolUsed(sinkURL))
            return FormValidation.error(PROTOCOL_ERROR_MESSAGE);
        return FormValidation.ok();
    }

    private boolean validateProtocolUsed(String sinkURL) {
        return !(sinkURL.startsWith("http://") || sinkURL.startsWith("https://"));
    }

}
