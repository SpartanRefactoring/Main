package il.org.spartan.refactoring.utils;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;

/** TODO: Niv Issue*94
 * <p>
 * Tells how much we know about the type of of a variable, function, or
 * expression.
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
enum Type {
  // Those anonymous characters that known little or nothing about themselves
  NOTHING("none", "when nothing can be said, e.g., f(f(),f(f(f()),f()))"), //
  NONNULL("!null", "e.g., new Object() and that's about it"), //
  BAPTIZED("!double&!long&!int", "an object of some type, for which we have a name only"), //
  VOID("void", "nothing at all"),
  // Doubtful types, from four fold uncertainty down to bilalteral
  // schizophrenia" .
  ALPHANUMERIC("String|double|long|int|", "only in binary plus: f()+g(), not 2 + f(), nor f() + null"), //
  NUMERIC("double|long|int", "must be either f()*g(), 2L*f(), 2.*a(), not 2 %a(), nor 2"), //
  INTEGRAL("long|int", "must be either int or long: f()%g()^h()<<f()|g()&h(), not 2+(long)f() "), //
  // Certain types
  NULL("null", "when it is certain to be null: null, (null), ((null)), etc. but nothing else"), //
  CHAR("char", "must be char: 'a', (char)97, pretty much nothing else"), //
  INT("int", "must be int: 2, 2*(int)f(), 2%(int)f(), no 2*f()"), //
  LONG("long", "must be long: 2L, 2*(long)f(), 2%(long)f(), no 2*f()"), //
  DOUBLE("double", "must be double: 2.0, 2.0*a()+g(), no 2%a(), yes 2*f()"), //
  BOOLEAN("boolean", "must be boolean: !f(), f() || g() "), //
  STRING("String", "must be string: \"\"+a, a.toString(), f()+null, not f()+g()"),//
  ;
  
  /**@param e JD
   * @return The most specific Type information that can be deduced about the expression,
   * will never return null */
  public static Type kind(Expression e) {
    return kind(e,null,null);
  }
  
  static Type kind(Expression e, final Type t){
    return kind(e,t,null);
  }
  
  static Type kind(Expression e, final Type t1, final Type t2){
    throw new RuntimeException("Not implemented yet");
   /* if (e instanceof NullLiteral){
      return NULL;
    }
    if (e instanceof CharacterLiteral)
      return CHAR;
    if (e instanceof NumberLiteral){
      return kind((NumberLiteral)e);
    }
    if (e instanceof CastExpression){
      return kind((CastExpression)e);
    }
    if (e instanceof PrefixExpression){
      return kind((PrefixExpression)e, t1);
    }
    if (e instanceof InfixExpression){
      return kind((InfixExpression)e, t1, t2);
    }
    return NOTHING;*/
  }
  
  private static Type kind(NumberLiteral e){
    return NUMERIC;
  }
  
  private static Type kind(CastExpression e){
    switch ("" + extract.type(e)){
      case "char":
      case "Character":
        return CHAR;
      case "int" :
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
        return BAPTIZED;
    }
  }
  
  private static Type kind(PrefixExpression e, final Type t1){
    PrefixExpression.Operator o = e.getOperator();
    Type ¢ = t1 != null ? t1 : kind(e.getOperand());
    return ¢.under(o);
  }
  
  private static Type kind(InfixExpression e, final Type t1, final Type t2){
    InfixExpression.Operator o = e.getOperator();
    Type ¢1 = t1 != null ? t1 : kind(e.getLeftOperand());
    Type ¢2 = t2 != null ? t2 : kind(e.getRightOperand());
    return ¢1.underBinaryOperator(o, ¢2);
  }
  
  private static Type max(Type ¢1, Type ¢2) {
    return ¢1.ordinal() > ¢2.ordinal() ? ¢1 : ¢2;
  }

  final String description;

  final String name;

  Type(final String name, final String description) {
    this.name = name;
    this.description = description;
  }
  public final String fullName() {
    return this + "=" + name + " (" + description + ")";
  }
  /** @return one of {@link #BOOLEAN}, {@link #INT}, {@link #LONG},
   *         {@link #DOUBLE}, {@link #INTEGRAL} or {@link #NUMERIC}, in case it
   *         cannot decide */
  // TODO: should be private once kind is finished
  final Type under(final PrefixExpression.Operator o) {
    assert o != null;
    return o == NOT ? BOOLEAN //
        : o != COMPLEMENT ? asNumeric() : asIntegral();
  }
  /** @return one of {@link #BOOLEAN}, {@link #INT}, {@link #LONG},
   *         {@link #DOUBLE}, {@link #STRING}, {@link #INTEGRAL},
   *         {@link #NUMERIC}, or {@link #ALPHANUMERIC}, in case it cannot
   *         decide */
  // TODO: should be private once kind is finished
  final Type underBinaryOperator(InfixExpression.Operator o, Type k) {
    if (o == PLUS2)
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
    if (in(o, REMAINDER, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED, XOR, OR, AND))
      return underIntegersOnlyOperator(k);
    if (!in(o, TIMES, DIVIDE, MINUS2))
      throw new IllegalArgumentException("o=" + o + " k=" + k.fullName() + "this=" + this);
    return underNumericOnlyOperator(k);
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #DOUBLE},
   *         {@link #STRING}, {@link #INTEGRAL}, {@link #NUMERIC} or
   *         {@link #ALPHANUMERIC}, in case it cannot decide */
  private Type underPlus(Type k) {
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
  private Type underNumericOnlyOperator(Type k) {
    if (k == this)
      return k;
    if (!isNumeric())
      return asNumeric().underNumericOnlyOperator(k);
    assert (k != null);
    assert this != ALPHANUMERIC : "Don't confuse " + NUMERIC + " with " + ALPHANUMERIC;
    assert isNumeric() : this + ": is for some reason not numeric ";
    final Type $ = k.asNumeric();
    assert $ != null;
    assert $.isNumeric() : this + ": is for some reason not numeric ";
    if ($ == this)
      return $;
    // Double contaminates Numeric
    if (in(DOUBLE, $, this))
      return DOUBLE;
    // Numeric contaminates INTEGRAL
    if (in(NUMERIC, $, this))
      return NUMERIC;
    // LONG contaminates INTEGRAL
    if (in(LONG, $, this))
      return LONG;
    //INTEGRAL contaminates INT
    if (in(INTEGRAL, $, this))
      return INTEGRAL;
    //CHAR contaminates INT
    if (in(INT, $, this)){
      return INT;
    }
    return CHAR;
  }
  private Type underIntegersOnlyOperator(Type k) {
    return max(asIntegral(), k.asIntegral());
  }

  private boolean isIntegral() {
    return in(this, LONG, INT, CHAR, INTEGRAL);
  }
  /** @return one of {@link #INT}, {@link #LONG}, {@link #CHAR}, or {@link #INTEGRAL}, in case
   *         it cannot decide */
  private Type asIntegral() {
    return isIntegral() ? this : INTEGRAL;
  }
  

  private boolean isNumeric() {
    return in(this, INT, LONG, CHAR, DOUBLE, INTEGRAL, NUMERIC);
  }
  /** @return one of {@link #INT}, {@link #LONG}, {@link #CHAR}, {@link #DOUBLE},
   *         {@link #INTEGRAL} or {@link #NUMERIC}, in case no further
   *         information is available */
  private Type asNumeric() {
    return isNumeric() ? this : NUMERIC;
  }
}