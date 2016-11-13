package il.org.spartan.spartanizer.cmdline;

import static il.org.spartan.utils.Box.*;

import java.io.*;

import il.org.spartan.java.*;
import il.org.spartan.plugin.*;
import il.org.spartan.utils.*;

/** Not such a good name for a bunch of static functions
 * @author Yossi Gil
 * @since 2016 */
public interface system {
  static Process dumpOutput(final Process p) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
      for (String line = in.readLine(); line != null; line = in.readLine())
        System.out.println(line);
    } catch (final IOException x) {
      monitor.infoIOException(x, p + "");
    }
    return p;
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
  static ProcessBuilder runScript() {
    return new ProcessBuilder("/bin/bash");
  }
  static String runScript(final Process p) throws IOException {
    try (final InputStream s = p.getInputStream(); final BufferedReader r = new BufferedReader(new InputStreamReader(s))) {
      String ¢;
      for (final StringBuffer $ = new StringBuffer();; $.append(¢))
        if ((¢ = r.readLine()) == null)
          return $ + "";
    }
  }
  static String runScript(final String pathname) throws IOException {
    return runScript(BatchSpartanizer.runScript¢(pathname).start());
  }
  static int tokens(final String s) {
    int $ = 0;
    for (final Tokenizer tokenizer = new Tokenizer(new StringReader(s));;) {
      final Token t = tokenizer.next();
      if (t == null || t == Token.EOF)
        return $;
      if (t.kind == Token.Kind.COMMENT || t.kind == Token.Kind.NONCODE)
        continue;
      ++$;
    }
  }
  static boolean isTestFile(final File ¢) {
    return system.isTestSourceFile(¢.getName());
  }
  static boolean isTestSourceFile(final String fileName) {
    return fileName.contains("/test/") || fileName.matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*")
        || fileName.matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java$");
  }
  static Process bash(final String shellCommand) {
    final String[] command = { "/bin/bash", "-c", shellCommand };
    try {
      final Process p = Runtime.getRuntime().exec(command);
      if (p != null)
        return dumpOutput(p);
    } catch (final IOException x) {
      monitor.logProbableBug(shellCommand, x);
    }
    return null;
  }
  static double d(final double n1, final double n2) {
    return 1 - n2 / n1;
  }
  static String p(final int n1, final int n2) {
    return formatRelative(d(n1, n2));
  }
  static String formatRelative(final double ¢) {
    return String.format(format2(¢) + "%%", box(100 * ¢));
  }
  static String formatRelative(final double d1, final double d2) {
    return formatRelative(d1 / d2);
  }
  static String format2(final double d) {
    if (d < 0)
      return "-" + format2(-d);
    final double p = 100 * d;
    return "%" + (p < 0.01 ? ".0f" : p < 0.1 ? ".2f" : p < 1 || p < 10 ? ".1f" : p < 100 || p < 1000 ? ".0f" : "5.0g");
  }
  static double round3(final double ¢) {
    switch (digits(¢)) {
      case -1:
      case 0:
        return Math.round(1000 * ¢) / 1000.0;
      case 1:
        return Math.round(100 * ¢) / 100.0;
      case 2:
        return Math.round(10 * ¢) / 10.0;
      default:
        return ¢;
    }
  }
  static int digits(final double d) {
    if (d == 0)
      return -1;
    final double log = Math.log10(d);
    return log < 0 ? 0 : (int) log + 1;
  }
  static String format3(final double d) {
    final double fraction = d - (int) d;
    if (d == 0 || d >= 1 && fraction < 0.0005)
      return "%.0f";
    switch (digits(round3(d))) {
      case -1:
      case 0:
        return "%.3f";
      case 1:
        return "%.2f";
      case 2:
        return "%.1f";
      default:
        return "%.0f";
    }
  }
  static double ratio(final double n1, final double n2) {
    return n2 / n1;
  }
  static Process shellEssenceMetrics(final String fileName) {
    return bash("./essence <" + fileName + ">" + essenced(fileName));
  }
}
