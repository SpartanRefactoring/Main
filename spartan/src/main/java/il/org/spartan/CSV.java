package il.org.spartan;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** This class realize the CSV specification, by comprising methods for
 * manipulating CSV files. e.g. 1, 2, 3 4, 5, 6 The class supports string arrays
 * as data. e.g. 1, "{9; 10}", 3 4, "{8; 11}", 6 This class also supports
 * converting strings to enums instances. e.g. 1, EXTENDS, 3 4, IMPLEMENTS, 6
 * This is a simplified version of the CSV specification, each record must be a
 * single line. Within are some other useful auxiliary functions for string
 * manipulations.
 * @author Oren Rubin */
public enum CSV {
  ;
  private static final String NULL = "\\0";

  /** Combine the given array of Class objects values into a comma separated
   * string.
   * @param cs Input array
   * @return Combined string
   * @see #splitToClasses(String) */
  public static String combine(final Class<?>[] cs) {
    final String[] $ = new String[cs.length];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = cs[¢] == null ? null : cs[¢].getName();
    return combine($);
  }
  /** Combine the given array into a comma separated string. Each element is
   * escaped, so commas inside the elements cannot do not collide with the
   * separating commas.
   * @param <T> type of array elements
   * @param parts Input array
   * @return Combined string
   * @see CSV#escape(String) */
  public static <T> String combine(final T[] parts) {
    assert parts != null;
    final StringBuilder $ = new StringBuilder(10 * parts.length);
    final Separator sep = new Separator(",");
    for (final T ¢ : parts)
      $.append(sep + escape(¢ == null ? null : ¢ + ""));
    return $ + "";
  }
  /** Combine the given array of enum values into a comma separated string. Each
   * array element is first converted into a string using its name() method and
   * then is escaped.
   * @param <T> type of array elements
   * @param parts Input array
   * @return Combined string
   * @see CSV#escape(String) */
  public static <T extends Enum<T>> String combine(final T[] parts) {
    final String[] $ = new String[parts.length];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = parts[¢] == null ? null : parts[¢].name();
    return combine($);
  }
  /** Escape the given input
   * @param s Input string
   * @return Escaped form of the input */
  public static String escape(final String s) {
    if (s == null)
      return NULL;
    final int len = s.length();
    final StringBuilder $ = new StringBuilder(len);
    for (final char ¢ : s.toCharArray())
      $.append(¢ == '\\' ? "\\\\" : ¢ == '\n' ? "\\n" : ¢ == '\r' ? "\\r" : ¢ == '\t' ? "\\t" : ¢ == ',' ? "\\." : ¢);
    return $ + "";
  }
  /** Read a CSV file.
   * @param ¢ Input file
   * @return A two dimensional array of strings
   * @throws IOException some problem with file 'filename' */
  public static String[][] load(final File ¢) throws IOException {
    return load(new FileReader(¢));
  }
  /** Read a CSV file from the given Reader object.
   * @param r input reader
   * @return a two dimensional array of strings */
  public static String[][] load(final Reader r) {
    final ArrayList<String[]> $ = new ArrayList<>(20);
    try (final Scanner ¢ = new Scanner(r)) {
      while (¢.hasNext())
        $.add(split(¢.nextLine()));
    }
    return $.toArray(new String[$.size()][]);
  }
  public static void save(final File f, final String[][] data) throws IOException {
    try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
      pw.print(toCsv(data));
    }
  }
  /** Split a comma separated string into an array of enum values.
   * @param <T> Type of enum class
   * @param clazz Class object of T
   * @param s Input string
   * @return Array of T */
  public static <T extends Enum<T>> T[] split(final Class<T> clazz, final String s) {
    final String[] ss = split(s);
    @SuppressWarnings("unchecked") final T[] $ = (T[]) Array.newInstance(clazz, ss.length);
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = ss[¢] == null ? null : Enum.valueOf(clazz, ss[¢]);
    return $;
  }
  /** Split a comma separated string into its sub parts
   * @param s input string
   * @return Array of sub parts, in their original order */
  public static String[] split(final String s) {
    if (s.length() == 0)
      return new String[0];
    final List<String> $ = new ArrayList<>();
    for (int from = 0;;) {
      final int to = s.indexOf(',', from);
      if (to < 0) {
        $.add(unescape(s.substring(from, s.length())));
        return $.toArray(new String[$.size()]);
      }
      $.add(unescape(s.substring(from, to)));
      from = to + 1;
    }
  }
  /** Split a comma separated string into an array of classes.
   * @param s input string
   * @return Array of T */
  public static Class<?>[] splitToClasses(final String s) {
    final String[] names = split(s);
    final Class<?>[] $ = new Class<?>[names.length]; // (T[])
    // Array.newInstance(cls,
    // arr.length);
    for (int i = 0; i < $.length; ++i)
      try {
        $[i] = names[i] == null ? null : Class.forName(names[i]);
      } catch (final ClassNotFoundException ¢) {
        throw new RuntimeException("s=" + s, ¢);
      }
    return $;
  }
  public static String toCsv(final String[][] data) {
    final StringWriter $ = new StringWriter();
    final PrintWriter pw = new PrintWriter($);
    for (final String[] line : data) {
      final Separator comma = new Separator(",");
      for (final String ¢ : line)
        pw.print(comma + escape(¢));
      pw.println();
    }
    pw.flush();
    return $ + "";
  }
  /** Unescape the given input
   * @param s Input string
   * @return Unescaped string */
  public static String unescape(final String s) {
    if (NULL.equals(s))
      return null;
    boolean faceValue = true;
    final int length = s.length();
    final StringBuilder $ = new StringBuilder(length);
    for (int i = 0; i < length; ++i) {
      final char c = s.charAt(i);
      if (faceValue) {
        if (c == '\\')
          faceValue = false;
        else
          $.append(c);
        continue;
      }
      faceValue = true;
      switch (c) {
        case 'n':
          $.append("\n");
          break;
        case 'r':
          $.append("\r");
          break;
        case 't':
          $.append("\t");
          break;
        case '.':
          $.append(",");
          break;
        case '\\':
          $.append("\\");
          break;
        default:
      }
    }
    return $ + "";
  }
}
