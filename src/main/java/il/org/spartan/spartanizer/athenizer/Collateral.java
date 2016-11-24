package il.org.spartan.spartanizer.athenizer;

public class Collateral {
  public static void ₡(Runnable... collateralStatements) {
    if (collateralStatements == null)
      throw new IllegalArgumentException();
    for (Runnable ¢ : collateralStatements)
      ¢.run();
  }
}
