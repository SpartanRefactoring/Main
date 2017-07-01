package il.org.spartan.spartanizer.cmdline.good;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.traversal.*;

/** Simple no-gimmicks singleton service that does the simple job of applying a
 * {@link Toolbox} {@link #once(String)}, {@link #twice(String)},
 * {@link #thrice(String)}, and {@link #repetitively(String)}.
 * @author Yossi Gil
 * @since 2017-03-08 */
public interface theSpartanizer {
  static Tipper<?> firstTipper(final String from) {
    final Wrapper<Tipper<?>> ret = new Wrapper<>();
    final ASTNode n = make.ast(from);
    if (n != null)
      n.accept(new DispatchingVisitor() {
        @Override protected <N extends ASTNode> boolean go(final N ¢) {
          return searching && go(traversal.toolbox.firstTipper(¢));
        }
        <N extends ASTNode> boolean go(final Tipper<N> ¢) {
          if (¢ == null)
            return true;
          ret.set(¢);
          return false;
        }

        boolean searching = true;
      });
    return ret.get();
  }
  /** Apply trimming once
   * @param from what to process
   * @return trimmed text, or null in case of error or no more applicable
   *         tippers */
  @SuppressWarnings("hiding") static String once(final String from) {
    final Traversal traversal = new TraversalImplementation();
    final IDocument ret = new Document(from);
    final ASTNode root = make.ast(from);
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
          return false;
        }
        <N extends ASTNode> void apply(final Tip t, final N n) {
          final ASTRewrite r = ASTRewrite.create(n.getAST());
          t.go(r, null);
          final TextEdit e = r.rewriteAST(ret, null);
          try {
            e.apply(ret);
          } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
            note.bug(traversal, ¢);
          }
        }

        boolean searching = true;
      });
    return ret.get();
  }
  static String repetitively(final String from) {
    int n = 0;
    for (String ret = from, next;; ret = next) {
      next = once(ret);
      if (Trivia.same(ret, next) || ++n > 20)
        return ret;
    }
  }

  Traversal traversal = new TraversalImplementation();

  static <N extends ASTNode> Tipper<N> safeFirstTipper(final N $) {
    try {
      return traversal.toolbox.firstTipper($);
    } catch (final Exception ret) {
      return note.bug(ret);
    }
  }
  static String thrice(final String javaCode) {
    return once(twice(javaCode));
  }
  static String twice(final String javaCode) {
    return once(once(javaCode));
  }
}
