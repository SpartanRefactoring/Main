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
public class Deducer {
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
    public Computed(final Supplier<T> supplier) {
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
    private @Nullable Supplier<T> supplier;
  }

  /** a^5 := a^2 * a^3 */

  /**
   * An example deducer, in which all cells are of type <code>@NonNull</code
   * {@link Integer}. The root cells are:
   * <ol>
   * <li>{@link #a()}
   * <li>{@link #b()}
   * <li>{@link #c()}
   * <li>{@link #d()}
   * </ol>
   * From these, the following cells are computed. cell.`
   * <ol>
   * <li>{@link Example#a()}
   * <li>{@link Example#aP()}
   * <li>{@link Example#c()}
   * <li>{@link Example#d()}
   * </ol>
   * *
   *
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016
   */
  @SuppressWarnings({ "boxing", "null", "unused" }) public static class Example extends Deducer {
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

    /** Must not be private; used for testing of proper lazy evaluation */
    int _aPower02Calls;
    /** Must not be private; used for testing of proper lazy evaluation */
    int _aPower03Calls;
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
    /** Can, and often should be made private; package is OK */
    final Cell<@Nullable Integer> aPower05 = new Computed<@Nullable Integer>(//
        () -> aPower02() * aPower03()).dependsOn(aPower02, aPower03);
    /** Can, and often should be made private; package is OK */
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
    final Cell<@Nullable Integer> b = new Valued<@Nullable Integer>(3);
    final Cell<@Nullable Integer> c = new Valued<@Nullable Integer>(5);
    final Cell<@Nullable Integer> d = new Computed<@Nullable Integer>(() -> a() + b.get() + c.get()).dependsOn(a, b, c);

    @FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "javadoc" }) public static class TEST extends Example {
      @Test public void layerA05() {
        a.set(2);
        azzert.notNull(a());
      }
      @Test public void layerA06() {
        a.set(2);
        azzert.notNull(aPower02());
        azzert.that(aPower02(), is(4));
      }
      @Test public void layerA07() {
        a.set(2);
        azzert.notNull(aPower03());
        azzert.that(aPower03(), is(8));
      }
      @Test public void layerA08() {
        a.set(2);
        azzert.notNull(aPower02());
      }
      @Test public void layerA09() {
        a.set(null);
        azzert.isNull(a());
      }
      @Test public void layerA1() {
        azzert.isNull(a());
      }
      @Test(expected = NullPointerException.class) public void layerA10() {
        a.set(null);
        aPower02();
      }
      @Test(expected = NullPointerException.class) public void layerA11() {
        a.set(null);
        aPower03();
      }
      @Test(expected = NullPointerException.class) public void layerA12() {
        a.set(null);
        aPower02();
      }
      @Test public void layerA13() {
        a.set(null);
        azzert.isNull(aPower17NullSafe());
        a.set(2);
        azzert.notNull(aPower17NullSafe());
        azzert.that(a(), is(2));
      }
      @Test public void layerA14() {
        a.set(2);
        azzert.notNull(aPower17NullSafe());
        azzert.that(a(), is(2));
        azzert.that(aPower17NullSafe(), is(1 << 17));
      }
      @Test public void layerA15() {
        a.set(null);
        azzert.isNull(aPower17NullSafe());
      }
      @Test public void layerA16() {
        a.set(null);
        azzert.isNull(aPower17NullSafe.get());
      }
      @Test public void layerA17() {
        a.set(null);
        final Computed<?> x = (Computed<?>) aPower17NullSafe;
        azzert.isNull(x.get());
      }
      @Test public void layerA18() {
        a.set(null);
        final Computed<?> x = (Computed<?>) aPower17NullSafe;
        f(x);
      }
      @Test public void layerA19() {
        a.set(null);
        aPower17NullSafe.get();
      }
      @Test(expected = NullPointerException.class) public void layerA2() {
        aPower02().getClass();
      }
      @Test public void layerA20() {
        aPower17NullSafe.get();
      }
      @Test(expected = NullPointerException.class) public void layerA3() {
        aPower03().getClass();
      }
      @Test(expected = NullPointerException.class) public void layerA4() {
        aPower05().getClass();
      }
      @Test(expected = NullPointerException.class) public void layerA5() {
        a().toString().getClass();
      }
      @Test public void layerB01() {
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
      @Test public void layerB02() {
        a.set(2);
        azzert.notNull(aPower02());
        azzert.that(aPower02(), is(4));
        a.set(3);
        azzert.notNull(aPower02());
        azzert.that(aPower02(), is(9));
      }
      @Test public void layerB03() {
        a.set(2);
        azzert.notNull(aPower03());
        azzert.that(aPower03(), is(8));
        a.set(3);
        azzert.notNull(aPower03());
        azzert.that(aPower03(), is(27));
      }
      @Test public void layerB04() {
        a.set(2);
        azzert.notNull(aPower02());
      }
      @Test public void layerC00() {
        a.set(-3);
        azzert.that(_aPower03Calls, is(0));
        azzert.that(_aPower02Calls, is(0));
      }
      @Test public void layerC01() {
        a.set(-3);
        azzert.that(aPower03(), is(-27));
        azzert.that(_aPower03Calls, is(1)); // Force invocation
        azzert.that(_aPower02Calls, is(1));
      }
      @Test public void layerC02() {
        azzert.that(a.version(), is(0L));
        azzert.that(aPower17NullSafe.version(), is(0L));
      }
      @Test public void layerC03() {
        azzert.that(aPower02.version(), is(0L));
        azzert.that(aPower03.version(), is(0L));
      }
      @Test public void layerC04() {
        a.set(-2);
        azzert.that(a.version(), is(1L));
        azzert.that(aPower03.version(), is(0L));
        azzert.that(_aPower03Calls, is(0));
        azzert.that(aPower17NullSafe(), is(-(1 << 17))); // Force invocation
        azzert.that(_aPower02Calls, is(1));
        azzert.that(_aPower03Calls, is(1));
      }
      @Test public void layerC05() {
        a.set(-2);
        azzert.that(aPower17NullSafe(), is(-(1 << 17))); // Force invocation
        azzert.that(_aPower02Calls, is(1));
        azzert.that(_aPower03Calls, is(1));
      }
      @Test public void layerD01() {
        azzert.that(a.version, is(0L));
        azzert.that(aPower02.version, is(0L));
        azzert.that(aPower03.version, is(0L));
        azzert.that(aPower17NullSafe.version, is(0L));
      }
      @Test public void layerD02() {
        azzert.that(a.version, is(0L));
        a.set(1);
        azzert.that(a.version, is(1L));
        azzert.that(aPower02.version, is(0L));
        azzert.that(aPower03.version, is(0L));
        azzert.that(aPower17NullSafe.version, is(0L));
      }
      @Test public void layerD03() {
        a.set(14);
        azzert.that(aPower02.version, is(0L));
        azzert.that(aPower02.get(), is(196)); // Force evaluation
        azzert.that(aPower03.version, is(0L));
        azzert.that(aPower02.version, is(2L));
        azzert.that(aPower17NullSafe.version, is(0L));
      }
      @Test public void layerD04() {
        a.set(14);
        azzert.notNull(a.get());
      }
      @Test public void layerD05() {
        a.set(14);
        azzert.notNull(a.get());
        azzert.that(a.get(), is(14));
        azzert.that(aPower02.get(), is(196)); // Sanity check
      }
      @Test public void layerD06() {
        a.set(14);
        azzert.notNull(a.get());
        a.get(); // Force evaluation
        azzert.that(aPower02.version(), is(0L));
        a.get(); // Force evaluation
        azzert.that(aPower02.version(), is(0L));
      }
      @Test public void layerD07() {
        a.set(14);
        azzert.notNull(a.get());
        a.get(); // Force evaluation
        azzert.not(aPower02.updated());
      }
      @Test public void layerD08() {
        a.set(14);
        azzert.that(a.get(), is(14)); // Force evaluation
        azzert.that(a.version(), is(1L));
        azzert.that(aPower02.version, is(0L));
        azzert.that(((Computed<Integer>) aPower02).latestPrequisiteVersion(), is(1L));
      }
      @Test public void layerD09() {
        a.set(14);
        azzert.that(a.get(), is(14)); // Force evaluation
        azzert.that(a.version(), is(1L));
        azzert.that(aPower02.version, is(0L));
        azzert.notNull(a.dependents);
      }
      @Test public void layerD10() {
        a.set(14);
        azzert.that(a.get(), is(14)); // Force evaluation
        azzert.that(a.version(), is(1L));
        azzert.that(aPower02.version, is(0L));
        azzert.that(a.dependents.size(), is(4)); // d, aPower2, aPower3, aPowe17
        azzert.assertTrue("", a.dependents.contains(aPower02));
        azzert.falze(a.dependents.contains(null));
      }
      @Test public void layerD11() {
        a.set(14);
        azzert.that(a.get(), is(14)); // Force evaluation
        azzert.that(a.version(), is(1L));
      }
      @Test public void layerD12() {
        assertTrue(a.dependents.contains(aPower02));
      }
      @Test public void layerD13() {
        assertTrue(a.dependents.contains(aPower03));
      }
      @Test public void layerD14() {
        assertFalse(a.dependents.contains(aPower05));
      }
      @Test public void layerD15() {
        assertTrue(a.dependents.contains(aPower17NullSafe));
      }
      @Test public void layerD16() {
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
      @Test public void layerD17() {
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
      @Test public void layerE01() {
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
      @Test public void layerE02() {
        azzert.that(a.version(), is(0L));
        azzert.that(aPower02.version(), is(0L));
        azzert.that(aPower03.version(), is(0L));
        azzert.that(aPower05.version(), is(0L));
        azzert.that(aPower17NullSafe.version(), is(0L));
      }
      @Test public void layerE03() {
        a.set(2);
        b.set(3);
        c.set(4);
        azzert.that(d.get(), is(9));
      }
      @Test public void layerE04() {
        a.set(2);
        aPower02.set(3);
        aPower03.set(5);
        azzert.that(aPower05.get(), is(15));
      }
      @Test public void layerE05() {
        a.set(2);
        azzert.that(aPower05.get(), is(1 << 5));
        azzert.that(aPower17NullSafe.version(), is(0L));
        azzert.that(aPower02.version(), is(2L));
        azzert.that(aPower03.version(), is(3L));
        azzert.that(aPower05.version(), is(4L));
      }
      @Test public void layerE06() {
        a.set(2);
        assertFalse("aPower5 should not be updated! (recursive dependency on a)", aPower05.updated());
      }
      @Test public void layerF01() {
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
      @Test public void layerF02() {
        a.set(null);
        azzert.isNull(aPower17NullSafe());
      }
      @Override @Test public void layerF02() {
        a.set(2);
        azzert.that(aPower05.get(), is(1 << 5));
        azzert.that(aPower17NullSafe.version(), is(0L));
        azzert.that(aPower02.version(), is(2L));
        azzert.that(aPower03.version(), is(3L));
        azzert.that(aPower05.version(), is(4L));
      }
      private void f(final Computed<?> x) {
        x.get();
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