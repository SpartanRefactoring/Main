package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.*;
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
  public int nodesCovered;
  public Map<String, LightWeightMethodRecord> methods = new HashMap<>();
  public final Set<String> nps = new HashSet<>();

  public CompilationUnitRecord(final CompilationUnit cu) {
    nodesBeforeSpartanization = count.nodes(cu);
  }

  public void logAfterSpartanization(final CompilationUnit ¢) {
    nodesAfterSpartanization = count.nodes(¢);
  }

  public boolean touched() {
    return !nps.isEmpty();
  }

  public void markContainedInMethod(final MethodDeclaration ¢, final ASTNode n) {
    String mangle = Vocabulary.mangle(¢);
    methods.putIfAbsent(mangle, new LightWeightMethodRecord(¢));
    nodesCovered += methods.get(mangle).logAndGet(count.nodes(n));
  }

  public void markNP(final ASTNode n, final String np) {
    MethodDeclaration $ = ancestorMethod(n);
    if ($ == null)
      markRegular(n);
    else
      markContainedInMethod($, n);
    nps.add(np);
  }

  private static MethodDeclaration ancestorMethod(final ASTNode ¢) {
    for (ASTNode $ = ¢; $ != null; $ = parent($)) {
      if (iz.methodDeclaration($))
        return az.methodDeclaration($);
      if (iz.typeDeclaration($))
        break;
    }
    return null;
  }

  private void markRegular(ASTNode ¢) {
    nodesCovered += count.nodes(¢);
  }

  public int nodesCovered() {
    return Math.min(nodesCovered, nodesAfterSpartanization);
  }

  class LightWeightMethodRecord {
    private final int numNodes;
    private int numNPNodes;

    public LightWeightMethodRecord(MethodDeclaration ¢) {
      numNodes = count.nodes(¢);
    }

    /** makes sure we don't exceed 100% of nodes of a method */
    public int logAndGet(int nodes) {
      final int $ = Math.min(nodes, numNodes - numNPNodes);
      numNPNodes += $;
      return $;
    }
  }
}
