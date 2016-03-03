package org.spartan.hamcrest;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

/**
 * Non-auto-boxing version for all primitive types of Hamcrest number comparison
 * matchers in {@link org.hamcrest.number.OrderingComparison}
 *
 * @author Yossi Gil
 * @since 2015-07-18
 */
public class OrderingComparison {
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Boolean> comparesEqualTo(final boolean b) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Byte> comparesEqualTo(final byte b) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Byte.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>char</b></code>.
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Character> comparesEqualTo(final char c) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Character.valueOf(c));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>double</b></code>.
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Double> comparesEqualTo(final double d) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Double.valueOf(d));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>float</b></code>.
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Float> comparesEqualTo(final float f) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Integer> comparesEqualTo(final int i) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Integer.valueOf(i));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>long</b></code>.
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Long> comparesEqualTo(final long l) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Long.valueOf(l));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} for
   * primitive type <code><b>short</b></code>.
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#comparesEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Short> comparesEqualTo(final short s) {
    return org.hamcrest.number.OrderingComparison.comparesEqualTo(Short.valueOf(s));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static Matcher<Boolean> greaterThan(final boolean b) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static Matcher<Byte> greaterThan(final byte b) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Byte.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>char</b></code> .
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static Matcher<Character> greaterThan(final char c) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Character.valueOf(c));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>double</b></code> .
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static Matcher<Double> greaterThan(final double d) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Double.valueOf(d));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>float</b></code> .
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static Matcher<Float> greaterThan(final float f) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static Matcher<Integer> greaterThan(final int i) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Integer.valueOf(i));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>long</b></code> .
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static Matcher<Long> greaterThan(final long l) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Long.valueOf(l));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThan} for primitive
   * type <code><b>short</b></code> .
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThan} to the
   *         parameter
   */
  @Factory public static Matcher<Short> greaterThan(final short s) {
    return org.hamcrest.number.OrderingComparison.greaterThan(Short.valueOf(s));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo} for
   * primitive type <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static Matcher<Boolean> greaterThanOrEqualTo(final boolean b) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo} for
   * primitive type <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static Matcher<Byte> greaterThanOrEqualTo(final byte b) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Byte.valueOf(b));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>char</b></code> .
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static Matcher<Character> greaterThanOrEqualTo(final char c) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Character.valueOf(c));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>double</b></code> .
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static Matcher<Double> greaterThanOrEqualTo(final double d) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Double.valueOf(d));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>float</b></code> .
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static Matcher<Float> greaterThanOrEqualTo(final float f) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo} for
   * primitive type <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static Matcher<Integer> greaterThanOrEqualTo(final int i) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Integer.valueOf(i));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>long</b></code> .
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static Matcher<Long> greaterThanOrEqualTo(final long l) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Long.valueOf(l));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   * for primitive type <code><b>short</b></code> .
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#greaterThanOrEqualTo}
   *         to the parameter
   */
  @Factory public static Matcher<Short> greaterThanOrEqualTo(final short s) {
    return org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo(Short.valueOf(s));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThan} for primitive type
   * <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static Matcher<Boolean> lessThan(final boolean b) {
    return org.hamcrest.number.OrderingComparison.lessThan(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThan} for primitive type
   * <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static Matcher<Byte> lessThan(final byte b) {
    return org.hamcrest.number.OrderingComparison.lessThan(Byte.valueOf(b));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>char</b></code>.
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static Matcher<Character> lessThan(final char c) {
    return org.hamcrest.number.OrderingComparison.lessThan(Character.valueOf(c));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>double</b></code> .
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static Matcher<Double> lessThan(final double d) {
    return org.hamcrest.number.OrderingComparison.lessThan(Double.valueOf(d));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>float</b></code>.
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static Matcher<Float> lessThan(final float f) {
    return org.hamcrest.number.OrderingComparison.lessThan(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThan} for primitive type
   * <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static Matcher<Integer> lessThan(final int i) {
    return org.hamcrest.number.OrderingComparison.lessThan(Integer.valueOf(i));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>long</b></code>.
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static Matcher<Long> lessThan(final long l) {
    return org.hamcrest.number.OrderingComparison.lessThan(Long.valueOf(l));
  }
  /**
   * A non - auto - boxing wrapper of the original (auto-boxing) Hamcrest
   * matcher {@link org.hamcrest.number.OrderingComparison#lessThan} for
   * primitive type <code><b>short</b></code>.
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThan} to the
   *         parameter
   */
  @Factory public static Matcher<Short> lessThan(final short s) {
    return org.hamcrest.number.OrderingComparison.lessThan(Short.valueOf(s));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>boolean</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Boolean> lessThanOrEqualTo(final boolean b) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Boolean.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>byte</b></code>.
   *
   * @param b JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Byte> lessThanOrEqualTo(final byte b) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Byte.valueOf(b));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>char</b></code>.
   *
   * @param c JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Character> lessThanOrEqualTo(final char c) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Character.valueOf(c));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>double</b></code>.
   *
   * @param d JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Double> lessThanOrEqualTo(final double d) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Double.valueOf(d));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>float</b></code>.
   *
   * @param f JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Float> lessThanOrEqualTo(final float f) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Float.valueOf(f));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>int</b></code>.
   *
   * @param i JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Integer> lessThanOrEqualTo(final int i) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Integer.valueOf(i));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>long</b></code>.
   *
   * @param l JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Long> lessThanOrEqualTo(final long l) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Long.valueOf(l));
  }
  /**
   * A non-auto-boxing wrapper of the original (auto-boxing) Hamcrest matcher
   * {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} for
   * primitive type <code><b>short</b></code>.
   *
   * @param s JD
   * @return the result of applying
   *         {@link org.hamcrest.number.OrderingComparison#lessThanOrEqualTo} to
   *         the parameter
   */
  @Factory public static Matcher<Short> lessThanOrEqualTo(final short s) {
    return org.hamcrest.number.OrderingComparison.lessThanOrEqualTo(Short.valueOf(s));
  }
}
