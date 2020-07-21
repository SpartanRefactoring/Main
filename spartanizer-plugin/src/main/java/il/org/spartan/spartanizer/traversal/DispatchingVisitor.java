package il.org.spartan.spartanizer.traversal;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;

/** A visitor hack converting the type specific visit functions, into a single
 * call to {@link #go(ASTNode)}. Needless to say, this is foolish! You can use
 * {@link #preVisit(ASTNode)} or {@link #preVisit2(ASTNode)} instead. Currently,
 * we do not because some of the tests rely on the functions here returning
 * false/true, or for no reason. No one really know...
 * @contributor Oren Afek
 * @see ExclusionManager
 * @author Yossi Gil
 * @since Sep 18, 2016 */
public abstract class DispatchingVisitor extends ASTVisitor {
  public DispatchingVisitor() {
    super(true);
  }

  static abstract class ME extends DispatchingVisitor implements ZZZZ.ID {
    static ME p1 = new ME() {
      <@All N extends ASTNode> boolean f(N ¢) {
        return cautiousGo(¢);
      }

      @Override protected <N extends ASTNode> boolean go(N n) {
        return falze.forgetting(n); 
      }
    };
    static ME p2 = new ME() {
      <@Only({ Block.class, DoStatement.class }) N extends ASTNode> boolean visit(N ¢) {
        return cautiousGo(¢);
      }
      @Override protected <N extends ASTNode> boolean go(N n) {
        return false;
      }
    };
    static ME p3 = new ME() {
      <@Only({ Block.class, DoStatement.class }) N extends ASTNode> boolean g(N ¢) {
        return cautiousGo(¢);
      }
      @Override protected <N extends ASTNode> boolean go(N n) {
        return false;
      }
    };
  }

  private boolean initialized;

  boolean cautiousGo(final ASTNode ¢) {
    return go(¢);
  }
  protected abstract <N extends ASTNode> boolean go(N n);
  protected void initialization(@SuppressWarnings("unused") final ASTNode type) {
    // to be overridden
  }
  @Override public void preVisit(final ASTNode ¢) {
    if (initialized)
      return;
    initialization(¢);
    initialized = true;
  }
  @Override public final boolean visit(final AnnotationTypeDeclaration ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final ArrayAccess ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final Assignment ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final Block ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final BreakStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final DoStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final CastExpression ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final CatchClause ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final ClassInstanceCreation ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final EmptyStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final ConditionalExpression ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final ContinueStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final EnhancedForStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final EnumConstantDeclaration ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final EnumDeclaration ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final ExpressionStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final FieldDeclaration ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final ForStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final IfStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final InfixExpression ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final Initializer ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final InstanceofExpression ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final Javadoc ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final LambdaExpression ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final MethodDeclaration ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final AssertStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final MethodInvocation ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final Modifier ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final NormalAnnotation ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final NumberLiteral ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final ParenthesizedExpression ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final PostfixExpression ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final PrefixExpression ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final QualifiedType ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final ReturnStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final SingleMemberAnnotation ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final SingleVariableDeclaration ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final SuperConstructorInvocation ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final SwitchCase ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final SwitchStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final ThrowStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final TryStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final TypeDeclaration ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final TypeParameter ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final VariableDeclarationExpression ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final VariableDeclarationFragment ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final VariableDeclarationStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final WhileStatement ¢) {
    return cautiousGo(¢);
  }
  @Override public final boolean visit(final WildcardType ¢) {
    return cautiousGo(¢);
  }
}
