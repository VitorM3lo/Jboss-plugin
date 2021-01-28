package com.github.vitorm3lo.jbossplugin.forms;

import com.github.vitorm3lo.jbossplugin.model.Instance;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class EditServerDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JList<String> serverList;
    private JButton editButton;

    private DefaultListModel<String> listModel;
    private Instance selectedInstance;

    public EditServerDialog(List<Instance> instances) {
        super.setMinimumSize(new Dimension(500, 300));
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        List<String> names = instances.stream().map(Instance::getServerName).collect(Collectors.toList());
        listModel.addAll(names);

        editButton.addActionListener(e -> {
            for (Instance instance : instances) {
                if (instance.getServerName().equals(listModel.get(serverList.getSelectedIndex()))) {
                    selectedInstance = instance;
                    break;
                }
            }
            if (selectedInstance != null) {
                AddServerDialog dialog = new AddServerDialog(names, selectedInstance);
                Instance modifiedInstance = dialog.showDialog();
                if (!selectedInstance.equals(modifiedInstance)) {
                    selectedInstance.setServerName(modifiedInstance.getServerName());
                    selectedInstance.setDebugPort(modifiedInstance.getDebugPort());
                    selectedInstance.setServerPath(modifiedInstance.getServerPath());
                    selectedInstance.setDeployablePath(modifiedInstance.getDeployablePath());
                    List<String> newNames = instances.stream().map(Instance::getServerName).collect(Collectors.toList());
                    listModel.removeAllElements();
                    listModel.addAll(newNames);
                }
            }

        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onOK();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void showDialog() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onOK() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        listModel = new DefaultListModel<>();
        serverList = new JBList<>(listModel);
        serverList.setDragEnabled(false);
        serverList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
    }
}
