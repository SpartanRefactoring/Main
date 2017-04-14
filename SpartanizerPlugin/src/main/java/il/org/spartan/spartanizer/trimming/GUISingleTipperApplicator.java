package il.org.spartan.spartanizer.trimming;

import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** An adapter that converts the protocol of a single @{link Tipper} instance
 * into that of {@link GUIConfigurationApplicator}. This class must eventually
 * die.
 * @author Yossi Gil
 * @since 2015/07/25 */
public final class GUISingleTipperApplicator extends GUIConfigurationApplicator {
  public final Tipper<ASTNode> tipper;
  public final Class<? extends ASTNode> clazz;

  /** Instantiates this class
   * @param statementsTipper The tipper we wish to convert
   * @param name The title of the refactoring */
  @SuppressWarnings("unchecked") public GUISingleTipperApplicator(final Tipper<? extends ASTNode> w) {
    super(w.technicalName());
    tipper = (Tipper<ASTNode>) w;
    clazz = w.myActualOperandsClass();
    // assert clazz != null : "Oops, cannot find kind of operands of " +
    // w.technicalName();
  }

  @Override protected ASTRewrite computeMaximalRewrite(final CompilationUnit u, final IMarker m, final Consumer<ASTNode> n) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    final ASTVisitor visitor = new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        super.preVisit(¢);
        if (¢.getClass() != clazz && !tipper.check(¢) && !inRange(m, ¢))
          return;
        tipper.tip(¢).go($, null);
        if (n != null)
          n.accept(¢);
      }
    };
    u.accept(visitor);
    return $;
  }

  @Override protected ASTVisitor tipsCollector(final Tips $) {
    return new ASTVisitor(true) {
      @Override public void preVisit(final ASTNode ¢) {
        super.preVisit(¢);
        progressMonitor().worked(1);
        if (¢.getClass() == clazz)
          return;
        progressMonitor().worked(1);
        if (!tipper.check(¢))
          return;
        progressMonitor().worked(1);
        $.add(tipper.tip(¢));
      }
    };
  }

  public ASTRewrite createRewrite(final CompilationUnit ¢) {
    return rewriterOf(¢, null, new Int());
  }

  private ASTRewrite rewriterOf(final CompilationUnit u, final IMarker m, final Int counter) {
    note.logger.fine("Weaving maximal rewrite of " + u);
    progressMonitor.beginTask("Weaving maximal rewrite ...", IProgressMonitor.UNKNOWN);
    final Int count = new Int();
    final ASTRewrite $ = computeMaximalRewrite(u, m, __ -> count.step());
    counter.add(count);
    progressMonitor.done();
    return $;
  }

  /** creates an ASTRewrite which contains the changes
   * @param u the Compilation Unit (outermost ASTNode in the Java Grammar)
   * @param m a progress monitor in which the progress of the refactoring is
   *        displayed
   * @return an ASTRewrite which contains the changes */
  public ASTRewrite createRewrite(final CompilationUnit ¢, final Int counter) {
    return rewriterOf(¢, null, counter);
  }

  String compilationUnitName() {
    return iCompilationUnit.getElementName();
  }

  /** creates an ASTRewrite, under the context of a text marker, which contains
   * the changes
   * @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker
   * @return an ASTRewrite which contains the changes */
  ASTRewrite createRewrite(final IMarker ¢) {
    return rewriterOf((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢, progressMonitor), ¢, new Int());
  }
}
