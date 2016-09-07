package il.org.spartan.spartanizer.wring;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.spartanizations.*;

/** @author Yossi Gil
 * @since 2015/07/10 */
public class Trimmer extends Spartanization {
  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  public static String fixedPoint(final String from) {
    return new Trimmer().fixed(from);
  }

  static boolean prune(final Rewrite r, final List<Rewrite> rs) {
    if (r != null) {
      r.pruneIncluders(rs);
      rs.add(r);
    }
    return true;
  }

  public final Toolbox toolbox;

  /** Instantiates this class */
  public Trimmer() {
    this(Toolbox.defaultInstance());
  }

  public Trimmer(final Toolbox toolbox) {
    super("Trimmer");
    this.toolbox = toolbox;
  }

  protected ASTVisitor collect(final List<Rewrite> $) {
    Toolbox.refresh();
    return new DispatchingVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        return w == null || w.nonEligible(n) || prune(w.make(n, exclude), $);
      }
    };
  }

  @Override protected ASTVisitor collect(final List<Rewrite> $, final CompilationUnit u) {
    // TODO: Ori Roth, I cannot get this to run. final DisabledChecker dc = new
    // DisabledChecker(u);
    return new DispatchingVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        // if (dc.check(n)) return false;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        return w == null || w.nonEligible(n) || prune(w.make(n, exclude), $);
      }
    };
  }

  @Override protected void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    Toolbox.refresh();
    u.accept(new DispatchingVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        // if (dc.check(n))
        // return false;
        if (!inRange(m, n))
          return true;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        if (w != null) {
          final Rewrite make = w.make(n, exclude);
          if (make != null)
            make.go(r, null);
        }
        return true;
      }
    });
  }

  String fixed(final String from) {
    final Document $ = new Document(from);
    for (;;) {
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from($.get());
      final ASTRewrite r = createRewrite(u, null);
      final TextEdit e = r.rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException x) {
        x.printStackTrace();
        throw new AssertionError(x);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }

  @SuppressWarnings("static-method") ExclusionManager makeExcluder() {
    return new ExclusionManager();
  }

  public class With {
    public Trimmer trimmer() {
      return Trimmer.this;
    }
  }

  abstract class DispatchingVisitor extends ASTVisitor {
    final ExclusionManager exclude = makeExcluder();

    @Override public final boolean visit(final Assignment ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final Block ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final CastExpression ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final ConditionalExpression x) {
      return cautiousGo(x);
    }

    @Override public final boolean visit(final EnumDeclaration ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final FieldDeclaration ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final IfStatement ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final InfixExpression ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final MethodDeclaration ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final MethodInvocation ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final NormalAnnotation ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final PostfixExpression ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final PrefixExpression ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final ReturnStatement ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final SingleVariableDeclaration d) {
      return cautiousGo(d);
    }

    @Override public final boolean visit(final SuperConstructorInvocation ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final TypeDeclaration ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final VariableDeclarationFragment ¢) {
      return cautiousGo(¢);
    }

    abstract <N extends ASTNode> boolean go(final N n);

    private boolean cautiousGo(final ASTNode n) {
      return !exclude.isExcluded(n) && go(n);
    }
  }
}
