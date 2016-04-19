package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.duplicate;
import static il.org.spartan.refactoring.utils.Funcs.logicalNot;
import static il.org.spartan.refactoring.utils.Funcs.rebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

// TODO: document this class
// edited by Ori Roth
@SuppressWarnings("javadoc") public class Subject {
  public static Operand operand(final Expression inner) {
    return new Operand(inner);
  }
  public static Several operands(final Expression... e) {
    return new Several(Arrays.asList(e));
  }
  public static Several operands(final List<Expression> es) {
    return new Several(es);
  }
  public static Pair pair(final Expression left, final Expression right) {
    return new Pair(left, right);
  }
  public static StatementPair pair(final Statement s1, final Statement s2) {
    return new StatementPair(s1, s2);
  }
  public static SeveralStatements statement(final Statement s) {
    return statements(s);
  }
  public static SeveralStatements ss(final List<Statement> ss) {
    return new SeveralStatements(ss);
  }
  public static SeveralStatements statements(final Statement... ss) {
    return ss(Arrays.asList(ss));
  }

  public static class Claimer {
    protected final AST ast;
    public Claimer(final ASTNode n) {
      ast = n == null ? null : n.getAST();
    }
    Expression claim(final Expression e) {
      return rebase(duplicate(Extract.core(e)), ast);
    }
    Statement claim(final Statement s) {
      final Statement core = Extract.core(s);
      return core == null ? null : rebase(duplicate(core), ast);
    }
  }

  public static class Operand extends Claimer {
    private final Expression inner;
    Operand(final Expression inner) {
      super(inner);
      this.inner = claim(inner);
    }
    public ParenthesizedExpression parenthesis() {
      final ParenthesizedExpression $ = ast.newParenthesizedExpression();
      $.setExpression(inner);
      return $;
    }
    public Expression to(final PostfixExpression.Operator o) {
      final PostfixExpression $ = ast.newPostfixExpression();
      $.setOperator(o);
      $.setOperand(new Plant(inner).into($));
      return $;
    }
    public PrefixExpression to(final PrefixExpression.Operator o) {
      final PrefixExpression $ = ast.newPrefixExpression();
      $.setOperator(o);
      $.setOperand(new Plant(inner).into($));
      return $;
    }
    /**
     * Create a new {@link ReturnStatement} with which returns our operand
     *
     * @return the new return statement
     */
    public ReturnStatement toReturn() {
      final ReturnStatement $ = ast.newReturnStatement();
      $.setExpression(inner);
      return $;
    }
    public ExpressionStatement toStatement() {
      return ast.newExpressionStatement(inner);
    }
    public ThrowStatement toThrow() {
      final ThrowStatement $ = ast.newThrowStatement();
      $.setExpression(inner);
      return $;
    }
  }

  public static class Pair extends Claimer {
    final Expression left, right;
    Pair(final Expression left, final Expression right) {
      super(left);
      this.left = claim(left);
      this.right = claim(right);
    }
    public Assignment to(final Assignment.Operator o) {
      final Assignment $ = ast.newAssignment();
      $.setOperator(o);
      $.setLeftHandSide(new Plant(left).into($));
      $.setRightHandSide(new Plant(right).into($));
      return $;
    }
    public InfixExpression to(final InfixExpression.Operator o) {
      final InfixExpression $ = ast.newInfixExpression();
      $.setOperator(o);
      $.setLeftOperand(new Plant(left).into($));
      $.setRightOperand(new Plant(right).into($));
      return $;
    }
    public ConditionalExpression toCondition(final Expression condition) {
      final ConditionalExpression $ = ast.newConditionalExpression();
      $.setExpression(new Plant(claim(condition)).into($));
      $.setThenExpression(new Plant(left).into($));
      $.setElseExpression(new Plant(right).into($));
      return $;
    }
    public Statement toStatement(final Assignment.Operator o) {
      return Subject.operand(to(o)).toStatement();
    }
  }

  public static class Several extends Claimer {
    private final List<Expression> operands;
    public Several(final List<Expression> operands) {
      super(operands.get(0));
      this.operands = new ArrayList<>();
      for (final Expression e : operands)
        this.operands.add(claim(e));
    }
    public InfixExpression to(final InfixExpression.Operator o) {
      assert operands.size() >= 2;
      final InfixExpression $ = Subject.pair(operands.get(0), operands.get(1)).to(o);
      for (int i = 2; i < operands.size(); ++i)
        $.extendedOperands().add(new Plant(operands.get(i)).into($));
      return $;
    }
  }

  public static class SeveralStatements extends Claimer {
    private final List<Statement> inner;
    public SeveralStatements(final List<Statement> inner) {
      super(inner.isEmpty() ? null : inner.get(0));
      this.inner = new ArrayList<>();
      for (final Statement s : inner)
        this.inner.add(claim(s));
    }
    public Block toBlock() {
      final Block $ = ast.newBlock();
      $.statements().addAll(inner);
      return $;
    }
    public Statement toOptionalBlock() {
      switch (inner.size()) {
        case 0:
          return ast.newEmptyStatement();
        case 1:
          return inner.get(0);
        default:
          return toBlock();
      }
    }
    public Statement toOneStatementOrNull() {
      return inner.isEmpty() ? null : toOptionalBlock();
    }
  }

  public static class StatementPair extends Claimer {
    private final Statement elze;
    private final Statement then;
    StatementPair(final Statement then, final Statement elze) {
      super(then);
      this.then = claim(then);
      this.elze = claim(elze);
    }
    public IfStatement toIf(final Expression condition) {
      final IfStatement $ = ast.newIfStatement();
      $.setExpression(claim(condition));
      if (then != null)
        new PlantStatement(then).intoThen($);
      if (elze != null)
        $.setElseStatement(elze);
      return $;
    }
    public IfStatement toNot(final Expression condition) {
      return toIf(logicalNot(condition));
    }
  }
}
