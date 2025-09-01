package fr.isitech;

import fr.isitech.gui.LoginDialog;
import fr.isitech.gui.MainFrame;
import fr.isitech.gui.SetupDialog;
import fr.isitech.service.ConfigService;
import fr.isitech.service.DatabaseHelper;
import fr.isitech.service.StringEncryptor;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Objects;

public class Main {
    public static ConfigService configService;
    public static StringEncryptor stringEncryptor;
    public static String password;

    public static void main(String[] args) throws Exception {
        initServices();
        initConfig();
        run();
    }

    public static void initServices() throws Exception {
        configService = new ConfigService("app.properties");
    }

    public static void initConfig() {
        if (configService.getProperty("app.isSetup") == null) {
            configService.setProperty("app.isSetup", "false");
        }
        System.out.println("App isSetup: " + configService.getProperty("app.isSetup"));
    }

    public static void runSetup() {
        SwingUtilities.invokeLater(() -> {
            SetupDialog setupFrame = new SetupDialog(null);
            setupFrame.setVisible(true);
        });
    }

    public static void runLogin() {
        SwingUtilities.invokeLater(() -> {
            LoginDialog loginDialog = new LoginDialog(null);
            loginDialog.setVisible(true);
        });
    }

    public static void runMainApp() throws Exception {
        String encryptedKey = configService.getProperty("user.secretKey");
        if (encryptedKey == null) {
            String generatedKey = StringEncryptor.generateRandomKey(16);
            String encryptedAesKey = StringEncryptor.encryptWithPassword(generatedKey, password);
            configService.setProperty("user.secretKey", encryptedAesKey);
            configService.saveConfig();
            stringEncryptor = new StringEncryptor(generatedKey);
        } else {
            String aesKey = StringEncryptor.decryptWithPassword(encryptedKey, password);
            stringEncryptor = new StringEncryptor(aesKey);
        }
        System.out.println("App secretKey: " + configService.getProperty("app.secretKey"));
        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseHelper dbHelper = new DatabaseHelper();
                MainFrame mainFrame = new MainFrame(dbHelper);
                mainFrame.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Failed to initialize database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Objects.equals(configService.getProperty("app.isSetup"), "false")) {
            runSetup();
        } else {
            runLogin();
        }
    }
}
