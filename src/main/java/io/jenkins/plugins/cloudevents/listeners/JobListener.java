package io.jenkins.plugins.cloudevents.listeners;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import io.jenkins.plugins.cloudevents.Stage;


/**
 * Class which implements RunListener and responds to events relating to a job in Jenkins.
 */
@Extension
public class JobListener extends RunListener<Run> {

    public JobListener() {
        super(Run.class);
    }

    @Override
    public void onCompleted(Run run, @NonNull TaskListener listener) {
        Stage.COMPLETED.handle(run, listener, run.getTimeInMillis() + run.getDuration());
    }

    @Override
    public void onFinalized(Run run) {
        Stage.FINALIZED.handle(run, TaskListener.NULL, System.currentTimeMillis());
    }

    @Override
    public void onStarted(Run run, TaskListener listener) {
        Stage.STARTED.handle(run, listener, run.getTimeInMillis());
    }
}
