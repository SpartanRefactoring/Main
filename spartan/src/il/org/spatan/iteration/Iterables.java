package il.org.spatan.iteration;

import static nano.ly.Box.*;
import static nano.ly.Unbox.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.Map.*;

import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.iteration.closures.*;
import nano.ly.*;

public class Iterables {
  @NotNull public static <T, C extends Collection<T>> C addAll(@NotNull final C $, @NotNull final Iterable<? extends T> ts) {
    for (final T ¢ : ts)
      $.add(¢);
    return $;
  }

  @NotNull public static <T, C extends Collection<T>> C addAll(@NotNull final C $, @NotNull final T... ts) {
    for (final T ¢ : ts)
      $.add(¢);
    return $;
  }

  @NotNull public static <F, T> Iterable<T> apply(@NotNull final Iterable<? extends F> fs, @NotNull final Converter<F, T> c) {
    @NotNull final ArrayList<T> $ = new ArrayList<>();
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

  public static <T> boolean before(@NotNull final Iterable<T> ts, @NotNull final T t1, @NotNull final T t2) {
    boolean seen = false;
    for (final T ¢ : ts) {
      if (!seen && t1.equals(¢))
        seen = true;
      if (seen && t2.equals(¢))
        return true;
    }
    return false;
  }

  public static <T> boolean contains(@NotNull final Iterable<? extends T> ts, final T t) {
    for (final T candidate : ts)
      if (isEqual(t, candidate))
        return true;
    return false;
  }

  @NotNull public static <T> ArrayList<T> copy(@NotNull final ArrayList<T> to, @NotNull final Iterable<? extends T> from) {
    return addAll(to, from);
  }

  /** @param <T> type of elements iterated over
   * @param ts an arbitrary iterable over this type
   * @return the number of elements in this iterable */
  public static <T> int count(@NotNull final Iterable<? extends T> ts) {
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
  public static <T> int count(@NotNull final Iterable<? extends T> ts, final T t) {
    int $ = 0;
    for (final T candidate : ts)
      $ += as.bit(isEqual(t, candidate));
    return $;
  }

  public static <T> int count(@NotNull final Iterable<T> ts, @NotNull final Condition<T> c) {
    int $ = 0;
    for (final T ¢ : ts)
      $ += as.bit(c.holds(¢));
    return $;
  }

  public static <T> int count(@NotNull final T[] ts, @NotNull final Condition<T> c) {
    int $ = 0;
    for (final T ¢ : ts)
      $ += as.bit(c.holds(¢));
    return $;
  }

  public static double[] doubles(final double... ¢) {
    return ¢;
  }

  @NotNull public static <T> Iterable<T> empty(@SuppressWarnings("unused") final Class<T> __) {
    return new ArrayList<>();
  }

  /** Determines whether an iterable has any values.
   * @param <T> type of elements iterated over
   * @param ¢ an arbitrary iterable over this type
   * @return <code><b>true</b></code> <em>if an only if</em> the iterable is
   *         empty. */
  public static <T> boolean empty(@NotNull final Iterable<T> ¢) {
    return !¢.iterator().hasNext();
  }

  /** Retrieves the first element of a stream
   * @param <T> type of elements iterated over
   * @param ¢ an arbitrary iterable over this type
   * @return the first element of the parameter, supposing there is one. If
   *         there is not such element, the results are unpredictable. */
  public static <T> T first(@NotNull final Iterable<T> ¢) {
    return ¢.iterator().next();
  }

  @Nullable public static <T> T first(@NotNull final Iterable<T> ts, @NotNull final Condition<T> c) {
    return first(ts.iterator(), c);
  }

  /** Retrieves a prefix of a specified size of a stream
   * @param <T> type of elements iterated over
   * @param ts an arbitrary iterable over this type
   * @param n a non-negative integer
   * @return an array containing the first */
  @NotNull public static <T> ArrayList<T> first(@NotNull final Iterable<T> ts, final int n) {
    @NotNull final ArrayList<T> $ = new ArrayList<>();
    int i = 0;
    for (final T ¢ : ts) {
      $.add(¢);
      if (++i == n)
        break;
    }
    return $;
  }

  public static <T> T first(@NotNull final Iterator<T> t, @NotNull final Condition<T> c) {
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
  public static <T> T first(@NotNull final T[] ¢) {
    return ¢.length > 0 ? ¢[0] : null;
  }

  public static <T> T get(@NotNull final Iterable<T> ts, final int i) {
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
  public static int index(final int j, @NotNull final int[] is) {
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
  public static <T> int index(final T t, @NotNull final Iterable<? extends T> ts) {
    int $ = 0;
    for (final T __ : ts) {
      if (t == __)
        return $;
      ++$;
    }
    return -1;
  }

  public static <T> int[] indices(@NotNull final Collection<? extends T> ts, @NotNull final Condition<T> c) {
    @NotNull final int[] $ = new int[ts.size()];
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

  public static <T> void iterate(@NotNull final T[] ts, @NotNull final Iteration<T> what) {
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

  @NotNull public static int[] make(@NotNull final BitSet s) {
    @NotNull final int[] $ = new int[s.cardinality()];
    for (int ¢ = 0, value = s.nextSetBit(0); value >= 0; value = s.nextSetBit(value + 1))
      $[¢++] = value;
    return $;
  }

  public static CharIterable make(@NotNull final char... cs) {
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

  public static <F, T> Iterable<T> make(@NotNull final F[] fs, @NotNull final Converter<F, T> c) {
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

  public static <F, T> Iterable<T> make(@NotNull final Iterable<F> fs, @NotNull final Converter<F, T> c) {
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

  @NotNull public static <T> Iterable<T> make(final T... ¢) {
    return new IterableArray<>(¢);
  }

  @NotNull public static <F, T> Iterable<T> map(@NotNull final Iterable<? extends F> fs, @NotNull final Function<F, T> f) {
    @NotNull final List<T> $ = new ArrayList<>();
    for (final F ¢ : fs)
      $.add(f.eval(¢));
    return $;
  }

  @NotNull public static <E> Iterable<E> reverse(@NotNull final Iterable<E> in) {
    @NotNull final List<E> $ = toList(in);
    Collections.reverse($);
    return $;
  }

  public static <T> boolean same(@Nullable final Iterable<? extends T> ts1, @Nullable final Iterable<? extends T> ts2) {
    if (ts1 == null || ts2 == null)
      return ts1 == ts2;
    @NotNull final Iterator<? extends T> t1 = ts1.iterator(), $ = ts2.iterator();
    while (t1.hasNext())
      if (!$.hasNext() || t1.next() != $.next())
        return false;
    return !$.hasNext();
  }

  @NotNull public static <T> Iterable<? extends T> select(final Iterable<? extends T> ts, @NotNull final Condition<T> c) {
    return new FilteredIterable<T>(ts) {
      @Override public boolean holds(final T ¢) {
        return c.holds(¢);
      }
    };
  }

  @NotNull public static <T> Iterable<? extends T> select(final T[] ts, @NotNull final Condition<T> c) {
    return select(make(ts), c);
  }

  @NotNull public static double[] seq(@NotNull final double ¢[]) {
    return seq(¢.length);
  }

  /** Construct a finite prefix of the infinite sequence 0,1,2,...
   * @param i a non-negative integers
   * @return an array containing, in order, all non-negative integers up to the
   *         parameter. */
  @NotNull public static double[] seq(final int i) {
    @NotNull final double[] $ = new double[i];
    for (int ¢ = 0; ¢ < i; ++¢)
      $[¢] = ¢;
    return $;
  }

  @NotNull public static <T> ArrayList<T> serialize(@NotNull final Iterable<? extends T> ¢) {
    return copy(new ArrayList<T>(count(¢)), ¢);
  }

  @NotNull public static <T> Iterable<T> sort(@NotNull final Iterable<T> os) {
    return addAll(new TreeSet<T>(), os);
  }

  @NotNull public static <T> Iterable<T> sort(@NotNull final Iterable<T> os, final Comparator<T> c) {
    return addAll(new TreeSet<>(c), os);
  }

  @NotNull public static String[] toArray(@NotNull final Collection<String> ss) {
    @NotNull final String[] $ = new String[ss.size()];
    int i = 0;
    for (final String ¢ : ss)
      $[i++] = ¢;
    return $;
  }

  public static <E> E[] toArray(@NotNull final Iterable<? extends E> in, final Class<E> clazz) {
    @NotNull final List<E> $ = toList(in);
    @NotNull @SuppressWarnings("unchecked") final E[] __ = (E[]) Array.newInstance(clazz, $.size());
    return $.toArray(__);
  }

  @NotNull public static double[] toArray(@NotNull final Iterable<Double> ¢) {
    return toArray(toList(¢));
  }

  @NotNull public static double[] toArray(@NotNull final List<Double> ds) {
    @NotNull final double[] $ = new double[ds.size()];
    int i = 0;
    for (@NotNull final Double ¢ : ds)
      $[i++] = unbox(¢);
    return $;
  }

  @NotNull public static List<Double> toList(@NotNull final double... ds) {
    @NotNull final List<Double> $ = new ArrayList<>();
    for (final double ¢ : ds)
      $.add(Box.it(¢));
    return $;
  }

  @NotNull public static List<Integer> toList(@NotNull final int... is) {
    @NotNull final List<Integer> $ = new ArrayList<>();
    for (final int ¢ : is)
      $.add(Box.it(¢));
    return $;
  }

  @NotNull public static <T> List<T> toList(@NotNull final Iterable<? extends T> ¢) {
    return addAll(new ArrayList<T>(), ¢);
  }

  @NotNull public static <T> ArrayList<T> toList(final T... ¢) {
    return new ArrayList<>(Arrays.asList(¢));
  }

  public static String toString(@NotNull final Iterable<String> items, final String sep) {
    String $ = "";
    for (final String ¢ : items)
      $ += ¢ + sep;
    return $;
  }

  public static String toString(@NotNull final Set<Entry<String, String>> entrySet, final String sep) {
    String $ = "";
    for (final Entry<String, String> ¢ : entrySet)
      $ += ¢ + sep;
    return $;
  }

  @NotNull public static <T> List<T> union(@NotNull final List<T>... tss) {
    @NotNull final List<T> $ = new ArrayList<>();
    for (@NotNull final List<T> ¢ : tss)
      $.addAll(¢);
    return $;
  }

  /** Determines whether an iterable is null or empty
   * @param <T>
   * @param ¢ an arbitrary iterable
   * @return <code><b>true</b></code> <em>if an only if</em> the parameter is
   *         <code><b>null</b></code> or offers no values. */
  public static <T> boolean vacuous(@Nullable final Iterable<T> ¢) {
    return ¢ == null || empty(¢);
  }

  private static <T> boolean isEqual(@Nullable final T a, final T b) {
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
    @Nullable public static <T> Iterable<T> make(@Nullable final T ¢) {
      return ¢ == null ? null : new Singleton<>(¢);
    }

    @Nullable T t;

    /** Instantiate the adapter with an object
     * @param t the object on which we can iterate. */
    public Singleton(final T t) {
      this.t = t;
    }

    @Override @NotNull public Iterator<T> iterator() {
      return new Iterables.ReadonlyIterator<T>() {
        @Override public boolean hasNext() {
          return t != null;
        }

        @Override @Nullable public T next() {
          @Nullable final T $ = t;
          t = null;
          return $;
        }
      };
    }
  }
}
