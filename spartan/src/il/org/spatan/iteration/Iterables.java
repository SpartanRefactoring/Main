package il.org.spatan.iteration;

import static fluent.ly.box.*;
import static fluent.ly.unbox.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;
import java.util.function.*;

import il.org.spartan.iteration.closures.*;
import fluent.ly.*;

public class Iterables {
  public static <T, C extends Collection<T>> C addAll(final C $, final Iterable<? extends T> ts) {
    for (final T ¢ : ts)
      $.add(¢);
    return $;
  }
  public static <T, C extends Collection<T>> C addAll(final C $, final T... ts) {
    for (final T ¢ : ts)
      $.add(¢);
    return $;
  }
  public static <F, T> Iterable<T> apply(final Iterable<? extends F> fs, final Converter<F, T> c) {
    final ArrayList<T> $ = new ArrayList<>();
    for (final F ¢ : fs)
      $.add(c.__(¢));
    return $;
  }
  public static int[] array(final int... ¢) {
    return ¢;
  }
  public static <T> T[] array(final T... ¢) {
    return ¢;
  }
  public static <T> boolean before(final Iterable<T> ts, final T t1, final T t2) {
    boolean seen = false;
    for (final T ¢ : ts) {
      if (!seen && t1.equals(¢))
        seen = true;
      if (seen && t2.equals(¢))
        return true;
    }
    return false;
  }
  public static <T> boolean contains(final Iterable<? extends T> ts, final T t) {
    for (final T candidate : ts)
      if (isEqual(t, candidate))
        return true;
    return false;
  }
  public static <T> ArrayList<T> copy(final ArrayList<T> to, final Iterable<? extends T> from) {
    return addAll(to, from);
  }
  /** @param <T> type of elements iterated over
   * @param ts an arbitrary iterable over this type
   * @return the number of elements in this iterable */
  public static <T> int count(final Iterable<? extends T> ts) {
    int $ = 0;
    for (@SuppressWarnings("unused") final T __ : ts)
      ++$;
    return $;
  }
  /** @param <T> type of elements iterated over
   * @param ts an arbitrary iterable over this type
   * @param t an arbitrary object
   * @return the number of elements in the stream which are equal to the
   *         parameter */
  public static <T> int count(final Iterable<? extends T> ts, final T t) {
    int $ = 0;
    for (final T candidate : ts)
      $ += as.bit(isEqual(t, candidate));
    return $;
  }
  public static <T> int count(final Iterable<T> ts, final Condition<T> c) {
    int $ = 0;
    for (final T ¢ : ts)
      $ += as.bit(c.holds(¢));
    return $;
  }
  public static <T> int count(final T[] ts, final Condition<T> c) {
    int $ = 0;
    for (final T ¢ : ts)
      $ += as.bit(c.holds(¢));
    return $;
  }
  public static double[] doubles(final double... ¢) {
    return ¢;
  }
  public static <T> Iterable<T> empty(@SuppressWarnings("unused") final Class<T> __) {
    return new ArrayList<>();
  }
  /** Determines whether an iterable has any values.
   * @param <T> type of elements iterated over
   * @param ¢ an arbitrary iterable over this type
   * @return <code><b>true</b></code> <em>if an only if</em> the iterable is
   *         empty. */
  public static <T> boolean empty(final Iterable<T> ¢) {
    return !¢.iterator().hasNext();
  }
  /** Retrieves the first element of a stream
   * @param <T> type of elements iterated over
   * @param ¢ an arbitrary iterable over this type
   * @return the first element of the parameter, supposing there is one. If
   *         there is not such element, the results are unpredictable. */
  public static <T> T first(final Iterable<T> ¢) {
    return ¢.iterator().next();
  }
  public static <T> T first(final Iterable<T> ts, final Condition<T> c) {
    return first(ts.iterator(), c);
  }
  /** Retrieves a prefix of a specified size of a stream
   * @param <T> type of elements iterated over
   * @param ts an arbitrary iterable over this type
   * @param n a non-negative integer
   * @return an array containing the first */
  public static <T> ArrayList<T> first(final Iterable<T> ts, final int n) {
    final ArrayList<T> $ = new ArrayList<>();
    int i = 0;
    for (final T ¢ : ts) {
      $.add(¢);
      if (++i == n)
        break;
    }
    return $;
  }
  public static <T> T first(final Iterator<T> t, final Condition<T> c) {
    while (t.hasNext()) {
      final T $ = t.next();
      if (c.holds($))
        return $;
    }
    return null;
  }
  /** Retrieves the first element of an array
   * @param <T> type of elements in the array
   * @param ¢ an arbitrary array of this type
   * @return the first element of the array if the array is of non-zero length,
   *         otherwise <code><b>null</b></code> */
  public static <T> T first(final T[] ¢) {
    return ¢.length > 0 ? ¢[0] : null;
  }
  public static <T> T get(final Iterable<T> ts, final int i) {
    int j = 0;
    for (final T $ : ts)
      if (++j > i)
        return $;
    return null;
  }
  /** Find the first location of a given integer in an array
   * @param j a value to find in this array
   * @param is an arbitrary array of integers
   * @return the index of the first occurrence of the argument in the array, or
   *         -1 if not found. */
  public static int index(final int j, final int[] is) {
    int $ = 0;
    for (final int ¢ : is) {
      if (¢ == j)
        return $;
      ++$;
    }
    return -1;
  }
  /** Find the first location of a given value in an iterable
   * @param t a value to find
   * @param ts an arbitrary iterable
   * @param <T> type of elements iterated over
   * @return the index of the first occurrence of the argument in the iterable,
   *         or -1 if not found. */
  public static <T> int index(final T t, final Iterable<? extends T> ts) {
    int $ = 0;
    for (final T __ : ts) {
      if (t == __)
        return $;
      ++$;
    }
    return -1;
  }
  public static <T> int[] indices(final Collection<? extends T> ts, final Condition<T> c) {
    final int[] $ = new int[ts.size()];
    int i = 0, position = 0;
    for (final T ¢ : ts) {
      if (c.holds(¢))
        $[i++] = position;
      ++position;
    }
    return Arrays.copyOf($, i);
  }
  public static int[] ints(final int... ¢) {
    return ¢;
  }
  public static <T> void iterate(final T[] ts, final Iteration<T> what) {
    for (int ¢ = 0; ¢ < ts.length; ++¢) {
      what.prolog(ts[¢]);
      if (¢ < ts.length - 1)
        what.next(ts[¢], ts[¢ + 1]);
      what.at(ts[¢]);
      if (¢ > 1)
        what.next(ts[¢], ts[¢ - 1]);
      what.prolog(ts[¢]);
    }
  }
  public static int[] make(final BitSet s) {
    final int[] $ = new int[s.cardinality()];
    for (int ¢ = 0, value = s.nextSetBit(0); value >= 0; value = s.nextSetBit(value + 1))
      $[¢++] = value;
    return $;
  }
  public static CharIterable make(final char... cs) {
    return () -> new CharIterator() {
      int i;

      @Override public boolean hasNext() {
        return i < cs.length;
      }
      @Override public char next() {
        return cs[i++];
      }
    };
  }
  public static <F, T> Iterable<T> make(final F[] fs, final Converter<F, T> c) {
    return () -> new ReadonlyIterator<T>() {
      int current;

      @Override public boolean hasNext() {
        return current < fs.length;
      }
      @Override public T next() {
        return c.__(fs[current++]);
      }
    };
  }
  /** Create an {@link Iterable} over a range of integers.
   * @param i an arbitrary integer
   * @return an {@link Iterable} yields the {@link Integer}s in the range 0
   *         through the value of the parameter. */
  public static Iterable<Integer> make(final int i) {
    return () -> new Iterables.ReadonlyIterator<Integer>() {
      int position;

      @Override public boolean hasNext() {
        return position < i;
      }
      @Override public Integer next() {
        return box(position++);
      }
    };
  }
  public static <F, T> Iterable<T> make(final Iterable<F> fs, final Converter<F, T> c) {
    return () -> new ReadonlyIterator<T>() {
      final Iterator<F> inner = fs.iterator();

      @Override public boolean hasNext() {
        return inner.hasNext();
      }
      @Override public T next() {
        return c.__(inner.next());
      }
    };
  }
  public static <T> Iterable<T> make(final Iterator<T> ¢) {
    return () -> ¢;
  }
  public static <T> Iterable<T> make(final T... ¢) {
    return new IterableArray<>(¢);
  }
  public static <F, T> Iterable<T> map(final Iterable<? extends F> fs, final Function<F, T> f) {
    final List<T> $ = new ArrayList<>();
    for (final F ¢ : fs)
      $.add(f.apply(¢));
    return $;
  }
  public static <E> Iterable<E> reverse(final Iterable<E> in) {
    final List<E> $ = toList(in);
    Collections.reverse($);
    return $;
  }
  public static <T> boolean same(final Iterable<? extends T> ts1, final Iterable<? extends T> ts2) {
    if (ts1 == null || ts2 == null)
      return ts1 == ts2;
    final Iterator<? extends T> t1 = ts1.iterator(), $ = ts2.iterator();
    while (t1.hasNext())
      if (!$.hasNext() || t1.next() != $.next())
        return false;
    return !$.hasNext();
  }
  public static <T> Iterable<? extends T> select(final Iterable<? extends T> ts, final Condition<T> c) {
    return new FilteredIterable<T>(ts) {
      @Override public boolean holds(final T ¢) {
        return c.holds(¢);
      }
    };
  }
  public static <T> Iterable<? extends T> select(final T[] ts, final Condition<T> c) {
    return select(make(ts), c);
  }
  public static double[] seq(final double ¢[]) {
    return seq(¢.length);
  }
  /** Construct a finite prefix of the infinite sequence 0,1,2,...
   * @param i a non-negative integers
   * @return an array containing, in order, all non-negative integers up to the
   *         parameter. */
  public static double[] seq(final int i) {
    final double[] $ = new double[i];
    for (int ¢ = 0; ¢ < i; ++¢)
      $[¢] = ¢;
    return $;
  }
  public static <T> ArrayList<T> serialize(final Iterable<? extends T> ¢) {
    return copy(new ArrayList<T>(count(¢)), ¢);
  }
  public static <T> Iterable<T> sort(final Iterable<T> os) {
    return addAll(new TreeSet<T>(), os);
  }
  public static <T> Iterable<T> sort(final Iterable<T> os, final Comparator<T> c) {
    return addAll(new TreeSet<>(c), os);
  }
  public static String[] toArray(final Collection<String> ss) {
    final String[] $ = new String[ss.size()];
    int i = 0;
    for (final String ¢ : ss)
      $[i++] = ¢;
    return $;
  }
  public static <E> E[] toArray(final Iterable<? extends E> in, final Class<E> clazz) {
    final List<E> $ = toList(in);
    @SuppressWarnings("unchecked") final E[] __ = (E[]) Array.newInstance(clazz, $.size());
    return $.toArray(__);
  }
  public static double[] toArray(final Iterable<Double> ¢) {
    return toArray(toList(¢));
  }
  public static double[] toArray(final List<Double> ds) {
    final double[] $ = new double[ds.size()];
    int i = 0;
    for (final Double ¢ : ds)
      $[i++] = unbox(¢);
    return $;
  }
  public static List<Double> toList(final double... ds) {
    final List<Double> $ = new ArrayList<>();
    for (final double ¢ : ds)
      $.add(box.it(¢));
    return $;
  }
  public static List<Integer> toList(final int... is) {
    final List<Integer> $ = new ArrayList<>();
    for (final int ¢ : is)
      $.add(box.it(¢));
    return $;
  }
  public static <T> List<T> toList(final Iterable<? extends T> ¢) {
    return addAll(new ArrayList<T>(), ¢);
  }
  public static <T> ArrayList<T> toList(final T... ¢) {
    return new ArrayList<>(Arrays.asList(¢));
  }
  public static String toString(final Iterable<String> items, final String sep) {
    String $ = "";
    for (final String ¢ : items)
      $ += ¢ + sep;
    return $;
  }
  public static String toString(final Set<Entry<String, String>> entrySet, final String sep) {
    String $ = "";
    for (final Entry<String, String> ¢ : entrySet)
      $ += ¢ + sep;
    return $;
  }
  public static <T> List<T> union(final List<T>... tss) {
    final List<T> $ = new ArrayList<>();
    for (final List<T> ¢ : tss)
      $.addAll(¢);
    return $;
  }
  /** Determines whether an iterable is null or empty
   * @param <T>
   * @param ¢ an arbitrary iterable
   * @return <code><b>true</b></code> <em>if an only if</em> the parameter is
   *         <code><b>null</b></code> or offers no values. */
  public static <T> boolean vacuous(final Iterable<T> ¢) {
    return ¢ == null || empty(¢);
  }
  private static <T> boolean isEqual(final T a, final T b) {
    return b == a || a != null && a.equals(b);
  }

  public abstract static class RangeIterator<T> extends ReadonlyIterator<T> {
    private final int n;
    private int i;

    public RangeIterator(final int n) {
      this.n = n;
    }
    @Override public final boolean hasNext() {
      return i < n;
    }
    @Override public T next() {
      final T $ = value();
      ++i;
      return $;
    }
    protected int i() {
      return i;
    }
    protected abstract T value();
  }

  public abstract static class ReadonlyIterator<T> implements Iterator<T> {
    @Override public final void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /** An {@linkplain "http://en.wikipedia.org/wiki/Adapter_pattern Adapter"} of
   * a scalar object, adapting it to the {@link Iterable} interface whereby
   * making it possible to iterate over this object in the following sense:. If
   * the object is non-null, then the iteration will return the object and
   * terminate. If the object is null, then the iteration is vacuous.
   * @author Yossi Gil
   * @since Oct 19, 2009
   * @param <T> type of objects in the array */
  public static class Singleton<T> implements Iterable<T> {
    public static <T> Iterable<T> make(final T ¢) {
      return ¢ == null ? null : new Singleton<>(¢);
    }

    T t;

    /** Instantiate the adapter with an object
     * @param t the object on which we can iterate. */
    public Singleton(final T t) {
      this.t = t;
    }
    @Override public Iterator<T> iterator() {
      return new Iterables.ReadonlyIterator<T>() {
        @Override public boolean hasNext() {
          return t != null;
        }
        @Override public T next() {
          final T $ = t;
          t = null;
          return $;
        }
      };
    }
  }
}
