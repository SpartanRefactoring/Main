/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan.lazy;

import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static il.org.spartan.idiomatic.*;
import static java.lang.Math.*;
import il.org.spartan.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.annotation.*;
import org.junit.*;
import org.junit.runners.*;

/**
 * A lazy spreadsheet, with a DAG of interdependent cells; a cell's value is
 * only evaluated when it is accessed, and only when it out of date with respect
 * to the cells it depends on.
 *
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016
 */
public interface Cookbook {
  /**
   * A repository of test cases and examples
   *
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  public static enum _Examples {
    ;
    /**
     * Should not be used by clients. A development time only
     * <code><b>class</b></code> used for testing and as a documented demo of
     * using {@link Cookbook}.
     * <p>
     * All cells here are of type <code>@NonNull</code {@link Integer}. The
     * {@Valued} cells in this example are:
     * <ol>
     * <li>{@link #a()}
     * <li>{@link #b()}
     * <li>{@link #c()}
     * </ol>
     * <p>
     * The {@link Recipe} cells in the example are :
     * <ol>
     * <li>{@link #aPower02()},
     * <li>{@link #aPower03()},
     * <li>{@link #aPower05()}, and
     * <li>{@link #aPower17NullSafe()}.
     * </ol>
     * <p>
     * <i>the dollars in the name are magic against alphabetical method sorters
     * that cannot distinguish code from meta-code</i>
     *
     * @author Yossi Gil <Yossi.Gil@GMail.COM>
     * @since 2016
     */
    @SuppressWarnings({ "boxing", "unused" })//
    public static class Diophantine implements Cookbook {
      /** @return contents of cell a */
      public final @Nullable Integer a() {
        return a.get();
      }
      /** @return contents of cell a^2 := (a)^2 */
      public final @Nullable Integer aPower02() {
        return aPower02.get();
      }
      /** @return contents of cell a^3 := a^2 * a */
      public final @Nullable Integer aPower03() {
        return aPower03.get();
      }
      /** @return contents of cell a^5 := a^2 * a^3 */
      public final @Nullable Integer aPower05() {
        return aPower05.get();
      }
      /** @return contents of cell a^17 := (a)^4 * (a^2)^4 * (a^3)^3 */
      public final @Nullable Integer aPower17NullSafe() {
        return aPower17NullSafe.get();
      }
      /** @return contents of valued cell <code>b</code> */
      public final @Nullable Integer b() {
        return b.get();
      }
      /** @return contents of valued cell <code>c</code> */
      public final @Nullable Integer c() {
        return c.get();
      }
      /** @return contents of cell d := a + b + c */
      public final @Nullable Integer d() {
        return d.get();
      }

      /** Must not be private; used for testing of proper lazy evaluation */
      int _aPower02Calls;
      /** Must not be private; used for testing of proper lazy evaluation */
      int _aPower03Calls;
      /** a^5 := a^2 * a^3 */
      /** Can, and often should be made private; package is OK */
      final Edible<@Nullable Integer> a = new Cookbook.Ingredient<@Nullable Integer>();
      /** Can, and often should be made private; package is OK */
      final Edible<@Nullable Integer> aPower02 = new Recipe<@Nullable Integer>(() -> {
        ++_aPower02Calls;
        return a() * a();
      }).ingredients(a);
      /** Can, and often should be made private; package is OK */
      final Edible<@Nullable Integer> aPower03 = new Recipe<@Nullable Integer>(() -> {
        ++_aPower03Calls;
        return a() * aPower02();
      }).ingredients(aPower02, a);
      /** the actual cell behind {@link #aPower05()} */
      final Edible<@Nullable Integer> aPower05 = new Recipe<@Nullable Integer>(//
          () -> aPower02() * aPower03()).ingredients(aPower02, aPower03);
      /** Can, and often should be made private; package is OK */
      /** the actual cell behind {@link #b()} */
      final Edible<@Nullable Integer> aPower17NullSafe = new Recipe.NullRobust<@Nullable Integer>(//
          () -> {
            try {
              return a() * a() * a() * a() * aPower02() * aPower02() * aPower03() * aPower03() * aPower03();
            } catch (final NullPointerException x) {
              return null;
            }
          }).ingredients(a, aPower02, aPower03);
      /** the actual cell behind {@link #b()} */
      final Edible<@Nullable Integer> b = new Cookbook.Ingredient<@Nullable Integer>(3);
      /** the actual cell behind {@link #c()} */
      final Edible<@Nullable Integer> c = new Cookbook.Ingredient<@Nullable Integer>(5);
      /** the actual cell behind {@link #d()} */
      final Edible<@Nullable Integer> d = new Recipe<@Nullable Integer>(() -> a() + b() + c()).ingredients(a, b, c);

      @FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "null", "javadoc" })//
      public static class TEST extends Diophantine {
        @Test public void seriesA05() {
          a.of(2);
          azzert.notNull(a());
        }
        @Test public void seriesA06() {
          a.of(2);
          azzert.notNull(aPower02());
          azzert.that(aPower02(), is(4));
        }
        @Test public void seriesA07() {
          a.of(2);
          azzert.notNull(aPower03());
          azzert.that(aPower03(), is(8));
        }
        @Test public void seriesA08() {
          a.of(2);
          azzert.notNull(aPower02());
        }
        @Test public void seriesA09() {
          a.of(null);
          azzert.isNull(a());
        }
        @Test public void seriesA1() {
          azzert.isNull(a());
        }
        @Test(expected = NullPointerException.class) public void seriesA10() {
          a.of(null);
          aPower02();
        }
        @Test(expected = NullPointerException.class) public void seriesA11() {
          a.of(null);
          aPower03();
        }
        @Test(expected = NullPointerException.class) public void seriesA12() {
          a.of(null);
          aPower02();
        }
        @Test public void seriesA13() {
          a.of(null);
          azzert.isNull(aPower17NullSafe());
          a.of(2);
          azzert.notNull(aPower17NullSafe());
          azzert.that(a(), is(2));
        }
        @Test public void seriesA14() {
          a.of(2);
          azzert.notNull(aPower17NullSafe());
          azzert.that(a(), is(2));
          azzert.that(aPower17NullSafe(), is(1 << 17));
        }
        @Test public void seriesA15() {
          a.of(null);
          azzert.isNull(aPower17NullSafe());
        }
        @Test public void seriesA16() {
          a.of(null);
          azzert.isNull(aPower17NullSafe.get());
        }
        @Test public void seriesA17() {
          a.of(null);
          final Recipe<?> x = (Recipe<?>) aPower17NullSafe;
          azzert.isNull(x.get());
        }
        @Test public void seriesA18() {
          a.of(null);
          final Recipe<?> x = (Recipe<?>) aPower17NullSafe;
        }
        @Test public void seriesA19() {
          a.of(null);
          aPower17NullSafe.get();
        }
        @Test(expected = NullPointerException.class) public void seriesA2() {
          aPower02().getClass();
        }
        @Test public void seriesA20() {
          aPower17NullSafe.get();
        }
        @Test(expected = NullPointerException.class) public void seriesA3() {
          aPower03().getClass();
        }
        @Test(expected = NullPointerException.class) public void seriesA4() {
          aPower05().getClass();
        }
        @Test(expected = NullPointerException.class) public void seriesA5() {
          a().toString().getClass();
        }
        @Test public void seriesB01() {
          a.of(2);
          azzert.notNull(a());
          azzert.that(a(), is(2));
          a.of(3);
          azzert.that(a(), is(3));
          a.of(4);
          azzert.that(a(), is(4));
          a.of(null);
          azzert.isNull(a());
          a.of(5);
          azzert.that(a(), is(5));
        }
        @Test public void seriesB02() {
          a.of(2);
          azzert.notNull(aPower02());
          azzert.that(aPower02(), is(4));
          a.of(3);
          azzert.notNull(aPower02());
          azzert.that(aPower02(), is(9));
        }
        @Test public void seriesB03() {
          a.of(2);
          azzert.notNull(aPower03());
          azzert.that(aPower03(), is(8));
          a.of(3);
          azzert.notNull(aPower03());
          azzert.that(aPower03(), is(27));
        }
        @Test public void seriesB04() {
          a.of(2);
          azzert.notNull(aPower02());
        }
        @Test public void seriesC00() {
          a.of(-3);
          azzert.that(_aPower03Calls, is(0));
          azzert.that(_aPower02Calls, is(0));
        }
        @Test public void seriesC01() {
          a.of(-3);
          azzert.that(aPower03(), is(-27));
          azzert.that(_aPower03Calls, is(1)); // Force invocation
          azzert.that(_aPower02Calls, is(1));
        }
        @Test public void seriesC02() {
          azzert.that(a.version(), is(0L));
          azzert.that(aPower17NullSafe.version(), is(0L));
        }
        @Test public void seriesC03() {
          azzert.that(aPower02.version(), is(0L));
          azzert.that(aPower03.version(), is(0L));
        }
        @Test public void seriesC04() {
          a.of(-2);
          azzert.that(a.version(), is(1L));
          azzert.that(aPower03.version(), is(0L));
          azzert.that(_aPower03Calls, is(0));
          azzert.that(aPower17NullSafe(), is(-(1 << 17))); // Force invocation
          azzert.that(_aPower02Calls, is(1));
          azzert.that(_aPower03Calls, is(1));
        }
        @Test public void seriesC05() {
          a.of(-2);
          azzert.that(aPower17NullSafe(), is(-(1 << 17))); // Force invocation
          azzert.that(_aPower02Calls, is(1));
          azzert.that(_aPower03Calls, is(1));
        }
        @Test public void seriesD01() {
          azzert.that(a.version, is(0L));
          azzert.that(aPower02.version, is(0L));
          azzert.that(aPower03.version, is(0L));
          azzert.that(aPower17NullSafe.version, is(0L));
        }
        @Test public void seriesD02() {
          azzert.that(a.version, is(0L));
          a.of(1);
          azzert.that(a.version, is(1L));
          azzert.that(aPower02.version, is(0L));
          azzert.that(aPower03.version, is(0L));
          azzert.that(aPower17NullSafe.version, is(0L));
        }
        @Test public void seriesD03() {
          a.of(14);
          azzert.that(aPower02.version, is(0L));
          azzert.that(aPower02.get(), is(196)); // Force evaluation
          azzert.that(aPower03.version, is(0L));
          azzert.that(aPower02.version, is(2L));
          azzert.that(aPower17NullSafe.version, is(0L));
        }
        @Test public void seriesD04() {
          a.of(14);
          azzert.notNull(a.get());
        }
        @Test public void seriesD05() {
          a.of(14);
          azzert.notNull(a.get());
          azzert.that(a.get(), is(14));
          azzert.that(aPower02.get(), is(196)); // Sanity check
        }
        @Test public void seriesD06() {
          a.of(14);
          azzert.notNull(a.get());
          a.get(); // Force evaluation
          azzert.that(aPower02.version(), is(0L));
          a.get(); // Force evaluation
          azzert.that(aPower02.version(), is(0L));
        }
        @Test public void seriesD07() {
          a.of(14);
          azzert.notNull(a.get());
          a.get(); // Force evaluation
          azzert.not(aPower02.updated());
        }
        @Test public void seriesD08() {
          a.of(14);
          azzert.that(a.get(), is(14)); // Force evaluation
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version, is(0L));
          azzert.that(((Recipe<Integer>) aPower02).latestPrequisiteVersion(), is(1L));
        }
        @Test public void seriesD09() {
          a.of(14);
          azzert.that(a.get(), is(14)); // Force evaluation
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version, is(0L));
          azzert.notNull(a.dependents);
        }
        @Test public void seriesD10() {
          a.of(14);
          azzert.that(a.get(), is(14)); // Force evaluation
          azzert.that(a.version(), is(1L));
          azzert.that(aPower02.version, is(0L));
          azzert.that(a.dependents.size(), is(4)); // d, aPower2, aPower3,
          // aPowe17
          azzert.assertTrue("", a.dependents.contains(aPower02));
          azzert.falze(a.dependents.contains(null));
        }
        @Test public void seriesD11() {
          a.of(14);
          azzert.that(a.get(), is(14)); // Force evaluation
          azzert.that(a.version(), is(1L));
        }
        @Test public void seriesD12() {
          assertTrue(a.dependents.contains(aPower02));
        }
        @Test public void seriesD13() {
          assertTrue(a.dependents.contains(aPower03));
        }
        @Test public void seriesD14() {
          assertFalse(a.dependents.contains(aPower05));
        }
        @Test public void seriesD15() {
          assertTrue(a.dependents.contains(aPower17NullSafe));
        }
        @Test public void seriesD16() {
          a.of(2);
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(_aPower03Calls, is(1));
        }
        @Test public void seriesD17() {
          a.of(2);
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(_aPower02Calls, is(1));
          a.of(3);
          a.of(2);
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(aPower17NullSafe(), is(1 << 17));
          azzert.that(_aPower02Calls, is(2));
          azzert.that(_aPower03Calls, is(2));
        }
        @Test public void seriesE01() {
          azzert.that(a.version(), is(0L));
          azzert.that(aPower02.version(), is(0L));
          azzert.that(aPower03.version(), is(0L));
          azzert.that(aPower05.version(), is(0L));
          azzert.that(aPower17NullSafe.version(), is(0L));
          a.of(2);
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
          a.of(3);
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
        @Test public void seriesE02() {
          azzert.that(a.version(), is(0L));
          azzert.that(aPower02.version(), is(0L));
          azzert.that(aPower03.version(), is(0L));
          azzert.that(aPower05.version(), is(0L));
          azzert.that(aPower17NullSafe.version(), is(0L));
        }
        @Test public void seriesE03() {
          a.of(2);
          b.of(3);
          c.of(4);
          azzert.that(d.get(), is(9));
        }
        @Test public void seriesE04() {
          a.of(2);
          aPower02.of(3);
          aPower03.of(5);
          azzert.that(aPower05.get(), is(15));
        }
        @Test public void seriesE05() {
          a.of(2);
          azzert.that(aPower05.get(), is(1 << 5));
          azzert.that(aPower17NullSafe.version(), is(0L));
          azzert.that(aPower02.version(), is(2L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(4L));
        }
        @Test public void seriesE06() {
          a.of(2);
          assertFalse("aPower5 should not be updated! (recursive dependency on a)", aPower05.updated());
        }
        @Test public void seriesF01() {
          a.of(11);
          assertFalse(aPower02.updated());
          azzert.that(aPower02.get(), is(121));
          assertTrue(aPower02.updated());
          aPower02.of(0xDADA);
          assertTrue(aPower02.updated());
          azzert.that(aPower02.get(), is(0xDADA));
          a.of(0xCAFE);
          assertTrue(aPower02.updated());
          azzert.that(aPower02.get(), is(0xDADA));
        }
        @Test public void seriesF02() {
          a.of(null);
          azzert.isNull(aPower17NullSafe());
          azzert.that(aPower05.version(), is(0L));
        }
        @Test public void seriesF03() {
          a.of(2);
          azzert.that(aPower05.get(), is(1 << 5));
          azzert.that(aPower17NullSafe.version(), is(0L));
          azzert.that(aPower02.version(), is(2L));
          azzert.that(aPower03.version(), is(3L));
          azzert.that(aPower05.version(), is(4L));
        }
        @Test public void seriesF04() {
          a.of(null);
          azzert.isNull(aPower17NullSafe());
        }
        @Test public void seriesF05() {
          azzert.isNull(aPower17NullSafe());
        }

        @Test public void seriesG01() {
          aPower02.of(0xDADA);
          a.of(0xCAFE);
          assertTrue(aPower02.updated());
        }
        @SuppressWarnings("synthetic-access") @Test public void seriesG02() {
          aPower02.of(0xDADA);
          azzert.notNull(aPower02.cache);
          azzert.that(aPower02.cache, is(0xDADA));
          azzert.isNull(((Recipe<?>) aPower02).supplier);
        }
      }
    }
  }

  /**
   * A cell stores a value of some type (which is passed by parameter). A cell
   * may be either {@link Ingredient} or {@link Recipe}. A computed cell typically
   * depends on other cells, which may either valued, or computed, and hence
   * depending on yet other cells. A change to a cell's value is triggers
   * reevalution of all cells that depend on it.
   *
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   * @see Ingredient
   * @see Recipe
   */
  @SuppressWarnings("null") public abstract class Edible<T> implements Supplier<T>, Cloneable {
    /** @return the last value computed or set for this cell. */
    public final T cache() {
      return cache;
    }

    /** see @see java.util.function.Supplier#get() (auto-generated) */
    @Override public @Nullable T get() {
      return cache();
    }
    /**
     * sets the current value of this cell
     *
     * @param t JD
     * @return <code><b>this</b></code>
     */
    public final Edible<T> of(final T t) {
      cache(t);
      uponForcedSet();
      version = oldestDependent() + 1; // Invalidate all dependents
      return this;
    }
    /**
     * template function to be implemented by clients; normally an ingredient is
     * always updated and a dish is updated if all its ingredients are updated,
     * and the recipe was applied <i>after</i> all the ingredients where
     * updated.
     *
     * @return <code><b>true</b></code> <i>iff</i> the contents of the cache
     *         stored in this node is updated.
     */
    public abstract boolean updated();
    private final long oldestDependent() {
      long $ = 0;
      for (final Edible<?> c : dependents)
        $ = max($, c.version);
      return $;
    }
    void cache(@SuppressWarnings("hiding") final T cache) {
      this.cache = cache;
    }
    /**
     * by overriding this function, inheriting classes can ask to be notified
     * when this cell was set.
     */
    void uponForcedSet() {
      // empty by default
    }
    protected long version() {
      return version;
    }

    /** The last value computed for this cell */
    @Nullable T cache;
    /** other cells that depend on this cell */
    final List<Edible<?>> dependents = new ArrayList<>();
    long version = 0;
  }

  /**
   * cell which does not depend on others
   *
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  public class Ingredient<@Nullable T> extends Edible<T> {
    /** Instantiates this class.* */
    public Ingredient() {
      // Make sure we have a public constructor
    }
    /**
     * instantiates this class
     *
     * @param value JD
     */
    public Ingredient(final T value) {
      cache(value);
    }
    @Override public boolean updated() {
      return true;
    }
    /**
     * cell which does not depend on others
     *
     * @param <T> JD
     * @author Yossi Gil <Yossi.Gil@GMail.COM>
     * @since 2016
     */
    public static class NotNull<T> extends Cookbook.Edible<T> {
      /**
       * instantiates this class
       *
       * @param value JD
       */
      public NotNull(final T value) {
        cache(value);
      }
      /**
       * prevent clients from instantiating this this class; this consructor
       * should never be called
       */
      @SuppressWarnings("unused") private NotNull() {
        assert false;
      }
      @Override public boolean updated() {
        return true;
      }
    }
  }

  /**
   * A cell that may depend on others.
   *
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  public static class Recipe<@Nullable T> extends Edible<T> {
    /**
     * Instantiates this class.
     *
     * @param supplier JD
     */
    public Recipe(final Supplier<? extends T> supplier) {
      this.supplier = supplier;
    }
    @SuppressWarnings({ "unchecked" }) @Override public Edible<T> clone() throws CloneNotSupportedException {
      return (Edible<T>) super.clone();
    }
    /**
     * Add another cell on which this instance depends
     *
     * @param es JD
     * @return <code><b>this</b></code>
     */
    public Recipe<T> ingredients(final Edible<?>... es) {
      for (final Edible<?> e : es)
        ingredient(e);
      return this;
    }
    /**
     * Add another cell on which this instance depends
     *
     * @param e JD
     * @return <code><b>this</b></code>
     */
    public Recipe<T> ingredient(final Edible<?> e) {
      $(() -> {
        e.dependents.add(this);
      }).unless(e.dependents.contains(this));
      $(() -> {
        prerequisites.add(e);
      }).unless(prerequisites.contains(this));
      return this;
    }
    @Override public T get() {
      if (updated())
        return cache();
      assert supplier != null;
      for (final Edible<?> c : prerequisites)
        c.get();
      assert supplier != null;
      cache(filter(eval()));
      version = latestPrequisiteVersion() + 1;
      return cache();
    }
    @Override public boolean updated() {
      if (supplier == null)
        return true;
      for (final Edible<?> c : prerequisites)
        if (!c.updated() || version() < c.version())
          return false;
      return true;
    }
    @Nullable T eval() {
      assert supplier != null;
      return supplier.get();
    }
    /**
     * To be overridden by extending classes for e.g., null protection
     *
     * @param $ result
     * @return the parameter
     */
    @SuppressWarnings("static-method") <N> N filter(final N n) {
      return n;
    }
    final long latestPrequisiteVersion() {
      long $ = 0;
      for (final Edible<?> c : prerequisites)
        if ($ < c.version())
          $ = c.version();
      return $;
    }
    @Override void uponForcedSet() {
      supplier = null;
    }

    private final List<Edible<?>> prerequisites = new ArrayList<>();
    private @Nullable Supplier<? extends @Nullable T> supplier;
    /**
     * A cell that may depend on others.
     *
     * @param <T> JD
     * @author Yossi Gil <Yossi.Gil@GMail.COM>
     * @since 2016
     */
    @SuppressWarnings("null") public static class NotNull<T> extends Cookbook.Recipe<T> {
      /**
       * Instantiates this class.
       *
       * @param supplier JD
       */
      public NotNull(final Supplier<? extends @NonNull T> supplier) {
        super(cantBeNull(supplier));
        cache(cantBeNull(supplier).get());
      }
      @SuppressWarnings({}) @Override public NotNull<T> clone() throws CloneNotSupportedException {
        return (NotNull<T>) super.clone();
      }
      /**
       * Add another cell on which this instance depends
       *
       * @param cs JD
       * @return <code><b>this</b></code>
       */
      @Override public NotNull<T> ingredients(final Cookbook.Edible<?>... cs) {
        return (NotNull<T>) super.ingredients(cs);
      }
      @Override public final boolean updated() {
        if (supplier == null)
          return true;
        for (final Cookbook.Edible<?> c : prerequisites)
          if (!c.updated() || version() < c.version())
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

      private final List<Cookbook.Edible<?>> prerequisites = new ArrayList<>();
      private @Nullable Supplier<? extends @Nullable T> supplier;
    }
    /**
     * A cell that may depend on others.
     *
     * @param <T> JD
     * @author Yossi Gil <Yossi.Gil@GMail.COM>
     * @since 2016
     */
    public static class NullRobust<@Nullable T> extends Cookbook.Recipe<T> {
      /**
       * Instantiates this class.
       *
       * @param supplier JD
       */
      public NullRobust(final Supplier<? extends T> supplier) {
        super(supplier);
        assert supplier != null;
      }
      @SuppressWarnings({}) @Override public Cookbook.Edible<T> clone() throws CloneNotSupportedException {
        return super.clone();
      }
      /**
       * Add another cell on which this instance depends
       *
       * @param cs JD
       * @return <code><b>this</b></code>
       */
      @Override public NullRobust<T> ingredients(final Cookbook.Edible<?>... cs) {
        super.ingredients(cs);
        return this;
      }
      @Override public T get() {
        try {
          return super.get();
        } catch (final NullPointerException x) {
          return null;
        }
      }
      @Override void cache(@SuppressWarnings("hiding") @Nullable final T cache) {
        try {
          super.cache(cache);
        } catch (final NullPointerException x) {
          x.printStackTrace();
        }
      }
      @Override @Nullable T eval() {
        try {
          return super.eval();
        } catch (final NullPointerException x) {
          return null;
        }
      }
    }
  }
}