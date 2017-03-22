package il.org.spartan.spartanizer.ast.factory;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An empty {@code enum} with a variety of {@code public
 * static} functions for restructuring expressions.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-21 */
public enum copy {
  ;
  @NotNull static Iterable<Expression> adjust(final Operator o, @NotNull final Collection<Expression> xs) {
    return o != wizard.MINUS2 ? xs : xs.stream().map(λ -> subject.operand(λ).to(wizard.MINUS1)).collect(toList());
  }

  /** Duplicate all {@link ASTNode} objects found in a given list into another
   * list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(@NotNull final Iterable<N> from, final Collection<N> into) {
    from.forEach(λ -> into(λ, into));
  }

  /** Duplicate a {@link Statement} into another list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(final N from, @NotNull final Collection<N> into) {
    into.add(copy.of(from));
  }

  public static void modifiers(@NotNull final Iterable<IExtendedModifier> from, @NotNull final Collection<IExtendedModifier> to) {
    for (@NotNull final IExtendedModifier ¢ : from)
      if (¢.isModifier())
        to.add(copy.of((Modifier) ¢));
      else if (¢.isAnnotation())
        to.add(copy.of((Annotation) ¢));
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @Nullable @SuppressWarnings("unchecked") public static <¢ extends ASTNode> ¢ of(@Nullable final ¢ ¢) {
    return ¢ == null ? null : (¢) copySubtree(¢.getAST(), ¢);
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢s JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @SuppressWarnings("unchecked") public static <¢ extends ASTNode> Collection<¢> of(@NotNull final Collection<¢> ¢s) {
    return ¢s.stream().map(λ -> (¢) copySubtree(λ.getAST(), λ)).collect(toList());
  }

  public static Expression ofWhileExpression(final WhileStatement ¢) {
    return of(expression(¢));
  }
}
