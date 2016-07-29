package il.org.spartan.refactoring.wring;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.utils.As;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * @author Yossi Gil
 * @since 2015/07/10
 */
public class Trimmer extends Spartanization {
  /**
   * Apply trimming repeatedly, until no more changes
   *
   * @param from what to process
   * @return the trimmed text
   */
  public static String fixedPoint(final String from) {
    final Trimmer trimmer = new Trimmer();
    final Document $ = new Document(from);
    for (;;) {
      final CompilationUnit u = (CompilationUnit) As.COMPILIATION_UNIT.ast($.get());
      final ASTRewrite r = trimmer.createRewrite(u, null);
      final TextEdit e = r.rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException x) {
        x.printStackTrace();
      }
      if (!e.hasChildren())
        return $.get();
    }
  }
  static boolean prune(final Rewrite r, final List<Rewrite> rs) {
    if (r != null) {
      r.pruneIncluders(rs);
      rs.add(r);
    }
    return true;
  }
  /** Instantiates this class */
  public Trimmer() {
    super("Trimmer");
    Toolbox.generate();
  }
  @Override protected ASTVisitor collect(final List<Rewrite> $) {
    return new DispatchingVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        final Wring<N> w = Toolbox.instance().find(n);
        return w == null || w.nonEligible(n) || prune(w.make(n, exclude), $);
      }
    };
  }
  @Override protected final void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    u.accept(new DispatchingVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        if (!inRange(m, n))
          return true;
        final Wring<N> w = Toolbox.instance().find(n);
        if (w != null) {
          final Rewrite make = w.make(n, exclude);
          if (make != null)
            make.go(r, null);
        }
        return true;
      }
    });
  }
  @SuppressWarnings("static-method") ExclusionManager makeExcluder() {
    return new ExclusionManager();
  }

  abstract class DispatchingVisitor extends ASTVisitor {
    final ExclusionManager exclude = makeExcluder();
    @Override public final boolean visit(final Assignment it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final Block it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final SingleVariableDeclaration d) {
      return cautiousGo(d);
    }
    @Override public final boolean visit(final ConditionalExpression e) {
      return cautiousGo(e);
    }
    @Override public final boolean visit(final IfStatement it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final InfixExpression it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final MethodDeclaration it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final PostfixExpression it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final PrefixExpression it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final ReturnStatement it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final SuperConstructorInvocation it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final TypeDeclaration it) {
        return cautiousGo(it);
      }
    @Override public final boolean visit(final VariableDeclarationFragment it) {
      return cautiousGo(it);
    }
    abstract <N extends ASTNode> boolean go(final N n);
    private boolean cautiousGo(final ASTNode n) {
      return !exclude.isExcluded(n) && go(n);
    }
  }
}
