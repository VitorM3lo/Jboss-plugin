package com.github.vitorm3lo.jbossplugin.services;

import com.github.vitorm3lo.jbossplugin.actions.CommandLineUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.project.VetoableProjectManagerListener;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.content.Content;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CommandLineService {

    public ProcessHandler runServer(String jbossPath, String serverName) {
        return runServer(jbossPath, serverName, false);
    }

    public ProcessHandler debugServer(String jbossPath, String serverName) {
        return runServer(jbossPath, serverName, true);
    }

    protected ProcessHandler runServer(String jbossPath, String serverName, boolean debug) {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add(jbossPath);

        Project activeProject = getActiveProject();

        if (activeProject == null) {
            return null;
        }

        CommandLineUtil.CUSTOM_CONSOLE = debug ? CommandLineUtil.DEBUG_CONSOLE : CommandLineUtil.RUN_CONSOLE;

        CommandLineUtil.DISPLAY_NAME = serverName;
        CommandLineUtil.activateConsoleView(activeProject);

        Content content = CommandLineUtil.getConsoleViewContent(activeProject);
        assert content != null;
        ConsoleView console = (ConsoleView) (content.getComponent());

        GeneralCommandLine commandLine = new GeneralCommandLine(cmds);
        commandLine.setCharset(StandardCharsets.UTF_8);
        commandLine.setWorkDirectory(activeProject.getBasePath());

        ProcessHandler processHandler = null;
        try {
            processHandler = new OSProcessHandler(commandLine);
        } catch (ExecutionException exception) {
            exception.printStackTrace();
        }
        assert processHandler != null : "Process Handler should not be null";
        console.attachToProcess(processHandler);

        processHandler.startNotify();
        return processHandler;
    }

    public Project getActiveProject() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        Project activeProject = null;
        for (Project project : projects) {
            Window window = WindowManager.getInstance().suggestParentWindow(project);
            if (window != null && window.isActive()) {
                activeProject = project;
            }
        }
        return activeProject;
    }

}
