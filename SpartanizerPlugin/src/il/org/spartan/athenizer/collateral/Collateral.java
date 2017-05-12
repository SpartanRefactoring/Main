package il.org.spartan.athenizer.collateral;

import java.util.stream.*;

/** Implementation of the collateral function: ₡. An identical copy of this
 * class should be present in the SpartanFeature project.
 * @author Ori Roth
 * @since Nov 25, 2016 */
enum Collateral {
  ;
  public static void ₡(final Runnable... collateralStatements) {
    if (collateralStatements == null)
      throw new IllegalArgumentException();
    Stream.of(collateralStatements).forEach(Runnable::run);
  }
}
