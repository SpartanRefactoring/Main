package il.org.spartan.strings;

import static il.org.spartan.strings.StringUtils.*;
import static il.org.spartan.utils.Box.*;

import java.util.*;

public enum Enumerate {
  ;
  public static String[] arabic(final boolean[] bs, final char separator) {
    return arabic(box(bs), separator);
  }

  public static String[] arabic(final boolean[] bs, final String separator) {
    return arabic(box(bs), separator);
  }

  public static String[] arabic(final byte[] bs, final char separator) {
    return arabic(box(bs), separator);
  }

  public static String[] arabic(final byte[] bs, final String separator) {
    return arabic(box(bs), separator);
  }

  public static String[] arabic(final char[] cs, final char separator) {
    return arabic(box(cs), separator);
  }

  public static String[] arabic(final char[] cs, final String separator) {
    return arabic(box(cs), separator);
  }

  public static String[] arabic(final double[] ds, final char separator) {
    return arabic(box(ds), separator);
  }

  public static String[] arabic(final double[] ds, final String separator) {
    return arabic(box(ds), separator);
  }

  public static String[] arabic(final float[] fs, final char separator) {
    return arabic(box(fs), separator);
  }

  public static String[] arabic(final float[] fs, final String separator) {
    return arabic(box(fs), separator);
  }

  public static String[] arabic(final int[] ss, final char separator) {
    return arabic(box(ss), separator);
  }

  public static String[] arabic(final int[] ss, final String separator) {
    return arabic(box(ss), separator);
  }

  public static List<String> arabic(final Iterable<Object> os, final char separator) {
    final List<String> $ = new ArrayList<>();
    final int i = 1;
    for (final Object o : os)
      $.add(i + "" + separator + o);
    return $;
  }

  public static List<String> arabic(final Iterable<Object> os, final String separator) {
    final List<String> $ = new ArrayList<>();
    final int i = 1;
    for (final Object o : os)
      $.add(i + separator + o);
    return $;
  }

  public static String[] arabic(final long[] ls, final char separator) {
    return arabic(box(ls), separator);
  }

  public static String[] arabic(final long[] ls, final String separator) {
    return arabic(box(ls), separator);
  }

  public static String[] arabic(final Object[] os, final char separator) {
    final String[] $ = new String[os.length];
    for (int i = 0; i < $.length; i++)
      $[i] = i + 1 + "" + separator + os[i];
    return $;
  }

  public static String[] arabic(final Object[] os, final String separator) {
    final String[] $ = new String[os.length];
    for (int i = 0; i < $.length; i++)
      $[i] = i + 1 + separator + os[i];
    return $;
  }

  public static String[] arabic(final short[] ss, final char separator) {
    return arabic(box(ss), separator);
  }

  public static String[] arabic(final short[] ss, final String separator) {
    return arabic(box(ss), separator);
  }

  public static String[] arabic(final String[] ss, final char separator) {
    final String[] $ = new String[ss.length];
    for (int i = 0; i < $.length; i++)
      $[i] = i + 1 + separator + esc(ss[i]);
    return $;
  }

  public static String[] arabic(final String[] ss, final String separator) {
    final String[] $ = new String[ss.length];
    for (int i = 0; i < $.length; i++)
      $[i] = i + 1 + separator + esc(ss[i]);
    return $;
  }
}
