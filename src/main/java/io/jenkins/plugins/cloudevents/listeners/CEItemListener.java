package io.jenkins.plugins.cloudevents.listeners;

import hudson.Extension;
import hudson.model.Item;
import io.jenkins.plugins.cloudevents.Stage;

@Extension
public class CEItemListener extends hudson.model.listeners.ItemListener {

    public CEItemListener() {
    }

    @Override
    public void onCreated(Item item) {
        Stage.CREATED.handleEvent(item, "item");
    }

    @Override
    public void onUpdated(Item item) {
        Stage.UPDATED.handleEvent(item, "item");
    }
}
