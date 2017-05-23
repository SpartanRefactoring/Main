/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.reap;

import static fluent.ly.azzert.*;
import static fluent.ly.idiomatic.*;
import static il.org.spartan.Utils.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.*;

import fluent.ly.*;
import il.org.spartan.reap.Cookbook.Internal.*;
import il.org.spartan.reap.Cookbook.Recipe.*;

/** This interface represents the concept of a <i>lazy symbolic spreadsheet</i>,
 * made by DAG of interdependent {@link Cell}s. A {@link Cell} is either an
 * <ol>
 * <li>{@link Ingredient} is a generic storing a value of the type parameter,
 * <i>or</i> a
 * <li>{@link Recipe} for making such a value from <i>prerequisite</i> cells.
 * </ol>
 * <p>
 * The protocol of cells include:
 * <ol>
 * <li>{@link Cell#get()}, returning the cell's value, recomputing it if it is
 * less recent than any {@link Ingredient} on which it depends, directly or
 * indirectly. The computed value is cached, and used in subsequent calls to
 * prevent unnecessary re-computation.
 * <li>{@link Cell#cache()}, returning the last value cached in the cell.
 * <li>{@link Cell#set(Object)}, whose parameter must be of the correct type,
 * stores its parameter in the cell, whereby invalidating the contents of all
 * cells whose recipe depends on it.
 * <p>
 * When a {@link Recipe} cell is set to a specific value, the recipe is lost
 * forever.
 * </ol>
 * <p>
 * Thus, a cell's value is only evaluated when it is accessed, and only when it
 * out of date with respect to the cells it depends on.
 * <p>
 * Note that
 * <ol>
 * <li>Classes in this interface give several implementations of the cell
 * concept.
 * <li>Inner classes of {@link ____META} provide examples and extensive unit
 * testing.
 * <li>There are no other members: A client class that
 * <code><b>implements</b></code> this interface should be ok.
 * <ol>
 * apply
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
public interface Cookbook {
  /** @param $ result
   * @return parameter */
  static Cell<?>[] asArray(@NonNull final Collection<Cell<?>> $) {
    return $.toArray(new Cell<?>[$.size()]);
  }
  /** write a recipe
   * @param <T> parameter type
   * @param <R> result type
   * @param λ function from the first type argument to the second type argument
   * @return $$Function */
  static <T, R> $$Function<T, R> compute(final Function<T, R> λ) {
    return ¢ -> new Recipe<>(() -> λ.apply(¢.get())).ingredient(¢);
  }
  /** <code>compute</code>
   * @param <T1> first parameter type
   * @param <T2> second parameter type
   * @param <R> result type
   * @param λ function from the first type argument to the second type argument
   * @return $$Function */
  static <T1, T2, R> $$Function2<T1, T2, R> compute(final Function2<T1, T2, R> λ) {
    return (¢1, ¢2) -> new Recipe<>(() -> λ.apply(¢1.get(), ¢2.get())).ingredients(¢1, ¢2);
  }
  /** @param <T> JD
   * @param $ JD
   * @return a newly created {@link Cell} */
  static <T> Cell<T> cook(final Supplier<T> $) {
    Cell.trace = new HashSet<>();
    $.get();
    final Cell<?>[] trace = asArray(Cell.trace);
    Cell.trace = null;
    return new Recipe<>($).ingredients(trace);
  }
  /** Fluent API function to be used as in
   *
   * <pre>
   *  Cell<Integer> d = from(a,b,c).make(.....)
   * </pre>
   *
   * @param ingredients list of ingredients
   * @return a {@link $$RecipeMaker} which can be used to continue the fluent
   *         API chain. */
  static $$RecipeMaker from(final Cell<?>... ingredients) {
    return new $$RecipeMaker() {
      @Override public <T> Cell<T> make(final Supplier<T> ¢) {
        return new Recipe<>(¢).ingredients(ingredients);
      }
    };
  }
  /** creates a new ingredient with a specific type
   * @param <T> JD
   * @return newly created instance */
  static <T> Cell<T> input() {
    return new Ingredient<>();
  }
  /** Fluent API factory method that returns a recipe
   * @param <T> JD
   * @param ¢ JD
   * @return newly created {@link Recipe} object */
  static <T> Recipe<T> recipe(final Supplier<T> ¢) {
    return new Recipe<>(¢);
  }
  /** A factory method for class {@link Ingredient} of an {@link Integer} as in
   *
   * <pre>
   * Cell&lt;String&gt; genesis = Cookbook.value(2);
   * </pre>
   *
   * @param ¢ JD
   * @return newly created instance of {@link Ingredient} */
  static Cell<Integer> value(final int ¢) {
    return new Ingredient<>(Integer.valueOf(¢));
  }
  /** A factory method for class {@link Ingredient} as in
   *
   * <pre>
   * Cell&lt;String&gt; genesis = Cookbook.value(&quot;&quot;);
   * </pre>
   *
   * @param <T> JD
   * @param ¢ JD
   * @return newly created instance of {@link Ingredient} */
  static <T> Cell<T> value(final T ¢) {
    return new Ingredient<>(¢);
  }

  /** A repository of test cases and examples
   * <p>
   * <i>the underscores in the name are magic against alphabetical method
   * sorters that cannot distinguish code from meta-code</i>
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  enum ____META {
    ;
    public static class A {
      final Cell<String> begin = value("<");
      final Cell<String> end = new Ingredient<>(">");
      final Cell<String> text = value("p");
      final Cell<String> wrap = from(begin, end, text).make(() -> begin() + text() + end());
      final Cell<String> xBoxed = from(wrap).make(() -> "[[" + wrap() + "]]");
      final Cell<String> zzz = new Ingredient<String>().of("zzz");

      String begin() {
        return begin.get();
      }
      String end() {
        return end.get();
      }
      String text() {
        return text.get();
      }
      String wrap() {
        return wrap.get();
      }

      @SuppressWarnings("synthetic-access")
      public static class TEST extends A {
        @Test public void sessionA00() {
          azzert.that(wrap(), is("<p>"));
        }
        @Test public void sessionA01() {
          begin.set("(");
          azzert.that(wrap(), is("(p>"));
          end.set(")");
          azzert.that(wrap(), is("(p)"));
        }
        @Test public void sessionA02() {
          azzert.that(wrap, instanceOf(Recipe.class));
          final Recipe<?> r = (Recipe<?>) wrap;
          azzert.that(r.dependents.size(), is(1));
          azzert.that(r.prerequisites.size(), is(3));
          azzert.that(begin(), is("<"));
          azzert.that(end(), is(">"));
          azzert.that(text(), is("p"));
          azzert.that(wrap(), is("<p>"));
        }
        @Test public void sessionA03() {
          azzert.that(xBoxed.get(), is("[[<p>]]"));
          end.set("+");
          azzert.that(xBoxed.get(), is("[[<p+]]"));
        }
        /** Local ingredients and recipes */
        @Test public void sessionA04() {
          azzert.that(zzz.get(), is("zzz"));
          final Cell<String> foo = value("foo"), ba = value("ba"), bazzz = from(ba, zzz).make(() -> ba.get() + zzz.get());
          azzert.that(from(foo, bazzz).make(() -> foo.get() + bazzz.get()).get(), is("foobazzz"));
        }
        /** Cloning */
        @Test public void sessionA05() {
          final Cell<String> foo = value("foo"), ba = value("ba"), bazzz = from(ba, zzz).make(() -> ba.get() + zzz.get());
          azzert.that(from(foo, bazzz).make(() -> foo.get() + bazzz.get()).clone().get(), is("foobazzz"));
        }
      }
    }

    @SuppressWarnings({ "static-method", "null" })
    public static class C {
      @Test public void sessionA01() {
        azzert.that(compute((final Integer ¢) -> "(" + ¢ + ")").from(value(Integer.valueOf(12))).get(), is("(12)"));
      }
      @Test public void sessionA02() {
        azzert.that(compute((final Integer ¢) -> "(" + ¢ + ")").from(value(Integer.valueOf(12))).get(), is("(12)"));
      }
      @Test public void sessionA03() {
        azzert.that(compute((final Integer ¢) -> "(" + ¢ + ")").from(value(Integer.valueOf(12))).get(), is("(12)"));
      }
      @Test public void sessionA04() {
        azzert.that(
            compute((final Integer i, final Character c) -> c + "(" + i + ")").from(value(Integer.valueOf(13)), value(Character.valueOf('f'))).get(),
            is("f(13)"));
      }
      @Test @SuppressWarnings("synthetic-access") public void sessionA05() {
        final Cell<Integer> x = value(Integer.valueOf(13));
        final Cell<Character> f = value(Character.valueOf('f'));
        final Cell<String> fx = cook(() -> f.get() + "(" + x.get() + ")");
        azzert.that(fx.dependents.size(), is(0));
        azzert.that(f.dependents.size(), is(1));
        azzert.that(((Recipe<String>) fx).prerequisites.size(), is(2));
        azzert.that(fx.get(), is("f(13)"));
        f.set(Character.valueOf('g'));
        azzert.that(fx.dependents.size(), is(0));
        azzert.that(((Recipe<String>) fx).prerequisites.size(), is(2));
        azzert.that(fx.get(), is("g(13)"));
      }
    }

    /** Should not be used by clients. A development time only
     * <code><b>class</b></code> used for testing and as a documented demo of
     * using {@link Cookbook} .
     * <p>
     * All cells here are of type <code></code {@link Integer} . The
     * {@Valued} cells in this example are:
     * <ol>
     * <li>{@link #a()}
     * <li>{@link #b()}
     * <li>{@link #c()}
     * </ol>
     * <p>
     * The {@link Recipe} cells in the example are:
     * <ol>
     * <li>{@link #aPower02()} ,
     * <li>{@link #aPower03()} ,
     * <li>{@link #aPower05()} ,
     * <li>{@link #aPower17NullSafe()} , and,
     * <li>{@link #d()} .
     * </ol>
     * <p>
     * @author Yossi Gil <Yossi.Gil@GMail.COM>
     * @since 2016 */
    @SuppressWarnings("boxing")
    public static class Z implements Cookbook {
      /** Must not be private; used for testing of proper lazy evaluation */
      int __aPower02Calls;
      /** Must not be private; used for testing of proper lazy evaluation */
      int __aPower03Calls;
      /** Can, and often should be made private; package is OK */
      final Cell<Integer> a = new Cookbook.Ingredient<>();
      /** Can, and often should be made private; package is OK */
      final Cell<Integer> aPower02 = new Recipe<>(() -> {
        ++__aPower02Calls;
        return a() * a();
      }).ingredients(a);
      /** Can, and often should be made private; package is OK */
      final Cell<Integer> aPower03 = new Recipe<>(() -> {
        ++__aPower03Calls;
        return a() * aPower02();
      }).ingredients(aPower02, a);
      /** the actual cell behind {@link #aPower05()} */
      final Cell<Integer> aPower05 = new Recipe<>(() -> aPower02() * aPower03()).ingredients(aPower02, aPower03);
      /** the actual cell behind {@link #b()} */
      final Cell<Integer> aPower17NullSafe = new Recipe.NullRobust<>(
          () -> a() * a() * a() * a() * aPower02() * aPower02() * aPower03() * aPower03() * aPower03()).ingredients(a, aPower02, aPower03);
      /** the actual cell behind {@link #b()} */
      final Cell<Integer> b = new Cookbook.Ingredient<>(3);
      /** the actual cell behind {@link #c()} */
      final Cell<Integer> c = new Cookbook.Ingredient<>(5);
      /** the actual cell behind {@link #d()} */
      final Cell<Integer> d = Cookbook.from(a, b, c).make(() -> a() + b() + c());

      /** @return contents of cell a */
      public final Integer a() {
        return a.get();
      }
      /** @return contents of cell a^2 := (a)^2 */
      public final Integer aPower02() {
        return aPower02.get();
      }
      /** @return contents of cell a^3 := a^2 * a */
      public final Integer aPower03() {
        return aPower03.get();
      }
      /** @return contents of cell a^5 := a^2 * a^3 */
      public final Integer aPower05() {
        return aPower05.get();
      }
      /** @return contents of cell a^17 := (a)^4 * (a^2)^4 * (a^3)^3 */
      public final Integer aPower17NullSafe() {
        return aPower17NullSafe.get();
      }
      /** @return contents of valued cell <code>b</code> */
      public final Integer b() {
        return b.get();
      }
      /** @return contents of valued cell <code>c</code> */
      public final Integer c() {
        return c.get();
      }
      /** @return contents of cell d := a + b + c */
      public final Integer d() {
        return d.get();
      }

      /** @author Yossi Gil <Yossi.Gil@GMail.COM>
       * @since 2016 */

      public static class TEST extends Z {
        @Test public void sessionA01() {
          azzert.isNull(a());
        }
        @Test public void sessionA05() {
          a.set(2);
          assert a() != null;
        }
        @Test public void sessionA06() {
          a.set(2);
          assert aPower02() != null;
          azzert.that(aPower02(), is(4));
        }
        @Test public void sessionA07() {
          a.set(2);
          assert aPower03() != null;
          azzert.that(aPower03(), is(8));
        }
        @Test public void sessionA08() {
          a.set(2);
          assert aPower02() != null;
        }
        @Test public void sessionA09() {
          a.set(null);
          azzert.isNull(a());
        }
        @Test(expected = NullPointerException.class) public void sessionA10() {
          a.set(null);
          aPower02();
        }
        @Test(expected = NullPointerException.class) public void sessionA11() {
          a.set(null);
          aPower03();
        }
        @Test(expected = NullPointerException.class) public void sessionA12() {
          a.set(null);
          aPower02();
        }
        @Test public void sessionA13() {
          a.set(null);
          azzert.isNull(aPower17NullSafe());
          a.set(2);
          assert aPower17NullSafe() != null;
          azzert.that(a(), is(2));
        }
        @Test public void sessionA14() {
          a.set(2);
          assert aPower17NullSafe() != null;
          azzert.that(a(), is(2));
          azzert.that(aPower17NullSafe(), is(1 << 17));
        }
        @Test public void sessionA15() {
          a.set(null);
          azzert.isNull(aPower17NullSafe());
        }
        @Test public void sessionA16() {
          a.set(null);
          azzert.isNull(aPower17NullSafe.get());
        }
        @Test public void sessionA17() {
          a.set(null);
          azzert.isNull(((NullRobust<?>) aPower17NullSafe).get());
        }
        @Test public void sessionA18() {
          a.set(null);
        }
        @Test public void sessionA19() {
          a.set(null);
          aPower17NullSafe.get();
        }
        @Test(expected = NullPointerException.class) public void sessionA2() {
          aPower02().getClass();
        }
        @Test public void sessionA20() {
          aPower17NullSafe.get();
        }
        @Test(expected = NullPointerException.class) public void sessionA3() {
          aPower03().getClass();
        }
        @Test(expected = NullPointerException.class) public void sessionA4() {
          aPower05().getClass();
        }
        @Test(expected = NullPointerException.class) public void sessionA5() {
          (a() + "").getClass();
        }
        @Test public void sessionB01() {
          a.set(2);
          assert a() != null;
          azzert.that(a(), is(2));
          a.set(3);
          azzert.that(a(), is(3));
          a.set(4);
          azzert.that(a(), is(4));
          a.set(null);
          azzert.isNull(a());
          a.set(5);
          azzert.that(a(), is(5));
        }
        @Test public void sessionB02() {
          a.set(2);
          assert aPower02() != null;
          azzert.that(aPower02(), is(4));
          a.set(3);
          assert aPower02() != null;
          azzert.that(aPower02(), is(9));
        }
        @Test public void sessionB03() {
          a.set(2);
          assert aPower03() != null;
          azzert.that(aPower03(), is(8));
          a.set(3);
          assert aPower03() != null;
          azzert.that(aPower03(), is(27));
        }
        @Test public void sessionB04() {
          a.set(2);
          assert aPower02() != null;
        }
        @Test public void sessionC00() {
          a.set(-3);
          azzert.that(__aPower03Calls, is(0));
          azzert.that(__aPower02Calls, is(0));
        }
        @Test public void sessionC01() {
          a.set(-3);
          azzert.that(aPower03(), is(-27));
          azzert.that(__aPower03Calls, is(1));
          azzert.that(__aPower02Calls, is(1));
        }
        @Test public void sessionC02() {
          azzert.that(a.version(), is(0L));
          azzert.that(aPower17NullSafe.version(), is(0L));
        }
        @Test public void sessionC03() {
          azzert.that(aPower02.version(), is(0L));
          azzert.that(aPower03.version(), is(0L));
        }
        @Test public void sessionC04() {
          a.set(-2);
          azzert.that(a.version(), is(1L));
          azzert.that(aPower03.version(), is(0L));
          azzert.that(__aPower03Calls, is(0));
          azzert.that(aPower17NullSafe(), is(-(1 << 17)));
          azzert.that(__aPower02Calls, is(1));
          azzert.that(__aPower03Calls, is(1));
        }
        @Test public void sessionC05() {
          a.set(-2);
          azzert.that(aPower17NullSafe(), is(-(1 << 17)));
          azzert.that(__aPower02Calls, is(1));
          azzert.that(__aPower03Calls, is(1));
        }
        @Test public void sessionD01() {
          azzert.that(a.version, is(0L));
          azzert.that(aPower02.version, is(0L));
          azzert.that(aPower03.version, is(0L));
          azzert.that(aPower17NullSafe.version, is(0L));
        }
        @Test public void sessionD02() {
          azzert.that(a.version, is(0L));
          a.set(1);
          azzert.that(a.version, is(1L));
          azzert.that(aPower02.version, is(0L));
          azzert.that(aPower03.version, is(0L));
          azzert.that(aPower17NullSafe.version, is(0L));
        }
        @Test public void sessionD03() {
          a.set(14);
          azzert.that(aPower02.version, is(0L));
          azzert.that(aPower02.get(), is(196));
          azzert.that(aPower03.version, is(0L));
          azzert.that(aPower02.version, is(2L));
          azzert.that(aPower17NullSafe.version, is(0L));
        }
        @Test public void sessionD04() {
          a.set(14);
          assert a.get() != null;
        }
        @Test public void sessionD05() {
          a.set(14);
          assert a.get() != null;
          azzert.that(a.get(), is(14));
          azzert.that(aPower02.get(), is(196));
        }
        @Test public void sessionD06() {
          a.set(14);
          assert a.get() != null;
          a.get();
          azzert.that(aPower02.version(), is(0L));
          a.get();
          azzert.that(aPower02.version(), is(0L));
        }
        @Test public void sessionD07() {
          a.set(14);
          assert a.get() != null;
          a.get();
          azzert.not(aPower02.updated());
        }
        @Test public void sessionD08() {
          a.set(14);
          azzert.that(a.get(), is(14));
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version, is(0L));
          azzert.that(((Recipe<Integer>) aPower02).latestPrequisiteVersion(), is(1L));
        }
        @Test public void sessionD09() {
          a.set(14);
          azzert.that(a.get(), is(14));
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version, is(0L));
          assert a.dependents != null;
        }
        @Test public void sessionD10() {
          a.set(14);
          azzert.that(a.get(), is(14));
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version, is(0L));
          azzert.that(a.dependents.size(), is(4));
          azzert.aye("", a.dependents.contains(aPower02));
          azzert.falze(a.dependents.contains(null));
        }
        @Test public void sessionD11() {
          a.set(14);
          azzert.that(a.get(), is(14));
          azzert.that(a.version(), is(1L));
        }
        @Test public void sessionD12() {
          azzert.aye(a.dependents.contains(aPower02));
        }
        @Test public void sessionD13() {
          azzert.aye(a.dependents.contains(aPower03));
        }
        @Test public void sessionD14() {
          assert !a.dependents.contains(aPower05);
        }
        @Test public void sessionD15() {
          azzert.aye(a.dependents.contains(aPower17NullSafe));
        }
        @Test public void sessionD16() {
          a.set(2);
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(__aPower03Calls, is(1));
        }
        @Test public void sessionD17() {
          a.set(2);
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(__aPower02Calls, is(1));
          a.set(3);
          a.set(2);
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(__aPower02Calls, is(2));
          azzert.that(__aPower03Calls, is(2));
        }
        @Test public void sessionE01() {
          azzert.that(a.version(), is(0L));
          azzert.that(aPower02.version(), is(0L));
          azzert.that(aPower03.version(), is(0L));
          azzert.that(aPower05.version(), is(0L));
          azzert.that(aPower17NullSafe.version(), is(0L));
          a.set(2);
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version(), is(0L));
          azzert.that(aPower03.version(), is(0L));
          azzert.that(aPower05.version(), is(0L));
          azzert.that(aPower17NullSafe.version(), is(0L));
          aPower02();
          azzert.that(aPower02(), is(4));
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version(), is(2L));
          azzert.that(aPower03.version(), is(0L));
          azzert.that(aPower05.version(), is(0L));
          azzert.that(aPower17NullSafe.version(), is(0L));
          aPower03();
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version(), is(2L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(0L));
          azzert.that(aPower17NullSafe.version(), is(0L));
          aPower05();
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version(), is(2L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(4L));
          azzert.that(aPower17NullSafe.version(), is(0L));
          aPower17NullSafe();
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version(), is(2L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(4L));
          azzert.that(aPower17NullSafe.version(), is(4L));
          a.set(3);
          azzert.that(a.version(), is(5L));
          azzert.that(aPower02.version(), is(2L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(4L));
          azzert.that(aPower17NullSafe.version(), is(4L));
          aPower02();
          azzert.that(a.version(), is(5L));
          azzert.that(aPower02.version(), is(6L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(4L));
          azzert.that(aPower17NullSafe.version(), is(4L));
          aPower02();
          azzert.that(a.version(), is(5L));
          azzert.that(aPower02.version(), is(6L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(4L));
          azzert.that(aPower17NullSafe.version(), is(4L));
          aPower03();
          azzert.that(a.version(), is(5L));
          azzert.that(aPower02.version(), is(6L));
          azzert.that(aPower03.version(), is(7L));
          azzert.that(aPower05.version(), is(4L));
          azzert.that(aPower17NullSafe.version(), is(4L));
          aPower03();
          azzert.that(a.version(), is(5L));
          azzert.that(aPower02.version(), is(6L));
          azzert.that(aPower03.version(), is(7L));
          azzert.that(aPower05.version(), is(4L));
          azzert.that(aPower17NullSafe.version(), is(4L));
          aPower05();
          azzert.that(a.version(), is(5L));
          azzert.that(aPower02.version(), is(6L));
          azzert.that(aPower03.version(), is(7L));
          azzert.that(aPower05.version(), is(8L));
          azzert.that(aPower17NullSafe.version(), is(4L));
          aPower02();
          aPower03();
          aPower05();
          aPower05();
          aPower05();
          aPower03();
          aPower02();
          azzert.that(a.version(), is(5L));
          azzert.that(aPower02.version(), is(6L));
          azzert.that(aPower03.version(), is(7L));
          azzert.that(aPower05.version(), is(8L));
          azzert.that(aPower17NullSafe.version(), is(4L));
          aPower17NullSafe();
          azzert.that(a.version(), is(5L));
          azzert.that(aPower02.version(), is(6L));
          azzert.that(aPower03.version(), is(7L));
          azzert.that(aPower05.version(), is(8L));
          azzert.that(aPower17NullSafe.version(), is(8L));
          aPower17NullSafe();
          aPower02();
          aPower03();
          aPower05();
          aPower05();
          aPower17NullSafe();
          aPower05();
          aPower03();
          aPower02();
          aPower17NullSafe();
          azzert.that(a.version(), is(5L));
          azzert.that(aPower02.version(), is(6L));
          azzert.that(aPower03.version(), is(7L));
          azzert.that(aPower05.version(), is(8L));
          azzert.that(aPower17NullSafe.version(), is(8L));
        }
        @Test public void sessionE02() {
          azzert.that(a.version(), is(0L));
          azzert.that(aPower02.version(), is(0L));
          azzert.that(aPower03.version(), is(0L));
          azzert.that(aPower05.version(), is(0L));
          azzert.that(aPower17NullSafe.version(), is(0L));
        }
        @Test public void sessionE03() {
          a.set(2);
          b.set(3);
          c.set(4);
          azzert.that(d.get(), is(9));
        }
        @Test public void sessionE04() {
          a.set(2);
          aPower02.set(3);
          aPower03.set(5);
          azzert.that(aPower05.get(), is(15));
        }
        @Test public void sessionE05() {
          a.set(2);
          azzert.that(aPower05.get(), is(1 << 5));
          azzert.that(aPower17NullSafe.version(), is(0L));
          azzert.that(aPower02.version(), is(2L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(4L));
        }
        @Test public void sessionE06() {
          a.set(2);
          assert !aPower05.updated() : "aPower5 should not be updated! (recursive dependency on a)";
        }
        @Test public void sessionF01() {
          a.set(11);
          assert !aPower02.updated();
          azzert.that(aPower02.get(), is(121));
          azzert.aye(aPower02.updated());
          aPower02.set(0xDADA);
          azzert.aye(aPower02.updated());
          azzert.that(aPower02.get(), is(0xDADA));
          a.set(0xCAFE);
          azzert.aye(aPower02.updated());
          azzert.that(aPower02.get(), is(0xDADA));
        }
        @Test public void sessionF02() {
          a.set(null);
          azzert.isNull(aPower17NullSafe());
          azzert.that(aPower05.version(), is(0L));
        }
        @Test public void sessionF03() {
          a.set(2);
          azzert.that(aPower05.get(), is(1 << 5));
          azzert.that(aPower17NullSafe.version(), is(0L));
          azzert.that(aPower02.version(), is(2L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(4L));
        }
        @Test public void sessionF04() {
          a.set(null);
          azzert.isNull(aPower17NullSafe());
        }
        @Test public void sessionF05() {
          azzert.isNull(aPower17NullSafe());
        }
        @Test public void sessionG01() {
          aPower02.set(0xDADA);
          a.set(0xCAFE);
          azzert.aye(aPower02.updated());
        }
        @Test @SuppressWarnings("synthetic-access") public void sessionG02() {
          aPower02.set(0xDADA);
          assert aPower02.cache != null;
          azzert.that(aPower02.cache, is(0xDADA));
          azzert.isNull(((Recipe<?>) aPower02).supplier);
        }
      }
    }
  }

  /** A cell stores a value of some type (which is passed by parameter). A cell
   * may be either {@link Ingredient} or {@link Recipe} . A computed cell
   * typically depends on other cells, which may either valued, or computed, and
   * hence depending on yet other cells. A change to a cell's value is triggers
   * invalidates all cells that depend on it.
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   * @see Ingredient
   * @see Recipe */
  @SuppressWarnings("null")
  abstract class Cell<T> implements Supplier<T>, Cloneable {
    public static Set<Cell<?>> trace;
    /** The last value computed for this cell */
    T cache;
    /** other cells that depend on this cell */
    final List<Cell<?>> dependents = new ArrayList<>();
    long version;

    /** @return last value computed or set for this cell. */
    public final T cache() {
      return cache;
    }
    /** see @see java.util.function.Supplier#get() (auto-generated) */
    @Override public abstract T get();
    /** Used for fluent API, synonym of {@link Cell#set(Object)} . sets the
     * current value of this cell
     * @param ¢ JD
     * @return <code><b>this</b></code>* */
    public final Cell<T> of(final T ¢) {
      return set(¢);
    }
    /** sets the current value of this cell
     * @param ¢ JD
     * @return <code><b>this</b></code> */
    public final Cell<T> set(final T ¢) {
      cache(¢);
      uponForcedSet();
      version = oldestDependent() + 1;
      return this;
    }
    /** template function to be implemented by clients; normally an ingredient
     * is always updated and a dish is updated if all its ingredients are
     * updated, and the recipe was applied <i>after</i> all the ingredients
     * where updated.
     * @return <code><b>true</b></code> <i>iff</i> the contents of the cache
     *         stored in this node is updated. */
    public abstract boolean updated();
    @Override @SuppressWarnings("unchecked") protected Cell<T> clone() {
      try {
        return (Cell<T>) super.clone();
      } catch (final CloneNotSupportedException e) {
        return null;
      }
    }
    protected long version() {
      return version;
    }
    void cache(@SuppressWarnings("hiding") final T cache) {
      this.cache = cache;
    }
    /** by overriding this function, inheriting classes can ask to be notified
     * when this cell was set. */
    void uponForcedSet() {
      //
    }
    private long oldestDependent() {
      long $ = 0;
      for (final Cell<?> ¢ : dependents)
        $ = max($, ¢.version);
      return $;
    }
  }

  /** cell which does not depend on others
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  class Ingredient<T> extends Cell<T> {
    /** Instantiates this class.* */
    public Ingredient() {}
    /** instantiates this class
     * @param value JD */
    public Ingredient(final T value) {
      cache(value);
    }
    /** see @see il.org.spartan.lazy.Cookbook.Cell#get() (auto-generated) */
    @Override public T get() {
      idiomatic.run(() -> trace.add(this)).unless(trace == null);
      return cache();
    }
    @Override public final boolean updated() {
      return true;
    }

    /** cell which does not depend on others
     * @param <T> JD
     * @author Yossi Gil <Yossi.Gil@GMail.COM>
     * @since 2016 */
    public static class NonNull<T> extends Cookbook.Ingredient<T> {
      /** instantiates this class
       * @param value JD */
      public NonNull(final T value) {
        super(value);
      }
      @Override void cache(@SuppressWarnings("hiding") final T cache) {
        super.cache(cache);
        if (cache == null)
          throw new NullPointerException();
      }
    }
  }

  /** TODO(2016) Javadoc: automatically generated for type <code>Cookbook</code>
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  interface Internal {
    /** @return never! The <code><b>none</b></code> type. There is no legal
     *         value that this function can return, since the type <code></code>
     *         {@link Void} is empty. ( <code><b>null</b></code> is the single
     *         vale of {@link Void} , but it does not obey the {@link }
     *         annotation. */
    static Void shouldNeverBeCalled() {
      assert false;
      throw new RuntimeException();
    }

    interface $$Function<T, R> {
      Cell<R> from(Cell<T> ¢);
    }

    interface $$Function2<T1, T2, R> {
      Cell<R> from(Cell<T1> ¢1, Cell<T2> ¢2);
    }

    /** Fluent API */
    interface $$RecipeMaker {
      <X> Cell<X> make(Supplier<X> x);
    }

    @FunctionalInterface
    interface Function2<T1, T2, R> {
      R apply(T1 ¢1, T2 ¢2);
    }

    /** TODO(2016) Javadoc: automatically generated for type
     * <code>Cookbook</code>
     * @param <T1>
     * @param <T2>
     * @param <T3>
     * @param <R>
     * @author Yossi Gil <Yossi.Gil@GMail.COM>
     * @since 2016 */
    @FunctionalInterface
    interface Function3<T1, T2, T3, R> {
      R apply(T1 ¢1, T2 ¢2, T3 ¢3);
    }
  }

  /** A cell that may depend on others.
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  class Recipe<T> extends Cell<T> {
    private final List<Cell<?>> prerequisites = new ArrayList<>();
    private Supplier<? extends T> supplier;

    /** Instantiates this class.
     * @param supplier JD */
    public Recipe(final Supplier<? extends T> supplier) {
      this.supplier = supplier;
    }
    @Override public Cell<T> clone() {
      return super.clone();
    }
    @Override public T get() {
      if (updated())
        return cache();
      assert supplier != null;
      for (final Cell<?> ¢ : prerequisites)
        ¢.get();
      assert supplier != null;
      cache(filter(eval()));
      version = latestPrequisiteVersion() + 1;
      return cache();
    }
    /** Add another cell on which this instance depends
     * @param ¢ JD
     * @return <code><b>this</b></code> */
    public Recipe<T> ingredient(final Cell<?> ¢) {
      run(() -> ¢.dependents.add(this)).unless(¢.dependents.contains(this));
      run(() -> prerequisites.add(¢)).unless(prerequisites.contains(this));
      return this;
    }
    /** Add another cell on which this instance depends
     * @param cs JD
     * @return <code><b>this</b></code> */
    public Recipe<T> ingredients(final Cell<?>... cs) {
      for (final Cell<?> ¢ : cs)
        ingredient(¢);
      return this;
    }
    @Override public boolean updated() {
      if (supplier == null)
        return true;
      if (version() <= latestPrequisiteVersion())
        return false;
      for (final Cell<?> ¢ : prerequisites)
        if (!¢.updated())
          return false;
      return true;
    }
    T eval() {
      assert supplier != null;
      return supplier.get();
    }
    /** To be overridden by extending classes for e.g., null protection
     * @param $ result
     * @return ¢ */
    @SuppressWarnings("static-method") <N> N filter(final N ¢) {
      return ¢;
    }
    final long latestPrequisiteVersion() {
      long $ = 0;
      for (final Cell<?> ¢ : prerequisites)
        if ($ < ¢.version())
          $ = ¢.version();
      return $;
    }
    @Override void uponForcedSet() {
      supplier = null;
    }

    /** A cell that may depend on others.
     * @param <T> JD
     * @author Yossi Gil <Yossi.Gil@GMail.COM>
     * @since 2016 */
    @SuppressWarnings("null")
    public static class NonNull<T> extends Cookbook.Recipe<T> {
      private final List<Cookbook.Cell<?>> prerequisites = new ArrayList<>();
      private Supplier<? extends T> supplier;

      /** Instantiates this class.
       * @param supplier JD */
      public NonNull(final Supplier<? extends T> supplier) {
        super(cantBeNull(supplier));
        cache(cantBeNull(supplier).get());
      }
      @Override @SuppressWarnings({}) public NonNull<T> clone() {
        return (NonNull<T>) super.clone();
      }
      /** Add another cell on which this instance depends
       * @param ¢ JD
       * @return <code><b>this</b></code> */
      @Override public NonNull<T> ingredients(final Cookbook.Cell<?>... ¢) {
        return (NonNull<T>) super.ingredients(¢);
      }
      @Override public final boolean updated() {
        if (supplier == null)
          return true;
        for (final Cookbook.Cell<?> ¢ : prerequisites)
          if (!¢.updated() || version() < ¢.version())
            return false;
        return true;
      }
      @Override T eval() {
        assert supplier != null;
        return supplier.get();
      }
      @Override final <N> N filter(final N $) {
        return cantBeNull($);
      }
    }

    /** A cell that may depend on others.
     * @param <T> JD
     * @author Yossi Gil <Yossi.Gil@GMail.COM>
     * @since 2016 */
    public static class NullRobust<T> extends Cookbook.Recipe<T> {
      /** Instantiates this class.
       * @param supplier JD */
      public NullRobust(final Supplier<? extends T> supplier) {
        super(supplier);
        assert supplier != null;
      }
      @Override @SuppressWarnings({}) public Cookbook.Cell<T> clone() {
        return super.clone();
      }
      @Override public T get() {
        try {
          return super.get();
        } catch (final NullPointerException x) {
          return null;
        }
      }
      /** Add another cell on which this instance depends
       * @param ¢ JD
       * @return <code><b>this</b></code> */
      @Override public NullRobust<T> ingredients(final Cookbook.Cell<?>... ¢) {
        super.ingredients(¢);
        return this;
      }
      @Override void cache(@SuppressWarnings("hiding") final T cache) {
        try {
          super.cache(cache);
        } catch (final NullPointerException ¢) {
          ¢.printStackTrace();
        }
      }
      @Override T eval() {
        try {
          return super.eval();
        } catch (final NullPointerException x) {
          return null;
        }
      }
    }
  }
}