package il.org.spartan.spartanizer.wring.dispatch;

import org.eclipse.jdt.core.dom.*;

/** Weirdo that converts the type specific visit functions, into a single call
 * to {@link #go(ASTNode)}. Needless to say, this is foolish! You can use
 * {@link #preVisit(ASTNode)} or {@link #preVisit2(ASTNode)} instead. Currently,
 * we do not because some of the tests rely on the functions here returning
 * false/true, or for no reason. No one really know...
 * @author Yossi Gil
 * @year 2016
 * @see ExclusionManager */
public abstract class DispatchingVisitor extends ASTVisitor {
  protected final ExclusionManager exclude = Trimmer.makeExcluder();

  @Override public final boolean visit(final Assignment ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final Block ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final CastExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final ConditionalExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final EnhancedForStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final EnumDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final FieldDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final IfStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final InfixExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final MethodDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final MethodInvocation ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final NormalAnnotation ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final PostfixExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final PrefixExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final ReturnStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final SingleVariableDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final SuperConstructorInvocation ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final TypeDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(final VariableDeclarationFragment ¢) {
    return cautiousGo(¢);
  }

  protected abstract <N extends ASTNode> boolean go(final N n);

  private boolean cautiousGo(final ASTNode ¢) {
    return !exclude.isExcluded(¢) && go(¢);
  }
}