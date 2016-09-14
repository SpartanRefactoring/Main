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
  @Override protected ASTVisitor collect(final List<Rewrite> $, @SuppressWarnings("unused") final CompilationUnit __) {
    return new ASTVisitor() {
      @Override public boolean visit(final Block it) {
        return process(it);
      }

      @Override public boolean visit(final ConditionalExpression x) {
        return process(x);
      }

      @Override public boolean visit(final IfStatement it) {
        return process(it);
      }
      
      @Override public boolean visit(final ForStatement it) {
        return process(it);
      }
      
      @Override public boolean visit(final WhileStatement it) {
        return process(it);
      }

      @Override public boolean visit(final InfixExpression it) {
        return process(it);
      }

      @Override public boolean visit(final PrefixExpression it) {
        return process(it);
      }

      @Override public boolean visit(final VariableDeclarationFragment it) {
        return process(it);
      }

      <N extends ASTNode> boolean process(final N n) {
        if (!inner.claims(n) || inner.cantWring(n))
          return true;
        $.add(inner.make(n));
        return true;
      }
    };
  }

  @Override protected void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final Block e) {
        return go(e);
      }

      @Override public boolean visit(final ConditionalExpression x) {
        return go(x);
      }

      @Override public boolean visit(final IfStatement s) {
        return go(s);
      }

      @Override public boolean visit(final InfixExpression x) {
        return go(x);
      }

      @Override public boolean visit(final PrefixExpression x) {
        return go(x);
      }

      @Override public boolean visit(final VariableDeclarationFragment f) {
        return go(f);
      }

      <N extends ASTNode> boolean go(final N n) {
        if (inRange(m, n))
          inner.make(n).go(r, null);
        return true;
      }
    });
  }
}
