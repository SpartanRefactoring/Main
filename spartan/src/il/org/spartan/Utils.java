package il.org.spartan;

import static il.org.spartan.azzert.*;
import static org.junit.Assert.*;
import il.org.spartan.Utils.FoundHandleForT.FoundHandleForInt;

import java.io.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;
import org.junit.runners.*;

/**
 * An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> utility functions of reasonably wide use.
 *
 * @author Yossi Gil <code><yossi.gil [at] gmail.com></code>
 * @since 2013/07/01
 */
public enum Utils {
  ;
  /**
   * Appends an element to an array, by reallocating an array whose size is
   * greater by one and placing the element at the last position.
   *
   * @param ts an arbitrary array
   * @param t an element
   * @return the newly created array
   */
  public static <T> T[] append(final T[] ts, final T t) {
    final T[] $ = Arrays.copyOf(ts, 1 + ts.length);
    $[ts.length] = t;
    return $;
  }
  public static <F, T> Applicator<F, T> apply(final Function<F, T> f) {
    return new Applicator<>(f);
  }
  /**
   * Removes the @Nullable annotation present on the type of a value. This
   * function is mainly used to make <code><b>null</b></code> checkers happy.
   * <p>
   * The parameter is an instance of an arbitrary type, T. The hidden assumption
   * is that a @Nullable annotation is present on T. Thus, the parameter may be
   * either <code><b>null</b></code>, or an actual instance of T.
   * <p>
   * The function returns the same instance it received as a parameter, except
   * that this instance is returned as an instance of the type T <i>without</i>
   * the @Nullable annotation. Execution is aborted with an
   * {@link AssertionError} if the parameter is null.
   * <p>
   * As it turns out, this function is a (slow) logical no-op, but still
   * applicable to arguments of type T, where T does not have the @Nullable
   * annotation present on it.
   * <p>
   * For reasons related to the way non-nullability is managed in Java, the
   * compiler will not warn you from doing applying this function to a
   * {@link NonNull} type. However, there is absolutely no point in removing a @Nullable
   * annotation if the type that does not have it. Doing so a is plain clutter.
   * Since the compiler cannot assist you, you will have to be on the guard.
   *
   * @param <T> an arbitrary type
   * @param $ an instance of the type parameter
   * @return its parameter, after verifying that it is not
   *         <code><b>null</b></code>
   * @see #mustBeNull(Object)
   */
  public static <T> T cantBeNull(final @Nullable T $) {
    assert $ != null;
    return $;
  }
  /**
   * Impose an ordering on type <code><b>boolean</b></code> by which
   * <code><b>true</b></code> is greater than <code><b>false</b></code>.
   *
   * @param b1 JD
   * @param b2 JD
   * @return an integer that is negative, zero or positive depending on whether
   *         the first argument is less than, equal to, or greater than the
   *         second.
   * @see Comparable
   * @see Comparator
   */
  public static int compare(final boolean b1, final boolean b2) {
    return b1 == b2 ? 0 : b1 ? 1 : -1;
  }
  /**
   * Remove all non-essential spaces from a string that represents Java code.
   *
   * @param javaCodeFragment JD
   * @return the parameter, with all redundant spaces removed from it
   */
  public static String compressSpaces(final String javaCodeFragment) {
    String $ = javaCodeFragment//
        .replaceAll("(?m)\\s+", " ") // Squeeze whites
        .replaceAll("^\\s", "") // Opening whites
        .replaceAll("\\s$", "") // Closing whites
        ;
    for (final String operator : new String[] { ":", "/", "%", ",", "\\{", "\\}", "=", ":", "\\?", ";", "\\+", ">", ">=", "!=",
        "==", "<", "<=", "-", "\\*", "\\|", "\\&", "%", "\\(", "\\)", "[\\^]" })
      $ = $ //
      .replaceAll(WHITES + operator, operator) // Preceding whites
      .replaceAll(operator + WHITES, operator) // Trailing whites
      ;
    return $;
  }
  /**
   * Determine if a string contains any of a list of patterns.
   *
   * @param text string to be tested
   * @param patterns a list of substrings
   * @return tree iff the the first parameter contains any of the substrings
   *         found in the second parameter
   */
  public static boolean contains(final String text, final String... patterns) {
    for (final String pattern : patterns)
      if (pattern != null && text.contains(pattern))
        return true;
    return false;
  }
  /**
   * Deletes a specified element from an array, by reallocating an array whose
   * size is smaller by one and shifting the other elements down.
   *
   * @param ts an arbitrary array
   * @param i position of element to be deleted
   * @return the newly created array
   */
  public static <T> T[] delete(final T[] ts, final int i) {
    final T[] $ = Arrays.copyOf(ts, ts.length - 1);
    System.arraycopy(ts, i + 1, $, i, $.length - i);
    return $;
  }
  public static FoundHandleForInt found(final int i) {
    return new FoundHandleForInt(i);
  }
  public static <T> FoundHandleForT<T> found(final T t) {
    return new FoundHandleForT<T>(t);
  }
  /**
   * Determine if an item can be found in a list of values
   *
   * @param candidate what to search for
   * @param ts where to search
   * @return true if the the item is found in the list
   */
  @SafeVarargs public static <T> boolean in(final T candidate, final T... ts) {
    for (final T t : ts)
      if (t != null && t.equals(candidate))
        return true;
    return false;
  }
  /**
   * Determine whether an integer is a valid list index
   *
   * @param i some integer
   * @param ts a list of things
   * @return <code><b>true</b></code> <i>iff</i> the index is valid index into
   *         the list. and it is the last one in it.
   */
  public static <T> boolean inRange(final int i, final List<T> ts) {
    return i >= 0 && i < ts.size();
  }
  /**
   * Determine if an integer can be found in a list of values
   *
   * @param candidate what to search for
   * @param is where to search
   * @return true if the the item is found in the list
   */
  @SafeVarargs public static boolean intIsIn(final int candidate, final int... is) {
    for (final int i : is)
      if (i == candidate)
        return true;
    return false;
  }

  /**
   * @param ts JD
   * @return the last item in a list or <code><b>null</b></code> if the
   *         parameter is <code><b>null</b></code> or empty
   */
  public static <@Nullable T> T last(final List<T> ts) {
    return ts.isEmpty() ? null : ts.get(ts.size() - 1);
  }
  /**
   * Determine whether an {@link Object} is the last in a {@link List}.
   *
   * @param o JD
   * @param os JD
   * @return <code><b>true</b></code> <i>iff</i> the {@link Object} parameter is
   *         the same as the last element of the {@link List} parameter
   */
  public static boolean lastIn(final Object o, final List<?> os) {
    return last(os) == o;
  }
  /**
   * Computes the maximum of two or more integers.
   *
   * @param a some integer
   * @param is additional integers
   * @return the largest of the parameters
   */
  public static int max(final int a, final int... is) {
    int $ = a;
    for (final int i : is)
      $ = Math.max($, i);
    return $;
  }
  /**
   * Computes the minimum of two or more integers
   *
   * @param a some integer
   * @param is additional
   * @return the smallest of the parameters
   */
  public static int min(final int a, final int... is) {
    int $ = a;
    for (final int i : is)
      $ = Math.min($, i);
    return $;
  }
  /**
   * Aborts in case a given value is <code><b>null</b></code>.
   * <p>
   * This function is the lesser used dual of {@link #cantBeNull(Object)}.
   *
   * @param <T> some arbitrary type
   * @param $ an instance of the type parameter which is required to be
   *          <code><b>null</b></code>.
   * @return the parameter
   */
  public static <@Nullable T> @Nullable Void mustBeNull(final T $) {
    assert $ == null;
    return null;
  }
  /**
   * Convert variadic list of arguments into an array
   *
   * @param os JD _
   * @return the parameter, as an array.
   */
  public static Object[] objects(final Object... os) {
    return os;
  }
  /**
   * @param ts a list
   * @return the last item in a list or <code><b>null</b></code> if the
   *         parameter is <code><b>null</b></code> or empty
   */
  public static <@Nullable T> T penultimate(final @Nullable List<T> ts) {
    return ts == null || ts.size() < 2 ? null : ts.get(ts.size() - 2);
  }
  /**
   * Determine whether an {@link Object} is penultimate in its {@link Block}.
   *
   * @param o JD
   * @param os JD
   * @return <code><b>true</b></code> <i>iff</i> the an {@link Object} parameter
   *         occurs as the penultimate element of the {@link List} parameter
   */
  public static boolean penultimateIn(final Object o, final List<?> os) {
    assert os != null;
    return penultimate(os) == o;
  }
  /**
   * Prepend a given <code><b>char</b></code> to a {@link StringBuilder}
   *
   * @param $ prepend to what
   * @param c what needs to be prepended
   * @return the {@link StringBuilder} parameter with the
   *         <code><b>char</b></code> parameter prepended to it
   */
  public static StringBuilder prepend(final StringBuilder $, final char c) {
    return cantBeNull($.insert(0, c));
  }
  /**
   * Prepend a given {@link String} to a {@link StringBuilder}
   *
   * @param $ prepend to what
   * @param s what needs to be prepended
   * @return the {@link StringBuilder} parameter with the {@link String}
   *         parameter prepended to it
   */
  public static StringBuilder prepend(final StringBuilder $, final String s) {
    return cantBeNull($.insert(0, s));
  }
  /**
   * Remove any duplicates that may be present in a given {@link List}
   *
   * @param ts JD
   */
  public static <T> void removeDuplicates(final List<T> ts) {
    final Set<T> noDuplicates = new LinkedHashSet<>(ts);
    ts.clear();
    ts.addAll(noDuplicates);
  }
  /**
   * Remove all occurrences of a given prefix from a given {@link String}.
   *
   * @param s JD
   * @param prefix what should be removed
   * @return the parameter after all such occurrences are removed.
   */
  public static String removePrefix(final String s, final String prefix) {
    for (String $ = s;; $ = $.substring(prefix.length()))
      if (!$.startsWith(prefix))
        return $;
  }
  /**
   * Remove all occurrences of a given suffix from a given string.
   *
   * @param s JD
   * @param suffix what should be removed
   * @return the parameter after all such occurrences are removed.
   */
  public static String removeSuffix(final String s, final String suffix) {
    for (String $ = s;; $ = $.substring(0, $.length() - suffix.length()))
      if (!$.endsWith(suffix))
        return $;
  }
  /**
   * Remove all occurrences of white space character in a given {@link String}
   *
   * @param s JD
   * @return the parameter after all such occurrences are removed.
   */
  public static String removeWhites(final String s) {
    return s.replaceAll("\\s+", "");
  }
  /**
   * Sorts an array
   *
   * @param is what to sort
   * @return the given array with elements in sorted order
   */
  public static int[] sort(final int[] is) {
    Arrays.sort(is);
    return is;
  }
  /**
   * Computes the square of a given number
   *
   * @param d some number
   * @return the square of the parameter
   */
  public static double sqr(final double d) {
    return d * d;
  }
  /**
   * Computes the square of a given number
   *
   * @param i some integer
   * @return the square of the parameter
   */
  public static int sqr(final int i) {
    return i * i;
  }
  /**
   * Determine whether a file name ends with any one of the supplied extensions.
   *
   * @param f a file to examine
   * @param suffixes a list of potential extensions.
   * @return <code><b>true</b></code> <em>iff</em>the file name ends with any
   *         one of the supplied extensions.
   */
  public static boolean suffixedBy(final File f, final Iterable<String> suffixes) {
    return suffixedBy(f.getName(), suffixes);
  }
  /**
   * Determine whether a file name ends with any one of the supplied extensions.
   *
   * @param f a file to examine
   * @param suffixes a list of potential extensions.
   * @return <code><b>true</b></code> <em>iff</em>the file name ends with any
   *         one of the supplied extensions.
   */
  public static boolean suffixedBy(final File f, final String... suffixes) {
    return suffixedBy(f.getName(), suffixes);
  }
  /**
   * Determine whether a string ends with any one of the supplied suffixes.
   *
   * @param s a string to examine
   * @param suffixes a list of potential suffixes
   * @return <code><b>true</b></code> <em>iff</em> <code>s</code> ends with any
   *         one of the supplied suffixes.
   */
  public static boolean suffixedBy(final String s, final Iterable<String> suffixes) {
    for (final String end : suffixes)
      if (s.endsWith(end))
        return true;
    return false;
  }
  /**
   * Determine whether a string ends with any one of the supplied suffixes.
   *
   * @param s a string to examine
   * @param suffixes a list of potential suffixes
   * @return <code><b>true</b></code> <em>iff</em> <code>s</code> ends with any
   *         one of the supplied suffixes.
   */
  public static boolean suffixedBy(final String s, final String... suffixes) {
    for (final String end : suffixes)
      if (s.endsWith(end))
        return true;
    return false;
  }
  /**
   * Swap the contents of two cells in a given array
   *
   * @param <T> type of array elements
   * @param ts the given array
   * @param i index of one cell
   * @param j index of another cell
   */
  public static <T> void swap(final T[] ts, final int i, final int j) {
    final T t = ts[i];
    ts[i] = ts[j];
    ts[j] = t;
  }

  public static <T, C extends Collection<T>> accumulate<T, C> to(final C c) {
    return new accumulate<T, C>() {
      @Override public accumulate<T, C> add(final @Nullable T t) {
        if (t == null)
          return this;
        assert t != null;
        c.add(t);
        return this;
      }
      @Override public C elements() {
        return c;
      }
    };
  }

  static final String WHITES = "(?m)\\s+";


  /**
   * Reifies the notion of a function
   *
   * @author Yossi Gil
   * @param <F> the type of the function's argument
   * @param <T> the type of the function's result
   */
  public static class Applicator<F, T> {
    /**
     * Instantiates this class
     *
     * @param function which function to apply?
     */
    public Applicator(final Function<F, T> function) {
      this.function = function;
    }

    @SafeVarargs public final Iterable<T> to(final F... fs) {
      final List<T> $ = new ArrayList<>();
      for (final F f : fs)
        if (f != null)
          $.add(function.apply(f));
      return $;
    }
    public <FS extends Iterable<? extends F>> Iterable<T> to(final FS s) {
      final List<T> $ = new ArrayList<>();
      for (final @Nullable F f : s)
        if (f != null)
          $.add(function.apply(f));
      return $;
    }
    private final Function<F, T> function;
  }

  public static class FoundHandleForT<T> {
    /**
     * Instantiates this class. *
     *
     * @param candidate what to search for
     */
    public FoundHandleForT(final T candidate) {
      this.candidate = candidate;
    }

    /**
     * Determine if an integer can be found in a list of values
     *
     * @param ts where to search
     * @return true if the the item is found in the list
     */
    @SafeVarargs public final boolean in(final T... ts) {
      for (final T t : ts)
        if (t != null && t.equals(candidate))
          return true;
      return false;
    }
    final T candidate;

    public static class FoundHandleForInt {
      /**
       * Instantiates this class.
       *
       * @param candidate what to search for
       */
      public FoundHandleForInt(final int candidate) {
        this.candidate = candidate;
      }

      /**
       * Determine if an integer can be found in a list of values
       *
       * @param is where to search
       * @return true if the the item is found in the list
       */
      @SafeVarargs public final boolean in(final int... is) {
        for (final int i : is)
          if (i == candidate)
            return true;
        return false;
      }
      final int candidate;
    }
  }

  /**
   * A static nested class hosting unit tests for the nesting class Unit test
   * for the containing class. Note the naming convention: a) names of test
   * methods do not use are not prefixed by "test". This prefix is redundant. b)
   * test methods begin with the name of the method they check.
   *
   * @author Yossi Gil
   * @since 2014-05-31
   */
  @FixMethodOrder(MethodSorters.NAME_ASCENDING)//
  @SuppressWarnings({ "static-method", "javadoc", })//
  public static class TEST {
    public static Integer[] intToIntegers(final int... is) {
      final Integer[] $ = new Integer[is.length];
      for (int i = 0; i < is.length; ++i)
        $[i] = box.it(is[i]);
      return $;
    }
    @Test public void addAllTypical() {
      final Set<String> ss = new HashSet<>();
      to(ss).addAll(as.set("A", "B"), null, as.set("B", "C", "D"));
      assertFalse(ss.contains("E"));
      assertFalse(ss.contains(null));
      assertEquals(4, ss.size());
      for (final String s : ss)
        assertTrue(ss.contains(s));
    }
    @Test public void addTypical() {
      final Set<String> ss = new HashSet<>();
      to(ss).add(null, "A", null, "B", "B", null, "C", "D", null);
      assertFalse(ss.contains("E"));
      assertFalse(ss.contains(null));
      assertEquals(4, ss.size());
      for (final String s : ss)
        assertTrue(ss.contains(s));
    }
    @Test public void cantBeNullOfNull() {
      try {
        cantBeNull(null);
        fail("AssertionError expected prior to this line.");
      } catch (final AssertionError e) {
        assertTrue(true);
      }
    }
    @Test public void cantBeNullTypical() {
      assertNotNull(cantBeNull(new Object()));
    }
    @Test public void isNullTypical() {
      try {
        assertNull(mustBeNull(null));
        fail("AssertionError expected prior to this line.");
      } catch (final AssertionError e) {
        assertTrue(true);
      }
    }
    @Test public void mustBeNullOfNonNull() {
      try {
        mustBeNull(new Object());
        fail("AssertionError expected prior to this line.");
      } catch (final AssertionError e) {
        assertTrue(true);
      }
    }
    @Test public void quoteEmptyString() {
      assertEquals("''", idiomatic.quote(""));
    }
    @Test public void quoteNull() {
      assertEquals("<null reference>", idiomatic.quote(null));
    }
    @Test public void quoteSimpleString() {
      assertEquals("'A'", idiomatic.quote("A"));
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
      final Integer[] $ = intToIntegers(29, 1, 60);
      swap($, 0, 1);
      assertArrayEquals(intToIntegers(1, 29, 60), $);
    }
  }
}
