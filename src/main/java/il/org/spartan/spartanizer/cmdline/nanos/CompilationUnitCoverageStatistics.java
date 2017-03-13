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
  public final int nodesBeforeSpartanization;
  public int nodesAfterSpartanization;
  public int nodesCoveredByNonMethods;
  public final Map<String, LightWeightMethodRecord> methods = new HashMap<>();
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

  private int nodesCoveredByMethods() {
    return methods.values().stream().mapToInt(λ -> λ.nodes.np()).sum();
  }

  public void markContainedInMethod(final MethodDeclaration ¢, final ASTNode n) {
    final String mangle = Vocabulary.mangle(¢);
    methods.putIfAbsent(mangle, new LightWeightMethodRecord(¢));
    methods.get(mangle).logAndGet(n);
  }

  public void markNP(final ASTNode n, final String np) {
    final MethodDeclaration $ = ancestorMethod(n);
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

  private void markRegular(final ASTNode ¢) {
    nodesCoveredByNonMethods += count.nodes(¢);
  }

  public int nodesCovered() {
    return Math.min(nodesCoveredByNonMethods + nodesCoveredByMethods(), nodesAfterSpartanization);
  }

  class LightWeightMethodRecord {
    final NanoPatternCounter nodes;
    private final NanoPatternCounter commands;
    private final NanoPatternCounter expressions;

    public LightWeightMethodRecord(final MethodDeclaration ¢) {
      nodes = NanoPatternCounter.init(count.nodes(¢));
      commands = NanoPatternCounter.init(measure.commands(¢));
      expressions = NanoPatternCounter.init(measure.expressions(¢));
    }

    /** makes sure we don't exceed 100% of nodes of a method */
    public void logAndGet(final ASTNode ¢) {
      nodes.incAndGet(count.nodes(¢));
      commands.incAndGet(measure.commands(¢));
      expressions.incAndGet(measure.expressions(¢));
    }

    public boolean touched() {
      return nodes.np() > 0;
    }
  }

  static class NanoPatternCounter {
    private final int total;
    private int np;

    static NanoPatternCounter init(int ¢) {
      return new NanoPatternCounter(¢);
    }

    public int total() {
      return total;
    }

    public int np() {
      return np;
    }

    private NanoPatternCounter(int num) {
      this.total = num;
    }

    public int incAndGet(int amount) {
      final int $ = Math.min(amount, total - np);
      np += $;
      return $;
    }
  }
}
