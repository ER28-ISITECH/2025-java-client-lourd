package fr.isitech.gui;

import fr.isitech.utils.Constants;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(JFrame parent) {
        super(Constants.APP_NAME + " - Password");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

}
