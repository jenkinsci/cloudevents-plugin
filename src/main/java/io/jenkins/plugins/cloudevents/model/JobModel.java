package io.jenkins.plugins.cloudevents.model;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;

public class JobModel implements Model {

    private static final String JENKINS_SOURCE = "org.jenkinsci.job.";

    private String userId;

    private String userName;

    private Date createdDate;

    private Date updatedDate;

    private String status;

    @JsonIgnore
    private String stage;

    private String name;

    private String displayName;

    private String url;

    private BuildModel build;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BuildModel getBuild() {
        return build;
    }

    public void setBuild(BuildModel build) {
        this.build = build;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    @JsonIgnore
    @Override
    public String getSource() {
        return String.format("job/%s", this.getDisplayName());
    }

    @JsonIgnore
    @Override
    public String getType() {

        if (this.getBuild() != null) {
            if (this.getBuild().getPhase().toString().equals("FINALIZED") && this.getBuild().getStatus().equals("FAILURE") && this.getStage().equals("failed")) {
                return JENKINS_SOURCE + "failed";
            } else {
                return JENKINS_SOURCE + this.getBuild().getPhase().toString().toLowerCase();
            }
        } else {
            return JENKINS_SOURCE + this.getStatus().toLowerCase();
        }
    }

}
