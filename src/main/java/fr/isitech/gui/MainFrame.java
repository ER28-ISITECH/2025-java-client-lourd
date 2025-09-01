package fr.isitech.gui;

import fr.isitech.model.Password;
import fr.isitech.service.PasswordService;
import fr.isitech.utils.Constants;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class MainFrame extends JFrame {
    private PasswordService passwordService;
    private JList<Password> passwordList;
    private DefaultListModel<Password> listModel;
    private PasswordPanel passwordPanel;
    private JTextField searchField;
    private JSplitPane splitPane;
    private JPanel rightPanel;
    private JButton addPasswordButton;

    public MainFrame() {
        super(Constants.APP_NAME + " - Password");
        passwordService = new PasswordService();
        listModel = new DefaultListModel<>();
        for (Password pwd : passwordService.getAllPasswords()) {
            listModel.addElement(pwd);
        }

        // Left Panel: List + Search
        JPanel leftPanel = new JPanel(new BorderLayout());

        addPasswordButton = new JButton("Add Password");
        addPasswordButton.addActionListener(e -> {
            // Open a dialog to add a new password (not implemented)
            JOptionPane.showMessageDialog(this, "Add Password functionality not implemented.");
        });
        leftPanel.add(addPasswordButton, BorderLayout.SOUTH);

        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterList();
            }
        });
        leftPanel.add(searchField, BorderLayout.NORTH);

        passwordList = new JList<>(listModel);
        passwordList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Password) {
                    Password pwd = (Password) value;
                    setText(pwd.getTitle() + " (" + pwd.getUsername() + ")");
                }
                return this;
            }
        });
        leftPanel.add(new JScrollPane(passwordList), BorderLayout.CENTER);

        // Right Panel: Password details
        passwordPanel = new PasswordPanel();
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(passwordPanel, BorderLayout.CENTER);

        // Main layout
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        add(splitPane);

        // Event: show password details when selected
        passwordList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Password selected = passwordList.getSelectedValue();
                if (selected == null) {
                    rightPanel.removeAll();
                    rightPanel.revalidate();
                    rightPanel.repaint();
                } else {
                    rightPanel.removeAll();
                    rightPanel.add(passwordPanel, BorderLayout.CENTER);
                    passwordPanel.setPassword(selected);
                    rightPanel.revalidate();
                    rightPanel.repaint();
                }
            }
        });

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void filterList() {
        String search = searchField.getText().toLowerCase();
        listModel.clear();
        for (Password pwd : passwordService.getAllPasswords()) {
            if (pwd.getTitle().toLowerCase().contains(search) ||
                    pwd.getUsername().toLowerCase().contains(search)) {
                listModel.addElement(pwd);
            }
        }
        if (listModel.isEmpty()) {
            rightPanel.removeAll();
            rightPanel.revalidate();
            rightPanel.repaint();
        }
    }
}
