package il.org.spartan.spartanizer.research.analyses;

import java.text.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

/** Analyzer base class
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
abstract class Analyzer<T> {
  protected abstract int metric(ASTNode n);
  public abstract void logMethod(MethodDeclaration before, MethodDeclaration after);
  public abstract void printComparison();
  public abstract void printAccumulated();
  static Integer Integer(final int ¢) {
    return Integer.valueOf(¢);
  }
  int getMax(final Map<Integer, T> m) {
    return m.keySet().stream().max((x, y) -> x.intValue() > y.intValue() ? 1 : -1).get().intValue();
  }
  /** If parameter is integer, removes the .0. <br>
   * If parameter is double, leaves only 2 first digits.
   * @param ¢
   * @return */
  static String tidy(final double ¢) {
    final double ret = Double.parseDouble(new DecimalFormat("#0.00").format(¢));
    return ret != Math.floor(ret) ? ret + "" : asInt(ret);
  }
  private static String asInt(final double $) {
    return (int) $ + "";
  }
  protected abstract double enumElement(T t);
}
