package il.org.spartan.spartanizer.trimming;

import static il.org.spartan.spartanizer.engine.Tip.*;

import static java.util.stream.Collectors.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.fluent.*;
import nano.ly.*;

/** Apply a {@link Configuration} to a tree. Issues are
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
public class TraversalImplementation extends Traversal {
  /** Instantiates this class */
  public ASTRewrite bottomUp(final CompilationUnit u) {
    setCompilationUnit(u);
    final Tips tips = Tips.empty();
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N ¢) {
        setNode(¢);
        if (wizard.disjoint(¢, getRange()))
          return false;
        if (!check(¢) || disabling.on(¢))
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
        return true;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
    setRewrite(ASTRewrite.create(u.getAST()));
    for (final Tip ¢ : tips) {
      setTip(¢);
      applyTip();
    }
    return getRewrite();
  }

  private void applyTip() {
    tip().go(getRewrite(), currentEditGroup());
    notify.tipRewrite();
  }

  @Override public ASTRewrite go(final CompilationUnit ¢) {
    setCompilationUnit(¢);
    topDown(¢);
    return rewrite();
  }

  @SafeVarargs public final <N extends ASTNode> Traversal restrictTo(final Tipper<N>... ¢) {
    configuration.restrictTo(¢);
    return this;
  }

  public final <N extends ASTNode> Traversal fixBloater(final Tipper<N> ¢) {
    return restrictTo(¢);
  }

  @SafeVarargs public final <N extends ASTNode> Traversal fixTipper(final Tipper<N>... ¢) {
    return restrictTo(¢);
  }

  private void topDown(final CompilationUnit u) {
    final Tips tips = Tips.empty();
    setRewrite(ASTRewrite.create(u.getAST()));
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N ¢) {
        setNode(¢);
        if (wizard.disjoint(¢, getRange()))
          return false;
        if (!check(¢) || disabling.on(¢))
          return true;
        findTip(¢);
        if (tip() == null)
          return true;
        for (final Tip t : tips)
          if (setAuxiliaryTip(t) != null && Tip.overlap(t.span, tip().span)) {
            notify.tipPrune();
            return false;
          }
        tips.add(tip());
        tip().go(rewrite(), currentEditGroup());
        notify.tipRewrite();
        return false;
      }

      @Override protected void initialization(final ASTNode ¢) {
        disabling.scan(¢);
      }
    });
  }

  protected <N extends ASTNode> Tipper<N> findTipper(final N ¢) {
    return robust.lyNull(() -> {
      final Tipper<N> $ = configuration.firstTipper(¢);
      setTipper($);
      return $;
    }, λ -> note.bug(λ));
  }

  @Override public ASTVisitor tipsCollector(final Tips into) {
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