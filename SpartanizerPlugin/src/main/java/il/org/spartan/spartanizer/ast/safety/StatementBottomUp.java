package il.org.spartan.spartanizer.ast.safety;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-29 */
public abstract class StatementBottomUp<R> extends Reducer<R> {
  protected R map(final AssertStatement ¢) {
    return atomic(¢.getExpression(), ¢.getMessage());
  }

  protected R map(final Block b) {
    R $ = reduce();
    for (final Statement ¢ : statements(b))
      $ = reduce($, map(¢));
    return $;
  }

  protected R map(final BreakStatement ¢) {
    return atomic(¢.getLabel());
  }

  protected R map(final ConstructorInvocation ¢) {
    return reduceExpressions(step.arguments(¢));
  }

  protected R map(final ContinueStatement ¢) {
    return atomic(¢.getLabel());
  }

  protected R map(final DoStatement ¢) {
    return map(¢.getBody());
  }

  protected R map(@SuppressWarnings("unused") final EmptyStatement __) {
    return atomic();
  }

  protected R map(final EnhancedForStatement ¢) {
    return map(¢.getBody());
  }

  protected R map(@SuppressWarnings("unused") final Expression __) {
    return reduce();
  }

  protected R map(final ExpressionStatement ¢) {
    return atomic(¢.getExpression());
  }

  protected R map(final IfStatement ¢) {
    return reduce(map(expression(¢)), map(then(¢)), map(elze(¢)));
  }

  protected R map(final LabeledStatement ¢) {
    return reduce(map(¢.getLabel()), map(¢.getBody()));
  }

  protected R map(final ReturnStatement ¢) {
    return atomic(¢.getExpression());
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
        return map(¢);
      case VARIABLE_DECLARATION_STATEMENT:
        return map((VariableDeclarationStatement) ¢);
      case WHILE_STATEMENT:
        return map((WhileStatement) ¢);
      default:
        assert fault.unreachable() : fault.specifically(String.format("Missing 'case' in switch for class: %s", ¢.getClass().getSimpleName()));
        return reduce();
    }
  }

  protected R map(final VariableDeclarationStatement ¢) {
    return reduce(fragments(¢));
  }

  protected R map(final SuperConstructorInvocation ¢) {
    return reduce(map(expression(¢)), reduceExpressions(arguments(¢)));
  }

  protected R map(final SuperMethodInvocation ¢) {
    return reduce(map(expression(¢)), reduceExpressions(arguments(¢)));
  }

  protected R reduceExpressions(final List<Expression> xs) {
    R $ = reduce();
    if (xs != null)
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

  protected R reduceResources(final TryStatement ¢) {
    return resources(¢).stream().map(this::map).reduce(this::reduce).orElse(reduce());
  }

  protected R map(final VariableDeclarationExpression ¢) {
    return reduce(fragments(¢));
  }

  protected R reduce(final List<VariableDeclarationFragment> ¢) {
    return ¢.stream().map(VariableDeclarationFragment::getInitializer).map(this::map).reduce(this::reduce).orElse(reduce());
  }

  protected R reduceCatches(final TryStatement ¢) {
    return catchClauses(¢).stream().map(CatchClause::getBody).map(this::map).reduce(this::reduce).orElse(reduce());
  }

  protected R atomic(@SuppressWarnings("unused") final TypeDeclarationStatement __) {
    return reduce();
  }

  protected R map(final WhileStatement ¢) {
    return reduce(map(¢.getExpression()), map(¢.getBody()));
  }

  protected R atom() {
    return reduce();
  }

  protected R atomic(final Expression... ¢) {
    return reduce(atom(), reduceExpressions(as.list(¢)));
  }
}
