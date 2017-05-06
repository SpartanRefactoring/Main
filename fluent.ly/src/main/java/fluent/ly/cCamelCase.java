package fluent.ly;

/** An empty {@code interface} for fluent programming. The name should say it
 * all: The name, followed by a dot, followed by a method name, should read like
 * a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface cCamelCase {
  static String[] components(final String javaName) {
    return javaName.split(JAVA_CAMEL_CASE_SEPARATOR);
  }
  static String lastComponent(final Class<?> ¢) {
    return lastComponent(¢.getCanonicalName());
  }
  static String lastComponent(final String fullClassName) {
    return fullClassName.replaceAll("[a-z0-9A-Z]*\\.", "");
  }
  static boolean usefulTypeName(final String typeName) {
    return typeName.length() > 1 || !Character.isUpperCase(the.characterOf(typeName));
  }

  String JAVA_CAMEL_CASE_SEPARATOR = "[_]|(?<!(^|[_A-Z]))(?=[A-Z])|(?<!(^|_))(?=[A-Z][a-z])";
}
