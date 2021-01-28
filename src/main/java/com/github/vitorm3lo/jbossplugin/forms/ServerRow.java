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

        runButton.addActionListener(e -> runServer());
        debugButton.addActionListener(e -> startDebugSession());
        deployButton.addActionListener(e -> deployServer());
    }

    public JPanel getServerRow() {
        return serverRow;
    }

    private void deployServer() {
        boolean hasError = false;
        File dir = new File(instance.getServerPath().getAbsolutePath());
        File[] files = dir.listFiles((d, name) -> name.endsWith(".ear") || name.endsWith(".war") || name.endsWith(".jar"));
        for (File file : files) {
            try {
                Files.delete(Paths.get(file.toURI()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File deployable : instance.getDeployablePath()) {
            try {
                Files.copy(Paths.get(deployable.toURI()), Paths.get(instance.getServerPath().getAbsolutePath(), deployable.getName()), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                hasError = true;
                e.printStackTrace();
            }
        }
        if (hasError) {
            JOptionPane.showMessageDialog(null, "Some files were not deployed", "Error deploying", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Files deployed!", "", JOptionPane.INFORMATION_MESSAGE);
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
        runProcessHandler = commandLineService.runServer(jbossPath.toAbsolutePath().toString(), instance.getServerName(), instance.getDebugPort(), ProjectUtil.getActiveProject());
        runButton.setText("Stop");
    }

    private void startDebugSession() {
        if (instance.getDebugPort() == -1) {
            JOptionPane.showMessageDialog(null,
                    "Debug port is not set, debug session stopped",
                    "Debug port error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        DebugWindowService.attach(ProjectUtil.getActiveProject(),
                new RemoteConnection(true, "localhost", "" + instance.getDebugPort(), false),
                instance.getServerName());
    }

    public ProcessHandler getRunProcessHandler() {
        return runProcessHandler;
    }
}
