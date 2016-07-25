package il.org.spartan.refactoring.contexts;


import il.org.spartan.lazy.*;
import il.org.spartan.lazy.Cookbook.Cell;

import java.util.function.*;

import org.eclipse.jdt.core.dom.*;


/** @author Yossi Gil
 * @since 2016` */
@SuppressWarnings("javadoc")//
public class CurrentRewrite extends CurrentAST.Context {
  /** instantiates this class */
  public CurrentRewrite(CurrentAST ¢) {
    ¢.clone().super();
  }

  /** Inner class, inheriting all of its container's {@link Cell}s, and possibly
   * adding some of its own. Access to container's c {@link Cells} is through
   * the {@link #context} variable.
   * <p>
   * Clients extend this class to create more specialized contexts, adding more
   * {@link Cell}s and {@link Cookbook#recipe(Supplier)}'s.
   * @author Yossi Gil
   * @since 2016` */
  public abstract class Context {
    /** the containing instance */
    @SuppressWarnings("hiding") protected final CurrentRewrite context = CurrentRewrite.this;
  }

  /** Returns an exact copy of this instance
   * @return Created clone object */
  @Override public CurrentRewrite clone() {
    try {
      return (CurrentRewrite) super.clone();
    } catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }
  public final void fillRewrite() {
    context.root().accept(new ASTVisitor() {
      @Override public boolean visit(final Block e) {
        return go(e);
      }
      @Override public boolean visit(final ConditionalExpression e) {
        return go(e);
      }
      @Override public boolean visit(final IfStatement s) {
        return go(s);
      }
      @Override public boolean visit(final InfixExpression e) {
        return go(e);
      }
      @Override public boolean visit(final PrefixExpression e) {
        return go(e);
      }
      @Override public boolean visit(final VariableDeclarationFragment f) {
        return go(f);
      }
      private <N extends ASTNode> boolean go(final N n) {
        return !applicable(n); 
      }
      private boolean applicable(@SuppressWarnings("unused") ASTNode n) {
        return false;
      }
    });
  }
}