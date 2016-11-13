package il.org.spartan.spartanizer.dispatch;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Yossi Gil
 * @since 2015/07/10 */
public class Trimmer extends AbstractGUIApplicator {
  public static boolean prune(final Tip r, final List<Tip> rs) {
    if (r != null) {
      r.pruneIncluders(rs);
      rs.add(r);
    }
    return true;
  }

  public Toolbox toolbox;

  /** Instantiates this class */
  public Trimmer() {
    this(Toolbox.defaultInstance());
  }
  public Trimmer(final Toolbox toolbox) {
    super("Trimming");
    this.toolbox = toolbox;
  }
  @Override public void consolidateTips(final ASTRewrite r, final CompilationUnit u, final IMarker m, final AtomicInteger i) {
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        progressMonitor.worked(1);
        TrimmerLog.visitation(n);
        if (!check(n) || !inRange(m, n) || disabling.on(n))
          return true;
        Tipper<N> w = null;
        try {
          w = getTipper(n);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (w == null)
          return true;
        Tip s = null;
        try {
          s = w.tip(n, exclude);
          TrimmerLog.tip(w, n);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (s != null) {
          i.incrementAndGet();
          TrimmerLog.application(r, s);
        }
        return true;
      }
      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }
  public String fixed(final String from) {
    for (final Document $ = new Document(from);;) {
      final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from($.get());
      final ASTRewrite r = createRewrite(u);
      final TextEdit e = r.rewriteAST($, null);
      try {
        e.apply($);
      } catch (final MalformedTreeException | IllegalArgumentException | BadLocationException x) {
        monitor.logEvaluationError(this, x);
        throw new AssertionError(x);
      }
      if (!e.hasChildren())
        return $.get();
      // return "nothing changed!";
    }
  }
  @Override protected ASTVisitor makeTipsCollector(final List<Tip> $) {
    Toolbox.refresh(this);
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        progressMonitor.worked(1);
        if (!check(n) || disabling.on(n))
          return true;
        Tipper<N> w = null;
        try {
          w = getTipper(n);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        if (w != null)
          progressMonitor.worked(5);
        try {
          return w == null || w.cantTip(n) || prune(w.tip(n, exclude), $);
        } catch (final Exception x) {
          monitor.debug(this, x);
        }
        return false;
      }
      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    };
  }

  public abstract class With {
    public Trimmer trimmer() {
      return Trimmer.this;
    }
  }

  @SuppressWarnings("static-method") protected <N extends ASTNode> boolean check(@SuppressWarnings("unused") final N __) {
    return true;
  }
  protected <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    return toolbox.firstTipper(¢);
  }

  boolean changed;

  @SafeVarargs public final <N extends ASTNode> Trimmer add(final Class<N> n, final Tipper<N>... ns) {
    if (!changed)
      toolbox = Toolbox.muttableDefaultInstance();
    changed = true;
    toolbox.add(n, ns);
    return this;
  }
}
