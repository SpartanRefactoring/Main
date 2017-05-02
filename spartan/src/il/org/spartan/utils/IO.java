package il.org.spartan.utils;

import static il.org.spartan.utils.___.*;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;

import il.org.spartan.streotypes.*;

/** Static methods for I/O related operations */
@Antiexample public class IO {
  @NotNull public static String concatLines(@NotNull final Iterable<String> ss) {
    @NotNull final StringBuffer $ = new StringBuffer(1000);
    @NotNull final Separator nl = new Separator("\n");
    for (final String ¢ : ss)
      $.append(nl).append(¢);
    return $ + "";
  }

  @NotNull public static String concatLines(@NotNull final String... ss) {
    @NotNull final StringBuffer $ = new StringBuffer(1000);
    @NotNull final Separator nl = new Separator("\n");
    for (final String ¢ : ss)
      $.append(nl).append(¢);
    return $ + "";
  }

  @NotNull public static List<String> lines(@NotNull final String s) throws IOException {
    @NotNull final List<String> $ = new ArrayList<>();
    for (@NotNull final BufferedReader br = new BufferedReader(new StringReader(s));;) {
      final String line = br.readLine();
      if (line == null)
        return $;
      $.add(line);
    }
  }

  public static InputStream toInputStream(@NotNull final String $) {
    try {
      return new ByteArrayInputStream($.getBytes("UTF-8"));
    } catch (@NotNull final UnsupportedEncodingException e) {
      unreachable();
      return null;
    }
  }

  /** Read the contents of the given class-path file.
   * @param clazz Class - Specifies a location in the class-path tree
   * @param path Relative path to the file from the given class
   * @return Contents of the file
   * @throws IOException If an I/O error occur */
  @NotNull public static String toString(@NotNull final Class<?> clazz, final String path) throws IOException {
    return toString(clazz.getResourceAsStream(path));
  }

  /** Read the contents of the given stream and return it as a String
   * @param ¢ Input stream
   * @return the entire content of <code>is</code>
   * @throws IOException If an I/O error occur */
  @NotNull public static String toString(@NotNull final InputStream ¢) throws IOException {
    return toString(new InputStreamReader(¢));
  }

  /** Read the contents of the given reader and return it as a String
   * @param r Reader
   * @return the entire content of <code>r</code>
   * @throws IOException If an I/O error occur */
  @NotNull public static String toString(@NotNull final Reader r) throws IOException {
    @NotNull final StringBuilder $ = new StringBuilder();
    for (int c = r.read(); c >= 0; c = r.read())
      $.append((char) c);
    return $ + "";
  }

  /** Write a string to a file
   * @param outputFile File to be written
   * @param ss Strings to write
   * @throws IOException If an I/O error occur */
  public static void writeLines(@NotNull final File outputFile, @NotNull final String... ss) throws IOException {
    @NotNull final FileWriter fw = new FileWriter(outputFile);
    try {
      for (final String ¢ : ss) {
        fw.append(¢);
        fw.append("\n");
      }
    } finally {
      fw.close();
    }
  }
}
