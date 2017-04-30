package il.org.spartan.spartanizer.research.metatester;

/** TODO orenafek: document class
 * @author orenafek <tt>oren.afek@gmail.com</tt>
 * @since 2017-03-30 */
public class FileUtils {
  static final String FS = System.getProperty("file.separator");

  static String makePath(final String... dirs) {
    final StringBuilder $ = new StringBuilder();
    for (final String dir : dirs)
      $.append(dir).append(FS);
    return $.substring(0, $.length() - 1);
  }

  static String testSourcePath(final Class<?> current) {
    return makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\", current));
  }

  static String generatedSourcePath(final Class<?> current) {
    return makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\", current));
  }

  static String generatedClassPath(final Class<?> current) {
    return makePath(System.getProperty("user.dir"), "target", "classes", packageName("\\\\", current));
  }

  static String packageName(final String seperator, final Class<?> current) {
    return current.getPackage().getName().replaceAll("\\.", seperator);
  }
}
