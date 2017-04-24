/* Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.reap;

import static il.org.spartan.Utils.*;
import static nano.ly.idiomatic.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.annotation.*;

/** A cell that may depend on others.
 * @param <T> JD
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
public class Recipe<@Nullable T> extends Cell<T> {
  final List<Cell<?>> prerequisites = new ArrayList<>();
  @Nullable Supplier<? extends @Nullable T> supplier;

  /** Instantiates this class.
   * @param supplier JD */
  public Recipe(final Supplier<? extends T> supplier) {
    this.supplier = supplier;
  }

  @Override @Nullable public Cell<T> clone() {
    return super.clone();
  }

  @Override @Nullable public T get() {
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
  @org.jetbrains.annotations.NotNull public Recipe<T> ingredient(@org.jetbrains.annotations.NotNull final Cell<?> ¢) {
    run(() -> ¢.dependents.add(this)).unless(¢.dependents.contains(this));
    run(() -> prerequisites.add(¢)).unless(prerequisites.contains(this));
    return this;
  }

  /** Add another cell on which this instance depends
   * @param cs JD
   * @return <code><b>this</b></code> */
  @org.jetbrains.annotations.NotNull public Recipe<T> ingredients(@org.jetbrains.annotations.NotNull final Cell<?>... cs) {
    for (@org.jetbrains.annotations.NotNull final Cell<?> ¢ : cs)
      ingredient(¢);
    return this;
  }

  @Override public boolean updated() {
    if (supplier == null)
      return true;
    if (version() <= latestPrequisiteVersion())
      return false;
    for (@org.jetbrains.annotations.NotNull final Cell<?> ¢ : prerequisites)
      if (!¢.updated())
        return false;
    return true;
  }

  @Nullable T eval() {
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
    for (@org.jetbrains.annotations.NotNull final Cell<?> ¢ : prerequisites)
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
  @SuppressWarnings("null") public static class NotNull<T> extends Recipe<T> {
    private final List<Cell<?>> prerequisites = new ArrayList<>();
    @Nullable private Supplier<? extends @Nullable T> supplier;

    /** Instantiates this class.
     * @param supplier JD */
    public NotNull(@org.jetbrains.annotations.NotNull final Supplier<? extends @NonNull T> supplier) {
      super(cantBeNull(supplier));
      cache(cantBeNull(supplier).get());
    }

    @Override @SuppressWarnings({}) public Recipe.NotNull<T> clone() {
      return (Recipe.NotNull<T>) super.clone();
    }

    /** Add another cell on which this instance depends
     * @param ¢ JD
     * @return <code><b>this</b></code> */
    @Override @org.jetbrains.annotations.NotNull public Recipe.NotNull<T> ingredients(final Cell<?>... ¢) {
      return (Recipe.NotNull<T>) super.ingredients(¢);
    }

    @Override public final boolean updated() {
      if (supplier == null)
        return true;
      for (@org.jetbrains.annotations.NotNull final Cell<?> ¢ : prerequisites)
        if (!¢.updated() || version() < ¢.version())
          return false;
      return true;
    }

    @Override T eval() {
      assert supplier != null;
      return supplier.get();
    }

    @Override final <N> N filter(@org.jetbrains.annotations.NotNull final N $) {
      return cantBeNull($);
    }
  }

  /** A cell that may depend on others.
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  public static class NullRobust<@Nullable T> extends Recipe<T> {
    /** Instantiates this class.
     * @param supplier JD */
    public NullRobust(@org.jetbrains.annotations.NotNull final Supplier<? extends T> supplier) {
      super(supplier);
      assert supplier != null;
    }

    @Override @SuppressWarnings({}) @Nullable public Cell<T> clone() {
      return super.clone();
    }

    @Override @Nullable public T get() {
      try {
        return super.get();
      } catch (@org.jetbrains.annotations.NotNull final NullPointerException x) {
        return null;
      }
    }

    /** Add another cell on which this instance depends
     * @param ¢ JD
     * @return <code><b>this</b></code> */
    @Override @org.jetbrains.annotations.NotNull public Recipe.NullRobust<T> ingredients(final Cell<?>... ¢) {
      super.ingredients(¢);
      return this;
    }

    @Override void cache(@SuppressWarnings("hiding") @Nullable final T cache) {
      try {
        super.cache(cache);
      } catch (@org.jetbrains.annotations.NotNull final NullPointerException ¢) {
        ¢.printStackTrace();
      }
    }

    @Override @Nullable T eval() {
      try {
        return super.eval();
      } catch (@org.jetbrains.annotations.NotNull final NullPointerException x) {
        return null;
      }
    }
  }
}