package il.org.spartan.bench;

import static fluent.ly.___.ensure;
import static fluent.ly.___.require;
import static fluent.ly.azzert.is;
import static il.org.spartan.bench.LogBook.Consolidation.BOTH;
import static il.org.spartan.bench.LogBook.Consolidation.ENDS;
import static il.org.spartan.bench.LogBook.Consolidation.LIST;
import static il.org.spartan.bench.LogBook.Consolidation.SUMMARY;
import static il.org.spartan.bench.Unit.BYTES;
import static il.org.spartan.bench.Unit.DOUBLE;
import static il.org.spartan.bench.Unit.MILLISECONDS;
import static il.org.spartan.bench.Unit.NANOSECONDS;
import static il.org.spartan.bench.Unit.RELATIVE;
import static il.org.spartan.strings.StringUtils.atod;
import static il.org.spartan.strings.StringUtils.atoi;
import static il.org.spartan.strings.StringUtils.isDouble;
import static il.org.spartan.strings.StringUtils.isInt;
import static il.org.spartan.strings.StringUtils.signum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import fluent.ly.Iterables;
import fluent.ly.azzert;
import fluent.ly.idiomatic;
import fluent.ly.the;
import il.org.spartan.statistics.ImmutableStatistics;
import il.org.spartan.statistics.RealStatistics;
import il.org.spartan.statistics.Statistics;
import il.org.spartan.utils.Accumulator;
import il.org.spartan.utils.Accumulator.Counter;
import il.org.spartan.utils.Tab;

/** Represents an experiments log-book, that is a repository which stores a
 * collection of measurements carried out in a scientific experiment, or a
 * sequence of these.
 * <p>
 * An entry of the log-book, represented as class {@link Entry}, has two
 * components:
 * <ol>
 * <li>Experimental settings, represented as class {@link Setting}, which is
 * simply a set of "Key=Value" strings.
 * <li>Measurements, which are a set of <code><b>double</b></code> values,
 * represented as class {@link RealStatistics}.
 * <li>Unit of measurement, defined by class {@link Unit}.
 * <p>
 * Multiple measurements are typically stored with each settings.
 * <p>
 * This class does not allow any modifications to the book contents. For actual
 * logging, use an instance of the sub-class {@link Mutable}; when the
 * experiments are done, it is wise to up-cast, so as to prevent further
 * modifications.
 * @author Yossi Gil
 * @since 02/03/2011
 * @see Mutable */
public abstract class LogBook implements Serializable {
  /** A field for identifying a streamed version of objects of this class; we
   * use the values of <code>1L</code> to maintain upward compatibility. */
  public static final long serialVersionUID = 1;

  public static void removeKeys(final Iterable<Entry> es, final Keys exclude) {
    for (final Entry ¢ : es)
      removeKeys(¢, exclude);
  }
  public static Setting removeKeys(final Setting s, final Keys k) {
    final Setting $ = new Setting();
    s.keySet().stream().filter(λ -> !k.contains(λ)).forEach(λ -> $.put(λ, s.get(λ)));
    return $;
  }
  static Keys values(final Iterable<Entry> es, final String key) {
    final Keys $ = new Keys();
    for (final Entry e : es) {
      final String v = e.get(key);
      if (v != null)
        $.add(v);
    }
    return $;
  }

  protected final Entries book = new Entries();
  protected String format;

  /** @return the union of all keys of all entries */
  public final Keys allKeys() {
    final Keys $ = new Keys();
    for (final Entry ¢ : book)
      $.addAll(¢.keySet());
    return $;
  }
  /** @return the set of keys whose values are identical in all entries */
  public final Keys commonKeys() {
    return allKeys().stream().filter(this::hasCommonValue).collect(Collectors.toCollection(Keys::new));
  }
  /** @return the {@link Setting} common to all entries */
  public final Setting commonSettings() {
    final Setting $ = new Setting();
    for (final String key : commonKeys())
      $.put(key, commonValue(key));
    return $;
  }
  /** @param key an arbitrary key
   * @return if <em>all</em> entries associate the same value associated with
   *         the parameter, then this value, otherwise
   *         <code><b>null</b></code> */
  public final String commonValue(final String key) {
    final String $ = someValue(key);
    if ($ != null)
      for (final Entry ¢ : book)
        if (!$.equals(¢.get(key)))
          return null;
    return $;
  }
  public LogBook demote(final String key) {
    for (final Entry ¢ : book)
      ¢.demote(key);
    return this;
  }
  public final Setting distinctSettings(final Setting ¢) {
    final Setting $ = (Setting) ¢.clone();
    removeKeys($, commonKeys());
    return $;
  }
  /** @return an {@link Iterable} over entries */
  public final Iterable<Entry> entries() {
    return book;
  }
  public final boolean hasCommonValue(final String key) {
    final String $ = someValue(key);
    if ($ != null)
      for (final Entry ¢ : book)
        if (!$.equals(¢.get(key)))
          return false;
    return true;
  }
  public void merge(final File f) throws IOException, ClassNotFoundException {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
      merge((LogBook) in.readObject());
    }
  }
  public void merge(final LogBook other) {
    book.addAll(other.book);
  }
  public LogBook printBy(final Consolidation m, final String... keys) {
    close();
    sortBy(keys);
    final Keys $ = commonKeys(), distinctKeys = new Keys();
    for (final String key : keys)
      if (!$.contains(key))
        distinctKeys.add(key);
    System.out.println(commonSettings());
    return new Consolidator(m, $, distinctKeys).go(book.clone());
  }
  public LogBook remove(final String key) {
    for (final Entry ¢ : book)
      ¢.remove(key);
    return this;
  }
  public final void removeCommon() {
    commonKeys().forEach(this::remove);
  }
  public final LogBook setFormat(final String format) {
    this.format = format;
    return this;
  }
  /** @return the number of entries in this instance */
  public final int size() {
    return book.size();
  }
  public final String someValue(final String key) {
    for (final Entry $ : book)
      return $.get(key);
    return null;
  }
  public final LogBook sortBy(final String... keys) {
    final Entry[] es = Iterables.toArray(book, LogBook.Entry.class);
    Arrays.sort(es, new Comparator<Entry>() {
      @Override public int compare(final Entry e1, final Entry e2) {
        int $;
        for (final String key : keys)
          if (($ = compare(e1.get(key), e2.get(key))) != 0)
            return $;
        return 0;
      }
      int compare(final String s1, final String s2) {
        return s1 != null ? compareNumeric(s1, s2) : s2 == null ? 0 : 1;
      }
      int compareNumeric(final String s1, final String s2) {
        return isInt(s1) && isInt(s2) ? atoi(s1) - atoi(s2)
            : isDouble(s1) && isDouble(s2) ? signum(atod(s1) - atod(s2))
                : isInt(s1) || isDouble(s1) ? -1 : isInt(s2) || isDouble(s2) ? 1 : s1.compareTo(s2);
      }
    });
    book.clear();
    Iterables.addAll(book, es);
    return this;
  }
  public final Keys values(final String key) {
    return values(book, key);
  }
  /** @param ¢ a file to write to
   * @return <code><b>this</b></code>
   * @throws IOException in case of failure */
  public LogBook writeTo(final File ¢) throws IOException {
    try (FileOutputStream s = new FileOutputStream(¢)) {
      return writeTo(s);
    }
  }
  /** @param s a file to write to
   * @return <code><b>this</b></code>
   * @throws IOException in case of failure */
  public LogBook writeTo(final FileOutputStream s) throws IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(s)) {
      out.writeObject(this);
      return this;
    }
  }
  /** @param fileName name of file to write to
   * @return <code><b>this</b></code>
   * @throws IOException in case of failure */
  public LogBook writeTo(final String fileName) throws IOException {
    return writeTo(new File(fileName));
  }
  protected LogBook clear() {
    book.clear();
    return this;
  }
  abstract LogBook close();

  public enum Consolidation {
    LIST, SUMMARY, BOTH, ENDS
  }

  public static class Entries extends ArrayList<LogBook.Entry> {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1;

    @Override public Entries clone() {
      return (Entries) super.clone();
    }
  }

  public final class Entry extends Setting {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1;
    public Unit unit;
    public final RealStatistics records = new RealStatistics();

    Entry(final Setting s) {
      putAll(s);
    }
    public String format() {
      return format(format);
    }
    public String format(final String newFormat) {
      return records.format(unit, newFormat);
    }
    public double[] recorded() {
      return records.all();
    }
    public String settings() {
      return super.toString();
    }
    @Override public String toString() {
      return records.format(unit);
    }
    public Unit unit() {
      return unit;
    }
    void add(final double ¢) {
      records.record(idiomatic.box(¢));
    }
    void add(final long ¢) {
      add(1. * ¢);
    }
    Entry setUnit(final Unit ¢) {
      require(unit == null || ¢ == unit);
      unit = ¢;
      ensure(¢ == unit);
      return this;
    }
  }

  public static final class Keys extends LinkedHashSet<String> {
    private static final long serialVersionUID = 1;

    @Override public String toString() {
      return size() != 1 ? super.toString() : the.firstOf(this) + "";
    }
  }

  public static class Mutable extends LogBook {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1;

    public static Mutable readFrom(final File ¢) throws IOException, ClassNotFoundException {
      try (ObjectInputStream $ = new ObjectInputStream(new FileInputStream(¢))) {
        return (Mutable) $.readObject();
      }
    }
    public static Mutable readFrom(final String fileName) throws IOException, ClassNotFoundException {
      return readFrom(new File(fileName));
    }
    private static double divide(final long d, final long l) {
      return 1. * d * l / 1.;
    }
    private static Class<?> getClass(final Object ¢) {
      return ¢ instanceof Class ? (Class<?>) ¢ : ¢.getClass();
    }

    protected final Setting current = new Setting();
    protected final transient Dotter dotter = new Dotter();

    /** Instantiate {@link Mutable}.
     * @param initiator The {@link Class} object of the class that initiated the
     *        logging, or an instance of that class. */
    public Mutable(final Object initiator) {
      if (initiator != null)
        set("Initiator", getClass(initiator).getSimpleName());
    }
    @Override public final LogBook close() {
      if (dotter != null)
        dotter.end();
      return this;
    }
    public Setting current() {
      return current;
    }
    public Entry currentEntry() {
      return find();
    }
    @Override public LogBook demote(final String key) {
      super.demote(key);
      current.demote(key);
      return this;
    }
    public Entry find() {
      for (final Entry ¢ : entries())
        if (current.equals(¢))
          return ¢;
      final Entry $ = new Entry(current);
      book.add($);
      return $;
    }
    public Mutable record(final double ¢) {
      return record(¢, DOUBLE);
    }
    public Mutable record(final double d, final Unit u) {
      find().setUnit(u).add(d);
      if (dotter != null)
        dotter.click();
      return this;
    }
    public void record(final long v, final long l) {
      record(divide(v, l));
    }
    public LogBook record(final Stopwatch s, final long l) {
      return recordNanoseconds(s.time(), l);
    }
    public LogBook recordBytes(final double bytes) {
      return record(bytes, BYTES);
    }
    public LogBook recordBytes(final long bytes) {
      return recordBytes(1. * bytes);
    }
    public LogBook recordMilliseconds(final double ¢) {
      return record(¢, MILLISECONDS);
    }
    public LogBook recordMilliseconds(final long time, final long l) {
      return recordMilliseconds(divide(time, l));
    }
    public LogBook recordNanoseconds(final double ¢) {
      return record(¢, NANOSECONDS);
    }
    public LogBook recordNanoseconds(final long time, final long l) {
      return recordNanoseconds(divide(time, l));
    }
    public LogBook recordRelative(final double ¢) {
      return record(¢, RELATIVE);
    }
    public void recordRelative(final long l1, final long l2) {
      recordRelative(1. * l1 / l2);
    }
    public void recordRelative(final Stopwatch s1, final Stopwatch s2) {
      recordRelative(s1.time(), s2.time());
    }
    @Override public LogBook remove(final String key) {
      super.remove(key);
      current.remove(key);
      return this;
    }
    public Mutable set(final String option, final boolean value) {
      return set(option, value + "");
    }
    public Mutable set(final String option, final double value) {
      return set(option, value + "");
    }
    public Mutable set(final String option, final float value) {
      return set(option, value + "");
    }
    public Mutable set(final String option, final int value) {
      return set(option, value + "");
    }
    public Mutable set(final String option, final long value) {
      return set(option, value + "");
    }
    public Mutable set(final String option, final String value) {
      current.put(option, value);
      return this;
    }

    public static final class TEST {
      final Accumulator c = new Counter();
      final Mutable myBook = new Mutable(this).set("day", "Tuesday").record(13).set("B", c.value()).record(13).set("day", "Tuesday").record(13)
          .set("B", c.value()).record(13).set("C", c.value()).record(13).set("day", "Tuesday").record(13).set("D", c.value()).record(13)
          .set("day", "Tuesday");

      @Test public void containsKey() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        assert l.find().containsKey("A");
        assert l.find().containsKey("B");
        assert l.find().containsKey("C");
        assert l.find().containsKey("D");
      }
      @Test public void create() {
        assert new Mutable(this) != null;
      }
      @Test public void demoteCurrent() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.demote("C");
        azzert.that(l.current().keySet() + "", is("[A, B, D, C]"));
        azzert.that(l.size(), is(0));
      }
      @Test public void demoteEntry() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.demote("C");
        azzert.that(l.find().keySet() + "", is("[A, B, D, C]"));
      }
      @Test public void demoteWithRecord() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.demote("C");
        azzert.that(l.size(), is(0));
        l.demote("B");
        l.record(12);
        azzert.that(l.size(), is(1));
        l.demote("A");
        l.record(13);
        azzert.that(l.size(), is(1));
        azzert.that(l.find().keySet() + "", is("[D, C, B, A]"));
      }
      @Test public void findEntry1() {
        final Mutable l = new Mutable(this).set("day", "Tuesday").set("time", 12);
        l.record(1);
        azzert.that(l.find().records.n(), is(1));
      }
      @Test public void findEntry3() {
        final Mutable l = new Mutable(this);
        l.set("day", "Tuesday").set("time", 12);
        l.record(1).record(2).record(3);
        azzert.that(l.find().records.n(), is(3));
      }
      @Test public void findNonNullEntry() {
        assert new Mutable(this).set("day", "Tuesday").set("time", 12).find().records != null;
      }
      @Test public void keyOrder() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        azzert.that(l.find().keySet() + "", is("[A, B, C, D]"));
      }
      @Test public void keySet() {
        final Mutable l = new Mutable(this);
        l.set("day", "Tuesday");
        l.set("time", 12);
        final Set<String> ss = l.find().keySet();
        azzert.that(ss.size(), is(2));
        azzert.that(ss.size(), is(2));
        assert ss.contains("day");
        assert ss.contains("time");
      }
      @Test public void readWrite() throws Exception {
        final File f = new File("/tmp/delme.lgb");
        try {
          readFrom(f);
        } catch (final FileNotFoundException ¢) {
          ¢.printStackTrace();
        }
        myBook.writeTo(f);
      }
      @Test public void remove() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.remove("C");
        azzert.that(l.size(), is(0));
        l.remove("B");
        l.record(12);
        azzert.that(l.size(), is(1));
        l.remove("A");
        l.record(13);
        azzert.that(l.size(), is(1));
        azzert.that(l.find().keySet() + "", is("[D]"));
      }
      @Test public void repeatedSettingsCount() {
        int n = 0;
        final Mutable l = new Mutable(this);
        l.set("day", "Tuesday");
        l.set("time", 12);
        l.record(n++);
        l.set("day", "Wednesday");
        l.record(n++);
        l.set("time", 13);
        l.record(n++);
        l.set("day", "Tuesday");
        l.record(n++);
        l.set("time", 12);
        l.record(n++);
        azzert.that(l.find().records.n(), is(2));
      }
      @Test public void repeatedSettingsSize() {
        int n = 0;
        final Mutable l = new Mutable(this);
        l.set("day", "Tuesday");
        azzert.that(l.size(), is(0));
        l.set("time", 12);
        azzert.that(l.size(), is(0));
        l.record(n++);
        azzert.that(l.size(), is(1));
        l.set("day", "Wednesday");
        l.record(n++);
        l.set("time", 13);
        l.record(n++);
        l.set("day", "Tuesday");
        l.record(n++);
        l.set("time", 12);
        l.record(n++);
        azzert.that(l.size(), is(4));
      }
      @Test public void sizeZero() {
        azzert.that(new Mutable(this).size(), is(0));
      }
      @Test public void value() {
        azzert.isNull(myBook.commonValue("B"));
        azzert.isNull(myBook.commonValue("C"));
        azzert.isNull(myBook.commonValue("D"));
        azzert.isNull(myBook.commonValue("A"));
        azzert.isNull(myBook.commonValue("E"));
        azzert.that(myBook.commonValue("day"), is("Tuesday"));
      }
    }
  }

  public static class Setting extends LinkedHashMap<String, String> {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1;

    public final void demote(final String key) {
      if (!containsKey(key))
        return;
      final String value = get(key);
      remove(key);
      put(key, value);
    }
    @Override public final Values values() {
      return new Values();
    }
  }

  public static final class Values extends ArrayList<String> {
    private static final long serialVersionUID = 1;

    @Override public String toString() {
      return size() != 1 ? super.toString() : the.firstOf(this) + "";
    }
  }

  class Consolidator extends Tab {
    private final Keys exclude;
    private final Keys stagger;
    private final Consolidation mode;

    public Consolidator(final Consolidation mode, final Keys exclude, final Keys stagger) {
      super("  ");
      this.exclude = exclude;
      this.stagger = stagger;
      this.mode = mode;
    }
    public final LogBook go(final Collection<Entry> $) {
      if (!stagger.isEmpty())
        return go($, the.firstOf(stagger));
      if (mode == LIST || mode == BOTH)
        for (final Entry ¢ : $)
          println(prefix(removeKeys(¢, exclude)) + ¢.format());
      if (mode == SUMMARY || mode == BOTH || mode == ENDS && $.size() > 1)
        summary($);
      if (mode != ENDS && $.size() <= 1)
        return LogBook.this;
      final Entry min = min($), max = max($);
      System.out.println(shortForm(min) + compare(min, max) + ratio(min, max) + shortForm(max));
      return LogBook.this;
    }
    private String compare(final Entry e1, final Entry e2) {
      return compare(e1.records, e2.records);
    }
    private String compare(final Statistics s1, final Statistics s2) {
      return new WelchT(s1, s2).p < 0.001 ? "==" : "~" + (new WelchT(s1, s2).p > 0.1 ? "" : RELATIVE.format(new WelchT(s1, s2).p) + "") + "~";
    }
    private boolean equals(final String s1, final String s2) {
      return s1 == null ? s2 == null : s1.equals(s2);
    }
    private LogBook go(final Collection<Entry> es, final String key) {
      exclude.add(key);
      stagger.remove(key);
      for (final String value : values(es, key)) {
        println(key, "=", value);
        more();
        go(select(es, key, value));
        less();
      }
      exclude.remove(key);
      stagger.add(key);
      return LogBook.this;
    }
    private Entry max(final Collection<Entry> es) {
      Entry $ = the.firstOf(es);
      for (final Entry ¢ : es)
        if (¢.records.median() > $.records.median())
          $ = ¢;
      return $;
    }
    private Entry min(final Collection<Entry> es) {
      Entry $ = the.firstOf(es);
      for (final Entry ¢ : es)
        if (¢.records.median() < $.records.median())
          $ = ¢;
      return $;
    }
    private String prefix(final Setting ¢) {
      return ¢.isEmpty() ? "" : ¢.values() + ": ";
    }
    private String ratio(final Entry e1, final Entry e2) {
      return String.format("%.2f*", idiomatic.box(e1.records.median() / e2.records.median())) + "";
    }
    private Collection<Entry> select(final Iterable<Entry> es, final String key, final String value) {
      final List<Entry> $ = new ArrayList<>();
      for (final Entry ¢ : es)
        if (equals(¢.get(key), value))
          $.add(¢);
      return $;
    }
    private String shortForm(final Entry ¢) {
      return removeKeys(¢, exclude).values() + ¢.format(" Jn");
    }
    private LogBook summary(final Entry[] es) {
      Arrays.sort(es, (e1, e2) -> signum(e1.records.median() - e2.records.median()));
      final StringBuilder s = new StringBuilder();
      for (int i = 0; i < es.length; ++i) {
        if (i > 0) {
          final ImmutableStatistics s1 = es[i - 1].records, s2 = es[i].records;
          s.append(" ");
          final double p = new WelchT(s1, s2).p;
          if (p < 0.001)
            s.append("== ");
          else if (p > 0.1)
            s.append("~~ ");
          else
            s.append("~").append(RELATIVE.format(p)).append("~ ");
          s.append(String.format("%.2f*", idiomatic.box(s1.median() / s2.median())));
        }
        s.append(shortForm(es[i]));
      }
      println(s);
      return LogBook.this;
    }
    private LogBook summary(final Iterable<Entry> ¢) {
      return summary(Iterables.toArray(¢, Entry.class));
    }
  }
}
