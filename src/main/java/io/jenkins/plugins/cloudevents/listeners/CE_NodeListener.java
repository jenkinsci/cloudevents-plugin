package io.jenkins.plugins.cloudevents.listeners;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Computer;
import hudson.model.TaskListener;
import hudson.slaves.ComputerListener;
import hudson.slaves.OfflineCause;
import io.jenkins.plugins.cloudevents.Stage;

import java.io.IOException;

@Extension
public class CE_NodeListener extends ComputerListener {

    public CE_NodeListener() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOnline(Computer c, TaskListener listener) throws IOException, InterruptedException {
        Stage.ONLINE.handleEvent(c, "node");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onOffline(@NonNull Computer c, OfflineCause cause) {
        Stage.OFFLINE.handleEvent(c, "node");
    }


}
