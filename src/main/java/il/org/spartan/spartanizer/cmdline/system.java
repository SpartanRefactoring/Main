package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import il.org.spartan.java.*;
import il.org.spartan.plugin.*;

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

  static double ratio(final double n1, final double n2) {
    return n2 / n1;
  }

  static String removePercentChar(final String p) {
    return !p.contains("--") ? p.replace("%", "") : p.replace("%", "").replaceAll("--", "-");
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
        || fileName.matches("[A-Za-z0-9_-]*[Tt]est[A-Za-z0-9_-]*.java$") || fileName.matches("[\\/A-Za-z0-9]*[\\/]test[\\/A-Za-z0-9]*");
  }
}
