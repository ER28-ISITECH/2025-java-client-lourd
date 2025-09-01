package fr.isitech.gui;

import fr.isitech.Main;
import fr.isitech.model.Password;

import javax.swing.*;
import java.awt.*;

public class PasswordPanel extends JPanel {
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    public PasswordPanel() {
        setLayout(new GridLayout(3, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel();
        usernameLabel = new JLabel();
        passwordLabel = new JLabel();

        add(titleLabel);
        add(usernameLabel);
        add(passwordLabel);
    }

    public void setPassword(Password password) {
        titleLabel.setText("Title: " + password.title);
        usernameLabel.setText("Username: " + password.username);
        try {
            String decryptedPassword = Main.stringEncryptor.decrypt(password.password);
            passwordLabel.setText("Password: " + decryptedPassword);
        } catch (Exception e) {
            passwordLabel.setText("Password: [Error]");
        }
    }
}
