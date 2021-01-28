package com.github.vitorm3lo.jbossplugin.forms;

import com.github.vitorm3lo.jbossplugin.listeners.ProjectListener;
import com.github.vitorm3lo.jbossplugin.model.Instance;
import com.github.vitorm3lo.jbossplugin.services.PersistenceService;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JBossToolWindow {
    private JPanel jbossToolWindow;
    private JButton addJBossServerButton;
    private JScrollPane scrollPane;
    private JPanel scrollPanel;
    private JButton removeJBossServerButton;
    private JButton editJBossServerButton;

    private List<Instance> instanceList;
    private final PersistenceService persistenceService;
    private final ProjectListener projectListener;

    public JBossToolWindow() {
        if (instanceList == null) {
            instanceList = new ArrayList<>();
        }

        projectListener = ProjectListener.getInstance();
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(ProjectManager.TOPIC, projectListener);

        persistenceService = PersistenceService.getInstance();
        // restore data
        if (persistenceService.getState() != null) {
            instanceList.addAll(
                    persistenceService.getState().instanceState.stream().map(is ->
                            new Instance(is.serverName, is.debugPort, new File(is.serverPath), is.deployablePath.stream()
                                    .map(File::new).collect(Collectors.toList()))
                    ).collect(Collectors.toList()));
        }

        updateList(instanceList);

        addJBossServerButton.addActionListener(e -> {
            AddServerDialog dialog = new AddServerDialog(instanceList.stream().map(Instance::getServerName).collect(Collectors.toList()));
            Instance instance = dialog.showDialog();
            if (instance.getServerPath() != null && instance.getDeployablePath() != null && instance.getDeployablePath().size() > 0) {
                instanceList.add(instance);
                // save data after creating new
                persistenceService.updateInstancesState(instanceList);
                ServerRow serverRow = new ServerRow(instance);
                JPanel row = serverRow.getServerRow();
                row.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
                projectListener.addRow(serverRow);
                scrollPanel.add(row);
                scrollPanel.revalidate();
                scrollPanel.repaint();
            }
        });

        removeJBossServerButton.addActionListener(e -> {
            RemoveServerDialog dialog = new RemoveServerDialog(instanceList.stream().map(Instance::getServerName).collect(Collectors.toList()));
            List<String> serverNames = dialog.showDialog();
            if (serverNames != null && serverNames.size() > 0 && instanceList != null && instanceList.size() > 0) {
                for (String serverName : serverNames) {
                    for (int i = 0; i < instanceList.size(); i++) {
                        Instance instance = instanceList.get(i);
                        if (instance.getServerName().equals(serverName)) {
                            scrollPanel.remove(i);
                            instanceList.remove(i);
                            break;
                        }
                    }
                }
                persistenceService.updateInstancesState(instanceList);
            }
        });

        editJBossServerButton.addActionListener(e -> {
            EditServerDialog dialog = new EditServerDialog(instanceList);
            dialog.showDialog();
            persistenceService.updateInstancesState(instanceList);
            updateList(instanceList);
        });
    }

    private void updateList(List<Instance> instanceList) {
        Component[] components = scrollPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            scrollPanel.remove(i);
        }
        for (Instance i : instanceList) {
            ServerRow serverRow = new ServerRow(i);
            JPanel row = serverRow.getServerRow();
            row.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
            projectListener.addRow(serverRow);
            scrollPanel.add(row);
            scrollPanel.revalidate();
            scrollPanel.repaint();
        }
    }

    public JPanel getContent() {
        return jbossToolWindow;
    }

    private void createUIComponents() {
        scrollPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(scrollPanel, BoxLayout.PAGE_AXIS);
        scrollPanel.setLayout(boxLayout);
        scrollPane = new JBScrollPane(scrollPanel);
    }
}
