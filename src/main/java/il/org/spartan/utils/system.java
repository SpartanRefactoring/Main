package il.org.spartan.utils;

import java.io.*;
import java.text.*;
import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.java.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Not such a good name for a bunch of static functions
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
public interface system {
  String tmp = System.getProperty("java.io.tmpdir", "/tmp") + System.getProperty("file.separator", "/");

  static Process bash(@NotNull final String shellCommand) {
    if (windows())
      return null;
    try {
      final Process $ = Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", shellCommand });
      if ($ != null)
        return dumpOutput($);
    } catch (@NotNull final IOException ¢) {
      monitor.logProbableBug(shellCommand, ¢);
    }
    return null;
  }

  /** @return the name of the class from which this method was called. */
  static String callingClassName() {
    final StackTraceElement[] $ = new Throwable().getStackTrace();
    for (int ¢ = 1; ¢ < $.length; ++¢)
      if (!$[¢].getClassName().equals($[0].getClassName()))
        return $[¢].getClassName();
    return new Object().getClass().getEnclosingClass().getCanonicalName();
  }

  @NotNull static String className(@NotNull final Class<?> ¢) {
    return ¢.getEnclosingClass() == null ? selfName(¢) : selfName(¢) + "." + className(¢.getEnclosingClass());
  }

  @NotNull static String className(@NotNull final Object ¢) {
    return className(¢.getClass());
  }

  @NotNull static Process dumpOutput(@NotNull final Process $) {
    if (windows())
      return $;
    try (@NotNull BufferedReader in = new BufferedReader(new InputStreamReader($.getInputStream()))) {
      for (String line = in.readLine(); line != null; line = in.readLine())
        System.out.println(line);
    } catch (@NotNull final IOException ¢) {
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
  @Deprecated static String essence(@NotNull final String codeFragment) {
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

  @NotNull static String essenced(final String fileName) {
    return fileName + ".essence";
  }

  static String folder2File(@NotNull final String path) {
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
      return new BufferedWriter(new FileWriter(ephemeral(callingClassName()).dot("txt")));
    } catch (@NotNull final IOException ¢) {
      monitor.infoIOException(¢);
    }
    return null;
  }

  static String now() {
    return (new Date() + "").replaceAll(" ", "-");
  }

  @NotNull static String read() {
    try (@NotNull Scanner $ = new Scanner(System.in)) {
      return read($);
    }
  }

  @NotNull static String read(@NotNull final Scanner ¢) {
    @NotNull String $ = "";
    while (¢.hasNext()) // Can be Nano?
      $ += "\n" + ¢.nextLine();
    return $;
  }

  @NotNull static ProcessBuilder runScript() {
    return new ProcessBuilder("/bin/bash");
  }

  @NotNull static String runScript(@NotNull final Process p) throws IOException {
    try (InputStream s = p.getInputStream(); @NotNull BufferedReader r = new BufferedReader(new InputStreamReader(s))) {
      String ¢;
      for (@NotNull final StringBuilder $ = new StringBuilder();; $.append(¢))
        if ((¢ = r.readLine()) == null)
          return $ + "";
    }
  }

  @NotNull static String selfName(@NotNull final Class<?> ¢) {
    return ¢.isAnonymousClass() ? "{}"
        : ¢.isAnnotation() ? "@" + ¢.getSimpleName() : !¢.getSimpleName().isEmpty() ? ¢.getSimpleName() : ¢.getCanonicalName();
  }

  @Nullable static Process shellEssenceMetrics(final String fileName) {
    return bash("./essence <" + fileName + ">" + essenced(fileName));
  }

  static int tokens(@NotNull final String s) {
    int $ = 0;
    for (@NotNull final Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
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
  static int wc(@Nullable final String $) {
    return $ == null || $.trim().isEmpty() ? 0 : $.trim().split("\\s+").length;
  }

  static boolean windows() {
    return System.getProperty("os.name").contains("indows");
  }

  interface Extension {
    @NotNull File dot(String extentsion);
  }
}
