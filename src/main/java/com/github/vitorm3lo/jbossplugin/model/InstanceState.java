package com.github.vitorm3lo.jbossplugin.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InstanceState {

    public String serverName;
    public String serverPath;
    public List<String> deployablePath;
    public int debugPort;

    public InstanceState() {
        serverPath = "";
        serverName = "";
        debugPort = -1;
        deployablePath = new ArrayList<>();
    }

    public InstanceState(String serverName, int debugPort, File serverPath, List<File> deployablePath) {
        this.serverName = serverName;
        this.serverPath = serverPath.getAbsolutePath();
        this.debugPort = debugPort;
        if (this.deployablePath == null) {
            this.deployablePath = new ArrayList<>();
        }
        for (File deployable : deployablePath) {
            this.deployablePath.add(deployable.getAbsolutePath());
        }
    }

}
