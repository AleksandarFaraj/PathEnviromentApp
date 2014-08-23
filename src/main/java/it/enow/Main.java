package it.enow;

import it.enow.windows.RegistryHandler;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event-dispatching thread:
        //adding TrayIcon.
        IRegistryHandler iRegistryHandler=null;
        if (SystemUtils.IS_OS_WINDOWS) {
            iRegistryHandler = new RegistryHandler();
        } else if (SystemUtils.IS_OS_LINUX) {
            //iRegistryHandler=new Linux.RegistryHandler();
            //
            // todo
            throw new UnsupportedOperationException("not yet implemented");
        } else {
            System.out.println("can't identify OS");
            return;
        }

        final Tray tray = new Tray(iRegistryHandler);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                tray.createAndShowGUI();
            }
        });
    }


}
