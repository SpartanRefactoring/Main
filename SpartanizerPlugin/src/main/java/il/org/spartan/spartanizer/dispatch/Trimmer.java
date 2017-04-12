package il.org.spartan.spartanizer.dispatch;

import static il.org.spartan.spartanizer.engine.Tip.*;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.bloater.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** A smorgasboard containing lots of stuff, but its main purpose, which should
 * be factored out somewhere is to apply a {@link Configuration} to a tree. The
 * main difficulties are:
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
public class Trimmer extends AbstractTipperNoBetterNameYet {
  /** Instantiates this class */
  public Trimmer() {
    this(Configurations.defaultConfiguration());
  }

  public Trimmer(final Configuration globalConfiguration) {
    super(system.myShortClassName());
    this.globalConfiguration = globalConfiguration;
  }

  public Tip auxiliaryTip() {
    return auxiliaryTip;
  }

  public ASTRewrite bottomUp(final CompilationUnit u, final IMarker m, final Consumer<ASTNode> nodeLogger) {
    setCompilationUnit(u);
    final Tips tips = Tips.empty();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N ¢) {
        setNode(¢);
        if (!check(¢) || !inRange(m, ¢) || disabling.on(¢))
          return true;
        findTip(¢);
        if (tip() == null)
          return true;
        tips.stream().filter(λ -> overlap(λ.span, tip().span)).collect(toList()).forEach(λ -> {
          auxiliaryTip = λ;
          tips.remove(λ);
          notify.tipPrune();
        });
        if (tip() != null)
          tips.add(tip());
        if (nodeLogger != null)
          nodeLogger.accept(¢);
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
    setRewrite(ASTRewrite.create(u.getAST()));
    for (final Tip ¢ : tips) {
      tip = ¢;
      tip().go(getRewrite(), currentEditGroup());
      notify.tipRewrite();
    }
    return getRewrite();
  }

  public void clearTipper() {
    tipper = null;
  }

  public ASTRewrite computeMaximalRewrite(final CompilationUnit ¢) {
    return computeMaximalRewrite(¢, null, null);
  }

  @Override public ASTRewrite computeMaximalRewrite(final CompilationUnit u, final IMarker m, final Consumer<ASTNode> nodeLogger) {
    setCompilationUnit(u);
    topDown(u, m, nodeLogger);
    return rewrite();
  }

  @SafeVarargs public final <N extends ASTNode> Trimmer fix(final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      globalConfiguration = new Configuration();
    }
    globalConfiguration.add(c, ts);
    return this;
  }

  @SafeVarargs public final Trimmer fixBloater(final Tipper<?>... ¢) {
    return (Trimmer) fix(InflaterProvider.freshCopyOfAllExpanders(), ¢);
  }

  @SafeVarargs public final Trimmer fixTipper(final Tipper<?>... ¢) {
    return (Trimmer) fix(Configurations.freshCopyOfAllTippers(), ¢);
  }

  public ASTRewrite getRewrite() {
    return rewrite;
  }

  public ASTNode node() {
    return node;
  }

  public Trimmer onException(final TrimmerExceptionListener ¢) {
    exceptionListener = ¢;
    return this;
  }

  public ASTRewrite rewrite() {
    return getRewrite();
  }

  public void setNode(final ASTNode currentNode) {
    node = currentNode;
    notify.setNode();
  }

  public void setRewrite(final ASTRewrite currentRewrite) {
    rewrite = currentRewrite;
  }

  public void setTipper(final Tipper<?> currentTipper) {
    tipper = currentTipper;
    if (tipper() == null)
      notify.noTipper();
    else
      notify.tipperAccepts();
  }

  public Tip tip() {
    return tip;
  }

  public Tipper<?> tipper() {
    return tipper;
  }

  private void setCompilationUnit(final CompilationUnit ¢) {
    currentConfiguration = !useProjectPreferences ? globalConfiguration : getPreferredConfiguration(¢);
    fileName = English.unknownIfNull(¢.getJavaElement(), IJavaElement::getElementName);
  }

  private void topDown(final CompilationUnit u, final IMarker m, final Consumer<ASTNode> nodeLogger) {
    final Tips tips = Tips.empty();
    setRewrite(ASTRewrite.create(u.getAST()));
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N ¢) {
        setNode(¢);
        if (!check(¢) || !inRange(m, ¢) || disabling.on(¢))
          return true;
        findTip(¢);
        if (tip() == null)
          return true;
        for (final Tip t : tips) {
          auxiliaryTip = t;
          if ((auxiliaryTip = t) != null && Tip.overlap(t.span, tip().span)) {
            notify.tipPrune();
            return false;
          }
        }
        tips.add(tip());
        tip().go(rewrite(), currentEditGroup());
        notify.tipRewrite();
        if (nodeLogger != null)
          nodeLogger.accept(¢);
        return false;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }

  protected <N extends ASTNode> Tipper<N> findTipper(final N ¢) {
    return robust.lyNull(() -> {
      final Tipper<N> $ = currentConfiguration.firstTipper(¢);
      setTipper($);
      return $;
    }, swallow);
  }

  @Override protected ASTVisitor tipsCollector(final Tips into) {
    Configurations.refresh(this);
    fileName = English.unknownIfNull(compilationUnit, λ -> English.unknownIfNull(λ.getJavaElement(), IJavaElement::getElementName));
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        setNode(n);
        if (!check(n) || disabling.on(n))
          return true;
        final Tipper<N> $ = findTipper(n);
        return $ == null || robust.lyTrue(() -> {
          setTip($.tip(n));
          if (tip() == null)
            return;
          into.removeIf(λ -> Tip.overlap(λ.highlight, tip().highlight));
          into.add(tip());
        }, swallow);
      }

      @Override protected void initialization(final ASTNode ¢) {
        currentConfiguration = !useProjectPreferences || !(¢ instanceof CompilationUnit) ? globalConfiguration : getPreferredConfiguration((CompilationUnit) ¢);
        disabling.scan(¢);
      }
    };
  }

  <N extends ASTNode> void findTip(final N ¢) {
    robust.ly(() -> {
      setTip(null);
      final Tipper<N> $ = findTipper(¢);
      if ($ == null)
        return;
      setTip($.tip(¢));
    }, swallow);
  }

  void setTip(final Tip ¢) {
    tip = ¢;
    if (¢ != null)
      notify.tipperTip();
  }

  public final Taps notify = new Taps()//
      .push(new ProgressTapper())//
      .push(new TrimmerMonitor(this));
  private ASTNode node;
  private ASTRewrite rewrite;
  private Tipper<?> tipper;
  protected Tip tip;
  Tip auxiliaryTip;
  Configuration currentConfiguration;
  String fileName;
  TrimmerExceptionListener exceptionListener = λ -> monitor.logToFile(λ, this, tip(), tipper(), fileName);
  final Consumer<Exception> swallow = λ -> exceptionListener.accept(λ);

  /** A {@link Tap} to update {@link #progressMonitor}
   * @author Yossi Gil
   * @since 2017-04-09 */
  public class ProgressTapper implements Tap {
    /** @formatter:off */
    @Override public void noTipper() { w(1); }
    @Override public void setNode() { w(1); }
    @Override public void tipperAccepts() { w(1); }
    @Override public void tipperRejects() { w(1); }
    @Override public void tipperTip() { w(3); }
    @Override public void tipPrune() { w(2); }
    @Override public void tipRewrite() { w(5); }
    void w(final int w) { progressMonitor().worked(w); }
    //@formatter:on
  }

  public interface Tap {
    /** @formatter:off */
    default void noTipper() {/**/}
    default void setNode()       {/**/}
    default void tipperAccepts() {/**/}
    default void tipperRejects() {/**/}
    default void tipperTip()     {/**/}
    default void tipPrune()      {/**/}
    default void tipRewrite()    {/**/}
    //@formatter:on
  }

  public static class Taps implements Tap {
    @Override public void noTipper() {
      inner.forEach(Tap::noTipper);
    }

    /** @formatter:off */
    public Taps pop() { inner.remove(inner.size()-1); return this; }
    public Taps push(final Tap ¢) { inner.add(¢); return this; }
    @Override public void setNode() { inner.forEach(Tap::setNode); }
    @Override public void tipperAccepts() { inner.forEach(Tap::tipperAccepts); }
    @Override public void tipperRejects() { inner.forEach(Tap::tipperRejects); }
    @Override public void tipperTip() { inner.forEach(Tap::tipperTip); }
    @Override public void tipPrune() { inner.forEach(Tap::tipPrune); }
    @Override public void tipRewrite() { inner.forEach(Tap::tipRewrite); }
    private final List<Tap> inner = new LinkedList<>();
    //@formatter:on
  }

  public abstract class With {
    public Trimmer current() {
      return Trimmer.this;
    }
  }
}