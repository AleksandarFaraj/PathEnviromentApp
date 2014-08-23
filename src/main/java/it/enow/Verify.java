package it.enow;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alex on 2014-08-15.
 */
public class Verify {
    private JOptionProxy jopDependency;
    private IRegistryHandler iRegistryHandler;
    public Verify(IRegistryHandler iRegistryHandler) {
        jopDependency = new JOptionProxy();
        this.iRegistryHandler = iRegistryHandler;
    }

    public void inject(JOptionProxy jOptionProxy) {
        jopDependency = jOptionProxy;
    }

    public String environmentifyDirectory(String directory) {
        //todo has to be fixed for linux, will silently work,
        //is intended for windows but won't bother linux unless % are used
        Pattern pattern = Pattern.compile("%(.*?)%");
        Matcher matcher = pattern.matcher(directory);
        while (matcher.find()) {
            directory = directory.replace(matcher.group(), System.getenv(matcher.group(1)));
        }
        return directory;
    }

    public String verifyAndCleanDir(String directory) throws IOException {
        File pathToVerify = new File(environmentifyDirectory(directory));
        if (!pathToVerify.isDirectory()) {
            throw new IOException("directory is not a directory");
        }
        //gets us a real path a la windows fashion.
        return pathToVerify.getAbsolutePath();
    }

    public String verifyAndCleanPath(String path) {
        String[] paths = getAllDirectories(path);
        //ugly use to handle duplicates.
        LinkedHashMap<String,Integer> verifiedPaths = new LinkedHashMap<String,Integer>(){
            @Override
            public Integer put(String s, Integer i) {
                if (get(s)!=null) {
                    System.err.println("warning removed duplicate.");
                }
                return super.put(s,i);
            }
        };
        for (int i = 0; i < paths.length; i++) {
            try {
                verifiedPaths.put(verifyAndCleanDir(paths[i]),0);
            } catch (IOException | NullPointerException e) {
                //todo add some kind of storage to not bother users constantly
                switch (promptRemoveDirectoryFromPath(paths[i],e)) {
                    case JOptionProxy.YES_OPTION:
                        //do nothing
                        break;
                    case JOptionProxy.NO_OPTION:
                        //do not remove it by adding it
                        verifiedPaths.put(paths[i],1);
                        break;
                    case JOptionProxy.CLOSED_OPTION:
                        //todo Cancel whole sequence?
                        verifiedPaths.put(paths[i],1);
                        break;
                };

            }
        }
        return String.join(iRegistryHandler.getPathDelimiter(), verifiedPaths.keySet());
    }

    public int promptRemoveDirectoryFromPath(String directory,Exception e) {
        if (e instanceof NullPointerException) {
            return jopDependency.showConfirmDialog(null, String.format("Couldn't match the following path with any environment variables. Do you wish to remove it?\n%s", directory), "Remove invalid directory from path enviroment", JOptionPane.YES_NO_OPTION);
        } else if (e instanceof IOException) {
            return jopDependency.showConfirmDialog(null, String.format("The following path is invalid. Do you wish to remove it?\n%s", directory), "Remove invalid directory from path enviroment", JOptionPane.YES_NO_OPTION);
        }
        return 0;
    }
    public String cleanString(String pathToAdd) throws IOException {
        return pathToAdd.trim();
    }
    public String[] getAllDirectories(String path) {
        return path.split(iRegistryHandler.getPathDelimiter());
    }

}
