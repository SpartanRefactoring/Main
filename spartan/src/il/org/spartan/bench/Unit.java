/**
 *
 */
package il.org.spartan.bench;

import static il.org.spartan.utils.Box.*;
import static org.junit.Assert.*;

import java.text.*;

import org.junit.*;

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

  public final static String format(final StopWatch s) {
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
      assertEquals(-1, Unit.digits(0));
      assertEquals(-1, Unit.digits(0.000));
      assertEquals(0, Unit.digits(0.1));
      assertEquals(0, Unit.digits(0.9));
      assertEquals(1, Unit.digits(1));
      assertEquals(1, Unit.digits(9));
      assertEquals(2, Unit.digits(10));
      assertEquals(2, Unit.digits(99));
      assertEquals(3, Unit.digits(100));
      assertEquals(3, Unit.digits(120));
      assertEquals(3, Unit.digits(999));
    }

    @Test public void format3() {
      assertEquals("%.0f", Unit.format3(0));
      assertEquals("%.3f", Unit.format3(0.001));
      assertEquals("%.3f", Unit.format3(0.00099999999999));
      assertEquals("%.3f", Unit.format3(0.00049999999999));
    }

    @Test public void from10to100() {
      assertEquals("1", DOUBLE.format(1));
      assertEquals("1.12", DOUBLE.format(1.123));
      assertEquals("1.12", DOUBLE.format(1.1234));
      assertEquals("9.12", DOUBLE.format(9.1235));
      assertEquals("9.01", DOUBLE.format(9.01499999));
      assertEquals("9.00", DOUBLE.format(8.9999999999));
      assertEquals("10.0", DOUBLE.format(9.999));
      assertEquals("2.00", DOUBLE.format(1.9999));
    }

    @Test public void from10to1000() {
      assertEquals("212", DOUBLE.format(211.9));
    }

    @Test public void from1to10() {
      assertEquals("1", DOUBLE.format(1));
      assertEquals("1.12", DOUBLE.format(1.123));
      assertEquals("1.12", DOUBLE.format(1.1234));
      assertEquals("9.12", DOUBLE.format(9.1235));
      assertEquals("9.01", DOUBLE.format(9.01499999));
      assertEquals("9.00", DOUBLE.format(8.9999999999));
      assertEquals("10.0", DOUBLE.format(9.999));
      assertEquals("2.00", DOUBLE.format(1.9999));
    }

    @Test public void nanoSeconds() {
      assertEquals("1㎰", formatNanoseconds(1E-3));
      assertEquals("1㎱", formatNanoseconds(1));
      assertEquals("1㎲", formatNanoseconds(1E3));
      assertEquals("1㎳", formatNanoseconds(1E6));
      assertEquals("1s", formatNanoseconds(1E9));
      assertEquals("1s", formatNanoseconds(1000000000));
      assertEquals("224㎳", formatNanoseconds(223525012));
      assertEquals("304㎳", formatNanoseconds(304232501));
    }

    @Test public void percent2_3() {
      assertEquals("2.3%", formatRelative(0.02349));
    }

    @Test public void percent200() {
      assertEquals("200%", formatRelative(2));
    }

    @Test public void percent3_765() {
      assertEquals("3.8%", formatRelative(0.03765));
    }

    @Test public void percentMisc() {
      assertEquals("29%", formatRelative(0.2887));
      assertEquals("5.3%", formatRelative(0.0525));
      assertEquals("5.0%", formatRelative(0.0501));
      assertEquals("1.1%", formatRelative(0.01089));
      assertEquals("11%", formatRelative(0.1089));
    }

    @Test public void percentPerHunderdThousand() {
      assertEquals("0%", formatRelative(0.9 * 0.0001));
      assertEquals("0%", formatRelative(0.00001));
      assertEquals("0%", formatRelative(0.00001456));
      assertEquals("0%", formatRelative(0.00001556));
    }

    @Test public void percentPerMille() {
      assertEquals("0.1%", formatRelative(0.001));
      assertEquals("0.1%", formatRelative(0.001456));
      assertEquals("0.2%", formatRelative(0.001556));
    }

    @Test public void percentPerTenThousand() {
      assertEquals("0.01%", formatRelative(0.0001));
      assertEquals("0.01%", formatRelative(0.0001456));
      assertEquals("0.02%", formatRelative(0.0001556));
    }

    @Test public void percentZero() {
      assertEquals("0%", formatRelative(0));
    }

    @Test public void round3() {
      assertEquals(0.001, Unit.round3(0.001499999), 1E-10);
      assertEquals(0.000, Unit.round3(0.000499999), 1E-10);
      assertEquals(0.000, Unit.round3(0.00049999999999), 1E-10);
    }

    @Test public void roundToOne() {
      assertEquals("1.00", DOUBLE.format(0.9999));
    }

    @Test public void under0_001() {
      assertEquals("0.001", DOUBLE.format(0.00099999999999));
    }

    @Test public void under1() {
      assertEquals("0", DOUBLE.format(0));
      assertEquals("0.123", DOUBLE.format(0.123));
      assertEquals("0.123", DOUBLE.format(0.1234));
      assertEquals("0.124", DOUBLE.format(0.1235));
      assertEquals("0.001", DOUBLE.format(0.001499999));
      assertEquals("0.000", DOUBLE.format(0.00049999999999));
      assertEquals("0.999", DOUBLE.format(0.999));
    }
  }
}