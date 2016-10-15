package il.org.spartan.bench;

import static il.org.spartan.azzert.*;
import static il.org.spartan.utils.Box.*;
import static org.junit.Assert.assertEquals;

import java.text.*;

import org.junit.*;

import il.org.spartan.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2010 */
public enum Unit {
  INTEGER {
    @Override public String format(final double d) {
      return new DecimalFormat("###,###,###,###,###,###,###.00").format(d);
    }

    @Override public String format(final long l) {
      return new DecimalFormat("###,###,###,###,###,###,###").format(l);
    }
  },
  DOUBLE {
    @Override public String format(final double d) {
      return String.format(format3(d), box(d));
    }
  },
  BYTES() {
    public static final long Kb = 1L << 10;
    public static final long Mb = 1L << 20;
    public static final long Gb = 1L << 30;
    public static final long Tb = 1L << 40;
    public static final long Pb = 1L << 50;
    public static final long Eb = 1L << 60;

    @Override public String format(final double m) {
      if (Double.isNaN(m))
        return "NaN";
      if (m < 0)
        return "-" + format(-m);
      if (Double.isInfinite(m))
        return "∞";
      if (m < Kb)
        return format(m, 1, "B");
      if (m < Mb)
        return format(m, Kb, "㎅");
      if (m < Gb)
        return format(m, Mb, "㎆");
      if (m < Tb)
        return format(m, Gb, "㎇");
      if (m < Pb)
        return format(m, Tb, "TB");
      if (m < Eb)
        return format(m, Pb, "PB");
      return format(m, Eb, "EB");
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
    @Override public String format(final double s) {
      if (Double.isNaN(s))
        return "NaN";
      if (s < 0)
        return "-" + format(-s);
      if (Double.isInfinite(s))
        return "∞";
      if (s >= 7 * 24 * 60 * 60)
        return format(s, 7 * 24 * 60 * 60, "wk");
      if (s >= 24 * 60 * 60)
        return format(s, 24 * 60 * 60, "day");
      if (s >= 60 * 60)
        return format(s, 24 * 60, "hr");
      if (s >= 60)
        return format(s, 60, "min");
      if (s >= 1)
        return format(s, 1, "s");
      if (s >= 1E-3)
        return format(s, 1E-3, "㎳");
      if (s >= 1E-6)
        return format(s, 1E-6, "㎲");
      if (s >= 1E-9)
        return format(s, 1E-9, "㎱");
      return format(s, 1E-12, "㎰");
    }
  },
  RELATIVE {
    @Override public String format(final double d) {
      return formatRelative(d);
    }
  };
  /** A field for identifying a streamed version of objects of this class; we
   * use the values of <code>1L</code> to maintain upward compatibility. */
  public static final long serialVersionUID = 1L;
  public final static double PERCENT = 0.01;
  public final static int NANOSECOND = 1;
  public final static int MICROSECOND = 1000 * NANOSECOND;
  public final static int MILLISECOND = 1000 * MICROSECOND;
  public final static long SECOND = 1000 * MILLISECOND;
  public final static long MINUTE = 60 * SECOND;

  public static int digits(final double d) {
    if (d == 0)
      return -1;
    final double log = Math.log10(d);
    return log < 0 ? 0 : 1 + (int) log;
  }

  public static String format(final double v, final double scale, final String units) {
    return format(v / scale, units);
  }

  public static String format(final double d, final String units) {
    return String.format(format3(d), box(d)) + units;
  }

  public final static String format(final Stopwatch s) {
    return formatNanoseconds(s.time());
  }

  public static String format2(final double d) {
    if (d < 0)
      return "-" + format2(-d);
    final double p = 100 * d;
    if (p < 0.01)
      return "%.0f";
    if (p < 0.1)
      return "%.2f";
    if (p < 1)
      return "%.1f";
    if (p < 10)
      return "%.1f";
    if (p < 100)
      return "%.0f";
    if (p < 1000)
      return "%.0f";
    return "%5.0g";
  }

  public static String format3(final double d) {
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

  public static String formatNanoseconds(final double t) {
    return NANOSECONDS.format(t);
  }

  public static String formatNanoseconds(final long l) {
    return NANOSECONDS.format(l);
  }

  public static String formatRelative(final double d) {
    return String.format(format2(d) + "%%", box(100 * d));
  }

  public static String formatRelative(final double d1, final double d2) {
    return formatRelative(d1 / d2);
  }

  public static String thousands(final long l) {
    return INTEGER.format(l);
  }

  static double round3(final double d) {
    switch (digits(d)) {
      case -1:
      case 0:
        return Math.round(1000 * d) / 1000.0;
      case 1:
        return Math.round(100 * d) / 100.0;
      case 2:
        return Math.round(10 * d) / 10.0;
      default:
        return d;
    }
  }

  public abstract String format(final double d);

  public final String format(final Double d) {
    return format(d.doubleValue());
  }

  public String format(final long l) {
    return format((double) l);
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
      azzert.that(formatRelative(0.9 * 0.0001), is("0%"));
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