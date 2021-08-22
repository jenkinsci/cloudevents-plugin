package io.jenkins.plugins.cloudevents;

import hudson.EnvVars;
import hudson.model.*;
import hudson.triggers.SCMTrigger;
import hudson.triggers.TimerTrigger;
import io.jenkins.plugins.cloudevents.model.*;
import io.jenkins.plugins.cloudevents.sinks.HTTPSink;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum Stage {
    STARTED, COMPLETED, FINALIZED, ENTERED_WAITING, LEFT, CREATED, UPDATED, ONLINE, OFFLINE;

    private static final Logger LOGGER = Logger.getLogger("Stage");

    public void handleEvent(Run run, TaskListener listener, long timestamp) throws NullPointerException {

        String sinkURL = CloudEventsGlobalConfig.get().getSinkURL();

        for (String event : CloudEventsGlobalConfig.get().getEvents()) {
            if (shouldSendBuild(event, run.getResult())) {

                try {
                    // Prepare the payload to send to the Sink.
                    JobModel jobModel = buildJobModel(run.getParent(), run, timestamp, listener);

                    jobModel.setStage(event);

                    switch (CloudEventsGlobalConfig.get().getSinkType()) {
                        case "http":
                            HTTPSink httpSink = new HTTPSink();
                            httpSink.sendCloudEvent(sinkURL, jobModel);
                            break;
                        default:
                            break;
                    }
                } catch (Throwable error) {
                    LOGGER.log(Level.WARNING, "Failed to notify the sink. Error: " + error.getMessage());
                }
            }

        }
    }

    public void handleEvent(Object o, String clazz) throws NullPointerException {

        String sinkURL = CloudEventsGlobalConfig.get().getSinkURL();

        Object sendObject = new Object();

        for (String event : CloudEventsGlobalConfig.get().getEvents()) {

            if (shouldSendItem(event)) {
                try {
                    switch (clazz) {

                        case "queue":
                            QueueModel queueModel = buildQueueModel((Queue.Item) o);
                            sendObject = queueModel;
                            break;

                        case "item":
                            JobModel jobModel = buildJobModel((Item) o);
                            sendObject = jobModel;
                            break;

                        case "node":
                            NodeModel nodeModel = buildNodeModel((Computer) o);
                            sendObject = nodeModel;
                            break;

                        default:
                            break;
                    }

                    if (CloudEventsGlobalConfig.get().getSinkType().equals("http")) {
                        HTTPSink httpSink = new HTTPSink();
                        httpSink.sendCloudEvent(sinkURL, sendObject);
                    }

                } catch (Throwable error) {
                    LOGGER.log(Level.INFO, "Error: " + error.getMessage());
                }
            }

        }
    }


    public JobModel buildJobModel(Job parent, Run run, long timestamp, TaskListener listener) throws IOException, InterruptedException {

        String rootUrl = Jenkins.get().getRootUrl();

        // Job model also stores information about the builds.
        JobModel jobModel = new JobModel();

        jobModel.setName(parent.getName());
        jobModel.setDisplayName(parent.getDisplayName());
        jobModel.setUrl(parent.getUrl());

        String userName = Jenkins.getAuthentication2().getCredentials().toString();
        User user = Jenkins.get().getUser(userName);
        if (user != null) {
            jobModel.setUserId(user.getId());
            jobModel.setUserName(user.getFullName());
        }

        // Get the result of the currently executing build.
        Result result = run.getResult();

        BuildModel buildModel = new BuildModel();
        buildModel.setNumber(run.number);
        buildModel.setQueueId(run.getQueueId());
        buildModel.setUrl(run.getUrl());
        buildModel.setPhase(this);
        buildModel.setTimestamp(timestamp);
        buildModel.setDuration(run.getDuration());
        ParametersAction parametersAction = run.getAction(ParametersAction.class);
        if (parametersAction != null) {
            EnvVars envVars = new EnvVars();
            for (ParameterValue parameterValue : parametersAction.getParameters()) {
                if (!parameterValue.isSensitive()) {
                    parameterValue.buildEnvironment(run, envVars);
                }
            }
            buildModel.setParameters(envVars);
        }
        jobModel.setBuild(buildModel);

        EnvVars envVars = run.getEnvironment(listener);

        ScmState scmState = new ScmState();
        if (envVars != null) {
            if (envVars.get("GIT_URL") != null) {
                scmState.setUrl(envVars.get("GIT_URL"));
            }
            if (envVars.get("GIT_BRANCH") != null) {
                scmState.setBranch(envVars.get("GIT_BRANCH"));
            }
            if (envVars.get("GIT_COMMIT") != null) {
                scmState.setCommit(envVars.get("GIT_COMMIT"));
            }
        }
        buildModel.setScmState(scmState);

        if (result != null) {
            buildModel.setStatus(result.toString());
        }

        if (rootUrl != null) {
            buildModel.setFullUrl(rootUrl + run.getUrl());
        }

        return jobModel;
    }

    public JobModel buildJobModel(Item item) throws IOException {
        JobModel jobModel = new JobModel();

        jobModel.setName(item.getName());
        jobModel.setDisplayName(item.getDisplayName());

        String userName = Jenkins.getAuthentication2().getName();
        User user = Jenkins.get().getUser(userName);
        if (user != null) {
            jobModel.setUserId(user.getId());
            jobModel.setUserName(user.getFullName());
        }

        jobModel.setUrl(item.getUrl());

        switch (this) {
            case CREATED:
                jobModel.setCreatedDate(new Date());
                jobModel.setStatus("CREATED");
                break;
            case UPDATED:
                jobModel.setUpdatedDate(new Date());
                jobModel.setStatus("UPDATED");
                break;
            default:
                jobModel.setStatus(null);
                break;
        }

        AbstractProject<?, ?> project = (AbstractProject<?, ?>) item;
        jobModel.setConfigFile(project.getConfigFile().asString());

        return jobModel;
    }


    public QueueModel buildQueueModel(Queue.Item item) {
        QueueModel queueModel = new QueueModel();

        String ciURL = Jenkins.get().getRootUrl();

        queueModel.setCiUrl(ciURL);
        queueModel.setDisplayName(item.task.getDisplayName());
        queueModel.setJenkinsQueueId((int) item.getId());
        queueModel.setStatus(this.toString());

        switch (this) {
            case ENTERED_WAITING:
                queueModel.setEntryTime(new Date());
                if (item.getCauseOfBlockage() != null) {
                    addWaitingCause(item, queueModel);
                }
                break;

            case LEFT:
                queueModel.setDuration(System.currentTimeMillis() - item.getInQueueSince());
                queueModel.setEntryTime(new Date(item.getInQueueSince()));
                queueModel.setExitTime(new Date());
                break;

            default:
                break;
        }


        addStartedBy(queueModel, item);

        return queueModel;
    }

    public NodeModel buildNodeModel(Computer computer) throws IOException, InterruptedException {
        NodeModel nodeModel = new NodeModel();
        String nodeName = null;
        String hostName = null;
        Long connectTime = null;

        if (computer != null) {
            hostName = computer.getHostName();
            connectTime = computer.getConnectTime();
    
            if (computer.getNode() != null) {
                Node node = computer.getNode();
                if (node != null) {
                    nodeName = node.getNodeName();
                    if (nodeName != null) {
                        nodeModel.setNodeName(nodeName);
                    }
                }
            }
            if (!hostName.isEmpty()) {
                nodeModel.setCachedHostName(hostName);
            }
            if (connectTime != null) {
                nodeModel.setConnectTime(connectTime);
            }

            switch (this) {
                case ONLINE:
                    nodeModel.setStatus("online");
                    break;
    
                case OFFLINE:
                    nodeModel.setStatus("offline");
                    break;
    
                default:
                    break;
            }
    
            if (computer.isOffline()) {
                nodeModel.setOfflineCause(computer.getOfflineCause());
                nodeModel.setTerminatedBy(computer.getTerminatedBy());
            }
        }
        
        return nodeModel;
    }


    /**
     * Checks whether the sink is configured to receive events relating to run of a job.
     *
     * @param event
     * @param result
     * @return
     */
    private boolean shouldSendBuild(String event, Result result) {

        switch (event) {
            case "failed":
                if (result == null) {
                    return false;
                }
                // Check for Failure in only the Finalized-stage of the build.
                return this.equals(FINALIZED) && result.equals(Result.FAILURE);

            default:
                return event.equals(this.toString().toLowerCase());
        }
    }

    /**
     * Checks whether the sink is configured to receive events emanating from Queue and Item.
     *
     * @param event
     * @return
     */
    private boolean shouldSendItem(String event) {
        return event.equals(this.toString().toLowerCase());
    }

    private void addStartedBy(QueueModel queueModel, Queue.Item item) {

        final String UPSTREAM = "UPSTREAM";
        final String SCM = "SCM";
        final String TIMER = "TIMER";

        List<Cause> causeList = item.getCauses();
        for (Cause cause : causeList) {
            if (cause instanceof Cause.UserIdCause) {
                queueModel.setStartedBy(((Cause.UserIdCause) cause).getUserName());
            } else if (cause instanceof Cause.UpstreamCause) {
                queueModel.setStartedBy(UPSTREAM);
            } else if (cause instanceof SCMTrigger.SCMTriggerCause) {
                queueModel.setStartedBy(SCM);
            } else if (cause instanceof TimerTrigger.TimerTriggerCause) {
                queueModel.setStartedBy(TIMER);
            }
        }
    }

    private void addWaitingCause(Queue.Item item, QueueModel queueModel) {
        QueueCauseModel queueCauseModel = new QueueCauseModel();
        queueCauseModel.setReasonForWaiting(item.getCauseOfBlockage().getShortDescription());
        queueCauseModel.setType(this.toString().toLowerCase());
        queueModel.addQueueCause(queueCauseModel);
    }
}
