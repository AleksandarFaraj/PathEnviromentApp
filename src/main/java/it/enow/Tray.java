package it.enow;

import it.enow.IRegistryHandler;
import it.enow.JOptionProxy;
import it.enow.Main;
import it.enow.Verify;
import it.enow.dialogs.AddToPathDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Created by alex on 2014-08-14.
 */
public class Tray {
    private IRegistryHandler iRegistryHandler;
    private Verify verify;
    private JOptionProxy jOptionProxy;
    public Tray(IRegistryHandler iRegistryHandler) {
        this.iRegistryHandler = iRegistryHandler;
        this.verify=new Verify(iRegistryHandler);
        jOptionProxy= new JOptionProxy();
    }

    public void createAndShowGUI() {
        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(createImage("/eleph.jpg", "tray icon"));
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a popup menu components
        MenuItem addToPath = new MenuItem("Add To Path");
        MenuItem aboutItem = new MenuItem("About");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to popup menu
        popup.add(addToPath);
        popup.addSeparator();
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }



        ActionListener addToPathActionListener = new AddToPathDialog(iRegistryHandler,verify,jOptionProxy);


        trayIcon.addActionListener(addToPathActionListener);
        addToPath.addActionListener(addToPathActionListener);


        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "A small program for windows made to aid people edit their path variable. Made by Aleksandar Faraj. GPL License");
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }

    //Obtain the image URL
    private Image createImage(String path, String description) {
        URL imageURL = Main.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + imageURL);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
