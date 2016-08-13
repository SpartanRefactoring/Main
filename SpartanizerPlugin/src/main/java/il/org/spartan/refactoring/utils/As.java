package il.org.spartan.refactoring.utils;
/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum As {
  ;
  /** Converts a boolean into a bit value
   * @param $ JD
   * @return 1 if the parameter is <code><b>true</b></code>, 0 if it is
   *         <code><b>false</b></code> */
  public static int bit(final boolean $) {
    return $ ? 1 : 0;
  }
}
