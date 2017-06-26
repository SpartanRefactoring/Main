package il.org.spartan.spartanizer.ast.safety;

import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.Assignment.Operator.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;
import static il.org.spartan.spartanizer.ast.navigate.step.nodeType;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.InfixExpression.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.utils.*;

/** An empty {@code interface} for fluent programming. The name should say it
 * all: The name, followed by a dot, followed by a method name, should read like
 * a sentence phrase.
 * @author Yossi Gil
 * @since 2015-07-16 */
@SuppressWarnings("ClassWithTooManyMethods")
public interface iz {
  interface literal {
    /** @param ¢ JD
     * @return */
    static boolean classInstanceCreation(final ASTNode ¢) {
      return ¢ != null && iz.nodeTypeEquals(¢, CLASS_INSTANCE_CREATION);
    }
    /** @param ¢ JD
     * @return whether the given node is a literal false or false otherwise */
    static boolean false¢(final ASTNode ¢) {
      return iz.literal(¢, false);
    }
    /** @param ¢ JD
     * @return */
    static boolean fieldAccess(final Expression ¢) {
      return ¢ != null && iz.nodeTypeEquals(¢, FIELD_ACCESS);
    }
    /** @param ¢ JD
     * @return whether the given node is a literal true or false otherwise */
    static boolean true¢(final ASTNode ¢) {
      return iz.literal(¢, true);
    }
    static boolean xliteral(final String s, final ASTNode ¢) {
      return iz.literal(az.stringLiteral(¢), s);
    }
  }

  List<String> defaultLiterals = as.list("null", "0", "false", "0.", "0L");
  int[] sequencerTypes = { RETURN_STATEMENT, BREAK_STATEMENT, CONTINUE_STATEMENT, THROW_STATEMENT };

  static boolean abstract¢(final BodyDeclaration ¢) {
    return (¢.getModifiers() & Modifier.ABSTRACT) != 0;
  }
  static boolean abstractTypeDeclaration(final ASTNode ¢) {
    return ¢ instanceof AbstractTypeDeclaration;
  }
  static boolean annotation(final IExtendedModifier ¢) {
    return ¢ instanceof Annotation;
  }
  static boolean annotationTypeDeclaration(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, ANNOTATION_TYPE_DECLARATION);
  }
  static boolean annotationTypeMemberDeclaration(final BodyDeclaration ¢) {
    return iz.nodeTypeEquals(¢, ANNOTATION_TYPE_MEMBER_DECLARATION);
  }
  static boolean anonymousClassDeclaration(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, ANONYMOUS_CLASS_DECLARATION);
  }
  static boolean anyOperator(final ASTNode ¢) {
    return as.list(new Class<?>[] { InfixExpression.Operator.class, PrefixExpression.Operator.class, PostfixExpression.Operator.class,
        Assignment.Operator.class }).contains(¢.getClass());
  }
  /** Ceck if an ASTNode is an Array Acess
   * @param ¢ JD
   * @return */
  static boolean arrayAccess(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, ARRAY_ACCESS);
  }
  static boolean arrayInitializer(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, ARRAY_INITIALIZER);
  }
  /** @param pattern the statement or block to check if it is an assignment
   * @return {@code true} if the parameter an assignment or false if the
   *         parameter not or if the block Contains more than one statement */
  static boolean assignment(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, ASSIGNMENT);
  }
  static boolean astNode(final Object ¢) {
    return ¢ instanceof ASTNode;
  }
  /** @param ¢ JD
   * @return */
  static boolean atomic(final ASTNode ¢) {
    return iz.name(¢) || iz.literal(¢);
  }
  /** Determine whether a node is a {@link Block}
   * @param pattern JD
   * @return whether the parameter is a block statement */
  static boolean block(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, BLOCK);
  }
  /** @param subject JD
   * @return whether the parameter is an essential block or false otherwise */
  @SuppressWarnings("boxing") static boolean blockEssential(final Statement ¢) {
    final Block b = az.block(parent(¢));
    if (b == null)
      return false;
    Statement p = az.statement(parent(b));
    for (; !iz.ifStatement(p); p = az.statement(p.getParent()))
      if (p == null || !is.in(p.getNodeType(), WHILE_STATEMENT, FOR_STATEMENT, ENHANCED_FOR_STATEMENT))
        return false;
    final IfStatement parent = az.ifStatement(p);
    for (Statement current = ¢; current != null;)
      switch (current.getNodeType()) {
        case IF_STATEMENT:
          final IfStatement s = az.ifStatement(current);
          if (elze(s) == null)
            return (!eq(s, az.astNode(the.firstOf(statements(az.block(elze(parent)))))) || misc.recursiveElse(s) != null || elze(parent) == null)
                && (elze(parent) == null || misc.recursiveElse(s) == null)
                && (elze(parent) != null || misc.recursiveElse(s) != null || blockRequiredInReplacement(parent, s));
          current = elze(s);
          break;
        case WHILE_STATEMENT:
          current = az.whileStatement(current).getBody();
          break;
        case FOR_STATEMENT:
          current = az.forStatement(current).getBody();
          break;
        case ENHANCED_FOR_STATEMENT:
          current = az.enhancedFor(current).getBody();
          break;
        default:
          return false;
      }
    return false;
  }
  /** @param subject JD
   * @return */
  static boolean blockRequired(final IfStatement ¢) {
    return blockRequiredInReplacement(¢, ¢);
  }
  static boolean blockRequired(final Statement ¢) {
    return blockRequired(az.ifStatement(¢));
  }
  static boolean blockRequiredInReplacement(final IfStatement old, final IfStatement newIf) {
    if (newIf == null || old != newIf && elze(old) == null == (elze(newIf) == null))
      return false;
    final IfStatement $ = az.ifStatement(parent(old));
    return $ != null && then($) == old && (elze($) == null || elze(newIf) == null)
        && (elze($) != null || elze(newIf) != null || blockRequiredInReplacement($, newIf));
  }
  static boolean bodyDeclaration(final ASTNode ¢) {
    return ¢ instanceof BodyDeclaration;
  }
  /** Determine whether a node is a boolean literal
   * @param pattern JD
   * @return whether the parameter is a boolean literal */
  static boolean booleanLiteral(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, BOOLEAN_LITERAL);
  }
  /** @param ¢ node to check
   * @return whether the given node is a boolean or null literal or false
   *         otherwise */
  static boolean booleanOrNullLiteral(final ASTNode ¢) {
    return iz.nodeTypeIn(¢, BOOLEAN_LITERAL, NULL_LITERAL);
  }
  /** @param ¢ JD
   * @return */
  static boolean booleanType(final Type ¢) {
    return ¢ instanceof PrimitiveType && ((PrimitiveType) ¢).getPrimitiveTypeCode().equals(PrimitiveType.BOOLEAN);
  }
  /** @param ¢ JD
   * @return is boxed __ */
  static boolean boxedType(final Type ¢) {
    return ¢ instanceof SimpleType && as.list("Boolean", "Byte", "Character", "Float", "Integer", "Long", "Short", "Double")
        .contains(step.identifier(step.name((SimpleType) ¢)));
  }
  static boolean breakStatement(final Statement ¢) {
    return iz.nodeTypeEquals(¢, BREAK_STATEMENT);
  }
  /** @param ¢ JD
   * @return */
  static boolean castExpression(final ASTNode ¢) {
    return ¢ instanceof CastExpression;
  }
  static boolean catchClause(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, CATCH_CLAUSE);
  }
  static boolean classInstanceCreation(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, CLASS_INSTANCE_CREATION);
  }
  static boolean comparison(final Expression ¢) {
    return iz.infixExpression(¢) && iz.comparison(az.infixExpression(¢));
  }
  /** @param x JD
   * @return whether the parameter is a comparison expression. */
  static boolean comparison(final InfixExpression ¢) {
    return ¢ != null && is.in(¢.getOperator(), EQUALS, GREATER, GREATER_EQUALS, LESS, LESS_EQUALS, NOT_EQUALS);
  }
  static boolean comparison(final Operator ¢) {
    return is.in(¢, EQUALS, NOT_EQUALS, GREATER_EQUALS, GREATER, LESS, LESS_EQUALS);
  }
  static boolean compilationUnit(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, COMPILATION_UNIT);
  }
  /** Check whether an expression is a "conditional and" (&&)
   * @param x JD
   * @return whether the parameter is an expression whose operator is
   *         {@link InfixExpression.Operator#CONDITIONAL_AND} */
  static boolean conditionalAnd(final InfixExpression ¢) {
    return ¢.getOperator() == CONDITIONAL_AND;
  }
  static boolean conditionalExpression(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, CONDITIONAL_EXPRESSION);
  }
  /** @param xs JD
   * @return whether one of the parameters is a conditional or parenthesized
   *         conditional expression */
  static boolean conditionalExpression(final Expression... xs) {
    return Stream.of(xs).anyMatch(λ -> iz.nodeTypeEquals(extract.core(λ), CONDITIONAL_EXPRESSION));
  }
  /** Check whether an expression is a "conditional or" (||)
   * @param x JD
   * @return whether the parameter is an expression whose operator is
   *         {@link InfixExpression.Operator#CONDITIONAL_OR} */
  static boolean conditionalOr(final Expression ¢) {
    return conditionalOr(az.infixExpression(¢));
  }
  /** Check whether an expression is a "conditional or" (||)
   * @param x JD
   * @return whether the parameter is an expression whose operator is
   *         {@link InfixExpression.Operator#CONDITIONAL_OR} */
  static boolean conditionalOr(final InfixExpression ¢) {
    return ¢ != null && ¢.getOperator() == CONDITIONAL_OR;
  }
  /** Determine whether a node is a "specific", i.e., {@code null or
   * {@code this} or literal.
   * @param x JD
   * @return whether the parameter is a "specific" */
  static boolean constant(final Expression ¢) {
    return iz.nodeTypeIn(¢, CHARACTER_LITERAL, NUMBER_LITERAL, NULL_LITERAL, THIS_EXPRESSION)
        || iz.nodeTypeEquals(¢, PREFIX_EXPRESSION) && iz.constant(extract.core(((PrefixExpression) ¢).getOperand()));
  }
  /** @param ¢ JD
   * @return is public static final */
  static boolean constant(final FieldDeclaration ¢) {
    return iz.public¢(¢) && iz.static¢(¢) && iz.final¢(¢);
  }
  static boolean constructor(final ASTNode ¢) {
    return iz.methodDeclaration(¢) && az.methodDeclaration(¢).isConstructor();
  }
  static boolean constructorInvocation(final ASTNode ¢) {
    return ¢ != null && iz.nodeTypeEquals(¢, CONSTRUCTOR_INVOCATION);
  }
  /** Determine whether an {@link ASTNode} contains as a children a
   * {@link ContinueStatement}
   * @param ¢ JD
   * @return {@code true } iff ¢ contains any continue statement
   * @see {@link convertWhileToFor} */
  @SuppressWarnings("boxing") static boolean containsContinueStatement(final ASTNode ¢) {
    return ¢ != null
        && new Recurser<>(¢, 0).postVisit(λ -> λ.getRoot().getNodeType() != CONTINUE_STATEMENT ? λ.getCurrent() : λ.getCurrent() + 1) > 0;
  }
  /** @param n root node to search the identifier in it
   * @param x the identifier to search for
   * @return true if x contains the identifier of n */
  static boolean containsName(final SimpleName n, final ASTNode x) {
    return extract.countNameInSubtree(n, x) != 0;
  }
  static boolean containsOperator(final ASTNode ¢) {
    return iz.nodeTypeIn(¢, INFIX_EXPRESSION, PREFIX_EXPRESSION, POSTFIX_EXPRESSION, ASSIGNMENT);
  }
  /** @param ¢ JD
   * @return */
  static boolean continueStatement(final ASTNode ¢) {
    return ¢ instanceof ContinueStatement;
  }
  static boolean default¢(final BodyDeclaration node) {
    return (Modifier.DEFAULT & node.getModifiers()) != 0;
  }
  static boolean defaultLiteral(final ASTNode ¢2) {
    return iz.nullLiteral(¢2) || iz.literal0(¢2) || literal.false¢(¢2) || iz.literal(¢2, 0.0) || iz.literal(¢2, 0L);
  }
  static boolean definiteLoop(final ASTNode n) {
    if (!iz.loop(n))
      return false;
    final Bool $ = new Bool(true);
    // noinspection SameReturnValue,SameReturnValue,SameReturnValue
    n.accept(new ASTVisitor(true) {
      @Override public boolean visit(final BreakStatement ¢) {
        mark(¢);
        return false;
      }
      @Override public boolean visit(final ContinueStatement ¢) {
        mark(¢);
        return false;
      }
      @Override public boolean visit(final ReturnStatement ¢) {
        mark(¢);
        return false;
      }
      void mark(@SuppressWarnings("unused") final ASTNode __) {
        $.inner = false;
      }
    });
    return $.inner;
  }
  /** Check whether the operator of an expression is susceptible for applying
   * one of the two de Morgan laws.
   * @param x InfixExpression
   * @return whether the parameter is an operator on which the de Morgan laws
   *         apply. */
  static boolean deMorgan(final InfixExpression ¢) {
    return ¢ != null && iz.deMorgan(¢.getOperator());
  }
  /** Check whether an operator is susceptible for applying one of the two de
   * Morgan laws.
   * @param o JD
   * @return whether the parameter is an operator on which the de Morgan laws
   *         apply. */
  static boolean deMorgan(final Operator ¢) {
    return is.in(¢, CONDITIONAL_AND, CONDITIONAL_OR);
  }
  static boolean deterministic(final ArrayAccess $) {
    return iz.deterministic($.getArray(), $.getIndex());
  }
  static boolean deterministic(final Assignment $) {
    return iz.deterministic(to($), from($));
  }
  static boolean deterministic(final Collection<Expression> ¢) {
    return deterministic(¢.stream());
  }
  static boolean deterministic(final ConditionalExpression $) {
    return iz.deterministic(step.expression($), then($), elze($));
  }
  static boolean deterministic(final Expression... ¢) {
    return deterministic(Stream.of(¢));
  }
  static boolean deterministic(final Expression ¢) {
    if (haz.sideEffects(¢))
      return false;
    switch (¢.getNodeType()) {
      case PREFIX_EXPRESSION:
        return deterministic(az.prefixExpression(¢));
      case INFIX_EXPRESSION:
        return deterministic(extract.allOperands(az.infixExpression(¢)));
      case CONDITIONAL_EXPRESSION:
        return deterministic(az.conditionalExpression(¢));
      case INSTANCEOF_EXPRESSION:
        return deterministic(step.expression(az.instanceofExpression(¢)));
      case ARRAY_ACCESS:
        return deterministic(az.arrayAccess(¢));
      case PARENTHESIZED_EXPRESSION:
        return deterministic(extract.core(¢));
      case ASSIGNMENT:
        return deterministic(az.assignment(¢));
      case ARRAY_INITIALIZER:
        return deterministic(az.arrayAccess(¢).getArray()) && iz.deterministic(expressions(az.arrayInitializer(¢)));
      case ARRAY_CREATION:
      case CLASS_INSTANCE_CREATION:
      case POSTFIX_EXPRESSION:
        return false;
      case METHOD_INVOCATION:
        return deterministic(az.methodInvocation(¢));
      case SUPER_METHOD_INVOCATION:
        return deterministic(az.superMethodInvocation(¢));
      default:
        return true;
    }
  }
  static boolean deterministic(final List<Expression> ¢) {
    return deterministic(¢.stream());
  }
  static boolean deterministic(final MethodInvocation ¢) {
    return is.in(guessName.of(¢.getName() + ""), guessName.GETTER_METHOD, guessName.IS_METHOD) && //
        iz.deterministic(arguments(¢));
  }
  static boolean deterministic(final PrefixExpression ¢) {
    return !is.in(¢.getOperator(), op.INCREMENT_PRE, op.DECREMENT_PRE) && deterministic(¢.getOperand());
  }
  static boolean deterministic(final Stream<Expression> ¢) {
    return ¢.allMatch(iz::deterministic);
  }
  static boolean deterministic(final SuperMethodInvocation ¢) {
    return deterministic(arguments(¢));
  }
  static boolean doStatement(final ASTNode ¢) {
    return ¢ instanceof DoStatement;
  }
  static boolean doubleType(final Expression ¢) {
    return type.of(¢) == DOUBLE;
  }
  static boolean empty(final Javadoc ¢) {
    return tags(¢).stream().allMatch(iz::empty);
  }
  static boolean empty(final TagElement e) {
    return fragments(e).stream().allMatch(λ -> λ == null || λ instanceof SimpleName || λ instanceof TextElement && iz.empty((TextElement) λ));
  }
  static boolean empty(final TextElement ¢) {
    return ¢.getText().replaceAll("[\\s*]", "").isEmpty();
  }
  /** @param x JD
   * @return */
  static boolean emptyBlock(final Block x) {
    return statements(x) == null || statements(x).isEmpty();
  }
  /** Determine whether a node is an {@link EmptyStatement}
   * @param pattern JD
   * @return whether the parameter is an {@link EmptyStatement} */
  static boolean emptyStatement(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, EMPTY_STATEMENT);
  }
  static boolean emptyStringLiteral(final ASTNode ¢) {
    return iz.emptyStringLiteral(az.stringLiteral(¢));
  }
  static boolean emptyStringLiteral(final StringLiteral ¢) {
    return ¢ != null && ¢.getLiteralValue().isEmpty();
  }
  static boolean enhancedFor(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, ENHANCED_FOR_STATEMENT);
  }
  static boolean enumConstantDeclaration(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, ENUM_CONSTANT_DECLARATION);
  }
  static boolean enumDeclaration(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, ENUM_DECLARATION);
  }
  /** Determine whether a node is an "expression statement"
   * @param pattern JD
   * @return whether the parameter is an {@link ExpressionStatement}
   *         statement */
  static boolean expression(final ASTNode ¢) {
    return ¢ instanceof Expression;
  }
  static boolean expressionOfEnhancedFor(final ASTNode child, final ASTNode parent) {
    if (child == null || parent == null || !iz.enhancedFor(parent))
      return false;
    final EnhancedForStatement $ = az.enhancedFor(parent);
    assert $ != null;
    assert step.expression($) != null;
    return step.expression($) == child;
  }
  /** Determine whether a node is an "expression statement"
   * @param pattern JD
   * @return whether the parameter is an {@link ExpressionStatement}
   *         statement */
  static boolean expressionStatement(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, EXPRESSION_STATEMENT);
  }
  /** @param ¢ JD
   * @return */
  static boolean fieldAccess(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, FIELD_ACCESS);
  }
  static boolean fieldDeclaration(final BodyDeclaration ¢) {
    return iz.nodeTypeEquals(¢, FIELD_DECLARATION);
  }
  /** Determine whether a declaration is final or not
   * @param ¢ JD
   * @return whetherdeclaration is final */
  static boolean final¢(final BodyDeclaration ¢) {
    return (Modifier.FINAL & ¢.getModifiers()) != 0;
  }
  /** Determine whether a variable declaration is final or not
   * @param ¢ JD
   * @return whether the variable is declared as final */
  static boolean final¢(final VariableDeclarationStatement ¢) {
    return ¢ != null && (Modifier.FINAL & ¢.getModifiers()) != 0;
  }
  static int findRadix(final String $) {
    return $.matches("[+-]?0[xX].*") ? 16 : $.matches("[+-]?0[bB].*") ? 2 : $.matches("[+-]?0.*") ? 8 : 10;
  }
  static boolean finiteLoop(final ForStatement ¢) {
    final Expression $ = ¢.getExpression();
    return $ != null && !iz.literal.true¢($);
  }
  static boolean finiteLoop(final WhileStatement ¢) {
    return !iz.literal.true¢(¢.getExpression());
  }
  /** @param o The operator to check
   * @return True - if the operator have opposite one in terms of operands
   *         swap. */
  static boolean flipable(final Operator ¢) {
    return is.in(¢, AND, EQUALS, GREATER, GREATER_EQUALS, LESS_EQUALS, LESS, NOT_EQUALS, OR, PLUS, TIMES, XOR, null);
  }
  /** @param pattern the statement or block to check if it is an for statement
   * @return {@code true} if the parameter an for statement or false if the
   *         parameter not or if the block Contains more than one statement */
  static boolean forStatement(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, FOR_STATEMENT);
  }
  /** @param with /** @return */
  @SuppressWarnings("all") static boolean fragile(final Expression with) {
    // TODO Yossi Gil Auto-generated method stub for fragile
    if (new Object().hashCode() != 0)
      throw new AssertionError("Stub 'iz::fragile' not implemented yet (created on  2017-01-30).");
    return false;
  }
  static boolean identifier(final String identifier, final Name typeName) {
    return typeName.isQualifiedName() ? identifier(identifier, ((QualifiedName) typeName).getName())
        : simpleName(typeName) && identifier(identifier, az.simpleName(typeName));
  }
  static boolean identifier(final String identifier, final SimpleName n) {
    return identifier.equals(n.getIdentifier());
  }
  static boolean ifStatement(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, IF_STATEMENT);
  }
  static boolean incrementedOrDecremented(final ASTNode id) {
    final ASTNode $ = parent(id);
    return iz.prefixExpression($) || iz.updating(az.prefixExpression($));
  }
  /** @param ¢ JD
   * @return whether the given node is an infix expression or false otherwise */
  static boolean infix(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, INFIX_EXPRESSION);
  }
  static boolean infixDivide(final Expression ¢) {
    return operator(az.infixExpression(¢)) == DIVIDE;
  }
  static boolean infixEquals(final Expression ¢) {
    return operator(az.infixExpression(¢)) == EQUALS;
  }
  static boolean infixExpression(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, INFIX_EXPRESSION);
  }
  static boolean infixGreater(final Expression ¢) {
    return operator(az.infixExpression(¢)) == GREATER;
  }
  static boolean infixGreaterEquals(final InfixExpression ¢) {
    return operator(az.infixExpression(¢)) == GREATER_EQUALS;
  }
  static boolean infixLess(final Expression ¢) {
    return operator(az.infixExpression(¢)) == LESS;
  }
  static boolean infixLessEquals(final InfixExpression ¢) {
    return operator(az.infixExpression(¢)) == LESS_EQUALS;
  }
  static boolean infixMinus(final ASTNode ¢) {
    return operator(az.infixExpression(¢)) == op.MINUS2;
  }
  static boolean infixPlus(final ASTNode ¢) {
    return operator(az.infixExpression(¢)) == op.PLUS2;
  }
  static boolean infixTimes(final Expression ¢) {
    return operator(az.infixExpression(¢)) == TIMES;
  }
  static boolean initializer(final ASTNode c) {
    return iz.nodeTypeEquals(c, INITIALIZER);
  }
  /** @param ¢ JD
   * @return */
  static boolean instanceofExpression(final Expression ¢) {
    return ¢ instanceof InstanceofExpression;
  }
  /** @param ¢ JD
   * @return whether the given node is an interface or false otherwise */
  static boolean interface¢(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, TYPE_DECLARATION) && ((TypeDeclaration) ¢).isInterface();
  }
  static boolean intType(final Expression ¢) {
    return ¢ != null && type.of(¢) == INT;
  }
  static boolean intType(final Type ¢) {
    return ¢ instanceof PrimitiveType && ((PrimitiveType) ¢).getPrimitiveTypeCode().equals(PrimitiveType.INT);
  }
  /** @param ¢ JD
   * @return whether the given node is a method decleration or false
   *         otherwise */
  static boolean isMethodDeclaration(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, METHOD_DECLARATION);
  }
  /** @param ¢ node to check
   * @return whether the given node is a method invocation or false otherwise */
  static boolean isMethodInvocation(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, METHOD_INVOCATION);
  }
  /** @param a the assignment whose operator we want to check
   * @return whether the assignment'¢ operator is plus assign */
  static boolean isMinusAssignment(final Assignment ¢) {
    return ¢ != null && ¢.getOperator() == MINUS_ASSIGN;
  }
  static boolean isOneOf(final int i, final int... is) {
    return Arrays.stream(is).filter(λ -> i == λ).findAny().isPresent();
  }
  /** @param a the assignment whose operator we want to check
   * @return whether the assignment'¢ operator is assign */
  static boolean isPlainAssignment(final Assignment ¢) {
    return ¢ != null && ¢.getOperator() == ASSIGN;
  }
  /** @param a the assignment whose operator we want to check
   * @return whether the assignment'¢ operator is plus assign */
  static boolean isPlusAssignment(final Assignment ¢) {
    return ¢ != null && ¢.getOperator() == PLUS_ASSIGN;
  }
  /** @param ¢ node to check
   * @return whether the given node is a variable declaration statement or false
   *         otherwise */
  static boolean isVariableDeclarationStatement(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, VARIABLE_DECLARATION_STATEMENT);
  }
  static iz izParser(final String name) {
    return new iz() {
      @Override public String toString() {
        return name;
      }
    };
  }
  static iz izParser(final Throwable ¢) {
    return new iz() {
      @Override public String toString() {
        return Arrays.toString(¢.getStackTrace());
      }
    };
  }
  /** @param ¢ JD
   * @return */
  static boolean labeledStatement(final ASTNode ¢) {
    return ¢ instanceof LabeledStatement;
  }
  static boolean lambdaExpression(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, LAMBDA_EXPRESSION);
  }
  /** Determine whether an item is the last one in a list
   * @param t a list item
   * @param ts a list
   * @return whether the item is found in the list and it is the last one in
   *         it. */
  static <T> boolean last(final T t, final List<T> ts) {
    return ts.indexOf(t) == ts.size() - 1;
  }
  /** Determines whether a statement is last statement in its containing method
   * @param ¢ JD
   * @return whether the parameter is a statement which is last in its method */
  static boolean lastInMethod(final Statement ¢) {
    final Block $ = az.block(parent(¢));
    return last(¢, statements($)) && iz.methodDeclaration(parent($));
  }
  static boolean leftOfAssignment(final Expression ¢) {
    return left(az.assignment(¢.getParent())).equals(¢);
  }
  /** @param pattern Expression node
   * @return whether the Expression is literal */
  static boolean literal(final ASTNode ¢) {
    return ¢ != null && is.intIsIn(nodeType(¢), NULL_LITERAL, CHARACTER_LITERAL, NUMBER_LITERAL, STRING_LITERAL, BOOLEAN_LITERAL);
  }
  static boolean literal(final ASTNode ¢, final boolean b) {
    return ¢ != null && literal(az.booleanLiteral(¢), b);
  }
  static boolean literal(final ASTNode ¢, final double d) {
    final NumberLiteral numberLiteral = az.numberLiteral(¢);
    if (numberLiteral == null)
      return false;
    final String $ = numberLiteral.getToken();
    return NumericLiteralClassifier.of($) == DOUBLE && iz.izParser("Searching for double").parsesTo($, d);
  }
  static boolean literal(final ASTNode ¢, final int i) {
    final NumberLiteral numberLiteral = az.numberLiteral(¢);
    if (numberLiteral == null)
      return false;
    final String $ = numberLiteral.getToken();
    return NumericLiteralClassifier.of($) == INT && iz.izParser("Searching for int").parsesTo($, i);
  }
  static boolean literal(final ASTNode ¢, final long l) {
    final NumberLiteral numberLiteral = az.numberLiteral(¢);
    if (numberLiteral == null)
      return false;
    final String $ = numberLiteral.getToken();
    return NumericLiteralClassifier.of($) == LONG && iz.izParser("Seaching for LONG").parsesTo($, l);
  }
  static boolean literal(final BooleanLiteral ¢, final boolean b) {
    return ¢ != null && ¢.booleanValue() == b;
  }
  /** @param subject JD
   * @return whether the parameter return a literal */
  static boolean literal(final ReturnStatement ¢) {
    return ¢ != null && literal(¢.getExpression());
  }
  static boolean literal(final String literal, final ASTNode ¢) {
    return literal(literal, az.stringLiteral(¢));
  }
  static boolean literal(final String literal, final StringLiteral ¢) {
    return ¢ != null && ¢.getLiteralValue().equals(literal);
  }
  static boolean literal(final StringLiteral ¢, final String s) {
    return ¢ != null && ¢.getLiteralValue().equals(s);
  }
  /** @param ¢ JD
   * @return whether the given node is a literal 0 or false otherwise */
  static boolean literal0(final ASTNode ¢) {
    return literal(¢, 0);
  }
  /** @param ¢ JD
   * @return whether the given node is a literal 1 or false otherwise */
  static boolean literal1(final ASTNode ¢) {
    return literal(¢, 1);
  }
  static boolean longType(final Expression ¢) {
    return type.of(¢) == LONG;
  }
  static boolean longType(final Type ¢) {
    return ¢ instanceof PrimitiveType && ((PrimitiveType) ¢).getPrimitiveTypeCode().equals(PrimitiveType.LONG);
  }
  static boolean loop(final ASTNode ¢) {
    return forStatement(¢) || enhancedFor(¢) || whileStatement(¢) || doStatement(¢);
  }
  static boolean memberRef(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, MEMBER_REF);
  }
  /** Determine whether a node is a {@link MethodDeclaration}
   * @param pattern JD
   * @return whether the parameter is a method invocation. */
  static boolean methodDeclaration(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, METHOD_DECLARATION);
  }
  /** Determine whether a node is a {@link MethodInvocation}
   * @param pattern JD
   * @return whether the parameter is a method invocation. */
  static boolean methodInvocation(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, METHOD_INVOCATION);
  }
  static boolean modifier(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, MODIFIER);
  }
  static boolean name(final ASTNode ¢) {
    return ¢ instanceof Name;
  }
  static boolean native¢(final BodyDeclaration node) {
    return (Modifier.NATIVE & node.getModifiers()) != 0;
  }
  static boolean negative(final Expression ¢) {
    return negative(az.prefixExpression(¢)) || negative(az.numberLiteral(¢));
  }
  static boolean negative(final NumberLiteral ¢) {
    return ¢ != null && ¢.getToken().startsWith("-");
  }
  static boolean negative(final PrefixExpression ¢) {
    return ¢ != null && ¢.getOperator() == PrefixExpression.Operator.MINUS;
  }
  static boolean nodeTypeEquals(final ASTNode n, final int type) {
    return n != null && type == n.getNodeType();
  }
  /** Determine whether the __ of an {@link ASTNode} node is one of given list
   * @param n a node
   * @param types a list of types
   * @return whether function #ASTNode.getNodeType returns one of the types
   *         provided as parameters */
  static boolean nodeTypeIn(final ASTNode n, final int... types) {
    return n != null && is.intIsIn(n.getNodeType(), types);
  }
  /** Determine whether an {@link Expression} is so basic that it never needs to
   * be placed in parenthesis.
   * @param x JD
   * @return whether the parameter is so basic that it never needs to be placed
   *         in parenthesis. */
  static boolean noParenthesisRequired(final Expression ¢) {
    return iz.nodeTypeIn(¢, ARRAY_ACCESS, ARRAY_CREATION, BOOLEAN_LITERAL, CAST_EXPRESSION, CHARACTER_LITERAL, CLASS_INSTANCE_CREATION, FIELD_ACCESS,
        INSTANCEOF_EXPRESSION, METHOD_INVOCATION, NULL_LITERAL, NUMBER_LITERAL, PARAMETERIZED_TYPE, PARENTHESIZED_EXPRESSION, QUALIFIED_NAME,
        SIMPLE_NAME, STRING_LITERAL, SUPER_CONSTRUCTOR_INVOCATION, SUPER_FIELD_ACCESS, SUPER_METHOD_INVOCATION, THIS_EXPRESSION, TYPE_LITERAL);
  }
  static boolean normalAnnotations(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, NORMAL_ANNOTATION);
  }
  /** Determine whether a node is the {@code null keyword
   * @param pattern JD
   * @return whether it is the {@code null
   *         literal */
  static boolean nullLiteral(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, NULL_LITERAL);
  }
  static boolean number(final Expression ¢) {
    return iz.numberLiteral(¢) && (type.isInt(¢) || type.isDouble(¢) || type.isLong(¢));
  }
  static boolean numberLiteral(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, NUMBER_LITERAL);
  }
  /** Determine whether a node is {@code this} or {@code null
   * @param x JD
   * @return whether the parameter is a block statement */
  static boolean numericLiteral(final Expression ¢) {
    return iz.nodeTypeIn(¢, CHARACTER_LITERAL, NUMBER_LITERAL);
  }
  static boolean parameterizedType(final ASTNode ¢) {
    return ¢ instanceof ParameterizedType;
  }
  static boolean parenthesizedExpression(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, PARENTHESIZED_EXPRESSION);
  }
  static int parseInt(final String token) {
    final String $ = token.replaceAll("[\\s_]", "");
    return Integer.parseInt($.replaceFirst("0[xX]", "").replaceAll("0[bB]", ""), findRadix($));
  }
  static long parseLong(final String token) {
    final String $ = token.replaceAll("[\\s_Ll]", "");
    return Long.parseLong($.replaceFirst("0[xX]", "").replaceAll("0[bB]", ""), findRadix($));
  }
  /** @param a the assignment who's operator we want to check
   * @return true is the assignment's operator is assign */
  static boolean plainAssignment(final Assignment ¢) {
    return ¢ != null && ¢.getOperator() == ASSIGN;
  }
  static boolean postfixExpression(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, POSTFIX_EXPRESSION);
  }
  /** @param pattern JD
   * @return whether the parameter is a prefix expression. */
  static boolean prefixExpression(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, PREFIX_EXPRESSION);
  }
  static boolean prefixMinus(final Expression ¢) {
    return iz.prefixExpression(¢) && az.prefixExpression(¢).getOperator() == op.MINUS1;
  }
  /** @param ¢ JD
   * @return */
  static boolean primitiveType(final Type ¢) {
    return ¢ instanceof PrimitiveType;
  }
  /** Determine whether a declaration is private
   * @param ¢ JD
   * @return whetherdeclaration is private */
  static boolean private¢(final BodyDeclaration ¢) {
    return (Modifier.PRIVATE & ¢.getModifiers()) != 0;
  }
  /** Determine whether a Class Declaration is private
   * @param ¢ JD
   * @return whetherdeclaration is private */
  static boolean private¢(final TypeDeclaration ¢) {
    return (Modifier.PRIVATE & ¢.getModifiers()) != 0;
  }
  /** Determine whether a field declaration is private
   * @param ¢ JD
   * @return whetherdeclaration is private */
  static boolean private¢(final FieldDeclaration ¢) {
    return (Modifier.PRIVATE & ¢.getModifiers()) != 0;
  }
  static boolean protected¢(final BodyDeclaration ¢) {
    return (¢.getModifiers() & Modifier.PROTECTED) != 0;
  }
  static boolean pseudoNumber(final Expression ¢) {
    return number(¢) || iz.prefixMinus(¢) && iz.number(az.prefixExpression(¢).getOperand());
  }
  static boolean public¢(final BodyDeclaration ¢) {
    return (Modifier.PUBLIC & ¢.getModifiers()) != 0;
  }
  /** Determine whether a node is a qualified name
   * @param pattern JD
   * @return whether the parameter is a qualified name */
  static boolean qualifiedName(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, QUALIFIED_NAME);
  }
  /** Determine whether a node is a return statement
   * @param pattern JD
   * @return whether the parameter is a return statement. */
  static boolean returnStatement(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, RETURN_STATEMENT);
  }
  static boolean rightOfAssignment(final Expression ¢) {
    return ¢ != null && right(az.assignment(¢.getParent())).equals(¢);
  }
  /** Determine whether a node is a "sequencer", i.e., {@code return} ,
   * {@code break}, {@code continue} or {@code throw}
   * @param pattern JD
   * @return whether the parameter is a sequencer */
  static boolean sequencer(final ASTNode ¢) {
    return iz.nodeTypeIn(¢, sequencerTypes);
  }
  static boolean sequencer(final ASTNode ¢, final int type) {
    assert sequencerTypes[0] == type || sequencerTypes[1] == type || sequencerTypes[2] == type || sequencerTypes[3] == type;
    return ¢.getNodeType() == type;
  }
  /** As {@link iz#sequencer}, but also accepts complex sequencers, i.e. a
   * statement that makes the following statements unreachable. Example:
   * {@code if (b)
   *   return 1;
   * else
   *   return 2;
   * assert false: "Unreachable";
   * } snippet as this usually do not compile: nevertheless, complex sequencers
   * are relevant in switch statements.
   * @param ¢ JD
   * @return whether the parameter is a sequencer (may be complex) */
  static boolean sequencerComplex(final ASTNode ¢) {
    if (¢ == null)
      return false;
    switch (¢.getNodeType()) {
      case BLOCK: // Not the final implementation: should be changed
        // when adding support for loops, switches etc.
        return statements((Block) ¢).stream().anyMatch(iz::sequencerComplex);
      case IF_STATEMENT:
        final IfStatement $ = (IfStatement) ¢;
        return sequencerComplex($.getThenStatement()) && sequencerComplex($.getElseStatement());
      case TRY_STATEMENT:
        return sequencerComplex(az.tryStatement(¢).getFinally())
            || sequencerComplex(az.tryStatement(¢).getBody()) && step.catchClauses(az.tryStatement(¢)).stream().allMatch(iz::sequencerComplex);
      case CATCH_CLAUSE:
        return sequencerComplex(az.catchClause(¢).getBody());
      default:
        return sequencer(¢);
    }
  }
  /** @param ¢
   * @param __ Type of sequencer
   * @return true if ¢ contains this sequencer (only for if-else and blocks) In
   *         contrast to sequencerComplex(ASTNode) above, this method not
   *         necessarily checks the following statements are not reachable. */
  static boolean sequencerComplex(final ASTNode ¢, final int type) {
    if (¢ == null)
      return false;
    switch (¢.getNodeType()) {
      case BLOCK:
        return statements(az.block(¢)).stream().anyMatch(λ -> sequencerComplex(λ, type));
      case IF_STATEMENT:
        final IfStatement $ = (IfStatement) ¢;
        return sequencerComplex($.getThenStatement(), type) || sequencerComplex($.getElseStatement(), type);
      default:
        return sequencer(¢, type);
    }
  }
  /** Checks if expression is simple.
   * @param x an expression
   * @return whether argument is simple */
  static boolean simple(final Expression ¢) {
    return iz.nodeTypeIn(¢, BOOLEAN_LITERAL, CHARACTER_LITERAL, NULL_LITERAL, NUMBER_LITERAL, QUALIFIED_NAME, SIMPLE_NAME, STRING_LITERAL,
        THIS_EXPRESSION, TYPE_LITERAL);
  }
  static boolean simpleLoop(final ASTNode ¢) {
    return iz.loop(¢) && !block(//
        nodeType(¢) == ENHANCED_FOR_STATEMENT ? body(az.enhancedFor(¢)) //
            : nodeType(¢) == FOR_STATEMENT ? body(az.forStatement(¢))//
                : nodeType(¢) == WHILE_STATEMENT ? body(az.whileStatement(¢))//
                    : nodeType(¢) == DO_STATEMENT ? body(az.doStatement(¢))//
                        : null);
  }
  /** Determine whether a node is a simple name
   * @param pattern JD
   * @return whether the parameter is a simple name */
  static boolean simpleName(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, SIMPLE_NAME);
  }
  static boolean singleMemberAnnotation(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, SINGLE_MEMBER_ANNOTATION);
  }
  /** Determine whether a node is a singleton statement, i.e., not a block.
   * @param pattern JD
   * @return whether the parameter is a singleton statement. */
  static boolean singletonStatement(final ASTNode ¢) {
    return extract.statements(¢).size() == 1;
  }
  /** Determine whether the "then" branch of an {@link Statement} is a single
   * statement.
   * @param subject JD
   * @return whether the parameter is a statement */
  static boolean singletonThen(final IfStatement ¢) {
    return ¢ != null && iz.singletonStatement(then(¢));
  }
  static boolean singleVariableDeclaration(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, SINGLE_VARIABLE_DECLARATION);
  }
  /** @param ¢ JD
   * @return whether the given node is a statement or false otherwise */
  static boolean statement(final ASTNode ¢) {
    return ¢ instanceof Statement;
  }
  /** Determine whether a declaration is static or not
   * @param ¢ JD
   * @return whetherdeclaration is static */
  static boolean static¢(final BodyDeclaration ¢) {
    return (Modifier.STATIC & ¢.getModifiers()) != 0;
  }
  /** @param pattern JD
   * @return whether the parameter is a string literal */
  static boolean stringLiteral(final ASTNode ¢) {
    return ¢ != null && ¢.getNodeType() == STRING_LITERAL;
  }
  static boolean stringType(final Type ¢) {
    return ¢ instanceof SimpleType && "String".equals(step.identifier(step.name((SimpleType) ¢)));
  }
  static boolean superConstructorInvocation(final ASTNode ¢) {
    return ¢ != null && iz.nodeTypeEquals(¢, SUPER_CONSTRUCTOR_INVOCATION);
  }
  /** @param ¢ JD
   * @return */
  static boolean superMethodInvocation(final Expression ¢) {
    return ¢ instanceof SuperMethodInvocation;
  }
  /** Determine whether a node is a {@link SwitchCase}
   * @param pattern JD
   * @return whether the parameter is a switch case statement */
  static boolean switchCase(final ASTNode ¢) {
    return ¢ != null && ¢.getNodeType() == SWITCH_CASE;
  }
  static boolean switchStatement(final ASTNode ¢) {
    return ¢ != null && ¢.getNodeType() == SWITCH_STATEMENT;
  }
  static boolean synchronized¢(final BodyDeclaration node) {
    return (node.getModifiers() & Modifier.SYNCHRONIZED) != 0;
  }
  static boolean synchronizedStatement(final ASTNode ¢) {
    return ¢ instanceof SynchronizedStatement;
  }
  /** @param ¢ JD
   * @return */
  static boolean thisExpression(final Expression ¢) {
    return ¢ instanceof ThisExpression;
  }
  /** Determine whether a node is the {@code this} keyword
   * @param pattern JD
   * @return whether is the {@code this} keyword */
  static boolean thisLiteral(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, THIS_EXPRESSION);
  }
  /** Determine whether a node is {@code this} or {@code null
   * @param x JD
   * @return whether the parameter is a block statement */
  static boolean thisOrNull(final Expression ¢) {
    return iz.nodeTypeIn(¢, NULL_LITERAL, THIS_EXPRESSION);
  }
  static boolean tryStatement(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, TRY_STATEMENT);
  }
  /** @param ¢ JDs
   * @return */
  static boolean type(final ASTNode ¢) {
    return ¢ instanceof Type;
  }
  /** @param ¢ JDs
   * @return */
  static boolean typeDeclaration(final ASTNode ¢) {
    return ¢ != null && iz.nodeTypeEquals(¢, TYPE_DECLARATION);
  }
  static boolean typeDeclarationStatement(final Statement ¢) {
    return iz.nodeTypeEquals(¢, TYPE_DECLARATION_STATEMENT);
  }
  /** @param ¢ JDs
   * @return */
  static boolean unionType(final ASTNode ¢) {
    return ¢ != null && iz.nodeTypeEquals(¢, UNION_TYPE);
  }
  static boolean updater(final PrefixExpression ¢) {
    return is.in(¢.getOperator(), op.INCREMENT_PRE, op.DECREMENT_PRE);
  }
  /** @param pattern JD
   * @return whether the node is an Expression Statement of __ Post or Pre
   *         Expression with ++ or -- operator false if node is not an
   *         Expression Statement or its a Post or Pre fix expression that its
   *         operator is not ++ or -- */
  static boolean updating(final ASTNode ¢) {
    if (¢ == null)
      return false;
    switch (¢.getNodeType()) {
      case ASSIGNMENT:
      case EXPRESSION_STATEMENT:
        return true;
      case POSTFIX_EXPRESSION:
        return updating(az.postfixExpression(¢));
      case PREFIX_EXPRESSION:
        return updating(az.prefixExpression(¢));
      default:
        return false;
    }
  }
  static boolean updating(final PostfixExpression ¢) {
    return is.in(¢.getOperator(), PostfixExpression.Operator.INCREMENT, PostfixExpression.Operator.DECREMENT);
  }
  static boolean updating(final PrefixExpression ¢) {
    return is.in(¢.getOperator(), PrefixExpression.Operator.INCREMENT, PrefixExpression.Operator.DECREMENT);
  }
  /** @param ¢ JD
   * @return whether the statement is side effect and updating an initializer
   *         from the for initializers. returns false if the parent is not a for
   *         loop. */
  static boolean usingForInitializer(final Statement ¢) {
    return az.forStatement(¢.getParent()) != null;
  }
  /** Determine whether a given {@link Statement} is an {@link EmptyStatement}
   * or has nothing but empty sideEffects in it.
   * @param subject JD
   * @return whether there are no non-empty sideEffects in the parameter */
  static boolean vacuous(final Statement ¢) {
    return extract.statements(¢).isEmpty();
  }
  /** Determine whether the 'else' part of an {@link IfStatement} is vacuous.
   * @param subject JD
   * @return whether there are no non-empty sideEffects in the 'else' part of
   *         the parameter */
  static boolean vacuousElse(final IfStatement ¢) {
    return vacuous(elze(¢));
  }
  /** Determine whether a statement is an {@link EmptyStatement} or has nothing
   * but empty sideEffects in it.
   * @param subject JD
   * @return whether there are no non-empty sideEffects in the parameter */
  static boolean vacuousThen(final IfStatement ¢) {
    return iz.vacuous(then(¢));
  }
  static boolean validForEvaluation(final InfixExpression ¢) {
    return extract.allOperands(¢).stream().allMatch(iz::pseudoNumber);
  }
  static boolean variableDeclarationExpression(final ASTNode $) {
    return iz.nodeTypeEquals($, VARIABLE_DECLARATION_EXPRESSION);
  }
  static boolean variableDeclarationFragment(final ASTNode $) {
    return iz.nodeTypeEquals($, VARIABLE_DECLARATION_FRAGMENT);
  }
  /** @param ¢ JD
   * @return whether the parameter is a variable declaration statement. */
  static boolean variableDeclarationStatement(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, VARIABLE_DECLARATION_STATEMENT);
  }
  /** @param ¢ JD
   * @return */
  static boolean voidType(final Type ¢) {
    return iz.primitiveType(¢) && az.primitiveType(¢).getPrimitiveTypeCode().equals(PrimitiveType.VOID);
  }
  static boolean whileStatement(final ASTNode x) {
    return iz.nodeTypeEquals(x, WHILE_STATEMENT);
  }
  static boolean wildcardType(final ASTNode ¢) {
    return iz.nodeTypeEquals(¢, WILDCARD_TYPE);
  }
  /** @param ¢ JD
   * @return whether the given node is a literal or false otherwise */
  default boolean parsesTo(final String $, final double d) {
    try {
      return Double.parseDouble($) == d;
    } catch (final IllegalArgumentException ¢) {
      note.bug(this, ¢);
      return false;
    }
  }
  default boolean parsesTo(final String $, final int i) {
    try {
      return iz.parseInt($) == i;
    } catch (final IllegalArgumentException ¢) {
      note.bug(this, ¢);
      return false;
    }
  }
  default boolean parsesTo(final String $, final long l) {
    try {
      return iz.parseLong($) == l;
    } catch (final IllegalArgumentException ¢) {
      note.bug(box.it(l), ¢);
      return false;
    }
  }
  static boolean hexaDecimal(final Expression ¢) {
    return iz.numberLiteral(¢) && az.numberLiteral(¢).getToken().startsWith("0x");
  }
}