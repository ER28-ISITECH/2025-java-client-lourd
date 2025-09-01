package fr.isitech.gui;

import fr.isitech.model.Password;
import javax.swing.*;
import java.awt.*;

public class PasswordPanel extends JPanel {
    private JLabel titleLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    public PasswordPanel() {
        setLayout(new GridLayout(3, 2, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("Title: ");
        usernameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");

        add(titleLabel);
        add(new JLabel());
        add(usernameLabel);
        add(new JLabel());
        add(passwordLabel);
        add(new JLabel());
    }

    public void setPassword(Password pwd) {
        if (pwd == null) {
            titleLabel.setText("Title: ");
            usernameLabel.setText("Username: ");
            passwordLabel.setText("Password: ");
        } else {
            titleLabel.setText("Title: " + pwd.getTitle());
            usernameLabel.setText("Username: " + pwd.getUsername());
            passwordLabel.setText("Password: " + pwd.getPassword());
        }
    }
}
