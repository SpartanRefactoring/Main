package il.org.spartan.bloater.collateral;

import il.org.spartan.*;

/** Implementation of the collateral function: ₡. An identical copy of this
 * class should be present in the SpartanFeature project.
 * @author Ori Roth
 * @since Nov 25, 2016 */
enum Collateral {
  ;
  public static void ₡(final Runnable... collateralStatements) {
    if (collateralStatements == null)
      throw new IllegalArgumentException();
    as.list(collateralStatements).forEach(Runnable::run);
  }
}
