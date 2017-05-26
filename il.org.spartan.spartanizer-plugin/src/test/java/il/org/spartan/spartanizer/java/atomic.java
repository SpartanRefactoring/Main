package il.org.spartan.spartanizer.java;

/** Use overloading to determine the __ of an expression statically.
 * @author Yossi Gil
 * @since 2016 */
@SuppressWarnings({ "unused", "BooleanParameter" }) //
public interface atomic {
  static boolean isBoolean(final boolean __) {
    return true;
  }
  static boolean isBoolean(final byte __) {
    return false;
  }
  static boolean isBoolean(final char __) {
    return false;
  }
  static boolean isBoolean(final double __) {
    return false;
  }
  static boolean isBoolean(final float __) {
    return false;
  }
  static boolean isBoolean(final Object __) {
    return false;
  }
  static boolean isBoolean(final short __) {
    return false;
  }
  static boolean isBoolean(final String __) {
    return false;
  }
  static boolean isByte(final boolean __) {
    return false;
  }
  static boolean isByte(final byte __) {
    return true;
  }
  static boolean isByte(final char __) {
    return false;
  }
  static boolean isByte(final double __) {
    return false;
  }
  static boolean isByte(final float __) {
    return false;
  }
  static boolean isByte(final int __) {
    return false;
  }
  static boolean isByte(final long __) {
    return false;
  }
  static boolean isByte(final Object __) {
    return false;
  }
  static boolean isByte(final short __) {
    return false;
  }
  static boolean isByte(final String __) {
    return false;
  }
  static boolean isChar(final byte __) {
    return false;
  }
  static boolean isChar(final char __) {
    return true;
  }
  static boolean isChar(final double __) {
    return false;
  }
  static boolean isChar(final float __) {
    return false;
  }
  static boolean isChar(final int __) {
    return false;
  }
  static boolean isChar(final long __) {
    return false;
  }
  static boolean isChar(final Object __) {
    return false;
  }
  static boolean isChar(final short __) {
    return false;
  }
  static boolean isChar(final String __) {
    return false;
  }
  static boolean isDouble(final boolean __) {
    return false;
  }
  static boolean isDouble(final byte __) {
    return false;
  }
  static boolean isDouble(final char __) {
    return false;
  }
  static boolean isDouble(final double __) {
    return true;
  }
  static boolean isDouble(final float __) {
    return false;
  }
  static boolean isDouble(final int __) {
    return false;
  }
  static boolean isDouble(final long __) {
    return false;
  }
  static boolean isDouble(final Object __) {
    return false;
  }
  static boolean isDouble(final short __) {
    return false;
  }
  static boolean isDouble(final String __) {
    return false;
  }
  static boolean isFloat(final boolean __) {
    return false;
  }
  static boolean isFloat(final byte __) {
    return false;
  }
  static boolean isFloat(final char __) {
    return false;
  }
  static boolean isFloat(final double __) {
    return false;
  }
  static boolean isFloat(final float __) {
    return true;
  }
  static boolean isFloat(final int __) {
    return false;
  }
  static boolean isFloat(final long __) {
    return false;
  }
  static boolean isFloat(final Object __) {
    return false;
  }
  static boolean isFloat(final short __) {
    return false;
  }
  static boolean isFloat(final String __) {
    return false;
  }
  static boolean isInt(final boolean __) {
    return false;
  }
  static boolean isInt(final byte __) {
    return false;
  }
  static boolean isInt(final char __) {
    return false;
  }
  static boolean isInt(final double __) {
    return false;
  }
  static boolean isInt(final float __) {
    return false;
  }
  static boolean isInt(final int __) {
    return true;
  }
  static boolean isInt(final long __) {
    return false;
  }
  static boolean isInt(final Object __) {
    return false;
  }
  static boolean isInt(final short __) {
    return false;
  }
  static boolean isInt(final String __) {
    return false;
  }
  static boolean isLong(final boolean __) {
    return false;
  }
  static boolean isLong(final byte __) {
    return false;
  }
  static boolean isLong(final char __) {
    return false;
  }
  static boolean isLong(final double __) {
    return false;
  }
  static boolean isLong(final float __) {
    return false;
  }
  static boolean isLong(final int __) {
    return true;
  }
  static boolean isLong(final long __) {
    return true;
  }
  static boolean isLong(final Object __) {
    return false;
  }
  static boolean isLong(final short __) {
    return false;
  }
  static boolean isLong(final String __) {
    return true;
  }
  static boolean isObject(final short __) {
    return false;
  }
  static boolean isShort(final boolean __) {
    return false;
  }
  static boolean isShort(final byte __) {
    return true;
  }
  static boolean isShort(final char __) {
    return false;
  }
  static boolean isShort(final double __) {
    return false;
  }
  static boolean isShort(final float __) {
    return false;
  }
  static boolean isShort(final int __) {
    return false;
  }
  static boolean isShort(final long __) {
    return false;
  }
  static boolean isShort(final Object __) {
    return false;
  }
  static boolean isShort(final short __) {
    return true;
  }
  static boolean isShort(final String __) {
    return false;
  }
  static boolean isString(final double __) {
    return false;
  }
  static boolean isString(final float __) {
    return false;
  }
  static boolean isString(final Object __) {
    return false;
  }
  static boolean isString(final short __) {
    return false;
  }
  static boolean isString(final String __) {
    return true;
  }
}
