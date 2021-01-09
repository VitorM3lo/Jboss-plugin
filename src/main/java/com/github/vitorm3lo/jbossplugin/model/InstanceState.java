package com.github.vitorm3lo.jbossplugin.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InstanceState {

    public String serverName;
    public String serverPath;
    public List<String> deployablePath;

    public InstanceState() {
    }

    public InstanceState(String serverName, File serverPath, List<File> deployablePath) {
        this.serverName = serverName;
        this.serverPath = serverPath.getAbsolutePath();
        if(this.deployablePath == null) {
            this.deployablePath = new ArrayList<>();
        }
        for (File deployable : deployablePath) {
            this.deployablePath.add(deployable.getAbsolutePath());
        }
    }
}
