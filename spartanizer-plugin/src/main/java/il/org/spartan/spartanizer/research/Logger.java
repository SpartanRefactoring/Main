package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.ast.navigate.step.name;
import static il.org.spartan.spartanizer.ast.navigate.step.parametersTypes;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import il.org.spartan.spartanizer.ast.navigate.descendants;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.java.haz;
import il.org.spartan.spartanizer.utils.tdd.enumerate;

/** The purpose of this class is to gather information about NPs and summarize
 * it, so we can submit nice papers and win eternal fame.
 * <p>
 * Whenever an NP is matched it should log itself.
 * @author Ori Marcovitch
 * @since 2016 */
public final class Logger {
  public static int numMethods;
  private static String currentFile;
  private static Stack<AbstractTypeDeclaration> currentType = new Stack<>();
  private static final Collection<BiConsumer<ASTNode, String>> subscribers = an.empty.list();

  /** Suppresses default constructor, ensuring non-instantiability */
  private Logger() {}
  /** subscribe to logNP. Every time an NP will hit, the subscriber will be
   * invoked.
   * @param ¢ */
  public static void subscribe(final BiConsumer<ASTNode, String> ¢) {
    subscribers.add(¢);
  }

  public static final Map<Integer, MethodRecord> methodsStatistics = new HashMap<>();

  private static void logMethodInfo(final MethodDeclaration ¢) {
    methodsStatistics.putIfAbsent(hashMethod(¢), new MethodRecord(¢));
  }
  public static void reset() {
    methodsStatistics.clear();
    numMethods = 0;
  }
  public static void logNP(final ASTNode n, final String np) {
    subscribers.forEach(λ -> λ.accept(n, np));
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
    descendants.whoseClassIs(AbstractTypeDeclaration.class).from(¢).stream().filter(haz::methods).forEach(Logger::logType);
  }
  /** Collect statistics of a compilation unit which will be analyzed.
   * @param u compilation unit */
  public static void logType(final AbstractTypeDeclaration d) {
    currentType.push(d);
    final List<MethodDeclaration> ms = step.methods(d).stream().filter(λ -> enumerate.statements(λ) != 0 && !λ.isConstructor()).collect(toList());
    ms.forEach(Logger::logMethodInfo);
    numMethods += ms.size();
  }
  /** Collect statistics of a compilation unit which will be analyzed.
   * @param ¢ compilation unit */
  public static void logFile(final String fileName) {
    currentFile = fileName;
  }
  public static void finishedType() {
    currentType.pop();
  }
}