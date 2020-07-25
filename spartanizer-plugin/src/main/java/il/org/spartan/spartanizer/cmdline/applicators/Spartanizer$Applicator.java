package il.org.spartan.spartanizer.cmdline.applicators;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import fluent.ly.English;
import fluent.ly.as;
import fluent.ly.note;
import il.org.spartan.CSVStatistics;
import il.org.spartan.collections.ChainStringToIntFunctionegerMap;
import il.org.spartan.spartanizer.ast.factory.makeAST;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.cmdline.CommandLineSelection;
import il.org.spartan.spartanizer.cmdline.library.FileHeuristics;
import il.org.spartan.spartanizer.cmdline.runnables.ReportGenerator;
import il.org.spartan.spartanizer.plugin.AbstractSelection;
import il.org.spartan.spartanizer.plugin.WrappedCompilationUnit;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.tipping.TraversalMonitor;
import il.org.spartan.spartanizer.traversal.DispatchingVisitor;
import il.org.spartan.spartanizer.traversal.Toolbox;
import il.org.spartan.spartanizer.traversal.disabling;

/** TODO Matteo Orru' please add a description
 * @author Matteo Orru'
 * @since 2016 */
public class Spartanizer$Applicator extends GenericApplicator {
  CSVStatistics spectrumStats;
  final ChainStringToIntFunctionegerMap spectrum = new ChainStringToIntFunctionegerMap();

  /** Instantiates this class */
  public Spartanizer$Applicator() {
    this(il.org.spartan.spartanizer.traversal.Toolbox.full());
  }
  public Spartanizer$Applicator(final Toolbox toolbox) {
    this.toolbox = toolbox;
  }
  /** Apply the spartanization to a selection of CompilationUnits
   * @param u
   * @param forTrueConditionRemove
   * @return */
  public boolean apply(final AbstractSelection<?> __) {
    final List<WrappedCompilationUnit> list = ((CommandLineSelection) __).get();
    for (final WrappedCompilationUnit w : list) {
      assert w != null;
      assert w.compilationUnit != null;
      System.out.println(w.compilationUnit);
      w.compilationUnit.accept(new ASTVisitor(true) {
        @Override public boolean preVisit2(final ASTNode ¢) {
          return !selectedNodeTypes.contains(¢.getClass()) || go(¢); // ||
                                                                     // !filter(¢)
        }
      });
    }
    return false;
  }
  /** Apply the spartanization to a single CompilationUnit
   * @param u
   * @param forTrueConditionRemove
   * @return
   * @author matteo */
  @SuppressWarnings("unused") public boolean apply(final WrappedCompilationUnit u, final AbstractSelection<?> __) {
    go(u.compilationUnit);
    return false;
  }
  void go(final CompilationUnit u) {
    u.accept(new ASTVisitor(true) {
      @Override public boolean preVisit2(final ASTNode ¢) {
        System.out.println("!selectedNodeTypes.contains(¢.getClass()): " + !selectedNodeTypes.contains(¢.getClass()));
        // System.out.println("!filter(¢): " + !filter(¢));
        System.out.println("¢.getClass(): " + ¢.getClass());
        return !selectedNodeTypes.contains(¢.getClass()) || go(¢); // ||
                                                                   // !filter(¢)
      }
    });
  }
  boolean go(final ASTNode input) {
    tippersAppliedOnCurrentObject = 0;
    final String output = fixedPoint(input + "");
    final ASTNode outputASTNode = makeAST.COMPILATION_UNIT.from(output); // makeAST.CLASS_BODY_DECLARATIONS.from(output);
    ReportGenerator.printFile(input + "", "before");
    ReportGenerator.printFile(output, "after");
    computeMetrics(input, outputASTNode);
    return false;
  }
  protected void computeMetrics(final ASTNode input, final ASTNode output) {
    System.err.println(++done + " " + extract.category(input) + " " + extract.name(input));
    ReportGenerator.summaryFileName("metrics");
    ReportGenerator.name(input);
    ReportGenerator.writeMetrics(input, output, null);
    ReportGenerator.write(input, output, "Δ ", (n1, n2) -> (n1 - n2));
    ReportGenerator.write(input, output, "δ ", FileHeuristics::d);
    ReportGenerator.writePerc(input, output, "δ ");
    // Reports.writeRatio(input, output, "", (n1,n2)->(n1/n2));
    ReportGenerator.nl("metrics");
  }
  private String fixedPoint(final String from) {
    for (final IDocument $ = new Document(from);;) {
      final TextEdit e = createRewrite((CompilationUnit) makeAST.COMPILATION_UNIT.from($.get())).rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
        note.bug(this, ¢);
        throw new AssertionError(¢);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }
  /** Rewrite CompilationUnit
   * @param ¢
   * @return */
  public ASTRewrite createRewrite(final CompilationUnit ¢) {
    final ASTRewrite $ = ASTRewrite.create(¢.getAST());
    consolidateTips($, ¢);
    return $;
  }
  /** ConsolidateTips on CompilationUnit
   * @param r
   * @param u */
  public void consolidateTips(final ASTRewrite r, final CompilationUnit u) {
    toolbox = il.org.spartan.spartanizer.traversal.Toolbox.full();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        TraversalMonitor.visitation(n);
        if (disabling.on(n))
          return true;
        Tipper<N> tipper = null;
        try {
          tipper = getTipper(n);
        } catch (final Exception ¢) {
          note.ignore(this, ¢);
        }
        if (tipper == null)
          return true;
        Tip s = null;
        try {
          s = tipper.tip(n);
          tick(n, tipper);
        } catch (final Exception ¢) {
          note.ignore(this, ¢);
        }
        if (s == null)
          return true;
        ++tippersAppliedOnCurrentObject;
        TraversalMonitor.rewrite(r, s);
        return true;
      }
      <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
        return toolbox.firstTipper(¢);
      }
      <N extends ASTNode> void tick(final N n, final Tipper<N> w) {
        tick(w);
        TraversalMonitor.tip(w, n);
      }
      <N extends ASTNode> void tick(final Tipper<N> w) {
        final String key = English.name(w.getClass());
        if (!spectrum.containsKey(key))
          spectrum.put(key, 0);
        spectrum.put(key, spectrum.get(key) + 1);
      }
      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }
  static boolean filter(@SuppressWarnings("unused") final ASTNode __) {
    return false;
  }
  @SafeVarargs @SuppressWarnings("static-method") public final void selectedNodes(final Class<? extends BodyDeclaration>... ¢) {
    selectedNodeTypes = as.list(¢);
  }
}
