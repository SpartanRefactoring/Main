package il.org.spartan.refactoring.suggestions;

import org.eclipse.jdt.core.dom.*;

abstract class DispatchingVisitor extends ASTVisitor {
  @Override public final boolean visit(final Assignment ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final Block ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final ClassInstanceCreation c) {
    return g0(c);
  }
  @Override public final boolean visit(final ConditionalExpression e) {
    return g0(e);
  }
  @Override public final boolean visit(final EnumDeclaration ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final IfStatement ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final InfixExpression ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final MethodDeclaration ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final MethodInvocation ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final NormalAnnotation ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final PostfixExpression ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final PrefixExpression ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final ReturnStatement ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final SingleVariableDeclaration d) {
    return g0(d);
  }
  @Override public final boolean visit(final SuperConstructorInvocation ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final SwitchStatement s) {
    return g0(s);
  }
  @Override public final boolean visit(final TypeDeclaration ¢) {
    return g0(¢);
  }
  @Override public final boolean visit(final VariableDeclarationFragment ¢) {
    return g0(¢);
  }
  private boolean g0(final ASTNode n) {
    return allow(n) && go(n);
  }
  boolean allow(final ASTNode n) {
    return true;
  }
  <N extends ASTNode> boolean go(final N n) {
    return true;
  }
}