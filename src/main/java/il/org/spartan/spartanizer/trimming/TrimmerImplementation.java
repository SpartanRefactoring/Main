package il.org.spartan.spartanizer.trimming;

import static il.org.spartan.spartanizer.engine.Tip.*;

import static java.util.stream.Collectors.*;

import java.util.function.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.bloater.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.plugin.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** Apply a {@link Configuration} to a tree. 
 * Issues are
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
public class TrimmerImplementation extends Trimmer {
  /** Instantiates this class */
  public TrimmerImplementation() {
    this(Configurations.all());
  }

  public TrimmerImplementation(final Configuration globalConfiguration) {
    super(system.myShortClassName());
    this.globalConfiguration = globalConfiguration;
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
          setAuxiliaryTip(λ);
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

  public ASTRewrite computeMaximalRewrite(final CompilationUnit ¢) {
    return computeMaximalRewrite(¢, null, null);
  }

  @Override public ASTRewrite computeMaximalRewrite(final CompilationUnit u, final IMarker m, final Consumer<ASTNode> nodeLogger) {
    setCompilationUnit(u);
    topDown(u, m, nodeLogger);
    return rewrite();
  }

  @SafeVarargs public final <N extends ASTNode> GUIConfigurationApplicator fix(final Class<N> c, final Tipper<N>... ts) {
    if (firstAddition) {
      firstAddition = false;
      globalConfiguration = new Configuration();
    }
    globalConfiguration.add(c, ts);
    return this;
  }

  @SafeVarargs public final GUIConfigurationApplicator fixBloater(final Tipper<?>... ¢) {
    return fix(InflaterProvider.freshCopyOfAllExpanders(), ¢);
  }

  @SafeVarargs public final GUIConfigurationApplicator fixTipper(final Tipper<?>... ¢) {
    return fix(Configurations.allClone(), ¢);
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
          setAuxiliaryTip(t);
          if ((setAuxiliaryTip(t)) != null && Tip.overlap(t.span, tip().span)) {
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
      final Tipper<N> $ = currentConfiguration().firstTipper(¢);
      setTipper($);
      return $;
    }, λ -> note.bug(λ));
  }

  @Override protected ASTVisitor tipsCollector(final Tips into) {
    Configurations.refresh(this);
    fileName = English.unknownIfNull(compilationUnit(), λ -> English.unknownIfNull(λ.getJavaElement(), IJavaElement::getElementName));
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
        }, note::bug);
      }

      @Override protected void initialization(final ASTNode ¢) {
        setCurrentConfiguration(!useProjectPreferences || !(¢ instanceof CompilationUnit) ? globalConfiguration
            : getPreferredConfiguration((CompilationUnit) ¢));
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
    }, λ -> note.bug(λ));
  }

  void setTip(final Tip ¢) {
    tip = ¢;
    if (¢ != null)
      notify.tipperTip();
  }


}