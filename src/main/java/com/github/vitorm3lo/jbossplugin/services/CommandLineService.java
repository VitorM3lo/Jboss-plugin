package com.github.vitorm3lo.jbossplugin.services;

import com.github.vitorm3lo.jbossplugin.actions.CommandLineUtil;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.ui.content.Content;

import java.io.IOException;

public class CommandLineService {

    public ProcessHandler runServer(String jbossPath, String serverName, int debugPort, Project activeProject) {

        if (activeProject == null) {
            return null;
        }

        CommandLineUtil.DISPLAY_NAME = serverName;
        CommandLineUtil.activateConsoleView(activeProject);

        Content content = CommandLineUtil.getConsoleViewContent(activeProject);
        assert content != null;
        ConsoleView console = (ConsoleView) (content.getComponent());

        Runtime rt = Runtime.getRuntime();
        ProcessHandler processHandler = null;
        try {
            String commandLine = jbossPath + (debugPort != -1 ? " --debug " + debugPort : "");
            Process pr = rt.exec(commandLine);
            processHandler = new OSProcessHandler(pr, commandLine);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        assert processHandler != null : "Process Handler should not be null";
        console.attachToProcess(processHandler);

        processHandler.startNotify();
        return processHandler;
    }

}
