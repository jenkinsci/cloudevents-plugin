package io.jenkins.plugins.cloudevents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QueueModel implements Model {

    private static final String JENKINS_SOURCE = "org.jenkinsci.queue.";

    private String ciUrl;

    private String displayName;

    private Date entryTime;

    private Date exitTime;

    private String startedBy;

    private int jenkinsQueueId;

    private String status;

    private long duration;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<QueueCauseModel> queueCauses;


    public QueueModel() {
        this.ciUrl = "";
        this.displayName = "";
        this.entryTime = new Date();
        this.exitTime = new Date();
        this.startedBy = "";
        this.jenkinsQueueId = 0;
        this.status = "";
        this.duration = 0;
        this.queueCauses = new ArrayList<>();
    }

    public String getCiUrl() {
        return ciUrl;
    }

    public void setCiUrl(String ciUrl) {
        this.ciUrl = ciUrl;
    }

    public Date getEntryTime() {
        return new Date(entryTime.getTime());
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime =  new Date(entryTime.getTime());
    }

    public Date getExitTime() {
        return  new Date(exitTime.getTime());
    }

    public void setExitTime(Date exitTime) {
        this.exitTime =  new Date(exitTime.getTime());
    }

    public String getStartedBy() {
        return startedBy;
    }

    public void setStartedBy(String startedBy) {
        this.startedBy = startedBy;
    }

    public int getJenkinsQueueId() {
        return jenkinsQueueId;
    }

    public void setJenkinsQueueId(int jenkinsQueueId) {
        if (jenkinsQueueId <= 0) {
            throw new IllegalArgumentException();
        }
        this.jenkinsQueueId = jenkinsQueueId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<QueueCauseModel> getQueueCauses() {
        return queueCauses;
    }

    public void setQueueCauses(List<QueueCauseModel> queueCauses) {
        this.queueCauses = queueCauses;
    }

    public void addQueueCause(QueueCauseModel queueCauseModel) {
        this.queueCauses.add(queueCauseModel);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonIgnore
    @Override
    public String getSource() {
        return String.format("job/%s", this.getDisplayName());
    }

    @JsonIgnore
    @Override
    public String getType() {
        return JENKINS_SOURCE + this.getStatus().toLowerCase();
    }
}
