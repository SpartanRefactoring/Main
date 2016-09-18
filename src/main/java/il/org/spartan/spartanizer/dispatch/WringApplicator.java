package il.org.spartan.spartanizer.dispatch;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wringing.*;

/** An adapter that converts the protocol of a single @{link Wring} instance
 * into that of {@link Applicator}. This class must eventually die.
 * @author Yossi Gil
 * @since 2015/07/25 */
public final class WringApplicator extends Applicator {
  final Wring<ASTNode> wring;
  final Class<? extends ASTNode> clazz;

  /** Instantiates this class
   * @param wring The wring we wish to convert
   * @param name The title of the refactoring */
  @SuppressWarnings("unchecked") public WringApplicator(final Wring<? extends ASTNode> w) {
    super(w.name());
    wring = (Wring<ASTNode>) w;
    clazz = w.myActualOperandsClass();
    assert clazz != null : "Oops, cannot find kind of operands of " + w.name();
  }

  // TODO: Ori, how come we need this parameter?
  @Override protected ASTVisitor collectSuggestions(@SuppressWarnings("unused") final CompilationUnit __, final List<Suggestion> $) {
    return new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
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
