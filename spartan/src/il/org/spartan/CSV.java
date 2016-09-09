package il.org.spartan;

import static il.org.spartan.utils.____.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

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
   * @param classes Input array
   * @return Combined string
   * @see #splitToClasses(String) */
  public static String combine(final Class<?>[] classes) {
    final String[] ss = new String[classes.length];
    for (int i = 0; i < ss.length; ++i)
      ss[i] = classes[i] == null ? null : classes[i].getName();
    return combine(ss);
  }

  /** Combine the given array into a comma separated string. Each element is
   * escaped, so commas inside the elements cannot do not collide with the
   * separating commas.
   * @param <T> type of array elements
   * @param parts Input array
   * @return Combined string
   * @see CSV#escape(String) */
  public static <T> String combine(final T[] parts) {
    nonnull(parts);
    final StringBuilder $ = new StringBuilder(10 * parts.length);
    final Separator sep = new Separator(",");
    for (final T t : parts)
      $.append(sep + escape(t == null ? null : t.toString()));
    return $.toString();
  }

  /** Combine the given array of enum values into a comma separated string. Each
   * array element is first converted into a string using its name() method and
   * then is escaped.
   * @param <T> type of array elements
   * @param parts Input array
   * @return Combined string
   * @see CSV#escape(String) */
  public static <T extends Enum<T>> String combine(final T[] parts) {
    final String[] ss = new String[parts.length];
    for (int i = 0; i < ss.length; ++i)
      ss[i] = parts[i] == null ? null : parts[i].name();
    return combine(ss);
  }

  /** Escape the given input
   * @param in Input string
   * @return Escaped form of the input */
  public static String escape(final String in) {
    if (in == null)
      return NULL;
    final int len = in.length();
    final StringBuilder out = new StringBuilder(len);
    for (int i = 0; i < len; ++i) {
      final char c = in.charAt(i);
      if (c == '\\')
        out.append("\\\\");
      else if (c == '\n')
        out.append("\\n");
      else if (c == '\r')
        out.append("\\r");
      else if (c == '\t')
        out.append("\\t");
      else if (c == ',')
        out.append("\\.");
      else
        out.append(c);
    }
    return out.toString();
  }

  /** Read a CSV file.
   * @param file Input file
   * @return A two dimensional array of strings
   * @throws IOException some problem with file 'filename' */
  public static String[][] load(final File file) throws IOException {
    return load(new FileReader(file));
  }

  /** Read a CSV file from the given Reader object.
   * @param r input reader
   * @return a two dimensional array of strings */
  public static String[][] load(final Reader r) {
    final ArrayList<String[]> $ = new ArrayList<>(20);
    for (final Scanner s = new Scanner(r); s.hasNext();)
      $.add(split(s.nextLine()));
    return $.toArray(new String[$.size()][]);
  }

  public static void save(final File file, final String[][] data) throws IOException {
    final PrintWriter pw = new PrintWriter(new FileWriter(file));
    pw.print(toCsv(data));
    pw.close();
  }

  /** Split a comma separated string into an array of enum values.
   * @param <T> Type of enum class
   * @param clazz Class object of T
   * @param s Input string
   * @return Array of T */
  public static <T extends Enum<T>> T[] split(final Class<T> clazz, final String s) {
    final String[] ss = split(s);
    @SuppressWarnings("unchecked") final T[] $ = (T[]) Array.newInstance(clazz, ss.length);
    for (int i = 0; i < $.length; ++i)
      $[i] = ss[i] == null ? null : Enum.valueOf(clazz, ss[i]);
    return $;
  }

  /** Split a comma separated string into its sub parts
   * @param s input string
   * @return Array of sub parts, in their original order */
  public static String[] split(final String s) {
    if (s.length() == 0)
      return new String[0];
    final List<String> $ = new ArrayList<>();
    int from = 0;
    while (true) {
      final int to = s.indexOf(',', from);
      if (to < 0) {
        $.add(unescape(s.substring(from, s.length())));
        break;
      }
      $.add(unescape(s.substring(from, to)));
      from = to + 1;
    }
    return $.toArray(new String[$.size()]);
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
      } catch (final ClassNotFoundException e) {
        throw new RuntimeException("s=" + s, e);
      }
    return $;
  }

  public static String toCsv(final String[][] data) {
    final StringWriter sw = new StringWriter();
    final PrintWriter pw = new PrintWriter(sw);
    for (final String[] line : data) {
      final Separator comma = new Separator(",");
      for (final String s : line)
        pw.print(comma + escape(s));
      pw.println();
    }
    pw.flush();
    return sw.toString();
  }

  /** Unescape the given input
   * @param in Input string
   * @return Unescaped string */
  public static String unescape(final String in) {
    if (NULL.equals(in))
      return null;
    boolean esc = false;
    final int length = in.length();
    final StringBuilder out = new StringBuilder(length);
    for (int i = 0; i < length; ++i) {
      final char c = in.charAt(i);
      if (!esc) {
        if (c != '\\')
          out.append(c);
        else
          esc = true;
        continue;
      }
      esc = false;
      switch (c) {
        case 'n':
          out.append("\n");
          break;
        case 'r':
          out.append("\r");
          break;
        case 't':
          out.append("\t");
          break;
        case '.':
          out.append(",");
          break;
        case '\\':
          out.append("\\");
          break;
        default:
      }
    }
    return out.toString();
  }
}
