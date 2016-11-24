package il.org.spartan.spartanizer.research.analyses;

import java.text.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** @author Ori Marcovitch
 * @since Nov 3, 2016 */
public abstract class Analyzer<T> {
  protected abstract int metric(ASTNode n);

  public abstract void logMethod(final MethodDeclaration before, final MethodDeclaration after);

  public abstract void printComparison();

  public abstract void printAccumulated();

  protected static Integer Integer(final int ¢) {
    return Integer.valueOf(¢);
  }

  protected int getMax(final Map<Integer, T> i) {
    return i.keySet().stream().max((x, y) -> x.intValue() > y.intValue() ? 1 : -1).get().intValue();
  }

  /** If double is integer, removes the .0. <br>
   * If double is double, leaves only 2 first digits.
   * @param ¢
   * @return */
  protected static String tidy(final double ¢) {
    int $ = 0;
    final double ¢formatted = Double.parseDouble(new DecimalFormat("#0.00").format(¢));
    if (¢formatted != Math.floor(¢formatted))
      return ¢formatted + "";
    $ = (int) ¢formatted;
    return $ + "";
  }

  protected abstract double enumElement(T t);
}
