package il.org.spartan.spartanizer.cmdline;

/** @author Yossi Gil
 * @since 2016 */
public interface code {
  static String essence(final String codeFragment) {
    return codeFragment.replaceAll("//.*?\r\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "").replaceAll("^\\s*$", "")//
        .replaceAll("^\\s*\\n", "")//
        .replaceAll("\\s*$", "")//
        .replaceAll("\\s+", " ")
        // TODO Matteo: I think this is buggy; the replacement should not be
        // phrased like so. $1, $2
        .replaceAll("\\([^a-zA-Z¢$_]\\) \\([^a-zA-Z¢$_]\\)", "\\([^a-zA-Z¢$_]\\)\\([^a-zA-Z¢$_]\\)")
        .replaceAll("\\([^a-zA-Z¢$_]\\) \\([a-zA-Z¢$_]\\)", "\\([^a-zA-Z¢$_]\\)\\([a-zA-Z¢$_]\\)")
        .replaceAll("\\([a-zA-Z¢$_]\\) \\([^a-zA-Z¢$_]\\)", "\\([a-zA-Z¢$_]\\)\\([^a-zA-Z¢$_]\\)");
  }
  // TODO: Sharon et al. Document this function. Also, check whether the
  // conditional here is
  // necessary. But this can only be done after having tests for this function.
  // Also, take note that the function assumes that it gets not a null. You can
  // add this to your tests. --yg
  static int wc(final String $) {
    return $.trim().isEmpty() ? 0 : $.trim().split("\\s+").length;
  }
}
