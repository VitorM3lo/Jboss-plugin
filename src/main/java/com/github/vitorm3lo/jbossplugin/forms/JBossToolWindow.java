package com.github.vitorm3lo.jbossplugin.forms;

import com.github.vitorm3lo.jbossplugin.model.Instance;
import com.github.vitorm3lo.jbossplugin.model.InstanceState;
import com.github.vitorm3lo.jbossplugin.services.PersistenceService;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.wm.ToolWindow;
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

    private List<Instance> instanceList;
    private final PersistenceService persistenceService;

    public JBossToolWindow(ToolWindow toolWindow) {
        if (instanceList == null) {
            instanceList = new ArrayList<>();
        }

        persistenceService = PersistenceService.getInstance();
        // restore data
        if (persistenceService.getState() != null) {
            instanceList.addAll(
                    persistenceService.getState().instanceState.stream().map(
                            is -> new Instance(is.serverName, new File(is.serverPath), is.deployablePath.stream()
                                    .map(File::new).collect(Collectors.toList())))
                            .collect(Collectors.toList()));
        }

        for (Instance i : instanceList) {
            JPanel row = new ServerRow(i).getServerRow();
            row.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
            scrollPanel.add(row);
            scrollPanel.revalidate();
            scrollPanel.repaint();
        }

        addJBossServerButton.addActionListener(e -> {
            AddServerDialog dialog = new AddServerDialog(instanceList.stream().map(Instance::getServerName).collect(Collectors.toList()));
            Instance instance = dialog.showDialog();
            if (instance.getServerPath() != null && instance.getDeployablePath() != null && instance.getDeployablePath().size() > 0) {
                instanceList.add(instance);
                // save data after creating new - TODO: place in a new method to ease adding and removing
                updateInstancesState();
                JPanel row = new ServerRow(instance).getServerRow();
                row.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
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
                updateInstancesState();
            }
        });
    }

    private void updateInstancesState() {
        if (persistenceService.getState() != null) {
            if (persistenceService.getState().instanceState == null) {
                persistenceService.getState().instanceState = new ArrayList<>();
            }
            persistenceService.getState().instanceState =
                    instanceList.stream().map(i ->
                            new InstanceState(i.getServerName(), i.getServerPath(), i.getDeployablePath()))
                            .collect(Collectors.toList());
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
