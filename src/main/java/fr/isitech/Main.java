package fr.isitech;

import fr.isitech.gui.LoginDialog;
import fr.isitech.gui.MainFrame;
import fr.isitech.gui.SetupDialog;
import fr.isitech.service.ConfigService;

import javax.swing.*;
import java.util.Objects;

public class Main {
    public static ConfigService configService;

    public static void main(String[] args) {
        initServices();
        initConfig();
        run();
    }

    public static void initServices() {
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

    public static void runMainApp() {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new MainFrame(null);
            mainFrame.setVisible(true);
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
