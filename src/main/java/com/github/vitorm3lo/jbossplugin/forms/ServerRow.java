package com.github.vitorm3lo.jbossplugin.forms;

import com.github.vitorm3lo.jbossplugin.model.Instance;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.openapi.application.ApplicationStarter;
import com.intellij.openapi.application.ApplicationStarterBase;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.ProjectImpl;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServerRow extends JComponent {
    private JButton debugButton;
    private JButton deployButton;
    private JButton runButton;
    private JPanel serverRow;
    private JLabel serverNameLabel;

    private Instance instance;

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
        // use normal File and Path operations to copy the files
    }

    private void runServer() {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("java");
        cmds.add("--version");

        try {
            new ProcessBuilder("java", "--version").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        GeneralCommandLine generalCommandLine = new GeneralCommandLine(cmds);
//        generalCommandLine.setCharset(StandardCharsets.UTF_8);
//
//        GeneralCommandLine commandLine = new GeneralCommandLine(cmds);
//        OSProcessHandler processHandler = null;
//        try {
//            processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine);
//            ProcessTerminatedListener.attach(processHandler);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

//        Runtime rt = Runtime.getRuntime();
//        try {
//            rt.exec("cmd \\c java --version");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void startDebugSession() {
        runServer();
    }

}
