package il.org.spartan.spartanizer.dispatch;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A smorgasboard containing lots of stuff factored out of {@link Trimmer}
 * <ol>
 * <li>Configuration: sometime you wish to disable some of the tippers.
 * </ol>
 * @author Yossi Gil
 * @since 2015/07/10 */
abstract class AbstractTipperNoBetterNameYet extends AbstractGUIApplicator {
  /** return if got to fixed point of code */
  protected static boolean fixed(final TextEdit ¢) {
    return !¢.hasChildren();
  }

  public static boolean silent;

  public AbstractTipperNoBetterNameYet(final String string) {
    super(string);
  }

  @SafeVarargs public final <N extends ASTNode> AbstractTipperNoBetterNameYet addSingleTipper(final Class<N> c, final Tipper<N>... ts) {
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
      if (!silent)
        monitor.bug(this, ¢);
      throw new AssertionError(¢);
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

  protected AbstractTipperNoBetterNameYet fix(final Configuration all, final Tipper<?>... ts) {
    final List<Tipper<?>> tss = as.list(ts);
    if (!firstAddition)
      tss.addAll(globalConfiguration.getAllTippers());
    firstAddition = false;
    globalConfiguration = all;
    Stream.of(globalConfiguration.implementation).filter(Objects::nonNull)
        .forEachOrdered((final List<Tipper<? extends ASTNode>> ¢) -> ¢.retainAll(tss));
    return this;
  }

  public AbstractTipperNoBetterNameYet useProjectPreferences() {
    useProjectPreferences = true;
    configurations.clear();
    return this;
  }

  public Configuration globalConfiguration = Configurations.all();
  protected final Map<IProject, Configuration> configurations = new HashMap<>();
  protected boolean useProjectPreferences;
  protected boolean firstAddition = true;
}