package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.spartanizations.*;

/** @author Yossi Gil
 * @since 2015/07/10 */
public class Trimmer extends Spartanization {
  /** Apply trimming repeatedly, until no more changes
   * @param from what to process
   * @return trimmed text */
  public static String fixedPoint(final String from) {
    return new Trimmer().fixed(from);
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

  @Override protected ASTVisitor collect(final List<Rewrite> $) {
    Toolbox.refresh();
    return new DispatchingVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        return w == null || w.nonEligible(n) || prune(w.make(n, exclude), $);
      }
    };
  }

  @Override protected void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    Toolbox.refresh();
    u.accept(new DispatchingVisitor() {
      @Override <N extends ASTNode> boolean go(final N n) {
        if (dc.check(n))
          return false;
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

    @Override public final boolean visit(final ConditionalExpression e) {
      return cautiousGo(e);
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

  /**
   * Determines whether an {@link ASTNode} is spartanization disabled. In the
   * current implementation, only instances of {@link BodyDeclaration} may be
   * disabled, and only via their {@link Javadoc} comment
   *
   * @author Ori Roth
   * @since 2016/05/13
   */
  public class DisabledChecker {
    final Set<ASTNode> dns;
    /**
     * Disable spartanization identifier, used by the programmer to indicate a
     * method/class/code line not to be spartanized
     */
    public final static String dsi = "@DisableSpartan";

    protected DisabledChecker(final CompilationUnit u) {
      dns = new HashSet<>();
      if (u == null)
        return;
      u.accept(new BodyDeclarationVisitor(dns));
    }
    /**
     * @param n node
     * @return true iff spartanization is disabled for n
     */
    public boolean check(final ASTNode n) {
      ASTNode p = n;
      while (p != null) {
        if (dns.contains(p))
          return true;
        p = p.getParent();
      }
      return false;
    }

    private class BodyDeclarationVisitor extends ASTVisitor {
      @SuppressWarnings("hiding") Set<ASTNode> dns;

      BodyDeclarationVisitor(final Set<ASTNode> dns) {
        this.dns = dns;
      }
      @Override public boolean visit(final AnnotationTypeDeclaration d) {
        return go(d);
      }
      @Override public boolean visit(final EnumDeclaration d) {
        return go(d);
      }
      @Override public boolean visit(final TypeDeclaration d) {
        return go(d);
      }
      @Override public boolean visit(final AnnotationTypeMemberDeclaration d) {
        return go(d);
      }
      @Override public boolean visit(final EnumConstantDeclaration d) {
        return go(d);
      }
      @Override public boolean visit(final FieldDeclaration d) {
        return go(d);
      }
      @Override public boolean visit(final Initializer i) {
        return go(i);
      }
      @Override public boolean visit(final MethodDeclaration d) {
        return go(d);
      }
      private boolean go(final BodyDeclaration d) {
        final Javadoc j = d.getJavadoc();
        if (j == null || !j.toString().contains(dsi))
          return true;
        dns.add(d);
        return false;
      }
    }
  }
}
