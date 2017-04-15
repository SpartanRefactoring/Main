package il.org.spartan.spartanizer.trimming;

import static java.util.stream.Collectors.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.preferences.revision.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** A smorgasboard containing lots of stuff factored out of
 * {@link TrimmerImplementation}
 * <ol>
 * <li>Configuration: sometime you wish to disable some of the tippers.
 * </ol>
 * @author Yossi Gil
 * @since 2015/07/10 */
public abstract class Trimmer implements Selfie<Trimmer> {
  @SafeVarargs public final <N extends ASTNode> Trimmer addSingleTipper(final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      globalConfiguration = new Configuration();
    }
    globalConfiguration.add(c, ts);
    return this;
  }

  public Tip auxiliaryTip() {
    return auxiliaryTip;
  }

  public void clearTipper() {
    tipper = null;
  }

  /** Checks a Compilation Unit (outermost ASTNode in the Java Grammar) for
   * tipper tips
   * @param u what to check
   * @param ¢ TODO
   * @return a collection of {@link Tip} objects each containing a
   *         spartanization tip */
  public Tips collectTips(final CompilationUnit ¢) {
    tips = Tips.empty();
    ¢.accept(tipsCollector(tips));
    return tips;
  }

  public abstract ASTRewrite go(CompilationUnit u);


  public Configuration currentConfiguration() {
    return currentConfiguration;
  }

  public TextEditGroup currentEditGroup() {
    return currentEditGroup;
  }

  public Range getRange() {
    return range;
  }

  public ASTRewrite getRewrite() {
    return rewrite;
  }

  public ASTNode node() {
    return node;
  }

  public TrimmingTappers pop() {
    return notify.pop();
  }

  public Trimmer push(final TrimmingTapper ¢) {
    notify.push(¢);
    return this;
  }

  public ASTRewrite rewrite() {
    return getRewrite();
  }

  @Override public Trimmer self() {
    return this;
  }

  public Tip setAuxiliaryTip(final Tip auxiliaryTip) {
    return this.auxiliaryTip = auxiliaryTip;
  }

  public Trimmer setCurrentConfiguration(final Configuration currentConfiguration) {
    this.currentConfiguration = currentConfiguration;
    return this;
  }

  public void setNode(final ASTNode currentNode) {
    node = currentNode;
    notify.setNode();
  }

  public Trimmer setRange(Range ¢) {
    return self(() -> this.range = ¢);
  }

  public void setRewrite(final ASTRewrite currentRewrite) {
    rewrite = currentRewrite;
  }

  public Tip tip() {
    return tip;
  }

  public Tipper<?> tipper() {
    return tipper;
  }

  public abstract ASTVisitor tipsCollector(Tips $);

  public Trimmer useProjectPreferences() {
    useProjectPreferences = true;
    configurations.clear();
    return this;
  }

  @SuppressWarnings("static-method") protected <N extends ASTNode> boolean check(@SuppressWarnings("unused") final N __) {
    return true;
  }

  protected CompilationUnit compilationUnit() {
    return compilationUnit;
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

  protected void setCompilationUnit(final CompilationUnit ¢) {
    setCurrentConfiguration(!useProjectPreferences ? globalConfiguration : getPreferredConfiguration(¢));
    fileName = English.unknownIfNull(¢.getJavaElement(), IJavaElement::getElementName);
  }

  protected void setTipper(final Tipper<?> currentTipper) {
    tipper = currentTipper;
    if (tipper() == null)
      notify.noTipper();
    else
      notify.tipperAccepts();
  }

  public final Int rewriteCount = new Int();
  public final Configuration configuration = Configurations.allClone();
  public Configuration globalConfiguration = Configurations.all();
  public final TrimmingTappers notify = new TrimmingTappers()//
      .push(new TrimmerMonitor(this)) //
      .push(new TrimmingTapper() {
        @Override public void begin() {
          rewriteCount.clear();
        }

        @Override public void tipRewrite() {
          rewriteCount.step();
        }
      });
  private Tip auxiliaryTip;
  private CompilationUnit compilationUnit;
  private Configuration currentConfiguration;
  private TextEditGroup currentEditGroup;
  private Range range;
  private Tips tips;
  protected final Map<IProject, Configuration> configurations = new HashMap<>();
  protected String fileName;
  protected boolean firstAddition = true;
  protected ASTNode node;
  protected ASTRewrite rewrite;
  protected Tip tip;
  protected Tipper<?> tipper;
  protected boolean useProjectPreferences;

  public abstract class With {
    public Trimmer current() {
      return Trimmer.this;
    }
  }
}