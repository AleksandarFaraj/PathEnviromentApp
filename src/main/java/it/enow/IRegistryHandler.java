package it.enow;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by alex on 2014-08-15.
 */
public interface IRegistryHandler {

    //appends string to the path.
    String getPathEnvironmentVariable() throws IOException, InterruptedException, InvocationTargetException, IllegalAccessException;
    String getPathDelimiter();
    void setPathEnvironmentVariable(String path) throws InterruptedException, InvocationTargetException, IllegalAccessException, IOException;
}
