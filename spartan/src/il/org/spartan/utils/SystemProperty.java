package il.org.spartan.utils;

import java.util.*;

import il.org.spartan.strings.*;

public enum SystemProperty {
  FILE_SEPARATOR, //
  FILE_ENCODING, //
  FILE_ENCODING_PKG, //
  JAVA_COMPILER, //
  JAVA_CLASS_PATH, //
  JAVA_CLASS_VERSION, //
  JAVA_HOME, //
  JAVA_IO_TMPDIR, //
  JAVA_VENDOR, //
  JAVA_VENDOR_URL, //
  JAVA_VERSION, //
  LINE_SEPARATOR, //
  OS_ARCH, //
  OS_NAME, //
  OS_VERSION, //
  PATH_SEPARATOR, //
  USER_DIR, //
  USER_HOME, //
  USER_NAME, //
  USER_REGION, //
  USER_TIMEZONE, //
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
