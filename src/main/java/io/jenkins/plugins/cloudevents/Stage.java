package io.jenkins.plugins.cloudevents;

import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.cloudevents.model.BuildModel;
import io.jenkins.plugins.cloudevents.model.JobModel;
import jenkins.model.Jenkins;

import java.util.logging.Logger;

public enum Stage {
    STARTED, COMPLETED, FINALIZED;

    private static final Logger LOGGER = Logger.getLogger("Stage");

    public void handle(Run run, TaskListener listener, long timestamp) {

        for (EndPoint endpoint : CloudEventsConfig.get().getEndpoints()) {

            if (shouldSendInfo(endpoint, run.getResult())) {

                try {
                    String sinkURL = endpoint.getSinkURL();

                    listener.getLogger().println("Notifying the Sink.");


                    // Prepare the payload to send to the Sink.
                    JobModel jobModel = buildJobModel(run.getParent(), run, listener, timestamp, endpoint);

                    endpoint.send(sinkURL, jobModel);

                } catch (Throwable error) {
                    listener.getLogger().println("Failed to notify the listener. Error: " + error.getMessage());
                }
            }

        }
    }


    private JobModel buildJobModel(Job parent, Run run, TaskListener listener, long timestamp, EndPoint endpoint) {

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

    /**
     * Checks if the current endpoint is configured to receive events for the current status of the run
     *
     * @param endpoint Endpoint object
     * @param result   The current status of the run, will be used to match with the endpoint-event
     * @return
     */
    private boolean shouldSendInfo(EndPoint endpoint, Result result) {
        String event = endpoint.getEvent();

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


}
