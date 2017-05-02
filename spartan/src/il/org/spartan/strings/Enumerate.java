package il.org.spartan.strings;

import static il.org.spartan.strings.StringUtils.*;
import static nano.ly.box.*;

import java.util.*;

import org.jetbrains.annotations.*;

public enum Enumerate {
  ;
  @NotNull public static String[] arabic(@NotNull final boolean[] bs, final char separator) {
    return arabic(box(bs), separator);
  }

  @NotNull public static String[] arabic(@NotNull final boolean[] bs, final String separator) {
    return arabic(box(bs), separator);
  }

  @NotNull public static String[] arabic(@NotNull final byte[] bs, final char separator) {
    return arabic(box(bs), separator);
  }

  @NotNull public static String[] arabic(@NotNull final byte[] bs, final String separator) {
    return arabic(box(bs), separator);
  }

  @NotNull public static String[] arabic(@NotNull final char[] cs, final char separator) {
    return arabic(box(cs), separator);
  }

  @NotNull public static String[] arabic(@NotNull final char[] cs, final String separator) {
    return arabic(box(cs), separator);
  }

  @NotNull public static String[] arabic(@NotNull final double[] ds, final char separator) {
    return arabic(box(ds), separator);
  }

  @NotNull public static String[] arabic(@NotNull final double[] ds, final String separator) {
    return arabic(box(ds), separator);
  }

  @NotNull public static String[] arabic(@NotNull final float[] fs, final char separator) {
    return arabic(box(fs), separator);
  }

  @NotNull public static String[] arabic(@NotNull final float[] fs, final String separator) {
    return arabic(box(fs), separator);
  }

  @NotNull public static String[] arabic(@NotNull final int[] ss, final char separator) {
    return arabic(box(ss), separator);
  }

  @NotNull public static String[] arabic(@NotNull final int[] ss, final String separator) {
    return arabic(box(ss), separator);
  }

  @NotNull public static List<String> arabic(@NotNull final Iterable<Object> os, final char separator) {
    @NotNull final List<String> $ = new ArrayList<>();
    int i = 1;
    for (final Object ¢ : os)
      $.add(++i + "" + separator + ¢);
    return $;
  }

  @NotNull public static List<String> arabic(@NotNull final Iterable<Object> os, final String separator) {
    @NotNull final List<String> $ = new ArrayList<>();
    int i = 1;
    for (final Object ¢ : os)
      $.add(++i + separator + ¢);
    return $;
  }

  @NotNull public static String[] arabic(@NotNull final long[] ls, final char separator) {
    return arabic(box(ls), separator);
  }

  @NotNull public static String[] arabic(@NotNull final long[] ls, final String separator) {
    return arabic(box(ls), separator);
  }

  @NotNull public static String[] arabic(@NotNull final Object[] os, final char separator) {
    @NotNull final String[] $ = new String[os.length];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = ¢ + 1 + "" + separator + os[¢];
    return $;
  }

  @NotNull public static String[] arabic(@NotNull final Object[] os, final String separator) {
    @NotNull final String[] $ = new String[os.length];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = ¢ + 1 + separator + os[¢];
    return $;
  }

  @NotNull public static String[] arabic(@NotNull final short[] ss, final char separator) {
    return arabic(box(ss), separator);
  }

  @NotNull public static String[] arabic(@NotNull final short[] ss, final String separator) {
    return arabic(box(ss), separator);
  }

  @NotNull public static String[] arabic(@NotNull final String[] ss, final char separator) {
    @NotNull final String[] $ = new String[ss.length];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = ¢ + 1 + separator + esc(ss[¢]);
    return $;
  }

  @NotNull public static String[] arabic(@NotNull final String[] ss, final String separator) {
    @NotNull final String[] $ = new String[ss.length];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = ¢ + 1 + separator + esc(ss[¢]);
    return $;
  }
}
