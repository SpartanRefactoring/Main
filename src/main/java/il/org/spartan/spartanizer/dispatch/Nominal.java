package il.org.spartan.spartanizer.dispatch;

import il.org.spartan.spartanizer.tipping.*;

/** The {@link TipperCategory} of renaming, and renaming related
 * {@link Tipper}s.
 * @author Yossi Gil
 * @year 2016 */
public interface Nominal extends TipperCategory {
  String label = "Nominal";

  @Override default String description() {
    return label;
  }
}