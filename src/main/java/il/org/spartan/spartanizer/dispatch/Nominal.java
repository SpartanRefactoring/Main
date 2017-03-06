package il.org.spartan.spartanizer.dispatch;

import il.org.spartan.spartanizer.tipping.*;

/** The {@link TipperCategory} of renaming, and renaming related
 * {@link Tipper}s.
 * @author Yossi Gil {@code yossi dot (optional) gil at gmail dot (required) com}
 * @since Sep 28, 2016 */
public interface Nominal extends TipperCategory {
  String ____ = "Spartanizing code by using more spartan names";

  @Override default String description() {
    return ____;
  }
}
