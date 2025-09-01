package fr.isitech;

import fr.isitech.gui.LoginDialog;
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
        // Initialisation des services
//            CryptageService cryptageService = new CryptageService();
//            FilePasswordDAO passwordDAO = new FilePasswordDAO(Constants.DATA_FILE, cryptageService);
//            PasswordService passwordService = new PasswordService(passwordDAO);

        // Vérification si c'est la première utilisation
//            boolean isFirstTime = !new File(Constants.DATA_FILE).exists();

        // Affichage du dialogue de connexion
//            LoginDialog loginDialog = new LoginDialog(null, false);
////            LoginDialog loginDialog = new LoginDialog(null, cryptageService, isFirstTime);
//            loginDialog.setVisible(true);
//
//            // Si l'authentification réussit, ouvrir l'application principale
//            if (loginDialog.isAuthenticated()) {
//                String masterPassword = loginDialog.getMasterPassword();
//
////                try {
////                    // Initialiser le service avec le mot de passe maître
////                    passwordService.setMasterPassword(masterPassword);
////
////                    // Ouvrir la fenêtre principale
////                    MainFrame mainFrame = new MainFrame(passwordService);
////                    mainFrame.setVisible(true);
////
////                } catch (Exception e) {
////                    JOptionPane.showMessageDialog(null,
////                            "Erreur lors du chargement des données : " + e.getMessage(),
////                            "Erreur", JOptionPane.ERROR_MESSAGE);
////                    System.exit(1);
////                }
//            } else {
//                // L'utilisateur a annulé ou l'authentification a échoué
//                System.exit(0);
//            }
//    });
}
