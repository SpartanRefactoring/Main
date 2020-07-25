// <a href=http://ssdl-linux.cs.technion.ac.il/wiki/index.php>SSDLPedia</a>
package il.org.spartan.classfiles;

import java.io.File;

import il.org.spartan.streotypes.Utility;
import il.org.spartan.utils.Separate;

@Utility public enum EXTENSIONPATH {
  ;
  private static final String JAVA_EXTENSION_DIRECTORIES = "java.ext.dirs";

  public static String[] asArray() {
    return System.getProperty(JAVA_EXTENSION_DIRECTORIES).split(File.pathSeparator);
  }

  /** Exercise this class, by printing the result of its principal function.
   *
   * @param __ unused */
  public static void main(final String[] __) {
    System.out.println(Separate.by(asArray(), "\n"));
  }
}
