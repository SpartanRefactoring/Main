package il.org.spartan.spartanizer.engine;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.engine.type.*;
import static il.org.spartan.spartanizer.engine.type.Odd.Types.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Certain.*;
import static il.org.spartan.spartanizer.engine.type.Primitive.Uncertain.*;
import static org.eclipse.jdt.core.dom.ASTNode.*;
import static org.eclipse.jdt.core.dom.InfixExpression.Operator.*;
import static org.eclipse.jdt.core.dom.PrefixExpression.Operator.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.extract.*;

import il.org.spartan.*;
import il.org.spartan.iterables.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.engine.type.Primitive.*;
import il.org.spartan.spartanizer.java.*;

/** @author Yossi Gil
 ** @author Dor Maayan
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

      @Override public String key() {
        return name;
      }
    }.join();
  }

  @SuppressWarnings("synthetic-access") static inner.implementation bring(final String name) {
    return inner.types.get(name);
  }

  // TODO: Matteo. Nano-pattern of values: not implemented
  @SuppressWarnings("synthetic-access") static type get(final Expression ¢) {
    return inner.get(¢);
  }

  @SuppressWarnings("synthetic-access") static boolean have(final String name) {
    return inner.types.containsKey(name);
  }

  static boolean isDouble(final Expression ¢) {
    return get(¢) == Certain.DOUBLE;
  }

  static boolean isInt(final Expression ¢) {
    return type.get(¢) == Certain.INT;
  }

  static boolean isLong(final Expression ¢) {
    return get(¢) == Certain.LONG;
  }

  /** @param x JD
   * @return <code><b>true</b></code> <i>if</i> the parameter is an expression
   *         whose type is provably not of type {@link String}, in the sense
   *         used in applying the <code>+</code> operator to concatenate
   *         strings. concatenation. */
  static boolean isNotString(final Expression ¢) {
    return !in(get(¢), STRING, ALPHANUMERIC);
  }

  static boolean isString(final Expression ¢) {
    return get(¢) == Certain.STRING;
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

  /** @return true if one of {@link #INT} , {@link #LONG} , {@link #CHAR} ,
   *         {@link BYTE} , {@link SHORT} , {@link FLOAT} , {@link #DOUBLE} ,
   *         {@link #INTEGRAL} or {@link #NUMERIC} , {@link #STRING} ,
   *         {@link #ALPHANUMERIC} or false otherwise */
  default boolean isAlphaNumeric() {
    return in(this, INT, LONG, CHAR, BYTE, SHORT, FLOAT, DOUBLE, INTEGRAL, NUMERIC, STRING, ALPHANUMERIC);
  }

  /** @return true if either a Primitive.Certain, Primitive.Odd.NULL or a
   *         baptized type */
  default boolean isCertain() {
    return this == NULL || have(key()) || asPrimitiveCertain() != null;
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

  /** @return the formal name of this type, the key under which it is stored in
   *         {@link #types}, e.g., "Object", "int", "String", etc. */
  String key();

  // TOOD: Ori, types are deterministic, everything is known at compile time.
  // See here.
  /** An interface with one method- type, overloaded for many different
   * parameter types. Can be used to find the type of an expression thats known
   * at compile time by using overloading. Only use for testing, mainly for
   * testing of type.
   * @author Niv Shalmon
   * @since 2016 */
  @SuppressWarnings("unused") interface Axiom {
    static Certain type(final boolean x) {
      return BOOLEAN;
    }

    static Certain type(final byte x) {
      return BYTE;
    }

    static Certain type(final char x) {
      return CHAR;
    }

    static Certain type(final double x) {
      return DOUBLE;
    }

    static Certain type(final float x) {
      return FLOAT;
    }

    static Certain type(final int x) {
      return INT;
    }

    static Certain type(final long x) {
      return LONG;
    }

    static type type(final Object o) {
      return baptize("Object");
    }

    static Certain type(final short x) {
      return SHORT;
    }

    static Certain type(final String x) {
      return STRING;
    }
  }

  static class inner {
    private static String propertyName = "spartan type";
    /** All type that were ever born , as well as all primitive types */
    private static Map<String, implementation> types = new LinkedHashMap<>();

    private static implementation conditionalWithNoInfo(final implementation ¢) {
      return in(¢, BYTE, SHORT, CHAR, INT, INTEGRAL, LONG, FLOAT, NUMERIC) //
          ? NUMERIC //
          : !in(¢, DOUBLE, STRING, BOOLEAN, BOOLEAN) //
              ? NOTHING : ¢;
    }

    private static implementation get(final Expression ¢) {
      return inner.hasType(¢) ? inner.getType(¢) : inner.setType(¢, inner.lookUp(¢, inner.lookDown(¢)));
    }

    /** @param n JD/
     * @return the type information stored inside the node n, or null if there
     *         is none */
    private static implementation getType(final ASTNode ¢) {
      return (implementation) ¢.getProperty(propertyName);
    }

    /** @param ¢ JD
     * @return true if n has a type property and false otherwise */
    private static boolean hasType(final ASTNode ¢) {
      return getType(¢) != null;
    }

    private static implementation lookDown(final Assignment x) {
      final implementation $ = get(x.getLeftHandSide());
      return !$.isNoInfo() ? $ : get(x.getRightHandSide()).isNumeric() ? NUMERIC : get(x.getRightHandSide());
    }

    private static implementation lookDown(final CastExpression ¢) {
      return baptize(step.type(¢) + "");
    }

    private static implementation lookDown(final ClassInstanceCreation ¢) {
      return baptize(¢.getType() + "");
    }

    private static implementation lookDown(final ConditionalExpression x) {
      final implementation $ = get(x.getThenExpression());
      final implementation ¢ = get(x.getElseExpression());
      // If we don't know much about one operand but do know enough about the
      // other, we can still learn something
      return $ == ¢ ? $
          : $.isNoInfo() || ¢.isNoInfo() ? conditionalWithNoInfo($.isNoInfo() ? ¢ : $) //
              : $.isIntegral() && ¢.isIntegral() ? $.underIntegersOnlyOperator(¢) //
                  : $.isNumeric() && ¢.isNumeric() ? $.underNumericOnlyOperator(¢)//
                      : $.isNumeric() && ¢ == ALPHANUMERIC ? $ : ¢.isNumeric() && $ == ALPHANUMERIC ? ¢ : NOTHING; //
    }

    /** @param x JD
     * @return The most specific Type information that can be deduced about the
     *         expression from it's structure, or {@link #NOTHING} if it cannot
     *         decide. Will never return null */
    private static implementation lookDown(final Expression ¢) {
      switch (¢.getNodeType()) {
        case NULL_LITERAL:
          return NULL;
        case CHARACTER_LITERAL:
          return CHAR;
        case STRING_LITERAL:
          return STRING;
        case BOOLEAN_LITERAL:
          return BOOLEAN;
        case NUMBER_LITERAL:
          return lookDown((NumberLiteral) ¢);
        case CAST_EXPRESSION:
          return lookDown((CastExpression) ¢);
        case PREFIX_EXPRESSION:
          return lookDown((PrefixExpression) ¢);
        case INFIX_EXPRESSION:
          return lookDown((InfixExpression) ¢);
        case POSTFIX_EXPRESSION:
          return lookDown((PostfixExpression) ¢);
        case PARENTHESIZED_EXPRESSION:
          return lookDown((ParenthesizedExpression) ¢);
        case CLASS_INSTANCE_CREATION:
          return lookDown((ClassInstanceCreation) ¢);
        case METHOD_INVOCATION:
          return lookDown((MethodInvocation) ¢);
        case CONDITIONAL_EXPRESSION:
          return lookDown((ConditionalExpression) ¢);
        case ASSIGNMENT:
          return lookDown((Assignment) ¢);
        case VARIABLE_DECLARATION_EXPRESSION:
          return lookDown((VariableDeclarationExpression) ¢);
        default:
          return NOTHING;
      }
    }

    private static implementation lookDown(final InfixExpression x) {
      final InfixExpression.Operator o = x.getOperator();
      final List<Expression> es = hop.operands(x);
      assert es.size() >= 2;
      implementation $ = get(first(es));
      chop(es);
      for (final Expression ¢ : es)
        $ = $.underBinaryOperator(o, get(¢));
      return $;
    }

    private static implementation lookDown(final MethodInvocation ¢) {
      return "toString".equals(¢.getName() + "") && ¢.arguments().isEmpty() ? STRING : NOTHING;
    }

    private static implementation lookDown(final NumberLiteral ¢) {
      return new LiteralParser(¢.getToken()).type();
    }

    private static implementation lookDown(final ParenthesizedExpression ¢) {
      return get(core(¢));
    }

    private static implementation lookDown(final PostfixExpression ¢) {
      return get(¢.getOperand()).asNumeric(); // see
                                              // testInDecreamentSemantics
    }

    private static implementation lookDown(final PrefixExpression ¢) {
      return get(¢.getOperand()).under(¢.getOperator());
    }

    private static implementation lookDown(final VariableDeclarationExpression ¢) {
      return baptize(¢.getType() + "");
    }

    private static implementation lookUp(final Expression x, final implementation i) {
      if (i.isCertain())
        return i;
      for (final ASTNode base : hop.ancestors(x)) {
        final ASTNode context = base.getParent();
        if (context != null)
          switch (context.getNodeType()) {
            case INFIX_EXPRESSION:
              return i.aboveBinaryOperator(az.infixExpression(context).getOperator());
            case ARRAY_ACCESS:
              return i.asIntegral();
            case PREFIX_EXPRESSION:
              return i.above(az.prefixExpression(context).getOperator());
            case POSTFIX_EXPRESSION:
              return i.asNumeric();
            case ASSERT_STATEMENT:
              return base.getLocationInParent() != AssertStatement.EXPRESSION_PROPERTY ? i : BOOLEAN;
            case FOR_STATEMENT:
              return base.getLocationInParent() != ForStatement.EXPRESSION_PROPERTY ? i : BOOLEAN;
            // case WHILE_STATEMENT:
            case IF_STATEMENT:
              return BOOLEAN;
            case PARENTHESIZED_EXPRESSION:
              continue;
            default:
              return i;
          }
      }
      return i;
    }

    /** sets the type property in the ASTNode
     * @param n JD
     * @param i the node's type property
     * @return the type property t */
    private static implementation setType(final ASTNode n, final implementation i) {
      // TODO: Alex and Dan: Take a look here to see how you store information
      // within a node
      // TODO: Ori, Matteo this is for you too
      n.setProperty(propertyName, i);
      return i;
    }

    // an interface for inner methods that shouldn't be public
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
       * @return true if one of {@link #CHAR}, {@link BYTE}, {@link SHORT} or
       *         false otherwise. */
      default boolean isIntUnderOperation() {
        return in(this, CHAR, BYTE, SHORT);
      }

      /** @return true if one of {@link #NOTHING}, {@link #BAPTIZED},
       *         {@link #NONNULL}, {@link #VOID}, {@link #NULL} or false
       *         otherwise */
      default boolean isNoInfo() {
        return in(this, NOTHING, NULL);
      }

      @SuppressWarnings("synthetic-access") default implementation join() {
        assert !have(key()) : "Bug: the dictionary should not have type " + key() + "\n receiver is " + this + "\n This is all I know";
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
        final implementation ¢1 = asIntegralUnderOperation();
        final implementation ¢2 = k.asIntegralUnderOperation();
        return in(LONG, ¢1, ¢2) ? LONG : !in(INTEGRAL, ¢1, ¢2) ? INT : INTEGRAL;
      }

      /** @return one of {@link #INT}, {@link #LONG}, {@link #INTEGRAL},
       *         {@link #DOUBLE}, or {@link #NUMERIC}, in case it cannot
       *         decide */
      default implementation underNumericOnlyOperator(final implementation k) {
        if (!isNumeric())
          return asNumericUnderOperation().underNumericOnlyOperator(k);
        assert k != null;
        assert this != ALPHANUMERIC : "Don't confuse " + NUMERIC + " with " + ALPHANUMERIC;
        assert isNumeric() : this + ": is for some reason not numeric ";
        final implementation $ = k.asNumericUnderOperation();
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
      default implementation underPlus(final implementation k) {
        // addition with NULL or String must be a String
        // unless both operands are numeric, the result may be a String
        return in(STRING, this, k) || in(NULL, this, k) ? STRING : !isNumeric() || !k.isNumeric() ? ALPHANUMERIC : underNumericOnlyOperator(k);
      }
    }
  }

  /** Types we do not full understand yet.
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

      private Types(final String description, final String key) {
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
    public enum Certain implements Primitive {
      BOOLEAN("boolean", "must be boolean: !f(), f() || g() ", "Boolean"), //
      BYTE("byte", "must be byte: (byte)1, nothing else", "Byte"), //
      CHAR("char", "must be char: 'a', (char)97, nothing else", "Character"), //
      DOUBLE("double", "must be double: 2.0, 2.0*a()-g(), no 2%a(), no 2*f()", "Double"), //
      FLOAT("float", "must be float: 2f, 2.3f+1, 2F-f()", "Float"), //
      INT("int", "must be int: 2, 2*(int)f(), 2%(int)f(), 'a'*2 , no 2*f()", "Integer"), //
      LONG("long", "must be long: 2L, 2*(long)f(), 2%(long)f(), no 2*f()", "Long"), //
      SHORT("short", "must be short: (short)15, nothing else", "Short"), //
      STRING("String", "must be string: \"\"+a, a.toString(), f()+null, not f()+g()", null), //
      ;
      final String description;
      final String key;

      @SuppressWarnings("synthetic-access") Certain(final String key, final String description, final String s) {
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
        return isIntegral() ? INTEGRAL //
            : isNumeric() ? NUMERIC //
                : isAlphaNumeric() ? ALPHANUMERIC //
                    : this != BOOLEAN ? null : BOOLEANINTEGRAL;
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

    /** Tells how much we know about the type of of a variable, function, or
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
    public enum Uncertain implements Primitive {
      // Doubtful types, from four fold uncertainty down to bilateral
      // schizophrenia" .
      INTEGER("must be either int or long: f()%g()^h()<<f()|g()&h(), not 2+(long)f() ", INT, LONG), //
      INTEGRAL("must be either int or long: f()%g()^h()<<f()|g()&h(), not 2+(long)f() ", INTEGER, CHAR, SHORT, BYTE), //
      NUMERIC("must be either f()*g(), 2L*f(), 2.*a(), not 2 %a(), nor 2", INTEGRAL, FLOAT, DOUBLE), //
      ALPHANUMERIC("only in binary plus: f()+g(), 2 + f(), nor f() + null", NUMERIC, STRING), //
      BOOLEANINTEGRAL("only in x^y,x&y,x|y", BOOLEAN, INTEGRAL), //
      ;
      final String description;
      final Set<Certain> options = new LinkedHashSet<>();

      private Uncertain(final String description, final Primitive... ps) {
        this.description = description;
        for (final Primitive p : ps)
          for (final Certain ¢ : p.options())
            if (!options.contains(¢))
              options.add(¢);
        // TODO: Niv, here is where you should insert yourself into the
        // dictionary.
        // TODO: Yossi, I don't think these types should even be in the
        // dictionary, since baptize should never return these
        // TODO: Niv, the point is that it makes it easy to manage and search
        // for the types in a simpler manner. Not sure we need to treat all
        // types equally, but this would help, would help produce consistent
        // naming etc. Moreover, it would make sure that the description is
        // accurate, no matter how you change the types. If you will check you
        // will find that INTEGER and INTEGRAL, have the same description, which
        // is obviously buggy.
        // TODO: Yossi, But each type inner.types is a concert type that has a
        // concrete name. Since Uncertain/Odd types don't have such names, they
        // don't have any valid key to be put in the dictionary with.
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