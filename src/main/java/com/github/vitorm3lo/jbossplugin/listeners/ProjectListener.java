package com.github.vitorm3lo.jbossplugin.listeners;

import com.github.vitorm3lo.jbossplugin.forms.ServerRow;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProjectListener implements ProjectManagerListener {
    private static ProjectListener projectListener;

    private final List<ServerRow> rows = new ArrayList<>();

    public static ProjectListener getInstance() {
        if (projectListener == null) {
            projectListener = new ProjectListener();
        }
        return projectListener;
    }

    @Override
    public void projectClosing(@NotNull Project project) {
        project.save();
        for (ServerRow row : projectListener.rows) {
            if (row.getRunProcessHandler() != null && !row.getRunProcessHandler().isProcessTerminated()) {
                row.getRunProcessHandler().destroyProcess();
            }
        }
    }

    public void addRow(ServerRow row) {
        rows.add(row);
    }
}
