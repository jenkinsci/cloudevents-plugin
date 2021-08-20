package io.jenkins.plugins.cloudevents.model;

import hudson.model.Computer;
import hudson.slaves.OfflineCause;

import java.beans.Transient;
import java.util.List;

public class NodeModel implements Model {

    private static final Object JENKINS_SOURCE = "org.jenkinsci.node.";

    private int numExecutors;

    private OfflineCause offlineCause;

    private String nodeName;

    private String cachedHostName;

    private List<Computer.TerminationRequest> terminatedBy;

    private long connectTime;

    private String status;

    public int getNumExecutors() {
        return numExecutors;
    }

    public void setNumExecutors(int numExecutors) {
        this.numExecutors = numExecutors;
    }

    public OfflineCause getOfflineCause() {
        return offlineCause;
    }

    public void setOfflineCause(OfflineCause offlineCause) {
        this.offlineCause = offlineCause;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        try{
            this.nodeName = nodeName;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    public String getCachedHostName() {
        return cachedHostName;
    }

    public void setCachedHostName(String cachedHostName) {
        this.cachedHostName = cachedHostName;
    }

    public List<Computer.TerminationRequest> getTerminatedBy() {
        return terminatedBy;
    }

    public void setTerminatedBy(List<Computer.TerminationRequest> terminatedBy) {
        this.terminatedBy = terminatedBy;
    }

    public long getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(long connectTime) {
        this.connectTime = connectTime;
    }

    @Transient
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Transient
    @Override
    public String getType() {
        return JENKINS_SOURCE + getStatus();
    }

    @Transient
    @Override
    public String getSource() {
        return String.format("node/%s", nodeName);
    }
}
