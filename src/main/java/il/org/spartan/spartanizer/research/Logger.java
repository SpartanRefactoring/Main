package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Logger {
  private static Map<Integer, MethodRecord> methodsStatistics = new HashMap<>();

  public static void summarize() {
    for (final Integer k : methodsStatistics.keySet()) {
      final MethodRecord m = methodsStatistics.get(k);
      System.out.println(m.npCounter + " : " + m.methodName);
    }
    methodsStatistics = new HashMap<>();
  }

  public static void logNanoPattern(final ASTNode n, final String np) {
    final MethodDeclaration m = findMethodAncestor(n);
    final Integer key = Integer.valueOf(m.hashCode());
    if (!methodsStatistics.containsKey(key))
      methodsStatistics.put(key, new MethodRecord(m));
    methodsStatistics.get(key).markNanoPattern(np);
  }

  /** @param ¢
   * @return */
  private static MethodDeclaration findMethodAncestor(final ASTNode ¢) {
    ASTNode n = ¢;
    while (!iz.methodDeclaration(n))
      n = n.getParent();
    return az.methodDeclaration(n);
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

  static class MethodRecord {
    public String methodName;
    public String methodClassName;
    public int npCounter;
    public List<String> nps = new ArrayList<>();
    public int numParameters;
    public int numStatements;

    public MethodRecord(final MethodDeclaration m) {
      methodName = m.getName() + "";
      methodClassName = findTypeAncestor(m);
      numParameters = m.parameters().size();
      numStatements = metrics.statementsQuantity(m);
    }

    /** @param np */
    public void markNanoPattern(final String np) {
      ++npCounter;
      nps.add(np);
    }
  }

  /** @param cu */
  public static void logCompilationUnit(final ASTNode cu) {
    // TODO Auto-generated method stub
  }

  /** @param outputDir */
  public static void summarize(final String outputDir) {
    // TODO Auto-generated method stub
  }
}
