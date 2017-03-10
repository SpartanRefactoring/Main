package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.utils.Box.*;

import java.io.*;
import java.text.*;
import java.util.*;

import il.org.spartan.java.*;
import il.org.spartan.spartanizer.utils.*;

/** Not such a good name for a bunch of static functions
 * @author Yossi Gil
 * @since 2016 */
public interface system {
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

  static double d(final double n1, final double n2) {
    return 1 - n2 / n1;
  }

  static int digits(final double ¢) {
    if (¢ == 0)
      return -1;
    final double $ = Math.log10(¢);
    return $ < 0 ? 0 : (int) $ + 1;
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

  /** @author Yossi Gil
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

  static String format2(final double ¢) {
    if (¢ < 0)
      return "-" + format2(-¢);
    final double $ = 100 * ¢;
    return "%" + ($ < 0.01 ? ".0f" : $ < 0.1 ? ".2f" : $ < 1 || $ < 10 ? ".1f" : $ < 100 || $ < 1000 ? ".0f" : "5.0g");
  }

  static String format3(final double ¢) {
    if (¢ == 0 || ¢ >= 1 && ¢ - (int) ¢ < 0.0005)
      return "%.0f";
    switch (digits(round3(¢))) {
      case 1:
        return "%.2f";
      case 2:
        return "%.1f";
      case -1:
      case 0:
        return "%.3f";
      default:
        return "%.0f";
    }
  }

  static String formatRelative(final double ¢) {
    return String.format(format2(¢) + "%%", box(100 * ¢));
  }

  static String formatRelative(final double d1, final double d2) {
    return formatRelative(d1 / d2);
  }

  static boolean isTestFile(final File ¢) {
    return system.isTestSourceFile(¢.getName());
  }

  static boolean isTestSourceFile(final String fileName) {
    return fileName.contains("/test/") || fileName.matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*")
        || fileName.matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java$");
  }

  static String now() {
    return (new Date() + "").replaceAll(" ", "-");
  }

  static String p(final int n1, final int n2) {
    return formatRelative(d(n1, n2));
  }

  static double ratio(final double n1, final double n2) {
    return n2 / n1;
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

  static double round3(final double ¢) {
    switch (digits(¢)) {
      case 1:
        return Math.round(100 * ¢) / 100.0;
      case 2:
        return Math.round(10 * ¢) / 10.0;
      case -1:
      case 0:
        return Math.round(1000 * ¢) / 1000.0;
      default:
        return ¢;
    }
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

  static String runScript(final String pathname) throws IOException {
    return runScript(BatchSpartanizer.runScript¢(pathname).start());
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

  interface Extension {
    File dot(String extentsion);
  }

  String tmp = System.getProperty("java.io.tmpdir", "/tmp") + System.getProperty("file.separator", "/");

  static Extension ephemeral(final String stem) {
    return λ -> new File(system.tmp + stem + new SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss").format(new Date()) + "." + λ);
  }
}
