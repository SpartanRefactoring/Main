package il.org.spartan.bench;

import static fluent.ly.azzert.is;
import static org.junit.Assert.assertEquals;

import java.text.DecimalFormat;

import org.junit.Test;

import fluent.ly.azzert;
import fluent.ly.idiomatic;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2010 */
public enum Unit {
  INTEGER {
    @Override public String format(final double ¢) {
      return new DecimalFormat("###,###,###,###,###,###,###.00").format(¢);
    }

    @Override public String format(final long ¢) {
      return new DecimalFormat("###,###,###,###,###,###,###").format(¢);
    }
  },
  DOUBLE {
    @Override public String format(final double ¢) {
      return String.format(format3(¢), idiomatic.box(¢));
    }
  },
  BYTES() {
    static final long Kb = 1L << 10;
    static final long Mb = 1L << 20;
    static final long Gb = 1L << 30;
    static final long Tb = 1L << 40;
    static final long Pb = 1L << 50;
    static final long Eb = 1L << 60;

    @Override public String format(final double m) {
      return Double.isNaN(m) ? "NaN"
          : m < 0 ? "-" + format(-m)
              : Double.isInfinite(m) ? "∞"
                  : m < Kb ? format(m, 1, "B")
                      : m < Mb ? format(m, Kb, "㎅")
                          : m < Gb ? format(m, Mb, "㎆")
                              : m < Tb ? format(m, Gb, "㎇") //
                                  : m < Pb ? format(m, Tb, "TB") //
                                      : m < Eb ? format(m, Pb, "PB") //
                                          : format(m, Eb, "EB");
    }
  },
  NANOSECONDS {
    @Override public String format(final double ns) {
      return SECONDS.format(ns / 1E9);
    }
  },
  MILLISECONDS {
    @Override public String format(final double ms) {
      return SECONDS.format(ms / 1E3);
    }
  },
  SECONDS {
    @Override public String format(final double ¢) {
      return Double.isNaN(¢) ? "NaN"
          : ¢ < 0 ? "-" + format(-¢)
              : Double.isInfinite(¢) ? "∞"
                  : ¢ >= 604800 ? format(¢, 604800, "wk")
                      : ¢ >= 86400 ? format(¢, 86400, "day")
                          : ¢ >= 3600 ? format(¢, 1440, "hr")
                              : ¢ >= 60 ? format(¢, 60, "min")
                                  : ¢ >= 1 ? format(¢, 1, "s")
                                      : ¢ >= 1E-3 ? format(¢, 1E-3, "㎳")
                                          : ¢ >= 1E-6 ? format(¢, 1E-6, "㎲")
                                              : ¢ >= 1E-9 ? format(¢, 1E-9, "㎱") : format(¢, 1E-12, "㎰");
    }
  },
  RELATIVE {
    @Override public String format(final double ¢) {
      return formatRelative(¢);
    }
  };

  /** A field for identifying a streamed version of objects of this class; we use
   * the values of <code>1L</code> to maintain upward compatibility. */
  public static final long serialVersionUID = 1;
  public static final double PERCENT = 0.01;
  public static final int NANOSECOND = 1;
  public static final int MICROSECOND = 1000 * NANOSECOND;
  public static final int MILLISECOND = 1000 * MICROSECOND;
  public static final long SECOND = 1000 * MILLISECOND;
  public static final long MINUTE = 60 * SECOND;

  public static int digits(final double ¢) {
    if (¢ == 0)
      return -1;
    final double $ = Math.log10(¢);
    return $ < 0 ? 0 : (int) $ + 1;
  }

  public static String format(final double v, final double scale, final String units) {
    return format(v / scale, units);
  }

  public static String format(final double d, final String units) {
    return String.format(format3(d), idiomatic.box(d)) + units;
  }

  public static String format(final Stopwatch ¢) {
    return formatNanoseconds(¢.time());
  }

  public static String format2(final double ¢) {
    return ¢ < 0 ? "-" + format2(-¢)
        : "%" + (100 * ¢ < 0.01 ? ".0f"
            : 100 * ¢ < 0.1 ? ".2f"
                : 100 * ¢ < 1 || 100 * ¢ < 10 ? ".1f" : 100 * ¢ < 100 || 100 * ¢ < 1000 ? ".0f" : "5.0g");
  }

  public static String format3(final double ¢) {
    if (¢ == 0 || ¢ >= 1 && ¢ - (int) ¢ < 0.0005)
      return "%.0f";
    switch (digits(round3(¢))) {
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

  public static String formatNanoseconds(final double t) {
    return NANOSECONDS.format(t);
  }

  public static String formatNanoseconds(final long ¢) {
    return NANOSECONDS.format(¢);
  }

  public static String formatRelative(final double ¢) {
    return String.format(format2(¢) + "%%", idiomatic.box(100 * ¢));
  }

  public static String formatRelative(final double d1, final double d2) {
    return formatRelative(d1 / d2);
  }

  public static String thousands(final long ¢) {
    return INTEGER.format(¢);
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

  public abstract String format(double d);

  public String format(final Double ¢) {
    return format(¢.doubleValue());
  }

  public String format(final long ¢) {
    return format(1. * ¢);
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void digits() {
      azzert.that(Unit.digits(0), is(-1));
      azzert.that(Unit.digits(0.000), is(-1));
      azzert.that(Unit.digits(0.1), is(0));
      azzert.that(Unit.digits(0.9), is(0));
      azzert.that(Unit.digits(1), is(1));
      azzert.that(Unit.digits(9), is(1));
      azzert.that(Unit.digits(10), is(2));
      azzert.that(Unit.digits(99), is(2));
      azzert.that(Unit.digits(100), is(3));
      azzert.that(Unit.digits(120), is(3));
      azzert.that(Unit.digits(999), is(3));
    }

    @Test public void format3() {
      azzert.that(Unit.format3(0), is("%.0f"));
      azzert.that(Unit.format3(0.001), is("%.3f"));
      azzert.that(Unit.format3(0.00099999999999), is("%.3f"));
      azzert.that(Unit.format3(0.00049999999999), is("%.3f"));
    }

    @Test public void from10to100() {
      azzert.that(DOUBLE.format(1), is("1"));
      azzert.that(DOUBLE.format(1.123), is("1.12"));
      azzert.that(DOUBLE.format(1.1234), is("1.12"));
      azzert.that(DOUBLE.format(9.1235), is("9.12"));
      azzert.that(DOUBLE.format(9.01499999), is("9.01"));
      azzert.that(DOUBLE.format(8.9999999999), is("9.00"));
      azzert.that(DOUBLE.format(9.999), is("10.0"));
      azzert.that(DOUBLE.format(1.9999), is("2.00"));
    }

    @Test public void from10to1000() {
      azzert.that(DOUBLE.format(211.9), is("212"));
    }

    @Test public void from1to10() {
      azzert.that(DOUBLE.format(1), is("1"));
      azzert.that(DOUBLE.format(1.123), is("1.12"));
      azzert.that(DOUBLE.format(1.1234), is("1.12"));
      azzert.that(DOUBLE.format(9.1235), is("9.12"));
      azzert.that(DOUBLE.format(9.01499999), is("9.01"));
      azzert.that(DOUBLE.format(8.9999999999), is("9.00"));
      azzert.that(DOUBLE.format(9.999), is("10.0"));
      azzert.that(DOUBLE.format(1.9999), is("2.00"));
    }

    @Test public void nanoSeconds() {
      azzert.that(formatNanoseconds(1E-3), is("1㎰"));
      azzert.that(formatNanoseconds(1), is("1㎱"));
      azzert.that(formatNanoseconds(1E3), is("1㎲"));
      azzert.that(formatNanoseconds(1E6), is("1㎳"));
      azzert.that(formatNanoseconds(1E9), is("1s"));
      azzert.that(formatNanoseconds(1000000000), is("1s"));
      azzert.that(formatNanoseconds(223525012), is("224㎳"));
      azzert.that(formatNanoseconds(304232501), is("304㎳"));
    }

    @Test public void percent2_3() {
      azzert.that(formatRelative(0.02349), is("2.3%"));
    }

    @Test public void percent200() {
      azzert.that(formatRelative(2), is("200%"));
    }

    @Test public void percent3_765() {
      azzert.that(formatRelative(0.03765), is("3.8%"));
    }

    @Test public void percentMisc() {
      azzert.that(formatRelative(0.2887), is("29%"));
      azzert.that(formatRelative(0.0525), is("5.3%"));
      azzert.that(formatRelative(0.0501), is("5.0%"));
      azzert.that(formatRelative(0.01089), is("1.1%"));
      azzert.that(formatRelative(0.1089), is("11%"));
    }

    @Test public void percentPerHunderdThousand() {
      azzert.that(formatRelative(9.0E-5), is("0%"));
      azzert.that(formatRelative(0.00001), is("0%"));
      azzert.that(formatRelative(0.00001456), is("0%"));
      azzert.that(formatRelative(0.00001556), is("0%"));
    }

    @Test public void percentPerMille() {
      azzert.that(formatRelative(0.001), is("0.1%"));
      azzert.that(formatRelative(0.001456), is("0.1%"));
      azzert.that(formatRelative(0.001556), is("0.2%"));
    }

    @Test public void percentPerTenThousand() {
      azzert.that(formatRelative(0.0001), is("0.01%"));
      azzert.that(formatRelative(0.0001456), is("0.01%"));
      azzert.that(formatRelative(0.0001556), is("0.02%"));
    }

    @Test public void percentZero() {
      azzert.that(formatRelative(0), is("0%"));
    }

    @Test public void round3() {
      assertEquals(0.001, Unit.round3(0.001499999), 1E-10);
      assertEquals(0.000, Unit.round3(0.000499999), 1E-10);
      assertEquals(0.000, Unit.round3(0.00049999999999), 1E-10);
    }

    @Test public void roundToOne() {
      azzert.that(DOUBLE.format(0.9999), is("1.00"));
    }

    @Test public void under0_001() {
      azzert.that(DOUBLE.format(0.00099999999999), is("0.001"));
    }

    @Test public void under1() {
      azzert.that(DOUBLE.format(0), is("0"));
      azzert.that(DOUBLE.format(0.123), is("0.123"));
      azzert.that(DOUBLE.format(0.1234), is("0.123"));
      azzert.that(DOUBLE.format(0.1235), is("0.124"));
      azzert.that(DOUBLE.format(0.001499999), is("0.001"));
      azzert.that(DOUBLE.format(0.00049999999999), is("0.000"));
      azzert.that(DOUBLE.format(0.999), is("0.999"));
    }
  }
}