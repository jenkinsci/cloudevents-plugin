package io.jenkins.plugins.cloudevents;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.message.MessageWriter;
import io.cloudevents.core.v1.CloudEventBuilder;
import io.jenkins.plugins.cloudevents.model.JobModel;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.jenkins.plugins.cloudevents.CloudEventMessageWriter.createMessageWriter;

/**
 * Represents an Endpoint-object which stores the URL to which an event will be sent along with the type of the event.
 */
public class EndPoint {

    private static final Logger LOGGER = Logger.getLogger(EndPoint.class.getName());
    private String sinkURL;
    private String event = "all";


    /**
     * Creates a new Endpoint object for capturing and sending CloudEvents.
     *
     * @param sinkURL The actual URL for sending the CLoudEvent to
     */
    @DataBoundConstructor
    public EndPoint(String sinkURL) {
        this.sinkURL = sinkURL;
    }

    public String getSinkURL() {
        if (this.sinkURL == null) {
            this.sinkURL = "";
        }
        return this.sinkURL;
    }


    public String getEvent() {
        return event;
    }

    /**
     * Sets the specific event to contact the endpoint for.
     *
     * @param event 'STARTED' - Fire on job started. 'COMPLETED' - Fire on job completed. 'FINALIZED' - Fire on job finalized.
     */
    @DataBoundSetter
    public void setEvent(String event) {
        this.event = event;
    }


    public void send(String sinkURL, Object data) throws IOException {

        JobModel jobModel = (JobModel) data;

        // Used for giving a unique-id to CloudEvents sent from this plugin.
        UUID uuid = UUID.randomUUID();

        // TODO: Add more types later compliant with Listeners
        String type = "JOB_" + jobModel.getBuild().getPhase();

        String jsonToPost = JSONUtil.convertToJson(data);

        // Constructs the CloudEvent in Binary Format
        CloudEvent cloudEvent = new CloudEventBuilder()
                .withId(uuid.toString())
                .withSource(URI.create("org.jenkinsci"))
                .withType(type)
                .withDataContentType("application/json")
                .withData(jsonToPost.getBytes(StandardCharsets.UTF_8))
                .build();

        URL url = new URL(sinkURL);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);


        MessageWriter messageWriter = createMessageWriter(httpUrlConnection);
        messageWriter.writeBinary(cloudEvent);

        int responseCode = httpUrlConnection.getResponseCode();

        LOGGER.log(Level.INFO, "Response received: " + responseCode);
    }
}
