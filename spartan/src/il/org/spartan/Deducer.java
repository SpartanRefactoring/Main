/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.azzert.*;
import static il.org.spartan.idiomatic.*;
import static java.lang.Math.*;

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
public interface Deducer {
  /**
   * A cell stores a value of some type (which is passed by parameter). A cell
   * may be either {@link Valued} or {@link Computed}. A computed cell typically
   * depends on other cells, which may either valued, or computed, and hence
   * depending on yet other cells. A change to a cell's value is triggers
   * recomputation of all cells that depend on it.
   *
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   * @see Valued
   * @see Computed
   */
  public abstract class Cell<@Nullable T> implements Supplier<T>, Cloneable {
    /**
     * Instantiates this class.
     *
     * @param value JD
     */
    Cell(final @Nullable T value) {
      this.value = value;
    }
    /**
     * sets the current value of this cell
     *
     * @param value JD
     */
    public void set(final T value) {
      this.value = value;
      this.version = oldestDependent() + 1; // Invalidate all dependents
    }
    private long oldestDependent() {
      long $ = 0;
      for (final Cell<?> c : dependents)
        $ = max($, c.version);
      return $;
    }
    abstract boolean updated();
    long version() {
      return version;
    }

    /** other instances that depend on this instance */
    final List<Cell<?>> dependents = new ArrayList<>();
    T value;
    long version = 0;
  }
  /**
   * A value which may depend on others and others may depend on
   *
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  public class Computed<@Nullable T> extends Cell<T> {
    /**
     * Instantiates this class.
     *
     * @param supplier JD
     */
    public Computed(final Supplier<? extends T> supplier) {
      super(null);
      this.supplier = supplier;
    }
    @SuppressWarnings({ "unchecked", "null" }) @Override public Cell<T> clone() throws CloneNotSupportedException {
      return (Cell<T>) super.clone();
    }
    /**
     * Add another cell on which this instance depends
     *
     * @param cs JD
     * @return <code><b>this</b></code>
     */
    public Cell<T> dependsOn(final Cell<?>... cs) {
      for (final Cell<?> c : cs) {
        $(() -> {
          c.dependents.add(this);
        }).unless(c.dependents.contains(this));
        $(() -> {
          prerequisites.add(c);
        }).unless(prerequisites.contains(this));
      }
      return this;
    }
    @Override public T get() {
      if (updated() || manuallySet())
        return value;
      for (final Cell<?> c : prerequisites)
        c.get();
      value = eval();
      version = latestPrequisiteVersion() + 1;
      return value;
    }
    @Override public void set(final T value) {
      super.set(value);
      supplier = null;
    }
    private @Nullable T eval() {
      assert supplier != null;
      azzert.notNull(supplier);
      assert supplier != null;
      return supplier.get();
    }
    /**
     * TODO Javadoc(2016): automatically generated for method
     * <code>manuallySet</code>
     *
     * @return boolean TODO Javadoc(2016) automatically generated for returned
     *         value of method <code>manuallySet</code>
     */
    private boolean manuallySet() {
      return supplier == null;
    }
    /**
     * TODO Javadoc(2016): automatically generated for method
     * <code>latestPrequisiteVersion</code>
     *
     * @return Object TODO Javadoc(2016) automatically generated for returned
     *         value of method <code>latestPrequisiteVersion</code>
     */
    long latestPrequisiteVersion() {
      long $ = 0;
      for (final Cell<?> c : prerequisites)
        if ($ < c.version())
          $ = c.version();
      return $;
    }
    @Override boolean updated() {
      if (manuallySet())
        return true;
      for (final Cell<?> c : prerequisites)
        if (!c.updated() || version() < c.version())
          return false;
      return true;
    }

    private final List<Cell<?>> prerequisites = new ArrayList<>();
    private @Nullable Supplier<? extends @Nullable T> supplier;
  }

  /**
   * An example deducer, in which all cells are of type <code>@NonNull</code
   * {@link Integer}. The root cells are:
   * <ol>
   * <li>{@link #a()}
   * <li>{@link #b()}
   * <li>{@link #c()}
   * </ol>
   * <p>
   * The {@link Computed} cells in the example are :
   * <ol>
   * <li>{@link #aPower02()}, `
   * <li>{@link #aPower03()}, `
   * <li>{@link #aPower05()},`and
   * <li>{@link #aPower17NullSafe()}.`
   * </ol>
   *
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  @SuppressWarnings({ "boxing", "null", "unused" }) //
  public static class Example implements Deducer {
    /** @return contents of cell a */
    public final @Nullable Integer a() {
      return a.get();
    }
    /** @return contents of cell a^2 := (a)^2 */
    public final @Nullable Integer aPower02() {
      return aPower02.get();
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
    /** Must not be private; used for testing of proper lazy evaluation */
    int _aPower02Calls;
    /** Must not be private; used for testing of proper lazy evaluation */
    int _aPower03Calls;
    /** a^5 := a^2 * a^3 */
    /** Can, and often should be made private; package is OK */
    final Cell<@Nullable Integer> a = new Valued<@Nullable Integer>();
    /** Can, and often should be made private; package is OK */
    final Cell<@Nullable Integer> aPower02 = new Computed<@Nullable Integer>(() -> {
      ++_aPower02Calls;
      return a() * a();
    }).dependsOn(a);
    /** Can, and often should be made private; package is OK */
    final Cell<@Nullable Integer> aPower03 = new Computed<@Nullable Integer>(() -> {
      ++_aPower03Calls;
      return a() * aPower02();
    }).dependsOn(aPower02, a);
    /** the actual cell behind {@link #aPower05()} */
    final Cell<@Nullable Integer> aPower05 = new Computed<@Nullable Integer>(//
        () -> aPower02() * aPower03()).dependsOn(aPower02, aPower03);
    /** Can, and often should be made private; package is OK */
    /** the actual cell behind {@link #b()} */
    final Cell<@Nullable Integer> aPower17NullSafe = new Computed<@Nullable Integer>(//
        () -> {
          int i = 1;
          if (a() == null)
            return null;
          System.err.println(++i);
          aPower02();
          System.err.println(++i);
          aPower03();
          System.err.println(++i);
          final Integer v = a() * a() * a() * a();
          System.err.println(++i);
          final Integer v2 = aPower02() * aPower02();
          System.err.println(++i);
          final Integer v3 = aPower03() * aPower03() * aPower03();
          System.err.println(++i);
          return v * v2 * v3;
        }).dependsOn(a, aPower02, aPower03);
    /** the actual cell behind {@link #b()} */
    final Cell<@Nullable Integer> b = new Valued<@Nullable Integer>(3);
    /** the actual cell behind {@link #c()} */
    final Cell<@Nullable Integer> c = new Valued<@Nullable Integer>(5);
    /** the actual cell behind {@link #d()} */
    final Cell<@Nullable Integer> d = new Computed<@Nullable Integer>(() -> a() + b.get() + c.get()).dependsOn(a, b, c);
    @FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "javadoc" }) //
    public static class TEST extends Example {
      @Test public void seriesA05() {
        a.set(2);
        azzert.notNull(a());
      }
      @Test public void seriesA06() {
        a.set(2);
        azzert.notNull(aPower02());
        azzert.that(aPower02(), is(4));
      }
      @Test public void seriesA07() {
        a.set(2);
        azzert.notNull(aPower03());
        azzert.that(aPower03(), is(8));
      }
      @Test public void seriesA08() {
        a.set(2);
        azzert.notNull(aPower02());
      }
      @Test public void seriesA09() {
        a.set(null);
        azzert.isNull(a());
      }
      @Test public void seriesA1() {
        azzert.isNull(a());
      }
      @Test(expected = NullPointerException.class) public void seriesA10() {
        a.set(null);
        aPower02();
      }
      @Test(expected = NullPointerException.class) public void seriesA11() {
        a.set(null);
        aPower03();
      }
      @Test(expected = NullPointerException.class) public void seriesA12() {
        a.set(null);
        aPower02();
      }
      @Test public void seriesA13() {
        a.set(null);
        azzert.isNull(aPower17NullSafe());
        a.set(2);
        azzert.notNull(aPower17NullSafe());
        azzert.that(a(), is(2));
      }
      @Test public void seriesA14() {
        a.set(2);
        azzert.notNull(aPower17NullSafe());
        azzert.that(a(), is(2));
        azzert.that(aPower17NullSafe(), is(1 << 17));
      }
      @Test public void seriesA15() {
        a.set(null);
        azzert.isNull(aPower17NullSafe());
      }
      @Test public void seriesA16() {
        a.set(null);
        azzert.isNull(aPower17NullSafe.get());
      }
      @Test public void seriesA17() {
        a.set(null);
        final Computed<?> x = (Computed<?>) aPower17NullSafe;
        azzert.isNull(x.get());
      }
      @Test public void seriesA18() {
        a.set(null);
        final Computed<?> x = (Computed<?>) aPower17NullSafe;
      }
      @Test public void seriesA19() {
        a.set(null);
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
        a.set(2);
        azzert.notNull(a());
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
      @Test public void seriesB02() {
        a.set(2);
        azzert.notNull(aPower02());
        azzert.that(aPower02(), is(4));
        a.set(3);
        azzert.notNull(aPower02());
        azzert.that(aPower02(), is(9));
      }
      @Test public void seriesB03() {
        a.set(2);
        azzert.notNull(aPower03());
        azzert.that(aPower03(), is(8));
        a.set(3);
        azzert.notNull(aPower03());
        azzert.that(aPower03(), is(27));
      }
      @Test public void seriesB04() {
        a.set(2);
        azzert.notNull(aPower02());
      }
      @Test public void seriesC00() {
        a.set(-3);
        azzert.that(_aPower03Calls, is(0));
        azzert.that(_aPower02Calls, is(0));
      }
      @Test public void seriesC01() {
        a.set(-3);
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
        a.set(-2);
        azzert.that(a.version(), is(1L));
        azzert.that(aPower03.version(), is(0L));
        azzert.that(_aPower03Calls, is(0));
        azzert.that(aPower17NullSafe(), is(-(1 << 17))); // Force invocation
        azzert.that(_aPower02Calls, is(1));
        azzert.that(_aPower03Calls, is(1));
      }
      @Test public void seriesC05() {
        a.set(-2);
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
        a.set(1);
        azzert.that(a.version, is(1L));
        azzert.that(aPower02.version, is(0L));
        azzert.that(aPower03.version, is(0L));
        azzert.that(aPower17NullSafe.version, is(0L));
      }
      @Test public void seriesD03() {
        a.set(14);
        azzert.that(aPower02.version, is(0L));
        azzert.that(aPower02.get(), is(196)); // Force evaluation
        azzert.that(aPower03.version, is(0L));
        azzert.that(aPower02.version, is(2L));
        azzert.that(aPower17NullSafe.version, is(0L));
      }
      @Test public void seriesD04() {
        a.set(14);
        azzert.notNull(a.get());
      }
      @Test public void seriesD05() {
        a.set(14);
        azzert.notNull(a.get());
        azzert.that(a.get(), is(14));
        azzert.that(aPower02.get(), is(196)); // Sanity check
      }
      @Test public void seriesD06() {
        a.set(14);
        azzert.notNull(a.get());
        a.get(); // Force evaluation
        azzert.that(aPower02.version(), is(0L));
        a.get(); // Force evaluation
        azzert.that(aPower02.version(), is(0L));
      }
      @Test public void seriesD07() {
        a.set(14);
        azzert.notNull(a.get());
        a.get(); // Force evaluation
        azzert.not(aPower02.updated());
      }
      @Test public void seriesD08() {
        a.set(14);
        azzert.that(a.get(), is(14)); // Force evaluation
        azzert.that(a.version(), is(1L));
        azzert.that(aPower02.version, is(0L));
        azzert.that(((Computed<Integer>) aPower02).latestPrequisiteVersion(), is(1L));
      }
      @Test public void seriesD09() {
        a.set(14);
        azzert.that(a.get(), is(14)); // Force evaluation
        azzert.that(a.version(), is(1L));
        azzert.that(aPower02.version, is(0L));
        azzert.notNull(a.dependents);
      }
      @Test public void seriesD10() {
        a.set(14);
        azzert.that(a.get(), is(14)); // Force evaluation
        azzert.that(a.version(), is(1L));
        azzert.that(aPower02.version, is(0L));
        azzert.that(a.dependents.size(), is(4)); // d, aPower2, aPower3, aPowe17
        azzert.assertTrue("", a.dependents.contains(aPower02));
        azzert.falze(a.dependents.contains(null));
      }
      @Test public void seriesD11() {
        a.set(14);
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
        a.set(2);
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
        a.set(2);
        azzert.that(aPower17NullSafe(), is(1 << 17));
        azzert.that(aPower17NullSafe(), is(1 << 17));
        azzert.that(_aPower02Calls, is(1));
        a.set(3);
        a.set(2);
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
      @Test public void seriesE02() {
        azzert.that(a.version(), is(0L));
        azzert.that(aPower02.version(), is(0L));
        azzert.that(aPower03.version(), is(0L));
        azzert.that(aPower05.version(), is(0L));
        azzert.that(aPower17NullSafe.version(), is(0L));
      }
      @Test public void seriesE03() {
        a.set(2);
        b.set(3);
        c.set(4);
        azzert.that(d.get(), is(9));
      }
      @Test public void seriesE04() {
        a.set(2);
        aPower02.set(3);
        aPower03.set(5);
        azzert.that(aPower05.get(), is(15));
      }
      @Test public void seriesE05() {
        a.set(2);
        azzert.that(aPower05.get(), is(1 << 5));
        azzert.that(aPower17NullSafe.version(), is(0L));
        azzert.that(aPower02.version(), is(2L));
        azzert.that(aPower03.version(), is(3L));
        azzert.that(aPower05.version(), is(4L));
      }
      @Test public void seriesE06() {
        a.set(2);
        assertFalse("aPower5 should not be updated! (recursive dependency on a)", aPower05.updated());
      }
      @Test public void seriesF01() {
        a.set(11);
        assertFalse(aPower02.updated());
        azzert.that(aPower02.get(), is(121));
        assertTrue(aPower02.updated());
        aPower02.set(0xDADA);
        assertTrue(aPower02.updated());
        azzert.that(aPower02.get(), is(0xDADA));
        a.set(0xCAFE);
        assertTrue(aPower02.updated());
        azzert.that(aPower02.get(), is(0xDADA));
      }
      @Test public void seriesF02() {
        a.set(null);
        azzert.notNull(aPower17NullSafe());
        azzert.that(aPower05.version(),is(0L));
      }
      @Test public void seriesF03() {
        a.set(2);
        azzert.that(aPower05.get(), is(1 << 5));
        azzert.that(aPower17NullSafe.version(), is(0L));
        azzert.that(aPower02.version(), is(2L));
        azzert.that(aPower03.version(), is(3L));
        azzert.that(aPower05.version(), is(4L));
      }
      @Test public void seriesF04() {
        a.set(null);
        azzert.notNull(aPower17NullSafe());
      }
    }

  }

  /**
   * TODO(2016) Javadoc: automatically generated for type <code></code>
   *
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  public class Valued<@Nullable T> extends Cell<T> {
    /** Instantiates this class. */
    public Valued() {
      super(null);
    }
    /**
     * Instantiates this class.
     *
     * @param value JD
     */
    public Valued(final T value) {
      super(value);
    }
    /** @see java.util.function.Supplier#get() (auto-generated) */
    @Override public T get() {
      return value;
    }
    @Override protected boolean updated() {
      return true;
    }
  }
}