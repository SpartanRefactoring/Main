package il.org.spartan.refactoring.java;

import static il.org.spartan.Utils.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;

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
  NULL("null", "when it is certain to be null: null, (null), ((null)), etc. but nothing else"), BYTE("byte", "must be byte: (byte)1, nothing else"), //
  SHORT("short", "must be short: (short)15, nothing else"), //
  CHAR("char", "must be char: 'a', (char)97, nothing else"), //
  INT("int", "must be int: 2, 2*(int)f(), 2%(int)f(), 'a'+2 , no 2*f()"), //
  LONG("long", "must be long: 2L, 2*(long)f(), 2%(long)f(), no 2*f()"), //
  FLOAT("float", "must be float: 2f, 2.3f+1, 2F+f()"), //
  DOUBLE("double", "must be double: 2.0, 2.0*a()+g(), no 2%a(), no 2*f()"), //
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
   * @param t1 the type of the left hand operand of the expression, the type of
   *        the then expression of the conditional, or null if unknown
   * @param t2 the type of the left hand operand of the expression, the type of
   *        the else expression of the conditional, or null if unknown */
  static PrudentType prudent(final Expression e, final PrudentType t1, final PrudentType t2) {
    final List<PrudentType> ¢ = new ArrayList<>();
    ¢.add(t1);
    ¢.add(t2);
    return prudent(e, ¢);
  }

  /** A version of {@link #prudent(Expression)} that receives the a list of the
   * operands' type for all operands of an expression. To be used for
   * InfixExpression that has extended operand. The order of the type's should
   * much the order of the operands returned by extract.allOperands(), and for
   * any operand whose type is unknown, there should be a null. The list won't
   * be used if the size of the list doesn't match that of
   * extract.allOperands().
   * @param ts list of types of operands. Must be at least of size 2 */
  static PrudentType prudent(final Expression e, final List<PrudentType> ts) {
    assert ts.size() >= 2;
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
        return prudentType((PrefixExpression) e, lisp.first(ts));
      case INFIX_EXPRESSION:
        return prudentType((InfixExpression) e, ts);
      case POSTFIX_EXPRESSION:
        return prudentType((PostfixExpression) e, lisp.first(ts));
      case PARENTHESIZED_EXPRESSION:
        return prudentType((ParenthesizedExpression) e, lisp.first(ts));
      case CLASS_INSTANCE_CREATION:
        return prudentType((ClassInstanceCreation) e);
      case METHOD_INVOCATION:
        return prudentType((MethodInvocation) e);
      case CONDITIONAL_EXPRESSION:
        return prudentType((ConditionalExpression) e, lisp.first(ts), lisp.second(ts));
      default:
        return NOTHING;
    }
  }

  private static PrudentType prudentType(final MethodInvocation e) {
    return "toString".equals(e.getName() + "") ? STRING : NOTHING;
  }

  private static PrudentType prudentType(final NumberLiteral e) {
    final String ¢ = e.getToken();
    if (¢.matches("[0-9]+"))
      return INT;
    if (¢.matches("[0-9]+[l,L]"))
      return LONG;
    if (¢.matches("[0-9]+\\.[0-9]*[f,F]") || ¢.matches("[0-9]+[f,F]"))
      return FLOAT;
    if (¢.matches("[0-9]+\\.[0-9]*[d,D]?") || ¢.matches("[0-9]+[d,D]"))
      return DOUBLE;
    return NUMERIC;
  }

  private static PrudentType prudentType(final CastExpression e) {
    return typeSwitch("" + step.type(e), BAPTIZED);
  }

  private static PrudentType prudentType(final PrefixExpression e, final PrudentType t1) {
    final PrefixExpression.Operator o = e.getOperator();
    final PrudentType ¢ = t1 != null ? t1 : prudent(e.getOperand());
    return ¢.under(o);
  }

  private static PrudentType prudentType(final InfixExpression e, final List<PrudentType> ts) {
    final InfixExpression.Operator o = e.getOperator();
    final List<Expression> es = extract.allOperands(e);
    assert es.size() >= 2;
    final List<PrudentType> ¢ = new ArrayList<>();
    if (ts.size() == es.size())
      for (int i = 0; i < ts.size(); ++i)
        ¢.add(i, ts.get(i) != null ? ts.get(i) : prudent(es.get(i)));
    else
      for (int i = 0; i < es.size(); ++i)
        ¢.add(i, prudent(es.get(i)));
    PrudentType $ = lisp.first(¢).underBinaryOperator(o, lisp.second(¢));
    lisp.chop(lisp.chop(¢));
    while (!¢.isEmpty()) {
      $ = $.underBinaryOperator(o, lisp.first(¢));
      lisp.chop(¢);
    }
    return $;
  }

  private static PrudentType prudentType(final PostfixExpression e, final PrudentType t1) {
    final PrudentType ¢ = t1 != null ? t1 : prudent(e.getOperand());
    return ¢.asNumeric(); //see testInDecreamentSemantics
  }

  private static PrudentType prudentType(final ParenthesizedExpression e, final PrudentType t) {
    return t != null ? t : prudent(extract.core(e));
  }

  private static PrudentType prudentType(final ClassInstanceCreation e) {
    return typeSwitch("" + e.getType(), NONNULL);
  }

  private static PrudentType prudentType(final ConditionalExpression e, final PrudentType t1, final PrudentType t2) {
    final PrudentType ¢1 = t1 != null ? t1 : prudent(e.getThenExpression());
    final PrudentType ¢2 = t2 != null ? t2 : prudent(e.getElseExpression());
    if (¢1 == ¢2)
      return ¢1;
    // If we don't know much about one operand but do know enough about the
    // other, we can still learn something
    if (¢1.isNoInfo() || ¢2.isNoInfo())
      return conditionalWithNothing(¢1.isNoInfo() ? ¢2 : ¢1);
    if (¢1.isIntegral() && ¢2.isIntegral())
      return ¢1.underIntegersOnlyOperator(¢2);
    if (¢1.isNumeric() && ¢2.isNumeric())
      return ¢1.underNumericOnlyOperator(¢2);
    return NOTHING;
  }

  private static PrudentType typeSwitch(final String s, final PrudentType $) {
    switch (s) {
      case "byte":
      case "Byte":
        return BYTE;
      case "short":
      case "Short":
        return SHORT;
      case "char":
      case "Character":
        return CHAR;
      case "int":
      case "Integer":
        return INT;
      case "long":
      case "Long":
        return LONG;
      case "float":
      case "Float":
        return FLOAT;
      case "double":
      case "Double":
        return DOUBLE;
      case "boolean":
      case "Boolean":
        return BOOLEAN;
      case "String":
        return STRING;
      default:
        return $;
    }
  }

  private static PrudentType conditionalWithNothing(final PrudentType t) {
    switch (t) {
      case BYTE:
      case SHORT:
      case CHAR:
      case INT:
      case INTEGRAL:
      case LONG:
      case FLOAT:
      case NUMERIC:
        return NUMERIC;
      case DOUBLE:
        return DOUBLE;
      case STRING:
        return STRING;
      case BOOLEAN:
        return BOOLEAN;
      default:
        return NOTHING;
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
        : in(o, DECREMENT, INCREMENT) ? asNumeric() //see testInDecreamentSemantics and testOnaryPlusMinusSemantics
        :  o != COMPLEMENT ? asNumericUnderOperation() : asIntegralUnderOperation();
  }

  /** @return one of {@link #BOOLEAN}, {@link #INT}, {@link #LONG},
   *         {@link #DOUBLE}, {@link #STRING}, {@link #INTEGRAL},
   *         {@link #NUMERIC}, or {@link #ALPHANUMERIC}, in case it cannot
   *         decide */
  private final PrudentType underBinaryOperator(final InfixExpression.Operator o, final PrudentType k) {
    if (o == wizard.PLUS2)
      return underPlus(k);
    // TODO: create a function in {@link wizard}
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
    // XOR, OR and AND support BOOLEAN if both operands are BOOLEAN
    // TODO: Niv, again, implement in a wizard.
    if (in(o, XOR, OR, AND) && this == BOOLEAN && k == BOOLEAN)
      return BOOLEAN;
    if (in(o, REMAINDER, XOR, OR, AND))
      return underIntegersOnlyOperator(k);
    if (in(o, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED))
      // shift is unique in that the left hand operand's type doesn't affect the
      // result's type
      return asIntegralUnderOperation();
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
      return asNumericUnderOperation().underNumericOnlyOperator(k);
    assert k != null;
    assert this != ALPHANUMERIC : "Don't confuse " + NUMERIC + " with " + ALPHANUMERIC;
    assert isNumeric() : this + ": is for some reason not numeric ";
    final PrudentType $ = k.asNumericUnderOperation();
    assert $ != null;
    assert $.isNumeric() : this + ": is for some reason not numeric ";
    // Double contaminates Numeric
    if (in(DOUBLE, $, this))
      return DOUBLE;
    // Numeric contaminates Float
    if (in(NUMERIC, $, this))
      return NUMERIC;
    // FLOAT contaminates Integral
    if (in(FLOAT, $, this))
      return FLOAT;
    // LONG contaminates INTEGRAL
    if (in(LONG, $, this))
      return LONG;
    // INTEGRAL contaminates INT
    if (in(INTEGRAL, $, this))
      return INTEGRAL;
    // Everything else is INT after an operation
    return INT;
  }

  private PrudentType underIntegersOnlyOperator(final PrudentType k) {
    final PrudentType ¢1 = asIntegralUnderOperation();
    final PrudentType ¢2 = k.asIntegralUnderOperation();
    return in(LONG, ¢1, ¢2) ? LONG : in(INTEGRAL, ¢1, ¢2) ? INTEGRAL : INT;
  }

  /** @return true if one of {@link #INT}, {@link #LONG}, {@link #CHAR},
   *         {@link BYTE}, {@link SHORT}, {@link #INTEGRAL} or false
   *         otherwise */
  public boolean isIntegral() {
    return in(this, LONG, INT, CHAR, BYTE, SHORT, INTEGRAL);
  }

  /** used to determine whether an integral type behaves as itself under
   * operations or as an INT.
   * @return true if one of {@link #CHAR}, {@link BYTE}, {@link SHORT} or false
   *         otherwise. */
  private boolean isIntUnderOperation() {
    return in(this, CHAR, BYTE, SHORT);
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #CHAR}, {@link BYTE},
   *         {@link SHORT} or {@link #INTEGRAL}, in case it cannot decide */
  private PrudentType asIntegral() {
    return isIntegral() ? this : INTEGRAL;
  }

  /** @return one of {@link #INT}, {@link #LONG}, or {@link #INTEGRAL}, in case
   *         it cannot decide */
  private PrudentType asIntegralUnderOperation() {
    return isIntUnderOperation() ? INT : asIntegral();
  }

  /** @return true if one of {@link #INT}, {@link #LONG}, {@link #CHAR},
   *         {@link BYTE}, {@link SHORT}, {@link FLOAT}, {@link #DOUBLE},
   *         {@link #INTEGRAL}, {@link #NUMERIC} or false otherwise */
  public boolean isNumeric() {
    return in(this, INT, LONG, CHAR, BYTE, SHORT, FLOAT, DOUBLE, INTEGRAL, NUMERIC);
  }
  
  /** @return one of {@link #INT}, {@link #LONG},, {@link #CHAR},
   *         {@link BYTE}, {@link SHORT}, {@link FLOAT}, {@link #DOUBLE},
   *         {@link #INTEGRAL} or {@link #NUMERIC}, in case no
   *         further information is available */
  private PrudentType asNumeric() {
    return !isNumeric() ? NUMERIC : this;
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #FLOAT},
   *         {@link #DOUBLE}, {@link #INTEGRAL} or {@link #NUMERIC}, in case no
   *         further information is available */
  private PrudentType asNumericUnderOperation() {
    return !isNumeric() ? NUMERIC : isIntUnderOperation() ? INT : this;
  }

  /** @return true if one of {@link #INT}, {@link #LONG}, {@link #CHAR},
   *         {@link BYTE}, {@link SHORT}, {@link FLOAT}, {@link #DOUBLE},
   *         {@link #INTEGRAL} or {@link #NUMERIC}, {@link #STRING},
   *         {@link #ALPHANUMERIC} or false otherwise */
  public boolean isAlphaNumeric() {
    return in(this, INT, LONG, CHAR, BYTE, SHORT, FLOAT, DOUBLE, INTEGRAL, NUMERIC, STRING, ALPHANUMERIC);
  }

  /** @return true if one of {@link #NOTHING}, {@link #BAPTIZED},
   *         {@link #NONNULL}, {@link #VOID}, {@link #NULL} or false
   *         otherwise */
  private boolean isNoInfo() {
    return in(this, NOTHING, BAPTIZED, NONNULL, VOID, NULL);
  }

  // from here on is the axiom method used for testing of PrudentType. see issue
  // #105 for more details
  @SuppressWarnings("unused") static PrudentType axiom(final byte x) {
    return BYTE;
  }

  @SuppressWarnings("unused") static PrudentType axiom(final char x) {
    return CHAR;
  }

  @SuppressWarnings("unused") static PrudentType axiom(final short x) {
    return SHORT;
  }

  @SuppressWarnings("unused") static PrudentType axiom(final int x) {
    return INT;
  }

  @SuppressWarnings("unused") static PrudentType axiom(final long x) {
    return LONG;
  }

  @SuppressWarnings("unused") static PrudentType axiom(final float x) {
    return FLOAT;
  }

  @SuppressWarnings("unused") static PrudentType axiom(final double x) {
    return DOUBLE;
  }

  @SuppressWarnings("unused") static PrudentType axiom(final boolean x) {
    return BOOLEAN;
  }

  @SuppressWarnings("unused") static PrudentType axiom(final String x) {
    return STRING;
  }

  @SuppressWarnings("unused") static PrudentType axiom(final Object o) {
    return NOTHING;
  }
}