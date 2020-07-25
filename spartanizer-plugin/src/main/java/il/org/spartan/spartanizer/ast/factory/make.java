package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.spartanizer.ast.safety.iz.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
public enum make {
  /** Strategy for conversion into a class body */
  CLASS_BODY_DECLARATIONS(ASTParser.K_CLASS_BODY_DECLARATIONS), //
  /** Strategy for conversion into a compilation unit */
  COMPILATION_UNIT(ASTParser.K_COMPILATION_UNIT), //
  /** Strategy for conversion into an expression */
  EXPRESSION(ASTParser.K_EXPRESSION), //
  /** Strategy for conversion into an sequence of sideEffects */
  STATEMENTS(ASTParser.K_STATEMENTS); //
  public static Expression assignmentAsExpression(final Assignment ¢) {
    final Operator $ = ¢.getOperator();
    return $ == ASSIGN ? copy.of(step.from(¢)) : subject.pair(step.to(¢), step.from(¢)).to(op.assign2infix($));
  }
  /** Converts a string into an AST, depending on it's form, as determined
   * by @link{GuessedContext.find}.
   * @param javaSnippet string to convert
   * @return AST, if string is not a valid AST according to any form, then
   *         null */
  public static ASTNode ast(final String javaSnippet) {
    switch (GuessedContext.find(javaSnippet)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
        return parse.cu(javaSnippet);
      case EXPRESSION_LOOK_ALIKE:
        return parse.e(javaSnippet);
      case METHOD_LOOK_ALIKE:
        return parse.m(javaSnippet);
      case OUTER_TYPE_LOOKALIKE:
        return parse.t(javaSnippet);
      case STATEMENTS_LOOK_ALIKE:
        return parse.s(javaSnippet);
      case BLOCK_LOOK_ALIKE:
        return az.astNode(the.firstOf(statements(az.block(parse.s(javaSnippet)))));
      default:
        for (final int guess : as.intArray(ASTParser.K_EXPRESSION, ASTParser.K_STATEMENTS, ASTParser.K_CLASS_BODY_DECLARATIONS,
            ASTParser.K_COMPILATION_UNIT)) {
          final ASTParser p = wizard.parser(guess);
          p.setSource(javaSnippet.toCharArray());
          final ASTNode $ = p.createAST(wizard.nullProgressMonitor);
          if (wizard.valid($))
            return $;
        }
        assert fault.unreachable() : fault.specifically("Snippet cannot be parsed", javaSnippet);
        return null;
    }
  }
  public static IfStatement blockIfNeeded(final IfStatement s, final ASTRewrite r, final TextEditGroup g) {
    if (!iz.blockRequired(s))
      return s;
    final Block $ = subject.statement(s).toBlock();
    r.replace(s, $, g);
    return (IfStatement) the.firstOf(statements($));
  }
  public static ForStatement forStatement(final VariableDeclarationFragment f, final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setBody(copy.of(body(¢)));
    $.setExpression(misc.pullInitializersFromExpression(copy.of(expression(¢)), LocalInitializedStatementWhile.parent(f)));
    initializers($).add(LocalInitializedStatementWhile.Initializers(f));
    return $;
  }
  public static VariableDeclarationFragment fragment(final VariableDeclarationFragment f, final Expression x) {
    final VariableDeclarationFragment $ = copy.of(f);
    $.setInitializer(copy.of(x));
    return $;
  }
  public static make.FromAST from(final AST t) {
    return new make.FromAST() {
      @Override
      public AST ast() {
        return t;
      }
      @Override public SimpleName identifier(final String identifier) {
        return ast().newSimpleName(identifier);
      }
      @Override public NumberLiteral literal(final int ¢) {
        return ast().newNumberLiteral(¢ + "");
      }
      @Override public StringLiteral literal(final String ¢) {
        final StringLiteral $ = ast().newStringLiteral();
        $.setLiteralValue(¢);
        return $;
      }
      @Override public NullLiteral nullLiteral() {
        return null;
      }
    };
  }
  public static make.FromAST from(final ASTNode ¢) {
    return from(¢.getAST());
  }
  /** Converts the {@link makeAST} value to its corresponding enum value
   * @param tipper The {@link makeAST} __
   * @return corresponding value to the argument */
  public static make from(final makeAST ¢) {
    switch (¢) {
      case CLASS_BODY_DECLARATIONS:
        return make.CLASS_BODY_DECLARATIONS;
      case COMPILATION_UNIT:
        return make.COMPILATION_UNIT;
      case EXPRESSION:
        return make.EXPRESSION;
      case STATEMENTS:
        return make.STATEMENTS;
      default:
        return null;
    }
  }
  public static boolean mixedLiteralKind(final Collection<Expression> xs) {
    if (xs.size() <= 2)
      return false;
    int previousKind = -1;
    for (final Expression x : xs)
      if (x instanceof NumberLiteral || x instanceof CharacterLiteral) {
        final int currentKind = new NumericLiteralClassifier(x + "").type().ordinal();
        assert currentKind >= 0;
        if (previousKind == -1)
          previousKind = currentKind;
        else if (previousKind != currentKind)
          return true;
      }
    return false;
  }
  public static Statement newSafeIf(final Statement parent, final Statement then, final Statement elze, final Expression x) {
    final IfStatement $ = then.getAST().newIfStatement();
    $.setExpression(copy.of(x));
    $.setThenStatement(fixedThen(then, elze));
    if (elze != null)
      $.setElseStatement(copy.of(elze));
    return !needsBlock(parent, elze) ? $ : subject.statements($).toBlock();
  }
  /** A fluent API method that wraps an {@link Expression} with parenthesis, if
   * the location in which this expression occurs requires such wrapping.
   * <p>
   * Typical usage is in the form {@code new Plant(expression).in(host)} where
   * {@code location} is the parent under which the expression is to be placed.
   * <p>
   * This function is a factory method recording the expression that might be
   * wrapped.
   * @param inner JD */
  public static make.PlantingExpression plant(final Expression ¢) {
    return new make.PlantingExpression(¢);
  }
  /** Factory method recording the statement might be wrapped.
   * @param inner JD */
  public static make.PlantingStatement plant(final Statement inner) {
    return new make.PlantingStatement(inner);
  }
  public static VariableDeclarationStatement variableDeclarationStatement(final Type t, final String name, final Expression x) {
    final AST create = x.getAST();
    final VariableDeclarationFragment fragment = create.newVariableDeclarationFragment();
    fragment.setName(create.newSimpleName(name));
    fragment.setInitializer(x);
    final VariableDeclarationStatement $ = create.newVariableDeclarationStatement(fragment);
    $.setType(t);
    return $;
  }
  /** Puts then statement in block if needed. For example when the then is an if
   * statement need to enclose it in a block so that the else won't be related
   * to it. */
  private static Statement fixedThen(final Statement then, final Statement elze) {
    return elze == null || !hasIfWithoutElse(then) ? copy.of(then) : subject.statements(copy.of(then)).toBlock();
  }
  /** Checks if in <code> st; else {...} <code> the else would be part of an
   * IfStatement in st. For example for st=<code> while(cond1) while(cond2)
   * if(cond3) f(); <code> if we concatenate <code> else {...} <code> it would
   * be part of the if statement in st (and not part of the statement containing
   * st) */
  @SuppressWarnings("boxing") private static boolean hasIfWithoutElse(final Statement st) {
    Statement s = st;
    for (IfStatement lf; s != null && is.in(s.getNodeType(), FOR_STATEMENT, WHILE_STATEMENT, ENHANCED_FOR_STATEMENT, IF_STATEMENT);)
      if ((lf = az.ifStatement(s)) == null)
        s = step.body(s);
      else if ((s = step.elze(lf)) == null)
        return true;
    return false;
  }
  /** Checks whether the if statement needs to be placed in block considering
   * it's containing block. For example when the containing statement is another
   * if statement that have else part and the if statement that is builded does
   * not have else part, then it should be placed in a block so that it the next
   * else won't be part of it. */
  @SuppressWarnings("boxing") private static boolean needsBlock(final Statement parent, final Statement elze) {
    if (parent == null)
      return false;
    Statement s;
    IfStatement lf;
    for (s = parent; s != null
        && is.in(s.getNodeType(), FOR_STATEMENT, WHILE_STATEMENT, ENHANCED_FOR_STATEMENT, IF_STATEMENT); s = az.statement(s.getParent()))
      if ((lf = az.ifStatement(s)) != null && step.elze(lf) != null)
        return elze == null || hasIfWithoutElse(elze);
    return false;
  }
  static Expression infix(final List<Expression> xs, final AST t) {
    if (xs.size() == 1)
      return the.firstOf(xs);
    final InfixExpression $ = t.newInfixExpression();
    $.setOperator(op.PLUS2);
    $.setLeftOperand(copy.of(the.firstOf(xs)));
    $.setRightOperand(copy.of(the.secondOf(xs)));
    for (int ¢ = 2;; ++¢, extendedOperands($).add(copy.of(xs.get(¢)))) // NANO
      if (¢ >= xs.size())
        return $;
  }
  static List<Expression> minus(final List<Expression> ¢) {
    final List<Expression> $ = an.empty.list();
    $.add(the.firstOf(¢));
    $.addAll(az.stream(the.tailOf(¢)).map(cons::minusOf).collect(toList()));
    return $;
  }

  private final int kind;

  make(final int kind) {
    this.kind = kind;
  }
  /** Creates a no-binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  @SuppressWarnings("MethodCanBeVariableArityMethod") public ASTParser parser(final char[] text) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(text);
    return $;
  }
  /** Creates a no-binding parser for a given compilation unit
   * @param u what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parser(final ICompilationUnit ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢);
    return $;
  }
  /** Creates a parser for a given {@link Document}
   * @param d JD
   * @return created parser */
  public ASTParser parser(final IDocument ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢.get().toCharArray());
    return $;
  }
  /** Creates a parser for a given {@link IFile}
   * @param function JD
   * @return created parser */
  public ASTParser parser(final IFile ¢) {
    return parser(JavaCore.createCompilationUnitFrom(¢));
  }
  /** Creates a parser for a given marked text.
   * @param ¢ JD
   * @return created parser */
  public ASTParser parser(final IMarker ¢) {
    return parser(makeAST.iCompilationUnit(¢));
  }
  /** Creates a no-binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parser(final String text) {
    return parser(text.toCharArray());
  }
  /** Creates a binding parser for a given compilation unit
   * @param u what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parserWithBinding(final ICompilationUnit ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢);
    $.setResolveBindings(true);
    return $;
  }
  public ASTParser parserWithBinding(final IFile ¢) {
    return parserWithBinding(makeAST.iCompilationUnit(¢));
  }

  public interface FromAST {
    AST ast();
    default StringLiteral emptyString() {
      return literal("");
    }
    default SimpleName identifier(final SimpleName ¢) {
      return identifier(¢.getIdentifier());
    }
    SimpleName identifier(String identifier);
    NumberLiteral literal(int i);
    StringLiteral literal(String ¢);
    NullLiteral nullLiteral();
  }

  public static class PlantingExpression {
    /** Determines whether an infix expression can be added to String concating
     * without parenthesis __ wise.
     * @param Expression
     * @return whethere is an infix expression and if it's first operand is of
     *         __ String and false otherwise */
    public static boolean isStringConactingSafe(final Expression ¢) {
      return infixExpression(¢) && isStringConcatingSafe(az.infixExpression(¢));
    }
    private static boolean isStringConcatingSafe(final InfixExpression ¢) {
      return type.of(¢.getLeftOperand()) == Certain.STRING;
    }

    private final Expression inner;

    /** Instantiates this class, recording the expression that might be wrapped.
     * @param inner JD */
    public PlantingExpression(final Expression inner) {
      this.inner = inner;
    }
    /** Executes conditional wrapping in parenthesis.
     * @param host the destined parent
     * @return either the expression itself, or the expression wrapped in
     *         parenthesis, depending on the relative precedences of the
     *         expression and its host. */
    public Expression into(final ASTNode host) {
      return host == null || noParenthesisRequiredIn(host) || stringConcatingSafeIn(host) || simple(inner) ? inner : parenthesize(inner);
    }
    public Expression intoLeft(final InfixExpression host) {
      return precedence.greater(host, inner) || precedence.equal(host, inner) || simple(inner) ? inner : parenthesize(inner);
    }
    private boolean noParenthesisRequiredIn(final ASTNode host) {
      return precedence.greater(host, inner) || precedence.equal(host, inner) && !op.nonAssociative(host);
    }
    private ParenthesizedExpression parenthesize(final Expression ¢) {
      final ParenthesizedExpression $ = inner.getAST().newParenthesizedExpression();
      $.setExpression(copy.of(¢));
      return $;
    }
    /** Determines whether inner can be added to host without parenthesis
     * because host is a String concating InfixExpression and host is an infix
     * expression starting with a String
     * @param host
     * @return */
    private boolean stringConcatingSafeIn(final ASTNode host) {
      if (!infixExpression(host))
        return false;
      final InfixExpression $ = az.infixExpression(host);
      return ($.getOperator() != op.PLUS2 || !type.isNotString($)) && isStringConactingSafe(inner);
    }
  }

  public static class PlantingStatement {
    private final Statement inner;

    public PlantingStatement(final Statement inner) {
      this.inner = inner;
    }
    public void intoThen(final IfStatement s) {
      final IfStatement plant = az.ifStatement(inner);
      s.setThenStatement(plant == null || plant.getElseStatement() != null ? inner : subject.statements(inner).toBlock());
    }
  }
}