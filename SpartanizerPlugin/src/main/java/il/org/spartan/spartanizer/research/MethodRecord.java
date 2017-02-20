package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.characteristics.*;
import il.org.spartan.spartanizer.research.util.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

/** Collects statistics for a method in which a nano was found.
 * @author Ori Marcovitch
 * @since 2016 */
public class MethodRecord {
  public final String methodName;
  public final String methodClassName;
  public int numNPStatements;
  public int numNPExpressions;
  public final Collection<String> nps = new ArrayList<>();
  public final int numParameters;
  public final int numStatements;
  public final int numExpressions;
  public final MethodDeclaration before;
  public MethodDeclaration after;

  public MethodRecord(final MethodDeclaration d) {
    before = d;
    methodName = d.getName() + "";
    methodClassName = findTypeAncestor(d);
    numParameters = d.parameters().size();
    numStatements = measure.statements(d);
    numExpressions = measure.expressions(d);
  }

  void setAfter(final MethodDeclaration ¢) {
    after = ¢;
  }

  public boolean fullyMatched() {
    return (javadoc(after) + "").contains("[[");
  }

  /** @param n matched node
   * @param np matching nano */
  public void markNP(final ASTNode n, final String np) {
    if (excluded(np))
      return;
    numNPStatements += measure.statements(n);
    numNPExpressions += measure.expressions(n);
    if (epxressionWholeStatement(n))
      ++numNPStatements;
    nps.add(np);
  }

  private static boolean epxressionWholeStatement(final ASTNode ¢) {
    return iz.expression(¢) && iz.expressionStatement(parent(¢));
  }

  private static final List<String> excluded = Collections.singletonList(ArgumentsTuple.class.getSimpleName());

  public static boolean excluded(final String np) {
    return excluded.contains(np);
  }

  static String findTypeAncestor(final ASTNode ¢) {
    ASTNode n = ¢;
    String $ = "";
    while (n != null) {
      while (!iz.abstractTypeDeclaration(n) && n != null)
        n = n.getParent();
      if (n == null)
        break;
      $ += "." + az.abstractTypeDeclaration(n).getName();
      n = n.getParent();
    }
    return $.substring(1);
  }
}