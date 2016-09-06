package il.org.spartan.refactoring.engine;

import static il.org.spartan.Utils.*;
import static il.org.spartan.refactoring.engine.type.Odd.Types.*;
import static il.org.spartan.refactoring.engine.type.Primitive.Certain.*;
import static il.org.spartan.refactoring.engine.type.Primitive.Uncertain.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.utils.*;

/** @author Yossi Gil
 ** @author Dor Maayan
 * @author Niv Shalmon
 * @since 2016 */
interface type {
  /** All type that were ever born */
  static Map<String, type> types = new LinkedHashMap<>();
  static String propertyName = "spartan type";

  /** @param n JD
   * @return the type information stored inside the node n, or null if there is
   *         none */
  static type getType(final ASTNode n) {
    return (type) n.getProperty(propertyName);
  }

  /** @param x JD
   * @return The most specific Type information that can be deduced about the
   *         expression from it's structure, or {@link #NOTHING} if it cannot
   *         decide. Will never return null */
  static type prudent(final Expression x) {
    if (getType(x) != null)
      return getType(x);
    switch (x.getNodeType()) {
      case NULL_LITERAL:
        return NULL;
      case CHARACTER_LITERAL:
        return CHAR;
      case STRING_LITERAL:
        return STRING;
      case BOOLEAN_LITERAL:
        return BOOLEAN;
      case NUMBER_LITERAL:
        return prudent((NumberLiteral) x);
      case CAST_EXPRESSION:
        return prudent((CastExpression) x);
      case PREFIX_EXPRESSION:
        return prudent((PrefixExpression) x);
      case INFIX_EXPRESSION:
        return prudent((InfixExpression) x);
      case POSTFIX_EXPRESSION:
        return prudent((PostfixExpression) x);
      case PARENTHESIZED_EXPRESSION:
        return prudent((ParenthesizedExpression) x);
      case CLASS_INSTANCE_CREATION:
        return prudent((ClassInstanceCreation) x);
      case METHOD_INVOCATION:
        return prudent((MethodInvocation) x);
      case CONDITIONAL_EXPRESSION:
        return prudent((ConditionalExpression) x);
      case ASSIGNMENT:
        return prudent((Assignment) x);
      default:
        return NOTHING;
    }
  }

  static type baptize(final String name) {
    return baptize(name, "anonymously born");
  }

  static type baptize(final String name, final String description) {
    return have(name) ? bring(name) : new type() {
      @Override public String description() {
        return description;
      }

      @Override public String key() {
        return name;
      }
    }.join();
  }

  static type bring(final String name) {
    return types.get(name);
  }

  static type conditionalWithNoInfo(final type t) {
    return in(t, BYTE, SHORT, CHAR, INT, INTEGRAL, LONG, FLOAT, NUMERIC) //
        ? NUMERIC //
        : !in(t, DOUBLE, STRING, BOOLEAN, BOOLEAN) //
            ? NOTHING : t;
  }

  // TODO: Matteo. Nano-pattern of values: not implemented
  // TODO: Dor, please implement yet
  static type get(final Expression ¢) {
    throw new NotImplementedException("code of this function was not implemented yet. Arg=" + ¢);
  }

  static boolean have(final String name) {
    return types.containsKey(name);
  }

  static type prudent(final Assignment x) {
    final type $ = prudent(x.getLeftHandSide());
    return !$.isNoInfo() ? $ : prudent(x.getRightHandSide()).isNumeric() ? NUMERIC : prudent(x.getRightHandSide());
  }

  static type prudent(final CastExpression x) {
    return typeSwitch("" + step.type(x));
  }

  static type prudent(final ClassInstanceCreation c) {
    return typeSwitch("" + c.getType());
  }

  static type prudent(final ConditionalExpression x) {
    final type $ = prudent(x.getThenExpression());
    final type ¢ = prudent(x.getElseExpression());
    // If we don't know much about one operand but do know enough about the
    // other, we can still learn something
    return $ == ¢ ? $
        : $.isNoInfo() || ¢.isNoInfo() ? conditionalWithNoInfo($.isNoInfo() ? ¢ : $) //
            : $.isIntegral() && ¢.isIntegral() ? $.underIntegersOnlyOperator(¢) //
                : $.isNumeric() && ¢.isNumeric() ? $.underNumericOnlyOperator(¢)//
                    : NOTHING; //
  }

  static type prudent(final InfixExpression x) {
    final InfixExpression.Operator o = x.getOperator();
    final List<Expression> es = extract.allOperands(x);
    assert es.size() >= 2;
    type $ = prudent(lisp.first(es)).underBinaryOperator(o, prudent(lisp.second(es)));
    lisp.chop(lisp.chop(es));
    while (!es.isEmpty()) {
      $ = $.underBinaryOperator(o, prudent(lisp.first(es)));
      lisp.chop(es);
    }
    return $;
  }

  static type prudent(final MethodInvocation i) {
    return "toString".equals(i.getName() + "") && i.arguments().isEmpty() ? STRING : NOTHING;
  }

  static type prudent(final NumberLiteral l) {
    // TODO: Dor use TypeLiteral instead. It is thoroughly tested and very
    // accurate.
    final String ¢ = l.getToken();
    return ¢.matches("[0-9]+") ? INT
        : ¢.matches("[0-9]+[l,L]") ? LONG
            : ¢.matches("[0-9]+\\.[0-9]*[f,F]") || ¢.matches("[0-9]+[f,F]") ? FLOAT
                : ¢.matches("[0-9]+\\.[0-9]*[d,D]?") || ¢.matches("[0-9]+[d,D]") ? DOUBLE : NUMERIC;
  }

  static type prudent(final ParenthesizedExpression x) {
    return prudent(extract.core(x));
  }

  static type prudent(final PostfixExpression x) {
    return prudent(x.getOperand()).asNumeric(); // see testInDecreamentSemantics
  }

  static type prudent(final PrefixExpression x) {
    return prudent(x.getOperand()).under(x.getOperator());
  }

  static type typeSwitch(final String s) {
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
        return baptize(s, s); // TODO: what should be the description here?
    }
  }

  /** @return true if one of {@link #INT} , {@link #LONG} , {@link #CHAR} ,
   *         {@link BYTE} , {@link SHORT} , {@link FLOAT} , {@link #DOUBLE} ,
   *         {@link #INTEGRAL} or {@link #NUMERIC} , {@link #STRING} ,
   *         {@link #ALPHANUMERIC} or false otherwise */
  default boolean isAlphaNumeric() {
    return in(this, INT, LONG, CHAR, BYTE, SHORT, FLOAT, DOUBLE, INTEGRAL, NUMERIC, STRING, ALPHANUMERIC);
  }

  /** @return true if one of {@link #INT} , {@link #LONG} , {@link #CHAR} ,
   *         {@link BYTE} , {@link SHORT} , {@link #INTEGRAL} or false
   *         otherwise */
  default boolean isIntegral() {
    return in(this, LONG, INT, CHAR, BYTE, SHORT, INTEGRAL);
  }

  /** @return true if one of {@link #INT} , {@link #LONG} , {@link #CHAR} ,
   *         {@link BYTE} , {@link SHORT} , {@link FLOAT} , {@link #DOUBLE} ,
   *         {@link #INTEGRAL} , {@link #NUMERIC} or false otherwise */
  default boolean isNumeric() {
    return in(this, INT, LONG, CHAR, BYTE, SHORT, FLOAT, DOUBLE, INTEGRAL, NUMERIC);
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #CHAR}, {@link BYTE},
   *         {@link SHORT} or {@link #INTEGRAL}, in case it cannot decide */
  default type asIntegral() {
    return isIntegral() ? this : INTEGRAL;
  }

  /** @return one of {@link #INT}, {@link #LONG}, or {@link #INTEGRAL}, in case
   *         it cannot decide */
  default type asIntegralUnderOperation() {
    return isIntUnderOperation() ? INT : asIntegral();
  }

  /** @return one of {@link #INT}, {@link #LONG},, {@link #CHAR}, {@link BYTE},
   *         {@link SHORT}, {@link FLOAT}, {@link #DOUBLE}, {@link #INTEGRAL} or
   *         {@link #NUMERIC}, in case no further information is available */
  default type asNumeric() {
    return isNumeric() ? this : NUMERIC;
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #FLOAT},
   *         {@link #DOUBLE}, {@link #INTEGRAL} or {@link #NUMERIC}, in case no
   *         further information is available */
  default type asNumericUnderOperation() {
    return !isNumeric() ? NUMERIC : isIntUnderOperation() ? INT : this;
  }

  default Primitive.Certain asPrimitiveCertain() {
    return null;
  }

  default type asPrimitiveUncertain() {
    return null;
  }

  default boolean canB(@SuppressWarnings("unused") final Primitive.Certain __) {
    return false;
  }

  String description();

  default String fullName() {
    return this + "=" + key() + " (" + description() + ")";
  }

  /** used to determine whether an integral type behaves as itself under
   * operations or as an INT.
   * @return true if one of {@link #CHAR}, {@link BYTE}, {@link SHORT} or false
   *         otherwise. */
  default boolean isIntUnderOperation() {
    return in(this, CHAR, BYTE, SHORT);
  }

  /** @return true if one of {@link #NOTHING}, {@link #BAPTIZED},
   *         {@link #NONNULL}, {@link #VOID}, {@link #NULL} or false
   *         otherwise */
  default boolean isNoInfo() {
    return in(this, NOTHING, NULL);
  }

  default type join() {
    assert !types.containsKey(key());
    types.put(key(), this);
    return this;
  }

  /** @return the formal name of this type, the key under which it is stored in
   *         {@link #types}, e.g., "Object", "int", "String", etc. */
  String key();

  /** @return one of {@link #BOOLEAN} , {@link #INT} , {@link #LONG} ,
   *         {@link #DOUBLE} , {@link #INTEGRAL} or {@link #NUMERIC} , in case
   *         it cannot decide */
  default type under(final PrefixExpression.Operator o) {
    assert o != null;
    return o == NOT ? BOOLEAN : in(o, DECREMENT, INCREMENT) ? asNumeric() : o != COMPLEMENT ? asNumericUnderOperation() : asIntegralUnderOperation();
  }

  /** @return one of {@link #BOOLEAN} , {@link #INT} , {@link #LONG} ,
   *         {@link #DOUBLE} , {@link #STRING} , {@link #INTEGRAL} ,
   *         {@link BOOLEANINTEGRAL} {@link #NUMERIC} , or {@link #ALPHANUMERIC}
   *         , in case it cannot decide */
  default type underBinaryOperator(final InfixExpression.Operator o, final type k) {
    if (o == wizard.PLUS2)
      return underPlus(k);
    if (wizard.isComparison(o))
      return BOOLEAN;
    if (wizard.isBitwiseOperator(o))
      return underBitwiseOperation(k);
    if (o == REMAINDER)
      return underIntegersOnlyOperator(k);
    if (in(o, LEFT_SHIFT, RIGHT_SHIFT_SIGNED, RIGHT_SHIFT_UNSIGNED))
      return asIntegralUnderOperation();
    if (!in(o, TIMES, DIVIDE, wizard.MINUS2))
      throw new IllegalArgumentException("o=" + o + " k=" + k.fullName() + "this=" + this);
    return underNumericOnlyOperator(k);
  }

  /** @return one of {@link #BOOLEAN}, {@link #INT}, {@link #LONG},
   *         {@link #INTEGRAL} or {@link BOOLEANINTEGRAL}, in case it cannot
   *         decide */
  default type underBitwiseOperation(final type k) {
    return k == this ? k
        : isIntegral() && k.isIntegral() ? underIntegersOnlyOperator(k)
            : isNoInfo() ? k.underBitwiseOperationNoInfo() //
                : k.isNoInfo() ? underBitwiseOperationNoInfo() //
                    : BOOLEANINTEGRAL;
  }

  /** @return one of {@link #BOOLEAN}, {@link #INT}, {@link #LONG},
   *         {@link #INTEGRAL} or {@link BOOLEANINTEGRAL}, in case it cannot
   *         decide */
  default type underBitwiseOperationNoInfo() {
    return this == BOOLEAN ? BOOLEAN : !isIntegral() ? BOOLEANINTEGRAL : this == LONG ? LONG : INTEGRAL;
  }

  default type underIntegersOnlyOperator(final type k) {
    final type ¢1 = asIntegralUnderOperation();
    final type ¢2 = k.asIntegralUnderOperation();
    return in(LONG, ¢1, ¢2) ? LONG : !in(INTEGRAL, ¢1, ¢2) ? INT : INTEGRAL;
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #INTEGRAL},
   *         {@link #DOUBLE}, or {@link #NUMERIC}, in case it cannot decide */
  default type underNumericOnlyOperator(final type k) {
    if (!isNumeric())
      return asNumericUnderOperation().underNumericOnlyOperator(k);
    assert k != null;
    assert this != ALPHANUMERIC : "Don't confuse " + NUMERIC + " with " + ALPHANUMERIC;
    assert isNumeric() : this + ": is for some reason not numeric ";
    final type $ = k.asNumericUnderOperation();
    assert $ != null;
    assert $.isNumeric() : this + ": is for some reason not numeric ";
    // Double contaminates Numeric
    // Numeric contaminates Float
    // FLOAT contaminates Integral
    // LONG contaminates INTEGRAL
    // INTEGRAL contaminates INT
    // Everything else is INT after an operation
    return in(DOUBLE, $, this) ? DOUBLE
        : in(NUMERIC, $, this) ? NUMERIC //
            : in(FLOAT, $, this) ? FLOAT //
                : in(LONG, $, this) ? LONG : //
                    !in(INTEGRAL, $, this) ? INT : INTEGRAL;
  }

  /** @return one of {@link #INT}, {@link #LONG}, {@link #DOUBLE},
   *         {@link #STRING}, {@link #INTEGRAL}, {@link #NUMERIC} or
   *         {@link #ALPHANUMERIC}, in case it cannot decide */
  default type underPlus(final type k) {
    // addition with NULL or String must be a String
    // unless both operands are numeric, the result may be a String
    return in(STRING, this, k) || in(NULL, this, k) ? STRING : !isNumeric() || !k.isNumeric() ? ALPHANUMERIC : underNumericOnlyOperator(k);
  }

  /** An interface with one method- axiom, overloaded for many different
   * parameter types. Can be used to find the type of an expression thats known
   * at compile time by using overloading. Only use for testing, mainly for
   * testing of type.
   * @author Shalmon Niv
   * @year 2016 */
  @SuppressWarnings("unused") interface Axiom {
    static type.Primitive.Certain type(final boolean x) {
      return type.Primitive.Certain.BOOLEAN;
    }

    // from here on is the axiom method used for testing of Doubt. see issue
    // #105 for more details
    static type.Primitive.Certain type(final byte x) {
      return BYTE;
    }

    static type.Primitive.Certain type(final char x) {
      return CHAR;
    }

    static type.Primitive.Certain type(final double x) {
      return DOUBLE;
    }

    static type.Primitive.Certain type(final float x) {
      return FLOAT;
    }

    static type.Primitive.Certain type(final int x) {
      return INT;
    }

    static type.Primitive.Certain type(final long x) {
      return LONG;
    }

    static type.Odd type(final Object o) {
      return OBJECT;
    }

    static type.Primitive.Certain type(final short x) {
      return SHORT;
    }

    static type.Primitive.Certain type(final String x) {
      return STRING;
    }
  }

  /** Types we do not full understand yet.
   * @author Yossi Gil
   * @author Shalmon Niv
   * @year 2016 */
  interface Odd extends type {
    // Those anonymous characters that known little or nothing about
    // themselves
    /** TODO: Not sure we need all these {@link type.Odd.Types} values. */
    enum Types implements Odd {
      OBJECT("null", "when it is certain to be null: null, (null), ((null)), etc. but nothing else"), //
      NULL("null", "when it is certain to be null: null, (null), ((null)), etc. but nothing else"), //
      NOTHING("none", "when nothing can be said, e.g., f(f(),f(f(f()),f()))"), //
      ;
      private final String description;
      private final String key;

      private Types(final String description, final String key) {
        this.description = description;
        this.key = key;
      }

      @Override public String description() {
        key();
        return description;
      }

      @Override public String key() {
        return key;
      }
    }
  }

  /** Primitive type or a set of primitive types
   * @author Yossi Gil
   * @year 2016 */
  interface Primitive extends type {
    /** Primitive types known for certain. {@link String} is also considered
     * {@link Primitive.Certain}
     * @author Yossi Gil
     * @since 2016 */
    public enum Certain implements type.Primitive {
      BOOLEAN("boolean", "must be boolean: !f(), f() || g() "), //
      BYTE("byte", "must be byte: (byte)1, nothing else"), //
      CHAR("char", "must be char: 'a', (char)97, nothing else"), //
      DOUBLE("double", "must be double: 2.0, 2.0*a()-g(), no 2%a(), no 2*f()"), //
      FLOAT("float", "must be float: 2f, 2.3f+1, 2F-f()"), //
      INT("int", "must be int: 2, 2*(int)f(), 2%(int)f(), 'a'*2 , no 2*f()"), //
      LONG("long", "must be long: 2L, 2*(long)f(), 2%(long)f(), no 2*f()"), //
      SHORT("short", "must be short: (short)15, nothing else"), //
      STRING("String", "must be string: \"\"+a, a.toString(), f()+null, not f()+g()"), //
      ;
      final String description;
      final String key;

      Certain(final String key, final String description) {
        this.key = key;
        this.description = description;
      }

      @Override public boolean canB(final Certain ¢) {
        return ¢ == this;
      }

      @Override public String description() {
        return description;
      }

      @Override public String key() {
        return key;
      }
    }

    /** TODO: Niv Issue*94
     * <p>
     * Tells how much we know about the type of of a variable, function, or
     * expression. This should be conservative approximation to the real type of
     * the entity, what a rational, but prudent programmer would case about the
     * type
     * <p>
     * Dispatching in this class should emulate the type inference of Java. It
     * is simple to that by hard coding constants.
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
    public enum Uncertain implements type.Primitive {
      // Doubtful types, from four fold uncertainty down to bilateral
      // schizophrenia" .
      ALPHANUMERIC("String|double|float|long|int|char|short|byte", "only in binary plus: f()+g(), 2 + f(), nor f() + null"), //
      BOOLEANINTEGRAL(as.list(BOOLEAN, BYTE, SHORT, CHAR, INT, LONG), "only in x^y,x&y,x|y"), //
      INTEGER(as.list(INT, LONG), "must be either int or long: f()%g()^h()<<f()|g()&h(), not 2+(long)f() "), //
      INTEGRAL(as.list(BYTE, CHAR, SHORT, INT, LONG), "must be either int or long: f()%g()^h()<<f()|g()&h(), not 2+(long)f() "), //
      NUMERIC("double|float|long|int|char|short|byte", "must be either f()*g(), 2L*f(), 2.*a(), not 2 %a(), nor 2"), //
      ;
      final String description;
      final String key;

      private Uncertain(final Iterable<? extends type> ts, final String description) {
        this(ts + "", description);
      }

      private Uncertain(final String key, final String description) {
        this.key = key;
        this.description = description;
      }

      @Override public String description() {
        return description;
      }

      @Override public String key() {
        return key;
      }
    }
  }
} // end of interface type