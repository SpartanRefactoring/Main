package il.org.spartan.spartanizer.research;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** The purpose of this class is to gather information about NPs and summarize
 * it, so we can submit nice papers and win eternal fame.
 * <p>
 * Whenever an NP is matched it should log itself.
 * @author Ori Marcovitch
 * @since 2016 */
public class Logger {
  private static int numMethods;
  private static String currentFile;
  private static Stack<AbstractTypeDeclaration> currentType = new Stack<>();
  private static final List<BiConsumer<ASTNode, String>> subscribers = new ArrayList<>();

  public static void summarize(final String outputDir) {
    summarizeMethodStatistics(outputDir);
    reset();
  }

  /** subscribe to logNP. Every time an NP will hit, the subscriber will be
   * invoked.
   * @param ¢ */
  public static void subscribe(final BiConsumer<ASTNode, String> ¢) {
    subscribers.add(¢);
  }

  private static final Map<Integer, MethodRecord> methodsStatistics = new HashMap<>();

  private static void logMethodInfo(final MethodDeclaration ¢) {
    methodsStatistics.putIfAbsent(hashMethod(¢), new MethodRecord(¢));
  }

  private static void reset() {
    methodsStatistics.clear();
    numMethods = 0;
  }

  private static void summarizeMethodStatistics(final String outputDir) {
    final CSVStatistics report = openMethodSummaryFile(outputDir);
    if (report == null)
      return;
    double sumSratio = 0, sumEratio = 0;
    for (final Integer k : methodsStatistics.keySet()) {
      final MethodRecord m = methodsStatistics.get(k);
      report //
          .put("Name", m.methodClassName + "~" + m.methodName) //
          .put("#Statement", m.numStatements) //
          .put("#NP Statements", m.numNPStatements) //
          .put("Statement ratio", m.numStatements == 0 ? 1 : min(1, safe.div(m.numNPStatements, m.numStatements))) //
          .put("#Expressions", m.numExpressions) //
          .put("#NP expressions", m.numNPExpressions) //
          .put("Expression ratio", m.numExpressions == 0 ? 1 : min(1, safe.div(m.numNPExpressions, m.numExpressions))) //
          .put("#Parameters", m.numParameters) //
          .put("#NP", m.nps.size()) //
      ;
      report.nl();
      sumSratio += m.numStatements == 0 ? 1 : min(1, safe.div(m.numNPStatements, m.numStatements));
      sumEratio += m.numExpressions == 0 ? 1 : min(1, safe.div(m.numNPExpressions, m.numExpressions));
    }
    System.out.println("Total methods: " + numMethods);
    System.out.println("Average statement ratio: " + safe.div(sumSratio, numMethods));
    System.out.println("Average Expression ratio: " + safe.div(sumEratio, numMethods));
    report.close();
  }

  public static CSVStatistics openMethodSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/methodStatistics");
  }

  public static CSVStatistics openNPSummaryFile(final String outputDir) {
    return openSummaryFile(outputDir + "/npStatistics.csv");
  }

  public static CSVStatistics openSummaryFile(final String $) {
    try {
      return new CSVStatistics($, "property");
    } catch (final IOException ¢) {
      monitor.infoIOException(¢, "opening report file");
      return null;
    }
  }

  public static void logNP(final ASTNode n, final String np) {
    subscribers.forEach(¢ -> ¢.accept(n, np));
  }

  private static Integer hashMethod(final MethodDeclaration ¢) {
    return Integer.valueOf((currentFile + "." + getType() + name(¢) + parametersTypes(¢)).hashCode());
  }

  private static String getType() {
    return currentType == null || currentType.isEmpty() ? "" : currentType.peek() + "";
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param ¢ compilation unit */
  public static void logCompilationUnit(final CompilationUnit ¢) {
    currentType = new Stack<>();
    searchDescendants.forClass(AbstractTypeDeclaration.class).from(¢).stream().filter(haz::methods).forEach(Logger::logType);
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param u compilation unit */
  public static void logType(final AbstractTypeDeclaration d) {
    currentType.push(d);
    final List<MethodDeclaration> ms = step.methods(d).stream().filter(m -> enumerate.statements(m) != 0 && !m.isConstructor())
        .collect(Collectors.toList());
    ms.forEach(Logger::logMethodInfo);
    numMethods += ms.size();
  }

  /** Collect statistics of a compilation unit which will be analyzed.
   * @param ¢ compilation unit */
  public static void logFile(final String fileName) {
    currentFile = fileName;
  }

  private static double min(final double a, final double d) {
    return a < d ? a : d;
  }

  public static void finishedType() {
    currentType.pop();
  }
}