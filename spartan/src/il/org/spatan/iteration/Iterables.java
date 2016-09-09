package il.org.spatan.iteration;

import static il.org.spartan.utils.Box.*;
import static il.org.spartan.utils.Unbox.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;

import il.org.spartan.iteration.closures.*;
import il.org.spartan.utils.*;

public class Iterables {
  public static <T, C extends Collection<T>> C addAll(final C c, final Iterable<? extends T> ts) {
    for (final T t : ts)
      c.add(t);
    return c;
  }

  public static <T, C extends Collection<T>> C addAll(final C c, final T... ts) {
    for (final T t : ts)
      c.add(t);
    return c;
  }

  public static <F, T> Iterable<T> apply(final Iterable<? extends F> fs, final Converter<F, T> c) {
    final ArrayList<T> $ = new ArrayList<>();
    for (final F f : fs)
      $.add(c._(f));
    return $;
  }

  public static int[] array(final int... is) {
    return is;
  }

  public static <T> T[] array(final T... ts) {
    return ts;
  }

  public static <T> boolean before(final Iterable<T> ts, final T t1, final T t2) {
    boolean seen = false;
    for (final T t : ts) {
      if (!seen && t1.equals(t))
        seen = true;
      if (seen && t2.equals(t))
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
    for (@SuppressWarnings("unused") final T _ : ts)
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
      $ += As.binary(isEqual(t, candidate));
    return $;
  }

  public static <T> int count(final Iterable<T> ts, final Condition<T> c) {
    int $ = 0;
    for (final T t : ts)
      $ += As.binary(c.holds(t));
    return $;
  }

  public static <T> int count(final T[] ts, final Condition<T> c) {
    int $ = 0;
    for (final T t : ts)
      $ += As.binary(c.holds(t));
    return $;
  }

  public static double[] doubles(final double... ds) {
    return ds;
  }

  public static <T> Iterable<T> empty(@SuppressWarnings("unused") final Class<T> cls) {
    return new ArrayList<>();
  }

  /** Determines whether an iterable has any values.
   * @param <T> type of elements iterated over
   * @param ts an arbitrary iterable over this type
   * @return <code><b>true</b></code> <em>if an only if</em> the iterable is
   *         empty. */
  public static <T> boolean empty(final Iterable<T> ts) {
    return !ts.iterator().hasNext();
  }

  /** Retrieves the first element of a stream
   * @param <T> type of elements iterated over
   * @param ts an arbitrary iterable over this type
   * @return the first element of the parameter, supposing there is one. If
   *         there is not such element, the results are unpredictable. */
  public static <T> T first(final Iterable<T> ts) {
    return ts.iterator().next();
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
    for (final T t : ts) {
      $.add(t);
      if (++i == n)
        break;
    }
    return $;
  }

  public static <T> T first(final Iterator<T> i, final Condition<T> c) {
    while (i.hasNext()) {
      final T $ = i.next();
      if (c.holds($))
        return $;
    }
    return null;
  }

  /** Retrieves the first element of an array
   * @param <T> type of elements in the array
   * @param ts an arbitrary array of this type
   * @return the first element of the array if the array is of non-zero length,
   *         otherwise <code><b>null</b></code> */
  public static <T> T first(final T[] ts) {
    return ts.length > 0 ? ts[0] : null;
  }

  public static <T> T get(final Iterable<T> ts, final int i) {
    int j = 0;
    for (final T t : ts)
      if (++j > i)
        return t;
    return null;
  }

  /** Find the first location of a given integer in an array
   * @param j a value to find in this array
   * @param is an arbitrary array of integers
   * @return the index of the first occurrence of the argument in the array, or
   *         -1 if not found. */
  public static int index(final int j, final int[] is) {
    int $ = 0;
    for (final int i : is) {
      if (i == j)
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
    for (final T _ : ts) {
      if (t == _)
        return $;
      ++$;
    }
    return -1;
  }

  public static <T> int[] indices(final Collection<? extends T> ts, final Condition<T> c) {
    final int[] $ = new int[ts.size()];
    int i = 0;
    int position = 0;
    for (final T t : ts) {
      if (c.holds(t))
        $[i++] = position;
      position++;
    }
    return Arrays.copyOf($, i);
  }

  public static int[] ints(final int... is) {
    return is;
  }

  public static <T> void iterate(final T[] ts, final Iteration<T> what) {
    for (int i = 0; i < ts.length; i++) {
      what.prolog(ts[i]);
      if (i < ts.length - 1)
        what.next(ts[i], ts[i + 1]);
      what.at(ts[i]);
      if (i > 1)
        what.next(ts[i], ts[i - 1]);
      what.prolog(ts[i]);
    }
  }

  public static int[] make(final BitSet bs) {
    final int[] $ = new int[bs.cardinality()];
    int i = 0;
    for (int value = bs.nextSetBit(0); value >= 0; value = bs.nextSetBit(value + 1))
      $[i++] = value;
    return $;
  }

  public static CharIterable make(final char... cs) {
    return () -> new CharIterator() {
      int i = 0;

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
      private int current = 0;

      @Override public boolean hasNext() {
        return current < fs.length;
      }

      @Override public T next() {
        return c._(fs[current++]);
      }
    };
  }

  /** Create an {@link Iterable} over a range of integers.
   * @param n an arbitrary integer
   * @return an {@link Iterable} yields the {@link Integer}s in the range 0
   *         through the value of the parameter. */
  public static Iterable<Integer> make(final int n) {
    return () -> new Iterables.ReadonlyIterator<Integer>() {
      int position = 0;

      @Override public boolean hasNext() {
        return position < n;
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
        return c._(inner.next());
      }
    };
  }

  public static <T> Iterable<T> make(final Iterator<T> ts) {
    return () -> ts;
  }

  public static <T> Iterable<T> make(final T... ts) {
    return new IterableArray<>(ts);
  }

  public static <F, T> Iterable<T> map(final Iterable<? extends F> fs, final Function<F, T> func) {
    final List<T> ts = new ArrayList<>();
    for (final F f : fs)
      ts.add(func.eval(f));
    return ts;
  }

  public static <E> Iterable<E> reverse(final Iterable<E> in) {
    final List<E> $ = toList(in);
    Collections.reverse($);
    return $;
  }

  public static <T> boolean same(final Iterable<? extends T> ts1, final Iterable<? extends T> ts2) {
    if (ts1 == null || ts2 == null)
      return ts1 == ts2;
    final Iterator<? extends T> t1 = ts1.iterator(), t2 = ts2.iterator();
    while (t1.hasNext())
      if (!t2.hasNext() || t1.next() != t2.next())
        return false;
    return !t2.hasNext();
  }

  public static <T> Iterable<? extends T> select(final Iterable<? extends T> ts, final Condition<T> c) {
    return new FilteredIterable<T>(ts) {
      @Override public boolean holds(final T t) {
        return c.holds(t);
      }
    };
  }

  public static <T> Iterable<? extends T> select(final T[] ts, final Condition<T> c) {
    return select(make(ts), c);
  }

  public static double[] seq(final double ds[]) {
    return seq(ds.length);
  }

  /** Construct a finite prefix of the infinite sequence 0,1,2,...
   * @param n a non-negative integers
   * @return an array containing, in order, all non-negative integers up to the
   *         parameter. */
  public static double[] seq(final int n) {
    final double[] $ = new double[n];
    for (int i = 0; i < n; i++)
      $[i] = i;
    return $;
  }

  public static <T> ArrayList<T> serialize(final Iterable<? extends T> ts) {
    return copy(new ArrayList<T>(count(ts)), ts);
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
    for (final String s : ss)
      $[i++] = s;
    return $;
  }

  public static <E> E[] toArray(final Iterable<? extends E> in, final Class<E> clazz) {
    final List<E> es = toList(in);
    @SuppressWarnings("unchecked") final E[] _ = (E[]) Array.newInstance(clazz, es.size());
    return es.toArray(_);
  }

  public static double[] toArray(final Iterable<Double> ds) {
    return toArray(toList(ds));
  }

  public static double[] toArray(final List<Double> ds) {
    final double[] $ = new double[ds.size()];
    int i = 0;
    for (final Double d : ds)
      $[i++] = unbox(d);
    return $;
  }

  public static List<Double> toList(final double... ds) {
    final List<Double> $ = new ArrayList<>();
    for (final double d : ds)
      $.add(Box.it(d));
    return $;
  }

  public static List<Integer> toList(final int... is) {
    final List<Integer> $ = new ArrayList<>();
    for (final int i : is)
      $.add(Box.it(i));
    return $;
  }

  public static <T> List<T> toList(final Iterable<? extends T> ts) {
    return addAll(new ArrayList<T>(), ts);
  }

  public static <T> ArrayList<T> toList(final T... ts) {
    return new ArrayList<>(Arrays.asList(ts));
  }

  public static String toString(final Iterable<String> items, final String sep) {
    String $ = "";
    for (final String s : items) {
      $ += s;
      $ += sep;
    }
    return $;
  }

  public static String toString(final Set<Entry<String, String>> entrySet, final String sep) {
    String $ = "";
    for (final Entry<String, String> e : entrySet) {
      $ += e.toString();
      $ += sep;
    }
    return $;
  }

  public static <T> List<T> union(final List<T>... tss) {
    final List<T> $ = new ArrayList<>();
    for (final List<T> ts : tss)
      $.addAll(ts);
    return $;
  }

  /** Determines whether an iterable is null or empty
   * @param <T>
   * @param ts an arbitrary iterable
   * @return <code><b>true</b></code> <em>if an only if</em> the parameter is
   *         <code><b>null</b></code> or offers no values. */
  public static <T> boolean vacuous(final Iterable<T> ts) {
    return ts == null || empty(ts);
  }

  private static <T> boolean isEqual(final T a, final T b) {
    return b == a || a != null && a.equals(b);
  }

  public static abstract class RangeIterator<T> extends ReadonlyIterator<T> {
    private final int n;
    private int i = 0;

    public RangeIterator(final int n) {
      this.n = n;
    }

    @Override public final boolean hasNext() {
      return i < n;
    }

    @Override public T next() {
      final T $ = value();
      i++;
      return $;
    }

    protected int i() {
      return i;
    }

    protected abstract T value();
  }

  public static abstract class ReadonlyIterator<T> implements Iterator<T> {
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
    public static <T> Iterable<T> make(final T t) {
      return t == null ? null : new Singleton<>(t);
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
