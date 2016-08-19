package il.org.spartan.refactoring.utils;

import static il.org.spartan.refactoring.utils.Funcs.*;
import static il.org.spartan.refactoring.utils.expose.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;

// TODO: document this class
@SuppressWarnings("javadoc") public class subject {
  public static class Claimer {
    protected final AST ast;

    public Claimer(final ASTNode n) {
      ast = n == null ? null : n.getAST();
    }
    Expression claim(final Expression e) {
      return rebase(duplicate(extract.core(e)), ast);
    }
    Statement claim(final Statement s) {
      final Statement core = extract.core(s);
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
    public MethodInvocation toMethod(final String methodName) {
      final MethodInvocation $ = ast.newMethodInvocation();
      $.setExpression(inner);
      $.setName(ast.newSimpleName(methodName));
      return $;
    }
    public Expression toQualifier(final String name) {
      return ast.newQualifiedName((SimpleName) inner, ast.newSimpleName(name));
    }
    // ** TODO: YG; integrate with fluent API
    public NumberLiteral literal(final String text) {
      final NumberLiteral $ = ast.newNumberLiteral();
      $.setToken(text);
      return $;
    }
    /** Create a new {@link ReturnStatement} which returns our operand
     * @return  new return statement */
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
      return subject.operand(to(o)).toStatement();
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
      final InfixExpression $ = subject.pair(operands.get(0), operands.get(1)).to(o);
      for (int i = 2; i < operands.size(); ++i)
        extendedOperands($).add(new Plant(operands.get(i)).into($));
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
      expose.statements($).addAll(inner);
      return $;
    }
    public Statement toOneStatementOrNull() {
      return inner.isEmpty() ? null : toOptionalBlock();
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

  public static Operand operand(final Expression inner) {
    return new Operand(inner);
  }
  public static Several operands(final Expression... e) {
    return new Several(as.list(e));
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
  public static SeveralStatements ss(final List<Statement> ss) {
    return new SeveralStatements(ss);
  }
  public static SeveralStatements statement(final Statement s) {
    return statements(s);
  }
  public static SeveralStatements statements(final Statement... ss) {
    return ss(as.list(ss));
  }
}
