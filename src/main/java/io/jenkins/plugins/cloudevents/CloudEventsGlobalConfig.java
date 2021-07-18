package io.jenkins.plugins.cloudevents;

import hudson.Extension;
import hudson.util.FormValidation;
import jenkins.model.GlobalConfiguration;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Jenkins global configuration.
 */
@Extension
public class CloudEventsGlobalConfig extends GlobalConfiguration {

    public static final String PROTOCOL_ERROR_MESSAGE = "Only http and https protocols are supported";

    private String sinkType;
    private String sinkURL;

    private boolean created;
    private boolean updated;
    private boolean enteredWaiting;
    private boolean left;
    private boolean started;
    private boolean completed;
    private boolean finalized;
    private boolean failed;
    private boolean online;
    private boolean offline;


    private List<String> events;

    public CloudEventsGlobalConfig() {
        load();
        sinkType = "http";
        sinkURL = "";
        created = true;
        updated = true;
        enteredWaiting = true;
        left = true;
        started = true;
        completed = true;
        finalized = true;
        failed = true;
        online = true;
        offline = true;
        events = new ArrayList<>();
    }

    /**
     * @return the singleton instance
     */
    public static CloudEventsGlobalConfig get() {
        return GlobalConfiguration.all().get(CloudEventsGlobalConfig.class);
    }

    public String getSinkURL() {
        return sinkURL;
    }

    @DataBoundSetter
    public void setSinkURL(String sinkURL) {
        this.sinkURL = sinkURL;
    }

    public String getSinkType() {
        return this.sinkType;
    }

    @DataBoundSetter
    public void setSinkType(String sinkType) {
        this.sinkType = sinkType;
    }

    public boolean isCreated() {
        return created;
    }

    @DataBoundSetter
    public void setCreated(boolean created) {
        this.created = created;
        addOrRemoveEvent(created, "created");
    }

    public boolean isUpdated() {
        return updated;
    }

    @DataBoundSetter
    public void setUpdated(boolean updated) {
        this.updated = updated;
        addOrRemoveEvent(updated, "updated");
    }

    public boolean isEnteredWaiting() {
        return enteredWaiting;
    }

    @DataBoundSetter
    public void setEnteredWaiting(boolean enteredWaiting) {
        this.enteredWaiting = enteredWaiting;
        addOrRemoveEvent(enteredWaiting, "entered_waiting");
    }

    public boolean isLeft() {
        return left;
    }

    @DataBoundSetter
    public void setLeft(boolean left) {
        this.left = left;
        addOrRemoveEvent(left, "left");
    }

    public boolean isStarted() {
        return started;
    }

    @DataBoundSetter
    public void setStarted(boolean started) {
        this.started = started;
        addOrRemoveEvent(started, "started");
    }

    public boolean isCompleted() {
        return completed;
    }

    @DataBoundSetter
    public void setCompleted(boolean completed) {
        this.completed = completed;
        addOrRemoveEvent(completed, "completed");
    }

    public boolean isFinalized() {
        return finalized;
    }

    @DataBoundSetter
    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
        addOrRemoveEvent(finalized, "finalized");
    }

    public boolean isFailed() {
        return failed;
    }

    @DataBoundSetter
    public void setFailed(boolean failed) {
        this.failed = failed;
        addOrRemoveEvent(failed, "failed");

    }

    public boolean isOnline() {
        return online;
    }

    @DataBoundSetter
    public void setOnline(boolean online) {
        this.online = online;
        addOrRemoveEvent(online, "online");
    }

    public boolean isOffline() {
        return offline;
    }

    @DataBoundSetter
    public void setOffline(boolean offline) {
        this.offline = offline;
        addOrRemoveEvent(offline, "offline");
    }

    public List<String> getEvents() {
        return events;
    }

    /**
     * If a particular event is selected in the UI, and the list of events already contains this event do nothing.
     * Else, add it to the list.
     * If the event is unselected, remove if from the list of events if it is an element inside the list.
     *
     * @param event
     * @param stringEvent
     */
    private void addOrRemoveEvent(boolean event, String stringEvent) {
        if (event) {
            if (events.contains(stringEvent)) {
                return;
            } else {
                events.add(stringEvent);
            }

        } else {
            if (events.contains(stringEvent)) {
                events.remove(stringEvent);
            }
        }
    }

    // TODO: Move validation to check for different sink types.
    public FormValidation doCheckSinkURL(@QueryParameter(value = "sinkURL") String sinkURL) {
        if (sinkURL == null || sinkURL.isEmpty()) {
            return FormValidation.error("Provide valid Sink URL. " +
                    "For ex: \"http://ci.mycompany.com/api/steps\"");
        }
        if (validateProtocolUsed(sinkURL))
            return FormValidation.error(PROTOCOL_ERROR_MESSAGE);
        return FormValidation.ok();

    }

    public boolean validateProtocolUsed(String sinkURL) {
        return !(sinkURL.startsWith("http://") || sinkURL.startsWith("https://"));
    }

}
