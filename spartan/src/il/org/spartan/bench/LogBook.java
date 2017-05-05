package il.org.spartan.bench;

import static fluent.ly.azzert.*;
import static il.org.spartan.bench.LogBook.Consolidation.*;
import static il.org.spartan.bench.Unit.*;
import static il.org.spartan.strings.StringUtils.*;
import static fluent.ly.___.*;
import static fluent.ly.box.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.jetbrains.annotations.*;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.statistics.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.Accumulator.Counter;
import il.org.spatan.iteration.*;

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

  public static void removeKeys(@NotNull final Iterable<Entry> es, @NotNull final Keys exclude) {
    for (@NotNull final Entry ¢ : es)
      removeKeys(¢, exclude);
  }
  @NotNull public static Setting removeKeys(@NotNull final Setting s, @NotNull final Keys k) {
    @NotNull final Setting $ = new Setting();
    s.keySet().stream().filter(λ -> !k.contains(λ)).forEach(λ -> $.put(λ, s.get(λ)));
    return $;
  }
  @NotNull static Keys values(@NotNull final Iterable<Entry> es, final String key) {
    @NotNull final Keys $ = new Keys();
    for (@NotNull final Entry e : es) {
      final String v = e.get(key);
      if (v != null)
        $.add(v);
    }
    return $;
  }

  protected final Entries book = new Entries();
  protected String format;

  /** @return the union of all keys of all entries */
  @NotNull public final Keys allKeys() {
    @NotNull final Keys $ = new Keys();
    for (@NotNull final Entry ¢ : book)
      $.addAll(¢.keySet());
    return $;
  }
  /** @return the set of keys whose values are identical in all entries */
  @NotNull public final Keys commonKeys() {
    @NotNull final Keys $ = allKeys().stream().filter(this::hasCommonValue).collect(Collectors.toCollection(Keys::new));
    return $;
  }
  /** @return the {@link Setting} common to all entries */
  @NotNull public final Setting commonSettings() {
    @NotNull final Setting $ = new Setting();
    for (final String key : commonKeys())
      $.put(key, commonValue(key));
    return $;
  }
  /** @param key an arbitrary key
   * @return if <em>all</em> entries associate the same value associated with
   *         the parameter, then this value, otherwise
   *         <code><b>null</b></code> */
  public final String commonValue(final String key) {
    @Nullable final String $ = someValue(key);
    if ($ != null)
      for (@NotNull final Entry ¢ : book)
        if (!$.equals(¢.get(key)))
          return null;
    return $;
  }
  @NotNull public LogBook demote(final String key) {
    for (@NotNull final Entry ¢ : book)
      ¢.demote(key);
    return this;
  }
  @NotNull public final Setting distinctSettings(@NotNull final Setting ¢) {
    @NotNull final Setting $ = (Setting) ¢.clone();
    removeKeys($, commonKeys());
    return $;
  }
  /** @return an {@link Iterable} over entries */
  @NotNull public final Iterable<Entry> entries() {
    return book;
  }
  public final boolean hasCommonValue(final String key) {
    @Nullable final String $ = someValue(key);
    if ($ != null)
      for (@NotNull final Entry ¢ : book)
        if (!$.equals(¢.get(key)))
          return false;
    return true;
  }
  public void merge(@NotNull final File f) throws IOException, ClassNotFoundException {
    @NotNull final ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
    merge((LogBook) in.readObject());
    in.close();
  }
  public void merge(@NotNull final LogBook other) {
    book.addAll(other.book);
  }
  @NotNull public LogBook printBy(final Consolidation m, @NotNull final String... keys) {
    close();
    sortBy(keys);
    @NotNull final Keys $ = commonKeys(), distinctKeys = new Keys();
    for (final String key : keys)
      if (!$.contains(key))
        distinctKeys.add(key);
    System.out.println(commonSettings());
    return new Consolidator(m, $, distinctKeys).go(book.clone());
  }
  @NotNull public LogBook remove(final String key) {
    for (@NotNull final Entry ¢ : book)
      ¢.remove(key);
    return this;
  }
  public final void removeCommon() {
    commonKeys().forEach(this::remove);
  }
  @NotNull public final LogBook setFormat(final String format) {
    this.format = format;
    return this;
  }
  /** @return the number of entries in this instance */
  public final int size() {
    return book.size();
  }
  public final String someValue(final String key) {
    for (@NotNull final Entry $ : book)
      return $.get(key);
    return null;
  }
  @NotNull public final LogBook sortBy(@NotNull final String... keys) {
    @NotNull final Entry[] es = Iterables.toArray(book, LogBook.Entry.class);
    Arrays.sort(es, new Comparator<Entry>() {
      @Override public int compare(@NotNull final Entry e1, @NotNull final Entry e2) {
        int $;
        for (final String key : keys)
          if (($ = compare(e1.get(key), e2.get(key))) != 0)
            return $;
        return 0;
      }
      int compare(@Nullable final String s1, @NotNull final String s2) {
        return s1 != null ? compareNumeric(s1, s2) : s2 == null ? 0 : 1;
      }
      int compareNumeric(@NotNull final String s1, @NotNull final String s2) {
        return isInt(s1) && isInt(s2) ? atoi(s1) - atoi(s2)
            : isDouble(s1) && isDouble(s2) ? signum(atod(s1) - atod(s2))
                : isInt(s1) || isDouble(s1) ? -1 : isInt(s2) || isDouble(s2) ? 1 : s1.compareTo(s2);
      }
    });
    book.clear();
    Iterables.addAll(book, es);
    return this;
  }
  @NotNull public final Keys values(final String key) {
    return values(book, key);
  }
  /** @param ¢ a file to write to
   * @return <code><b>this</b></code>
   * @throws IOException in case of failure */
  @NotNull public LogBook writeTo(@NotNull final File ¢) throws IOException {
    return writeTo(new FileOutputStream(¢));
  }
  /** @param s a file to write to
   * @return <code><b>this</b></code>
   * @throws IOException in case of failure */
  @NotNull public LogBook writeTo(final FileOutputStream s) throws IOException {
    @NotNull final ObjectOutputStream out = new ObjectOutputStream(s);
    out.writeObject(this);
    out.close();
    return this;
  }
  /** @param fileName name of file to write to
   * @return <code><b>this</b></code>
   * @throws IOException in case of failure */
  @NotNull public LogBook writeTo(@NotNull final String fileName) throws IOException {
    return writeTo(new File(fileName));
  }
  @NotNull protected LogBook clear() {
    book.clear();
    return this;
  }
  @NotNull abstract LogBook close();

  public enum Consolidation {
    LIST, SUMMARY, BOTH, ENDS
  }

  public static class Entries extends ArrayList<LogBook.Entry> {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1;

    @Override @NotNull public Entries clone() {
      return (Entries) super.clone();
    }
  }

  public final class Entry extends Setting {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1;
    public Unit unit;
    public final RealStatistics records = new RealStatistics();

    Entry(@NotNull final Setting s) {
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
      records.record(box(¢));
    }
    void add(final long ¢) {
      add(1. * ¢);
    }
    @NotNull Entry setUnit(final Unit ¢) {
      require(unit == null || ¢ == unit);
      unit = ¢;
      ensure(¢ == unit);
      return this;
    }
  }

  public static final class Keys extends LinkedHashSet<String> {
    private static final long serialVersionUID = 1;

    @Override @NotNull public String toString() {
      return size() != 1 ? super.toString() : Iterables.first(this) + "";
    }
  }

  public static class Mutable extends LogBook {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1;

    @NotNull public static Mutable readFrom(@NotNull final File f) throws IOException, ClassNotFoundException {
      @NotNull final ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
      @NotNull final Mutable $ = (Mutable) in.readObject();
      in.close();
      return $;
    }
    @NotNull public static Mutable readFrom(@NotNull final String fileName) throws IOException, ClassNotFoundException {
      return readFrom(new File(fileName));
    }
    private static double divide(final long d, final long l) {
      return 1. * d * l / 1.;
    }
    @NotNull private static Class<?> getClass(final Object ¢) {
      return ¢ instanceof Class ? (Class<?>) ¢ : ¢.getClass();
    }

    protected final Setting current = new Setting();
    protected final transient Dotter dotter = new Dotter();

    /** Instantiate {@link Mutable}.
     * @param initiator The {@link Class} object of the class that initiated the
     *        logging, or an instance of that class. */
    public Mutable(@Nullable final Object initiator) {
      if (initiator != null)
        set("Initiator", getClass(initiator).getSimpleName());
    }
    @Override @NotNull public final LogBook close() {
      if (dotter != null)
        dotter.end();
      return this;
    }
    @NotNull public Setting current() {
      return current;
    }
    public Entry currentEntry() {
      return find();
    }
    @Override @NotNull public LogBook demote(final String key) {
      super.demote(key);
      current.demote(key);
      return this;
    }
    public Entry find() {
      for (final Entry ¢ : entries())
        if (current.equals(¢))
          return ¢;
      @NotNull final Entry $ = new Entry(current);
      book.add($);
      return $;
    }
    @NotNull public Mutable record(final double ¢) {
      return record(¢, DOUBLE);
    }
    @NotNull public Mutable record(final double d, final Unit u) {
      find().setUnit(u).add(d);
      if (dotter != null)
        dotter.click();
      return this;
    }
    public void record(final long v, final long l) {
      record(divide(v, l));
    }
    @NotNull public LogBook record(@NotNull final Stopwatch s, final long l) {
      return recordNanoseconds(s.time(), l);
    }
    @NotNull public LogBook recordBytes(final double bytes) {
      return record(bytes, BYTES);
    }
    @NotNull public LogBook recordBytes(final long bytes) {
      return recordBytes(1. * bytes);
    }
    @NotNull public LogBook recordMilliseconds(final double ¢) {
      return record(¢, MILLISECONDS);
    }
    @NotNull public LogBook recordMilliseconds(final long time, final long l) {
      return recordMilliseconds(divide(time, l));
    }
    @NotNull public LogBook recordNanoseconds(final double ¢) {
      return record(¢, NANOSECONDS);
    }
    @NotNull public LogBook recordNanoseconds(final long time, final long l) {
      return recordNanoseconds(divide(time, l));
    }
    @NotNull public LogBook recordRelative(final double ¢) {
      return record(¢, RELATIVE);
    }
    public void recordRelative(final long l1, final long l2) {
      recordRelative(1. * l1 / l2);
    }
    public void recordRelative(@NotNull final Stopwatch s1, @NotNull final Stopwatch s2) {
      recordRelative(s1.time(), s2.time());
    }
    @Override @NotNull public LogBook remove(final String key) {
      super.remove(key);
      current.remove(key);
      return this;
    }
    @NotNull public Mutable set(final String option, final boolean value) {
      return set(option, value + "");
    }
    @NotNull public Mutable set(final String option, final double value) {
      return set(option, value + "");
    }
    @NotNull public Mutable set(final String option, final float value) {
      return set(option, value + "");
    }
    @NotNull public Mutable set(final String option, final int value) {
      return set(option, value + "");
    }
    @NotNull public Mutable set(final String option, final long value) {
      return set(option, value + "");
    }
    @NotNull public Mutable set(final String option, final String value) {
      current.put(option, value);
      return this;
    }

    public static final class TEST {
      @NotNull final Accumulator c = new Counter();
      final Mutable myBook = new Mutable(this).set("day", "Tuesday").record(13).set("B", c.value()).record(13).set("day", "Tuesday").record(13)
          .set("B", c.value()).record(13).set("C", c.value()).record(13).set("day", "Tuesday").record(13).set("D", c.value()).record(13)
          .set("day", "Tuesday");

      @Test public void containsKey() {
        @NotNull final Mutable l = new Mutable(this);
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
        @NotNull final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.demote("C");
        azzert.that(l.current().keySet() + "", is("[A, B, D, C]"));
        azzert.that(l.size(), is(0));
      }
      @Test public void demoteEntry() {
        @NotNull final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.demote("C");
        azzert.that(l.find().keySet() + "", is("[A, B, D, C]"));
      }
      @Test public void demoteWithRecord() {
        @NotNull final Mutable l = new Mutable(this);
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
        @NotNull final Mutable l = new Mutable(this).set("day", "Tuesday").set("time", 12);
        l.record(1);
        azzert.that(l.find().records.n(), is(1));
      }
      @Test public void findEntry3() {
        @NotNull final Mutable l = new Mutable(this);
        l.set("day", "Tuesday").set("time", 12);
        l.record(1).record(2).record(3);
        azzert.that(l.find().records.n(), is(3));
      }
      @Test public void findNotNullEntry() {
        assert new Mutable(this).set("day", "Tuesday").set("time", 12).find().records != null;
      }
      @Test public void keyOrder() {
        @NotNull final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        azzert.that(l.find().keySet() + "", is("[A, B, C, D]"));
      }
      @Test public void keySet() {
        @NotNull final Mutable l = new Mutable(this);
        l.set("day", "Tuesday");
        l.set("time", 12);
        @NotNull final Set<String> ss = l.find().keySet();
        azzert.that(ss.size(), is(2));
        azzert.that(ss.size(), is(2));
        assert ss.contains("day");
        assert ss.contains("time");
      }
      @Test public void readWrite() throws Exception {
        @NotNull final File f = new File("/tmp/delme.lgb");
        try {
          readFrom(f);
        } catch (@NotNull final FileNotFoundException ¢) {
          ¢.printStackTrace();
        }
        myBook.writeTo(f);
      }
      @Test public void remove() {
        @NotNull final Mutable l = new Mutable(this);
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
        @NotNull final Mutable l = new Mutable(this);
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
        @NotNull final Mutable l = new Mutable(this);
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
    @Override @NotNull public final Values values() {
      @NotNull final Values $ = new Values();
      $.addAll(super.values());
      return $;
    }
  }

  public static final class Values extends ArrayList<String> {
    private static final long serialVersionUID = 1;

    @Override @NotNull public String toString() {
      return size() != 1 ? super.toString() : Iterables.first(this) + "";
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
    @NotNull public final LogBook go(@NotNull final Collection<Entry> $) {
      if (!stagger.isEmpty())
        return go($, Iterables.first(stagger));
      if (mode == LIST || mode == BOTH)
        for (@NotNull final Entry ¢ : $)
          println(prefix(removeKeys(¢, exclude)) + ¢.format());
      if (mode == SUMMARY || mode == BOTH || mode == ENDS && $.size() > 1)
        summary($);
      if (mode != ENDS && $.size() <= 1)
        return LogBook.this;
      final Entry min = min($), max = max($);
      System.out.println(shortForm(min) + compare(min, max) + ratio(min, max) + shortForm(max));
      return LogBook.this;
    }
    @NotNull private String compare(@NotNull final Entry e1, @NotNull final Entry e2) {
      return compare(e1.records, e2.records);
    }
    @NotNull private String compare(final Statistics s1, final Statistics s2) {
      return new WelchT(s1, s2).p < 0.001 ? "==" : "~" + (new WelchT(s1, s2).p > 0.1 ? "" : RELATIVE.format(new WelchT(s1, s2).p) + "") + "~";
    }
    private boolean equals(@Nullable final String s1, @Nullable final String s2) {
      return s1 == null ? s2 == null : s1.equals(s2);
    }
    @NotNull private LogBook go(@NotNull final Collection<Entry> es, final String key) {
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
    private Entry max(@NotNull final Collection<Entry> es) {
      Entry $ = Iterables.first(es);
      for (@NotNull final Entry ¢ : es)
        if (¢.records.median() > $.records.median())
          $ = ¢;
      return $;
    }
    private Entry min(@NotNull final Collection<Entry> es) {
      Entry $ = Iterables.first(es);
      for (@NotNull final Entry ¢ : es)
        if (¢.records.median() < $.records.median())
          $ = ¢;
      return $;
    }
    @NotNull private String prefix(@NotNull final Setting ¢) {
      return ¢.isEmpty() ? "" : ¢.values() + ": ";
    }
    @NotNull private String ratio(@NotNull final Entry e1, @NotNull final Entry e2) {
      return String.format("%.2f*", box(e1.records.median() / e2.records.median())) + "";
    }
    @NotNull private Collection<Entry> select(@NotNull final Iterable<Entry> es, final String key, final String value) {
      @NotNull final List<Entry> $ = new ArrayList<>();
      for (@NotNull final Entry ¢ : es)
        if (equals(¢.get(key), value))
          $.add(¢);
      return $;
    }
    @NotNull private String shortForm(@NotNull final Entry ¢) {
      return removeKeys(¢, exclude).values() + ¢.format(" Jn");
    }
    @NotNull private LogBook summary(@NotNull final Entry[] es) {
      Arrays.sort(es, (e1, e2) -> signum(e1.records.median() - e2.records.median()));
      @NotNull final StringBuilder s = new StringBuilder();
      for (int i = 0; i < es.length; ++i) {
        if (i > 0) {
          @NotNull final ImmutableStatistics s1 = es[i - 1].records, s2 = es[i].records;
          s.append(" ");
          final double p = new WelchT(s1, s2).p;
          if (p < 0.001)
            s.append("== ");
          else if (p > 0.1)
            s.append("~~ ");
          else
            s.append("~").append(RELATIVE.format(p)).append("~ ");
          s.append(String.format("%.2f*", box(s1.median() / s2.median())));
        }
        s.append(shortForm(es[i]));
      }
      println(s);
      return LogBook.this;
    }
    @NotNull private LogBook summary(@NotNull final Iterable<Entry> ¢) {
      return summary(Iterables.toArray(¢, Entry.class));
    }
  }
}
