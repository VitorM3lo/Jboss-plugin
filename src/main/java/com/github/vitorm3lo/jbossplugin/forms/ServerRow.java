package com.github.vitorm3lo.jbossplugin.forms;

import com.github.vitorm3lo.jbossplugin.model.Instance;
import com.github.vitorm3lo.jbossplugin.services.CommandLineService;
import com.github.vitorm3lo.jbossplugin.services.DebugWindowService;
import com.github.vitorm3lo.jbossplugin.utils.ProjectUtil;
import com.intellij.execution.configurations.RemoteConnection;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ServerRow extends JComponent {
    private JButton debugButton;
    private JButton deployButton;
    private JButton runButton;
    private JPanel serverRow;
    private JLabel serverNameLabel;

    private final Instance instance;
    private ProcessHandler runProcessHandler;
    private final CommandLineService commandLineService = new CommandLineService();

    public ServerRow(Instance instance) {
        super();
        serverNameLabel.setText(instance.getServerName());
        this.instance = instance;
        deployServer();

        runButton.addActionListener(e -> runServer());
        debugButton.addActionListener(e -> startDebugSession());
        deployButton.addActionListener(e -> deployServer());
    }

    public JPanel getServerRow() {
        return serverRow;
    }

    private void deployServer() {
        for (File deployable : instance.getDeployablePath()) {
            try {
                Files.copy(Paths.get(deployable.toURI()), Paths.get(instance.getServerPath().getAbsolutePath(), deployable.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void runServer() {
        if (runProcessHandler != null && !runProcessHandler.isProcessTerminated()) {
            runProcessHandler.destroyProcess();
            runButton.setText("Run");
            return;
        }
        Path jbossPath = Paths.get(instance.getServerPath().getParentFile().getParentFile().getAbsolutePath(), "bin/", "standalone.bat");
        if (!jbossPath.toFile().exists()) {
            JOptionPane.showMessageDialog(null,
                    "It's required a jboss standalone.bat.",
                    "No runnable",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        runProcessHandler = commandLineService.runServer(jbossPath.toAbsolutePath().toString(), instance.getServerName(), ProjectUtil.getActiveProject());
        runButton.setText("Stop");
    }

    private void startDebugSession() {
        DebugWindowService.attach(ProjectUtil.getActiveProject(),
                new RemoteConnection(true, "localhost", "8787", false),
                instance.getServerName());
    }

    public ProcessHandler getRunProcessHandler() {
        return runProcessHandler;
    }
}
