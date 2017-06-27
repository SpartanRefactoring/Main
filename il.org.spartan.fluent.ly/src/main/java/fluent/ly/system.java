package fluent.ly;

import java.io.*;
import java.text.*;
import java.util.*;

/** Not such a good name for a bunch of static functions
 * @author Yossi Gil
 * @since 2016 */
public interface system {
  static Process bash(final String shellCommand) {
    if (isWindows())
      return null;
    try {
      final Process $ = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", shellCommand });
      if ($ != null)
        return dumpOutput($);
    } catch (final IOException ¢) {
      note.bug(shellCommand, ¢);
    }
    return null;
  }
  static BufferedWriter callingClassUniqueWriter() {
    try {
      return new BufferedWriter(new FileWriter(ephemeral(myFullClassName()).dot("txt")));
    } catch (final IOException ¢) {
      note.io(¢);
    }
    return null;
  }
  static Process dumpOutput(final Process $) {
    if (isWindows())
      return $;
    try (BufferedReader in = new BufferedReader(new InputStreamReader($.getInputStream()))) {
      for (String line = in.readLine(); line != null; line = in.readLine())
        System.out.println(line);
    } catch (final IOException ¢) {
      note.io(¢, $ + "");
    }
    return $;
  }
  static Extension ephemeral(final String stem) {
    return λ -> new File(system.tmp + stem + new SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss").format(new Date()) + "." + λ);
  }
  /** @author Yossi Gil
   * @author Yarden Lev
   * @author Sharon Kuninin
   * @since 2016 Returns the essence of this code fragment, removing
   *        non-executable code parts and formatting whitespace characters.
   * @deprecated since Nov 14, 2016, replaced by
   *             {@link Essence#booleans(String)}
   * @param codeFragment code fragment represented as a string
   * @return essence of the code fragment */
  @Deprecated static String essence(final String codeFragment) {
    return codeFragment.replaceAll("//.*?\r\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "")//
        .replaceAll("^\\s*$", "")//
        .replaceAll("^\\s*\\n", "")//
        .replaceAll("\\s*$", "")//
        .replaceAll("\\s+", " ")//
        .replaceAll("([^a-zA-Z¢$_]) ([^a-zA-Z¢$_])", "$1$2")//
        .replaceAll("([^a-zA-Z¢$_]) ([a-zA-Z¢$_])", "$1$2")//
        .replaceAll("([a-zA-Z¢$_]) ([^a-zA-Z¢$_])", "$1$2");
  }
  static String essenced(final String fileName) {
    return fileName + ".essence";
  }
  static String folder2File(final String path) {
    return path//
        .replaceAll("^[.]$", "CWD")//
        .replaceAll("^[.][.]$", "DOT-DOT")//
        .replaceAll("[\\ /.]", "-")//
        .replaceAll("-+", "-")//
        .replaceAll("^-", "")//
        .replaceAll("-$", "")//
    ;
  }
  @SuppressWarnings("boxing") static boolean isBalanced(final String s) {
    final Stack<Character> $ = new Stack<>();
    for (final char ¢ : s.toCharArray())
      switch (¢) {
        case '(':
        case '[':
        case '{':
          $.push(¢);
          continue;
        case ')':
          if ($.isEmpty() || $.pop() != '(')
            return false;
          continue;
        case ']':
          if ($.isEmpty() || $.pop() != '[')
            return false;
          continue;
        case '}':
          if ($.isEmpty() || $.pop() != '{')
            return false;
          continue;
      }
    return $.isEmpty();
  }
  /** @return if called from a (potentially static) method m in class A, gives
   *         the full name of the class B != A, such that a method in B, made a
   *         sequence of calls through methods in A, which ended in the call to
   *         m. */
  static String myCallerFullClassName() {
    final StackTraceElement[] trace = new Throwable().getStackTrace();
    int i = 0;
    for (; i < trace.length; ++i)
      if (!trace[i].getClassName().equals(trace[0].getClassName()))
        break;
    for (int $ = i; $ < trace.length; ++$)
      if (!trace[$].getClassName().equals(trace[i].getClassName()))
        return trace[$].getClassName();
    return new Object().getClass().getEnclosingClass().getCanonicalName();
  }
  /** @return the name of the class from which this method was called. */
  static String myFullClassName() {
    final StackTraceElement[] $ = new Throwable().getStackTrace();
    for (int ¢ = 1; ¢ < $.length; ++¢)
      if (!$[¢].getClassName().equals($[0].getClassName()))
        return $[¢].getClassName();
    return new Object().getClass().getEnclosingClass().getCanonicalName();
  }
  static String myShortClassName() {
    return cCamelCase.lastComponent(myFullClassName());
  }
  static String now() {
    return (new Date() + "").replaceAll(" ", "-");
  }
  static String read() {
    try (Scanner $ = new Scanner(System.in)) {
      return read($);
    }
  }
  static String read(final Scanner ¢) {
    String $ = "";
    while (¢.hasNext()) // Can be Nano?
      $ += "\n" + ¢.nextLine();
    return $;
  }
  static ProcessBuilder runScript() {
    return new ProcessBuilder("/bin/bash");
  }
  static String runScript(final Process p) throws IOException {
    try (InputStream s = p.getInputStream(); BufferedReader r = new BufferedReader(new InputStreamReader(s))) {
      String ¢;
      for (final StringBuilder $ = new StringBuilder();; $.append(¢))
        if ((¢ = r.readLine()) == null)
          return $ + "";
    }
  }
  static Process shellEssenceMetrics(final String fileName) {
    return bash("./essence <" + fileName + ">" + essenced(fileName));
  }
  static String userName() {
    return English.upperFirstLetter(System.getProperty("user.name", "User"));
  }
  /** This function counts the number of words the given string contains. Words
   * are separated by at least one whitespace.
   * @param $ the string its words are being counted
   * @return the number of words the given string contains */
  static int wc(final String $) {
    return $ == null || $.trim().isEmpty() ? 0 : $.trim().split("\\s+").length;
  }
  static boolean isWindows() {
    return System.getProperty("os.name").contains("indows");
  }

  String separator = System.getProperty("file.separator", "/");
  String tmp = separator + System.getProperty("java.io.tmpdir", "/tmp") + separator;
  String UTF_8 = "utf-8";

  public static Class<?> containingClass(Object o) {
    return o.getClass().getEnclosingClass();
  }

  interface Extension {
    File dot(String extentsion);
  }
}
