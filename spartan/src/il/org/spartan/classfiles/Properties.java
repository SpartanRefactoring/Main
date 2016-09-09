package il.org.spartan.classfiles;

import static il.org.spartan.strings.StringUtils.*;

import java.util.*;

/** A representation of the system global CLASSPATH.
 * @author Yossi Gil
 * @since 12/07/2007 */
public enum Properties {
  ;
  public static void main(final String[] args) {
    final TreeSet<String> t = new TreeSet<>();
    t.addAll(System.getProperties().stringPropertyNames());
    for (final String key : t)
      System.out.println(key + ": " + esc(System.getProperty(key)));
  }
}
