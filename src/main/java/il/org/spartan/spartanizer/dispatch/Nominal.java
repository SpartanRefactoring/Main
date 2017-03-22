package il.org.spartan.spartanizer.dispatch;

import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.Nullable;

/** The {@link TipperCategory} of renaming, and renaming related
 * {@link Tipper}s.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 28, 2016 */
public interface Nominal extends TipperCategory {
  String ____ = "Spartanizing code by using more spartan names";

  @Nullable
  @Override default String description() {
    return ____;
  }
}
