package io.jenkins.plugins.cloudevents.model;

public class QueueCauseModel {
    private String reasonForWaiting;

    private String type;

    public QueueCauseModel() {
        this.reasonForWaiting = "";
        this.type = "";
    }

    public String getReasonForWaiting() {
        return reasonForWaiting;
    }

    public void setReasonForWaiting(String reasonForWaiting) {
        this.reasonForWaiting = reasonForWaiting;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

