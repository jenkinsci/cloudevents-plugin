package io.jenkins.plugins.cloudevents;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class CloudEventsSink {

    abstract public void sendCloudEvent(String sinkURL, Object data) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException;

}
