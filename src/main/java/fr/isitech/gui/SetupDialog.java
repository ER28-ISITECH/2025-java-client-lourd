package fr.isitech.gui;

import fr.isitech.Main;
import fr.isitech.utils.Constants;
import fr.isitech.utils.PasswordHasher;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class SetupDialog extends JDialog {
    private JPasswordField passwordField;
    private JButton initButton;
    private JLabel[] requirementLabels;

    public SetupDialog(JFrame parent) {
        super(parent, Constants.APP_NAME + " - Authentication", true);
        initComponents();
        setupLayout();
        setupEvents();
        setupWindow();
    }

    private void setupWindow() {
        setSize(400, 350);
        setResizable(false);
        pack();
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        SwingUtilities.invokeLater(() -> {
            passwordField.requestFocusInWindow();
        });
    }

    private void setupEvents() {
        DocumentListener passwordListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRequirements();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRequirements();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRequirements();
            }
        };
        passwordField.getDocument().addDocumentListener(passwordListener);

        initButton.addActionListener(e -> {
            char[] password = passwordField.getPassword();
            if (!isPasswordValid(password)) {
                JOptionPane.showMessageDialog(this, "Le mot de passe ne répond pas à tous les critères.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String passwordHash = PasswordHasher.hashPassword(Arrays.toString(password));
            Main.configService.setProperty("user.password", passwordHash);
            JOptionPane.showMessageDialog(this, "Mot de passe maître initialisé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            Main.configService.setProperty("app.isSetup", "true");
            try {
                Main.configService.saveConfig();
                Main.runLogin();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    initButton.doClick();
                }
            }
        });
    }

    private boolean isPasswordValid(char[] password) {
        String pass = new String(password);
        boolean hasMinLength = pass.length() >= 8;
        boolean hasLower = Pattern.compile("[a-z]").matcher(pass).find();
        boolean hasUpper = Pattern.compile("[A-Z]").matcher(pass).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(pass).find();
        boolean hasSpecial = Pattern.compile("[^a-zA-Z0-9]").matcher(pass).find();
        return hasMinLength && hasLower && hasUpper && hasDigit && hasSpecial;
    }

    private void updateRequirements() {
        char[] password = passwordField.getPassword();
        String pass = new String(password);
        boolean hasMinLength = pass.length() >= 8;
        boolean hasLower = Pattern.compile("[a-z]").matcher(pass).find();
        boolean hasUpper = Pattern.compile("[A-Z]").matcher(pass).find();
        boolean hasDigit = Pattern.compile("[0-9]").matcher(pass).find();
        boolean hasSpecial = Pattern.compile("[^a-zA-Z0-9]").matcher(pass).find();

        requirementLabels[0].setForeground(hasMinLength ? Color.GREEN : Color.RED);
        requirementLabels[1].setForeground(hasLower ? Color.GREEN : Color.RED);
        requirementLabels[2].setForeground(hasUpper ? Color.GREEN : Color.RED);
        requirementLabels[3].setForeground(hasDigit ? Color.GREEN : Color.RED);
        requirementLabels[4].setForeground(hasSpecial ? Color.GREEN : Color.RED);
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
        JLabel instructionLabel = new JLabel("Initialisez votre mot de passe maître :");
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

        // Liste des exigences
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel requirementsPanel = new JPanel(new GridLayout(5, 1));
        requirementsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        requirementLabels = new JLabel[5];
        requirementLabels[0] = new JLabel("• Au moins 8 caractères");
        requirementLabels[1] = new JLabel("• Au moins une lettre minuscule");
        requirementLabels[2] = new JLabel("• Au moins une lettre majuscule");
        requirementLabels[3] = new JLabel("• Au moins un chiffre");
        requirementLabels[4] = new JLabel("• Au moins un caractère spécial");
        for (JLabel label : requirementLabels) {
            label.setForeground(Color.RED);
            requirementsPanel.add(label);
        }
        mainPanel.add(requirementsPanel, gbc);

        // Bouton initializer
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(initButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void initComponents() {
        passwordField = new JPasswordField(20);
        initButton = new JButton("Initialiser");
    }
}
