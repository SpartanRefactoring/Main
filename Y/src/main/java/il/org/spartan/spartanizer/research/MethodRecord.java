package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.nanos.characteristics.*;
import il.org.spartan.spartanizer.research.util.*;

/** Collects statistics for a method in which a nano was found.
 * @author Ori Marcovitch */
public class MethodRecord {
  public final String methodName;
  public final String methodClassName;
  private int numNPStatements;
  private int numNPNodes;
  int numNPExpressions;
  public final Collection<String> nps = an.empty.list();
  public final int numParameters;
  public final int numStatements;
  public final int numExpressions;
  public final int numNodes;
  public final MethodDeclaration before;
  public MethodDeclaration after;
  private boolean fullyMatched;

  public MethodRecord(final MethodDeclaration d) {
    before = d;
    methodName = identifier(name(d));
    methodClassName = findTypeAncestor(d);
    numParameters = parameters(d).size();
    numStatements = measure.commands(d);
    numExpressions = measure.expressions(d);
    numNodes = count.nodes(d);
  }

  void setAfter(final MethodDeclaration ¢) {
    after = ¢;
  }

  public boolean fullyMatched() {
    return fullyMatched;
  }

  public boolean touched() {
    return numNPStatements > 0 || numNPExpressions > 0;
  }

  public int numNPStatements() {
    return Math.min(numNPStatements, numStatements);
  }

  public int numNPExpressions() {
    return Math.min(numNPExpressions, numExpressions);
  }

  public int numNPNodes() {
    return Math.min(numNPNodes, numNodes);
  }

  public void markNP(final ASTNode n, final String np) {
    if (excluded(np)) {
      numNPExpressions += 1;
      numNPNodes += 1;
      return;
    }
    numNPStatements += measure.commands(n);
    numNPExpressions += measure.expressions(n);
    numNPNodes += count.nodes(n);
    if (epxressionWholeStatement(n))
      ++numNPStatements;
    nps.add(np);
    if (iz.methodDeclaration(n))
      fullyMatched = true;
  }

  private static boolean epxressionWholeStatement(final ASTNode ¢) {
    return iz.expression(¢)//
        && iz.expressionStatement(parent(¢));
  }

  private static final List<String> excluded = Collections.singletonList(MyArguments.class.getSimpleName());

  public static boolean excluded(final String np) {
    return excluded.contains(np);
  }

  static String findTypeAncestor(final ASTNode ¢) {
    ASTNode n = ¢;
    String $ = "";
    for (; n != null; n = parent(n)) {
      while (!iz.abstractTypeDeclaration(n) && n != null)
        n = parent(n);
      if (n == null)
        break;
      $ += "." + az.abstractTypeDeclaration(n).getName();
    }
    return $.substring(1);
  }
}