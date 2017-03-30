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

  static final String testSourcePath = makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\"));
  static final String generatedSourcePath = makePath(System.getProperty("user.dir"), "src", "test", "java", packageName("\\\\"), "generated");
  static final String generatedClassPath = makePath(System.getProperty("user.dir"), "target", "classes", packageName("\\\\"), "generated");

  static String packageName(String seperator) {
    return new TestClassGenerator().getClass().getPackage().getName().replaceAll("\\.", seperator);
  }
}
