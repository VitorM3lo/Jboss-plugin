package com.github.vitorm3lo.jbossplugin.forms;

import com.github.vitorm3lo.jbossplugin.model.Instance;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddServerDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JButton searchButton;
    private JList<String> deployableList;
    private JButton removeButton;
    private JButton addButton;
    private JTextField serverNameField;
    private JTextField debugPortTextField;

    private File deployableFolder;
    private List<File> deployableFiles;
    private DefaultListModel<String> listModel;
    private final List<String> serverNames;
    private Instance instance;

    public AddServerDialog(List<String> names, Instance instance) {
        this(names);
        this.instance = instance;
        debugPortTextField.setText(instance.getDebugPort() + "");
        serverNameField.setText(instance.getServerName());
        textField1.setText(instance.getServerPath().getAbsolutePath());
        listModel.addAll(instance.getDeployablePath().stream().map(File::getAbsolutePath).collect(Collectors.toList()));
        deployableFolder = instance.getServerPath();
        deployableFiles = instance.getDeployablePath();
    }

    public AddServerDialog(List<String> names) {
        deployableFiles = new ArrayList<>();
        serverNames = names;

        setContentPane(contentPane);
        super.setMinimumSize(new Dimension(500, 300));
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Deployable files", "war", "jar", "ear");
                jFileChooser.setFileFilter(extensionFilter);
                jFileChooser.setMultiSelectionEnabled(true);
                int returnVal = jFileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    deployableFiles.addAll(Arrays.asList(jFileChooser.getSelectedFiles()));
                    listModel.addAll(Arrays.stream(jFileChooser.getSelectedFiles()).map(File::getAbsolutePath).collect(Collectors.toList()));
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selected = deployableList.getSelectedIndices();
                for (int index : selected) {
                    listModel.remove(index);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setMultiSelectionEnabled(false);
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = jFileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    deployableFolder = jFileChooser.getSelectedFile();
                    textField1.setText(jFileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if (instance == null) {
            if (this.serverNames != null && serverNames.contains(this.serverNameField.getText())) {
                JOptionPane.showMessageDialog(null,
                        "Choose a different name for easy identification",
                        "Equal server name",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            if (!instance.getServerName().equals(serverNameField.getText()) && this.serverNames != null && serverNames.contains(this.serverNameField.getText())) {
                JOptionPane.showMessageDialog(null,
                        "Choose a different name for easy identification",
                        "Equal server name",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        if (this.serverNameField == null || this.serverNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "It's required a server name.",
                    "No server name",
                    JOptionPane.WARNING_MESSAGE);
        } else if (deployableFolder == null) {
            JOptionPane.showMessageDialog(null,
                    "It's required a deployable folder.",
                    "No deployable folder",
                    JOptionPane.WARNING_MESSAGE);
        } else if (deployableFiles == null || deployableFiles.size() == 0) {
            JOptionPane.showMessageDialog(null,
                    "It's required at least one deployable file.",
                    "No deployable file",
                    JOptionPane.WARNING_MESSAGE);
        } else if (debugPortTextField.getText() == null || debugPortTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "It's required a debug port.",
                    "No debug port",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            int port = -1;
            try {
                if (!debugPortTextField.getText().isEmpty()) {
                    port = Integer.parseInt(debugPortTextField.getText());
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Debug port format is wrong, introduce a number (-1 - disable debug mode)",
                        "Debug port error",
                        JOptionPane.WARNING_MESSAGE);
            }
            if (port > 1024 && port < 65535 || port == -1) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Debug port too low, insert a port between 1024 and 65535 or -1 to disable debug mode",
                        "Debug port error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public Instance showDialog() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        return new Instance(this.serverNameField.getText(), Integer.parseInt(debugPortTextField.getText()), deployableFolder, deployableFiles);
    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        listModel = new DefaultListModel<>();
        deployableList = new JBList<>(listModel);
    }
}
