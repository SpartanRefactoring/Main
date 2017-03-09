package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** simple no-gimmicks singleton serives that does the simple job of applying a
 * spartanizer once.
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-08 */
public interface theSpartanizer {
  Toolbox toolbox = Toolbox.defaultInstance();

  static <N extends ASTNode> Tipper<N> safeFirstTipper(final N ¢) {
    try {
      return toolbox.firstTipper(¢);
    } catch (final Exception $) {
      return monitor.debug(theSpartanizer.class, $);
    }
  }

  static String fixedPoint(final String from) {
    int n = 0;
    for (String $ = from, next = from;; next = once(from)) {
      if (next == null)
        return $;
      if (++n > 20) {
        monitor.debug($);
        return $;
      }
    }
  }

  /** Apply trimming once
   * @param from what to process
   * @return trimmed text, or null in case of error or no more applicable
   *         tippers */
  static String once(final String from) {
    final Trimmer trimmer = new Trimmer(toolbox);
    final IDocument $ = new Document(from);
    wizard.ast(from).accept(new DispatchingVisitor() {
      boolean done;

      @SuppressWarnings("hiding") @Override protected <N extends ASTNode> boolean go(final N n) {
        if (done)
          return false;
        final Tipper<N> t = safeFirstTipper(n);
        if (t == null)
          return true;
        final Tip $ = t.tip(n);
        if ($ == null)
          return true;
        done = true;
        return apply($, n);
      }

      <N extends ASTNode> boolean apply(final Tip t, final N n) {
        final ASTRewrite r = ASTRewrite.create(n.getAST());
        t.go(r, null);
        final TextEdit e = r.rewriteAST($, null);
        try {
          e.apply($);
        } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
          monitor.logEvaluationError(trimmer, ¢);
        }
        return false;
      }
    });
    return from.equals($.get()) ? null : $.get();
  }
}
