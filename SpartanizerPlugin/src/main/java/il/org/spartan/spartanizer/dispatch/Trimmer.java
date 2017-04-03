package il.org.spartan.spartanizer.dispatch;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.bloater.*;
import il.org.spartan.plugin.*;
import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A smorgasboard containing lots of stuff, but its main purpose, which should
 * be factored out somewhere is to apply a {@link Toolbox} to a tree. The main
 * difficulties are:
 * <ol>
 * <li>Top down or bottom up traversal
 * <li>Overlapping in the domain of distinct tippers
 * <li>Progress monitoring
 * <li>Fault recovery, not all tippers are bug free
 * <li>Configuration: sometime you wish to disable some of the tippers.
 * <li>Consolidation: it does not make sense to apply one tipper after the
 * other. Batch processing is required.
 * </ol>
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015/07/10 */
public class Trimmer extends AbstractGUIApplicator {
  public static boolean silent;

  public static boolean prune(@Nullable final Tip r, @NotNull final List<Tip> rs) {
    if (r == null)
      return true;
    r.pruneIncluders(rs);
    rs.add(r);
    return true;
  }

  boolean useProjectPreferences;
  private final Map<IProject, Toolbox> toolboxes = new HashMap<>();
  Consumer<Exception> exceptionListener = λ -> {/**/};
  public Toolbox toolbox;

  @NotNull public Trimmer useProjectPreferences() {
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

  public Trimmer setExceptionListener(final Consumer<Exception> ¢) {
    exceptionListener = ¢;
    return this;
  }

  @Override public int consolidateTips(final ASTRewrite r, @NotNull final CompilationUnit u, final IMarker m) {
    final Int $ = new Int();
    @Nullable final Toolbox t = !useProjectPreferences ? toolbox : getToolboxByPreferences(u);
    @NotNull final String fileName = English.unknownIfNull(u.getJavaElement(), IJavaElement::getElementName);
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(@NotNull final N n) {
        progressMonitor.worked(1);
        TrimmerLog.visitation(n);
        if (!check(n) || !inRange(m, n) || disabling.on(n))
          return true;
        @Nullable Tipper<N> w = null;
        try {
          w = getTipper(t, n);
        } catch (@NotNull final Exception ¢) {
          monitor.logProbableBug(this, ¢);
          exceptionListener.accept(¢);
        }
        if (w == null)
          return true;
        @Nullable Tip s = null;
        try {
          s = w.tip(n, exclude);
          TrimmerLog.tip(w, n);
        } catch (@NotNull final Exception ¢) {
          monitor.debug(this, ¢);
          monitor.logToFile(¢, fileName, n, n.getRoot());
          exceptionListener.accept(¢);
        }
        if (s == null)
          return true;
        $.step();
        TrimmerLog.application(r, s);
        return true;
      }

      @Override protected void initialization(@NotNull final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
    return $.get();
  }

  public String fixed(final String from) {
    for (@NotNull final IDocument $ = new Document(from);;)
      if (fixed(once($)))
        return $.get();
  }

  public String once(final String from) {
    @NotNull final IDocument $ = new Document(from);
    once($);
    return $.get();
  }

  /** return if got to fixed point of code */
  private static boolean fixed(@NotNull final TextEdit ¢) {
    return !¢.hasChildren();
  }

  /** Performs one iteration of Spartanization
   * @param $ idocument object
   * @return
   * @throws AssertionError */
  public TextEdit once(@NotNull final IDocument $) throws AssertionError {
    final TextEdit e;
    try {
      e = createRewrite((CompilationUnit) makeAST.COMPILATION_UNIT.from($.get())).rewriteAST($, null);
      e.apply($);
    } catch (@NotNull final NullPointerException | MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      if (!silent)
        monitor.logEvaluationError(this, ¢);
      throw new AssertionError(¢);
    }
    return e;
  }

  @Override @Nullable protected ASTVisitor makeTipsCollector(@NotNull final List<Tip> $) {
    Toolbox.refresh(this);
    return new DispatchingVisitor() {
      @Nullable Toolbox t;

      @Override protected <N extends ASTNode> boolean go(@NotNull final N n) {
        @NotNull final String fileName = English.unknownIfNull(az.compilationUnit(n.getRoot()),
            λ -> λ.getJavaElement() == null ? English.UNKNOWN : λ.getJavaElement().getElementName());
        progressMonitor.worked(1);
        if (!check(n) || disabling.on(n))
          return true;
        @Nullable Tipper<N> w = null;
        try {
          w = getTipper(t, n);
        } catch (@NotNull final Exception ¢) {
          monitor.debug(this, ¢);
          monitor.logToFile(¢, fileName, n, n.getRoot());
          exceptionListener.accept(¢);
        }
        if (w != null)
          progressMonitor.worked(5);
        try {
          return w == null /* || w.cantTip(n) [probably bug --or] */ || prune(w.tip(n, exclude), $);
        } catch (@NotNull final Exception ¢) {
          monitor.debug(this, ¢);
          monitor.logToFile(¢, fileName, n, n.getRoot());
          exceptionListener.accept(¢);
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
    @NotNull public Trimmer trimmer() {
      return Trimmer.this;
    }
  }

  @SuppressWarnings("static-method") protected <N extends ASTNode> boolean check(@SuppressWarnings("unused") final N __) {
    return true;
  }

  @SuppressWarnings("static-method") protected <N extends ASTNode> Tipper<N> getTipper(@NotNull final Toolbox t, final N ¢) {
    return t.firstTipper(¢);
  }

  boolean firstAddition = true;

  @SafeVarargs @NotNull public final <N extends ASTNode> Trimmer fix(final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      toolbox = new Toolbox();
    }
    toolbox.add(c, ts);
    return this;
  }

  @SafeVarargs @NotNull public final <N extends ASTNode> Trimmer addSingleTipper(final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      toolbox = new Toolbox();
    }
    toolbox.add(c, ts);
    return this;
  }

  @SafeVarargs @NotNull public final Trimmer fixTipper(final Tipper<?>... ¢) {
    return fix(Toolbox.freshCopyOfAllTippers(), ¢);
  }

  @SafeVarargs @NotNull public final Trimmer fixBloater(final Tipper<?>... ¢) {
    return fix(InflaterProvider.freshCopyOfAllExpanders(), ¢);
  }

  // Does not require node class --or
  @NotNull private Trimmer fix(final Toolbox all, final Tipper<?>... ts) {
    final List<Tipper<?>> tss = as.list(ts);
    if (!firstAddition)
      tss.addAll(toolbox.getAllTippers());
    firstAddition = false;
    toolbox = all;
    Stream.of(toolbox.implementation).filter(Objects::nonNull).forEachOrdered((final List<Tipper<? extends ASTNode>> ¢) -> ¢.retainAll(tss));
    return this;
  }

  /** @param u JD
   * @return {@link Toolbox} by project's preferences */
  @Nullable Toolbox getToolboxByPreferences(@Nullable final CompilationUnit u) {
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
    for (@NotNull final List<Tipper<? extends ASTNode>> ¢ : $.implementation)
      ¢.removeAll(xs);
    toolboxes.put(p, $);
    return $;
  }
}
