package com.github.vitorm3lo.jbossplugin.services;

import com.github.vitorm3lo.jbossplugin.actions.CommandLineUtil;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.ui.content.Content;

import java.io.IOException;
import java.util.ArrayList;

public class CommandLineService {

    public ProcessHandler runServer(String jbossPath, String serverName, int debugPort, Project activeProject) {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("cmd");
        cmds.add("start");
        cmds.add("");
        cmds.add(jbossPath);
        if (debugPort != -1) {
            cmds.add("--debug");
            cmds.add("" + debugPort);
        }

        if (activeProject == null) {
            return null;
        }

        CommandLineUtil.DISPLAY_NAME = serverName;
        CommandLineUtil.activateConsoleView(activeProject);

        Content content = CommandLineUtil.getConsoleViewContent(activeProject);
        assert content != null;
        ConsoleView console = (ConsoleView) (content.getComponent());

//        GeneralCommandLine commandLine = new GeneralCommandLine(cmds);
//        commandLine.setCharset(StandardCharsets.UTF_8);
//        commandLine.setWorkDirectory(activeProject.getBasePath());

        Runtime rt = Runtime.getRuntime();
        ProcessHandler processHandler = null;
        try {
            Process pr = rt.exec(jbossPath + " --debug 8787");
            processHandler = new OSProcessHandler(pr, jbossPath + " --debug 8787");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        assert processHandler != null : "Process Handler should not be null";
        console.attachToProcess(processHandler);

        processHandler.startNotify();
        return processHandler;
    }

}
