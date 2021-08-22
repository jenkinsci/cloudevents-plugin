package io.jenkins.plugins.cloudevents.listeners;

import hudson.Extension;
import hudson.model.Queue;
import io.jenkins.plugins.cloudevents.Stage;

@Extension
public class CEQueueListener extends hudson.model.queue.QueueListener {
    @Override
    public void onEnterWaiting(Queue.WaitingItem wi) {
        Stage.ENTERED_WAITING.handleEvent(wi, "queue");
    }

    @Override
    public void onLeft(Queue.LeftItem li) {
        Stage.LEFT.handleEvent(li, "queue");
    }
}
