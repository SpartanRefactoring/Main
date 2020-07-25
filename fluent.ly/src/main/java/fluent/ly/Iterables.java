package fluent.ly;

import static fluent.ly.unbox.it;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;


import il.org.spartan.iterables.iterables;
import il.org.spatan.iteration.CharIterable;
import il.org.spatan.iteration.CharIterator;
import il.org.spatan.iteration.FilteredIterable;
import il.org.spatan.iteration.IterableArray;
import il.org.spatan.iteration.Iteration;
public interface Iterables {
  static <T, @NonNull C extends Collection<T>> C addAll(final C $, final @NonNull Iterable<? extends T> ts) {
    for (final T ¢ : ts)
      $.add(¢);
    return $;
  }

  @SafeVarargs static <T, C extends Collection<T>> C addAll(final @NonNull C $, final T... ts) {
    for (final T ¢ : ts)
      $.add(¢);
    return $;
  }

  static <F, T> Iterable<T> apply(final Iterable<? extends F> fs, final Function<F, T> f) {
    final ArrayList<T> $ = new ArrayList<>();
    for (final F ¢ : fs)
      $.add(f.apply(¢));
    return $;
  }

  static <T> boolean before(final Iterable<T> ts, final @NonNull T t1, final @NonNull T t2) {
    boolean seen = false;
    for (final T ¢ : ts) {
      if (!seen && t1.equals(¢))
        seen = true;
      if (seen && t2.equals(¢))
        return true;
    }
    return false;
  }

  static <T> boolean contains(final Iterable<? extends T> ts, final T t) {
    for (final T candidate : ts)
      if (isEqual(t, candidate))
        return true;
    return false;
  }

  static <T> ArrayList<T> copy(final ArrayList<T> to, final Iterable<? extends T> from) {
    return addAll(to, from);
  }

  /**
   * @param <T> type of elements iterated over
   * @param ts  an arbitrary iterable over this type
   * @return the number of elements in this iterable
   */
  static <T> int count(final Iterable<? extends T> ts) {
    int $ = 0;
    for (@SuppressWarnings("unused") final T __ : ts)
      ++$;
    return $;
  }

  /**
   * @param <T> type of elements iterated over
   * @param ts  an arbitrary iterable over this type
   * @param t   an arbitrary object
   * @return the number of elements in the stream which are equal to the parameter
   */
  static <T> int count(final Iterable<? extends T> ts, final T t) {
    int $ = 0;
    for (final T candidate : ts)
      $ += as.bit(isEqual(t, candidate));
    return $;
  }

  static <T> int count(final Iterable<T> ts, final Predicate<T> p) {
    int $ = 0;
    for (final T ¢ : ts)
      $ += as.bit(p.test(¢));
    return $;
  }

  static <T> int count(final T[] ts, final Predicate<T> p) {
    int $ = 0;
    for (final T ¢ : ts)
      $ += as.bit(p.test(¢));
    return $;
  }

  static <@Nullable T> T get(final Iterable<T> ts, final int i) {
    int j = 0;
    for (final T $ : ts)
      if (++j > i)
        return $;
    return null;
  }

  /**
   * Find the first location of a given integer in an array
   *
   * @param j  a value to find in this array
   * @param is an arbitrary array of integers
   * @return the index of the first occurrence of the argument in the array, or -1
   *         if not found.
   */
  static int index(final int j, final int[] is) {
    int $ = 0;
    for (final int ¢ : is) {
      if (¢ == j)
        return $;
      ++$;
    }
    return -1;
  }

  /**
   * Find the first location of a given value in an iterable
   *
   * @param t   a value to find
   * @param ts  an arbitrary iterable
   * @param <T> type of elements iterated over
   * @return the index of the first occurrence of the argument in the iterable, or
   *         -1 if not found.
   */
  static <T> int index(final T t, final Iterable<? extends T> ts) {
    int $ = 0;
    for (final T __ : ts) {
      if (t == __)
        return $;
      ++$;
    }
    return -1;
  }

  static <T> int[] indices(final Collection<? extends T> ts, final Predicate<T> p) {
    final int[] $ = new int[ts.size()];
    int i = 0, position = 0;
    for (final T ¢ : ts) {
      if (p.test(¢))
        $[i++] = position;
      ++position;
    }
    return Arrays.copyOf($, i);
  }

  static <T> void iterate(final T[] ts, final Iteration<T> what) {
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

  static int[] make(final BitSet s) {
    final int[] $ = new int[s.cardinality()];
    for (int ¢ = 0, value = s.nextSetBit(0); value >= 0; value = s.nextSetBit(value + 1))
      $[¢++] = value;
    return $;
  }

  static CharIterable make(final char... cs) {
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

  static <F, T> Iterable<T> make(final F[] fs, final Function<F, T> f) {
    return () -> new iterables.ReadonlyIterator<>() {
      int current;

      @Override public boolean hasNext() {
        return current < fs.length;
      }

      @Override public T next() {
        return f.apply(fs[current++]);
      }
    };
  }

  /**
   * Create an {@link Iterable} over a range of integers.
   *
   * @param i an arbitrary integer
   * @return an {@link Iterable} yields the {@link Integer}s in the range 0
   *         through the value of the parameter.
   */
  static Iterable<Integer> make(final int i) {
    return () -> new iterables.ReadonlyIterator<>() {
      int position;

      @Override public boolean hasNext() {
        return position < i;
      }

      @Override public Integer next() {
        return idiomatic.box(position++);
      }
    };
  }

  static <F, T> Iterable<T> make(final Iterable<F> fs, final Function<F, T> f) {
    return () -> new iterables.ReadonlyIterator<>() {
      final Iterator<F> inner = fs.iterator();

      @Override public boolean hasNext() {
        return inner.hasNext();
      }

      @Override public T next() {
        return f.apply(inner.next());
      }
    };
  }

  static <T> Iterable<T> make(final Iterator<T> ¢) {
    return () -> ¢;
  }

  @SafeVarargs static <T> Iterable<T> make(final T... ¢) {
    return new IterableArray<>(¢);
  }

  static <F, T> Iterable<T> map(final Iterable<? extends F> fs, final Function<F, T> f) {
    final List<T> $ = new ArrayList<>();
    for (final F ¢ : fs)
      $.add(f.apply(¢));
    return $;
  }

  static <E> Iterable<E> reverse(final Iterable<E> in) {
    final List<E> $ = toList(in);
    Collections.reverse($);
    return $;
  }

  static <T> boolean same(final Iterable<? extends T> ts1, final Iterable<? extends T> ts2) {
    if (ts1 == null || ts2 == null)
      return ts1 == ts2;
    final Iterator<? extends T> t1 = ts1.iterator(), $ = ts2.iterator();
    while (t1.hasNext())
      if (!$.hasNext() || t1.next() != $.next())
        return false;
    return !$.hasNext();
  }

  static <T> Iterable<? extends T> select(final Iterable<? extends T> ts, final Predicate<T> p) {
    return new FilteredIterable<T>(ts) {
      @Override public boolean test(final T ¢) {
        return p.test(¢);
      }
    };
  }

  static <T> Iterable<? extends T> select(final T[] ts, final Predicate<T> p) {
    return select(make(ts), p);
  }

  static double[] seq(final double ¢[]) {
    return seq(¢.length);
  }

  /**
   * Construct a finite prefix of the infinite sequence 0,1,2,...
   *
   * @param i a non-negative integers
   * @return an array containing, in order, all non-negative integers up to the
   *         parameter.
   */
  static double[] seq(final int i) {
    final double[] $ = new double[i];
    for (int ¢ = 0; ¢ < i; ++¢)
      $[¢] = ¢;
    return $;
  }

  static <T> ArrayList<T> serialize(final Iterable<? extends T> ¢) {
    return copy(new ArrayList<T>(count(¢)), ¢);
  }

  static <T> Iterable<T> sort(final Iterable<T> os) {
    return addAll(new TreeSet<T>(), os);
  }

  static <T> Iterable<T> sort(final Iterable<T> os, final Comparator<T> c) {
    return addAll(new TreeSet<>(c), os);
  }

  static String[] toArray(final Collection<String> ss) {
    final String[] $ = new String[ss.size()];
    int i = 0;
    for (final String ¢ : ss)
      $[i++] = ¢;
    return $;
  }

  static <E> E[] toArray(final Iterable<? extends E> in, final Class<E> clazz) {
    final List<E> $ = toList(in);
    @SuppressWarnings("unchecked") final E[] __ = (E[]) Array.newInstance(clazz, $.size());
    return $.toArray(__);
  }

  static double[] toArray(final Iterable<Double> ¢) {
    return toArray(toList(¢));
  }

  static double[] toArray(final List<Double> ds) {
    final double[] $ = new double[ds.size()];
    int i = 0;
    for (final Double ¢ : ds)
      $[i++] = it(¢);
    return $;
  }

  static List<Double> toList(final double... ds) {
    final List<Double> $ = new ArrayList<>();
    for (final double ¢ : ds)
      $.add(box.it(¢));
    return $;
  }

  static List<Integer> toList(final int... is) {
    final List<Integer> $ = new ArrayList<>();
    for (final int ¢ : is)
      $.add(box.it(¢));
    return $;
  }

  static <T> List<T> toList(final Iterable<? extends T> ¢) {
    return addAll(new ArrayList<T>(), ¢);
  }

  @SafeVarargs static <T> ArrayList<T> toList(final T... ¢) {
    return new ArrayList<>(Arrays.asList(¢));
  }

  static String toString(final Iterable<String> items, final String sep) {
    String $ = "";
    for (final String ¢ : items)
      $ += ¢ + sep;
    return $;
  }

  static String toString(final Set<Entry<String, String>> entrySet, final String sep) {
    String $ = "";
    for (final Entry<String, String> ¢ : entrySet)
      $ += ¢ + sep;
    return $;
  }

  @SafeVarargs static <T> List<T> union(final List<T>... tss) {
    final List<T> $ = new ArrayList<>();
    for (final List<T> ¢ : tss)
      $.addAll(¢);
    return $;
  }

  /**
   * Determines whether an iterable is null or empty
   *
   * @param <T>
   * @param ¢   an arbitrary iterable
   * @return <code><b>true</b></code> <em>if an only if</em> the parameter is
   *         <code><b>null</b></code> or offers no values.
   */
  static <T> boolean vacuous(final Iterable<T> ¢) {
    return ¢ == null || is.empty(¢);
  }

  static <T> boolean isEqual(final T a, final T b) {
    return b == a || a != null && a.equals(b);
  }
}
