package il.org.spartan.refactoring.utils;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.Is.*;
import static il.org.spartan.refactoring.utils.extract.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;
import org.junit.*;

import il.org.spartan.*;

/** An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> functions for restructuring expressions.
 * @author Yossi Gil
 * @since 2015-07-21 */
public enum Restructure {
  ;
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

  public static Expression minus(final Expression e) {
    final PrefixExpression ¢ = asPrefixExpression(e);
    return ¢ == null ? minus(e, asNumberLiteral(e))
        : ¢.getOperator() == MINUS1 ? ¢.getOperand() //
            : ¢.getOperator() == PLUS1 ? subject.operand(¢.getOperand()).to(MINUS1)//
                : e;
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

  static Expression minus(final Expression e, final NumberLiteral l) {
    return l == null ? newMinus(e) //
        : newLiteral(l, isLiteralZero(l) ? "0" : signAdjust(l.getToken())) //
    ;
  }

  static List<Expression> minus(final List<Expression> es) {
    final List<Expression> $ = new ArrayList<>();
    $.add(first(es));
    for (final Expression e : rest(es))
      $.add(newMinus(e));
    return $;
  }

  static Expression newMinus(final Expression e) {
    return isLiteralZero(e) ? e : subject.operand(e).to(Funcs.MINUS1);
  }

  private static List<Expression> add(final Expression e, final List<Expression> $) {
    $.add(e);
    return $;
  }

  private static List<Expression> adjust(final Operator o, final List<Expression> es) {
    if (o != MINUS2)
      return es;
    final List<Expression> $ = new ArrayList<>();
    for (final Expression e : es)
      $.add(subject.operand(e).to(MINUS1));
    return $;
  }

  private static List<Expression> flattenInto(final Operator o, final Expression e, final List<Expression> $) {
    final Expression core = core(e);
    final InfixExpression inner = asInfixExpression(core);
    return inner == null || inner.getOperator() != o ? add(!Is.simple(core) ? e : core, $) : flattenInto(o, adjust(o, extract.operands(inner)), $);
  }

  private static List<Expression> flattenInto(final Operator o, final List<Expression> es, final List<Expression> $) {
    assert $ != null;
    for (final Expression e : es)
      flattenInto(o, e, $);
    return $;
  }

  private static NumberLiteral newLiteral(final ASTNode n, final String token) {
    final NumberLiteral $ = n.getAST().newNumberLiteral();
    $.setToken(token);
    return $;
  }

  private static String signAdjust(final String token) {
    return token.startsWith("-") ? token.substring(1) //
        : "-" + token.substring(token.startsWith("+") ? 1 : 0);
  }

  @SuppressWarnings("static-method") public static class TEST {
    @Test public void issue72me4xA() {
      azzert.that(minus(Into.e("-x")), iz("x"));
    }

    @Test public void issue72me4xB() {
      azzert.that(minus(Into.e("x")), iz("-x"));
    }

    @Test public void issue72me4xC() {
      azzert.that(minus(Into.e("+x")), iz("-x"));
    }

    @Test public void issue72me4xD() {
      azzert.that(minus(Into.e("-x")), iz("x"));
    }

    @Test public void issue72me4xF() {
      azzert.that(minus(Into.e("x")), iz("-x"));
    }

    @Test public void issue72me4xG() {
      azzert.that(minus(Into.e("+x")), iz("-x"));
    }
  }
}
