package io.jenkins.plugins.cloudevents.model;

/**
 * Interface to set the type and source for each CloudEvent.
 */
public interface Model {

    public String getType();

    public String getSource();
}
