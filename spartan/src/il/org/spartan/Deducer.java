/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.idiomatic.*;
import static java.lang.Math.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.annotation.*;

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
    long version() {
      return version;
    }

    abstract boolean updated();

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
      if (updated())
        return value;
      version = latestPrequisiteVersion() + 1;
      return value = supplier.get();
    }
    @Override public void set(final T value) {
      super.set(value);
      supplier = null;
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
      // if (supplier == null)
      // return true;
      for (final Cell<?> c : prerequisites)
        if (!c.updated() || version() < c.version())
          return false;
      return true;
    }

    private final List<Cell<?>> prerequisites = new ArrayList<>();
    private Supplier<T> supplier;
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