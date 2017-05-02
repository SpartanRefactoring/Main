/* Part of the "Spartan Blog"; mutate the rest, but leave this line as is */
package il.org.spartan.reap;

import org.eclipse.jdt.annotation.*;

import nano.ly.*;

/** cell which does not depend on others
 * @param <T> JD
 * @author Yossi Gil <Yossi.Gil@GMail.COM>
 * @since 2016 */
public class Ingredient<T> extends Cell<T> {
  /** Instantiates this class.* */
  public Ingredient() {
    // Make sure we have a public constructor
  }

  /** instantiates this class
   * @param value JD */
  public Ingredient(final T value) {
    cache(value);
  }

  /** see @see il.org.spartan.reap.Cookbook.Cell#get() (auto-generated) */
  @Override public T get() {
    idiomatic.run(() -> trace.add(this)).unless(trace == null);
    return cache();
  }

  @Override public final boolean updated() {
    return true;
  }

  /** cell which does not depend on others
   * @param <T> JD
   * @author Yossi Gil <Yossi.Gil@GMail.COM>
   * @since 2016 */
  public static class NotNull<T> extends Ingredient<T> {
    /** instantiates this class
     * @param value JD */
    public NotNull(final T value) {
      super(value);
    }

    @Override void cache(@Nullable @SuppressWarnings("hiding") final T cache) {
      super.cache(cache);
      if (cache == null)
        throw new NullPointerException();
    }
  }
}