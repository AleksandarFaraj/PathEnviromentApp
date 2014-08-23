package it.enow.windows;

import it.enow.IRegistryHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by alex on 2014-08-15.
 */
public class RegistryHandler implements IRegistryHandler {
    //appends string to the path.
    @Override
    public String getPathEnvironmentVariable() throws IOException, InterruptedException, InvocationTargetException, IllegalAccessException {
        String value = WinRegistry.readString(
                WinRegistry.HKEY_CURRENT_USER,  //HKEY
                "Environment",                  //Key
                "Path");                        //ValueName
        if (value == null) {
            return "";
        }
        return value + getPathDelimiter();
    }

    @Override
    public String getPathDelimiter() {
        return ";";
    }

    @Override
    public void setPathEnvironmentVariable(String path) throws InterruptedException, InvocationTargetException, IllegalAccessException, IOException {
        WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER,
                "Environment",
                "Path",
                path);
    }
}
