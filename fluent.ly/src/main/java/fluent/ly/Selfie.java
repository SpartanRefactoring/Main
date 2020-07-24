package fluent.ly;

import java.util.function.Supplier;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-14 */
public interface Selfie<Self extends Selfie<Self>> {
  default <U> void change(final U ¢) {
    forget.it(¢);
  }
  @SuppressWarnings("unchecked") default Self self() {
    return (Self) this;
  }
  default <F> Self self(final Supplier<F> t) {
    change(t.get());
    return self();
  }
}
