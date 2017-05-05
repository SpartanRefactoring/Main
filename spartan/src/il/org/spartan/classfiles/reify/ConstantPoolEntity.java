package il.org.spartan.classfiles.reify;

import org.jetbrains.annotations.*;

import il.org.spartan.classfiles.reify.ClassInfo.*;

/** @author Yossi Gil
 * @since 25 November 2011 */
public class ConstantPoolEntity extends FlaggedEntity {
  public final ConstantPool constantPool;

  public ConstantPoolEntity(@NotNull final Builder b) {
    this(b.constantPool, b.accessFlags, b.name, b.attributes);
  }
  public ConstantPoolEntity(final ConstantPool constantPool, final int flags, final String name, final AttributeInfo[] attributes) {
    super(flags, name, attributes);
    this.constantPool = constantPool;
  }
  @Nullable public String getClassName(final int classIndex) {
    return constantPool.getClassName(classIndex);
  }
  @Nullable public String getPackage(final int classIndex) {
    return constantPool.getPackage(classIndex);
  }
  @NotNull public String[] getReferencedClasses() {
    return constantPool.getReferencedClasses();
  }
  @NotNull public int[] getReferencedClassesIndices() {
    return constantPool.getReferencedClassesIndices();
  }
  @NotNull public double[] getReferencedDoubles() {
    return constantPool.getReferencedDoubles();
  }
  @NotNull public float[] getReferencedFloats() {
    return constantPool.getReferencedFloats();
  }
  @NotNull public int[] getReferencedInts() {
    return constantPool.getReferencedInts();
  }
  @NotNull public long[] getReferencedLongs() {
    return constantPool.getReferencedLongs();
  }
  @NotNull public String[] getReferencedStrings() {
    return constantPool.getReferencedStrings();
  }
  @NotNull public String[] getReferencedUTF8() {
    return constantPool.getReferencedUTF8();
  }
  @Nullable public String getShortClassName(final int classIndex) {
    return constantPool.getShortClassName(classIndex);
  }
  public String getUTF8(final int index) {
    return constantPool.getUTF8(index);
  }
}