package il.org.spartan.spartanizer.ast.safety;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class {@link }
 * @author Yossi Gil
 * @since 2017-01-29 */
abstract class MapOfLeaves<R> extends Reduce<R> {
  protected R map(final BooleanLiteral ¢) {
    return leaf(¢);
  }

  protected R map(final CharacterLiteral ¢) {
    return leaf(¢);
  }

  protected R map(final EmptyStatement ¢) {
    return leaf(¢);
  }

  protected R leaf(@SuppressWarnings("unused") final ASTNode ¢) {
    return reduce();
  }

  protected R map(final Modifier ¢) {
    return leaf(¢);
  }

  protected R map(final NullLiteral ¢) {
    return leaf(¢);
  }

  protected R map(final NumberLiteral ¢) {
    return leaf(¢);
  }

  protected R map(final SimpleName ¢) {
    return leaf(¢);
  }

  protected R map(final StringLiteral ¢) {
    return leaf(¢);
  }
}