package il.org.spartan.spartanizer.engine;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.engine.type.Odd.Types.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Uncertain.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.*;
import il.org.spartan.iterables.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;

/** TODO: Niv Shalmon please add a description
 * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
 * @author Dor Maayan
 * @author Niv Shalmon
 * @since 2016 */
public interface type {
  static inner.implementation baptize(final String name) {
    return baptize(name, "anonymously born");
  }

  static inner.implementation baptize(final String name, final String description) {
    return have(name) ? bring(name) : new inner.implementation() {
      @Override public String description() {
        return description;
      }

      @Override public String toString() {
        return name + "";
      }

      @Override public String key() {
        return name;
      }
    }.join();
  }

  static inner.implementation bring(final String name) {
    return inner.types.get(name);
  }

  static boolean have(final String name) {
    return inner.types.containsKey(name);
  }

  static boolean isDouble(final Expression ¢) {
    return type.of(¢) == DOUBLE;
  }

  static boolean isInt(final Expression ¢) {
    return type.of(¢) == INT;
  }

  static boolean isLong(final Expression ¢) {
    return type.of(¢) == LONG;
  }

  /** @param x JD
   * @return {@code true} <i>if</i> the parameter is an expression whose type is
   *         provably not of type {@link String}, in the sense used in applying
   *         the {@code +} operator to concatenate strings. concatenation. */
  static boolean isNotString(final Expression ¢) {
    return !in(of(¢), STRING, ALPHANUMERIC);
  }

  static boolean isString(final Expression ¢) {
    return of(¢) == STRING;
  }

  // TODO: Matteo: Nano-pattern of values: not implemented
  @SuppressWarnings("synthetic-access") static type of(final Expression ¢) {
    return inner.get(¢);
  }

  default Certain asPrimitiveCertain() {
    return null;
  }

  default Uncertain asPrimitiveUncertain() {
    return null;
  }

  default boolean canB(@SuppressWarnings("unused") final Certain __) {
    return false;
  }

  String description();

  default String fullName() {
    return this + "=" + key() + " (" + description() + ")";
  }

  /** @return whetherone of {@link #INT} , {@link #LONG} , {@link #CHAR} ,
   *         {@link BYTE} , {@link SHORT} , {@link FLOAT} , {@link #DOUBLE} ,
   *         {@link #INTEGRAL} or {@link #NUMERIC} , {@link #STRING} ,
   *         {@link #ALPHANUMERIC} or false otherwise */
  default boolean isAlphaNumeric() {
    return in(this, INT, LONG, CHAR, BYTE, SHORT, FLOAT, DOUBLE, INTEGRAL, NUMERIC, STRING, ALPHANUMERIC);
  }

  /** @return whethereither a Primitive.Certain, Primitive.Odd.NULL or a
   *         baptized type */
  default boolean isCertain() {
    return this == NULL || have(key()) || asPrimitiveCertain() != null;
  }

  /** @return whetherone of {@link #INT} , {@link #LONG} , {@link #CHAR} ,
   *         {@link BYTE} , {@link SHORT} , {@link #INTEGRAL} or false
   *         otherwise */
  default boolean isIntegral() {
    return in(this, LONG, INT, CHAR, BYTE, SHORT, INTEGRAL);
  }

  /** @return whetherone of {@link #INT} , {@link #LONG} , {@link #CHAR} ,
   *         {@link BYTE} , {@link SHORT} , {@link FLOAT} , {@link #DOUBLE} ,
   *         {@link #INTEGRAL} , {@link #NUMERIC} or false otherwise */
  default boolean isNumeric() {
    return in(this, INT, LONG, CHAR, BYTE, SHORT, FLOAT, DOUBLE, INTEGRAL, NUMERIC);
  }

  /** @return the formal name of this type, the key under which it is stored in
   *         {@link #types}, e.g., "Object", "int", "String", etc. */
  String key();

  /** An interface with one method- type, overloaded for many different
   * parameter types. Can be used to find the type of an expression thats known
   * at compile time by using overloading. Only use for testing, mainly for
   * testing of type.
   * @author Niv Shalmon
   * @since 2016 */
  @SuppressWarnings("unused")
  interface Axiom {
    static Certain type(final boolean __) {
      return BOOLEAN;
    }

    static Certain type(final byte __) {
      return BYTE;
    }

    static Certain type(final char __) {
      return CHAR;
    }

    static Certain type(final double __) {
      return DOUBLE;
    }

    static Certain type(final float __) {
      return FLOAT;
    }

    static Certain type(final int __) {
      return INT;
    }

    static Certain type(final long __) {
      return LONG;
    }

    static type type(final Object __) {
      return NOTHING;
    }

    static Certain type(final short __) {
      return SHORT;
    }

    static Certain type(final String __) {
      return STRING;
    }
  }

  enum inner {
    ;
    private static final String propertyName = "spartan type";
    /** All type that were ever born , as well as all primitive types */
    static final Map<String, implementation> types = new LinkedHashMap<>();

    private static implementation get(final Expression ¢) {
      return (implementation) (property.has(¢, propertyName) ? property.get(¢, propertyName) : property.set(¢, propertyName, lookUp(¢, lookDown(¢))));
    }

    private static boolean isCastedToShort(final implementation i1, final implementation i2, final Expression x) {
      if (i1 != SHORT || i2 != INT || !iz.numberLiteral(x))
        return false;
      final int $ = Integer.parseInt(token(az.numberLiteral(x)));
      return $ < Short.MAX_VALUE && $ > Short.MIN_VALUE;
    }

    private static implementation lookDown(final Assignment x) {
      final implementation $ = get(to(x));
      return !$.isNoInfo() ? $ : get(from(x)).isNumeric() ? NUMERIC : get(from(x));
    }

    private static implementation lookDown(final CastExpression ¢) {
      return get(expression(¢)) == NULL ? NULL : baptize(step.type(¢) + "");
    }

    private static implementation lookDown(final ClassInstanceCreation ¢) {
      return baptize(step.type(¢) + "");
    }

    private static implementation lookDown(final ConditionalExpression x) {
      final implementation $ = get(then(x)), ¢ = get(elze(x));
      return $ == ¢ ? $
          : isCastedToShort($, ¢, elze(x)) || isCastedToShort(¢, $, then(x)) ? SHORT
              : !$.isNumeric() || !¢.isNumeric() ? NOTHING : $.underNumericOnlyOperator(¢);
    }

    /** @param x JD
     * @return The most specific Type information that can be deduced about the
     *         expression from it's structure, or {@link #NOTHING} if it cannot
     *         decide. Will never return null */
    private static implementation lookDown(final Expression ¢) {
      switch (¢.getNodeType()) {
        case BOOLEAN_LITERAL:
          return BOOLEAN;
        case CHARACTER_LITERAL:
          return CHAR;
        case NULL_LITERAL:
          return NULL;
        case STRING_LITERAL:
          return STRING;
        case ASSIGNMENT:
          return lookDown((Assignment) ¢);
        case CAST_EXPRESSION:
          return lookDown((CastExpression) ¢);
        case CLASS_INSTANCE_CREATION:
          return lookDown((ClassInstanceCreation) ¢);
        case CONDITIONAL_EXPRESSION:
          return lookDown((ConditionalExpression) ¢);
        case INFIX_EXPRESSION:
          return lookDown((InfixExpression) ¢);
        case METHOD_INVOCATION:
          return lookDown((MethodInvocation) ¢);
        case NUMBER_LITERAL:
          return lookDown((NumberLiteral) ¢);
        case PARENTHESIZED_EXPRESSION:
          return lookDown((ParenthesizedExpression) ¢);
        case POSTFIX_EXPRESSION:
          return lookDown((PostfixExpression) ¢);
        case PREFIX_EXPRESSION:
          return lookDown((PrefixExpression) ¢);
        case VARIABLE_DECLARATION_EXPRESSION:
          return lookDown((VariableDeclarationExpression) ¢);
        default:
          return NOTHING;
      }
    }

    private static implementation lookDown(final InfixExpression x) {
      final InfixExpression.Operator o = operator(x);
      final List<Expression> es = hop.operands(x);
      implementation $ = get(first(es));
      for (final Expression ¢ : rest(es))
        $ = $.underBinaryOperator(o, get(¢));
      return $;
    }

    private static implementation lookDown(final MethodInvocation ¢) {
      return "toString".equals(step.name(¢) + "") && arguments(¢).isEmpty() ? STRING : NOTHING;
    }

    private static implementation lookDown(final NumberLiteral ¢) {
      return new NumericLiteralClassifier(token(¢)).type();
    }

    private static implementation lookDown(final ParenthesizedExpression ¢) {
      return get(core(¢));
    }

    private static implementation lookDown(final PostfixExpression ¢) {
      return get(operand(¢)).asNumeric(); // see
                                          // testInDecreamentSemantics
    }

    private static implementation lookDown(final PrefixExpression ¢) {
      return get(operand(¢)).under(operator(¢));
    }

    private static implementation lookDown(final VariableDeclarationExpression ¢) {
      return baptize(step.type(¢) + "");
    }

    private static implementation lookUp(final Expression x, final implementation i) {
      if (i.isCertain())
        return i;
      for (final ASTNode $ : hop.ancestors(x)) {
        final ASTNode context = $.getParent();
        if (context != null)
          switch (context.getNodeType()) {
            case IF_STATEMENT:
              return BOOLEAN;
            case ARRAY_ACCESS:
              return i.asIntegral();
            case POSTFIX_EXPRESSION:
              return i.asNumeric();
            case INFIX_EXPRESSION:
              return i.aboveBinaryOperator(az.infixExpression(context).getOperator());
            case PREFIX_EXPRESSION:
              return i.above(az.prefixExpression(context).getOperator());
            case ASSERT_STATEMENT:
              return $.getLocationInParent() != AssertStatement.EXPRESSION_PROPERTY ? i : BOOLEAN;
            case FOR_STATEMENT:
              return $.getLocationInParent() != ForStatement.EXPRESSION_PROPERTY ? i : BOOLEAN;
            case PARENTHESIZED_EXPRESSION:
              continue;
            default:
              return i;
          }
      }
      return i;
    }

    // an interface for inner methods that shouldn'tipper be public
    private interface implementation extends type {
      /** To be used to determine the type of something that o was used on
       * @return one of {@link #BOOLEAN} , {@link #INT} , {@link #LONG} ,
       *         {@link #DOUBLE} , {@link #INTEGRAL} or {@link #NUMERIC} , in
       *         case it cannot decide */
      default implementation above(final PrefixExpression.Operator ¢) {
        return ¢ == NOT ? BOOLEAN : ¢ != COMPLEMENT ? asNumeric() : asIntegral();
      }

      default implementation aboveBinaryOperator(final InfixExpression.Operator ¢) {
        return in(¢, EQUALS, NOT_EQUALS) ? this
            : ¢ == wizard.PLUS2 ? asAlphaNumeric()
                : wizard.isBitwiseOperator(¢) ? asBooleanIntegral() : wizard.isShift(¢) ? asIntegral() : asNumeric();
      }

      default implementation asAlphaNumeric() {
        return isAlphaNumeric() ? this : ALPHANUMERIC;
      }

      default implementation asBooleanIntegral() {
        return isIntegral() || this == BOOLEAN ? this : BOOLEANINTEGRAL;
      }

      /** @return one of {@link #INT}, {@link #LONG}, {@link #CHAR},
       *         {@link BYTE}, {@link SHORT} or {@link #INTEGRAL}, in case it
       *         cannot decide */
      default implementation asIntegral() {
        return isIntegral() ? this : INTEGRAL;
      }

      /** @return one of {@link #INT}, {@link #LONG}, or {@link #INTEGRAL}, in
       *         case it cannot decide */
      default implementation asIntegralUnderOperation() {
        return isIntUnderOperation() ? INT : asIntegral();
      }

      /** @return one of {@link #INT}, {@link #LONG},, {@link #CHAR},
       *         {@link BYTE}, {@link SHORT}, {@link FLOAT}, {@link #DOUBLE},
       *         {@link #INTEGRAL} or {@link #NUMERIC}, in case no further
       *         information is available */
      default implementation asNumeric() {
        return isNumeric() ? this : NUMERIC;
      }

      /** @return one of {@link #INT}, {@link #LONG}, {@link #FLOAT},
       *         {@link #DOUBLE}, {@link #INTEGRAL} or {@link #NUMERIC}, in case
       *         no further information is available */
      default implementation asNumericUnderOperation() {
        return !isNumeric() ? NUMERIC : isIntUnderOperation() ? INT : this;
      }

      /** used to determine whether an integral type behaves as itself under
       * operations or as an INT.
       * @return whetherone of {@link #CHAR}, {@link BYTE}, {@link SHORT} or
       *         false otherwise. */
      default boolean isIntUnderOperation() {
        return in(this, CHAR, BYTE, SHORT);
      }

      /** @return whetherone of {@link #NOTHING}, {@link #NULL} or false
       *         otherwise */
      default boolean isNoInfo() {
        return in(this, NOTHING, NULL);
      }

      default implementation join() {
        assert !have(key()) : "fault: the dictionary should not have type " + key() + "\n receiver is " + this + "\n This is all I know";
        inner.types.put(key(), this);
        return this;
      }

      /** To be used to determine the type of the result of o being used on the
       * caller
       * @return one of {@link #BOOLEAN} , {@link #INT} , {@link #LONG} ,
       *         {@link #DOUBLE} , {@link #INTEGRAL} or {@link #NUMERIC} , in
       *         case it cannot decide */
      default implementation under(final PrefixExpression.Operator ¢) {
        assert ¢ != null;
        return ¢ == NOT ? BOOLEAN
            : in(¢, DECREMENT, INCREMENT) ? asNumeric() : ¢ != COMPLEMENT ? asNumericUnderOperation() : asIntegralUnderOperation();
      }

      /** @return one of {@link #BOOLEAN} , {@link #INT} , {@link #LONG} ,
       *         {@link #DOUBLE} , {@link #STRING} , {@link #INTEGRAL} ,
       *         {@link BOOLEANINTEGRAL} {@link #NUMERIC} , or
       *         {@link #ALPHANUMERIC} , in case it cannot decide */
      default implementation underBinaryOperator(final InfixExpression.Operator o, final implementation k) {
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
      default implementation underBitwiseOperation(final implementation k) {
        return k == this ? k
            : isIntegral() && k.isIntegral() ? underIntegersOnlyOperator(k)
                : isNoInfo() ? k.underBitwiseOperationNoInfo() //
                    : k.isNoInfo() ? underBitwiseOperationNoInfo() //
                        : BOOLEANINTEGRAL;
      }

      /** @return one of {@link #BOOLEAN}, {@link #INT}, {@link #LONG},
       *         {@link #INTEGRAL} or {@link BOOLEANINTEGRAL}, in case it cannot
       *         decide */
      default implementation underBitwiseOperationNoInfo() {
        return this == BOOLEAN ? BOOLEAN : !isIntegral() ? BOOLEANINTEGRAL : this == LONG ? LONG : INTEGRAL;
      }

      default implementation underIntegersOnlyOperator(final implementation k) {
        final implementation $ = asIntegralUnderOperation(), ¢2 = k.asIntegralUnderOperation();
        return in(LONG, $, ¢2) ? LONG : !in(INTEGRAL, $, ¢2) ? INT : INTEGRAL;
      }

      /** @return one of {@link #INT}, {@link #LONG}, {@link #INTEGRAL},
       *         {@link #DOUBLE}, or {@link #NUMERIC}, in case it cannot
       *         decide */
      default implementation underNumericOnlyOperator(final implementation k) {
        if (!isNumeric())
          return asNumericUnderOperation().underNumericOnlyOperator(k);
        assert k != null;
        assert this != ALPHANUMERIC : "Don'tipper confuse " + NUMERIC + " with " + ALPHANUMERIC;
        assert isNumeric() : this + ": is for some reason not numeric ";
        final implementation $ = k.asNumericUnderOperation();
        assert $ != null;
        assert $.isNumeric() : this + ": is for some reason not numeric ";
        return in(DOUBLE, $, this) ? DOUBLE // Double contaminates Numeric
            : in(NUMERIC, $, this) ? NUMERIC // Numeric contaminates Float
                : in(FLOAT, $, this) ? FLOAT // FLOAT contaminates Integral
                    : in(LONG, $, this) ? LONG : // LONG contaminates INTEGRAL
                        !in(INTEGRAL, $, this) ? INT : INTEGRAL;// INTEGRAL
                                                                // contaminates
                                                                // INT
      }

      /** @return one of {@link #INT}, {@link #LONG}, {@link #DOUBLE},
       *         {@link #STRING}, {@link #INTEGRAL}, {@link #NUMERIC} or
       *         {@link #ALPHANUMERIC}, in case it cannot decide */
      default implementation underPlus(final implementation k) {
        // addition with NULL or String must be a String
        // unless both operands are numeric, the result is alphanumeric
        return in(STRING, this, k) || in(NULL, this, k) ? STRING : !isNumeric() || !k.isNumeric() ? ALPHANUMERIC : underNumericOnlyOperator(k);
      }
    }
  }

  /** Types we do not fully understand yet.
   * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
   * @author Niv Shalmon
   * @since 2016 */
  interface Odd extends inner.implementation {
    /** Those anonymous characters that know little or nothing about
     * themselves */
    enum Types implements Odd {
      /** TOOD: Dor, take note that in certain situations, null could be a
       * {@link Boolean} type */
      NULL("null", "when it is certain to be null: null, (null), ((null)), etc. but nothing else"), //
      NOTHING("none", "when nothing can be said, e.g., f(f(),f(f(f()),f()))"), //
      ;
      private final String description;
      private final String key;

      Types(final String description, final String key) {
        this.description = description;
        this.key = key;
      }

      @Override public String description() {
        return description;
      }

      @Override public String key() {
        return key;
      }
    }
  }

  /** Primitive type or a set of primitive types
   * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
   * @since 2016 */
  interface Primitive extends inner.implementation {
    /** @return All {@link Certain} types that an expression of this type can
     *         be **/
    Iterable<Certain> options();

    /** Primitive types known for certain. {@link String} is also considered
     * {@link Primitive.Certain}
     * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
     * @since 2016 */
    enum Certain implements Primitive {
      BOOLEAN("boolean", "must be boolean: !f(), f() || g() ", "Boolean")//
      , BYTE("byte", "must be byte: (byte)1, nothing else", "Byte")//
      , CHAR("char", "must be char: 'a', (char)97, nothing else", "Character")//
      , DOUBLE("double", "must be double: 2.0, 2.0*a()-g(), no 2%a(), no 2*f()", "Double") //
      , FLOAT("float", "must be float: 2f, 2.3f+1, 2F-f()", "Float")//
      , INT("int", "must be int: 2, 2*(int)f(), 2%(int)f(), 'a'*2 , no 2*f()", "Integer")//
      , LONG("long", "must be long: 2L, 2*(long)f(), 2%(long)f(), no 2*f()", "Long")//
      , SHORT("short", "must be short: (short)15, nothing else", "Short")//
      , STRING("String", "must be string: \"\"+a, a.toString(), f()+null, not f()+g()", null)//
      ;
      final String description;
      final String key;

      Certain(final String key, final String description, final String s) {
        this.key = key;
        this.description = description;
        inner.types.put(key, this);
        if (s != null)
          inner.types.put(s, this);
      }

      @Override public Certain asPrimitiveCertain() {
        return this;
      }

      @Override public Uncertain asPrimitiveUncertain() {
        return isIntegral() ? INTEGRAL : isNumeric() ? NUMERIC : isAlphaNumeric() ? ALPHANUMERIC : this != BOOLEAN ? null : BOOLEANINTEGRAL;
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

      @Override public Iterable<Certain> options() {
        return iterables.singleton(this);
      }
    }

    /** A set of {@link Primitive.Certain} types, where the expressions type
     * cannot be determined for certain
     * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
     * @author Niv Shalmon
     * @since 2016-08-XX */
    enum Uncertain implements Primitive {
      INTEGER("must be either int or long: f()%g()^h()<<f()|g()&h(), not 2+(long)f() ", INT, LONG)//
      , INTEGRAL("must be either int or long: f()%g()^h()<<f()|g()&h(), not 2+(long)f() ", INTEGER, CHAR, SHORT, BYTE)//
      , NUMERIC("must be either f()*g(), 2L*f(), 2.*a(), not 2 %a(), nor 2", INTEGRAL, FLOAT, DOUBLE)//
      , ALPHANUMERIC("only in binary plus: f()+g(), 2 + f(), nor f() + null", NUMERIC, STRING)//
      , BOOLEANINTEGRAL("only in x^y,x&y,x|y", BOOLEAN, INTEGRAL)//
      ;
      final String description;
      final Set<Certain> options = new LinkedHashSet<>();

      Uncertain(final String description, final Primitive... ps) {
        this.description = description;
        as.list(ps).forEach(p -> options.addAll(az.stream(p.options()).filter(λ -> !options.contains(λ)).collect(toList())));
      }

      @Override public boolean canB(final Certain ¢) {
        return options.contains(¢);
      }

      @Override public String description() {
        return description;
      }

      @Override public String key() {
        return separate.these(options).by('|');
      }

      @Override public Iterable<Certain> options() {
        return options;
      }
    }
  }
} // end of interface type
