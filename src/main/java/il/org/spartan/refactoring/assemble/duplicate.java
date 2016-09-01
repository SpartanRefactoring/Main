package il.org.spartan.refactoring.assemble;

import static il.org.spartan.refactoring.ast.extract.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import il.org.spartan.refactoring.ast.*;

/** An empty <code><b>enum</b></code> with a variety of <code>public
 * static</code> functions for restructuring expressions.
 * @author Yossi Gil
 * @since 2015-07-21 */
public enum duplicate {
  ;
  /** Duplicate a {@link Statement} into another list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void duplicate(final N from, final List<N> into) {
    into.add(wizard.duplicate(from));
  }


  /** Duplicate all {@link ASTNode} objects found in a given list into another
   * list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void duplicateInto(final List<N> from, final List<N> into) {
    for (final N s : from)
      duplicate(s, into);
  }

  public static void duplicateModifiers(final List<IExtendedModifier> from, final List<IExtendedModifier> to) {
    for (final IExtendedModifier m : from)
      if (m.isModifier())
        to.add(wizard.duplicate((Modifier) m));
      else if (m.isAnnotation())
        to.add(wizard.duplicate((Annotation) m));
  }

  /** Flatten the list of arguments to an {@link InfixExpression}, e.g., convert
   * an expression such as <code>(a + b) + c</code> whose inner form is roughly
   * "+(+(a,b),c)", into <code>a + b + c</code>, whose inner form is (roughly)
   * "+(a,b,c)".
   * @param $ JD
   * @return a duplicate of the argument, with the a flattened list of
   *         operands. */
  public static InfixExpression flatten(final InfixExpression $) {
    assert $ != null;
    final Operator o = $.getOperator();
    assert o != null;
    return subject.operands(flattenInto(o, hop.operands($), new ArrayList<Expression>())).to(wizard.duplicate($).getOperator());
  }

  public static List<Expression> flattenInto(final Operator o, final List<Expression> es, final List<Expression> $) {
    for (final Expression e : es)
      flattenInto(o, e, $);
    return $;
  }

  public static Expression minus(final Expression e) {
    final PrefixExpression ¢ = az.prefixExpression(e);
    return ¢ == null ? make.minus(e, az.numberLiteral(e))
        : ¢.getOperator() == wizard.MINUS1 ? ¢.getOperand() //
            : ¢.getOperator() == wizard.PLUS1 ? subject.operand(¢.getOperand()).to(wizard.MINUS1)//
                : e;
  }

  /** Parenthesize an expression (if necessary).
   * @param e JD
   * @return a {@link wizard#duplicate(Expression)} of the parameter wrapped in
   *         parenthesis. */
  public static Expression parenthesize(final Expression e) {
    return iz.noParenthesisRequired(e) ? wizard.duplicate(e) : make.parethesized(e);
  }

  static List<Expression> add(final Expression e, final List<Expression> $) {
    $.add(e);
    return $;
  }
  static List<Expression> adjust(final Operator o, final List<Expression> es) {
    if (o != wizard.MINUS2)
      return es;
    final List<Expression> $ = new ArrayList<>();
    for (final Expression e : es)
      $.add(subject.operand(e).to(wizard.MINUS1));
    return $;
  }

  static String signAdjust(final String token) {
    return token.startsWith("-") ? token.substring(1) //
        : "-" + token.substring(token.startsWith("+") ? 1 : 0);
  }

  static List<Expression> flattenInto(final Operator o, final Expression e, final List<Expression> $) {
    final Expression core = core(e);
    final InfixExpression inner = az.infixExpression(core);
    return inner != null && inner.getOperator() == o ? flattenInto(o, adjust(o, hop.operands(inner)), $)
        : add(!iz.noParenthesisRequired(core) ? e : core, $);
  }


  /** Duplicate a {@link Statement} into another list.
   * @param from JD
   * @param into JD */
  public static <N extends ASTNode> void duplicateInto(final N from, final List<N> into) {
    into.add(wizard.duplicate(from));
  }
}
