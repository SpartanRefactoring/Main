package il.org.spartan.spartanizer.assemble;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.*;

/** Contains subclasses and tools to build expressions and statements */
public final class subject {
  public static InfixExpression append(final InfixExpression base, final Expression add) {
    final InfixExpression $ = duplicate.of(base);
    extendedOperands($).add(make.plant(duplicate.of(add)).into($));
    return $;
  }

  /** Create a new Operand
   * @param inner the expression of the operand
   * @return the new operand */
  public static Operand operand(final Expression inner) {
    return new Operand(inner);
  }

  /** Create an instance of several operands together here we get the
   * expressions in separate and not as a list
   * @param xs JD
   * @return a new instance using the given expressions */
  public static Several operands(final Expression... ¢) {
    return new Several(as.list(¢));
  }

  /** Create an instance of several operands together here we get the
   * expressions as a list
   * @param xs a list of expressions
   * @return a new Several instance using the given list of expressions */
  public static Several operands(final List<Expression> ¢) {
    return new Several(¢);
  }

  /** Create an instance of 2 expressions together
   * @param left the left expression
   * @param right the right expression
   * @return a new instance of the class pair */
  public static Pair pair(final Expression left, final Expression right) {
    return new Pair(left, right);
  }

  /** Create an instance of 2 statements together
   * @param s1 the first statement
   * @param s2 the second statement
   * @return a new instance of the class StatementPair */
  public static StatementPair pair(final Statement s1, final Statement s2) {
    return new StatementPair(s1, s2);
  }

  /** Create an instance of several statements together here we get the
   * statements as a list
   * @param ss a list of statements
   * @return a new instance using the given statements */
  public static SeveralStatements ss(final List<Statement> ¢) {
    return new SeveralStatements(¢);
  }

  /** Create an instance of several statements together here we get only one
   * statement
   * @param context JD
   * @return a new instance using the given statement */
  public static SeveralStatements statement(final Statement ¢) {
    return statements(¢);
  }

  /** Create an instance of several statements together here we get the
   * statements in separate and not as a list
   * @param ss JD
   * @return a new instance using the given statements */
  public static SeveralStatements statements(final Statement... ¢) {
    return ss(as.list(¢));
  }

  public static class Claimer {
    protected final AST ast;

    /** Assign to ast the AST that owns the node n (the parameter)
     * @param n an AST node */
    public Claimer(final ASTNode n) {
      ast = n == null ? null : n.getAST();
    }

    /** Make a deep copy of expression and assign it to ast
     * @param x JD
     * @return a copy of the expression e
     * @see #rebase
     * @see duplicate#duplicate */
    Expression claim(final Expression ¢) {
      return wizard.rebase(duplicate.of(extract.core(¢)), ast);
    }

    /** A deep copy of statement and assign it to ast, if the statement exists
     * @param s a Statement
     * @return a copy of the statement s if it is'nt null, else returns null
     * @see rebase
     * @see duplicate */
    Statement claim(final Statement s) {
      final Statement core = extract.core(s);
      return core == null ? null : wizard.rebase(duplicate.of(core), ast);
    }
  }

  /** All the expressions that use a single operand */
  public static class Operand extends Claimer {
    private final Expression inner;

    /** Assign the expression inner to the parameter inner
     * @param inner an Expression */
    Operand(final Expression inner) {
      super(inner);
      this.inner = claim(inner);
    }

    // ** TODO: YG; integrate with fluent API
    /** Create a number literal node owned by ast
     * @param text the number of the literal node
     * @return the number literal node with text as a number */
    public NumberLiteral literal(final String text) {
      final NumberLiteral $ = ast.newNumberLiteral();
      $.setToken(text);
      return $;
    }

    /** Create a new parenthesis expression owned by ast and put the expression
     * inner (a field of Operand) between the parenthesis of the new expression
     * @return the expression inner between parenthesis */
    public ParenthesizedExpression parenthesis() {
      final ParenthesizedExpression $ = ast.newParenthesizedExpression();
      $.setExpression(inner);
      return $;
    }

    /** Create a new expression with postfix operator owned by this ast, the
     * expression is a combination of the expression inner with a postfix
     * operator
     * @param o a postfix operator
     * @return the expression inner together with the postfix operator o */
    public Expression to(final PostfixExpression.Operator ¢) {
      final PostfixExpression $ = ast.newPostfixExpression();
      $.setOperator(¢);
      $.setOperand(make.plant(inner).into($));
      return $;
    }

    /** Create a new expression with prefix operator owned by this ast, the
     * expression is a combination of the expression inner with a prefix
     * operator
     * @param o a prefix operator
     * @return the expression inner together with the prefix operator o */
    public PrefixExpression to(final PrefixExpression.Operator ¢) {
      final PrefixExpression $ = ast.newPrefixExpression();
      $.setOperator(¢);
      $.setOperand(make.plant(inner).into($));
      return $;
    }

    /** Create a new expression of method invocation owned by this AST
     * @param methodName a string contains the method name
     * @return a method invocation expression of the method methodName with
     *         inner as an expression */
    public MethodInvocation toMethod(final String methodName) {
      assert ast != null : "Cannot find ast for method: " + methodName + ". Inner = " + inner;
      final MethodInvocation $ = ast.newMethodInvocation();
      $.setExpression(inner);
      $.setName(ast.newSimpleName(methodName));
      return $;
    }

    /** Creates and returns a new qualified name node for inner.
     * @param name a string of the name to be qualified
     * @return a qualified name node with name */
    public Expression toQualifier(final String name) {
      return ast.newQualifiedName((SimpleName) inner, ast.newSimpleName(name));
    }

    /** Create a new {@link ReturnStatement} which returns our operand
     * @return new return statement */
    public ReturnStatement toReturn() {
      final ReturnStatement $ = ast.newReturnStatement();
      $.setExpression(inner);
      return $;
    }

    /** convert the expression inner into statement
     * @return an ExpressionStatement of inner */
    public ExpressionStatement toStatement() {
      return ast.newExpressionStatement(inner);
    }

    /** Create a new throw statement owned by this ast
     * @return a throw statement of the expression inner */
    public ThrowStatement toThrow() {
      final ThrowStatement $ = ast.newThrowStatement();
      $.setExpression(inner);
      return $;
    }
  }

  /** All the expressions that use two operands */
  public static class Pair extends Claimer {
    /** The two expressions in the pair */
    final Expression left, right;

    /** Assign the expressions left and right to the parameters, the newly-
     * created ast will own the left node
     * @param left an Expression
     * @param right an Expression */
    Pair(final Expression left, final Expression right) {
      super(left);
      this.left = claim(left);
      this.right = claim(right);
    }

    /** Create a new assignment expression owned by ast the left/right hand side
     * of the assignment expression is the field left/right respectively,
     * @param o an assignment operator
     * @return an assignment expression with operator o */
    public Assignment to(final Assignment.Operator ¢) {
      assert ¢ != null;
      final Assignment $ = ast.newAssignment();
      $.setOperator(¢);
      $.setLeftHandSide(make.plant(left).into($));
      $.setRightHandSide(make.plant(right).into($));
      return $;
    }

    /** Create a new infix expression owned by ast the left/right hand side of
     * the assignment expression is the field left/right respectively, and the
     * operator is the given one
     * @param o
     * @return an expression with the parameter o as an operator */
    public InfixExpression to(final InfixExpression.Operator ¢) {
      final InfixExpression $ = ast.newInfixExpression();
      $.setOperator(¢);
      $.setLeftOperand(make.plant(left).intoLeft($));
      $.setRightOperand(¢ != wizard.PLUS2 ? make.plant(right).into($)
          : !precedence.greater($, right)
              && (!precedence.equal($, right) || !type.isNotString(left) && !make.PlantingExpression.isStringConactingSafe(right))
              && !iz.simple(right) ? subject.operand(right).parenthesis() : right);
      return $;
    }

    /** Create a new conditional expression owned by ast the condition is given
     * as a parameter, the true path is the left field and the false is the
     * right field
     * @param condition an expression of the condition
     * @return a conditional expression with the parameter condition as a
     *         condition */
    public ConditionalExpression toCondition(final Expression condition) {
      final ConditionalExpression $ = ast.newConditionalExpression();
      $.setExpression(make.plant(claim(condition)).into($));
      $.setThenExpression(make.plant(left).into($));
      assert make.plant(right).into($) != null : "Planting " + right + " into " + $ + "/" + parent($) + "returned null";
      $.setElseExpression(make.plant(right).into($));
      return $;
    }

    /** Convert the assignment operator into a statement
     * @param o JD
     * @return a statement of the operator */
    public Statement toStatement(final Assignment.Operator ¢) {
      return subject.operand(to(¢)).toStatement();
    }
  }

  public static class Several extends Claimer {
    /** To deal with more than 2 operands, we maintain a list */
    private final List<Expression> operands;

    /** assign each of the given operands to the operands list the left operand
     * is the owner
     * @param operands a list of expression, these are the operands */
    public Several(final List<Expression> operands) {
      super(first(operands));
      this.operands = new ArrayList<>();
      for (final Expression ¢ : operands)
        this.operands.add(claim(¢));
    }

    /** Create an infix expression from the given operator and the operands
     * @param o JD
     * @return JD */
    public InfixExpression to(final InfixExpression.Operator o) {
      assert !operands.isEmpty();
      assert operands.size() != 1;
      assert operands.size() >= 2;
      final InfixExpression $ = subject.pair(first(operands), second(operands)).to(o);
      for (int i = 2; i < operands.size(); ++i)
        extendedOperands($).add(make.plant(operands.get(i)).into($));
      return $;
    }
  }

  /** Some Statements */
  public static class SeveralStatements extends Claimer {
    private final List<Statement> inner; // here we work with several statements
                                         // so we have a statements list

    /** assign each of the given operands to the inner list the left operand is
     * the owner
     * @param inner a list of statements */
    public SeveralStatements(final List<Statement> inner) {
      super(first(inner));
      this.inner = new ArrayList<>();
      for (final Statement ¢ : inner)
        this.inner.add(claim(¢));
    }

    /** Transform the inner into a block
     * @return a Block statement */
    public Block toBlock() {
      final Block $ = ast.newBlock();
      step.statements($).addAll(inner);
      return $;
    }

    /** Transform the inner into a block if it's possible
     * @return a Block statement <code>or</code> a <code>null</code> */
    public Statement toOneStatementOrNull() {
      return inner.isEmpty() ? null : toOptionalBlock();
    }

    /** use the inner list to make a block depending on it's size (only in case
     * there are more than 2 elements)
     * @return
     *         <ol>
     *         <li>empty statement, if inner is empty
     *         <li>single statement, if |inner|==1
     *         <li>a block statement, if |inner|>1
     *         </ol>
     * @see subject#toBlock */
    public Statement toOptionalBlock() {
      switch (inner.size()) {
        case 0:
          return ast.newEmptyStatement();
        case 1:
          return first(inner);
        default:
          return toBlock();
      }
    }
  }

  /** A pair of statements */
  public static class StatementPair extends Claimer {
    private final Statement elze;
    private final Statement then;

    /** assign then and elze to the matching fields the then operand is the
     * owner
     * @param flat a list of statements */
    StatementPair(final Statement then, final Statement elze) {
      super(then);
      this.then = claim(then);
      this.elze = claim(elze);
    }

    /** Create a new if statement owned by ast the if statement contains a given
     * condition and uses the class parameters (then, elze)
     * @param condition the condition of the if statement
     * @return an If statement with the given condition */
    public IfStatement toIf(final Expression condition) {
      final IfStatement $ = ast.newIfStatement();
      $.setExpression(claim(condition));
      if (then != null)
        make.plant(then).intoThen($);
      if (elze != null)
        $.setElseStatement(elze);
      return $;
    }

    /** Create a new if statement owned by ast the if statement contains the
     * logical not of the given condition and uses the class parameters (then,
     * elze)
     * @param condition the logical not of the condition of the if statement
     * @return an If statement with the logical not of the given condition
     * @see toIf
     * @see logicalNot */
    public IfStatement toNot(final Expression condition) {
      return toIf(make.notOf(condition));
    }
  }
}