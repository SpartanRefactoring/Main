package il.org.spartan.refactoring.wring;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import il.org.spartan.refactoring.spartanizations.Spartanization;
import il.org.spartan.refactoring.utils.Rewrite;
import il.org.spartan.refactoring.utils.Source;
import il.org.spartan.utils.FileUtils;

/**
 * An adapter that converts the @{link Wring} protocol into that of
 * {@link Spartanization}
 *
 * @author Yossi Gil
 * @since 2015/07/25
 */
public class AsSpartanization extends Spartanization {
  final Wring<ASTNode> inner;

  /**
   * Instantiates this class
   *
   * @param inner The wring we wish to convert
   * @param name The title of the refactoring
   */
  @SuppressWarnings("unchecked") public AsSpartanization(final Wring<? extends ASTNode> inner, final String name) {
    super(name);
    this.inner = (Wring<ASTNode>) inner;
  }
  @Override protected ASTVisitor collect(final List<Rewrite> $) {
    return new ASTVisitor() {
      @Override public boolean visit(final Block it) {
        return process(it);
      }
      @Override public boolean visit(final ConditionalExpression e) {
        return process(e);
      }
      @Override public boolean visit(final IfStatement it) {
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
        if (!inner.scopeIncludes(n) || inner.nonEligible(n))
          return true;
        $.add(inner.make(n));
        return true;
      }
    };
  }
  @Override protected final void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    Source.setASTRewrite(r);
    Source.setCompilationUnit(u);
    try {
      Source.set(FileUtils.readFromFile(Source.getPath().toString()));
    } catch (Exception e) {
      Source.set(null);
    }
    u.accept(new ASTVisitor() {
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
        if (inRange(m, n))
          inner.make(n).go(r, null);
        return true;
      }
    });
  }
}
