package il.org.spartan.zoomer.collateral;

/** Implementation of the collateral function: ₡. An identical copy of this
 * class should be present in the SpartanFeature project.
 * @author Ori Roth
 * @since Nov 25, 2016 */
class Collateral {
  public static void ₡(final Runnable... collateralStatements) {
    if (collateralStatements == null)
      throw new IllegalArgumentException();
    for (final Runnable ¢ : collateralStatements)
      ¢.run();
  }
}
