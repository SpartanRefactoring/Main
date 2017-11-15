package metatester.aux_layer;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * A Modified Class Loader for reloading fresh classes that have just been compiled in runtime.
 * While the default Java's Class loader, doesn't reload a class if its already been loaded, this class loader
 * reloads a compiled class and its inners from a file in the file system.
 */
public class SophisticatedClassLoader extends URLClassLoader {

    public SophisticatedClassLoader(URL[] urls) {
        super(urls);
    }

    /**
     * Loads a comipled meta class and its inner clases as well.
     *
     * @param className the meta classes name (e.g. for tests.Test => tests.Test_Meta)
     * @param binary    the compiled binary of the <b>enclosing meta class</b>
     * @return a Java class object representing the class that has been loaded.
     * @throws ClassNotFoundException in case the given class name doesn't exist or it doesn't fits the binary provided.
     */
    public Class<?> loadMetaClass(String className, File binary) throws ClassNotFoundException {
        try {

            URL myUrl = binary.toURI().toURL();
            loadInners(className, binary, myUrl);
            return loadSingleClass(className, myUrl);

        } catch (IOException ignore) { /**/ }

        return Object.class;
    }

    private Class<?> loadSingleClass(String className, URL enclosingClassURL) {

        try {
            URLConnection connection = enclosingClassURL.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while (data != -1) {
                buffer.write(data);
                data = input.read();
            }

            input.close();

            byte[] classData = buffer.toByteArray();

            return defineClass(className, classData, 0, classData.length);
        } catch (IOException ignore) {
        }

        return Object.class;
    }

    private void loadInners(String className, File binary, URL rootUrl) {
        Arrays.stream(binary.getParentFile().listFiles((dir, name) ->
                !name.equals(className) &&
                        name.startsWith(className)))
                .forEach(c -> loadSingleClass(c.getName(), rootUrl));

    }


}

