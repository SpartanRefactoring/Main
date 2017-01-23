package il.org.spartan.spartanizer.research.analyses.util;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.utils.tdd.*;
import il.org.spartan.utils.*;

/** TODO:  Ori Marcovitch
 please add a description 
 @author Ori Marcovitch
 * @since Dec 14, 2016 
 */

public enum Count {
  ;
  static final Pair<Int, Int> ifStatements = newPair();
  static final Pair<Int, Int> loopsStatements = newPair();
  static final Pair<Int, Int> statements = newPair();
  static final Pair<Int, Int> ternaries = newPair();

  public static void before(final ASTNode ¢) {
    ifStatements.first.inner += enumerate.ifStatements(¢);
    loopsStatements.first.inner += enumerate.loops(¢);
    statements.first.inner += enumerate.statements(¢);
    ternaries.first.inner += enumerate.ternaries(¢);
  }

  public static void after(final ASTNode ¢) {
    ifStatements.second.inner += enumerate.ifStatements(¢);
    loopsStatements.second.inner += enumerate.loops(¢);
    statements.second.inner += enumerate.statements(¢);
    ternaries.second.inner += enumerate.ternaries(¢);
  }

  public static void print() {
    System.out.println("statements: " + statements.first.inner + " ---> " + statements.second.inner//
        + " ratio: [" + safeDiv(statements.second.inner, statements.first.inner) + "]");
    System.out.println("loops: " + loopsStatements.first.inner + " ---> " + loopsStatements.second.inner//
        + " ratio: [" + safeDiv(loopsStatements.second.inner, loopsStatements.first.inner) + "]");
    System.out.println("ifStatements: " + ifStatements.first.inner + " ---> " + ifStatements.second.inner//
        + " ratio: [" + safeDiv(ifStatements.second.inner, ifStatements.first.inner) + "]");
    System.out.println("ternaries: " + ternaries.first.inner + " ---> " + ternaries.second.inner//
        + " ratio: [" + safeDiv(ternaries.second.inner, ternaries.first.inner) + "]");
  }

  private static double safeDiv(final double sumSratio, final double d) {
    return d == 0 ? 1 : sumSratio / d;
  }

  private static Pair<Int, Int> newPair() {
    return new Pair<>(new Int(), new Int());
  }
}
