package il.org.spartan.bench;

import static il.org.spartan.bench.LogBook.Consolidation.*;
import static il.org.spartan.bench.Unit.*;
import static il.org.spartan.strings.StringUtils.*;
import static il.org.spartan.utils.Box.*;
import static il.org.spartan.utils.____.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.*;

import il.org.spartan.statistics.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.Accumulator.Counter;
import il.org.spatan.iteration.*;

/** Represents an experiments log-book, that is a repository which stores a
 * collection of measurements carried out in an scientific experiments.
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
 * experiments are done, it is prudent to up-cast, so as to prevent further
 * modifications.
 * @author Yossi Gil
 * @since 02/03/2011
 * @see Mutable */
public abstract class LogBook implements Serializable {
  /** A field for identifying a streamed version of objects of this class; we
   * use the values of <code>1L</code> to maintain upward compatibility. */
  public static final long serialVersionUID = 1L;

  /** @param es
   * @param exclude */
  public static void removeKeys(final Iterable<Entry> es, final Keys exclude) {
    for (final Entry e : es)
      removeKeys(e, exclude);
  }

  public final static Setting removeKeys(final Setting s, final Keys keys) {
    final Setting $ = new Setting();
    for (final String key : s.keySet())
      if (!keys.contains(key))
        $.put(key, s.get(key));
    return $;
  }

  final static Keys values(final Iterable<Entry> es, final String key) {
    final Keys $ = new Keys();
    for (final Entry e : es) {
      final String v = e.get(key);
      if (v != null)
        $.add(v);
    }
    return $;
  }

  protected final Entries book = new Entries();
  protected String format = null;

  /** @return the union of all keys of all entries */
  public final Keys allKeys() {
    final Keys $ = new Keys();
    for (final Entry e : book)
      $.addAll(e.keySet());
    return $;
  }

  /** @return the set of keys whose values are identical in all entries */
  public final Keys commonKeys() {
    final Keys $ = new Keys();
    for (final String key : allKeys())
      if (hasCommonValue(key))
        $.add(key);
    return $;
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
      for (final Entry e : book)
        if (!$.equals(e.get(key)))
          return null;
    return $;
  }

  public LogBook demote(final String key) {
    for (final Entry e : book)
      e.demote(key);
    return this;
  }

  public final Setting distinctSettings(final Setting s) {
    final Setting $ = (Setting) s.clone();
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
      for (final Entry e : book)
        if (!$.equals(e.get(key)))
          return false;
    return true;
  }

  /** @param f
   * @throws IOException
   * @throws ClassNotFoundException
   * @throws FileNotFoundException */
  public void merge(final File f) throws IOException, ClassNotFoundException {
    final ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
    final LogBook l = (LogBook) in.readObject();
    merge(l);
    in.close();
  }

  /** @param other */
  public void merge(final LogBook other) {
    book.addAll(other.book);
  }

  public LogBook printBy(final Consolidation m, final String... keys) {
    close();
    sortBy(keys);
    final Keys commonKeys = commonKeys();
    final Keys distinctKeys = new Keys();
    for (final String key : keys)
      if (!commonKeys.contains(key))
        distinctKeys.add(key);
    System.out.println(commonSettings());
    return new Consolidator(m, commonKeys, distinctKeys).go(book.clone());
  }

  public LogBook remove(final String key) {
    for (final Entry e : book)
      e.remove(key);
    return this;
  }

  public final void removeCommon() {
    for (final String key : commonKeys())
      remove(key);
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
    for (final Entry e : book)
      return e.get(key);
    return null;
  }

  public final LogBook sortBy(final String... keys) {
    final Entry[] es = Iterables.toArray(book, LogBook.Entry.class);
    Arrays.sort(es, new Comparator<Entry>() {
      @Override public final int compare(final Entry e1, final Entry e2) {
        int $;
        for (final String key : keys)
          if (($ = compare(e1.get(key), e2.get(key))) != 0)
            return $;
        return 0;
      }

      int compareNumeric(final String s1, final String s2) {
        if (isInt(s1) && isInt(s2))
          return atoi(s1) - atoi(s2);
        if (isDouble(s1) && isDouble(s2))
          return signum(atod(s1) - atod(s2));
        if (isInt(s1) || isDouble(s1))
          return -1;
        if (isInt(s2) || isDouble(s2))
          return 1;
        return s1.compareTo(s2);
      }

      private int compare(final String s1, final String s2) {
        return s1 == null ? As.binary(s2) : compareNumeric(s1, s2);
      }
    });
    book.clear();
    Iterables.addAll(book, es);
    return this;
  }

  public final Keys values(final String key) {
    return values(book, key);
  }

  /** @param f a file to write to
   * @return <code><b>this</b></code>
   * @throws IOException in case of failure */
  public LogBook writeTo(final File f) throws IOException {
    return writeTo(new FileOutputStream(f));
  }

  /** @param f a file to write to
   * @return <code><b>this</b></code>
   * @throws IOException in case of failure */
  public LogBook writeTo(final FileOutputStream f) throws IOException {
    final ObjectOutputStream out = new ObjectOutputStream(f);
    out.writeObject(this);
    out.close();
    return this;
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
    LIST, SUMMARY, BOTH, ENDS;
  }

  public static class Entries extends ArrayList<LogBook.Entry> {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1L;

    @Override public Entries clone() {
      return (Entries) super.clone();
    }
  }

  public final class Entry extends Setting {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1L;
    public Unit unit = null;
    public final RealStatistics records = new RealStatistics();

    Entry(final Setting s) {
      putAll(s);
    }

    public final String format() {
      return format(format);
    }

    public final String format(final String newFormat) {
      return records.format(unit, newFormat);
    }

    public final double[] recorded() {
      return records.all();
    }

    public final String settings() {
      return super.toString();
    }

    @Override public final String toString() {
      return records.format(unit);
    }

    public final Unit unit() {
      return unit;
    }

    final void add(final double d) {
      records.record(box(d));
    }

    final void add(final long l) {
      add((double) l);
    }

    final Entry setUnit(final Unit u) {
      require(unit == null || u == unit);
      unit = u;
      ensure(u == unit);
      return this;
    }
  }

  public final static class Keys extends LinkedHashSet<String> {
    private static final long serialVersionUID = 1L;

    @Override public final String toString() {
      return size() != 1 ? super.toString() : Iterables.first(this).toString();
    }
  }

  public static class Mutable extends LogBook {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1L;

    public static Mutable readFrom(final File f) throws IOException, FileNotFoundException, ClassNotFoundException {
      final ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
      final Mutable $ = (Mutable) in.readObject();
      in.close();
      return $;
    }

    public static Mutable readFrom(final String fileName) throws IOException, ClassNotFoundException {
      return readFrom(new File(fileName));
    }

    private static double divide(final long d, final long n) {
      return (double) d / (double) n;
    }

    private static Class<?> getClass(final Object o) {
      return o instanceof Class ? (Class<?>) o : o.getClass();
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

    /* (non-Javadoc)
     *
     * @see il.org.spartan.bench.ImmutableLogBook#close() */
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
      for (final Entry e : entries())
        if (current.equals(e))
          return e;
      final Entry $ = new Entry(current);
      book.add($);
      return $;
    }

    public Mutable record(final double d) {
      return record(d, DOUBLE);
    }

    public Mutable record(final double d, final Unit u) {
      find().setUnit(u).add(d);
      if (dotter != null)
        dotter.click();
      return this;
    }

    public void record(final long v, final long n) {
      record(divide(v, n));
    }

    public LogBook record(final StopWatch s, final long n) {
      return recordNanoseconds(s.time(), n);
    }

    public LogBook recordBytes(final double bytes) {
      return record(bytes, BYTES);
    }

    public LogBook recordBytes(final long bytes) {
      return recordBytes((double) bytes);
    }

    public LogBook recordMilliseconds(final double d) {
      return record(d, MILLISECONDS);
    }

    public LogBook recordMilliseconds(final long time, final long n) {
      return recordMilliseconds(divide(time, n));
    }

    public LogBook recordNanoseconds(final double d) {
      return record(d, NANOSECONDS);
    }

    public LogBook recordNanoseconds(final long time, final long n) {
      return recordNanoseconds(divide(time, n));
    }

    public LogBook recordRelative(final double d) {
      return record(d, RELATIVE);
    }

    public void recordRelative(final long l1, final long l2) {
      recordRelative((double) l1 / l2);
    }

    public void recordRelative(final StopWatch s1, final StopWatch s2) {
      recordRelative(s1.time(), s2.time());
    }

    @Override public LogBook remove(final String key) {
      super.remove(key);
      current.remove(key);
      return this;
    }

    public Mutable set(final String option, final boolean value) {
      return set(option, "" + value);
    }

    public Mutable set(final String option, final double value) {
      return set(option, "" + value);
    }

    public Mutable set(final String option, final float value) {
      return set(option, "" + value);
    }

    public Mutable set(final String option, final int value) {
      return set(option, "" + value);
    }

    public Mutable set(final String option, final long value) {
      return set(option, "" + value);
    }

    public Mutable set(final String option, final String value) {
      current.put(option, value);
      return this;
    }

    public final static class TEST {
      Accumulator c = new Counter();
      final Mutable myBook = new Mutable(this).set("day", "Tuesday").record(13).set("B", c.value()).record(13).set("day", "Tuesday").record(13)
          .set("B", c.value()).record(13).set("C", c.value()).record(13).set("day", "Tuesday").record(13).set("D", c.value()).record(13)
          .set("day", "Tuesday");

      @Test public final void containsKey() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        assertTrue(l.find().containsKey("A"));
        assertTrue(l.find().containsKey("B"));
        assertTrue(l.find().containsKey("C"));
        assertTrue(l.find().containsKey("D"));
      }

      @Test public final void create() {
        assertNotNull(new Mutable(this));
      }

      @Test public final void demoteCurrent() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.demote("C");
        assertEquals("[A, B, D, C]", l.current().keySet().toString());
        assertEquals(0, l.size());
      }

      @Test public final void demoteEntry() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.demote("C");
        assertEquals("[A, B, D, C]", l.find().keySet().toString());
      }

      @Test public final void demoteWithRecord() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.demote("C");
        assertEquals(0, l.size());
        l.demote("B");
        l.record(12);
        assertEquals(1, l.size());
        l.demote("A");
        l.record(13);
        assertEquals(1, l.size());
        assertEquals("[D, C, B, A]", l.find().keySet().toString());
      }

      @Test public final void findEntry1() {
        final Mutable l = new Mutable(this).set("day", "Tuesday").set("time", 12);
        l.record(1);
        assertEquals(1, l.find().records.n());
      }

      @Test public final void findEntry3() {
        final Mutable l = new Mutable(this);
        l.set("day", "Tuesday").set("time", 12);
        l.record(1).record(2).record(3);
        assertEquals(3, l.find().records.n());
      }

      @Test public final void findNotNullEntry() {
        final Mutable l = new Mutable(this).set("day", "Tuesday").set("time", 12);
        assertNotNull(l.find().records);
      }

      @Test public final void keyOrder() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        final Set<String> ss = l.find().keySet();
        assertEquals("[A, B, C, D]", ss.toString());
      }

      @Test public final void keySet() {
        final Mutable l = new Mutable(this);
        l.set("day", "Tuesday");
        l.set("time", 12);
        final Set<String> ss = l.find().keySet();
        assertEquals(2, ss.size());
        assertEquals(2, ss.size());
        assertTrue(ss.contains("day"));
        assertTrue(ss.contains("time"));
      }

      @Test public final void readWrite() throws Exception {
        final File f = new File("/tmp/delme.lgb");
        try {
          readFrom(f);
        } catch (final FileNotFoundException e) {
          e.printStackTrace();
        }
        myBook.writeTo(f);
      }

      @Test public final void remove() {
        final Mutable l = new Mutable(this);
        l.set("A", "Tuesday");
        l.set("B", c.value());
        l.set("C", c.value());
        l.set("D", c.value());
        l.remove("C");
        assertEquals(0, l.size());
        l.remove("B");
        l.record(12);
        assertEquals(1, l.size());
        l.remove("A");
        l.record(13);
        assertEquals(1, l.size());
        assertEquals("[D]", l.find().keySet().toString());
      }

      @Test public final void repeatedSettingsCount() {
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
        assertEquals(2, l.find().records.n());
      }

      @Test public final void repeatedSettingsSize() {
        int n = 0;
        final Mutable l = new Mutable(this);
        l.set("day", "Tuesday");
        assertEquals(0, l.size());
        l.set("time", 12);
        assertEquals(0, l.size());
        l.record(n++);
        assertEquals(1, l.size());
        l.set("day", "Wednesday");
        l.record(n++);
        l.set("time", 13);
        l.record(n++);
        l.set("day", "Tuesday");
        l.record(n++);
        l.set("time", 12);
        l.record(n++);
        assertEquals(4, l.size());
      }

      @Test public final void sizeZero() {
        assertEquals(0, new Mutable(this).size());
      }

      @Test public final void value() {
        assertNull(myBook.commonValue("B"));
        assertNull(myBook.commonValue("C"));
        assertNull(myBook.commonValue("D"));
        assertNull(myBook.commonValue("A"));
        assertNull(myBook.commonValue("E"));
        assertEquals("Tuesday", myBook.commonValue("day"));
      }
    }
  }

  public static class Setting extends LinkedHashMap<String, String> {
    /** A field for identifying a streamed version of objects of this class; we
     * use the values of <code>1L</code> to maintain upward compatibility. */
    private static final long serialVersionUID = 1L;

    public final void demote(final String key) {
      if (!containsKey(key))
        return;
      final String value = get(key);
      remove(key);
      put(key, value);
    }

    @Override public final Values values() {
      final Values $ = new Values();
      for (final String value : super.values())
        $.add(value);
      return $;
    }
  }

  public final static class Values extends ArrayList<String> {
    private static final long serialVersionUID = 1L;

    @Override public final String toString() {
      return size() != 1 ? super.toString() : Iterables.first(this).toString();
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

    public final LogBook go(final Collection<Entry> es) {
      if (stagger.size() != 0)
        return go(es, Iterables.first(stagger));
      if (mode == LIST || mode == BOTH)
        for (final Entry e : es) {
          final Setting s = removeKeys(e, exclude);
          println(prefix(s) + e.format());
        }
      if (mode == SUMMARY || mode == BOTH || mode == ENDS && es.size() > 1)
        summary(es);
      if (mode == ENDS || es.size() > 1) {
        final Entry min = min(es);
        final Entry max = max(es);
        System.out.println(shortForm(min) + compare(min, max) + ratio(min, max) + shortForm(max));
      }
      return LogBook.this;
    }

    private Collection<Entry> select(final Iterable<Entry> es, final String key, final String value) {
      final List<Entry> $ = new ArrayList<>();
      for (final Entry e : es)
        if (equals(e.get(key), value))
          $.add(e);
      return $;
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

    private boolean equals(final String s1, final String s2) {
      return s1 == null ? s2 == null : s1.equals(s2);
    }
    private String compare(final Entry e1, final Entry e2) {
      return compare(e1.records, e2.records);
    }

    private String compare(final Statistics s1, final Statistics s2) {
      final double p = new WelchT(s1, s2).p;
      if (p < 0.001)
        return "==";
      if (p > 0.1)
        return "~~";
      return "~" + RELATIVE.format(p) + "~";
    }

    private Entry max(final Collection<Entry> es) {
      Entry $ = Iterables.first(es);
      for (final Entry e : es)
        if (e.records.median() > $.records.median())
          $ = e;
      return $;
    }

    private Entry min(final Collection<Entry> es) {
      Entry $ = Iterables.first(es);
      for (final Entry e : es)
        if (e.records.median() < $.records.median())
          $ = e;
      return $;
    }

    private final String prefix(final Setting s) {
      return s.isEmpty() ? "" : s.values() + ": ";
    }

    private String ratio(final Entry e1, final Entry e2) {
      return String.format("%.2f*", box(e1.records.median() / e2.records.median())).toString();
    }

    private String shortForm(final Entry e) {
      return removeKeys(e, exclude).values() + e.format(" Jn");
    }

    private final LogBook summary(final Entry[] es) {
      Arrays.sort(es, new Comparator<Entry>() {
        @Override public final int compare(final Entry e1, final Entry e2) {
          return signum(e1.records.median() - e2.records.median());
        }
      });
      final StringBuilder s = new StringBuilder();
      for (int i = 0; i < es.length; i++) {
        if (i > 0) {
          final ImmutableStatistics s1 = es[i - 1].records;
          final ImmutableStatistics s2 = es[i].records;
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

    /** @param es
     * @param exclude2
     * @return */
    private final LogBook summary(final Iterable<Entry> es) {
      return summary(Iterables.toArray(es, Entry.class));
    }
  }
}
