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
public class Trimmer2 extends Trimmer {



  @Override public ASTRewrite computeMaximalRewrite(final CompilationUnit u, final IMarker m, final Consumer<ASTNode> nodeLogger) {
    final Tips tips = Tips.empty();
    currentToolbox = !useProjectPreferences ? globalToolbox : getToolboxByPreferences(u);
    fileName = English.unknownIfNull(u.getJavaElement(), IJavaElement::getElementName);
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
    for (Tip ¢ : tips) {
      super.tip = ¢;
      tip().go(getRewrite(), currentEditGroup());
      notify.tipRewrite();
    }
    return getRewrite(); 
  }



  @Override protected ASTVisitor tipsCollector(final Tips into) {
    Toolbox.refresh(this);
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
          into.removeIf(λ -> overlap(λ.highlight, tip().highlight));
          into.add(tip());
        }, swallow);
      }

      @Override protected void initialization(final ASTNode ¢) {
        currentToolbox = !useProjectPreferences || !(¢ instanceof CompilationUnit) ? globalToolbox : getToolboxByPreferences((CompilationUnit) ¢);
        disabling.scan(¢);
      }
    };
  }

  <N extends ASTNode> void findTip(final N ¢) {
    robust.ly(() -> {
      setTip(null);
      final Tipper<N> $ = findTipper(¢);
      if ($ != null)
        setTip($.tip(¢));
    }, swallow);
  }
}