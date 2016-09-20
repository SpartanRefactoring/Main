package il.org.spartan.spartanizer.dispatch;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.spartanizer.wringing.*;

/** @author Yossi Gil
 * @since 2015/07/10 */
public class Trimmer extends GUI$Applicator {
  public static boolean prune(final Suggestion r, final List<Suggestion> rs) {
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
    super("  Apply");
    this.toolbox = toolbox;
  }

  @Override public void consolidateSuggestions(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    Toolbox.refresh();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        TrimmerLog.visitation(n);
        if (!inRange(m, n))
          return true;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        if (w == null)
          return true;
        final Suggestion s = w.suggest(n, exclude);
        TrimmerLog.suggestion(w, n);
        if (s != null) {
          if (LogManager.isActive())
            LogManager.getLogWriter().printRow(u.getJavaElement().getElementName(), s.description, s.lineNumber + "");
          TrimmerLog.application(r, s);
        }
        return true;
      }
    });
  }

  public String fixed(final String from) {
    final Document $ = new Document(from);
    for (;;) {
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from($.get());
      final ASTRewrite r = createRewrite(u);
      final TextEdit e = r.rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException x) {
        Plugin.log(x);
        throw new AssertionError(x);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }

  @Override protected ASTVisitor collectSuggestions(final CompilationUnit u, final List<Suggestion> $) {
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        if (new DisabledChecker(u).check(n))
          return true;
        final Wring<N> w = Toolbox.defaultInstance().find(n);
        return w == null || w.cantSuggest(n) || prune(w.suggest(n, exclude), $);
      }
    };
  }

  public abstract class With {
    public Trimmer trimmer() {
      return Trimmer.this;
    }
  }
}
