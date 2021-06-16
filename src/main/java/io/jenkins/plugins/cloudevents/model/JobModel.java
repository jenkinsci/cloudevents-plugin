package io.jenkins.plugins.cloudevents.model;

public class JobModel {
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

}
