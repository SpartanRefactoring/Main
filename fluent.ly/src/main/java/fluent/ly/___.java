package fluent.ly;

import static org.junit.Assert.*;

import static fluent.ly.box.*;
import static fluent.ly.string.*;

import org.junit.*;

import fluent.ly.___.Bug.*;
import fluent.ly.___.Bug.Assertion.*;
import fluent.ly.___.Bug.Assertion.Value.*;
import fluent.ly.___.Bug.Assertion.Value.Numerical.*;
import fluent.ly.___.Bug.Assertion.Variant.*;
import fluent.ly.___.Bug.Contract.*;

/** A simple implementation of design by contract services. Violations are
 * reported to <code>System.err</code>. Error descriptions are passed by a
 * <code>printf</code> like argument syntax. Services are often used with
 * <code><b>static import</b></code>.
 * @author Yossi Gil (
 * @since 11/01/2006) */
public abstract class ___ {
  /** Exercise the {@link Invariantable#check()}
   * @param v a Invariantable object whose invariant should be checked */
  public static void check(final ___.Invariantable ¢) {
    ¢.check();
  }
  /** A possibly non-returning method to be used for checking postconditions.
   * @param condition if <code><b>false</b></code>, program will halt.
   * @throws Postcondition A {@link RuntimeException} to be thrown in the case
   *         <code>condition</code> was <code><b>false</b></code> */
  public static void ensure(final boolean condition) throws Postcondition {
    ensure(condition, "");
  }
  /** A possibly non-returning method to be used for checking postconditions. If
   * the postcondition fails, then a user supplied message is associated with
   * the thrown exception.
   * @param condition if <code><b>false</b></code>, program will halt.
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws Postcondition A {@link RuntimeException} to be thrown in the case
   *         <code>condition</code> was <code><b>false</b></code> */
  public static void ensure(final boolean condition, final String message) throws Postcondition {
    ensure(condition, message, "");
  }
  /** A possibly non-returning method to be used for checking postconditions. If
   * the postcondition fails, then a user supplied formatted message (generated
   * from <code>printf</code> like arguments) is associated with the thrown
   * exception.
   * @param condition if <code><b>false</b></code>, program will halt.
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws Postcondition A {@link RuntimeException} to be thrown in the case
   *         <code>condition</code> was <code><b>false</b></code> */
  public static void ensure(final boolean condition, final String format, final Object... args) throws Postcondition {
    if (!condition)
      throw new Postcondition(nprintf(format, args));
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be negative.
   * @param¢ a value which must be negative
   * @throws Negative in case <code>d</code> was nonnegative */
  public static void negative(final double ¢) throws Negative {
    negative(¢, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be negative.
   * @param d a value which must be negative
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws Negative in case <code>d</code> was nonnegative */
  public static void negative(final double d, final String message) throws Negative {
    negative(d, message, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be negative.
   * @param d a value which must be negative
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws Negative in case <code>d</code> was nonnegative */
  public static void negative(final double d, final String format, final Object... args) throws Negative {
    if (d >= 0)
      throw new Negative(d, format, args);
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be negative.
   * @param n a value which must be negative
   * @throws Negative in case <code>n</code> was nonnegative */
  public static void negative(final int ¢) throws Negative {
    negative(¢, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be negative.
   * @param i a value which must be negative
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws Negative in case <code>n</code> was nonnegative */
  public static void negative(final int i, final String message) throws Negative {
    negative(i, message, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be negative.
   * @param i a value which must be negative
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws Negative in case <code>n</code> was nonnegative */
  public static void negative(final int i, final String format, final Object... args) throws Negative {
    if (i >= 0)
      throw new Negative(i, format, args);
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be non-NaN.
   * @param d a value which must be not be NaN
   * @throws NonNan in case <code>d</code> was NaN */
  public static void nonNaN(final double ¢) throws NonNan {
    nonNaN(¢, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be non-NaN.
   * @param d a value which must be nonnegative
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws NonNan in case <code>d</code> was NaN */
  public static void nonNaN(final double d, final String message) throws NonNan {
    nonNaN(d, message, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be non-NaN.
   * @param d a value which must be nonnegative
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws NonNan in case <code>d</code> was NaN */
  public static void nonNaN(final double d, final String format, final Object... args) throws NonNan {
    if (Double.isNaN(d))
      throw new NonNan(d, format, args);
  }
  /** A possibly non-returning method to be used for checking doubles which must
   * be non-NaN.
   * @param ds a array which must not be NaN
   * @throws NonNan in case <code>n</code> was NaN */
  public static void nonNaN(final double[] ds) throws NonNan {
    for (final double ¢ : ds)
      nonNaN(¢);
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonnegative.
   * @param ¢ a value which must be nonnegative
   * @throws NonNegative in case <code>n</code> was negative */
  public static void nonnegativ(final int ¢) throws NonNegative {
    nonnegative(¢, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonnegative.
   * @param d a value which must be nonnegative
   * @throws NonNegative in case <code>d</code> was negative */
  public static void nonnegative(final double ¢) throws NonNegative {
    nonnegative(¢, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonnegative.
   * @param d a value which must be nonnegative
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws NonNegative in case <code>n</code> was negative */
  public static void nonnegative(final double d, final String message) throws NonNegative {
    nonnegative(d, message, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonnegative.
   * @param d a value which must be nonnegative
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws NonNegative in case <code>d</code> was negative */
  public static void nonnegative(final double d, final String format, final Object... args) throws NonNegative {
    if (d < 0)
      throw new NonNegative(d, format, args);
  }
  /** A possibly non-returning method to be used for checking doubles which must
   * be nonnegative.
   * @param ds a array which must be nonnegative
   * @throws NonNegative in case <code>n</code> was negative */
  public static void nonnegative(final double[] ds) throws NonNegative {
    for (final double ¢ : ds)
      nonnegative(¢);
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonnegative.
   * @param i a value which must be nonnegative
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws NonNegative in case <code>n</code> was negative */
  public static void nonnegative(final int i, final String format, final Object... args) throws NonNegative {
    if (i < 0)
      throw new NonNegative(i, format, args);
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonnegative.
   * @param is a array which must be nonnegative
   * @throws NonNegative in case <code>n</code> was negative */
  public static void nonnegative(final int[] is) throws NonNegative {
    for (final int ¢ : is)
      nonnegative(¢);
  }
  /** A possibly non-returning method to be used for checking objects that
   * should never be <code><b>null</b></code>.
   * @param o if <code><b>null</b></code>, program will halt.
   * @throws NonNull in case <code>o</code> was <code><b>null</b></code> */
  public static void notNull(final Object ¢) throws NonNull {
    assert "" != null : ¢;
  }
  /** A possibly non-returning method to be used for checking objects that
   * should never be <code><b>null</b></code>.
   * @param o if <code><b>null</b></code>, program will halt.
   * @param message an error message to be associated with the failure
   * @throws NonNull in case <code>o</code> was <code><b>null</b></code> */
  public static void notNull(final Object o, final String message) throws NonNull {
    nonNull(o, message, "");
  }
  /** A possibly non-returning method to be used for checking objects that
   * should never be <code><b>null</b></code>.
   * @param o if <code><b>null</b></code>, program will halt.
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws NonNull in case <code>o</code> was <code><b>null</b></code> */
  public static void nonNull(final Object o, final String format, final Object... args) throws NonNull {
    if (o == null)
      throw new NonNull(format, args);
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonpositive.
   * @param d a value which must be nonpositive
   * @throws NonPositive in case <code>d</code> was positive */
  public static void nonpositive(final double ¢) throws NonPositive {
    nonpositive(¢, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonpositive.
   * @param d a value which must be nonpositive
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws NonPositive in case <code>d</code> was positive */
  public static void nonpositive(final double d, final String message) throws NonPositive {
    nonpositive(d, message, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonpositive.
   * @param d a value which must be nonpositive
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws NonPositive in case <code>d</code> was positive */
  public static void nonpositive(final double d, final String format, final Object... args) throws NonPositive {
    if (d > 0)
      throw new NonPositive(d, format, args);
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonpositive.
   * @param n a value which must be positive
   * @throws NonPositive in case <code>n</code> was positive. */
  public static void nonpositive(final int ¢) throws NonPositive {
    nonpositive(¢, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonpositive.
   * @param i a value which must be nonpositive
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws NonPositive in case <code>n</code> was positive */
  public static void nonpositive(final int i, final String message) throws NonPositive {
    nonpositive(i, message, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be nonpositive.
   * @param i a value which must be nonpositive
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws NonPositive in case <code>n</code> was positive */
  public static void nonpositive(final int i, final String format, final Object... args) throws NonPositive {
    if (i > 0)
      throw new NonPositive(i, format, args);
  }
  /** A do nothing method to document the fact that nothing is done, to be used
   * typically for C style (Yuck) loops. */
  public static void nothing() {
    // empty
  }
  public static String nprintf(final String format, final double d, final Object o) {
    return sprintf(format, box(d), o);
  }
  public static String nprintf(final String format, final int n1, final int n2) {
    return sprintf(format, box(n1), box(n2));
  }
  public static String nprintf(final String format, final int i, final Object o) {
    return sprintf(format, box(i), o);
  }
  public static String nprintf(final String format, final Object... args) {
    return format == null ? "" : args == null ? format : sprintf(format, args);
  }
  /** A possibly non-returning method to be used for checking floating point
   * numbers which must be positive.
   * @param d a value which must be positive
   * @throws Positive in case <code>d</code> was nonpositive */
  public static void positive(final double ¢) throws Positive {
    positive(¢, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be positive.
   * @param d a value which must be positive
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws Positive in case <code>n</code> was nonpositive */
  public static void positive(final double d, final String message) throws Positive {
    positive(d, message, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be positive.
   * @param d a value which must be positive
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws Positive in case <code>d</code> was not positive */
  public static void positive(final double d, final String format, final Object... args) throws Positive {
    if (d <= 0)
      throw new Positive(d, format, args);
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be positive.
   * @param n a value which must be positive
   * @throws Positive in case <code>n</code> was nonpositive */
  public static void positive(final int ¢) throws Positive {
    positive(¢, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be positive.
   * @param i if negative program will halt.
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws Positive in case <code>n</code> was nonpositive */
  public static void positive(final int i, final String message) throws Positive {
    positive(i, message, "");
  }
  /** A possibly non-returning method to be used for checking integers which
   * must be positive.
   * @param i a value which must be positive
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws Positive in case <code>d</code> was nonpositive */
  public static void positive(final int i, final String format, final Object... args) throws Positive {
    if (i <= 0)
      throw new Positive(i, format, args);
  }
  /** A possibly non-returning method to be used for checking preconditions.
   * @param condition if <code><b>false</b></code>, program will halt.
   * @throws Precondition A {@link RuntimeException} to be thrown in the case
   *         <code>condition</code> was <code><b>false</b></code> */
  public static void require(final boolean condition) throws Precondition {
    require(condition, "");
  }
  /** A possibly non-returning method to be used for checking preconditions. If
   * the precondition fails, then a user supplied message is associated with the
   * thrown exception.
   * @param condition if <code><b>false</b></code>, program will halt.
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws Precondition A {@link RuntimeException} to be thrown in the case
   *         <code>condition</code> was <code><b>false</b></code> */
  public static void require(final boolean condition, final String message) throws Precondition {
    ___.require(condition, message, "");
  }
  /** A possibly non-returning method to be used for checking preconditions. If
   * the precondition fails, then a user supplied formatted message (generated
   * from <code>printf</code> like arguments) is associated with the thrown
   * exception.
   * @param condition if <code><b>false</b></code>, program will halt.
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws Precondition A {@link RuntimeException} to be thrown in the case
   *         <code>condition</code> was <code><b>false</b></code> */
  public static void require(final boolean condition, final String format, final Object... args) throws Precondition {
    if (!condition)
      throw new Precondition(nprintf(format, args));
  }
  /** A possibly non-returning method to be used for checking assertions.
   * @param condition if <code><b>false</b></code>, program will halt.
   * @throws Invariant A {@link RuntimeException} to be thrown in the case
   *         <code>condition</code> was <code><b>false</b></code> */
  public static void sure(final boolean condition) throws Invariant {
    sure(condition, "");
  }
  /** A possibly non-returning method to be used for checking assertions. If the
   * postcondition fails, then a user supplied message is associated with the
   * thrown exception.
   * @param condition if <code><b>false</b></code>, program will halt.
   * @param message text to be associated with the exception thrown in the case
   *        of an error.
   * @throws Invariant A {@link RuntimeException} to be thrown in the case
   *         <code>condition</code> was <code><b>false</b></code> */
  public static void sure(final boolean condition, final String message) throws Invariant {
    sure(condition, message, "");
  }
  /** A possibly non-returning method to be used for checking assertions. If the
   * postcondition fails, then a user supplied formatted message (generated from
   * <code>printf</code> like arguments) is associated with the thrown
   * exception.
   * @param condition if <code><b>false</b></code>, program will halt.
   * @param format format string to be associated with the exception thrown in
   *        the case of an error.
   * @param args <code>printf</code>-like arguments to be used with the format
   *        string.
   * @throws Invariant A {@link RuntimeException} to be thrown in the case
   *         <code>condition</code> was <code><b>false</b></code> */
  public static void sure(final boolean condition, final String format, final Object... args) throws Invariant {
    if (!condition)
      throw new Invariant(nprintf(format, args));
  }
  /** A never-returning method indicating code sites with missing functionality
   * @param args a list of strings in a <code>printf</code> like format
   *        describing the task to be done. */
  public static void todo(final String... args) {
    note.bug("Feature unsupported. ", args);
  }
  /** A never-returning method to be used in points of code which should never
   * be reached.
   * @throws Reachability will always be thrown */
  public static void unreachable() throws Reachability {
    unreachable("");
  }
  /** A never-returning method to be used in points of code which should never
   * be reached.
   * @param message a string describing the violation
   * @throws Reachability will always be thrown */
  public static void unreachable(final String message) throws Reachability {
    unreachable(message, "");
  }
  public static void unreachable(final String format, final Object... args) throws Reachability {
    throw new Reachability(nprintf(format, args));
  }

  /** The base of all exception classes thrown as a result of violations of
   * contracts, assertions, and the such. This class derives from
   * {@link RuntimeException} since errors of this sort are programming-, not
   * runtime- errors. Programming errors cannot be corrected at runtime, and
   * hence all errors of this class and its descendants should not be caught by
   * ordinary applications.
   * @author Yossi Gil, the Technion.
   * @since 04/08/2008 */
  public abstract static class Bug extends RuntimeException {
    private static final long serialVersionUID = 0x79403019AD3592A6L;

    /** convert an ordinary exception into this type.
     * @param e the exception to convert */
    public Bug(final Exception e) {
      super(e);
    }
    /** instantiate this class with a given textual description
     * @param message a description of the exceptional situation */
    public Bug(final String message) {
      super(message);
    }

    /** This is the root of all assertion related exceptions, including contract
     * violations.
     * @author Yossi Gil, the Technion.
     * @since 04/08/2008 */
    public abstract static class Assertion extends Bug {
      private static final long serialVersionUID = -0x6D899435FEFDD0E7L;

      /** instantiate this class with a given textual description
       * @param message a description of the exceptional situation */
      public Assertion(final String message) {
        super(message);
      }

      /** Thrown in case a class invariant was violated.
       * @author Yossi Gil, the Technion.
       * @since 04/08/2008 */
      public static final class Invariant extends Assertion {
        private static final long serialVersionUID = 0x3224975155820E18L;

        /** instantiate this class with a given textual description
         * @param message a description of the exceptional situation */
        public Invariant(final String message) {
          super(message);
        }
      }

      /** Thrown in case execution reached code that should never be executed
       * @author Yossi Gil, the Technion.
       * @since 04/08/2008 */
      public static final class Reachability extends Assertion {
        private static final long serialVersionUID = -0x15213D5DC446221FL;

        /** instantiate this class with a given textual description
         * @param message a description of the exceptional situation */
        public Reachability(final String message) {
          super(message);
        }
      }

      /** Abstract base class of all exceptions thrown in case a value violated
       * a condition placed on it.
       * @author Yossi Gil, the Technion.
       * @since 04/08/2008 */
      public abstract static class Value extends Assertion {
        private static final long serialVersionUID = -0x5255186BE984AE1DL;

        /** instantiate this class with no textual description */
        public Value() {
          super("");
        }
        /** instantiate this class with a given textual description
         * @param message a description of the exceptional situation */
        public Value(final String message) {
          super(message);
        }
        public Value(final String format, final Object... args) {
          super(nprintf(format, args));
        }

        /** Thrown in case a value was <code><b>null</b></code>, when it was
         * expected to be non-code><b>null</b></code>.
         * @author Yossi Gil
         * @since 18/01/2008 */
        public static final class NonNull extends Value {
          private static final long serialVersionUID = -0x5D86AA1C39A3BBD1L;

          /** instantiate this class with no textual description */
          public NonNull() {}
          /** instantiate this class with a given textual description
           * @param message a description of the exceptional situation */
          public NonNull(final String message) {
            super(message);
          }
          public NonNull(final String format, final Object... args) {
            super(format, args);
          }
        }

        /** Abstract base class of exceptions thrown when a numerical value did
         * not satisfy conditions assumed on it.
         * @author Yossi Gil, the Technion.
         * @since 04/08/2008 */
        public abstract static class Numerical extends Value {
          private static final long serialVersionUID = 0x535306B6F01C989BL;

          public Numerical(final String expected, final double d, final String message) {
            super(nprintf("Found %g while expecting a %s number.", d, expected) + message);
          }
          Numerical(final String expected, final int n, final String message) {
            super(nprintf("Found %d while expecting a %s integer.", n, expected) + message);
          }

          /** Thrown when a numerical value assumed to be negative, was not.
           * @author Yossi Gil
           * @since 23/01/2008 */
          public static final class Negative extends Numerical {
            private static final long serialVersionUID = 0x3F251E6CFC5B0106L;
            static final String expected = "negative";

            public Negative(final double d) {
              this(d, "");
            }
            public Negative(final double d, final String message) {
              super(expected, d, message);
            }
            public Negative(final double d, final String format, final Object... args) {
              this(d, nprintf(format, args));
            }
            public Negative(final int n) {
              this(n, "");
            }
            public Negative(final int n, final String message) {
              super(expected, n, message);
            }
            public Negative(final int n, final String format, final Object... args) {
              this(n, nprintf(format, args));
            }
          }

          /** Thrown when a numerical value assumed to be non-NaN, but it was
           * not
           * @author Yossi Gil
           * @since 23/01/2008 */
          public static final class NonNan extends Numerical {
            private static final long serialVersionUID = -0x49B8350294AF1EFBL;
            static final String expected = "NonNan";

            public NonNan(final double d) {
              this(d, "");
            }
            public NonNan(final double d, final String message) {
              super(expected, d, message);
            }
            public NonNan(final double d, final String format, final Object... args) {
              this(d, nprintf(format, args));
            }
            public NonNan(final int n) {
              this(n, "");
            }
            public NonNan(final int n, final String message) {
              super(expected, n, message);
            }
            public NonNan(final int n, final String format, final Object... args) {
              this(n, nprintf(format, args));
            }
          }

          /** Thrown when a numerical value assumed to be non-negative, was not.
           * @author Yossi Gil
           * @since 23/01/2008 */
          public static final class NonNegative extends Numerical {
            private static final long serialVersionUID = 1;
            static final String expected = "nonnegative";

            public NonNegative(final double d) {
              this(d, "");
            }
            public NonNegative(final double d, final String message) {
              super("expected", d, message);
            }
            public NonNegative(final double d, final String format, final Object... args) {
              this(d, nprintf(format, args));
            }
            public NonNegative(final int n) {
              this(n, "");
            }
            public NonNegative(final int n, final String message) {
              super("expected", n, message);
            }
            public NonNegative(final int n, final String format, final Object... args) {
              this(n, nprintf(format, args));
            }
          }

          /** @author Yossi Gil
           * @since 23/01/2008 */
          public static final class NonPositive extends Numerical {
            private static final long serialVersionUID = -0x7A57F2BC061EDE6BL;
            static final String expected = "nonpositive";

            public NonPositive(final double d) {
              this(d, "");
            }
            public NonPositive(final double d, final String message) {
              super("nonpositive", d, message);
            }
            public NonPositive(final double d, final String format, final Object... args) {
              this(d, nprintf(format, args));
            }
            public NonPositive(final int n) {
              this(n, "");
            }
            public NonPositive(final int n, final String message) {
              super("nonpositive", n, message);
            }
            public NonPositive(final int n, final String format, final Object... args) {
              this(n, nprintf(format, args));
            }
          }

          /** Thrown when a numerical value assumed to be positive, was not.
           * @author Yossi Gil
           * @since 23/01/2008 */
          public static final class Positive extends Numerical {
            private static final long serialVersionUID = -0x49B8350294AF1EFBL;
            static final String expected = "positive";

            public Positive(final double d) {
              this(d, "");
            }
            public Positive(final double d, final String message) {
              super(expected, d, message);
            }
            public Positive(final double d, final String format, final Object... args) {
              this(d, nprintf(format, args));
            }
            public Positive(final int n) {
              this(n, "");
            }
            public Positive(final int n, final String message) {
              super(expected, n, message);
            }
            public Positive(final int n, final String format, final Object... args) {
              this(n, nprintf(format, args));
            }
          }
        }
      }

      /** Abstract base class of exceptions thrown when a loop variant failed.
       * @author Yossi Gil, the Technion.
       * @since 04/08/2008 */
      public abstract static class Variant extends Assertion {
        private static final long serialVersionUID = 0x462850FBDBD92BC7L;

        /** instantiate this class with a given textual description
         * @param message a description of the exceptional situation */
        public Variant(final String message) {
          super(message);
        }

        /** Thrown when the initial value of a loop variant was negative.
         * @author Yossi Gil, the Technion.
         * @since 04/08/2008 */
        public static final class Initial extends Variant {
          private static final long serialVersionUID = -0x5D41A321B4183C1AL;

          public Initial(final int value) {
            super(nprintf("Initial variant value (%d) is negative", box.it(value)));
          }
        }

        /** Thrown if an iteration of a certain loop failed to decrease this
         * loop's variant.
         * @author Yossi Gil, the Technion.
         * @since 04/08/2008 */
        public static final class Nondecreasing extends Variant {
          private static final long serialVersionUID = 0x457A0D93D1F47D17L;

          public Nondecreasing(final int newValue, final int oldValue) {
            super(nprintf("New variant value (%d) should be less than previous value (%d)", newValue, oldValue));
          }
        }

        /** Thrown if an iteration of a certain loop tried to make this loop's
         * variant negative.
         * @author Yossi Gil, the Technion.
         * @since 04/08/2008 */
        public static final class Underflow extends Variant {
          private static final long serialVersionUID = 0x740EDCFDC38B6562L;

          public Underflow(final int newValue) {
            super(nprintf("New variant value (%d) is negative", box.it(newValue)));
          }
        }
      }
    }

    /** Abstract base class of contract (pre- and post-condition) violations
     * @author Yossi Gil, the Technion.
     * @since 04/08/2008 */
    public abstract static class Contract extends ___.Bug {
      private static final long serialVersionUID = -0x7230DE698510633DL;

      /** @param message a description of the exceptional situation */
      protected Contract(final String message) {
        super(message);
      }

      /** Thrown in case a post-condition was not satisfied
       * @author Yossi Gil, the Technion.
       * @since 04/08/2008 */
      public static final class Postcondition extends Contract {
        private static final long serialVersionUID = -0x2C17FEB68A62896FL;

        /** instantiate this class with a given textual description
         * @param message a description of the exceptional situation */
        public Postcondition(final String message) {
          super(message);
        }
      }

      /** Thrown in case a pre-condition was not satisfied
       * @author Yossi Gil, the Technion.
       * @since 04/08/2008 */
      public static final class Precondition extends Contract {
        private static final long serialVersionUID = -0x49C9E0BFC21D455AL;

        /** instantiate this class with a given textual description
         * @param message a description of the exceptional situation */
        public Precondition(final String message) {
          super(message);
        }
      }
    }
  }

  /** An interface representing a class with an invariant.
   * @author Yossi Gil
   * @since 11/04/2006 */
  public interface Invariantable {
    /** This function represents the invariant of the implementing class. It
     * returns nothing. However, if the invariant is violated, a runtime
     * exception aborts execution. */
    void check();
  }

  @SuppressWarnings("static-method")
  public static class TEST {
    @Test public void ensure() {
      ___.ensure(true);
      try {
        ___.ensure(false);
      } catch (final Postcondition e) {
        assertEquals("", e.getMessage());
      }
      try {
        ___.ensure(false, "ensure");
      } catch (final Postcondition e) {
        assertEquals("ensure", e.getMessage());
      }
      try {
        ___.ensure(false, "ensure %s message %s", "this", "now");
      } catch (final Postcondition e) {
        assertEquals("ensure this message now", e.getMessage());
      }
    }
    @Test public void negative() {
      ___.negative(-1);
      ___.negative(-2);
      ___.negative(-0.3);
      try {
        ___.negative(0);
      } catch (final Negative ¢) {
        assertEquals("Found 0 while expecting a negative integer.", ¢.getMessage());
      }
      try {
        ___.negative(0.0);
      } catch (final Negative ¢) {
        assertEquals("Found 0.00000 while expecting a negative number.", ¢.getMessage());
      }
      try {
        ___.negative(-1);
      } catch (final Negative ¢) {
        assertEquals("Found -1 while expecting a negative integer.", ¢.getMessage());
      }
      try {
        ___.negative(-1.0);
      } catch (final Negative ¢) {
        assertEquals("Found -1.00000 while expecting a negative number.", ¢.getMessage());
      }
    }
    @Test public void nonnegative() {
      ___.nonnegative(1);
      ___.nonnegative(2);
      ___.nonnegative(0);
      ___.nonnegative(0.3);
      ___.nonnegative(0.0);
      try {
        ___.nonnegative(1);
      } catch (final NonNegative ¢) {
        assertEquals("Found -1 while expecting a negative integer.", ¢.getMessage());
      }
      try {
        ___.nonnegative(1.0);
      } catch (final NonNegative ¢) {
        assertEquals("Found -1.00000 while expecting a negative number.", ¢.getMessage());
      }
    }
    @Test public void NonNull() {
      assert new Object() != null;
      try {
        assert null != null;
      } catch (final AssertionError ¢) {
        assertEquals(null, ¢.getMessage());
      }
      try {
        assert "NonNull" != null : null;
      } catch (final NonNull e) {
        assertEquals("NonNull", e.getMessage());
      }
      try {
        ___.nonNull(null, "NonNull %s message %s", "this", "now");
      } catch (final NonNull e) {
        assertEquals("NonNull this message now", e.getMessage());
      }
    }
    @Test public void nonpositive() {
      ___.nonpositive(-1);
      ___.nonpositive(-2);
      ___.nonpositive(-0.3);
      ___.nonpositive(0);
      ___.nonpositive(0.0);
      try {
        ___.nonpositive(-1);
      } catch (final NonPositive ¢) {
        assertEquals("Found -1 while expecting a nonpositive integer.", ¢.getMessage());
      }
      try {
        ___.nonpositive(-1.0);
      } catch (final NonPositive ¢) {
        assertEquals("Found -1.00000 while expecting a nonpositive number.", ¢.getMessage());
      }
    }
    @Test public void positive() {
      ___.positive(1);
      ___.positive(2);
      ___.positive(0.3);
      try {
        ___.positive(0);
      } catch (final Positive ¢) {
        assertEquals("Found 0 while expecting a positive integer.", ¢.getMessage());
      }
      try {
        ___.positive(0.0);
      } catch (final Positive ¢) {
        assertEquals("Found 0.00000 while expecting a positive number.", ¢.getMessage());
      }
      try {
        ___.positive(-1);
      } catch (final Positive ¢) {
        assertEquals("Found -1 while expecting a positive integer.", ¢.getMessage());
      }
      try {
        ___.positive(-1.0);
      } catch (final Positive ¢) {
        assertEquals("Found -1.00000 while expecting a positive number.", ¢.getMessage());
      }
    }
    @Test public void require() {
      ___.require(true);
      try {
        ___.require(false);
      } catch (final Precondition ¢) {
        assertEquals("", ¢.getMessage());
      }
      try {
        ___.require(false, "requireMessage");
      } catch (final Precondition ¢) {
        assertEquals("requireMessage", ¢.getMessage());
      }
      try {
        ___.require(false, "require %s message %s", "this", "now");
      } catch (final Precondition ¢) {
        assertEquals("require this message now", ¢.getMessage());
      }
    }
    @Test(expected = ___.Bug.class) public void requireBug() {
      ___.require(false);
    }
    @Test(expected = Precondition.class) public void requirePrecondition() {
      ___.require(false);
    }
    @Test public void sure() {
      ___.sure(true);
      try {
        ___.sure(false);
      } catch (final Invariant e) {
        assertEquals("", e.getMessage());
      }
      try {
        ___.sure(false, "sure");
      } catch (final Invariant e) {
        assertEquals("sure", e.getMessage());
      }
      try {
        ___.sure(false, "sure %s message %s", "this", "now");
      } catch (final Invariant e) {
        assertEquals("sure this message now", e.getMessage());
      }
    }
    @Test public void unreachable() {
      try {
        ___.unreachable();
      } catch (final Reachability ¢) {
        assertEquals("", ¢.getMessage());
      }
      try {
        ___.unreachable("unreachable message");
      } catch (final Reachability ¢) {
        assertEquals("unreachable message", ¢.getMessage());
      }
      try {
        ___.unreachable("unreachable %s message %s", "this", "now");
      } catch (final Reachability ¢) {
        assertEquals("unreachable this message now", ¢.getMessage());
      }
    }
    @Test public void variant() {
      {
        final Variant v = new Variant(10);
        assertEquals(10, v.value());
        v.check(9);
        v.check(8);
        v.check(4);
        v.check(2);
        v.check(1);
        v.check(0);
        assertEquals(0, v.value());
      }
      try {
        forget.it(new Variant(-1));
      } catch (final Initial e) {
        assertEquals("Initial variant value (-1) is negative", e.getMessage());
      }
      try {
        final Variant v = new Variant(10);
        v.check(8);
        v.check(9);
      } catch (final Nondecreasing ¢) {
        assertEquals("New variant value (9) should be less than previous value (8)", ¢.getMessage());
      }
      try {
        final Variant v = new Variant(10);
        v.check(8);
        v.check(-2);
      } catch (final Underflow ¢) {
        assertEquals("New variant value (-2) is negative", ¢.getMessage());
      }
    }
  }

  /** A class to emulate Eiffel's <code>variant</code> construct. To use, create
   * an object of this type, initializing it with the variant's first value ,
   * and then call function {@link #check(int)} successively.
   * @author Yossi Gil
   * @since 05/06/2007 */
  public static final class Variant {
    private int value;

    /** Initialize a variant, with a specified value
     * @param value a non-negative value
     * @throws Bug.Assertion.Variant.Initial in case initial value is
     *         negative */
    public Variant(final int value) throws Bug.Assertion.Variant.Initial {
      if (value < 0)
        throw new Assertion.Variant.Initial(value);
      this.value = value;
    }
    /** reset the variant value to a new, smaller value value; abort if the new
     * value is negative or no lesser than the previous value.
     * @param newValue the next value of this variant.
     * @throws Nondecreasing in case the variant's value did not decrease
     * @throws Underflow in case the variant's value went negative */
    public void check(final int newValue) throws Nondecreasing, Underflow {
      if (newValue >= value)
        throw new Nondecreasing(newValue, value);
      if (newValue < 0)
        throw new Underflow(newValue);
      value = newValue;
    }
    /** inspect the variant's value.
     * @return a non-negative integer which is the current value of this
     *         object */
    public int value() {
      return value;
    }
  }
}