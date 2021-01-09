package com.github.vitorm3lo.jbossplugin.model;

import java.io.File;
import java.util.List;

public class Instance {

    private String serverName;
    private final File serverPath;
    private final List<File> deployablePath;

    public Instance(String serverName, File serverPath, List<File> deployablePath) {
        this.serverName = serverName;
        this.serverPath = serverPath;
        this.deployablePath = deployablePath;
    }

    public File getServerPath() {
        return serverPath;
    }

    public List<File> getDeployablePath() {
        return deployablePath;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
