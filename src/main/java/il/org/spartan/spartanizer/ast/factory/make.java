package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.spartanizer.ast.safety.iz.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jface.text.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.utils.*;

/** An empty {@code enum} for fluent programming. The name should say it all:
 * The name, followed by a dot, followed by a method name, should read like a
 * sentence phrase.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2015-07-16 */
public enum make {
  /** Strategy for conversion into a compilation unit */
  COMPILATION_UNIT(ASTParser.K_COMPILATION_UNIT), //
  /** Strategy for conversion into an expression */
  EXPRESSION(ASTParser.K_EXPRESSION), //
  /** Strategy for conversion into an sequence of sideEffects */
  STATEMENTS(ASTParser.K_STATEMENTS), //
  /** Strategy for conversion into a class body */
  CLASS_BODY_DECLARATIONS(ASTParser.K_CLASS_BODY_DECLARATIONS); //
  /** Converts the {@link makeAST} value to its corresponding enum value
   * @param tipper The {@link makeAST} type
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

  /** Creates a parser for a given {@link Document}
   * @param d JD
   * @return created parser */
  public ASTParser parser(final IDocument ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢.get().toCharArray());
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

  /** Creates a binding parser for a given compilation unit
   * @param u what to parse
   * @return a newly created parser for the parameter */
  public ASTParser parserWithBinding(final ICompilationUnit ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢);
    $.setResolveBindings(true);
    return $;
  }

  /** Creates a parser for a given {@link IFile}
   * @param f JD
   * @return created parser */
  public ASTParser parser(final IFile ¢) {
    return parser(JavaCore.createCompilationUnitFrom(¢));
  }

  /** Creates a parser for a given marked text.
   * @param m JD
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

  public static ForStatement buildForStatement(final VariableDeclarationFragment f, final WhileStatement ¢) {
    final ForStatement $ = ¢.getAST().newForStatement();
    $.setBody(copy.of(body(¢)));
    $.setExpression(action.pullInitializersFromExpression(copy.ofWhileExpression(¢), FragmentInitializerWhile.parent(f)));
    initializers($).add(FragmentInitializerWhile.Initializers(f));
    return $;
  }

  /** Converts a string into an AST, depending on it's form, as determined
   * by @link{GuessedContext.find}.
   * @param javaSnippet string to convert
   * @return AST, if string is not a valid AST according to any form, then
   *         null */
  public static ASTNode ast(final String javaSnippet) {
    switch (GuessedContext.find(javaSnippet)) {
      case COMPILATION_UNIT_LOOK_ALIKE:
        return into.cu(javaSnippet);
      case EXPRESSION_LOOK_ALIKE:
        return into.e(javaSnippet);
      case METHOD_LOOK_ALIKE:
        return into.m(javaSnippet);
      case OUTER_TYPE_LOOKALIKE:
        return into.t(javaSnippet);
      case STATEMENTS_LOOK_ALIKE:
        return into.s(javaSnippet);
      case BLOCK_LOOK_ALIKE:
        return az.astNode(first(statements(az.block(into.s(javaSnippet)))));
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

  public static List<Statement> listMe(final Expression ¢) {
    return as.list(¢.getAST().newExpressionStatement(copy.of(¢)));
  }

  public static VariableDeclarationExpression variableDeclarationExpression(final VariableDeclarationStatement ¢) {
    if (¢ == null)
      return null;
    final VariableDeclarationExpression $ = ¢.getAST().newVariableDeclarationExpression(copy.of(first(fragments(copy.of(¢)))));
    fragments($).addAll(extract.nextFragmentsOf(¢));
    $.setType(copy.of(step.type(¢)));
    extendedModifiers($).addAll(az.modifiersOf(¢));
    return $;
  }

  static Expression makeInfix(final List<Expression> xs, final AST t) {
    if (xs.size() == 1)
      return first(xs);
    final InfixExpression $ = t.newInfixExpression();
    $.setOperator(wizard.PLUS2);
    $.setLeftOperand(copy.of(first(xs)));
    $.setRightOperand(copy.of(second(xs)));
    for (int ¢ = 2;; ++¢, extendedOperands($).add(copy.of(xs.get(¢)))) // NANO
      if (¢ >= xs.size())
        return $;
  }

  public static Expression minus(final Expression x, final NumberLiteral l) {
    return l == null ? minusOf(x) //
        : newLiteral(l, iz.literal0(l) ? "0" : signAdjust(l.getToken())) //
    ;
  }

  static List<Expression> minus(final List<Expression> ¢) {
    final List<Expression> $ = new ArrayList<>();
    $.add(first(¢));
    $.addAll(az.stream(rest(¢)).map(make::minusOf).collect(toList()));
    return $;
  }

  static Expression minusOf(final Expression ¢) {
    return iz.literal0(¢) ? ¢ : subject.operand(¢).to(wizard.MINUS1);
  }

  static NumberLiteral newLiteral(final ASTNode n, final String token) {
    final NumberLiteral $ = n.getAST().newNumberLiteral();
    $.setToken(token);
    return $;
  }

  private static String signAdjust(final String token) {
    return token.startsWith("-") ? token.substring(1) //
        : "-" + token.substring(as.bit(token.startsWith("+")));
  }

  public static Expression assignmentAsExpression(final Assignment ¢) {
    final Operator $ = ¢.getOperator();
    return $ == ASSIGN ? copy.of(step.from(¢)) : subject.pair(step.to(¢), step.from(¢)).to(wizard.assign2infix($));
  }

  /** Swap the order of the left and right operands to an expression, changing
   * the operator if necessary.
   * @param ¢ JD
   * @return a newly created expression with its operands thus swapped.
   * @throws IllegalArgumentException when the parameter has extra operands.
   * @see InfixExpression#hasExtendedOperands */
  public static InfixExpression conjugate(final InfixExpression ¢) {
    if (¢.hasExtendedOperands())
      throw new IllegalArgumentException(¢ + ": flipping undefined for an expression with extra operands ");
    return subject.pair(right(¢), left(¢)).to(wizard.conjugate(¢.getOperator()));
  }

  public static EmptyStatement emptyStatement(final ASTNode ¢) {
    return ¢.getAST().newEmptyStatement();
  }

  public static make.FromAST from(final AST t) {
    return new make.FromAST() {
      @Override public SimpleName identifier(final String identifier) {
        return t.newSimpleName(identifier);
      }

      @Override public NumberLiteral literal(final int ¢) {
        return t.newNumberLiteral(¢ + "");
      }

      @Override public StringLiteral literal(final String ¢) {
        final StringLiteral $ = t.newStringLiteral();
        $.setLiteralValue(¢);
        return $;
      }
    };
  }

  public static make.FromAST from(final ASTNode ¢) {
    return from(¢.getAST());
  }

  public static IfStatement ifWithoutElse(final Statement s, final InfixExpression condition) {
    final IfStatement $ = condition.getAST().newIfStatement();
    $.setExpression(condition);
    $.setThenStatement(s);
    $.setElseStatement(null);
    return $;
  }

  public static StringLiteral makeEmptyString(final ASTNode ¢) {
    return make.from(¢).literal("");
  }

  public static NullLiteral makeNullLiteral(final ASTNode ¢) {
    return ¢.getAST().newNullLiteral();
  }

  public static Expression minus(final Expression ¢) {
    final PrefixExpression $ = az.prefixExpression(¢);
    return $ == null ? make.minus(¢, az.numberLiteral(¢))
        : $.getOperator() == wizard.MINUS1 ? $.getOperand() //
            : $.getOperator() == wizard.PLUS1 ? subject.operand($.getOperand()).to(wizard.MINUS1)//
                : ¢;
  }

  /** @param ¢ JD
   * @return parameter, but logically negated and simplified */
  public static Expression notOf(final Expression ¢) {
    assert ¢ != null;
    final PrefixExpression $ = subject.operand(¢).to(NOT);
    assert $ != null;
    final Expression $$ = PrefixNotPushdown.simplifyNot($);
    return $$ == null ? $ : $$;
  }

  public static ParenthesizedExpression parethesized(final Expression ¢) {
    final ParenthesizedExpression $ = ¢.getAST().newParenthesizedExpression();
    $.setExpression(parent(¢) == null ? ¢ : copy.of(¢));
    return $;
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

  /** @param ¢ the expression to return in the return statement
   * @return new return statement */
  public static ThrowStatement throwOf(final Expression ¢) {
    return subject.operand(¢).toThrow();
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

  public interface FromAST {
    default SimpleName identifier(final SimpleName ¢) {
      return identifier(¢.getIdentifier());
    }

    SimpleName identifier(String identifier);

    NumberLiteral literal(int i);

    StringLiteral literal(String literal);
  }

  public static class PlantingExpression {
    /** Determines whether an infix expression can be added to String concating
     * without parenthesis type wise.
     * @param Expression
     * @return whethere is an infix expression and if it's first operand is of
     *         type String and false otherwise */
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
      return precedence.greater(host, inner) || precedence.equal(host, inner) && !wizard.nonAssociative(host);
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
      return ($.getOperator() != wizard.PLUS2 || !type.isNotString($)) && isStringConactingSafe(inner);
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