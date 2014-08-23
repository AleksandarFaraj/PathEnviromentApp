package it.enow;

import javax.swing.*;
import java.awt.*;

/**
 * Created by alex on 2014-08-15.
 */
//Sole purpose is to automate testing But could also be used to make custom GUI
public class JOptionProxy {
    public static final int YES_OPTION = JOptionPane.YES_OPTION;
    public static final int NO_OPTION = JOptionPane.NO_OPTION;
    public static final int CLOSED_OPTION = JOptionPane.CLOSED_OPTION;

    public final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;

    public int showConfirmDialog(Component c, String message, String title, int type) {
        return JOptionPane.showConfirmDialog(c, message, title, type);
    }

    public String showInputDialog(Component c, String message, String title, int type) {
        return JOptionPane.showInputDialog(c, message, title, type);
    }

    public void showMessageDialog(Component c, String message) {
        JOptionPane.showMessageDialog(c, message);
    }
}
