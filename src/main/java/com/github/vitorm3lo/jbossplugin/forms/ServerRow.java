package com.github.vitorm3lo.jbossplugin.forms;

import com.github.vitorm3lo.jbossplugin.model.Instance;
import com.github.vitorm3lo.jbossplugin.services.CommandLineService;
import com.github.vitorm3lo.jbossplugin.services.PersistenceService;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.BaseContentCloseListener;
import com.intellij.execution.ui.RunContentManagerImpl;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.project.VetoableProjectManagerListener;
import com.intellij.openapi.project.impl.ProjectManagerImpl;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ServerRow extends JComponent {
    private JButton debugButton;
    private JButton deployButton;
    private JButton runButton;
    private JPanel serverRow;
    private JLabel serverNameLabel;

    private final Instance instance;
    private ProcessHandler runProcessHandler;
    private ProcessHandler debugProcessHandler;
    private final CommandLineService commandLineService = new CommandLineService();

    public ServerRow(Instance instance) {
        super();
        serverNameLabel.setText(instance.getServerName());
        this.instance = instance;
        deployServer();

        runButton.addActionListener(e -> {
            runServer();
        });
        debugButton.addActionListener(e -> {
            startDebugSession();
        });
        deployButton.addActionListener(e -> {
            deployServer();
        });
    }

    public JPanel getServerRow() {
        return serverRow;
    }

    private void deployServer() {
        
    }

    private void runServer() {
        if (runProcessHandler != null && !runProcessHandler.isProcessTerminated()) {
            runProcessHandler.destroyProcess();
            runButton.setText("Run");
            return;
        }
        runProcessHandler = commandLineService.runServer("C:\\Users\\Vitor\\Downloads\\jboss-eap-7.3.0\\jboss-eap-7.3\\bin\\standalone.bat", instance.getServerName());
        runButton.setText("Stop");
    }

    private void startDebugSession() {
        if (runProcessHandler == null || runProcessHandler.isProcessTerminated()) {
            JOptionPane.showMessageDialog(null,
                    "It's required a running server.",
                    "No server running",
                    JOptionPane.WARNING_MESSAGE);
        }
        if (debugProcessHandler != null && !debugProcessHandler.isProcessTerminated()) {
            debugProcessHandler.destroyProcess();
            debugButton.setText("Debug");
            return;
        }
        debugProcessHandler = commandLineService.debugServer("java -Xdebug -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n", instance.getServerName() + " - Debug");
        debugButton.setText("Stop debug");
    }

    public ProcessHandler getRunProcessHandler() {
        return runProcessHandler;
    }
}
