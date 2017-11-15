package metatester.aux_layer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static metatester.TestClassGenerator.JAVA_SUFFIX;

/**
 * Utility function for file operations
 *
 * @author orenafek <tt>oren.afek@gmail.com</tt>
 * @since 30/3/17
 */
public class FileUtils {
    public static final String FS = System.getProperty("file.separator");
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final List<String> possibleSourceTestPathPrefixes = Arrays.asList(
            makePath(USER_DIR, "src", "test"),
            makePath(USER_DIR, "src", "test", "main"),
            makePath(USER_DIR, "src", "test", "main", "java"),
            makePath(USER_DIR, "src", "test", "java", "main"),
            makePath(USER_DIR, "src", "test", "java"),
            makePath(USER_DIR, "test"),
            makePath(USER_DIR, "test", "main"),
            makePath(USER_DIR, "test", "main", "java"),
            makePath(USER_DIR, "test", "java", "main"),
            makePath(USER_DIR, "test", "java"),
            makePath(USER_DIR)
    );
    private static final List<String> possibleBinaryTestPathPrefixes = Arrays.asList(
            getBinarisPath(),
            makePath(USER_DIR, "target", "classes"),
            //makePath(makePath(USER_DIR, "build", "classes", "main")),
            makePath(USER_DIR, "build", "classes", "test"),
            makePath(USER_DIR, "out", "test"),
            makePath(USER_DIR, "out", "test", "classes")
    );

    /**
     * combining strings separated by {@link {{@link #FS}} FileSeparator}
     *
     * @param ¢ strings
     * @return comined strings
     */
    public static String makePath(final String... ¢) {
        final StringBuilder $ = new StringBuilder();
        for (final String dir : ¢)
            $.append(dir).append(FS);
        return $.substring(0, $.length() - 1);
    }

    private static boolean tryPath(String cent) {
        return new File(cent).exists();
    }

    private static String searchForPathOfClass(List<String> prefixes, final Class<?> cls) {
        return prefixes.stream()
                .map(prefix -> makePath(prefix, packageName("\\\\", cls), cls.getSimpleName() + ".java"))
                .filter(FileUtils::tryPath)
                .findFirst()
                .orElse("");
    }

    /**
     * Class's source's file path on file system.
     *
     * @param ¢ class
     * @return path
     */
    public static String testSourcePath(final Class<?> ¢) {
        return searchForPathOfClass(possibleSourceTestPathPrefixes, ¢);
    }

    /**
     * New Generated test source path from Class's source's file on file system.
     *
     * @param ¢ class
     * @return path
     */
    public static String generatedSourcePath(final Class<?> ¢) {
        return addMetaSuffix(testSourcePath(¢), ¢);

    }

    /**
     * New Generated test binary path from Class's source's file on file system.
     *
     * @param ¢ class
     * @return path
     */
    public static String generatedClassPath(final Class<?> ¢) {
        //return makePath(path, ¢.getSimpleName() + "_Meta.java");
        return possibleBinaryTestPathPrefixes.stream()
                .filter(FileUtils::tryToWrite)
                //.map(prefix -> makePath(prefix, packageName("\\\\", ¢)))
                .findFirst()
                .orElse("");
    }

    private static boolean tryToWrite(String cent) {
        try {
            File f = new File(cent);
            if (f.exists() && f.isDirectory())
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * Package name of a class
     *
     * @param seperator File System's file separator (/ for unix and \ for windows)
     * @param current   class.
     * @return packgae name.
     */
    public static String packageName(final String seperator, final Class<?> current) {
        Package p = current.getPackage();
        return p != null ? p.getName().replaceAll("\\.", seperator) : "";
    }

    private static String addMetaSuffix(String path, Class<?> ¢) {
        int ios = path.lastIndexOf(FS);
        return ios == -1 ? path : path.substring(0, ios + 1) + ¢.getSimpleName() + "_Meta" + ".java";
    }

    private static String getBinarisPath() {
        return makePath(USER_DIR, JSONReader.create(USER_DIR).read("testBinaryRoot"));
    }

    public static File removeFileName(File f) {
        return new File(removeFileName(f.getName()));
    }

    public static String removeFileName(String fileName) {
        if (!fileName.endsWith(JAVA_SUFFIX)) {
            return fileName;
        }

        File f = new File(fileName);
        return f.exists() ? f.getParent() : fileName;
    }

    public static String readAll(final File f) {
        final StringBuilder $ = new StringBuilder();
        try (final BufferedReader linesStream = new BufferedReader(new FileReader(f))) {
            String line = linesStream.readLine();
            for (; line != null; ) {
                $.append(line).append("\n");
                line = linesStream.readLine();
            }
            return $ + "";
        } catch (final IOException x) {/**/}

        return "";
    }
}
