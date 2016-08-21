package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

/** An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> functions for restructuring expressions.
 * @author Yossi Gil
 * @since 2015-07-21 */
public enum Restructure {
  ;
  private static List<Expression> add(final Expression e, final List<Expression> $) {
    $.add(e);
    return $;
  }

  public static Expression minus(final Expression e) {
    final PrefixExpression ¢ = asPrefixExpression(e);
    return ¢ == null ? minus(e, asNumberLiteral(e))
        : ¢.getOperator() == MINUS1 ? ¢.getOperand() //
            : ¢.getOperator() == PLUS1 ? subject.operand(¢.getOperand()).to(MINUS1)//
                : e;
  }

  private static Expression minus(final Expression e, final NumberLiteral l) {
    if (l == null || !l.getToken().startsWith("-"))
      return e;
    NumberLiteral $ = l.getAST().newNumberLiteral();
    $.setToken(l.getToken().substring(1));
    return $;
  }

  /** Compute the "de Morgan" conjugate of the operator present on an
   * {@link InfixExpression}.
   * @param e an expression whose operator is either
   *        {@link Operator#CONDITIONAL_AND} or {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the operator present on the
   *         parameter is {@link Operator#CONDITIONAL_OR}, or
   *         {@link Operator#CONDITIONAL_OR} if this operator is
   *         {@link Operator#CONDITIONAL_AND}
   * @see Restructure#conjugate(Operator) */
  public static Operator conjugate(final InfixExpression e) {
    return conjugate(e.getOperator());
  }

  /** Compute the "de Morgan" conjugate of an operator.
   * @param o must be either {@link Operator#CONDITIONAL_AND} or
   *        {@link Operator#CONDITIONAL_OR}
   * @return {@link Operator#CONDITIONAL_AND} if the parameter is
   *         {@link Operator#CONDITIONAL_OR}, or {@link Operator#CONDITIONAL_OR}
   *         if the parameter is {@link Operator#CONDITIONAL_AND}
   * @see Restructure#conjugate(InfixExpression) */
  public static Operator conjugate(final Operator o) {
    assert Is.deMorgan(o);
    return o.equals(CONDITIONAL_AND) ? CONDITIONAL_OR : CONDITIONAL_AND;
  }

  /** Duplicate all {@link ASTNode} objects found in a given list into another
   * list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void duplicateInto(final List<N> from, final List<N> into) {
    for (final N s : from)
      duplicateInto(s, into);
  }

  /** Duplicate a {@link Statement} into another list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void duplicateInto(final N from, final List<N> into) {
    into.add(duplicate(from));
  }

  public static void duplicateModifiers(final List<IExtendedModifier> from, final List<IExtendedModifier> to) {
    for (final IExtendedModifier m : from)
      if (m.isModifier())
        to.add(duplicate((Modifier) m));
      else if (m.isAnnotation())
        to.add(duplicate((Annotation) m));
  }

  /** Flatten the list of arguments to an {@link InfixExpression}, e.g., convert
   * an expression such as <code>(a + b) + c</code> whose inner form is roughly
   * "+(+(a,b),c)", into <code>a + b + c</code>, whose inner form is (roughly)
   * "+(a,b,c)".
   * @param $ JD
   * @return a duplicate of the argument, with the a flattened list of
   *         operands. */
  public static InfixExpression flatten(final InfixExpression $) {
    return subject.operands(flattenInto($.getOperator(), extract.operands($), new ArrayList<Expression>())).to(duplicate($).getOperator());
  }

  private static List<Expression> flattenInto(final Operator o, final Expression e, final List<Expression> $) {
    if (o == DIVIDE) {
      $.add(e);
      return $;
    }
    final Expression core = core(e);
    final InfixExpression inner = asInfixExpression(core);
    return inner == null || inner.getOperator() != o ? add(!Is.simple(core) ? e : core, $) : flattenInto(o, adjust(o, extract.operands(inner)), $);
  }

  static final PrefixExpression.Operator MINUS1 = PrefixExpression.Operator.MINUS;
  static final PrefixExpression.Operator PLUS1 = PrefixExpression.Operator.PLUS;
  static final InfixExpression.Operator MINUS2 = InfixExpression.Operator.MINUS;
  static final InfixExpression.Operator PLUS2 = InfixExpression.Operator.PLUS;

  private static List<Expression> adjust(final Operator o, final List<Expression> es) {
    if (o != MINUS2)
      return es;
    final List<Expression> $ = new ArrayList<>();
    for (final Expression e : es)
      $.add(subject.operand(e).to(MINUS1));
    return $;
  }

  private static List<Expression> flattenInto(final Operator o, final List<Expression> es, final List<Expression> $) {
    for (final Expression e : es)
      flattenInto(o, e, $);
    return $;
  }

  /** Parenthesize an expression (if necessary).
   * @param e JD
   * @return a {@link Funcs#duplicate(Expression)} of the parameter wrapped in
   *         parenthesis. */
  public static Expression parenthesize(final Expression e) {
    if (Is.simple(e))
      return duplicate(e);
    final ParenthesizedExpression $ = e.getAST().newParenthesizedExpression();
    $.setExpression(e.getParent() == null ? e : duplicate(e));
    return $;
  }
}
