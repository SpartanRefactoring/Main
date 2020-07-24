package il.org.spartan.classfiles;

import static il.org.spartan.strings.StringUtils.esc;

import java.util.TreeSet;

/** A representation of the system global CLASSPATH.
 *
 * @author Yossi Gil
 * @since 12/07/2007 */
public enum Properties {
  ;
  public static void main(final String[] args) {
    for (final String key : new TreeSet<>(System.getProperties().stringPropertyNames()))
      System.out.println(key + ": " + esc(System.getProperty(key)));
  }
}
