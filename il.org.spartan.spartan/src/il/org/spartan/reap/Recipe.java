/* Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.reap;

import static fluent.ly.idiomatic.*;
import static il.org.spartan.Utils.*;

import java.util.*;
import java.util.function.*;

/** A cell that may depend on others.
 * @param <T> JD
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
public class Recipe<T> extends Cell<T> {
  final List<Cell<?>> prerequisites = new ArrayList<>();
  Supplier<? extends T> supplier;

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
    prerequisites.forEach(Cell::get);
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
   * @return parameter */
  @SuppressWarnings("static-method") <N> N filter(final N $) {
    return $;
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
  public static class NonNull<T> extends Recipe<T> {
    private final List<Cell<?>> prerequisites = new ArrayList<>();
    private Supplier<? extends T> supplier;

    /** Instantiates this class.
     * @param supplier JD */
    public NonNull(final Supplier<? extends T> supplier) {
      super(cantBeNull(supplier));
      cache(cantBeNull(supplier).get());
    }
    @Override @SuppressWarnings({}) public Recipe.NonNull<T> clone() {
      return (Recipe.NonNull<T>) super.clone();
    }
    /** Add another cell on which this instance depends
     * @param ¢ JD
     * @return <code><b>this</b></code> */
    @Override public Recipe.NonNull<T> ingredients(final Cell<?>... ¢) {
      return (Recipe.NonNull<T>) super.ingredients(¢);
    }
    @Override public final boolean updated() {
      if (supplier == null)
        return true;
      for (final Cell<?> ¢ : prerequisites)
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
  public static class NullRobust<T> extends Recipe<T> {
    /** Instantiates this class.
     * @param supplier JD */
    public NullRobust(final Supplier<? extends T> supplier) {
      super(supplier);
      assert supplier != null;
    }
    @Override @SuppressWarnings({}) public Cell<T> clone() {
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
    @Override public Recipe.NullRobust<T> ingredients(final Cell<?>... ¢) {
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