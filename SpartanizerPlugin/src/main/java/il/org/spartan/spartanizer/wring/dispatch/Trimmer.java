package il.org.spartan.spartanizer.wring.dispatch;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** @author Yossi Gil
 * @since 2015/07/10 */
public class Trimmer extends Applicator {
  public static abstract class DispatchingVisitor extends ASTVisitor {
    protected final ExclusionManager exclude = makeExcluder();

    private boolean cautiousGo(final ASTNode ¢) {
      return !exclude.isExcluded(¢) && go(¢);
    }

    protected abstract <N extends ASTNode> boolean go(final N n);

    @Override public final boolean visit(final Assignment ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final Block ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final CastExpression ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final ConditionalExpression ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final EnhancedForStatement ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final EnumDeclaration ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final FieldDeclaration ¢) {
      return cautiousGo(¢);
    }

    @Override public final boolean visit(final ForStatement ¢) {
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

    @Override public final boolean visit(final SingleVariableDeclaration ¢) {
      return cautiousGo(¢);
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

    @Override public final boolean visit(final WhileStatement ¢) {
      return cautiousGo(¢);
    }
  }

  public class With {
    public Trimmer trimmer() {
      return Trimmer.this;
    }
  }

  static ASTVisitor collect(final List<Rewrite> $) {
    Toolbox.refresh();
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        return w == null || w.cantSuggest(n) || prune(w.suggest(n, exclude), $);
      }
    };
  }

  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  public static String fixedPoint(final String from) {
    return new Trimmer().fixed(from);
  }

  static ExclusionManager makeExcluder() {
    return new ExclusionManager();
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
    super("  Spartanize");
    this.toolbox = toolbox;
  }

  @Override protected ASTVisitor collectSuggestions(final List<Rewrite> $, final CompilationUnit u) {
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        if (new DisabledChecker(u).check(n))
          return true;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        return w == null || w.cantSuggest(n) || prune(w.suggest(n, exclude), $);
      }
    };
  }

  @Override protected void consolidateSuggestions(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    Toolbox.refresh();
    final DisabledChecker dc = new DisabledChecker(u);
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        if (dc.check(n) || !inRange(m, n))
          return true;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        if (w != null) {
          final Rewrite make = w.suggest(n, exclude);
          if (make != null) {
            if (LogManager.isActive())
              // LogManager.initialize();
              LogManager.getLogWriter().printRow(u.getJavaElement().getElementName(), make.description, make.lineNumber + "");
            make.go(r, null);
          }
        }
        return true;
      }
    });
  }

  String fixed(final String from) {
    final Document $ = new Document(from);
    for (;;) {
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from($.get());
      final ASTRewrite r = createRewrite(u, new NullProgressMonitor());
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
}
