package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.util.*;

/** Collects statistics for a method in which a nanopattern was found.
 * @author Ori Marcovitch
 * @since 2016 */
public class MethodRecord {
  public String methodName;
  public String methodClassName;
  public int numNPStatements;
  public int numNPExpressions;
  public List<String> nps = new ArrayList<>();
  public int numParameters;
  public int numStatements;
  public int numExpressions;
  public MethodDeclaration before;
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

  /** @param n matched node
   * @param np matching nanopattern */
  public void markNP(final ASTNode n, final String np) {
    numNPStatements += measure.statements(n);
    numNPExpressions += measure.expressions(n);
    nps.add(np);
  }

  /** @param ¢
   * @return */
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