package il.org.spartan.spartanizer.cmdline;

import java.io.*;

import il.org.spartan.java.*;
import il.org.spartan.plugin.*;

/**
 * Not such a good name for a bunch of static functions
 * @author Yossi Gil 
 * @since 2016 */
public interface run {

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

  static String folder2File(final String path) {
    return path//
        .replaceAll("[\\ /.]", "-")//
        .replaceAll("-+", "-")//
        .replaceAll("^-", "")//
        .replaceAll("-$", "")//
        .replaceAll("^[.]$", "CWD")//
        .replaceAll("^[.][.]$", "DOT-DOT")//
    ;
  }

  static Process dumpOutput(final Process p) {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
      for (String line = in.readLine(); line != null; line = in.readLine(), System.out.println(line))
        ;
    } catch (final IOException x) {
      monitor.infoIOException(x, p + "");
    }
    return p;
  }

  static String removePercentChar(final String p) {
    return !p.contains("--") ? p.replace("%", "") : p.replace("%", "").replaceAll("--", "-");
  }

  static String runScript(final String pathname) throws IOException {
    return runScript(BatchSpartanizer.runScript¢(pathname).start());
  }

  static int wc(final String $) {
    return $.trim().isEmpty() ? 0 : $.trim().split("\\s+").length;
  }

  static String essenced(final String fileName) {
    return fileName + ".essence";
  }

  static String essenceNew(final String codeFragment) {
    return codeFragment.replaceAll("//.*?\r\n", "\n").replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "")
        .replaceAll("^\\s*$", "").replaceAll("^\\s*\\n", "").replaceAll("\\s*$", "").replaceAll("\\s+", " ")
        .replaceAll("\\([^a-zA-Z]\\) \\([^a-zA-Z]\\)", "\\([^a-zA-Z]\\)\\([^a-zA-Z]\\)")
        .replaceAll("\\([^a-zA-Z]\\) \\([a-zA-Z]\\)", "\\([^a-zA-Z]\\)\\([a-zA-Z]\\)")
        .replaceAll("\\([a-zA-Z]\\) \\([^a-zA-Z]\\)", "\\([a-zA-Z]\\)\\([^a-zA-Z]\\)");
  }
}
