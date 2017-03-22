package il.org.spartan.spartanizer.dispatch;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

/** A visitor hack converting the type specific visit functions, into a single
 * call to {@link #go(ASTNode)}. Needless to say, this is foolish! You can use
 * {@link #preVisit(ASTNode)} or {@link #preVisit2(ASTNode)} instead. Currently,
 * we do not because some of the tests rely on the functions here returning
 * false/true, or for no reason. No one really know...
 * @contributor Oren Afek
 * @see ExclusionManager
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 18, 2016 */
public abstract class DispatchingVisitor extends ASTVisitor {
  public DispatchingVisitor() {
    super(true);
  }

  public final ExclusionManager exclude = new ExclusionManager();
  private boolean initialized;

  boolean cautiousGo(@NotNull final ASTNode ¢) {
    return !exclude.isExcluded(¢) && go(¢);
  }

  protected abstract <N extends ASTNode> boolean go(N n);

  protected void initialization(@SuppressWarnings("unused") final ASTNode __) {
    // to be overridden
  }

  @Override public void preVisit(final ASTNode ¢) {
    if (initialized)
      return;
    initialization(¢);
    initialized = true;
  }

  @Override public final boolean visit(@NotNull final AnnotationTypeDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final ArrayAccess ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final Assignment ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final Block ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final BreakStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final CastExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final CatchClause ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final ClassInstanceCreation ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final ConditionalExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final ContinueStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final EnhancedForStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final EnumConstantDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final EnumDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final ExpressionStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final FieldDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final ForStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final IfStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final InfixExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final Initializer ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final InstanceofExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final Javadoc ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final LambdaExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final MethodDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final MethodInvocation ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final Modifier ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final NormalAnnotation ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final NumberLiteral ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final ParenthesizedExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final PostfixExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final PrefixExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final QualifiedType ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final ReturnStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final SingleMemberAnnotation ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final SingleVariableDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final SuperConstructorInvocation ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final SwitchCase ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final SwitchStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final ThrowStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final TryStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final TypeDeclaration ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final TypeParameter ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final VariableDeclarationExpression ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final VariableDeclarationFragment ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final VariableDeclarationStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final WhileStatement ¢) {
    return cautiousGo(¢);
  }

  @Override public final boolean visit(@NotNull final WildcardType ¢) {
    return cautiousGo(¢);
  }
}
