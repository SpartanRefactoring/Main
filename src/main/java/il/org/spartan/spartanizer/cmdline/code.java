package il.org.spartan.spartanizer.cmdline;

/** @author Yossi Gil
 * @author Yarden Lev
 * @author Sharon Kuninin
 * @since 2016 */
public interface code {
  static String essence(final String codeFragment) {
    return codeFragment.replaceAll("//.*?\r\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "")//
        .replaceAll("^\\s*$", "")//
        .replaceAll("^\\s*\\n", "")//
        .replaceAll("\\s*$", "")//
        .replaceAll("\\s+", " ")//
        // TODO Matteo: I think this is buggy; the replacement should not be
        // phrased like so. $1, $2
        .replaceAll("(\\([^a-zA-Z¢$_]\\)) (\\([^a-zA-Z¢$_]\\))", "$1$2")//
        .replaceAll("(\\([^a-zA-Z¢$_]\\)) (\\([a-zA-Z¢$_]\\))", "$1$2")//
        .replaceAll("(\\([a-zA-Z¢$_]\\)) (\\([^a-zA-Z¢$_]\\))", "$1$2");
  }
  /** This function counts the number of words the given string contains. Words
   * are separated by at least one whitespace.
   * @param $ the string its words are being counted
   * @return the number of words the given string contains */
  static int wc(final String $) {
    return $ == null || $.trim().isEmpty() ? 0 : $.trim().split("\\s+").length;
  }
}
