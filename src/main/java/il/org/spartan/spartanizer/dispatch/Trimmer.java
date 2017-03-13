package il.org.spartan.spartanizer.dispatch;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import static java.util.stream.Collectors.*;

import il.org.spartan.plugin.*;
import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015/07/10 */
public class Trimmer extends AbstractGUIApplicator {
  public static boolean silent;

  public static boolean prune(final Tip r, final List<Tip> rs) {
    if (r != null) {
      r.pruneIncluders(rs);
      rs.add(r);
    }
    return true;
  }

  boolean useProjectPreferences;
  private final Map<IProject, Toolbox> toolboxes = new HashMap<>();
  public Toolbox toolbox;

  public Trimmer useProjectPreferences() {
    useProjectPreferences = true;
    toolboxes.clear();
    return this;
  }

  /** Instantiates this class */
  public Trimmer() {
    this(Toolbox.defaultInstance());
  }

  public Trimmer(final Toolbox toolbox) {
    super("Trimming");
    this.toolbox = toolbox;
  }

  @Override public void consolidateTips(final ASTRewrite r, final CompilationUnit u, final IMarker m, final Int i) {
    final Toolbox t = !useProjectPreferences ? toolbox : getToolboxByPreferences(u);
    final String fileName = English.unknownIfNull(u.getJavaElement(), IJavaElement::getElementName);
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        progressMonitor.worked(1);
        TrimmerLog.visitation(n);
        if (!check(n) || !inRange(m, n) || disabling.on(n))
          return true;
        Tipper<N> w = null;
        try {
          w = getTipper(t, n);
        } catch (final Exception ¢) {
          monitor.logProbableBug(this, ¢);
        }
        if (w == null)
          return true;
        Tip s = null;
        try {
          s = w.tip(n, exclude);
          TrimmerLog.tip(w, n);
        } catch (final Exception ¢) {
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
    for (final IDocument $ = new Document(from);;)
      if (fixed(once($)))
        return $.get();
  }

  public String once(final String from) {
    final IDocument $ = new Document(from);
    once($);
    return $.get();
  }

  /** return if got to fixed point of code */
  private static boolean fixed(final TextEdit ¢) {
    return !¢.hasChildren();
  }

  /** Performs one iteration of Spartanization
   * @param $ idocument object
   * @return
   * @throws AssertionError */
  public TextEdit once(final IDocument $) throws AssertionError {
    final TextEdit e;
    try {
      e = createRewrite((CompilationUnit) makeAST.COMPILATION_UNIT.from($.get())).rewriteAST($, null);
      e.apply($);
    } catch (final NullPointerException | MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      if (!silent)
        monitor.logEvaluationError(this, ¢);
      throw new AssertionError(¢);
    }
    return e;
  }

  @Override protected ASTVisitor makeTipsCollector(final List<Tip> $) {
    Toolbox.refresh(this);
    return new DispatchingVisitor() {
      Toolbox t;

      @Override protected <N extends ASTNode> boolean go(final N n) {
        final String fileName = English.unknownIfNull(az.compilationUnit(n.getRoot()),
            λ -> λ.getJavaElement() == null ? English.UNKNOWN : λ.getJavaElement().getElementName());
        progressMonitor.worked(1);
        if (!check(n) || disabling.on(n))
          return true;
        Tipper<N> w = null;
        try {
          w = getTipper(t, n);
        } catch (final Exception ¢) {
          monitor.debug(this, ¢);
          monitor.logToFile(¢, fileName, n, n.getRoot());
        }
        if (w != null)
          progressMonitor.worked(5);
        try {
          return w == null || w.cantTip(n) || prune(w.tip(n, exclude), $);
        } catch (final Exception ¢) {
          monitor.debug(this, ¢);
          monitor.logToFile(¢, fileName, n, n.getRoot());
        }
        return false;
      }

      @Override protected void initialization(final ASTNode ¢) {
        t = !useProjectPreferences || !(¢ instanceof CompilationUnit) ? toolbox : getToolboxByPreferences((CompilationUnit) ¢);
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

  @SuppressWarnings("static-method") protected <N extends ASTNode> Tipper<N> getTipper(final Toolbox t, final N ¢) {
    return t.firstTipper(¢);
  }

  boolean firstAddition = true;

  @SafeVarargs public final <N extends ASTNode> Trimmer fix(final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      toolbox = new Toolbox();
    }
    toolbox.add(c, ts);
    return this;
  }

  @SafeVarargs public final <N extends ASTNode> Trimmer addSingleTipper(final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      toolbox = new Toolbox();
    }
    toolbox.add(c, ts);
    return this;
  }

  /** @param u JD
   * @return {@link Toolbox} by project's preferences */
  Toolbox getToolboxByPreferences(final CompilationUnit u) {
    if (u == null)
      return null;
    final ITypeRoot r = u.getTypeRoot();
    if (r == null)
      return null;
    final IJavaProject jp = r.getJavaProject();
    if (jp == null)
      return null;
    final IProject p = jp.getProject();
    if (p == null)
      return null;
    if (toolboxes.containsKey(p))
      return toolboxes.get(p);
    final Toolbox $ = Toolbox.freshCopyOfAllTippers();
    final Set<Class<Tipper<? extends ASTNode>>> es = XMLSpartan.enabledTippers(p);
    final Collection<Tipper<?>> xs = $.getAllTippers().stream().filter(λ -> !es.contains(λ.getClass())).collect(toList());
    for (final List<Tipper<? extends ASTNode>> ¢ : $.implementation)
      ¢.removeAll(xs);
    toolboxes.put(p, $);
    return $;
  }
}
