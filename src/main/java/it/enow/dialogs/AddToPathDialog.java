package it.enow.dialogs;

import it.enow.IRegistryHandler;
import it.enow.JOptionProxy;
import it.enow.Verify;
import it.enow.windows.RegistryHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


/**
 * Created by alex on 2014-08-14.
 */
public class AddToPathDialog implements ActionListener {
    private JOptionProxy jopDependency;
    private Verify verify;
    private IRegistryHandler registryHandler;

    public AddToPathDialog(IRegistryHandler iRegistryHandler, Verify verify, JOptionProxy jOptionProxy) {
        this.jopDependency = jOptionProxy;
        this.registryHandler = iRegistryHandler;
        this.verify = verify;
    }

    public void inject(JOptionProxy jOptionProxy) {
        jopDependency = jOptionProxy;
    }

    public String promptUserForDirectory() {
        String s = jopDependency.showInputDialog(null, "Enter the path you wish to add to the path environment variable", "Add environment variable", jopDependency.INFORMATION_MESSAGE);
        return s;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = promptUserForDirectory();
        if (s == null) {
            return; //if user pressed cancel or close do nothing.
        }
        try {
            //todo tidify?
            //das magic
            registryHandler.setPathEnvironmentVariable(verify.verifyAndCleanPath(registryHandler.getPathEnvironmentVariable()+verify.cleanString(s)));
        } catch (IOException | InterruptedException | InvocationTargetException err) {
            jopDependency.showInputDialog(null, "Error Error", "https://www.youtube.com/watch?v=4aMD4uy-QGc&t=2m44s", jopDependency.ERROR_MESSAGE);
            err.printStackTrace();

        } catch (IllegalAccessException iae) {
            jopDependency.showMessageDialog(null, "Something is up with your rights!");
            iae.printStackTrace();
        }
    }


}
