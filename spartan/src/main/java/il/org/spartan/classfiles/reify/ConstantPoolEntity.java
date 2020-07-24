package il.org.spartan.classfiles.reify;

import il.org.spartan.classfiles.reify.ClassInfo.Builder;

/** @author Yossi Gil
 * @since 25 November 2011 */
public class ConstantPoolEntity extends FlaggedEntity {
  public final ConstantPool constantPool;

  public ConstantPoolEntity(final Builder b) {
    this(b.constantPool, b.accessFlags, b.name, b.attributes);
  }

  public ConstantPoolEntity(final ConstantPool constantPool, final int flags, final String name,
      final AttributeInfo[] attributes) {
    super(flags, name, attributes);
    this.constantPool = constantPool;
  }

  public String getClassName(final int classIndex) {
    return constantPool.getClassName(classIndex);
  }

  public String getPackage(final int classIndex) {
    return constantPool.getPackage(classIndex);
  }

  public String[] getReferencedClasses() {
    return constantPool.getReferencedClasses();
  }

  public int[] getReferencedClassesIndices() {
    return constantPool.getReferencedClassesIndices();
  }

  public double[] getReferencedDoubles() {
    return constantPool.getReferencedDoubles();
  }

  public float[] getReferencedFloats() {
    return constantPool.getReferencedFloats();
  }

  public int[] getReferencedInts() {
    return constantPool.getReferencedInts();
  }

  public long[] getReferencedLongs() {
    return constantPool.getReferencedLongs();
  }

  public String[] getReferencedStrings() {
    return constantPool.getReferencedStrings();
  }

  public String[] getReferencedUTF8() {
    return constantPool.getReferencedUTF8();
  }

  public String getShortClassName(final int classIndex) {
    return constantPool.getShortClassName(classIndex);
  }

  public String getUTF8(final int index) {
    return constantPool.getUTF8(index);
  }
}