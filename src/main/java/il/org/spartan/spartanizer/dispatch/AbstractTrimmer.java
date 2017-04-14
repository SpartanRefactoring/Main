package il.org.spartan.spartanizer.dispatch;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** A smorgasboard containing lots of stuff factored out of {@link Trimmer}
 * <ol>
 * <li>Configuration: sometime you wish to disable some of the tippers.
 * </ol>
 * @author Yossi Gil
 * @since 2015/07/10 */
public abstract class AbstractTrimmer extends GUIConfigurationApplicator {
  public AbstractTrimmer(final String name) {
    super(name);
  }

  /** return if got to fixed point of code */
  protected static boolean fixed(final TextEdit ¢) {
    return !¢.hasChildren();
  }

  @SafeVarargs public final <N extends ASTNode> AbstractTrimmer addSingleTipper(final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      globalConfiguration = new Configuration();
    }
    globalConfiguration.add(c, ts);
    return this;
  }

  @SuppressWarnings("static-method") protected <N extends ASTNode> boolean check(@SuppressWarnings("unused") final N __) {
    return true;
  }

  /** @param u JD
   * @return {@link Configuration} by project's preferences */
  protected Configuration getPreferredConfiguration(final CompilationUnit u) {
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
    if (configurations.containsKey(p))
      return configurations.get(p);
    final Configuration $ = Configurations.allClone();
    final Set<Class<Tipper<? extends ASTNode>>> es = XMLSpartan.enabledTippers(p);
    final Collection<Tipper<?>> xs = $.getAllTippers().stream().filter(λ -> !es.contains(λ.getClass())).collect(toList());
    for (final List<Tipper<? extends ASTNode>> ¢ : $.implementation)
      ¢.removeAll(xs);
    configurations.put(p, $);
    return $;
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
      return note.bug(this, ¢);
    }
  }

  public String once(final String from) {
    final IDocument $ = new Document(from);
    once($);
    return $.get();
  }

  public String fixed(final String from) {
    for (final IDocument $ = new Document(from);;)
      if (fixed(once($)))
        return $.get();
  }

  protected AbstractTrimmer fix(final Configuration all, final Tipper<?>... ts) {
    final List<Tipper<?>> tss = as.list(ts);
    if (!firstAddition)
      tss.addAll(globalConfiguration.getAllTippers());
    firstAddition = false;
    globalConfiguration = all;
    Stream.of(globalConfiguration.implementation).filter(Objects::nonNull)
        .forEachOrdered((final List<Tipper<? extends ASTNode>> ¢) -> ¢.retainAll(tss));
    return this;
  }

  public AbstractTrimmer useProjectPreferences() {
    useProjectPreferences = true;
    configurations.clear();
    return this;
  }

  public ASTRewrite createRewrite(final CompilationUnit ¢) {
    return rewriterOf(¢, null, new Int());
  }

  private ASTRewrite rewriterOf(final CompilationUnit u, final IMarker m, final Int counter) {
    note.logger.fine("Weaving maximal rewrite of " + u);
    progressMonitor.beginTask("Weaving maximal rewrite ...", IProgressMonitor.UNKNOWN);
    final Int count = new Int();
    final ASTRewrite $ = computeMaximalRewrite(u, m, __ -> count.step());
    counter.add(count);
    progressMonitor.done();
    return $;
  }

  /** creates an ASTRewrite which contains the changes
   * @param u the Compilation Unit (outermost ASTNode in the Java Grammar)
   * @param m a progress monitor in which the progress of the refactoring is
   *        displayed
   * @return an ASTRewrite which contains the changes */
  public ASTRewrite createRewrite(final CompilationUnit ¢, final Int counter) {
    return rewriterOf(¢, null, counter);
  }

  public String compilationUnitName() {
    return iCompilationUnit.getElementName();
  }

  /** creates an ASTRewrite, under the context of a text marker, which contains
   * the changes
   * @param pm a progress monitor in which to display the progress of the
   *        refactoring
   * @param m the marker
   * @return an ASTRewrite which contains the changes */
  public ASTRewrite createRewrite(final IMarker ¢) {
    return rewriterOf((CompilationUnit) makeAST.COMPILATION_UNIT.from(¢, progressMonitor), ¢, new Int());
  }

  public Configuration globalConfiguration = Configurations.all();
  protected final Map<IProject, Configuration> configurations = new HashMap<>();
  private final IProgressMonitor progressMonitor = nullProgressMonitor;
  protected boolean useProjectPreferences;
  protected boolean firstAddition = true;
}