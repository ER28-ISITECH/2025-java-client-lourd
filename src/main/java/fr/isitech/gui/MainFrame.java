package fr.isitech.gui;

import fr.isitech.model.Password;
import fr.isitech.service.DatabaseHelper;
import fr.isitech.service.PasswordService;
import fr.isitech.utils.Constants;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.SQLException;

public class MainFrame extends JFrame {
    private PasswordService passwordService;
    private JList<Password> passwordList;
    private DefaultListModel<Password> listModel;
    private PasswordPanel passwordPanel;
    private JTextField searchField;
    private JSplitPane splitPane;
    private JPanel rightPanel;
    private JButton addPasswordButton;

    public MainFrame(DatabaseHelper dbHelper) throws SQLException {
        super(Constants.APP_NAME + " - Password");
        passwordService = new PasswordService(dbHelper);
        listModel = new DefaultListModel<>();

        // Initialize UI components first
        initComponents();
        setupLayout();
        setupEvents();

        // Now refresh the list
        refreshList();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        passwordPanel = new PasswordPanel();
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(passwordPanel, BorderLayout.CENTER);
    }

    private void setupLayout() {
        // Left Panel: List + Search
        JPanel leftPanel = new JPanel(new BorderLayout());
        addPasswordButton = new JButton("Add Password");
        addPasswordButton.addActionListener(e -> {
            AddPasswordDialog dialog = new AddPasswordDialog(MainFrame.this, passwordService, listModel);
            dialog.setVisible(true);
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

        // Main layout
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);
        add(splitPane);
    }

    private void setupEvents() {
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
    }

    private void filterList() {
        String search = searchField.getText().toLowerCase();
        listModel.clear();
        try {
            for (Password pwd : passwordService.getAllPasswords()) {
                if (pwd.getTitle().toLowerCase().contains(search) ||
                        pwd.getUsername().toLowerCase().contains(search)) {
                    listModel.addElement(pwd);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to filter passwords: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (listModel.isEmpty()) {
            rightPanel.removeAll();
            rightPanel.revalidate();
            rightPanel.repaint();
        }
    }

    public void refreshList() {
        listModel.clear();
        try {
            for (Password pwd : passwordService.getAllPasswords()) {
                listModel.addElement(pwd);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to refresh passwords: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (listModel.isEmpty()) {
            rightPanel.removeAll();
            rightPanel.revalidate();
            rightPanel.repaint();
        }
    }
}
