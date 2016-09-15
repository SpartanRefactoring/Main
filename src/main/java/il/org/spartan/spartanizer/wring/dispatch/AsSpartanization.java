package il.org.spartan.spartanizer.wring.dispatch;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** An adapter that converts the protocol of a single @{link Wring} instance
 * into that of {@link Spartanization}. This class must eventually die.
 * @author Yossi Gil
 * @since 2015/07/25 */
public final class AsSpartanization extends Spartanization {
  final Wring<ASTNode> wring;
  final Class<? extends ASTNode> clazz;

  /** Instantiates this class
   * @param wring The wring we wish to convert
   * @param name The title of the refactoring */
  @SuppressWarnings("unchecked") public AsSpartanization(final Wring<? extends ASTNode> w) {
    super(w.name());
    this.wring = (Wring<ASTNode>) w;
    this.clazz = w.myActualOperandsClass();
    assert this.clazz != null : "Oops, cannot find kind of operands of " + w.name();
  }

  // TODO: Ori, how come we need this parameter?
  @Override protected ASTVisitor collectSuggestions(final List<Rewrite> $, @SuppressWarnings("unused") final CompilationUnit __) {
    return new ASTVisitor() {
      @Override public void preVisit(ASTNode ¢) {
        super.preVisit(¢);
        if (¢.getClass() == clazz || wring.demandsToSuggestButPerhapsCant(¢) || wring.canSuggest(¢))
          $.add(wring.suggest(¢));
      }
    };
  }

  @Override protected void consolidateSuggestions(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    u.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        super.preVisit(¢);
        if (¢.getClass() == clazz || wring.demandsToSuggestButPerhapsCant(¢) || wring.canSuggest(¢) || inRange(m, ¢))
          wring.suggest(¢).go(r, null);
      }
    });
  }
}
