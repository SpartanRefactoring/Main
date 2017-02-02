package il.org.spartan.spartanizer.dispatch;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2015/07/10 */
public class Trimmer extends AbstractGUIApplicator {
  public static boolean silent;

  public static boolean prune(@Nullable final Tip r, @NotNull final List<Tip> rs) {
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

  @Override public void consolidateTips(final ASTRewrite r, @NotNull final CompilationUnit u, final IMarker m, @NotNull final Int i) {
    final String fileName = Linguistic.unknownIfNull(u.getJavaElement(), IJavaElement::getElementName);
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(@NotNull final N n) {
        progressMonitor.worked(1);
        TrimmerLog.visitation(n);
        if (!check(n) || !inRange(m, n) || disabling.on(n))
          return true;
        Tipper<N> w = null;
        try {
          w = getTipper(n);
        } catch (@NotNull final Exception ¢) {
          if (!silent)
            ¢.printStackTrace();
          monitor.debug(this, ¢);
          monitor.logToFile(¢, fileName, n, n.getRoot());
        }
        if (w == null)
          return true;
        Tip s = null;
        try {
          s = w.tip(n, exclude);
          TrimmerLog.tip(w, n);
        } catch (@NotNull final Exception ¢) {
          ¢.printStackTrace();
          monitor.debug(this, ¢);
          monitor.logToFile(¢, fileName, n, n.getRoot());
        }
        if (s != null) {
          i.step();
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
      final TextEdit e = createRewrite((CompilationUnit) makeAST.COMPILATION_UNIT.from($.get())).rewriteAST($, null);
      try {
        e.apply($);
      } catch (@NotNull final MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
        if (!silent)
          monitor.logEvaluationError(this, ¢);
        throw new AssertionError(¢);
      }
      if (!e.hasChildren())
        return $.get();
    }
  }

  @Override @Nullable protected ASTVisitor makeTipsCollector(@NotNull final List<Tip> $) {
    Toolbox.refresh(this);
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(@NotNull final N n) {
        final String fileName = Linguistic.unknownIfNull(az.compilationUnit(n.getRoot()),
            λ -> λ.getJavaElement() == null ? Linguistic.UNKNOWN : λ.getJavaElement().getElementName());
        progressMonitor.worked(1);
        if (!check(n) || disabling.on(n))
          return true;
        Tipper<N> w = null;
        try {
          w = getTipper(n);
        } catch (@NotNull final Exception ¢) {
          monitor.debug(this, ¢);
          monitor.logToFile(¢, fileName, n, n.getRoot());
        }
        if (w != null)
          progressMonitor.worked(5);
        try {
          return w == null || w.cantTip(n) || prune(w.tip(n, exclude), $);
        } catch (@NotNull final Exception ¢) {
          monitor.debug(this, ¢);
          monitor.logToFile(¢, fileName, n, n.getRoot());
        }
        return false;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    };
  }

  public abstract class With {
    @NotNull public Trimmer trimmer() {
      return Trimmer.this;
    }
  }

  @SuppressWarnings("static-method") protected <N extends ASTNode> boolean check(@SuppressWarnings("unused") final N __) {
    return true;
  }

  @Nullable protected <N extends ASTNode> Tipper<N> getTipper(@NotNull final N ¢) {
    return toolbox.firstTipper(¢);
  }

  boolean firstAddition = true;

  @SafeVarargs @NotNull public final <N extends ASTNode> Trimmer fix(@NotNull final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      toolbox = new Toolbox();
    }
    toolbox.add(c, ts);
    return this;
  }

  @SafeVarargs @NotNull public final <N extends ASTNode> Trimmer addSingleTipper(@NotNull final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      toolbox = new Toolbox();
    }
    toolbox.add(c, ts);
    return this;
  }
}
