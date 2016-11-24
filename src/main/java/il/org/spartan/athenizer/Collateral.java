package il.org.spartan.athenizer;

/** Implementation of the collateral function: ₡. An identical copy of this
 * class should be present in the SpartanFeature project.
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class Collateral {
  public static void ₡(final Runnable... collateralStatements) {
    if (collateralStatements == null)
      throw new IllegalArgumentException();
    for (final Runnable ¢ : collateralStatements)
      ¢.run();
  }
}
