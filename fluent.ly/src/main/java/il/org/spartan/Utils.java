package il.org.spartan;

import static org.junit.Assert.*;

import static fluent.ly.azzert.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

import org.junit.*;

import fluent.ly.*;

/** An empty <code><b>interface</b></code> with a variety of <code>public
 * static</code> utility functions of reasonably wide use.
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>
 * @since 2013/07/01 */
public interface Utils {
  String WHITES = "(?m)\\s+";

  static <T, C extends Collection<T>> C add(final C $, final Iterable<? extends T> ts) {
    for (final T ¢ : ts)
      if (¢ != null)
        $.add(¢);
    return $;
  }
  @SafeVarargs static <T, C extends Collection<T>> C add(final C $, final T... ts) {
    for (final T ¢ : ts)
      if (¢ != null)
        $.add(¢);
    return $;
  }
  @SafeVarargs static <T, C extends Collection<T>> C addAll(final C $, final Collection<? extends T>... tss) {
    for (final Collection<? extends T> ¢ : tss)
      if (¢ != null)
        $.addAll(¢);
    return $;
  }
  @SafeVarargs static <T, C extends Collection<T>> C addAll(final C $, final Iterable<? extends T>... tss) {
    for (final Iterable<? extends T> ¢ : tss)
      if (¢ != null)
        add($, ¢);
    return $;
  }
  @SafeVarargs static <T, C extends Collection<T>> C addAll(final C $, final T... ts) {
    for (final T ¢ : ts)
      if (¢ != null)
        add($, ¢);
    return $;
  }
  /** Appends an element to an array, by reallocating an array whose size is
   * greater by one and placing the element at the last position.
   * @param <T> JD
   * @param ts an arbitrary array
   * @param t an element
   * @return newly created array */
  static <T> T[] append(final T[] ts, final T t) {
    final T[] $ = Arrays.copyOf(ts, 1 + ts.length);
    $[ts.length] = t;
    return $;
  }
  /** @param <F> JD
   * @param <T> JD
   * @param ¢ JD */
  static <F, T> Applicator<F, T> apply(final Function<F, T> ¢) {
    return new Applicator<>(¢);
  }
  /** Removes the annotation present on the type of a value. This function is
   * mainly used to make <code><b>null</b></code<]> checkers happy.
   * <p>
   * The parameter is an instance of an arbitrary type, T. The hidden assumption
   * is that a annotation is present on T. Thus, the parameter may be either
   * <code><b>null</b></code<]>, or an actual instance of T.
   * <p>
   * The function returns the same instance it received as a parameter, except
   * that this instance is returned as an instance of the type T <i>without</i>
   * the annotation. Execution is aborted with an {@link AssertionError} if the
   * parameter is null.
   * <p>
   * as it turns out, this function is a (slow) logical no-op, but still
   * applicable to arguments of type T, where T does not have the annotation
   * present on it.
   * <p>
   * For reasons related to the way non-nullability is managed in Java, the
   * compiler will not warn you from doing applying this function to a
   * {@link NonNull} type. However, there is absolutely no point in removing a
   * annotation if the type that does not have it. Doing so a is plain clutter.
   * Since the compiler cannot assist you, you will have to be on the guard.
   * @param $ result
   * @param <T> JD
   * @return parameter, but guaranteed to be {@link NonNull}
   * @see #mustBeNull(Object) */
  static <T> T canBeNull(final T $) {
    return $;
  }
  /** Removes the annotation present on the type of a value. This function is
   * mainly used to make <code><b>null</b></code> checkers happy.
   * <p>
   * The parameter is an instance of an arbitrary type, T. The hidden assumption
   * is that a annotation is present on T. Thus, the parameter may be either
   * <code><b>null</b></code>, or an actual instance of T.
   * <p>
   * The function returns the same instance it received as a parameter, except
   * that this instance is returned as an instance of the type T <i>without</i>
   * the annotation. Execution is aborted with an {@link AssertionError} if the
   * parameter is null.
   * <p>
   * As it turns out, this function is a (slow) logical no-op, but still
   * applicable to arguments of type T, where T does not have the annotation
   * present on it.
   * <p>
   * For reasons related to the way non-nullability is managed in Java, the
   * compiler will not warn you from doing applying this function to a
   * {@link NonNull} type. However, there is absolutely no point in removing a
   * annotation if the type that does not have it. Doing so a is plain clutter.
   * Since the compiler cannot assist you, you will have to be on the guard.
   * @param <T> an arbitrary type
   * @param $ an instance of the type parameter
   * @return its parameter, after verifying that it is not
   *         <code><b>null</b></code>
   * @see #mustBeNull(Object) */
  static <T> T cantBeNull(final T $) {
    assert $ != null;
    return $;
  }
  /** Impose an ordering on type <code><b>boolean</b></code> by which
   * <code><b>true</b></code> is greater than <code><b>false</b></code>.
   * @param b1 JD
   * @param b2 JD
   * @return an integer that is negative, zero or positive depending on whether
   *         the first argument is less than, equal to, or greater than the
   *         second.
   * @see Comparable
   * @see Comparator */
  static int compare(final boolean b1, final boolean b2) {
    return b1 == b2 ? 0 : b1 ? 1 : -1;
  }
  /** Remove all non-essential spaces from a string that represents Java code.
   * @param javaCodeFragment JD
   * @return parameter, with all redundant spaces removed from it */
  static String compressSpaces(final String javaCodeFragment) {
    String $ = javaCodeFragment.replaceAll("(?m)\\s+", " ").replaceAll("^\\s", "").replaceAll("\\s$", "");
    for (final String operator : new String[] { ":", "/", "%", ",", "\\{", "\\}", "=", ":", "\\?", ";", "\\+", ">", ">=", "!=", "==", "<", "<=", "-",
        "\\*", "\\|", "\\&", "%", "\\(", "\\)", "[\\^]" })
      $ = $.replaceAll(WHITES + operator, operator).replaceAll(operator + WHITES, operator);
    return cantBeNull($);
  }
  /** Determine whether a string contains any of a list of patterns.
   * @param text string to be tested
   * @param patterns a list of substrings
   * @return tree iff the the first parameter contains any of the substrings
   *         found in the second parameter */
  static boolean contains(final String text, final String... patterns) {
    for (final String pattern : patterns)
      if (pattern != null && text.contains(pattern))
        return true;
    return false;
  }
  /** Deletes a specified element from an array, by reallocating an array whose
   * size is smaller by one and shifting the other elements down.
   * @param <T> JD
   * @param ts an arbitrary array
   * @param i position of element to be deleted
   * @return newly created array */
  static <T> T[] delete(final T[] ts, final int i) {
    final T[] $ = Arrays.copyOf(ts, ts.length - 1);
    System.arraycopy(ts, i + 1, $, i, $.length - i);
    return $;
  }
  /** Aborts in case a given value is <code><b>null</b></code>.
   * <p>
   * This function is the lesser used dual of {@link #cantBeNull(Object)} .
   * @param <T> some arbitrary type
   * @param $ an instance of the type parameter which is required to be
   *        <code><b>null</b></code>.
   * @return parameter */
  static <T> Void mustBeNull(final T $) {
    assert $ == null;
    return null;
  }
  /** @param ¢ JD
   * @return name of the parameter, which must not be
   *         <code><b>null</b></code> */
  static String name(final File ¢) {
    return cantBeNull(¢.getName());
  }
  /** Determine whether an {@link Object} is penultimate in its {@link List} .
   * @param <T> JD
   * @param o JD
   * @param os JD
   * @return <code><b>true</b></code> <i>iff</i> the an {@link Object} parameter
   *         occurs as the penultimate element of the {@link List} parameter */
  static <T> boolean penultimateIn(final T o, final List<T> os) {
    assert os != null;
    return the.penultimateOf(os) == o;
  }
  /** Prepend a given <code><b>char</b></code> to a {@link StringBuilder}
   * @param $ prepend to what
   * @param c what needs to be prepended
   * @return {@link StringBuilder} parameter with the <code><b>char</b></code>
   *         parameter prepended to it */
  static StringBuilder prepend(final StringBuilder $, final char c) {
    return cantBeNull($.insert(0, c));
  }
  /** Prepend a given {@link String} to a {@link StringBuilder}
   * @param $ prepend to what
   * @param s what needs to be prepended
   * @return {@link StringBuilder} parameter with the {@link String} parameter
   *         prepended to it */
  static StringBuilder prepend(final StringBuilder $, final String s) {
    return cantBeNull($.insert(0, s));
  }
  /** Remove any duplicates that may be present in a given {@link List}
   * @param <T> JD
   * @param ts JD */
  static <T> void removeDuplicates(final List<T> ts) {
    final Set<T> noDuplicates = new LinkedHashSet<>(ts);
    ts.clear();
    ts.addAll(noDuplicates);
  }
  /** Remove all occurrences of a given prefix from a given {@link String} .
   * @param s JD
   * @param prefix what should be removed
   * @return parameter after all such occurrences are removed. */
  static String removePrefix(final String s, final String prefix) {
    for (String $ = s;; $ = $.substring(prefix.length()))
      if (!$.startsWith(prefix))
        return $;
  }
  /** Remove all occurrences of a given suffix from a given string.
   * @param s JD
   * @param suffix what should be removed
   * @return parameter after all such occurrences are removed. */
  static String removeSuffix(final String s, final String suffix) {
    for (String $ = s;; $ = $.substring(0, $.length() - suffix.length()))
      if (!$.endsWith(suffix))
        return $;
  }
  /** Remove all occurrences of white space character in a given {@link String}
   * @param ¢ JD
   * @return parameter after all such occurrences are removed. */
  static String removeWhites(final String ¢) {
    return cantBeNull(¢.replaceAll("\\s+", ""));
  }
  /** Sorts an array
   * @param ¢ what to sort
   * @return given array with elements in sorted order */
  static int[] sort(final int[] ¢) {
    Arrays.sort(¢);
    return ¢;
  }
  /** Computes the square of a given double
   * @param ¢ some number
   * @return square of the parameter */
  static double sqr(final double ¢) {
    return ¢ * ¢;
  }
  /** Determine whether a file name ends with any one of the supplied
   * extensions.
   * @param f a file to examine
   * @param suffixes a list of potential extensions.
   * @return <code><b>true</b></code> <em>iff</em>the file name ends with any
   *         one of the supplied extensions. */
  static boolean suffixedBy(final File f, final Iterable<String> suffixes) {
    return suffixedBy(name(f), suffixes);
  }
  /** Determine whether a file name ends with any one of the supplied
   * extensions.
   * @param f a file to examine
   * @param suffixes a list of potential extensions.
   * @return <code><b>true</b></code> <em>iff</em>the file name ends with any
   *         one of the supplied extensions. */
  static boolean suffixedBy(final File f, final String... suffixes) {
    return suffixedBy(name(f), suffixes);
  }
  /** Determine whether a string ends with any one of the supplied suffixes.
   * @param s a string to examine
   * @param suffixes a list of potential suffixes
   * @return <code><b>true</b></code> <em>iff</em> <code>s</code> ends with any
   *         one of the supplied suffixes. */
  static boolean suffixedBy(final String s, final Iterable<String> suffixes) {
    for (final String end : suffixes)
      if (s.endsWith(end))
        return true;
    return false;
  }
  /** Determine whether a string ends with any one of the supplied suffixes.
   * @param s a string to examine
   * @param suffixes a list of potential suffixes
   * @return <code><b>true</b></code> <em>iff</em> <code>s</code> ends with any
   *         one of the supplied suffixes. */
  static boolean suffixedBy(final String s, final String... suffixes) {
    for (final String end : suffixes)
      if (s.endsWith(end))
        return true;
    return false;
  }
  /** Swap the contents of two cells in a given array
   * @param <T> type of array elements
   * @param ts the given array
   * @param i index of one cell
   * @param j index of another cell */
  static <T> void swap(final T[] ts, final int i, final int j) {
    final T t = ts[i];
    ts[i] = ts[j];
    ts[j] = t;
  }

  /** Reifies the notion of a function
   * @author Yossi Gil
   * @param <F> the type of the function's argument
   * @param <T> the type of the function's result */
  class Applicator<F, T> {
    private final Function<F, T> function;

    /** Instantiates this class
     * @param function which function to apply? */
    public Applicator(final Function<F, T> function) {
      this.function = function;
    }
    @SafeVarargs public final Iterable<T> to(final F... fs) {
      final List<T> $ = new ArrayList<>();
      for (final F ¢ : fs)
        if (¢ != null)
          $.add(function.apply(¢));
      return $;
    }
    /** @param <FS> JD
     * @param s JD */
    public <FS extends Iterable<? extends F>> Iterable<T> to(final FS s) {
      final List<T> $ = new ArrayList<>();
      for (final F ¢ : s)
        if (¢ != null)
          $.add(function.apply(¢));
      return $;
    }
  }

  /** A static nested class hosting unit tests for the nesting class Unit test
   * for the containing class. Note the naming convention: a) names of test
   * methods do not use are not prefixed by "test". This prefix is redundant. b)
   * test methods begin with the name of the method they check.
   * @author Yossi Gil
   * @since 2014-05-31 */
  @SuppressWarnings("static-method")
  class TEST {
    public static Integer[] intToIntFunctionegers(final int... is) {
      final Integer[] $ = new Integer[is.length];
      for (int ¢ = 0; ¢ < is.length; ++¢)
        $[¢] = fluent.ly.box.it(is[¢]);
      return $;
    }
    @Test @SuppressWarnings("unchecked") public void addAllTypical() {
      final Set<String> ss = new HashSet<>();
      accumulate.to(ss).addAll(as.set("A", "B"), null, as.set("B", "C", "D"));
      azzert.nay(ss.contains("E"));
      azzert.nay(ss.contains(null));
      azzert.that(ss.size(), is(4));
      for (final String ¢ : ss)
        azzert.aye("", ss.contains(¢));
    }
    @Test public void addTypical() {
      final Set<String> ss = new HashSet<>();
      accumulate.to(ss).add(null, "A", null, "B", "B", null, "C", "D", null);
      azzert.nay(ss.contains("E"));
      azzert.nay(ss.contains(null));
      azzert.that(ss.size(), is(4));
      for (final String ¢ : ss)
        azzert.aye("", ss.contains(¢));
      azzert.aye(ss.contains("A"));
    }
    @Test public void cantBeNullOfNull() {
      try {
        cantBeNull(null);
        azzert.fail("AssertionError expected prior to this line.");
      } catch (final AssertionError e) {
        azzert.aye(e + "", true);
      }
    }
    @Test public void cantBeNullTypical() {
      assert cantBeNull(new Object()) != null;
    }
    @Test public void isNullTypical() {
      try {
        isNull(mustBeNull(null));
        azzert.fail("AssertionError expected prior to this line.");
      } catch (final AssertionError e) {
        azzert.aye("", true);
      }
    }
    @Test public void mustBeNullOfNonNull() {
      try {
        mustBeNull(new Object());
        azzert.fail("AssertionError expected prior to this line.");
      } catch (final AssertionError e) {
        azzert.aye("", true);
      }
    }
    @Test public void quoteEmptyString() {
      azzert.that(idiomatic.quote(""), is("''"));
    }
    @Test public void quoteNull() {
      azzert.that(idiomatic.quote(null), is("<null reference>"));
    }
    @Test public void quoteSimpleString() {
      azzert.that(idiomatic.quote("A"), is("'A'"));
    }
    @Test public void swapDegenerate() {
      final String[] ss = as.array("A", "B", "C", "D");
      swap(ss, 1, 1);
      assertArrayEquals(as.array("A", "B", "C", "D"), ss);
    }
    @Test public void swapTypical() {
      final String[] ss = as.array("A", "B", "C", "D");
      swap(ss, 1, 2);
      assertArrayEquals(as.array("A", "C", "B", "D"), ss);
    }
    @Test public void swapTypicalCase() {
      final Integer[] $ = intToIntFunctionegers(29, 1, 60);
      swap($, 0, 1);
      assertArrayEquals(intToIntFunctionegers(1, 29, 60), $);
    }
  }
}
