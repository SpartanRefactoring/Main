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
    super("  Spartanize");
    this.toolbox = toolbox;
  }

  protected ASTVisitor collect(final List<Rewrite> $) {
    Toolbox.refresh();
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        return w == null || w.cantWring(n) || prune(w.make(n, exclude), $);
      }
    };
  }

  @Override protected ASTVisitor collectSuggestions(final List<Rewrite> $, final CompilationUnit u) {
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        if (new DisabledChecker(u).check(n))
          return true;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        return w == null || w.cantWring(n) || prune(w.make(n, exclude), $);
      }
    };
  }

  @Override protected void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    Toolbox.refresh();
    final DisabledChecker dc = new DisabledChecker(u);
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        if (dc.check(n) || !inRange(m, n))
          return true;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        if (w != null) {
          final Rewrite make = w.make(n, exclude);
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

  static ExclusionManager makeExcluder() {
    return new ExclusionManager();
  }

  public class With {
    public Trimmer trimmer() {
      return Trimmer.this;
    }
  }
}
