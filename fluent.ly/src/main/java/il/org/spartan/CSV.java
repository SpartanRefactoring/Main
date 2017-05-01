package il.org.spartan;

import static fluent.ly.___.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.*;

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
  @NotNull public static String combine(@NotNull final Class<?>[] cs) {
    @NotNull final String[] $ = new String[cs.length];
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
  @NotNull public static <T> String combine(@NotNull final T[] parts) {
    nonnull(parts);
    @NotNull final StringBuilder $ = new StringBuilder(10 * parts.length);
    @NotNull final Separator sep = new Separator(",");
    for (@Nullable final T ¢ : parts)
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
  @NotNull public static <T extends Enum<T>> String combine(@NotNull final T[] parts) {
    @NotNull final String[] $ = new String[parts.length];
    for (int ¢ = 0; ¢ < $.length; ++¢) {
      @NotNull final T t = parts[¢];
      $[¢] = t == null ? null : t.name();
    }
    return combine($);
  }

  /** Escape the given input
   * @param s Input string
   * @return Escaped form of the input */
  @NotNull public static String escape(@Nullable final String s) {
    if (s == null)
      return NULL;
    final int len = s.length();
    @NotNull final StringBuilder $ = new StringBuilder(len);
    for (final char ¢ : s.toCharArray())
      $.append(¢ == '\\' ? "\\\\" : ¢ == '\n' ? "\\n" : ¢ == '\r' ? "\\r" : ¢ == '\t' ? "\\t" : ¢ == ',' ? "\\." : ¢);
    return $ + "";
  }

  /** Read a CSV file.
   * @param ¢ Input file
   * @return A two dimensional array of strings
   * @throws IOException some problem with file 'filename' */
  public static String[][] load(@NotNull final File ¢) throws IOException {
    return load(new FileReader(¢));
  }

  /** Read a CSV file from the given Reader object.
   * @param r input reader
   * @return a two dimensional array of strings */
  public static String[][] load(@NotNull final Reader r) {
    @NotNull final ArrayList<String[]> $ = new ArrayList<>(20);
    for (@NotNull final Scanner ¢ = new Scanner(r); ¢.hasNext();)
      $.add(split(¢.nextLine()));
    return $.toArray(new String[$.size()][]);
  }

  public static void save(@NotNull final File f, @NotNull final String[][] data) throws IOException {
    @NotNull final PrintWriter pw = new PrintWriter(new FileWriter(f));
    pw.print(toCsv(data));
    pw.close();
  }

  /** Split a comma separated string into an array of enum values.
   * @param <T> Type of enum class
   * @param clazz Class object of T
   * @param s Input string
   * @return Array of T */
  @NotNull public static <T extends Enum<T>> T[] split(@NotNull final Class<T> clazz, @NotNull final String s) {
    @NotNull final String[] ss = split(s);
    @NotNull @SuppressWarnings("unchecked") final T[] $ = (T[]) Array.newInstance(clazz, ss.length);
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = ss[¢] == null ? null : Enum.valueOf(clazz, ss[¢]);
    return $;
  }

  /** Split a comma separated string into its sub parts
   * @param s input string
   * @return Array of sub parts, in their original order */
  @NotNull public static String[] split(@NotNull final String s) {
    if (s.length() == 0)
      return new String[0];
    @NotNull final List<String> $ = new ArrayList<>();
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
  @NotNull public static Class<?>[] splitToClasses(@NotNull final String s) {
    @NotNull final String[] names = split(s);
    @NotNull final Class<?>[] $ = new Class<?>[names.length]; // (T[])
    // Array.newInstance(cls,
    // arr.length);
    for (int i = 0; i < $.length; ++i)
      try {
        $[i] = names[i] == null ? null : Class.forName(names[i]);
      } catch (@NotNull final ClassNotFoundException ¢) {
        throw new RuntimeException("s=" + s, ¢);
      }
    return $;
  }

  @NotNull public static String toCsv(@NotNull final String[][] data) {
    @NotNull final StringWriter $ = new StringWriter();
    @NotNull final PrintWriter pw = new PrintWriter($);
    for (@NotNull final String[] line : data) {
      @NotNull final Separator comma = new Separator(",");
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
  public static String unescape(@NotNull final String s) {
    if (NULL.equals(s))
      return null;
    boolean esc = false;
    final int length = s.length();
    @NotNull final StringBuilder $ = new StringBuilder(length);
    for (int i = 0; i < length; ++i) {
      final char c = s.charAt(i);
      if (!esc) {
        if (c == '\\')
          esc = true;
        else
          $.append(c);
        continue;
      }
      esc = false;
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
