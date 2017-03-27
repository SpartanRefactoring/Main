package il.org.spartan.utils;

import java.io.*;
import java.text.*;
import java.util.*;

import il.org.spartan.java.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Not such a good name for a bunch of static functions
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
public interface system {
  static String chopLast(final String ¢) {
    return ¢.substring(0, ¢.length() - 1);
  }

  String tmp = System.getProperty("java.io.tmpdir", "/tmp") + System.getProperty("file.separator", "/");

  static Process bash(final String shellCommand) {
    if (windows())
      return null;
    try {
      final Process $ = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", shellCommand });
      if ($ != null)
        return dumpOutput($);
    } catch (final IOException ¢) {
      monitor.logProbableBug(shellCommand, ¢);
    }
    return null;
  }

  /** @return the name of the class from which this method was called. */
  static String myFullClassName() {
    final StackTraceElement[] $ = new Throwable().getStackTrace();
    for (int ¢ = 1; ¢ < $.length; ++¢)
      if (!$[¢].getClassName().equals($[0].getClassName()))
        return $[¢].getClassName();
    return new Object().getClass().getEnclosingClass().getCanonicalName();
  }

  /** @return if called from a (potentially static) method m in class A, gives
   *         the full name of the class B != A, such that a method in B, made a
   *         sequence of calls through methods in A, which ended in the call to
   *         m. */
  static String myCallerFullClassName() {
    final StackTraceElement[] trace = new Throwable().getStackTrace();
    int i = 0;
    for (; i < trace.length; ++i) {
      if (trace[i].getClassName().equals(trace[0].getClassName()))
        continue;
      break;
    }
    for (int $ = i; $ < trace.length; ++$)
      if (!trace[$].getClassName().equals(trace[i].getClassName()))
        return trace[$].getClassName();
    return new Object().getClass().getEnclosingClass().getCanonicalName();
  }

  static String className(final Class<?> ¢) {
    return ¢.getEnclosingClass() == null ? selfName(¢) : selfName(¢) + "." + className(¢.getEnclosingClass());
  }

  static String className(final Object ¢) {
    return className(¢.getClass());
  }

  static Process dumpOutput(final Process $) {
    if (windows())
      return $;
    try (BufferedReader in = new BufferedReader(new InputStreamReader($.getInputStream()))) {
      for (String line = in.readLine(); line != null; line = in.readLine())
        System.out.println(line);
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, $ + "");
    }
    return $;
  }

  static Extension ephemeral(final String stem) {
    return λ -> new File(system.tmp + stem + new SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss").format(new Date()) + "." + λ);
  }

  /** @author Yossi Gil {@code Yossi.Gil@GMail.COM}
   * @author Yarden Lev
   * @author Sharon Kuninin
   * @since 2016 Returns the essence of this code fragment, removing
   *        non-executable code parts and formatting whitespace characters.
   * @deprecated since Nov 14, 2016, replaced by {@link Essence#of(String)}
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

  static BufferedWriter callingClassUniqueWriter() {
    try {
      return new BufferedWriter(new FileWriter(ephemeral(myFullClassName()).dot("txt")));
    } catch (final IOException ¢) {
      monitor.infoIOException(¢);
    }
    return null;
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

  static String selfName(final Class<?> ¢) {
    return ¢.isAnonymousClass() ? "{}"
        : ¢.isAnnotation() ? "@" + ¢.getSimpleName() : !¢.getSimpleName().isEmpty() ? ¢.getSimpleName() : ¢.getCanonicalName();
  }

  static Process shellEssenceMetrics(final String fileName) {
    return bash("./essence <" + fileName + ">" + essenced(fileName));
  }

  static int tokens(final String s) {
    int $ = 0;
    for (final Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
      final Token t = tokenizer.next();
      if (t == null || t == Token.EOF)
        return $;
      if (t.kind != Token.Kind.COMMENT && t.kind != Token.Kind.NONCODE)
        ++$;
    }
  }

  static String userName() {
    return English.upperFirstLetter(System.getProperty("user.name", "Killroy"));
  }

  /** This function counts the number of words the given string contains. Words
   * are separated by at least one whitespace.
   * @param $ the string its words are being counted
   * @return the number of words the given string contains */
  static int wc(final String $) {
    return $ == null || $.trim().isEmpty() ? 0 : $.trim().split("\\s+").length;
  }

  static boolean windows() {
    return System.getProperty("os.name").contains("indows");
  }

  @SuppressWarnings({ "boxing", "incomplete-switch" }) static boolean isBalanced(final String s) {
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

  interface Extension {
    File dot(String extentsion);
  }

  static String callinClassLastName() {
    return myFullClassName().replaceAll("[a-z0-9A-Z]*\\.", "");
  }
}