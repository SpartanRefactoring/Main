package il.org.spartan.spartanizer.dispatch;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

/** An adapter that converts the protocol of a single @{link Tipper} instance
 * into that of {@link AbstractGUIApplicator}. This class must eventually die.
 * @author Yossi Gil
 * @since 2015/07/25 */
public final class TipperApplicator extends AbstractGUIApplicator {
  final Tipper<ASTNode> tipper;
  final Class<? extends ASTNode> clazz;

  /** Instantiates this class
   * @param tipper The tipper we wish to convert
   * @param name The title of the refactoring */
  @SuppressWarnings("unchecked") public TipperApplicator(final Tipper<? extends ASTNode> w) {
    super(w.myName());
    tipper = (Tipper<ASTNode>) w;
    clazz = w.myActualOperandsClass();
    assert clazz != null : "Oops, cannot find kind of operands of " + w.myName();
  }

  @Override protected void consolidateTips(final ASTRewrite r, final CompilationUnit u, final IMarker m,
      @SuppressWarnings("unused") final AtomicInteger __) {
    u.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        super.preVisit(¢);
        if (¢.getClass() == clazz || tipper.canTip(¢) || inRange(m, ¢))
          tipper.tip(¢).go(r, null);
      }
    });
  }

  @Override protected ASTVisitor makeTipsCollector(final List<Tip> $) {
    return new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        super.preVisit(¢);
        progressMonitor.worked(1);
        if (¢.getClass() == clazz)
          return;
        progressMonitor.worked(1);
        if (!tipper.canTip(¢))
          return;
        progressMonitor.worked(1);
        $.add(tipper.tip(¢));
      }
    };
  }
}
