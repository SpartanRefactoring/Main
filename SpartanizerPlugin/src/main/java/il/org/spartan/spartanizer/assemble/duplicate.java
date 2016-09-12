package il.org.spartan.spartanizer.assemble;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.spartanizer.ast.*;

/** An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> functions for restructuring expressions.
 * @author Yossi Gil
 * @since 2015-07-21 */
public enum duplicate {
  ;
  static List<Expression> adjust(final Operator o, final List<Expression> xs) {
    if (o != wizard.MINUS2)
      return xs;
    final List<Expression> $ = new ArrayList<>();
    for (final Expression e : xs)
      $.add(subject.operand(e).to(wizard.MINUS1));
    return $;
  }

  /** Duplicate all {@link ASTNode} objects found in a given list into another
   * list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(final List<N> from, final List<N> into) {
    for (final N s : from)
      into(s, into);
  }

  /** Duplicate a {@link Statement} into another list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void into(final N from, final List<N> into) {
    into.add(duplicate.of(from));
  }

  public static void modifiers(final List<IExtendedModifier> from, final List<IExtendedModifier> to) {
    for (final IExtendedModifier m : from)
      if (m.isModifier())
        to.add(duplicate.of((Modifier) m));
      else if (m.isAnnotation())
        to.add(duplicate.of((Annotation) m));
  }

  /** Make a duplicate, suitable for tree rewrite, of the parameter
   * @param ¢ JD
   * @return a duplicate of the parameter, downcasted to the returned type. */
  @SuppressWarnings("unchecked") public static <¢ extends ASTNode> ¢ of(final ¢ ¢) {
    return ¢ == null ? null : (¢) copySubtree(¢.getAST(), ¢);
  }
}
