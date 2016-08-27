package il.org.spartan.refactoring.utils;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.utils.Funcs.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import javax.management.*;

import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;

import org.eclipse.jdt.core.dom.*;
import org.omg.CORBA.*;

/** TODO: Niv Issue*94
 * 
 * <p>Tells how much we know about the type of of a variable, function, or
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
 * @since 2016-08-XX */
enum Kind {
  // Those anonymous characters that known little or nothing about themselves
  NOTHING("none", "when nothing can be said, e.g., f(f(),f(f(f()),f()))"), //
  NONNULL("!null", "e.g., new Object() and that's about it"), //
  BAPTIZED("!double&!long&!int", "an object of some type, for which we have a name only"), //
  VOID("void", "nothing at all"),
  // Doubtful types, from four fold uncertainety down to bilalteral
  // schinography" .
  ALPHANUMERIC("String|double|long|int|", "only in binary plus: f()+g(), not 2 + f(), nor f() + null"), //
  NUMERIC("double|long|int", "must be either f()*g(), 2L*f(), 2.*a(), not 2 %a(), nor 2"), //
  INTEGRAL("long|int", "must be either int or long: f()%g()^h()<<f()|g()&h(), not 2+(long)f() "), //
  // Certain types
  NULL("null", "when it is certain to be null: null, (null), ((null)), etc. but nothing else"), //
  INT("int", "must to be int: 2, 2*(int)f(), 2%(int)f(), no 2*f()"), //
  LONG("long", "must be long: 2L, 2*(long)f(), 2%(long)f(), no 2*f()"), //
  DOUBLE("double", "must be double: 2.0, 2.0*a()+g(), no 2%a(), yes 2*f()"), //
  BOOLEAN("boolean", "must be boolean: !f(), f() || g() "), //
  STRING("String", "must be string: \"\"+a, a.toString(), f()+null, not f()+g()"),//
  ;
  final String description;
  final String name;

  Kind(final String name, final String description) {
    this.name = name;
    this.description = description;
  }

  public final String fullName() {
    return this + "=" + name + " (" + description + ")";
  }

  public final Kind under(final PrefixExpression.Operator o) {
    assert o != null;
    assert in(o, BOOLEAN, MINUS1, PLUS1, COMPLEMENT, DECREMENT_POST, DECREMENT_PRE);
    return o == NOT ? BOOLEAN //
        : in(o, MINUS1, PLUS1) ? asNumeric() //
            : underIntegersOnlyOperator();
  }

  /** @return one of {@link #INT}, {@link #LONG}, or {@link #INTEGRAL}, in case
   *         it cannot decide */
  public Kind underIntegersOnlyOperator() {
    return in(this, LONG, INT) ? this : INTEGRAL;
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #DOUBLE},
   *         {@link #INTEGRAL} or {@link #NUMERIC}, in case no further
   *         information is available */
  public Kind asNumeric() {
    return in(this, DOUBLE, LONG, INT, INTEGRAL) ? this : NUMERIC;
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #DOUBLE},
   *         {@link #STRING} or {@link #ALPHANUMERIC}, in case it cannot
   *         decide */
  public Kind underPlus(Kind k) {
    return k == STRING && this == STRING ? STRING : ALPHANUMERIC;
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #DOUBLE}, or
   *         {@link #NUMERIC}, in case it cannot decide */
  private Kind asNumeric(Kind k) {
    if (k == this)
      return k;
    if (!isNumeric())
      return asNumeric().asNumeric(k);
    assert (k != null);
    assert this != ALPHANUMERIC : "Don't confuse " + NUMERIC + " with " + ALPHANUMERIC;
    assert in(this, INT, DOUBLE, LONG, INTEGRAL, NUMERIC) : this + ": does not fit our list of numeric types";
    assert isNumeric() : this + ": is for some reason not numeric ";
    final Kind $ = k.asNumeric();
    assert $ != null;
    assert $.isNumeric() : this + ": is for some reason not numeric ";
    assert in($, INT, DOUBLE, LONG, INTEGRAL, NUMERIC) : $ + ": does not fit our list of numeric types";
    if ($ == this)
      return $;
    if (in(DOUBLE, $, this))
      return DOUBLE;
    assert in($, INT, LONG, INTEGRAL, NUMERIC) : $ + ": does not fit our narrowed list";
    assert in(this, INT, LONG, INTEGRAL, NUMERIC) : this + ": does not fit our narrowed list";
    if (in(NUMERIC, $, this))
      return NUMERIC;
    assert in($, INT, LONG, INTEGRAL) : $ + ": does not fit our narrowed list";
    assert in(this, INT, LONG, INTEGRAL) : this + ": does not fit our narrowed list";
    // Numeric contaminates INTEGRAL
    if (in(INTEGRAL, $, this))
      return INTEGRAL;
    assert in($, INT, LONG) : $ + ": does not fit our narrowed list";
    assert in(this, INT, LONG) : this + ": does not fit our narrowed list";
    // LONG contaminates INTEGRAL
    if (in(LONG, $, this))
      return LONG;
    assert in($, INT) : $ + ": does not fit our narrowed list";
    assert in(this, INT) : this + ": does not fit our narrowed list";
    return INT;
  }

  private boolean isNumeric() {
    return in(this, INT, LONG, DOUBLE, INTEGRAL, NUMERIC);
  }

  private Kind underIntegersOnlyOperator(Kind k) {
    final Kind ¢ = k.underIntegersOnlyOperator();
    return !in(¢, this, INTEGRAL) ? max(¢) : ¢ != Kind.ALPHANUMERIC ? ¢ : INTEGRAL;
  }

  private Kind max(Kind ¢) {
    return ¢.ordinal() > ordinal() ? ¢ : this;
  }

  public final Kind underBinaryOperator(InfixExpression.Operator o, Kind k) {
    if (o == PLUS2)
      return underPlus(k);
    if (in(this, //
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
    if (!in(this, TIMES, DIVIDE, MINUS2))
      throw new IllegalArgumentException("o=" + o + " k=" + k.fullName() + "this=" + this);
    return asNumeric(k);
  }

  public static boolean kind(Expression e) {
    throw new RuntimeException("Team3 needs to implement this: " + e);
  }
}