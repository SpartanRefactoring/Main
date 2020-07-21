package il.org.spartan.reflection;

import java.lang.reflect.*;

/** This class realizes a memory model for Java objects, as per the following
 * rules.
 * <ol>
 * <li><b>Arrays of boolean, byte, char, short, int:</b>
 *
 * <pre>
 *          2 * 4 (Object header)
 *      +   4 (length-field)
 *      +   sizeof(primitiveType) * length
 *  -> align result up to a multiple of 8
 * </pre>
 *
 * <li><b>Arrays of objects:</b>
 *
 * <pre>
 *          2 * 4 (Object header)
 *       +  4 (length-field)
 *       +  4 * length
 *   -> align result up to a multiple of 8
 * </pre>
 *
 * <li><b>Arrays of longs and doubles:<b>
 *
 * <pre>
 *          2 * 4 (Object header)
 *       +  4 (length-field)
 *       +  4 (dead space due to alignment restrictions)
 *       +  8 * length
 * </pre>
 *
 * <li><b>java.lang.Object:</b>
 *
 * <pre>
 *          2 * 4 (Object header)
 * </pre>
 *
 * <li><b>other objects:</b>
 *
 * <pre>
 *               sizeofSuperClass
 *             + 8 * nrOfLongAndDoubleFields
 *             + 4 * nrOfIntFloatAndObjectFields
 *             + 2 * nrOfShortAndCharFields
 *             + 1 * nrOfByteAndBooleanFields
 *     -> align result up to a multiple of 8
 * </pre>
 * </ol>
*/
public class ShallowSize {
  public static int arraySize(final int length) {
    return arraySize(length, referenceSize());
  }
  public static int of(final boolean ¢[]) {
    return arraySize(¢.length, 1);
  }
  public static int of(final byte ¢[]) {
    return arraySize(¢.length, 1);
  }
  public static int of(final char ¢[]) {
    return arraySize(¢.length, 2);
  }
  public static int of(final Class<?> ¢) {
    final Class<?> $ = ¢.getSuperclass();
    return align(intrinsic(¢) + ($ != null ? of($) : headerSize()));
  }
  public static int of(final double ¢[]) {
    return arraySize(¢.length, 8);
  }
  public static int of(final float ¢[]) {
    return arraySize(¢.length, 4);
  }
  public static int of(final int ¢[]) {
    return arraySize(¢.length, 4);
  }
  public static int of(final long ¢[]) {
    return arraySize(¢.length, 8);
  }
  public static int of(final Object ¢[]) {
    return arraySize(¢.length);
  }
  public static int of(final Object ¢) {
    return ¢ == null ? 0 : of(¢.getClass());
  }
  public static int of(final short ¢[]) {
    return arraySize(¢.length, 2);
  }
  public static int referenceSize() {
    return 4;
  }
  static int align(final int ¢) {
    return 8 * (¢ - 1) / 8 + 8;
  }
  static int arraySize(final int length, final int size) {
    return align(headerSize() + lengthSize() + size * length);
  }
  static int headerSize() {
    return 8;
  }
  static int intrinsic(final Class<?> c) {
    int $ = 0;
    for (final Field ¢ : c.getDeclaredFields())
      $ += size(¢);
    return $;
  }
  static int intrinsic(final Object ¢) {
    return intrinsic(¢.getClass());
  }
  static int lengthSize() {
    return 4;
  }
  static int size(final Field ¢) {
    if (Modifier.isStatic(¢.getModifiers()))
      return 0;
    final Class<?> $ = ¢.getType();
    return $ == byte.class || $ == boolean.class ? 1
        : $ == short.class || $ == char.class ? 2
            : $ == int.class || $ == float.class ? 4 : $ == long.class || $ == double.class ? 8 : referenceSize();
  }
}
