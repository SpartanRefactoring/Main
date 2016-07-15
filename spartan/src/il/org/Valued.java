/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org;

import org.eclipse.jdt.annotation.*;

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
}