package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.util.*;

/** Collects statistics about {@link CompilationUnit}s NanoPatterns coverage
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-07 */
public class CompilationUnitCoverageStatistics extends ArrayList<CompilationUnitRecord> {
  private static final long serialVersionUID = -7244420312239137288L;

  public void logCompilationUnit(final CompilationUnit ¢) {
    add(new CompilationUnitRecord(¢));
  }

  public void logAfterSpartanization(final CompilationUnit ¢) {
    last(this).logAfterSpartanization(¢);
  }

  public void markNP(final ASTNode n, final String np) {
    last(this).markNP(n, np);
  }

  public double covergae() {
    return format.perc(nodesCovered(), nodes());
  }

  public int nodes() {
    return stream().mapToInt(λ -> λ.nodesAfterSpartanization).sum();
  }

  private int nodesCovered() {
    return stream().mapToInt(CompilationUnitRecord::nodesCovered).sum();
  }
}

class CompilationUnitRecord {
  public int nodesBeforeSpartanization;
  public int nodesAfterSpartanization;
  public Map<String, MethodRecord> nodesCovered = new HashMap<>();
  public final Set<String> nps = new HashSet<>();
  private String currentMethod;

  public CompilationUnitRecord(final CompilationUnit cu) {
    nodesBeforeSpartanization = count.nodes(cu);
  }

  public void logAfterSpartanization(final CompilationUnit ¢) {
    nodesAfterSpartanization = count.nodes(¢);
  }

  public boolean touched() {
    return !nps.isEmpty();
  }

  public void logMethod(final MethodDeclaration ¢) {
    currentMethod = identifier(name(¢)) + "$" + arity(¢);
    nodesCovered.putIfAbsent(currentMethod, new MethodRecord(¢));
  }

  public void markNP(final ASTNode n, final String np) {
    nodesCovered.get(currentMethod).markNP(n, np);
    nps.add(np);
  }

  public int nodesCovered() {
    return nodesCovered.values().stream().mapToInt(MethodRecord::numNPNodes).sum();
  }
}
