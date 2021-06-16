package io.jenkins.plugins.cloudevents;

import io.cloudevents.core.message.MessageWriter;
import io.cloudevents.http.HttpMessageFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;

/**
 * Write CloudEvents payload and metadata into a HTTP Request (or Response).
 */
public class CloudEventMessageWriter {

    public CloudEventMessageWriter() {
    }

    public static MessageWriter createMessageWriter(HttpURLConnection httpUrlConnection) {
        return HttpMessageFactory.createWriter(
                httpUrlConnection::setRequestProperty,
                body -> {
                    try {
                        if (body != null) {
                            httpUrlConnection.setRequestProperty("content-length", String.valueOf(body.length));
                            try (OutputStream outputStream = httpUrlConnection.getOutputStream()) {
                                outputStream.write(body);
                            }
                        } else {
                            httpUrlConnection.setRequestProperty("content-length", "0");
                        }
                    } catch (IOException t) {
                        throw new UncheckedIOException(t);
                    }
                }
        );
    }
}
