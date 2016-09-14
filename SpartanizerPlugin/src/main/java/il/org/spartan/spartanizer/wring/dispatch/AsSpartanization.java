package il.org.spartan.spartanizer.wring.dispatch;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** An adapter that converts the @{link Wring} protocol into that of
 * {@link Spartanization}
 * @author Yossi Gil
 * @since 2015/07/25 */
public final class AsSpartanization extends Spartanization {
  final Wring<ASTNode> inner;

  /** Instantiates this class
   * @param inner The wring we wish to convert
   * @param name The title of the refactoring */
  @SuppressWarnings("unchecked") public AsSpartanization(final Wring<? extends ASTNode> inner, final String name) {
    super(name);
    this.inner = (Wring<ASTNode>) inner;
  }

  // TODO: Ori, how come we need this parameter?

  @Override protected ASTVisitor collectSuggestions(final List<Rewrite> $, @SuppressWarnings("unused") final CompilationUnit __) {
    return new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(N ¢) {
        if (!inner.claims(¢) || inner.cantWring(¢))
          return true;
        $.add(inner.make(¢));
        return true;
      }
    };
  }

  @Override protected void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    u.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(N ¢) {
        if (inRange(m, ¢))
          inner.make(¢).go(r, null);
        return true;
      }
    });
  }
}
