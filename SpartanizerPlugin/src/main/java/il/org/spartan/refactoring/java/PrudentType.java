package il.org.spartan.refactoring.java;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.utils.*;

/** TODO: Niv Issue*94
 * <p>
 * Tells how much we know about the type of of a variable, function, or
 * expression. This should be conservative approximation to the real type of the
 * entity, what a rational, but prudent programmer would case about the type
 * <p>
 * Dispatching in this class should emulate the type inference of Java. It is
 * simple to that by hard coding constants.
 * <p>
 * This type should never be <code><b>null</b></code>. Don't bother to check
 * that it is. We want a {@link NullPointerException} thrown if this is the
 * case. or, you may as well write
 *
 * <pre>
 * Kind k = f();
 * assert k != null : //
 * "Implementation of Kind is buggy";
 * </pre>
 *
 * @author Yossi Gil
 * @author Niv Shalmon
 * @since 2016-08-XX */
public enum PrudentType {
  // Those anonymous characters that known little or nothing about themselves
  NOTHING("none", "when nothing can be said, e.g., f(f(),f(f(f()),f()))"), //
  NONNULL("!null", "e.g., new Object() and that's about it"), //
  BAPTIZED("!double&!long&!int", "an object of some type, for which we have a name only"), //
  VOID("void", "nothing at all"),
  // Doubtful types, from four fold uncertainty down to bilalteral
  // schizophrenia" .
  ALPHANUMERIC("String|double|long|int|char", "only in binary plus: f()+g(), not 2 + f(), nor f() + null"), //
  NUMERIC("double|long|int|char", "must be either f()*g(), 2L*f(), 2.*a(), not 2 %a(), nor 2"), //
  INTEGRAL("long|int|char", "must be either int or long: f()%g()^h()<<f()|g()&h(), not 2+(long)f() "), //
  // Certain types
  NULL("null", "when it is certain to be null: null, (null), ((null)), etc. but nothing else"), //
  CHAR("char", "must be char: 'a', (char)97, nothing else"), //
  INT("int", "must be int: 2, 2*(int)f(), 2%(int)f(), 'a'+2 , no 2*f()"), //
  LONG("long", "must be long: 2L, 2*(long)f(), 2%(long)f(), no 2*f()"), //
  DOUBLE("double", "must be double: 2.0, 2.0*a()+g(), no 2%a(), yes 2*f()"), //
  BOOLEAN("boolean", "must be boolean: !f(), f() || g() "), //
  STRING("String", "must be string: \"\"+a, a.toString(), f()+null, not f()+g()"),//
  ;
  /** @param e JD
   * @return The most specific Type information that can be deduced about the
   *         expression, or {@link #NOTHING} if it cannot decide. Will never
   *         return null */
  public static PrudentType prudent(final Expression e) {
    return prudent(e, null, null);
  }

  /** A version of {@link #prudent(Expression)} that receives the operand's type
   * for a single operand expression. The call kind(e,null) is equivalent to
   * kind(e) */
  static PrudentType prudent(final Expression e, final PrudentType t) {
    return prudent(e, t, null);
  }

  /** A version of {@link #prudent(Expression)} that receives the operands' type
   * for a two operand expression. The call kind(e,null,null) is equivalent to
   * kind(e)
   * @param t1 the type of the left hand operand of the expression, or null if
   *        unknown
   * @param t2 the type of the left hand operand of the expression, or null if
   *        unknown */
  static PrudentType prudent(final Expression e, final PrudentType t1, final PrudentType t2) {
    switch (e.getNodeType()) {
      case NULL_LITERAL:
        return NULL;
      case CHARACTER_LITERAL:
        return CHAR;
      case STRING_LITERAL:
        return STRING;
      case BOOLEAN_LITERAL:
        return BOOLEAN;
      case NUMBER_LITERAL:
        return prudentType((NumberLiteral) e);
      case CAST_EXPRESSION:
        return prudentType((CastExpression) e);
      case PREFIX_EXPRESSION:
        return prudentType((PrefixExpression) e, t1);
      case INFIX_EXPRESSION:
        return prudentType((InfixExpression) e, t1, t2);
      case POSTFIX_EXPRESSION:
        return prudentType((PostfixExpression) e, t1);
      case PARENTHESIZED_EXPRESSION:
        return prudentType((ParenthesizedExpression) e, t1);
      case CLASS_INSTANCE_CREATION:
        return prudentType((ClassInstanceCreation) e);
      default:
        return NOTHING;
    }
  }

  private static PrudentType prudentType(final NumberLiteral e) {
    final String ¢ = e.getToken();
    if (¢.matches("[0-9]+"))
      return INT;
    if (¢.matches("[0-9]+\\.[0-9]?"))
      return DOUBLE;
    if (¢.matches("[0-9]+L"))
      return LONG;
    return NUMERIC;
  }

  private static PrudentType prudentType(final CastExpression e) {
    return typeSwitch("" + extract.type(e), BAPTIZED);
  }

  private static PrudentType prudentType(final PrefixExpression e, final PrudentType t1) {
    final PrefixExpression.Operator o = e.getOperator();
    final PrudentType ¢ = t1 != null ? t1 : prudent(e.getOperand());
    return ¢.under(o);
  }

  private static PrudentType prudentType(final InfixExpression e, final PrudentType t1, final PrudentType t2) {
    final InfixExpression.Operator o = e.getOperator();
    final PrudentType ¢1 = t1 != null ? t1 : prudent(e.getLeftOperand());
    final PrudentType ¢2 = t2 != null ? t2 : prudent(e.getRightOperand());
    return ¢1.underBinaryOperator(o, ¢2);
  }

  private static PrudentType prudentType(final PostfixExpression e, final PrudentType t1) {
    final PrudentType ¢ = t1 != null ? t1 : prudent(e.getOperand());
    return ¢.asNumeric();
  }

  private static PrudentType prudentType(final ParenthesizedExpression e, final PrudentType t) {
    return t != null ? t : prudent(e.getExpression());
  }

  private static PrudentType prudentType(final ClassInstanceCreation e) {
    return typeSwitch("" + e.getType(), NONNULL);
  }

  private static PrudentType typeSwitch(final String s, final PrudentType $) {
    switch (s) {
      case "char":
      case "Character":
        return CHAR;
      case "int":
      case "Integer":
        return INT;
      case "double":
      case "Double":
        return DOUBLE;
      case "long":
      case "Long":
        return LONG;
      case "boolean":
      case "Boolean":
        return BOOLEAN;
      case "String":
        return STRING;
      default:
        return $;
    }
  }

  final String description;
  final String name;

  PrudentType(final String name, final String description) {
    this.name = name;
    this.description = description;
  }

  public final String fullName() {
    return this + "=" + name + " (" + description + ")";
  }

  /** @return one of {@link #BOOLEAN}, {@link #INT}, {@link #LONG},
   *         {@link #DOUBLE}, {@link #INTEGRAL} or {@link #NUMERIC}, in case it
   *         cannot decide */
  private final PrudentType under(final PrefixExpression.Operator o) {
    assert o != null;
    return o == NOT ? BOOLEAN //
        : o != COMPLEMENT ? asNumeric() : asIntegralNonChar();
  }

  /** @return one of {@link #BOOLEAN}, {@link #INT}, {@link #LONG},
   *         {@link #DOUBLE}, {@link #STRING}, {@link #INTEGRAL},
   *         {@link #NUMERIC}, or {@link #ALPHANUMERIC}, in case it cannot
   *         decide */
  private final PrudentType underBinaryOperator(final InfixExpression.Operator o, final PrudentType k) {
    if (o == wizard.PLUS2)
      return underPlus(k);
    if (in(o, //
        LESS, //
        GREATER, //
        LESS_EQUALS, //
        GREATER_EQUALS, //
        EQUALS, //
        NOT_EQUALS, //
        CONDITIONAL_OR, //
        CONDITIONAL_AND//
    ))
      return BOOLEAN;
    if (in(o, REMAINDER, XOR, OR, AND))
      return underIntegersOnlyOperator(k);
    if (in(o, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED))
      // shift is unique in that the left hand operand's type doesn't affect the
      // result's type
      return asIntegralNonChar();
    if (!in(o, TIMES, DIVIDE, wizard.MINUS2))
      throw new IllegalArgumentException("o=" + o + " k=" + k.fullName() + "this=" + this);
    return underNumericOnlyOperator(k);
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #DOUBLE},
   *         {@link #STRING}, {@link #INTEGRAL}, {@link #NUMERIC} or
   *         {@link #ALPHANUMERIC}, in case it cannot decide */
  private PrudentType underPlus(final PrudentType k) {
    // addition with NULL or String must be a String
    if (in(STRING, this, k) || in(NULL, this, k))
      return STRING;
    // not String, null or numeric, so we can't determine anything
    if (!isNumeric() && !k.isNumeric())
      return ALPHANUMERIC;
    return underNumericOnlyOperator(k);
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #INTEGRAL},
   *         {@link #DOUBLE}, or {@link #NUMERIC}, in case it cannot decide */
  private PrudentType underNumericOnlyOperator(final PrudentType k) {
    if (!isNumeric())
      return asNumeric().underNumericOnlyOperator(k);
    assert k != null;
    assert this != ALPHANUMERIC : "Don't confuse " + NUMERIC + " with " + ALPHANUMERIC;
    assert isNumeric() : this + ": is for some reason not numeric ";
    final PrudentType $ = k.asNumeric();
    assert $ != null;
    assert $.isNumeric() : this + ": is for some reason not numeric ";
    // Double contaminates Numeric
    if (in(DOUBLE, $, this))
      return DOUBLE;
    // Numeric contaminates INTEGRAL
    if (in(NUMERIC, $, this))
      return NUMERIC;
    // LONG contaminates INTEGRAL
    if (in(LONG, $, this))
      return LONG;
    // INTEGRAL contaminates INT
    if (in(INTEGRAL, $, this))
      return INTEGRAL;
    // plus contaminates CHAR
    return INT;
  }

  private PrudentType underIntegersOnlyOperator(final PrudentType k) {
    final PrudentType ¢1 = asIntegralNonChar();
    final PrudentType ¢2 = k.asIntegralNonChar();
    return ¢1 == INTEGRAL && ¢2 == INTEGRAL ? INTEGRAL : ¢1.max(¢2);
  }

  /** @return true if one of {@link #INT}, {@link #LONG}, {@link #CHAR},
   *         {@link #INTEGRAL} or false otherwise */
  public boolean isIntegral() {
    return in(this, LONG, INT, CHAR, INTEGRAL);
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #CHAR}, or
   *         {@link #INTEGRAL}, in case it cannot decide */
  private PrudentType asIntegral() {
    return isIntegral() ? this : INTEGRAL;
  }

  /** @return one of {@link #INT}, {@link #LONG}, or {@link #INTEGRAL}, in case
   *         it cannot decide */
  private PrudentType asIntegralNonChar() {
    return in(this, CHAR, INT) ? INT : asIntegral();
  }

  /** @return true if one of @link #INT}, {@link #LONG}, {@link #CHAR},
   *         {@link #DOUBLE}, {@link #INTEGRAL}, {@link #NUMERIC} or false
   *         otherwise */
  public boolean isNumeric() {
    return in(this, INT, LONG, CHAR, DOUBLE, INTEGRAL, NUMERIC);
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #CHAR},
   *         {@link #DOUBLE}, {@link #INTEGRAL} or {@link #NUMERIC}, in case no
   *         further information is available */
  private PrudentType asNumeric() {
    return !isNumeric() ? NUMERIC : this != CHAR ? this : INT;
  }

  /** @return true if one of {@link #INT}, {@link #LONG}, {@link #CHAR},
   *         {@link #DOUBLE}, {@link #INTEGRAL} or {@link #NUMERIC},
   *         {@link #STRING}, {@link #ALPHANUMERIC} or false otherwise */
  public boolean isAlphaNumeric() {
    return in(this, INT, LONG, CHAR, DOUBLE, INTEGRAL, NUMERIC, STRING, ALPHANUMERIC);
  }

  private PrudentType max(final PrudentType ¢) {
    return ordinal() > ¢.ordinal() ? this : ¢;
  }
}