package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.spartanizations.*;
import il.org.spartan.refactoring.utils.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

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
    final Trimmer t = new Trimmer();
    final Document $ = new Document(from);
    for (;;) {
      final CompilationUnit u = (CompilationUnit) ast.COMPILIATION_UNIT.from($.get());
      final ASTRewrite r = t.createRewrite(u, null);
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
  }
  /**
   * @DisableSpartan
   */
  @Override protected ASTVisitor collect(final List<Rewrite> $, final CompilationUnit u) {
    final Disable disable = Source.getDisable(u);
    final Toolbox toolbox = Toolbox.generate(u);
    return new DispatchingVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        if (disable.check(n))
          return false;
        final Wring<N> w = toolbox.find(n);
        return w == null || w.nonEligible(n) || prune(w.createScalpel(null, null).make(n, exclude), $);
      }
    };
  }
  /**
   * @DisableSpartan
   */
  @Override protected final void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    final Disable disable = Source.getDisable(u);
    final Toolbox toolbox = Toolbox.generate(u);
    u.accept(new DispatchingVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        if (disable.check(n))
          return false;
        if (!inRange(m, n))
          return true;
        final Wring<N> w = toolbox.find(n);
        if (w != null) {
          final Rewrite make = w.createScalpel(r, null).make(n, exclude);
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
    @Override public final boolean visit(final ClassInstanceCreation c) {
      return cautiousGo(c);
    }
    @Override public final boolean visit(final ConditionalExpression e) {
      return cautiousGo(e);
    }
    @Override public final boolean visit(final EnumDeclaration it) {
      return cautiousGo(it);
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
    @Override public final boolean visit(final MethodInvocation it) {
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
    @Override public final boolean visit(final NormalAnnotation it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final SuperConstructorInvocation it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final VariableDeclarationFragment it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final TypeDeclaration it) {
      return cautiousGo(it);
    }
    @Override public final boolean visit(final SwitchStatement s) {
      return cautiousGo(s);
    }
    abstract <N extends ASTNode> boolean go(final N n);
    private boolean cautiousGo(final ASTNode n) {
      return !exclude.isExcluded(n) && go(n);
    }
  }

  abstract class DispatchingPostVisitor extends ASTVisitor {
    final ExclusionManager exclude = makeExcluder();

    @Override public void postVisit(final ASTNode n) {
      postGo(n);
    }
    abstract <N extends ASTNode> void postGo(final N n);
  }
}
