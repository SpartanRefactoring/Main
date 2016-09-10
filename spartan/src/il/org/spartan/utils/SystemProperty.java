package il.org.spartan.utils;

import java.util.*;

import il.org.spartan.strings.*;

public enum SystemProperty {
  FILE__SEPARATOR, //
  FILE__ENCODING, //
  FILE__ENCODING__PKG, //
  JAVA__COMPILER, //
  JAVA__CLASS__PATH, //
  JAVA__CLASS__VERSION, //
  JAVA__HOME, //
  JAVA__IO__TMPDIR, //
  JAVA__VENDOR, //
  JAVA__VENDOR__URL, //
  JAVA__VERSION, //
  LINE__SEPARATOR, //
  OS__ARCH, //
  OS__NAME, //
  OS__VERSION, //
  PATH__SEPARATOR, //
  USER__DIR, //
  USER__HOME, //
  USER__NAME, //
  USER__REGION, //
  USER__TIMEZONE, //
  ;
  public static void main(final String[] args) throws RuntimeException {
    for (final SystemProperty sp : values()) {
      if (sp.value() == null)
        throw new RuntimeException("property " + sp + " is probably misspelled");
      System.out.println(sp.key + "='" + sp.value() + "'");
    }
    for (final String s : objectsToStrings(System.getProperties().keySet()))
      System.out.println(s + " = '" + StringUtils.visualize((String) System.getProperties().get(s)) + "'");
  }

  private static TreeSet<String> objectsToStrings(final Set<Object> s) {
    final TreeSet<String> $ = new TreeSet<>();
    for (final Object o : s)
      $.add((String) o);
    return $;
  }

  public final String key;

  private SystemProperty() {
    key = name().toLowerCase().replace('_', '.');
  }

  public String value() {
    return StringUtils.visualize(value(System.getProperties()));
  }

  public String value(final Properties p) {
    return p.getProperty(key);
  }
}
