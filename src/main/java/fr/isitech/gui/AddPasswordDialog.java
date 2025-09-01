package fr.isitech.gui;

import fr.isitech.Main;
import fr.isitech.model.Password;
import fr.isitech.service.PasswordService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddPasswordDialog extends JDialog {
    private JTextField titleField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private PasswordService passwordService;
    private DefaultListModel<Password> listModel;
    private MainFrame parent;

    public AddPasswordDialog(MainFrame parent, PasswordService passwordService, DefaultListModel<Password> listModel) {
        super(parent, "Add New Password", true);
        this.passwordService = passwordService;
        this.listModel = listModel;
        this.parent = parent;

        // Title
        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(20);

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        // Buttons
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        // Layout
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(cancelButton);
        panel.add(addButton);

        // Events
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPassword();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Dialog settings
        setContentPane(panel);
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void addPassword() {
        String title = titleField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (title.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String encryptedPassword = Main.stringEncryptor.encrypt(password);
            Password newPassword = new Password();
            newPassword.title = title;
            newPassword.username = username;
            newPassword.password = encryptedPassword;
            passwordService.addPassword(newPassword);
            parent.refreshList();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to add password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
