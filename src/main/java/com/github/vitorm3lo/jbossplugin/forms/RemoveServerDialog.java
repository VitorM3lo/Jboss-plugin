package com.github.vitorm3lo.jbossplugin.forms;

import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class RemoveServerDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JList<String> serverList;
    private JButton removeButton;

    private DefaultListModel<String> listModel;
    private List<String> selectedNames;

    public RemoveServerDialog(List<String> names) {
        super.setMinimumSize(new Dimension(500, 300));
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        listModel.addAll(names);

        if(selectedNames == null) {
            selectedNames = new ArrayList<>();
        }

        removeButton.addActionListener(e -> {
            selectedNames.add(listModel.remove(serverList.getSelectedIndex()));
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

    public List<String> showDialog() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        return selectedNames;
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
