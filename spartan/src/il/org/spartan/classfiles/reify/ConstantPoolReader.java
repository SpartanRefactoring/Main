package il.org.spartan.classfiles.reify;

import java.io.*;

import org.jetbrains.annotations.*;

import il.org.spartan.classfiles.*;
import il.org.spartan.classfiles.reify.ConstantPool.*;

/** @author Yossi Gil
 * @since 26 November 2011 */
public class ConstantPoolReader extends RobustReader {
  private final ConstantPool constantPool;

  public ConstantPoolReader(final DataInputStream inner, final ConstantPool constantPool) {
    super(inner);
    this.constantPool = constantPool;
  }

  @Nullable public String classPackage(final int classIndex) {
    return classIndex == 0 ? null : constantPool.getPackage(classIndex);
  }

  @Nullable public String classShortName(final int classIndex) {
    return classIndex == 0 ? null : constantPool.getShortClassName(classIndex);
  }

  @NotNull public AttributeInfo[] readAttributes() {
    @NotNull final AttributeInfo[] $ = new AttributeInfo[readUnsignedShort()];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = readAttribute();
    return $;
  }

  @NotNull public byte[] readBytesArrray() {
    return readBytes(new byte[readInt()]);
  }

  @NotNull public ClassConstant readClassConstant() {
    return constantPool.getClassConstant(readUnsignedShort());
  }

  @NotNull public ClassConstant[] readClasses() {
    @NotNull final ClassConstant[] $ = new ClassConstant[readUnsignedShort()];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = readClassConstant();
    return $;
  }

  @Nullable public String readClassName() {
    return readClassName(readUnsignedShort());
  }

  @Nullable public String readClassName(final int classIndex) {
    return classIndex == 0 ? null : constantPool.getClassName(classIndex);
  }

  @NotNull public TypedEntity[] readMembers() {
    @NotNull final TypedEntity[] $ = new TypedEntity[readUnsignedShort()];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = new TypedEntity(constantPool, readUnsignedShort(), readStringConstant(), readStringConstant(), readAttributes());
    return $;
  }

  public String readStringConstant() {
    return constantPool.getUTF8(readUnsignedShort());
  }

  @NotNull private AttributeInfo readAttribute() {
    return new AttributeInfo(readStringConstant(), readBytesArrray());
  }
}
