package il.org.spartan.spartanizer.ast.factory;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.ast.safety.iz.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jface.text.*;
import org.jetbrains.annotations.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tippers.*;

/** An empty <code><b>enum</b></code> for fluent programming. The name should
 * say it all: The name, followed by a dot, followed by a method name, should
 * read like a sentence phrase.
 * @author Yossi Gil
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
  /** Converts the {@link makeAST} value to its corresponding {@link make}
   * enum value
   * @param tipper The {@link makeAST} type
   * @return corresponding {@link make} value to the argument */
  public static make from(@NotNull final makeAST ¢) {
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
  @NotNull public ASTParser parser(final char[] text) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(text);
    return $;
  }

  /** Creates a parser for a given {@link Document}
   * @param d JD
   * @return created parser */
  @NotNull public ASTParser parser(@NotNull final Document ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢.get().toCharArray());
    return $;
  }

  /** Creates a no-binding parser for a given compilation unit
   * @param u what to parse
   * @return a newly created parser for the parameter */
  @NotNull public ASTParser parser(final ICompilationUnit ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢);
    return $;
  }

  /** Creates a binding parser for a given compilation unit
   * @param u what to parse
   * @return a newly created parser for the parameter */
  @NotNull public ASTParser parserWithBinding(final ICompilationUnit ¢) {
    final ASTParser $ = wizard.parser(kind);
    $.setSource(¢);
    $.setResolveBindings(true);
    return $;
  }

  /** Creates a parser for a given {@link IFile}
   * @param f JD
   * @return created parser */
  @NotNull public ASTParser parser(final IFile ¢) {
    return parser(JavaCore.createCompilationUnitFrom(¢));
  }

  /** Creates a parser for a given marked text.
   * @param m JD
   * @return created parser */
  @NotNull public ASTParser parser(@NotNull final IMarker ¢) {
    return parser(makeAST.iCompilationUnit(¢));
  }

  /** Creates a no-binding parser for a given text
   * @param text what to parse
   * @return a newly created parser for the parameter */
  @NotNull public ASTParser parser(@NotNull final String text) {
    return parser(text.toCharArray());
  }

  static Expression makeInfix(@NotNull final List<Expression> xs, @NotNull final AST t) {
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

  @NotNull
  public static Expression minus(final Expression x, @Nullable final NumberLiteral l) {
    return l == null ? minusOf(x) //
        : newLiteral(l, iz.literal0(l) ? "0" : signAdjust(l.getToken())) //
    ;
  }

  @NotNull static List<Expression> minus(final List<Expression> xs) {
    final List<Expression> $ = new ArrayList<>();
    $.add(first(xs));
    $.addAll(az.stream(rest(xs)).map(λ -> minusOf(λ)).collect(Collectors.toList()));
    return $;
  }

  static Expression minusOf(final Expression ¢) {
    return iz.literal0(¢) ? ¢ : subject.operand(¢).to(wizard.MINUS1);
  }

  @NotNull static NumberLiteral newLiteral(@NotNull final ASTNode n, @NotNull final String token) {
    final NumberLiteral $ = n.getAST().newNumberLiteral();
    $.setToken(token);
    return $;
  }

  @NotNull private static String signAdjust(@NotNull final String token) {
    return token.startsWith("-") ? token.substring(1) //
        : "-" + token.substring(token.startsWith("+") ? 1 : 0);
  }

  @Nullable public static Expression assignmentAsExpression(@NotNull final Assignment ¢) {
    final Operator $ = ¢.getOperator();
    return $ == ASSIGN ? copy.of(step.from(¢)) : subject.pair(step.to(¢), step.from(¢)).to(wizard.assign2infix($));
  }

  /** Swap the order of the left and right operands to an expression, changing
   * the operator if necessary.
   * @param ¢ JD
   * @return a newly created expression with its operands thus swapped.
   * @throws IllegalArgumentException when the parameter has extra operands.
   * @see InfixExpression#hasExtendedOperands */
  public static InfixExpression conjugate(@NotNull final InfixExpression ¢) {
    if (¢.hasExtendedOperands())
      throw new IllegalArgumentException(¢ + ": flipping undefined for an expression with extra operands ");
    return subject.pair(right(¢), left(¢)).to(wizard.conjugate(¢.getOperator()));
  }

  public static EmptyStatement emptyStatement(@NotNull final ASTNode ¢) {
    return ¢.getAST().newEmptyStatement();
  }

  @NotNull public static make.FromAST from(@NotNull final AST t) {
    return new make.FromAST() {
      @Override @NotNull public SimpleName identifier(@NotNull final String identifier) {
        return t.newSimpleName(identifier);
      }
  
      @Override @NotNull public NumberLiteral literal(final int ¢) {
        return t.newNumberLiteral(¢ + "");
      }
  
      @Override @NotNull public StringLiteral literal(@NotNull final String ¢) {
        final StringLiteral $ = t.newStringLiteral();
        $.setLiteralValue(¢);
        return $;
      }
    };
  }

  @NotNull public static make.FromAST from(@NotNull final ASTNode ¢) {
    return from(¢.getAST());
  }

  @NotNull public static IfStatement ifWithoutElse(@NotNull final Statement s, @NotNull final InfixExpression condition) {
    final IfStatement $ = condition.getAST().newIfStatement();
    $.setExpression(condition);
    $.setThenStatement(s);
    $.setElseStatement(null);
    return $;
  }

  @NotNull public static StringLiteral makeEmptyString(@NotNull final ASTNode ¢) {
    return make.from(¢).literal("");
  }

  public static NullLiteral makeNullLiteral(@NotNull final ASTNode ¢) {
    return ¢.getAST().newNullLiteral();
  }

  @NotNull public static Expression minus(final Expression ¢) {
    final PrefixExpression $ = az.prefixExpression(¢);
    return $ == null ? make.minus(¢, az.numberLiteral(¢))
        : $.getOperator() == wizard.MINUS1 ? $.getOperand() //
            : $.getOperator() == wizard.PLUS1 ? subject.operand($.getOperand()).to(wizard.MINUS1)//
                : ¢;
  }

  /** @param ¢ JD
   * @return parameter, but logically negated and simplified */
  public static Expression notOf(final Expression ¢) {
    final PrefixExpression $ = subject.operand(¢).to(NOT);
    final Expression $$ = PrefixNotPushdown.simplifyNot($);
    return $$ == null ? $ : $$;
  }

  @NotNull public static ParenthesizedExpression parethesized(@NotNull final Expression ¢) {
    final ParenthesizedExpression $ = ¢.getAST().newParenthesizedExpression();
    $.setExpression(step.parent(¢) == null ? ¢ : copy.of(¢));
    return $;
  }

  /** A fluent API method that wraps an {@link Expression} with parenthesis, if
   * the location in which this expression occurs requires such wrapping.
   * <p>
   * Typical usage is in the form {@code new Plan(expression).in(host)} where
   * {@code location} is the parent under which the expression is to be placed.
   * <p>
   * This function is a factory method recording the expression that might be
   * wrapped.
   * @param inner JD */
  @NotNull public static make.PlantingExpression plant(final Expression ¢) {
    return new make.PlantingExpression(¢);
  }

  /** Factory method recording the statement might be wrapped.
   * @param inner JD */
  @NotNull public static make.PlantingStatement plant(final Statement inner) {
    return new make.PlantingStatement(inner);
  }

  /** @param ¢ the expression to return in the return statement
   * @return new return statement */
  public static ThrowStatement throwOf(final Expression ¢) {
    return subject.operand(¢).toThrow();
  }

  @NotNull public static VariableDeclarationStatement variableDeclarationStatement(@NotNull final Type t, @NotNull final String name,
      @NotNull final Expression x) {
    final AST create = x.getAST();
    final VariableDeclarationFragment fragment = create.newVariableDeclarationFragment();
    fragment.setName(create.newSimpleName(name));
    fragment.setInitializer(x);
    final VariableDeclarationStatement $ = create.newVariableDeclarationStatement(fragment);
    $.setType(t);
    return $;
  }

  public interface FromAST {
    @NotNull default SimpleName identifier(@NotNull final SimpleName ¢) {
      return identifier(¢.getIdentifier());
    }
  
    @NotNull SimpleName identifier(String identifier);
  
    @NotNull NumberLiteral literal(int i);
  
    @NotNull StringLiteral literal(String literal);
  }

  public static class PlantingExpression {
    /** Determines whether an infix expression can be added to String concating
     * without parenthesis type wise.
     * @param Expression
     * @return <code><b>true</b></code> <em>iff</em>e is an infix expression and
     *         if it's first operand is of type String and false otherwise */
    public static boolean isStringConactingSafe(final Expression ¢) {
      return infixExpression(¢) && isStringConcatingSafe(az.infixExpression(¢));
    }
  
    private static boolean isStringConcatingSafe(@NotNull final InfixExpression ¢) {
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
    @NotNull public Expression into(final ASTNode host) {
      return noParenthesisRequiredIn(host) || stringConcatingSafeIn(host) || simple(inner) ? inner : parenthesize(inner);
    }
  
    @NotNull public Expression intoLeft(final InfixExpression host) {
      return precedence.greater(host, inner) || precedence.equal(host, inner) || simple(inner) ? inner : parenthesize(inner);
    }
  
    private boolean noParenthesisRequiredIn(final ASTNode host) {
      return precedence.greater(host, inner) || precedence.equal(host, inner) && !wizard.nonAssociative(host);
    }
  
    @NotNull private ParenthesizedExpression parenthesize(final Expression ¢) {
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
  
    public void intoThen(@NotNull final IfStatement s) {
      final IfStatement plant = az.ifStatement(inner);
      s.setThenStatement(plant == null || plant.getElseStatement() != null ? inner : subject.statements(inner).toBlock());
    }
  }
}