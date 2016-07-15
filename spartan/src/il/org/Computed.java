/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.annotation.*;

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
   * @param generator JD
   */
  public Computed(final Supplier<T> generator) {
    super(null);
    this.maker = generator;
  }
  @SuppressWarnings({ "unchecked", "null" }) @Override public Computed<@Nullable T> clone() throws CloneNotSupportedException {
    return (Computed<@Nullable T>) super.clone();
  }
  @Override public Computed<T> dependsOn(final Cell<?> c) {
    super.dependsOn(c);
    prerequisites.add(c);
    return this;
  }
  /** @see java.util.function.Supplier#get() (auto-generated) */
  @Override public T get() {
    if (maker == null)
      return value;
    if (!valid())
      for (final Cell<?> c : prerequisites)
        c.get();
    validateSelf();
    assert maker != null;
    return value = maker.get();
  }
  @Override public void set(@Nullable final T value) {
    super.set(value);
    maker = null;
  }

  private @Nullable Supplier<T> maker;
  private final List<Cell<?>> prerequisites = new ArrayList<>();
}