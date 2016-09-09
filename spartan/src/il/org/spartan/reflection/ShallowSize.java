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

  public static int of(final boolean it[]) {
    return arraySize(it.length, 1);
  }

  public static int of(final byte it[]) {
    return arraySize(it.length, 1);
  }

  public static int of(final char it[]) {
    return arraySize(it.length, 2);
  }

  public static int of(final Class<?> c) {
    final Class<?> parent = c.getSuperclass();
    return align(intrinsic(c) + (parent == null ? headerSize() : of(parent)));
  }

  public static int of(final double it[]) {
    return arraySize(it.length, 8);
  }

  public static int of(final float it[]) {
    return arraySize(it.length, 4);
  }

  public static int of(final int it[]) {
    return arraySize(it.length, 4);
  }

  public static int of(final long it[]) {
    return arraySize(it.length, 8);
  }

  public static int of(final Object it[]) {
    return arraySize(it.length);
  }

  public static int of(final Object o) {
    return o == null ? 0 : of(o.getClass());
  }

  public static int of(final short it[]) {
    return arraySize(it.length, 2);
  }

  public static int referenceSize() {
    return 4;
  }

  static int align(final int n) {
    final int i = 8;
    return i + (n - 1) / i * i;
  }

  static int arraySize(final int length, final int size) {
    return align(headerSize() + lengthSize() + length * size);
  }

  static int headerSize() {
    return 8;
  }

  static int intrinsic(final Class<?> c) {
    int $ = 0;
    for (final Field f : c.getDeclaredFields())
      $ += size(f);
    return $;
  }

  static int intrinsic(final Object o) {
    return intrinsic(o.getClass());
  }

  static int lengthSize() {
    return 4;
  }

  static int size(final Field f) {
    if (Modifier.isStatic(f.getModifiers()))
      return 0;
    final Class<?> c = f.getType();
    if (c == byte.class || c == boolean.class)
      return 1;
    if (c == short.class || c == char.class)
      return 2;
    if (c == int.class || c == float.class)
      return 4;
    if (c == long.class || c == double.class)
      return 8;
    return referenceSize();
  }
}
