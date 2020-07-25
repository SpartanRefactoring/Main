package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.spartanizer.ast.navigate.extract.core;
import static java.util.stream.Collectors.toList;
import static org.eclipse.jdt.core.dom.ASTNode.INFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.NUMBER_LITERAL;
import static org.eclipse.jdt.core.dom.ASTNode.PARENTHESIZED_EXPRESSION;
import static org.eclipse.jdt.core.dom.ASTNode.PREFIX_EXPRESSION;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.DIVIDE;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.TIMES;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.Statement;

import fluent.ly.is;
import il.org.spartan.spartanizer.ast.navigate.hop;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;

/**
 * takes care of of multiplicative terms with minus symbol in them.
 * <p>
 * An empty {@code enum} for fluent programming. The name should say it all: The
 * name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * 
 * @author Yossi Gil
 * @since 2016
 */
public enum eliminate {
	DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;

	/**
	 * Remove the last statement residing under a given {@link Statement}, ifj is
	 * empty or has only one statement return empty statement.
	 * 
	 * @param $ JD {@code null} if not such sideEffects exists.
	 * @return Given {@link Statement} without the last inner statement, if
	 *         parameter is empty or has only one statement return empty statement.
	 */
	public static Statement lastStatement(final Statement $) {
		final Block b = az.block($);
		if (b == null)
			return atomic.emptyStatement($);
		final List<Statement> ss = step.statements(b);
		if (ss.isEmpty())
			return atomic.emptyStatement($);
		ss.remove(ss.size() - 1);
		return $;
	}

	@SuppressWarnings("boxing")
	public static int level(final Collection<Expression> xs) {
		return xs.stream().map(eliminate::level).reduce((x, y) -> x + y).get();
	}

	public static int level(final Expression ¢) {
		return iz.nodeTypeEquals(¢, PREFIX_EXPRESSION) ? level((PrefixExpression) ¢)
				: iz.nodeTypeEquals(¢, PARENTHESIZED_EXPRESSION) ? level(core(¢)) //
						: iz.nodeTypeEquals(¢, INFIX_EXPRESSION) ? level((InfixExpression) ¢) //
								: iz.nodeTypeEquals(¢, NUMBER_LITERAL)
										? az.bit(az.numberLiteral(¢).getToken().startsWith("-")) //
										: 0;
	}

	public static int level(final InfixExpression ¢) {
		return is.out(¢.getOperator(), TIMES, DIVIDE) ? 0 : level(hop.operands(¢));
	}

	public static Expression peel(final Expression $) {
		return iz.nodeTypeEquals($, PREFIX_EXPRESSION) ? peel((PrefixExpression) $)
				: iz.nodeTypeEquals($, PARENTHESIZED_EXPRESSION) ? peel(core($)) //
						: iz.nodeTypeEquals($, INFIX_EXPRESSION) ? peel((InfixExpression) $) //
								: iz.nodeTypeEquals($, NUMBER_LITERAL) ? peel((NumberLiteral) $) //
										: $;
	}

	public static Expression peel(final InfixExpression ¢) {
		return is.out(¢.getOperator(), TIMES, DIVIDE) ? ¢ : subject.operands(peel(hop.operands(¢))).to(¢.getOperator());
	}

	public static Expression peel(final NumberLiteral $) {
		return !$.getToken().startsWith("-") && !$.getToken().startsWith("+") ? $
				: $.getAST().newNumberLiteral($.getToken().substring(1));
	}

	public static Expression peel(final PrefixExpression $) {
		return is.out($.getOperator(), op.MINUS1, op.PLUS1) ? $ : peel($.getOperand());
	}

	private static int level(final PrefixExpression ¢) {
		return az.bit(¢.getOperator() == op.MINUS1) + level(¢.getOperand());
	}

	private static List<Expression> peel(final Collection<Expression> ¢) {
		return ¢.stream().map(eliminate::peel).collect(toList());
	}
}
