package il.org.spartan.spartanizer.dispatch;

/** The {@link TipperCategory} of renaming, and renaming related
 * {@link Tipper}s.
 * @author Yossi Gil
 * @year 2016 */
public interface Nominal extends TipperCategory {
  final String label = "Nominal";

  @Override default String description() {
    return label;
  }
}