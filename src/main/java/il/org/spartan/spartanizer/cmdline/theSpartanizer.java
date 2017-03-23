package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** simple no-gimmicks singleton service that does the simple job of applying a
 * {@link Toolbox} {@link #once(String)}, {@link #twice(String)},
 * {@link #thrice(String)}, and {@link #repetitively(String)}.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-03-08 */
public interface theSpartanizer {
  static Tipper<?> firstTipper(final String from) {
    final Wrapper<Tipper<?>> $ = new Wrapper<>();
    final ASTNode n = wizard.ast(from);
    if (n != null)
      n.accept(new DispatchingVisitor() {
        @Override protected <N extends ASTNode> boolean go(final N ¢) {
          return searching && go(toolbox.firstTipper(¢));
        }

        <N extends ASTNode> boolean go(final Tipper<N> ¢) {
          if (¢ == null)
            return true;
          $.set(¢);
          return searching = false;
        }

        boolean searching = true;
      });
    return $.get();
  }

  /** Apply trimming once
   * @param from what to process
   * @return trimmed text, or null in case of error or no more applicable
   *         tippers */
  @SuppressWarnings("hiding") static String once(final String from) {
    final Trimmer trimmer = new Trimmer(toolbox);
    final IDocument $ = new Document(from);
    final ASTNode root = wizard.ast(from);
    if (root != null)
      root.accept(new DispatchingVisitor() {
        @Override protected <N extends ASTNode> boolean go(final N n) {
          if (!searching)
            return false;
          final Tipper<N> t = safeFirstTipper(n);
          if (t == null)
            return true;
          final Tip $ = t.tip(n);
          if ($ == null)
            return true;
          apply($, n);
          return searching = false;
        }

        <N extends ASTNode> void apply(final Tip t, final N n) {
          final ASTRewrite r = ASTRewrite.create(n.getAST());
          t.go(r, null);
          final TextEdit e = r.rewriteAST($, null);
          try {
            e.apply($);
          } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
            monitor.logEvaluationError(trimmer, ¢);
          }
        }

        boolean searching = true;
      });
    return $.get();
  }

  static String repetitively(final String from) {
    int n = 0;
    for (String $ = from, next;; $ = next) {
      next = once($);
      if (same($, next) || ++n > 20)
        return $;
    }
  }

  static <N extends ASTNode> Tipper<N> safeFirstTipper(final N $) {
    try {
      return toolbox.firstTipper($);
    } catch (final Exception ¢) {
      return monitor.logProbableBug(¢);
    }
  }

  static boolean same(final String s1, final String s2) {
    return s1 == null || s2 == null || s2.equals(s1) || trivia.essence(s1).equals(trivia.essence(s2));
  }

  static String thrice(final String javaCode) {
    return once(twice(javaCode));
  }

  static String twice(final String javaCode) {
    return once(once(javaCode));
  }

  Toolbox toolbox = Toolbox.defaultInstance();
}
