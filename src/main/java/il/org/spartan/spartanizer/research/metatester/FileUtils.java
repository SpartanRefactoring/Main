package il.org.spartan.spartanizer.research.metatester;

/** TODO orenafek: document class
 * @author orenafek <tt>oren.afek@gmail.com</tt>
 * @since 2017-03-30 */
public class FileUtils {
  static final String FS = System.getProperty("file.separator");

  static String makePath(String... dirs) {
    StringBuilder $ = new StringBuilder();
    for (String dir : dirs)
      $.append(dir).append(FS);
    return $.substring(0, $.length() - 1);
  }

  static String testSourcePath(Class<?> current) {
    return makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\", current));
  }

  static String generatedSourcePath(Class<?> current) {
    return makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\", current));
  }

  static String generatedClassPath(Class<?> current) {
    return makePath(System.getProperty("user.dir"), "target", "classes", packageName("\\\\", current));
  }

  static String packageName(String seperator, Class<?> current) {
    return current.getPackage().getName().replaceAll("\\.", seperator);
  }
}
