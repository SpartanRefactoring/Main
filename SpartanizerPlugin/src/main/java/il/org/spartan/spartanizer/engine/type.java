package il.org.spartan.spartanizer.engine;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.engine.type.Odd.Types.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Uncertain.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.extract.*;

import il.org.spartan.*;
import il.org.spartan.iterables.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @author Dor Maayan
 * @author Niv Shalmon
 * @since 2016 */
public interface type {
  @NotNull
  static inner.implementation baptize(@NotNull final String name) {
    return baptize(name, "anonymously born");
  }

  @NotNull
  static inner.implementation baptize(@NotNull final String name, @NotNull final String description) {
    return have(name) ? bring(name) : new inner.implementation() {
      @NotNull
      @Override public String description() {
        return description;
      }

      @NotNull
      @Override public String toString() {
        return name + "";
      }

      @NotNull
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

  static boolean isDouble(@NotNull final Expression ¢) {
    return type.of(¢) == Certain.DOUBLE;
  }

  static boolean isInt(@NotNull final Expression ¢) {
    return type.of(¢) == Certain.INT;
  }

  static boolean isLong(@NotNull final Expression ¢) {
    return type.of(¢) == Certain.LONG;
  }

  /** @param x JD
   * @return <code><b>true</b></code> <i>if</i> the parameter is an expression
   *         whose type is provably not of type {@link String}, in the sense
   *         used in applying the {@code +} operator to concatenate strings.
   *         concatenation. */
  static boolean isNotString(@NotNull final Expression ¢) {
    return !in(of(¢), STRING, ALPHANUMERIC);
  }

  static boolean isString(@NotNull final Expression ¢) {
    return of(¢) == Certain.STRING;
  }

  // TODO: Matteo: Nano-pattern of values: not implemented
  @Nullable
  @SuppressWarnings("synthetic-access") static type of(@NotNull final Expression ¢) {
    return inner.get(¢);
  }

  @Nullable
  default Certain asPrimitiveCertain() {
    return null;
  }

  @Nullable
  default Uncertain asPrimitiveUncertain() {
    return null;
  }

  default boolean canB(@SuppressWarnings("unused") final Certain __) {
    return false;
  }

  String description();

  @NotNull
  default String fullName() {
    return this + "=" + key() + " (" + description() + ")";
  }

  /** @return <code><b>true</b></code> <em>iff</em>one of {@link #INT} ,
   *         {@link #LONG} , {@link #CHAR} , {@link BYTE} , {@link SHORT} ,
   *         {@link FLOAT} , {@link #DOUBLE} , {@link #INTEGRAL} or
   *         {@link #NUMERIC} , {@link #STRING} , {@link #ALPHANUMERIC} or false
   *         otherwise */
  default boolean isAlphaNumeric() {
    return in(this, INT, LONG, CHAR, BYTE, SHORT, FLOAT, DOUBLE, INTEGRAL, NUMERIC, STRING, ALPHANUMERIC);
  }

  /** @return <code><b>true</b></code> <em>iff</em>either a Primitive.Certain,
   *         Primitive.Odd.NULL or a baptized type */
  default boolean isCertain() {
    return this == NULL || have(key()) || asPrimitiveCertain() != null;
  }

  /** @return <code><b>true</b></code> <em>iff</em>one of {@link #INT} ,
   *         {@link #LONG} , {@link #CHAR} , {@link BYTE} , {@link SHORT} ,
   *         {@link #INTEGRAL} or false otherwise */
  default boolean isIntegral() {
    return in(this, LONG, INT, CHAR, BYTE, SHORT, INTEGRAL);
  }

  /** @return <code><b>true</b></code> <em>iff</em>one of {@link #INT} ,
   *         {@link #LONG} , {@link #CHAR} , {@link BYTE} , {@link SHORT} ,
   *         {@link FLOAT} , {@link #DOUBLE} , {@link #INTEGRAL} ,
   *         {@link #NUMERIC} or false otherwise */
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
    @NotNull
    static Certain type(final boolean __) {
      return BOOLEAN;
    }

    @NotNull
    static Certain type(final byte __) {
      return BYTE;
    }

    @NotNull
    static Certain type(final char __) {
      return CHAR;
    }

    @NotNull
    static Certain type(final double __) {
      return DOUBLE;
    }

    @NotNull
    static Certain type(final float __) {
      return FLOAT;
    }

    @NotNull
    static Certain type(final int __) {
      return INT;
    }

    @NotNull
    static Certain type(final long __) {
      return LONG;
    }

    @NotNull
    static type type(final Object __) {
      return NOTHING;
    }

    @NotNull
    static Certain type(final short __) {
      return SHORT;
    }

    @NotNull
    static Certain type(final String __) {
      return STRING;
    }
  }

  enum inner {
    ;
    private static final String propertyName = "spartan type";
    /** All type that were ever born , as well as all primitive types */
    static final Map<String, implementation> types = new LinkedHashMap<>();

    @Nullable
    private static implementation get(@NotNull final Expression ¢) {
      return (implementation) (property.has(¢, propertyName) ? property.get(¢, propertyName) : property.set(¢, propertyName, lookUp(¢, lookDown(¢))));
    }

    private static boolean isCastedToShort(final implementation i1, final implementation i2, final Expression x) {
      if (i1 != SHORT || i2 != INT || !iz.numberLiteral(x))
        return false;
      final int $ = Integer.parseInt(step.token(az.numberLiteral(x)));
      return $ < Short.MAX_VALUE && $ > Short.MIN_VALUE;
    }

    @Nullable
    private static implementation lookDown(final Assignment x) {
      final implementation $ = get(step.to(x));
      return !$.isNoInfo() ? $ : get(step.from(x)).isNumeric() ? NUMERIC : get(step.from(x));
    }

    @NotNull
    private static implementation lookDown(final CastExpression ¢) {
      return get(step.expression(¢)) == NULL ? NULL : baptize(step.type(¢) + "");
    }

    @NotNull
    private static implementation lookDown(final ClassInstanceCreation ¢) {
      return baptize(step.type(¢) + "");
    }

    @NotNull
    private static implementation lookDown(final ConditionalExpression x) {
      final implementation $ = get(step.then(x)), ¢ = get(step.elze(x));
      return $ == ¢ ? $
          : isCastedToShort($, ¢, elze(x)) || isCastedToShort(¢, $, then(x)) ? SHORT
              : !$.isNumeric() || !¢.isNumeric() ? NOTHING : $.underNumericOnlyOperator(¢);
    }

    /** @param x JD
     * @return The most specific Type information that can be deduced about the
     *         expression from it's structure, or {@link #NOTHING} if it cannot
     *         decide. Will never return null */
    @Nullable
    private static implementation lookDown(@NotNull final Expression ¢) {
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

    @Nullable
    private static implementation lookDown(final InfixExpression x) {
      final InfixExpression.Operator o = operator(x);
      final List<Expression> es = hop.operands(x);
      implementation $ = get(first(es));
      for (final Expression ¢ : rest(es))
        $ = $.underBinaryOperator(o, get(¢));
      return $;
    }

    @NotNull
    private static implementation lookDown(final MethodInvocation ¢) {
      return "toString".equals(step.name(¢) + "") && arguments(¢).isEmpty() ? STRING : NOTHING;
    }

    @NotNull
    private static implementation lookDown(final NumberLiteral ¢) {
      return new NumericLiteralClassifier(step.token(¢)).type();
    }

    @Nullable
    private static implementation lookDown(final ParenthesizedExpression ¢) {
      return get(core(¢));
    }

    @NotNull
    private static implementation lookDown(final PostfixExpression ¢) {
      return get(step.operand(¢)).asNumeric(); // see
                                               // testInDecreamentSemantics
    }

    @NotNull
    private static implementation lookDown(final PrefixExpression ¢) {
      return get(step.operand(¢)).under(step.operator(¢));
    }

    @NotNull
    private static implementation lookDown(final VariableDeclarationExpression ¢) {
      return baptize(step.type(¢) + "");
    }

    @NotNull
    private static implementation lookUp(final Expression x, @NotNull final implementation i) {
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
      @NotNull
      default implementation above(final PrefixExpression.Operator ¢) {
        return ¢ == NOT ? BOOLEAN : ¢ != COMPLEMENT ? asNumeric() : asIntegral();
      }

      @NotNull
      default implementation aboveBinaryOperator(final InfixExpression.Operator ¢) {
        return in(¢, EQUALS, NOT_EQUALS) ? this
            : ¢ == wizard.PLUS2 ? asAlphaNumeric()
                : wizard.isBitwiseOperator(¢) ? asBooleanIntegral() : wizard.isShift(¢) ? asIntegral() : asNumeric();
      }

      @NotNull
      default implementation asAlphaNumeric() {
        return isAlphaNumeric() ? this : ALPHANUMERIC;
      }

      @NotNull
      default implementation asBooleanIntegral() {
        return isIntegral() || this == BOOLEAN ? this : BOOLEANINTEGRAL;
      }

      /** @return one of {@link #INT}, {@link #LONG}, {@link #CHAR},
       *         {@link BYTE}, {@link SHORT} or {@link #INTEGRAL}, in case it
       *         cannot decide */
      @NotNull
      default implementation asIntegral() {
        return isIntegral() ? this : INTEGRAL;
      }

      /** @return one of {@link #INT}, {@link #LONG}, or {@link #INTEGRAL}, in
       *         case it cannot decide */
      @NotNull
      default implementation asIntegralUnderOperation() {
        return isIntUnderOperation() ? INT : asIntegral();
      }

      /** @return one of {@link #INT}, {@link #LONG},, {@link #CHAR},
       *         {@link BYTE}, {@link SHORT}, {@link FLOAT}, {@link #DOUBLE},
       *         {@link #INTEGRAL} or {@link #NUMERIC}, in case no further
       *         information is available */
      @NotNull
      default implementation asNumeric() {
        return isNumeric() ? this : NUMERIC;
      }

      /** @return one of {@link #INT}, {@link #LONG}, {@link #FLOAT},
       *         {@link #DOUBLE}, {@link #INTEGRAL} or {@link #NUMERIC}, in case
       *         no further information is available */
      @NotNull
      default implementation asNumericUnderOperation() {
        return !isNumeric() ? NUMERIC : isIntUnderOperation() ? INT : this;
      }

      /** used to determine whether an integral type behaves as itself under
       * operations or as an INT.
       * @return <code><b>true</b></code> <em>iff</em>one of {@link #CHAR},
       *         {@link BYTE}, {@link SHORT} or false otherwise. */
      default boolean isIntUnderOperation() {
        return in(this, CHAR, BYTE, SHORT);
      }

      /** @return <code><b>true</b></code> <em>iff</em>one of {@link #NOTHING},
       *         {@link #NULL} or false otherwise */
      default boolean isNoInfo() {
        return in(this, NOTHING, NULL);
      }

      @NotNull
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
      @NotNull
      default implementation under(@NotNull final PrefixExpression.Operator ¢) {
        assert ¢ != null;
        return ¢ == NOT ? BOOLEAN
            : in(¢, DECREMENT, INCREMENT) ? asNumeric() : ¢ != COMPLEMENT ? asNumericUnderOperation() : asIntegralUnderOperation();
      }

      /** @return one of {@link #BOOLEAN} , {@link #INT} , {@link #LONG} ,
       *         {@link #DOUBLE} , {@link #STRING} , {@link #INTEGRAL} ,
       *         {@link BOOLEANINTEGRAL} {@link #NUMERIC} , or
       *         {@link #ALPHANUMERIC} , in case it cannot decide */
      @NotNull
      default implementation underBinaryOperator(final InfixExpression.Operator o, @NotNull final implementation k) {
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
      @NotNull
      default implementation underBitwiseOperation(@NotNull final implementation k) {
        return k == this ? k
            : isIntegral() && k.isIntegral() ? underIntegersOnlyOperator(k)
                : isNoInfo() ? k.underBitwiseOperationNoInfo() //
                    : k.isNoInfo() ? underBitwiseOperationNoInfo() //
                        : BOOLEANINTEGRAL;
      }

      /** @return one of {@link #BOOLEAN}, {@link #INT}, {@link #LONG},
       *         {@link #INTEGRAL} or {@link BOOLEANINTEGRAL}, in case it cannot
       *         decide */
      @NotNull
      default implementation underBitwiseOperationNoInfo() {
        return this == BOOLEAN ? BOOLEAN : !isIntegral() ? BOOLEANINTEGRAL : this == LONG ? LONG : INTEGRAL;
      }

      @NotNull
      default implementation underIntegersOnlyOperator(@NotNull final implementation k) {
        final implementation $ = asIntegralUnderOperation(), ¢2 = k.asIntegralUnderOperation();
        return in(LONG, $, ¢2) ? LONG : !in(INTEGRAL, $, ¢2) ? INT : INTEGRAL;
      }

      /** @return one of {@link #INT}, {@link #LONG}, {@link #INTEGRAL},
       *         {@link #DOUBLE}, or {@link #NUMERIC}, in case it cannot
       *         decide */
      @NotNull
      default implementation underNumericOnlyOperator(@NotNull final implementation k) {
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
      @NotNull
      default implementation underPlus(@NotNull final implementation k) {
        // addition with NULL or String must be a String
        // unless both operands are numeric, the result is alphanumeric
        return in(STRING, this, k) || in(NULL, this, k) ? STRING : !isNumeric() || !k.isNumeric() ? ALPHANUMERIC : underNumericOnlyOperator(k);
      }
    }
  }

  /** Types we do not fully understand yet.
   * @author Yossi Gil
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
   * @author Yossi Gil
   * @since 2016 */
  interface Primitive extends inner.implementation {
    /** @return All {@link Certain} types that an expression of this type can
     *         be **/
    Iterable<Certain> options();

    /** Primitive types known for certain. {@link String} is also considered
     * {@link Primitive.Certain}
     * @author Yossi Gil
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

      Certain(final String key, final String description, @Nullable final String s) {
        this.key = key;
        this.description = description;
        inner.types.put(key, this);
        if (s != null)
          inner.types.put(s, this);
      }

      @NotNull
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
     * @author Yossi Gil
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
        as.list(ps).forEach(p -> options.addAll(az.stream(p.options()).filter(λ -> !options.contains(λ)).collect(Collectors.toList())));
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

      @NotNull
      @Override public Iterable<Certain> options() {
        return options;
      }
    }
  }
} // end of interface type
