package il.org.spartan.spartanizer.ast.safety;

import static il.org.spartan.utils.monitor.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil
 * @since 2017-01-29 */
public abstract class ASTMapReducer<R> extends Reduce<R> {
  public R map(final ASTNode ¢) {
    return iz.statement(¢) ? map(az.statement(¢)) : iz.expression(¢) ? map(az.expression(¢)) : reduce();
  }

  protected R atomic(final Expression... ¢) {
    return foldl(as.list(¢));
  }

  protected R map(final AbstractTypeDeclaration ¢) {
    return reduce(map(¢.getJavadoc()), foldListModifiers(step.extendedModifiers(¢)), map(¢.getName()), foldl(step.bodyDeclarations(¢)));
  }

  protected R foldListModifiers(final List<IExtendedModifier> ms) {
    R $ = reduce();
    for (final IExtendedModifier ¢ : ms)
      $ = reduce($, map(¢));
    return $;
  }

  protected R map(@SuppressWarnings("unused") final IExtendedModifier __) {
    return reduce();
  }

  protected R map(final ArrayAccess ¢) {
    return reduce(map(¢.getArray()), map(¢.getIndex()));
  }

  protected R map(final ArrayCreation ¢) {
    return reduce(foldl(dimensions(¢)), map(¢.getInitializer()));
  }

  protected R map(final ArrayInitializer ¢) {
    return foldl(expressions(¢));
  }

  protected R map(final AssertStatement ¢) {
    return atomic(¢.getExpression(), ¢.getMessage());
  }

  protected R map(final Assignment ¢) {
    return reduce(map(to(¢)), map(from(¢)));
  }

  protected R map(final Block ¢) {
    return foldl(statements(¢));
  }

  protected R map(@SuppressWarnings("unused") final BooleanLiteral ¢) {
    return reduce();
  }

  protected R map(final BreakStatement ¢) {
    return atomic(¢.getLabel());
  }

  protected R map(final CastExpression ¢) {
    return map(¢.getExpression());
  }

  protected R map(final ClassInstanceCreation ¢) {
    return reduce(map(¢.getExpression()), foldl(arguments(¢)));
  }

  protected R map(final ConditionalExpression ¢) {
    return reduce(map(¢.getExpression()), map(then(¢)), map(elze(¢)));
  }

  protected R map(final ConstructorInvocation ¢) {
    return foldl(arguments(¢));
  }

  protected R map(final ContinueStatement ¢) {
    return atomic(¢.getLabel());
  }

  protected R map(final DoStatement ¢) {
    return map(¢.getBody());
  }

  protected R map(@SuppressWarnings("unused") final EmptyStatement __) {
    return reduce();
  }

  protected R map(final EnhancedForStatement ¢) {
    return map(¢.getBody());
  }

  protected R map(final Expression ¢) {
    if (¢ == null)
      return reduce();
    switch (¢.getNodeType()) {
      case SIMPLE_NAME:
        return map((SimpleName) ¢);
      case QUALIFIED_NAME:
        return map((QualifiedName) ¢);
      case THIS_EXPRESSION:
        return map((ThisExpression) ¢);
      case NUMBER_LITERAL:
        return map((NumberLiteral) ¢);
      case NULL_LITERAL:
        return map((NullLiteral) ¢);
      case STRING_LITERAL:
        return map((StringLiteral) ¢);
      case PREFIX_EXPRESSION:
        return map((PrefixExpression) ¢);
      case INFIX_EXPRESSION:
        return foldl(extract.allOperands((InfixExpression) ¢));
      case CONDITIONAL_EXPRESSION:
        return map((ConditionalExpression) ¢);
      case INSTANCEOF_EXPRESSION:
        return map((InstanceofExpression) ¢);
      case ARRAY_ACCESS:
        return map((ArrayAccess) ¢);
      case PARENTHESIZED_EXPRESSION:
        return map(extract.core(¢));
      case ASSIGNMENT:
        return map((Assignment) ¢);
      case ARRAY_INITIALIZER:
        return map((ArrayInitializer) ¢);
      case ARRAY_CREATION:
        return map((ArrayCreation) ¢);
      case CLASS_INSTANCE_CREATION:
        return map((ClassInstanceCreation) ¢);
      case POSTFIX_EXPRESSION:
        return map((PostfixExpression) ¢);
      case METHOD_INVOCATION:
        return map((MethodInvocation) ¢);
      case SUPER_METHOD_INVOCATION:
        return map((SuperMethodInvocation) ¢);
      case BOOLEAN_LITERAL:
        return map((BooleanLiteral) ¢);
      case CAST_EXPRESSION:
        return map((CastExpression) ¢);
      case LAMBDA_EXPRESSION:
        return map((LambdaExpression) ¢);
      default:
        return bug("Unrecognized type %s NodeType= %d", ¢.getClass(), ¢, box.it(¢.getNodeType()));
    }
  }

  protected R map(final ExpressionStatement ¢) {
    return atomic(¢.getExpression());
  }

  protected R map(final IfStatement ¢) {
    return reduce(map(expression(¢)), map(then(¢)), map(elze(¢)));
  }

  protected R map(final InstanceofExpression ¢) {
    return map(¢.getLeftOperand());
  }

  protected final R foldl(final Iterable<? extends ASTNode> ns) {
    R $ = reduce();
    for (final ASTNode ¢ : ns)
      $ = reduce($, map(¢));
    return $;
  }

  protected R map(final Javadoc j) {
    return step.tags(j).stream().map(λ -> map(λ)).reduce(reduce(), (x1, x2) -> reduce(x1, x2));
  }

  protected R map(final LabeledStatement ¢) {
    return reduce(map(¢.getLabel()), map(¢.getBody()));
  }

  /** Note: this is one of the cases which expressions interact with
   * statements */
  protected R map(@SuppressWarnings("unused") final LambdaExpression __) {
    return reduce();
  }

  protected R map(final MethodInvocation ¢) {
    return reduce(map(expression(¢)), foldl(arguments(¢)));
  }

  protected final R mapList(final List<ASTNode> ns) {
    R $ = reduce();
    for (final ASTNode ¢ : ns)
      $ = reduce($, map(¢));
    return $;
  }

  protected R map(@SuppressWarnings("unused") final NullLiteral ¢) {
    return reduce();
  }

  protected R map(@SuppressWarnings("unused") final NumberLiteral ¢) {
    return reduce();
  }

  protected R map(final PostfixExpression ¢) {
    return map(expression(¢));
  }

  protected R map(final PrefixExpression ¢) {
    return map(expression(¢));
  }

  protected R map(final QualifiedName ¢) {
    return reduce(map(¢.getQualifier()), map(¢.getName()));
  }

  protected R map(final ReturnStatement ¢) {
    return atomic(¢.getExpression());
  }

  protected R map(@SuppressWarnings("unused") final SimpleName ¢) {
    return reduce();
  }

  protected R map(final Statement ¢) {
    if (¢ == null)
      return reduce();
    switch (¢.getNodeType()) {
      case ASSERT_STATEMENT:
        return map((AssertStatement) ¢);
      case BLOCK:
        return map((Block) ¢);
      case BREAK_STATEMENT:
        return map((BreakStatement) ¢);
      case CONSTRUCTOR_INVOCATION:
        return map((ConstructorInvocation) ¢);
      case CONTINUE_STATEMENT:
        return map((ContinueStatement) ¢);
      case DO_STATEMENT:
        return map((DoStatement) ¢);
      case EMPTY_STATEMENT:
        return map((EmptyStatement) ¢);
      case ENHANCED_FOR_STATEMENT:
        return map((EnhancedForStatement) ¢);
      case EXPRESSION_STATEMENT:
        return map((ExpressionStatement) ¢);
      case IF_STATEMENT:
        return map((IfStatement) ¢);
      case LABELED_STATEMENT:
        return map((LabeledStatement) ¢);
      case RETURN_STATEMENT:
        return map((ReturnStatement) ¢);
      case SUPER_CONSTRUCTOR_INVOCATION:
        return map((SuperConstructorInvocation) ¢);
      case SYNCHRONIZED_STATEMENT:
        return map((SynchronizedStatement) ¢);
      case THROW_STATEMENT:
        return map((SuperConstructorInvocation) ¢);
      case TRY_STATEMENT:
        return map((TryStatement) ¢);
      case TYPE_DECLARATION_STATEMENT:
        return map(¢);
      case VARIABLE_DECLARATION_STATEMENT:
        return map((VariableDeclarationStatement) ¢);
      case WHILE_STATEMENT:
        return map((WhileStatement) ¢);
      default:
        assert fault.unreachable() : fault.specifically(String.format("Missing 'case' in switch for class: %s", wizard.nodeName(¢)));
        return reduce();
    }
  }

  protected R map(@SuppressWarnings("unused") final StringLiteral ¢) {
    return reduce();
  }

  protected R map(final SuperConstructorInvocation ¢) {
    return reduce(map(expression(¢)), foldl(arguments(¢)));
  }

  protected R map(final SuperMethodInvocation ¢) {
    return reduce(map(expression(¢)), foldl(arguments(¢)));
  }

  protected R map(final SynchronizedStatement ¢) {
    return reduce(map(¢.getExpression()), map(¢.getBody()));
  }

  protected R map(final ThisExpression ¢) {
    return map(¢.getQualifier());
  }

  protected R map(final TryStatement ¢) {
    return reduce(//
        reduceResources(¢), //
        map(¢.getBody()), //
        reduceCatches(¢), //
        map(¢.getFinally())//
    );
  }

  protected R map(final TypeDeclarationStatement ¢) {
    return map(¢.getDeclaration());
  }

  protected R map(final VariableDeclarationExpression ¢) {
    return reduce(fragments(¢));
  }

  protected R map(final VariableDeclarationStatement ¢) {
    return reduce(fragments(¢));
  }

  protected R map(final WhileStatement ¢) {
    return reduce(map(¢.getExpression()), map(¢.getBody()));
  }

  protected R reduce(final Collection<VariableDeclarationFragment> ¢) {
    return ¢.stream().map(VariableDeclarationFragment::getInitializer).map(this::map).reduce(this::reduce).orElse(reduce());
  }

  protected R reduceCatches(final TryStatement ¢) {
    return catchClauses(¢).stream().map(CatchClause::getBody).map(this::map).reduce(this::reduce).orElse(reduce());
  }

  protected R reduceResources(final TryStatement ¢) {
    return resources(¢).stream().map(this::map).reduce(this::reduce).orElse(reduce());
  }
}
