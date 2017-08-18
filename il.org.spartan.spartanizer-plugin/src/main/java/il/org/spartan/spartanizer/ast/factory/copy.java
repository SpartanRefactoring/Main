package il.org.spartan.spartanizer.ast.factory;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

/** An empty {@code enum} with a variety of {@code public
 * static} functions for restructuring expressions.
 * @author Yossi Gil
 * @since 2015-07-21 */
public enum copy {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  static Iterable<Expression> adjust(final Operator o, final Collection<Expression> xs) {
    return o != il.org.spartan.spartanizer.ast.navigate.op.MINUS2 ? xs
        : xs.stream().map(λ -> subject.operand(λ).to(il.org.spartan.spartanizer.ast.navigate.op.MINUS1)).collect(toList());
  }
  /** Duplicate all {@link ASTNode} objects found in a given list into another
   * list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(final Iterable<N> from, final Collection<N> into) {
    from.forEach(λ -> into(λ, into));
  }
  /** Duplicate a {@link Statement} into another list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(final N from, final Collection<N> into) {
    into.add(copy.of(from));
  }
  public static void modifiers(final Iterable<IExtendedModifier> from, final Collection<IExtendedModifier> to) {
    for (final IExtendedModifier ¢ : from)
      if (¢.isModifier())
        to.add(copy.of((Modifier) ¢));
      else if (¢.isAnnotation())
        to.add(copy.of((Annotation) ¢));
  }
  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned __. */
  @SuppressWarnings("unchecked") public static <N extends ASTNode> N of(final N ¢) {
    return ¢ == null ? null : (N) ASTNode.copySubtree(¢.getAST(), ¢);
  }
  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned __. */
  public static <N extends ASTNode> Collection<N> of(final Collection<N> ¢) {
    return ¢.stream().map(copy::of).collect(toList());
  }
}
