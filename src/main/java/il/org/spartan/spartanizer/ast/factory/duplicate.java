package il.org.spartan.spartanizer.ast.factory;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> functions for restructuring expressions.
 * @author Yossi Gil
 * @since 2015-07-21 */
public enum duplicate {
  ;
  static List<Expression> adjust(final Operator o, final List<Expression> xs) {
    return o != wizard.MINUS2 ? xs : xs.stream().map(¢ -> subject.operand(¢).to(wizard.MINUS1)).collect(Collectors.toList());
  }

  /** Duplicate all {@link ASTNode} objects found in a given list into another
   * list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(final List<N> from, final List<N> into) {
    for (final N ¢ : from)
      into(¢, into);
  }

  /** Duplicate a {@link Statement} into another list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(final N from, final List<N> into) {
    into.add(duplicate.of(from));
  }

  public static void modifiers(final List<IExtendedModifier> from, final List<IExtendedModifier> to) {
    for (final IExtendedModifier ¢ : from)
      if (¢.isModifier())
        to.add(duplicate.of((Modifier) ¢));
      else if (¢.isAnnotation())
        to.add(duplicate.of((Annotation) ¢));
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @SuppressWarnings("unchecked") public static <¢ extends ASTNode> ¢ of(final ¢ ¢) {
    return ¢ == null ? null : (¢) copySubtree(¢.getAST(), ¢);
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢s JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @SuppressWarnings("unchecked") public static <¢ extends ASTNode> List<¢> of(final List<¢> ¢s) {
    return ¢s.stream().map(cent -> (¢) copySubtree(cent.getAST(), cent)).collect(Collectors.toList());
  }
}
