/* Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.reap;

import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/** A cell stores a value of some type (which is passed by parameter). A cell
 * may be either {@link Ingredient} or {@link Recipe}. A computed cell typically
 * depends on other cells, which may either valued, or computed, and hence
 * depending on yet other cells. A change to a cell's value is triggers
 * invalidates all cells that depend on it.
 * @param <T> JD
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016
 * @see Ingredient
 * @see Recipe */
//
public abstract class Cell<T> implements Supplier<T>, Cloneable {
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
  /** Used for fluent API, synonym of {@link Cell#set(Object)}. sets the current
   * value of this cell
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
    version = oldestDependent() + 1; // Invalidate all dependents
    return this;
  }
  /** template function to be implemented by clients; normally an ingredient is
   * always updated and a dish is updated if all its ingredients are updated,
   * and the recipe was applied <i>after</i> all the ingredients where updated.
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
    // empty by default
  }
  private long oldestDependent() {
    long $ = 0;
    for (final Cell<?> ¢ : dependents)
      $ = max($, ¢.version);
    return $;
  }

  @FunctionalInterface
  interface Function2<T1, T2, R> {
    R apply(T1 ¢1, T2 ¢2);
  }

  /** @author Yossi Gil <Yossi.Gil@GMail.COM>
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

    /** <code>Cookbook</code>
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
}