package io.jenkins.plugins.cloudevents.sinks;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.message.MessageWriter;
import io.cloudevents.core.v1.CloudEventBuilder;
import io.jenkins.plugins.cloudevents.CloudEventsSink;
import io.jenkins.plugins.cloudevents.CloudEventsUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.jenkins.plugins.cloudevents.CloudEventMessageWriter.createMessageWriter;

public class HTTPSink extends CloudEventsSink {

    private static final Logger LOGGER = Logger.getLogger(HTTPSink.class.getName());


    public CloudEvent buildCloudEvent(Object data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        UUID uuid = UUID.randomUUID();

        Method getType = data.getClass().getMethod("getType");
        String type = (String) getType.invoke(data);

        Method getSource = data.getClass().getMethod("getSource");
        String source = (String) getSource.invoke(data);

        String cloudEventPayload = CloudEventsUtil.convertToJson(data);

        // Constructs the CloudEvent in Binary Format
        CloudEvent cloudEvent = new CloudEventBuilder()
                .withId(uuid.toString())
                .withSource(URI.create(source))
                .withType(type)
                .withDataContentType("application/json")
                .withData(cloudEventPayload.getBytes(StandardCharsets.UTF_8))
                .build();

        return cloudEvent;
    }

    @Override
    public void sendCloudEvent(String sinkURL, Object data) throws IOException, NullPointerException {

        CloudEvent cloudEventToPost = null;
        try {
            cloudEventToPost = buildCloudEvent(data);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        URL url = new URL(sinkURL);
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        try {
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);

            MessageWriter messageWriter = createMessageWriter(httpUrlConnection);
            messageWriter.writeBinary(cloudEventToPost);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        int responseCode = httpUrlConnection.getResponseCode();
        LOGGER.log(Level.INFO, String.format("Received response: %s", responseCode));
    }
}
