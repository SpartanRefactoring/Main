package il.org.spartan.spartanizer.cmdline.nanos;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.characteristics.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import nano.ly.*;

/** Collects statistics about {@link CompilationUnit}s NanoPatterns coverage
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-07 */
public class CompilationUnitCoverageStatistics extends ArrayList<CompilationUnitRecord> {
  private static final long serialVersionUID = -0x648959E432A36208L;

  public void logCompilationUnit(final CompilationUnit ¢) {
    add(new CompilationUnitRecord(¢));
  }

  public void logAfterSpartanization(final CompilationUnit ¢) {
    the.lastOf(this).logAfterSpartanization(¢);
  }

  public void markNP(final ASTNode n, final String np) {
    the.lastOf(this).markNP(n, np);
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

  public double touched() {
    return format.perc(stream().mapToInt(CompilationUnitRecord::methodsTouched).sum(), methods() - methodsCovered());
  }

  public double methodsCoverage() {
    return format.perc(methodsCovered(), methods());
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

  private int methodsCovered() {
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
    private final Function<ASTNode, Integer> count;
    private final Function<LightWeightMethodRecord, Integer> np;

    public ElementCounter(final Function<ASTNode, Integer> count, final Function<LightWeightMethodRecord, Integer> np) {
      this.count = count;
      this.np = np;
    }

    public void before(final CompilationUnit ¢) {
      beforeSpartanization = count(¢);
    }

    private int count(final ASTNode ¢) {
      return count.apply(¢).intValue();
    }

    private int count(final CompilationUnit ¢) {
      return count.apply(¢).intValue();
    }

    private int np(final LightWeightMethodRecord λ) {
      return np.apply(λ).intValue();
    }

    public void after(final CompilationUnit ¢) {
      afterSpartanization = count(¢);
    }

    private int coveredByMethods() {
      return methods.values().stream().mapToInt(this::np).sum();
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
  }

  private void fetchAllmethods(final CompilationUnit u) {
    descendants.whoseClassIs(MethodDeclaration.class).from(u).forEach(//
        λ -> methods.put(Vocabulary.mangle(λ), new LightWeightMethodRecord(λ)));
  }

  public void logAfterSpartanization(final CompilationUnit ¢) {
    nodes.after(¢);
    commands.after(¢);
    expressions.after(¢);
    fetchAllmethods(¢);
  }

  public int methodsTouched() {
    return (int) methods.values().stream().filter(LightWeightMethodRecord::touched).count();
  }

  public int methodsCovered() {
    return (int) methods.values().stream().filter(LightWeightMethodRecord::fullyCovered).count();
  }

  public void markContainedInMethod(final MethodDeclaration ¢, final ASTNode n, final String np) {
    final String mangle = Vocabulary.mangle(¢);
    methods.putIfAbsent(mangle, new LightWeightMethodRecord(¢));
    methods.get(mangle).mark(n, np);
  }

  public void markNP(final ASTNode n, final String np) {
    final MethodDeclaration $ = ancestorMethod(n);
    if ($ == null)
      markRegular(n);
    else
      markContainedInMethod($, n, np);
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
  public void mark(final ASTNode ¢, final String np) {
    if (np.equals(LetInNext.class.getSimpleName()))
      markLetItBeIn(¢);
    else if (np.equals(MyArguments.class.getSimpleName()))
      markArgumentsTuple();
    else {
      mark(¢);
      if (iz.methodDeclaration(¢))
        fullyCovered = true;
    }
  }

  private void markArgumentsTuple() {
    nodes.inc(4);
    expressions.inc(4);
  }

  private void markLetItBeIn(final ASTNode ¢) {
    mark(extract.nextStatement(¢));
    mark(¢);
  }

  private void mark(final ASTNode ¢) {
    nodes.inc(count.nodes(¢));
    commands.inc(measure.commands(¢));
    expressions.inc(measure.expressions(¢));
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

    static NanoPatternCounter init(final int ¢) {
      return new NanoPatternCounter(¢);
    }

    public int total() {
      return total;
    }

    public Integer np() {
      return Integer.valueOf(np);
    }

    private NanoPatternCounter(final int num) {
      total = num;
    }

    public int inc(final int amount) {
      final int $ = Math.min(amount, total - np);
      np += $;
      return $;
    }
  }
}
