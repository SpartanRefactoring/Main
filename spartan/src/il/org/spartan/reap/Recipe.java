/** Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.reap;

import static il.org.spartan.Utils.*;
import static il.org.spartan.idiomatic.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.annotation.*;

/** A cell that may depend on others.
 * @param <T> JD
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
public class Recipe<@Nullable T> extends Cell<T> {
  /** A cell that may depend on others.
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  @SuppressWarnings("null") public static class NotNull<T> extends Recipe<T> {
    private final List<Cell<?>> prerequisites = new ArrayList<>();
    private @Nullable Supplier<? extends @Nullable T> supplier;

    /** Instantiates this class.
     * @param supplier JD */
    public NotNull(final Supplier<? extends @NonNull T> supplier) {
      super(cantBeNull(supplier));
      cache(cantBeNull(supplier).get());
    }

    @SuppressWarnings({}) @Override public Recipe.NotNull<T> clone() {
      return (Recipe.NotNull<T>) super.clone();
    }

    @Override T eval() {
      assert supplier != null;
      return supplier.get();
    }

    @Override final <N> N filter(final N $) {
      return cantBeNull($);
    }

    /** Add another cell on which this instance depends
     * @param cs JD
     * @return <code><b>this</b></code> */
    @Override public Recipe.NotNull<T> ingredients(final Cell<?>... cs) {
      return (Recipe.NotNull<T>) super.ingredients(cs);
    }

    @Override public final boolean updated() {
      if (supplier == null)
        return true;
      for (final Cell<?> c : prerequisites)
        if (!c.updated() || version() < c.version())
          return false;
      return true;
    }
  }

  /** A cell that may depend on others.
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  public static class NullRobust<@Nullable T> extends Recipe<T> {
    /** Instantiates this class.
     * @param supplier JD */
    public NullRobust(final Supplier<? extends T> supplier) {
      super(supplier);
      assert supplier != null;
    }

    @Override void cache(@SuppressWarnings("hiding") @Nullable final T cache) {
      try {
        super.cache(cache);
      } catch (final NullPointerException x) {
        x.printStackTrace();
      }
    }

    @SuppressWarnings({}) @Override public Cell<T> clone() {
      return super.clone();
    }

    @Override @Nullable T eval() {
      try {
        return super.eval();
      } catch (final NullPointerException x) {
        return null;
      }
    }

    @Override public T get() {
      try {
        return super.get();
      } catch (final NullPointerException x) {
        return null;
      }
    }

    /** Add another cell on which this instance depends
     * @param cs JD
     * @return <code><b>this</b></code> */
    @Override public Recipe.NullRobust<T> ingredients(final Cell<?>... cs) {
      super.ingredients(cs);
      return this;
    }
  }

  final List<Cell<?>> prerequisites = new ArrayList<>();
  @Nullable Supplier<? extends @Nullable T> supplier;

  /** Instantiates this class.
   * @param supplier JD */
  public Recipe(final Supplier<? extends T> supplier) {
    this.supplier = supplier;
  }

  @Override public Cell<T> clone() {
    return super.clone();
  }

  @Nullable T eval() {
    assert supplier != null;
    return supplier.get();
  }

  /** To be overridden by extending classes for e.g., null protection
   * @param $ result
   * @return parameter */
  @SuppressWarnings("static-method") <N> N filter(final N n) {
    return n;
  }

  @Override public T get() {
    if (updated())
      return cache();
    assert supplier != null;
    for (final Cell<?> c : prerequisites)
      c.get();
    assert supplier != null;
    cache(filter(eval()));
    version = latestPrequisiteVersion() + 1;
    return cache();
  }

  /** Add another cell on which this instance depends
   * @param e JD
   * @return <code><b>this</b></code> */
  public Recipe<T> ingredient(final Cell<?> e) {
    run(() -> {
      e.dependents.add(this);
    }).unless(e.dependents.contains(this));
    run(() -> {
      prerequisites.add(e);
    }).unless(prerequisites.contains(this));
    return this;
  }

  /** Add another cell on which this instance depends
   * @param es JD
   * @return <code><b>this</b></code> */
  public Recipe<T> ingredients(final Cell<?>... es) {
    for (final Cell<?> e : es)
      ingredient(e);
    return this;
  }

  final long latestPrequisiteVersion() {
    long $ = 0;
    for (final Cell<?> c : prerequisites)
      if ($ < c.version())
        $ = c.version();
    return $;
  }

  @Override public boolean updated() {
    if (supplier == null)
      return true;
    if (version() <= latestPrequisiteVersion())
      return false;
    for (final Cell<?> c : prerequisites)
      if (!c.updated())
        return false;
    return true;
  }

  @Override void uponForcedSet() {
    supplier = null;
  }
}