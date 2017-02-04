package il.org.spartan.spartanizer.ast.factory;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An empty <code><b>enum</b></code> with a variety of {@code public
 * static} functions for restructuring expressions.
 * @author Yossi Gil
 * @since 2015-07-21 */
public enum copy {
  ;
  @NotNull static List<Expression> adjust(final Operator o, @NotNull final List<Expression> xs) {
    return o != wizard.MINUS2 ? xs : xs.stream().map(λ -> subject.operand(λ).to(wizard.MINUS1)).collect(Collectors.toList());
  }

  /** Duplicate all {@link ASTNode} objects found in a given list into another
   * list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(@NotNull final List<N> from, @NotNull final List<N> into) {
    from.forEach(λ -> into(λ, into));
  }

  /** Duplicate a {@link Statement} into another list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(final N from, @NotNull final List<N> into) {
    into.add(copy.of(from));
  }

  public static void modifiers(@NotNull final List<IExtendedModifier> from, @NotNull final List<IExtendedModifier> to) {
    for (final IExtendedModifier ¢ : from)
      if (¢.isModifier())
        to.add(copy.of((Modifier) ¢));
      else if (¢.isAnnotation())
        to.add(copy.of((Annotation) ¢));
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @SuppressWarnings("unchecked") @Nullable public static <¢ extends ASTNode> ¢ of(@Nullable final ¢ ¢) {
    return ¢ == null ? null : (¢) copySubtree(¢.getAST(), ¢);
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢s JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @SuppressWarnings("unchecked") public static <¢ extends ASTNode> List<¢> of(@NotNull final List<¢> ¢s) {
    return ¢s.stream().map(λ -> (¢) copySubtree(λ.getAST(), λ)).collect(Collectors.toList());
  }

  @Nullable public static Expression ofWhileExpression(final WhileStatement ¢) {
    return of(expression(¢));
  }
}
