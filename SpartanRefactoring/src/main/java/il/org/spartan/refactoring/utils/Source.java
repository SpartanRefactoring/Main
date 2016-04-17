package il.org.spartan.refactoring.utils;

/**
 * An accessible container for the source code
 *
 * @author Ori Roth
 * @since 2016-04-17
 */
public class Source {

  static private String s = null;
  static public String get() {
    return s;
  }
  static public void set(String content) {
    s = content;
  }
}
