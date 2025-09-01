package fr.isitech.gui;

import fr.isitech.Main;
import fr.isitech.utils.Constants;
import fr.isitech.utils.PasswordHasher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class LoginDialog extends JDialog {
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginDialog(JFrame parent) {
        super(parent, Constants.APP_NAME + " - Connexion", true);
        initComponents();
        setupLayout();
        setupEvents();
        setupWindow();
    }

    private void setupWindow() {
        setSize(400, 250);
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        SwingUtilities.invokeLater(() -> {
            passwordField.requestFocusInWindow();
        });
    }

    private void setupEvents() {
        loginButton.addActionListener(e -> {
            char[] password = passwordField.getPassword();
            if (password.length < 8) {
                JOptionPane.showMessageDialog(this, "Le mot de passe doit contenir au moins 8 caractères.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Main.password = Arrays.toString(password);

            String storedHash = Main.configService.getProperty("user.password");
            if (!PasswordHasher.verifyPassword(Arrays.toString(password), storedHash)) {
                JOptionPane.showMessageDialog(this, "Mot de passe incorrect.", "Erreur", JOptionPane.ERROR_MESSAGE);
                Arrays.fill(password, '0'); // Effacer le mot de passe du tableau
                return;
            }
            JOptionPane.showMessageDialog(this, "Connexion réussie!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            try {
                Main.runMainApp();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Titre
        JLabel titleLabel = new JLabel(Constants.APP_NAME);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Instruction
        gbc.gridy = 1;
        JLabel instructionLabel = new JLabel("Entrez votre mot de passe maître :");
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(instructionLabel, gbc);

        // Champ mot de passe
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);

        // Bouton connexion
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void initComponents() {
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Se connecter");
    }
}
