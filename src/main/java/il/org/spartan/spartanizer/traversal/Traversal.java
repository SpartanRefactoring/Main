package il.org.spartan.spartanizer.traversal;

import static java.util.stream.Collectors.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** The interface of * {@link TraversalImplementation}, conducting a traversal
 * of an AST for two distinct purposes, {@link #collectTips(CompilationUnit)},
 * to present the tips in the problems view, and {@link #go(CompilationUnit)},
 * which collects a maximal set of tips for rewriting.
 * <p>
 * The search is restricted by the {@link #range} field. If the field is null,
 * no restriction. Otherwise, search only within the tree range specified by
 * this field.
 * <ol>
 * <li>
 * @author Yossi Gil
 * @since 2015/07/10 */
public abstract class Traversal implements Selfie<Traversal> {
  public Configuration configuration = Configurations.allClone();
  /** A list of all listeners to actions carried out by this instance. */
  public final TraversalTappers notify = new TraversalTappers()//
      .push(new TraversalMonitor(this)) //
      .push(new TraversalTapper() {
        @Override public void begin() {
          rewriteCount.clear();
        }

        @Override public void tipRewrite() {
          rewriteCount.step();
        }
      }) //
      .push(new TraversalTapper() {
        @Override public void begin() {
          if (!useProjectPreferences)
            return;
          final Configuration $ = getPreferredConfiguration(compilationUnit());
          configuration = $ != null ? $ : configuration;
        }
      });
  protected final Int rewriteCount = new Int();
  private Tip otherTip;
  private CompilationUnit compilationUnit;
  private TextEditGroup editGroup;
  private Range range;
  protected final Tips tips = Tips.empty();
  protected final Map<IProject, Configuration> configurations = new HashMap<>();
  protected String fileName;
  protected boolean firstAddition = true;
  protected ASTNode node;
  protected ASTRewrite rewrite;
  protected Tip tip;
  protected Tipper<?> tipper;
  protected boolean useProjectPreferences;

  /** Checks a Compilation Unit (outermost ASTNode in the Java Grammar) for
   * tipper tips
   * @param u what to check
   * @return a collection of non-overlapping {@link Tip} objects each containing
   *         a spartanization tip */
  public Tips collectTips(final CompilationUnit ¢) {
    tips.clear();
    setCompilationUnit(¢);
    ¢.accept(tipsCollector());
    return tips;
  }

  public abstract ASTRewrite go(CompilationUnit u);

  public TraversalTappers pop() {
    return notify.pop();
  }

  public Traversal push(final TraversalTapper ¢) {
    notify.push(¢);
    return this;
  }

  @Override public Traversal self() {
    return this;
  }

  public Traversal setRange(final Range ¢) {
    return self(() -> range = ¢);
  }

  public Tipper<?> tipper() {
    return tipper;
  }

  protected abstract ASTVisitor tipsCollector();

  public Traversal useProjectPreferences() {
    useProjectPreferences = true;
    configurations.clear();
    return this;
  }

  @SuppressWarnings("static-method") protected <N extends ASTNode> boolean check(@SuppressWarnings("unused") final N __) {
    return true;
  }

  protected void clearTipper() {
    tipper = null;
  }

  protected CompilationUnit compilationUnit() {
    return compilationUnit;
  }

  protected Tip getAuxiliaryTip() {
    return otherTip;
  }

  protected TextEditGroup getCurrentEditGroup() {
    return editGroup;
  }

  protected ASTNode getNode() {
    return node;
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

  protected Range getRange() {
    return range;
  }

  protected ASTRewrite getRewrite() {
    return rewrite;
  }

  protected Tip getTip() {
    return tip;
  }

  protected Tip setAuxiliaryTip(final Tip auxiliaryTip) {
    return otherTip = auxiliaryTip;
  }

  protected void setCompilationUnit(final CompilationUnit ¢) {
    compilationUnit = ¢;
    fileName = English.unknownIfNull(¢.getJavaElement(), IJavaElement::getElementName);
    notify.begin();
  }

  protected void setNode(final ASTNode currentNode) {
    node = currentNode;
    notify.setNode();
  }

  protected void setRewrite(final ASTRewrite currentRewrite) {
    rewrite = currentRewrite;
  }

  protected void setTipper(final Tipper<?> currentTipper) {
    tipper = currentTipper;
    if (tipper() == null)
      notify.noTipper();
    else
      notify.tipperAccepts();
  }

  public abstract class __ {
    // @formatter:off
    protected Tip auxiliaryTip() { return getAuxiliaryTip(); }
    protected ASTRewrite rewrite() { return getRewrite(); }
    protected Traversal self() { return Traversal.this.self(); }
    protected Tip tip() { return getTip(); }
    // @formatter:on

    protected String node() {
      final ASTNode $ = self().getNode();
      return String.format("%s(%s)", English.name($), Trivia.gist($));
    }
  }

  public int rewriteCount() {
    return rewriteCount.get();
  }
}