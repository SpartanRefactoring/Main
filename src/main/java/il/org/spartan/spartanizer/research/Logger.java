package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** The purpose of this class is to gather information about NPs and summarize
 * it, so we can submit nice papers and win eternal fame. <br>
 * Whenever an NP is matched it should log itself.
 * @author Ori Marcovitch
 * @since 2016 */
public class Logger {
  private static Map<Integer, MethodRecord> methodsStatistics = new HashMap<>();
  private static int numMethods;

  public static void summarize(String outputDir) {
    CSVStatistics report = null;
    try {
      report = new CSVStatistics(outputDir + "/report.csv", "property");
    } catch (IOException x) {
      x.printStackTrace();
      return;
    }
    double sumSratio = 0;
    double sumEratio = 0;
    for (Integer k : methodsStatistics.keySet()) {
      MethodRecord m = methodsStatistics.get(k);
      report //
          .put("Name", m.methodClassName + "~" + m.methodName) //
          .put("#Statement", m.numStatements) //
          .put("#NP Statements", m.numNPStatements) //
          .put("Statement ratio", m.numStatements == 0 ? 1 : m.numNPStatements / m.numStatements) //
          .put("#Expressions", m.numExpressions) //
          .put("#NP expressions", m.numNPExpressions) //
          .put("Expression ratio", m.numExpressions == 0 ? 1 : m.numNPExpressions / m.numExpressions) //
          .put("#Parameters", m.numParameters) //
          .put("#NP", m.nps.size()) //
      ;
      report.nl();
      sumSratio += m.numStatements == 0 ? 1 : m.numNPStatements / m.numStatements;
      sumEratio += m.numExpressions == 0 ? 1 : m.numNPExpressions / m.numExpressions;
    }
    System.out.println("Total methods number: " + numMethods);
    System.out.println("Average statement ratio: " + sumSratio / numMethods);
    System.out.println("Average Expression ratio: " + sumEratio / numMethods);
    report.close();
    reset();
  }

  private static void reset() {
    methodsStatistics = new HashMap<>();
    numMethods = 0;
  }

  public static void logNP(final ASTNode n, final String np) {
    MethodDeclaration m = findMethodAncestor(n);
    Integer key = Integer.valueOf(m.hashCode());
    if (!methodsStatistics.containsKey(key))
      methodsStatistics.put(key, new MethodRecord(m));
    methodsStatistics.get(key).markNP(n, np);
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
    public int numNPStatements;
    public int numNPExpressions;
    public List<String> nps = new ArrayList<>();
    public int numParameters;
    public int numStatements;
    public int numExpressions;

    public MethodRecord(MethodDeclaration m) {
      methodName = m.getName() + "";
      methodClassName = findTypeAncestor(m);
      numParameters = m.parameters().size();
      numStatements = metrics.countStatements(m);
      numExpressions = metrics.countExpressions(m);
    }

    /** @param np */
    public void markNP(ASTNode n, String np) {
      numNPStatements += metrics.countStatements(n);
      numNPExpressions += metrics.countExpressions(n);
      nps.add(np);
    }
  }

  /** @param cu */
  public static void logCompilationUnit(ASTNode cu) {
    numMethods += metrics.countMethods(cu);
  }
}