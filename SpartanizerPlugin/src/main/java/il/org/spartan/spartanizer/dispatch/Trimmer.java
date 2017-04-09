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
 * @author Yossi Gil
 * @since 2015/07/10 */
public class Trimmer extends AbstractGUIApplicator {
  public static boolean silent;
  boolean useProjectPreferences;
  private final Map<IProject, Toolbox> toolboxes = new HashMap<>();
  public Toolbox toolbox;
  private Tipper<?> tipper;
  Tip tip;
  Toolbox currentToolbox;
  String fileName;
  TrimmerExceptionListener exceptionListener = λ -> monitor.logToFile(λ, this, tip, tipper, fileName);
  final Consumer<Exception> swallow = λ -> exceptionListener.accept(λ);

  <N extends ASTNode> Tipper<N> findTipper(final N ¢) {
    return robust.lyNull(() -> getTipper(currentToolbox, ¢), swallow);
  }

  <N extends ASTNode> Tip findTip(final N ¢) {
    return robust.lyNull(() -> {
      tipper = null;
      final Tipper<N> $ = getTipper(currentToolbox, ¢);
      if ($ == null)
        return null;
      tipper = $;
      TrimmerLog.tip($, ¢);
      return $.tip(¢);
    }, swallow);
  }

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

  public Trimmer onException(final TrimmerExceptionListener ¢) {
    exceptionListener = ¢;
    return this;
  }

  @Override public ASTRewrite computeMaximalRewrite(final CompilationUnit u, final IMarker m, final Consumer<ASTNode> nodeLogger) {
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    currentToolbox = !useProjectPreferences ? toolbox : getToolboxByPreferences(u);
    fileName = English.unknownIfNull(u.getJavaElement(), IJavaElement::getElementName);
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N ¢) {
        progressMonitor.worked(1);
        tip = null;
        TrimmerLog.visitation(¢);
        if (!check(¢) || !inRange(m, ¢) || disabling.on(¢))
          return true;
        tip = findTip(¢);
        if (tip == null)
          return true;
        TrimmerLog.application($, tip);
        progressMonitor.worked(1);
        if (nodeLogger != null)
          nodeLogger.accept(¢);
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
    return $;
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

  /** Performs one spartanization iteration
   * @param d JD
   * @return
   * @throws AssertionError */
  public TextEdit once(final IDocument d) throws AssertionError {
    try {
      final TextEdit $ = createRewrite((CompilationUnit) makeAST.COMPILATION_UNIT.from(d.get()), new Int()).rewriteAST(d, null);
      $.apply(d);
      return $;
    } catch (final NullPointerException | MalformedTreeException | IllegalArgumentException | BadLocationException ¢) {
      if (!silent)
        monitor.logEvaluationError(this, ¢);
      throw new AssertionError(¢);
    }
  }

  @Override protected ASTVisitor makeTipsCollector(final Tips into) {
    Toolbox.refresh(this);
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        fileName = English.unknownIfNull(az.compilationUnit(n.getRoot()),
            λ -> English.unknownIfNull(λ.getJavaElement(), IJavaElement::getElementName));
        progressMonitor.worked(1);
        if (!check(n) || disabling.on(n))
          return true;
        final Tipper<N> $ = findTipper(n);
        if ($ == null)
          return true;
        progressMonitor.worked(1);
        return robust.lyTrue(() -> {
          tip = $.tip(n, exclude);
          if (tip == null)
            return;
          into.removeIf(λ -> λ.highlight.overlapping(tip.highlight));
          into.add(tip);
        }, swallow);
      }

      @Override protected void initialization(final ASTNode ¢) {
        currentToolbox = !useProjectPreferences || !(¢ instanceof CompilationUnit) ? toolbox : getToolboxByPreferences((CompilationUnit) ¢);
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

  @SafeVarargs public final Trimmer fixTipper(final Tipper<?>... ¢) {
    return fix(Toolbox.freshCopyOfAllTippers(), ¢);
  }

  @SafeVarargs public final Trimmer fixBloater(final Tipper<?>... ¢) {
    return fix(InflaterProvider.freshCopyOfAllExpanders(), ¢);
  }

  // Does not require node class --or
  private Trimmer fix(final Toolbox all, final Tipper<?>... ts) {
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
