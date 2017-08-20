package il.org.spartan.spartanizer.ast.navigate;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-01 */
@UnderConstruction("Dor -- 10/07/2017")
public enum compute {
  ;
  public static List<ReturnStatement> returns(final ASTNode n) {
    return new ASTMapReducer<List<ReturnStatement>>() {
      @Override protected int[] protect() {
        return new int[] { //
            LAMBDA_EXPRESSION, //
            FOR_STATEMENT, //
            ENHANCED_FOR_STATEMENT, //
            DO_STATEMENT, //
            ANONYMOUS_CLASS_DECLARATION, //
            SWITCH_STATEMENT, //
            TYPE_DECLARATION_STATEMENT, //
        };
      }
      @Override public List<ReturnStatement> map(final ReturnStatement ¢) {
        return as.list(¢);
      }
      @Override public List<ReturnStatement> reduce() {
        return an.empty.list();
      }
      @Override public List<ReturnStatement> reduce(final List<ReturnStatement> ss1, final List<ReturnStatement> ss2) {
        if (ss1 == null)
          return ss2;
        if (ss2 == null)
          return ss1;
        ss1.addAll(ss2);
        return ss1;
      }
    }.map(n);
  }
  public static List<ASTNode> usedSpots(final ASTNode x) {
    return new ASTMapReducer<List<ASTNode>>() {
      @Override public List<ASTNode> reduce() {
        return an.empty.list();
      }
      @Override public List<ASTNode> reduce(final List<ASTNode> ss1, final List<ASTNode> ss2) {
        if (ss1 == null && ss2 == null)
          return an.empty.list();
        if (ss1 == null)
          return ss2;
        if (ss2 == null)
          return ss1;
        ss1.addAll(ss2);
        return ss1;
      }
      @Override protected List<ASTNode> map(final SimpleName ¢) {
        final String $ = ¢.getIdentifier();
        return guessName.of($) != guessName.METHOD_OR_VARIABLE ? reduce() : as.list(¢);
      }
      @Override protected List<ASTNode> map(@SuppressWarnings("unused") final ThisExpression ¢) {
        return reduce();
      }
    }.map(x);
  }
  public static Stream<String> usedIdentifiers(final ASTNode x) {
    return usedSpots(x).stream().map(λ -> λ + "");
  }
  public static List<String> useSpots(final Expression x) {
    return new ASTMapReducer<List<String>>() {
      @Override public List<String> reduce() {
        return an.empty.list();
      }
      @Override public List<String> reduce(final List<String> ss1, final List<String> ss2) {
        if (ss1 == null && ss2 == null)
          return an.empty.list();
        if (ss1 == null)
          return ss2;
        if (ss2 == null)
          return ss1;
        ss1.addAll(ss2);
        return ss1;
      }
      @Override protected List<String> map(final SimpleName ¢) {
        final String $ = ¢.getIdentifier();
        return guessName.of($) != guessName.METHOD_OR_VARIABLE ? reduce() : as.list($);
      }
      @Override protected List<String> map(@SuppressWarnings("unused") final ThisExpression ¢) {
        return reduce();
      }
    }.map(x);
  }
  public static List<ASTNode> updateSpots(final ASTNode... ¢) {
    return Stream.of(¢).map(compute::updateSpots).flatMap(List<ASTNode>::stream).collect(toList());
  }
  public static List<ASTNode> updateSpots(final ASTNode x) {
    final List<ASTNode> $ = new ASTMapReducer<List<ASTNode>>() {
      @Override public List<ASTNode> reduce() {
        return an.empty.list();
      }
      public List<ASTNode> reduce(final ASTNode n, final List<ASTNode> ns) {
        if (n == null)
          return ns;
        if (ns == null)
          return as.list(n);
        ns.add(0, n);
        return ns;
      }
      @Override public List<ASTNode> reduce(final List<ASTNode> l1, final List<ASTNode> l2) {
        if (l1 == null)
          return l2;
        if (l2 == null)
          return l1;
        l1.addAll(l2);
        return l1;
      }
      @Override protected List<ASTNode> map(final Assignment ¢) {
        return reduce(to(¢), super.map(¢));
      }
      @Override protected List<ASTNode> map(final PostfixExpression ¢) {
        return reduce(¢.getOperand(), super.map(¢));
      }
      @Override protected List<ASTNode> map(final PrefixExpression ¢) {
        return reduce(!iz.updating(¢) ? reduce() : as.list(¢.getOperand()), super.map(¢));
      }
    }.map(x);
    return $ != null ? $ : an.empty.list();
  }
  public static List<Statement> decompose(final Expression x) {
    return new ASTMapReducer<List<Statement>>() {
      @Override public List<Statement> reduce() {
        return an.empty.list();
      }
      @Override public List<Statement> reduce(final List<Statement> $, final List<Statement> ss) {
        $.addAll(ss);
        return $;
      }
      @Override protected List<Statement> map(final Assignment ¢) {
        return wizard.listMe(¢);
      }
      @Override protected List<Statement> map(final ClassInstanceCreation ¢) {
        return wizard.listMe(¢);
      }
      @Override protected List<Statement> map(final MethodInvocation ¢) {
        return wizard.listMe(¢);
      }
      @Override protected List<Statement> map(final PostfixExpression ¢) {
        return wizard.listMe(¢);
      }
      /** the operator is not in INCREMENT DECREMENT x \not\in \{\} */
      @Override protected List<Statement> map(final PrefixExpression ¢) {
        return !is.in(¢.getOperator(), INCREMENT, DECREMENT) ? reduce() : wizard.listMe(¢);
      }
      @Override protected List<Statement> map(final SuperMethodInvocation ¢) {
        return wizard.listMe(¢);
      }
    }.map(x);
  }
  public static int level(final Expression ¢) {
    return iz.nodeTypeEquals(¢, PREFIX_EXPRESSION) ? level((PrefixExpression) ¢)
        : iz.nodeTypeEquals(¢, PARENTHESIZED_EXPRESSION) ? level(core(¢)) //
            : iz.nodeTypeEquals(¢, INFIX_EXPRESSION) ? level((InfixExpression) ¢) //
                : iz.nodeTypeEquals(¢, NUMBER_LITERAL) ? az.bit(az.numberLiteral(¢).getToken().startsWith("-")) //
                    : 0;
  }
  public static int level(final InfixExpression ¢) {
    return is.out(operator(¢), TIMES, DIVIDE) ? 0 : level(hop.operands(¢));
  }
  @SuppressWarnings("boxing") public static int level(final Collection<Expression> xs) {
    return xs.stream().map(compute::level).reduce((x, y) -> x + y).get();
  }
  public static int level(final PrefixExpression ¢) {
    return az.bit(operator(¢) == op.MINUS1) + level(operand(¢));
  }
  public static Expression peel(final Expression $) {
    return iz.nodeTypeEquals($, PREFIX_EXPRESSION) ? peel((PrefixExpression) $)
        : iz.nodeTypeEquals($, PARENTHESIZED_EXPRESSION) ? peel(core($)) //
            : iz.nodeTypeEquals($, INFIX_EXPRESSION) ? peel((InfixExpression) $) //
                : iz.nodeTypeEquals($, NUMBER_LITERAL) ? peel((NumberLiteral) $) //
                    : $;
  }
  public static Expression peel(final InfixExpression ¢) {
    return is.out(operator(¢), TIMES, DIVIDE) ? ¢ : subject.operands(peel(hop.operands(¢))).to(operator(¢));
  }
  private static List<Expression> peel(final Collection<Expression> ¢) {
    return ¢.stream().map(compute::peel).collect(toList());
  }
  public static Expression peel(final NumberLiteral $) {
    return !token($).startsWith("-") && !token($).startsWith("+") ? $ : $.getAST().newNumberLiteral(token($).substring(1));
  }
  public static Expression peel(final PrefixExpression $) {
    return is.out(operator($), op.MINUS1, op.PLUS1) ? $ : peel(operand($));
  }

}
