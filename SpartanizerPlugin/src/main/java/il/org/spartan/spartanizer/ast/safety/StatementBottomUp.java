package il.org.spartan.spartanizer.ast.safety;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-29 */
public abstract class StatementBottomUp<R> extends Reducer<R> {
  protected R map(final AssertStatement ¢) {
    return atomic(¢, ¢.getExpression(), ¢.getMessage());
  }

  protected R map(final Block b) {
    R $ = reduce();
    for (final Statement ¢ : statements(b))
      $ = reduce($, map(¢));
    return $;
  }

  protected R map(final BreakStatement ¢) {
    return atomic(¢, ¢.getLabel());
  }

  protected R map(final ConstructorInvocation ¢) {
    return atomic(¢);
  }

  protected R map(final ContinueStatement ¢) {
    return atomic(¢);
  }

  protected R map(final DoStatement ¢) {
    return map(¢.getBody());
  }

  protected R map(final EmptyStatement ¢) {
    return atomic(¢);
  }

  protected R map(final EnhancedForStatement ¢) {
    return map(¢.getBody());
  }

  protected R map(@SuppressWarnings("unused") final Expression __) {
    return reduce();
  }

  protected R map(final ExpressionStatement ¢) {
    return atomic(¢);
  }

  protected R map(final IfStatement ¢) {
    return reduce(map(expression(¢)), map(then(¢)), map(elze(¢)));
  }

  protected R map(final LabeledStatement ¢) {
    return reduce(map(¢.getLabel()), map(¢.getBody()));
  }

  protected R map(final ReturnStatement ¢) {
    return atomic(¢,¢.getExpression());
  }

  public final R map(final Statement ¢) {
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
        return map((TypeDeclarationStatement) ¢);
      case VARIABLE_DECLARATION_STATEMENT:
        return map((VariableDeclarationStatement) ¢);
      case WHILE_STATEMENT:
        return map((WhileStatement) ¢);
      default:
        assert fault.unreachable() : fault.specifically(String.format("Missing 'case' in switch for class: %s", ¢.getClass().getSimpleName()));
        return reduce();
    }
  }

  protected R map(final SuperConstructorInvocation ¢) {
    return reduce(map(expression(¢)), reduceExpressions(arguments(¢)));
  }

  protected R map(final SuperMethodInvocation ¢) {
    return reduce(map(expression(¢)), reduceExpressions(arguments(¢)));
  }

  protected R reduceExpressions(final List<Expression> xs) {
    R $ = reduce();
    for (final Expression ¢ : xs)
      $ = reduce($, map(¢));
    return $;
  }

  protected R map(final SynchronizedStatement ¢) {
    return reduce(map(¢.getExpression()), map(¢.getBody()));
  }

  protected R map(final TryStatement ¢) {
    return reduce(//
        reduceResources(¢), //
        map(¢.getBody()), //
        reduceCatches(¢), //
        map(¢.getFinally())//
    );
  }

  protected R reduceResources(final TryStatement s) {
    R $ = reduce();
    for (final VariableDeclarationExpression x : resources(s))
      for (final VariableDeclarationFragment ¢ : fragments(x))
        $ = reduce($, map(¢.getInitializer()));
    return $;
  }

  protected R reduceCatches(final TryStatement s) {
    R $ = reduce();
    for (final CatchClause ¢ : catchClauses(s))
      $ = reduce($, map(¢.getBody()));
    return $;
  }

  protected R atomic(final TypeDeclarationStatement ¢) {
    // TODO Yossi Gil Auto-generated method stub for map
    if (new Object().hashCode() != 0)
      throw new AssertionError("Method 'StatementBottomUp::map' not implemented yet by yossi");
    return null;
  }

  protected R map(final VariableDeclarationStatement ¢) {
    // TODO Yossi Gil Auto-generated method stub for map
    if (new Object().hashCode() != 0)
      throw new AssertionError("Method 'StatementBottomUp::map' not implemented yet by yossi");
    return null;
  }

  protected R map(final WhileStatement ¢) {
    // TODO Yossi Gil Auto-generated method stub for map
    if (new Object().hashCode() != 0)
      throw new AssertionError("Method 'StatementBottomUp::map' not implemented yet by yossi");
    return null;
  }

  protected R atom(@SuppressWarnings("unused") final Statement __) {
    return reduce();
  }

  protected R atomic(final Statement s, final Expression... xs) {
    return reduce(atom(s), reduceExpressions(as.list(xs)));
  }
}
