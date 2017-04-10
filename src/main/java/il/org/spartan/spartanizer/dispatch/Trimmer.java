package il.org.spartan.spartanizer.dispatch;

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
public class Trimmer extends AbstractTipperNoBetterNameYet {
  /** Instantiates this class */
  public Trimmer() {
    this(Toolbox.defaultInstance());
  }

  public Trimmer(final Toolbox globalToolbox) {
    super("Trimming");
    this.globalToolbox = globalToolbox;
  }

  @Override public ASTRewrite computeMaximalRewrite(final CompilationUnit u, final IMarker m, final Consumer<ASTNode> nodeLogger) {
    final Tips tips = Tips.empty();
    final ASTRewrite $ = ASTRewrite.create(u.getAST());
    currentRewrite = $;
    currentToolbox = !useProjectPreferences ? globalToolbox : getToolboxByPreferences(u);
    currentFileName = English.unknownIfNull(u.getJavaElement(), IJavaElement::getElementName);
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N ¢) {
        setNode(¢);
        if (!check(¢) || !inRange(m, ¢) || disabling.on(¢))
          return true;
        setCurrentTip(findTip(¢));
        if (tip() == null)
          return true;
        for (final Tip t : tips)
          if (t.span.overlapping(tip().span))
            return true;
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
    return $;
  }

  @SafeVarargs public final <N extends ASTNode> Trimmer fix(final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      globalToolbox = new Toolbox();
    }
    globalToolbox.add(c, ts);
    return this;
  }

  @SafeVarargs public final Trimmer fixBloater(final Tipper<?>... ¢) {
    return (Trimmer) fix(InflaterProvider.freshCopyOfAllExpanders(), ¢);
  }

  @SafeVarargs public final Trimmer fixTipper(final Tipper<?>... ¢) {
    return (Trimmer) fix(Toolbox.freshCopyOfAllTippers(), ¢);
  }

  public ASTNode node() {
    return node;
  }

  public Trimmer onException(final TrimmerExceptionListener ¢) {
    exceptionListener = ¢;
    return this;
  }

  public ASTRewrite rewrite() {
    return currentRewrite;
  }

  public void setNode(final ASTNode currentNode) {
    node = currentNode;
    notify.setNode();
  }

  public void setCurrentTip(final Tip currentTip) {
    this.currentTip = currentTip;
  }
  public void clearTipper() {
    this.currentTipper = null;
  }

  public void setTipper(final Tipper<?> currentTipper) {
    this.currentTipper = currentTipper;
    if (tipper() == null)
      notify.noTipper();
    else
      notify.tipperAccepts();
  }

  public Tip tip() {
    return currentTip;
  }

  public Tipper<?> tipper() {
    return currentTipper;
  }

  private void setTip(final Tip ¢) {
    currentTip = ¢;
    if (¢ != null)
      notify.tipperTip();
  }

  protected <N extends ASTNode> Tipper<N> getTipper(final N ¢) {
    final Tipper<N> $ = currentToolbox.firstTipper(¢);
    setTipper($);
    return $;
  }

  @Override protected ASTVisitor tipsCollector(final Tips into) {
    Toolbox.refresh(this);
    currentFileName = English.unknownIfNull(compilationUnit, λ -> English.unknownIfNull(λ.getJavaElement(), IJavaElement::getElementName));
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N n) {
        setNode(n);
        if (!check(n) || disabling.on(n))
          return true;
        final Tipper<N> $ = findTipper(n);
        return $ == null || robust.lyTrue(() -> {
          setCurrentTip($.tip(n, exclude));
          if (tip() == null)
            return;
          into.removeIf(λ -> λ.highlight.overlapping(tip().highlight));
          into.add(tip());
        }, swallow);
      }

      @Override protected void initialization(final ASTNode ¢) {
        currentToolbox = !useProjectPreferences || !(¢ instanceof CompilationUnit) ? globalToolbox : getToolboxByPreferences((CompilationUnit) ¢);
        disabling.scan(¢);
      }
    };
  }

  <N extends ASTNode> Tip findTip(final N ¢) {
    return robust.lyNull(() -> {
      setTip(null);
      final Tipper<N> $ = findTipper(¢);
      if ($ == null)
        return null;
      setTip($.tip(¢));
      return currentTip;
    }, swallow);
  }

  protected <N extends ASTNode> Tipper<N> findTipper(final N ¢) {
    return robust.lyNull(() -> {
      final Tipper<N> $ = currentToolbox.firstTipper(¢);
      setTipper($);
      return $;
    }, swallow);
  }

  public final Taps notify = new Taps()//
      .append(new Tap() {
  //@formatter:off
        @Override public void setNode() { setCurrentTip(null); }
        //@formatter:on
      }).append(new ProgressTapper())//
      .append(new TrimmerMonitor(this));
  private ASTNode node;
  private ASTRewrite currentRewrite;
  private Tip currentTip;
  private Tipper<?> currentTipper;
  String currentFileName;
  Toolbox currentToolbox;
  TrimmerExceptionListener exceptionListener = λ -> monitor.logToFile(λ, this, tip(), tipper(), currentFileName);
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
    /** @formatter:off */
    public Taps append(final Tap ¢) { inner.add(¢); return this; }
    @Override public void noTipper() { inner.forEach(Tap::noTipper); }
    @Override public void setNode() { inner.forEach(Tap::setNode); }
    @Override public void tipperAccepts() { inner.forEach(Tap::tipperAccepts); }
    @Override public void tipperRejects() { inner.forEach(Tap::tipperRejects); }
    @Override public void tipperTip() { inner.forEach(Tap::tipperTip); }
    @Override public void tipPrune() { inner.forEach(Tap::setNode); }
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