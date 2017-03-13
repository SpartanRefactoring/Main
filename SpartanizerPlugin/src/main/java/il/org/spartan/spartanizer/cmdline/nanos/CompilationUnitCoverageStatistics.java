package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;
import java.util.function.*;

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

  public double nodesCoverage() {
    return format.perc(nodesCovered(), nodes());
  }

  public double commandsCoverage() {
    return format.perc(commandsCovered(), commands());
  }

  public double expressionsCoverage() {
    return format.perc(expressionsCovered(), expressions());
  }

  public int nodes() {
    return stream().mapToInt(λ -> λ.nodes.afterSpartanization).sum();
  }

  public int commands() {
    return stream().mapToInt(λ -> λ.commands.afterSpartanization).sum();
  }

  public int expressions() {
    return stream().mapToInt(λ -> λ.expressions.afterSpartanization).sum();
  }

  private int nodesCovered() {
    return stream().mapToInt(λ -> λ.nodes.covered()).sum();
  }

  private int commandsCovered() {
    return stream().mapToInt(λ -> λ.commands.covered()).sum();
  }

  private int expressionsCovered() {
    return stream().mapToInt(λ -> λ.expressions.covered()).sum();
  }

  public int touched() {
    return stream().mapToInt(CompilationUnitRecord::methodsTouched).sum();
  }

  public int methodsCovered() {
    return stream().mapToInt(CompilationUnitRecord::methodsCovered).sum();
  }

  public int methods() {
    return stream().mapToInt(λ -> λ.methods.size()).sum();
  }
}

class CompilationUnitRecord {
  class ElementCounter {
    @SuppressWarnings("unused") private int beforeSpartanization;
    public int afterSpartanization;
    private int coveredByNonMethods;
    private Function<ASTNode, Integer> count;
    private Function<LightWeightMethodRecord, Integer> np;

    public ElementCounter(final Function<ASTNode, Integer> count, Function<LightWeightMethodRecord, Integer> np) {
      this.count = count;
      this.np = np;
    }

    public void before(final CompilationUnit ¢) {
      beforeSpartanization = count(¢);
    }

    private int count(final ASTNode ¢) {
      return count.apply(¢).intValue();
    }

    private int np(final LightWeightMethodRecord λ) {
      return np.apply(λ).intValue();
    }

    public void after(final CompilationUnit ¢) {
      afterSpartanization = count(¢);
    }

    private int coveredByMethods() {
      return methods.values().stream().mapToInt(λ -> np(λ)).sum();
    }

    public int covered() {
      return coveredByMethods() + coveredByNonMethods;
    }

    public void mark(final ASTNode ¢) {
      coveredByNonMethods += count(¢);
    }
  }

  final ElementCounter nodes = new ElementCounter(count::nodes, λ -> λ.nodes.np());
  final ElementCounter commands = new ElementCounter(measure::commands, λ -> λ.commands.np());
  final ElementCounter expressions = new ElementCounter(measure::expressions, λ -> λ.expressions.np());
  public final Map<String, LightWeightMethodRecord> methods = new HashMap<>();

  public CompilationUnitRecord(final CompilationUnit cu) {
    nodes.before(cu);
    commands.before(cu);
    expressions.before(cu);
    fetchAllmethods(cu);
  }

  private void fetchAllmethods(final CompilationUnit u) {
    descendants.whoseClassIs(MethodDeclaration.class).from(u).forEach(//
        λ -> methods.put(Vocabulary.mangle(λ), new LightWeightMethodRecord(λ)));
  }

  public void logAfterSpartanization(final CompilationUnit ¢) {
    nodes.after(¢);
    commands.after(¢);
    expressions.after(¢);
  }

  public int methodsTouched() {
    return (int) methods.values().stream().filter(LightWeightMethodRecord::touched).count();
  }

  public int methodsCovered() {
    return (int) methods.values().stream().filter(LightWeightMethodRecord::fullyCovered).count();
  }

  public void markContainedInMethod(final MethodDeclaration ¢, final ASTNode n) {
    final String mangle = Vocabulary.mangle(¢);
    methods.putIfAbsent(mangle, new LightWeightMethodRecord(¢));
    methods.get(mangle).mark(n);
  }

  public void markNP(final ASTNode n, @SuppressWarnings("unused") final String np) {
    final MethodDeclaration $ = ancestorMethod(n);
    if ($ == null)
      markRegular(n);
    else
      markContainedInMethod($, n);
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
    nodes.mark(¢);
    commands.mark(¢);
    expressions.mark(¢);
  }
}

class LightWeightMethodRecord {
  final NanoPatternCounter nodes;
  final NanoPatternCounter commands;
  final NanoPatternCounter expressions;
  private boolean fullyCovered;

  public LightWeightMethodRecord(final MethodDeclaration ¢) {
    nodes = NanoPatternCounter.init(count.nodes(¢));
    commands = NanoPatternCounter.init(measure.commands(¢));
    expressions = NanoPatternCounter.init(measure.expressions(¢));
  }

  /** makes sure we don't exceed 100% of nodes of a method */
  public void mark(final ASTNode ¢) {
    nodes.incAndGet(count.nodes(¢));
    commands.incAndGet(measure.commands(¢));
    expressions.incAndGet(measure.expressions(¢));
    if (iz.methodDeclaration(¢))
      fullyCovered = true;
  }

  public boolean touched() {
    return nodes.np().intValue() > 0 && !fullyCovered;
  }

  public boolean fullyCovered() {
    return fullyCovered;
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

    public Integer np() {
      return Integer.valueOf(np);
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
