package io.jenkins.plugins.cloudevents;

import hudson.model.*;
import io.jenkins.plugins.cloudevents.model.BuildModel;
import io.jenkins.plugins.cloudevents.model.JobModel;
import io.jenkins.plugins.cloudevents.model.QueueModel;
import io.jenkins.plugins.cloudevents.sinks.HTTPSink;
import jenkins.model.Jenkins;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum Stage {
    STARTED, COMPLETED, FINALIZED, ENTERED_WAITING, LEFT, CREATED, UPDATED;

    private static final Logger LOGGER = Logger.getLogger("Stage");

    public void handleBuild(Run run, TaskListener listener, long timestamp) throws NullPointerException {

        String sinkURL = CloudEventsGlobalConfig.get().getSinkURL();

        for (String event : CloudEventsGlobalConfig.get().getEvents()) {
            if (shouldSendBuild(event, run.getResult())) {

                try {
                    // Prepare the payload to send to the Sink.
                    JobModel jobModel = buildJobModel(run.getParent(), run, listener, timestamp);
                    jobModel.setStage(event);

                    if (CloudEventsGlobalConfig.get().getSinkType().equals("http")) {
                        HTTPSink httpSink = new HTTPSink();
                        httpSink.sendCloudEvent(sinkURL, jobModel);
                    }

                } catch (Throwable error) {
                    LOGGER.log(Level.WARNING, "Failed to notify the sink. Error: " + error.getMessage());
                }
            }

        }
    }

    public void handleQueue(Queue.Item item) throws NullPointerException {

        String sinkURL = CloudEventsGlobalConfig.get().getSinkURL();

        for (String event : CloudEventsGlobalConfig.get().getEvents()) {
            if (shouldSendItem(event)) {
                try {

                    QueueModel queueModel = buildQueueModel(item);

                    if (CloudEventsGlobalConfig.get().getSinkType().equals("http")) {
                        HTTPSink httpSink = new HTTPSink();
                        httpSink.sendCloudEvent(sinkURL, queueModel);
                    }

                } catch (Throwable error) {
                    LOGGER.log(Level.WARNING, "Failed to notify the sink. Error: " + error.getMessage());
                }
            }
        }
    }

    public void handleItem(Item item) throws NullPointerException {

        String sinkURL = CloudEventsGlobalConfig.get().getSinkURL();

        for (String event : CloudEventsGlobalConfig.get().getEvents()) {
            if (shouldSendItem(event)) {
                try {

                    JobModel jobModel = buildJobModel(item);

                    if (CloudEventsGlobalConfig.get().getSinkType().equals("http")) {
                        HTTPSink httpSink = new HTTPSink();
                        httpSink.sendCloudEvent(sinkURL, jobModel);
                    }
                } catch (Throwable error) {
                    LOGGER.log(Level.WARNING, "Failed to notify the sink. Error: " + error.getMessage());
                }
            }
        }
    }


    private JobModel buildJobModel(Job parent, Run run, TaskListener listener, long timestamp) {

        String rootUrl = Jenkins.get().getRootUrl();

        // Job model also stores information about the builds.
        JobModel jobModel = new JobModel();

        BuildModel buildModel = new BuildModel();

        // Get the result of the currently executing build.
        Result result = run.getResult();

        jobModel.setName(parent.getName());
        jobModel.setDisplayName(parent.getDisplayName());
        jobModel.setUrl(parent.getUrl());

        buildModel.setNumber(run.number);
        buildModel.setQueueId(run.getQueueId());
        buildModel.setUrl(run.getUrl());
        buildModel.setPhase(this);
        buildModel.setTimestamp(timestamp);
        buildModel.setDuration(run.getDuration());

        jobModel.setBuild(buildModel);

        if (result != null) {
            buildModel.setStatus(result.toString());
        }

        if (rootUrl != null) {
            buildModel.setFullUrl(rootUrl + run.getUrl());
        }

        return jobModel;
    }

    private JobModel buildJobModel(Item item) {
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

        // Sets the date and status for job-created and job-updated.
        if (this.toString().equals(Stage.CREATED.toString())) {
            jobModel.setCreatedDate(new Date());
            jobModel.setStatus("CREATED");
        } else if (this.toString().equals(Stage.UPDATED.toString())) {
            jobModel.setUpdatedDate(new Date());
            jobModel.setStatus("UPDATED");
        } else {
            jobModel.setStatus(null);
        }

        return jobModel;
    }


    private QueueModel buildQueueModel(Queue.Item item) {
        QueueModel queueModel = new QueueModel();

        String ciURL = Jenkins.get().getRootUrl();

        queueModel.setCiUrl(ciURL);
        queueModel.setDisplayName(item.task.getDisplayName());
        queueModel.setJenkinsQueueId((int) item.getId());
        queueModel.setStatus(this.toString());

        return queueModel;
    }

    /**
     * Checks whether the sink is configured to receive events relating to run of a job.
     * @param event
     * @param result
     * @return
     */
    private boolean shouldSendBuild(String event, Result result) {

        // Send all events.
        if (event == null) {
            return true;
        }

        switch (event) {
            case "all":
                return true;

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
     * @param event
     * @return
     */
    private boolean shouldSendItem(String event) {

        switch (event) {
            case "all":
                return true;
            default:
                return event.equals(this.toString().toLowerCase());
        }
    }


}
