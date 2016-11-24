package il.org.spartan.spartanizer.athenizer;

public class Collateral {
  public static void ₡(final Runnable... collateralStatements) {
    if (collateralStatements == null)
      throw new IllegalArgumentException();
    for (final Runnable ¢ : collateralStatements)
      ¢.run();
  }
}
