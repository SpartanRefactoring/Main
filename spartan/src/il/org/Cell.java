/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.annotation.*;

/**
 * A cell stores a value of some type (which is passed by parameter). A cell may
 * be either {@link Valued} or {@link Computed}. A computed cell typically
 * depends on other cells, which may either valued, or computed, and hence
 * depending on yet other cells.
 *
 * A change to a cell's value is triggers recomputation of all cells that depend
 * on it.
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
    invalidateDependents();
  }
  Cell<T> dependsOn(final Cell<?> c) {
    c.dependents.add(this);
    invalidateDependents();
    return this;
  }
  void invalidateDependents() {
    invalidateSelf();
    for (final Cell<?> c : dependents)
      c.invalidateDependents();
  }
  void invalidateSelf() {
    valid = false;
  }
  boolean valid() {
    return valid;
  }
  final void validateSelf() {
    valid = true;
  }
  /** @return the value stored in this cell */
  @Nullable T value() {
    return value;
  }

  private final List<Cell<?>> dependents = new ArrayList<>();
  private boolean valid = false;
  protected T value;
}