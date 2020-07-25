package il.org.spartan.spartanizer.cmdline.good;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import fluent.ly.note;
import il.org.spartan.Wrapper;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.engine.nominal.Trivia;
import il.org.spartan.spartanizer.tipping.Tip;
import il.org.spartan.spartanizer.tipping.Tipper;
import il.org.spartan.spartanizer.traversal.DispatchingVisitor;
import il.org.spartan.spartanizer.traversal.Toolbox;
import il.org.spartan.spartanizer.traversal.Traversal;
import il.org.spartan.spartanizer.traversal.TraversalImplementation;

/** Simple no-gimmicks singleton service that does the simple job of applying a
 * {@link Toolbox} {@link #once(String)}, {@link #twice(String)},
 * {@link #thrice(String)}, and {@link #repetitively(String)}.
 * @author Yossi Gil
 * @since 2017-03-08 */
public interface theSpartanizer {
  static Tipper<?> firstTipper(final String from) {
    final Wrapper<Tipper<?>> $ = new Wrapper<>();
    final ASTNode n = make.ast(from);
    if (n != null)
      n.accept(new DispatchingVisitor() {
        @Override protected <N extends ASTNode> boolean go(final N ¢) {
          return searching && go(traversal.toolbox.firstTipper(¢));
        }
        <N extends ASTNode> boolean go(final Tipper<N> ¢) {
          if (¢ == null)
            return true;
          $.set(¢);
          return false;
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
    for (String $ = from, next;; $ = next) {
      next = once($);
      if (Trivia.same($, next) || ++n > 20)
        return $;
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
