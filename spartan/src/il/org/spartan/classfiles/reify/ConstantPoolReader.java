package il.org.spartan.classfiles.reify;

import java.io.*;

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
  public String classPackage(final int classIndex) {
    return classIndex == 0 ? null : constantPool.getPackage(classIndex);
  }
  public String classShortName(final int classIndex) {
    return classIndex == 0 ? null : constantPool.getShortClassName(classIndex);
  }
  public AttributeInfo[] readAttributes() {
    final AttributeInfo[] $ = new AttributeInfo[readUnsignedShort()];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = readAttribute();
    return $;
  }
  public byte[] readBytesArrray() {
    return readBytes(new byte[readInt()]);
  }
  public ClassConstant readClassConstant() {
    return constantPool.getClassConstant(readUnsignedShort());
  }
  public ClassConstant[] readClasses() {
    final ClassConstant[] $ = new ClassConstant[readUnsignedShort()];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = readClassConstant();
    return $;
  }
  public String readClassName() {
    return readClassName(readUnsignedShort());
  }
  public String readClassName(final int classIndex) {
    return classIndex == 0 ? null : constantPool.getClassName(classIndex);
  }
  public TypedEntity[] readMembers() {
    final TypedEntity[] $ = new TypedEntity[readUnsignedShort()];
    for (int ¢ = 0; ¢ < $.length; ++¢)
      $[¢] = new TypedEntity(constantPool, readUnsignedShort(), readStringConstant(), readStringConstant(), readAttributes());
    return $;
  }
  public String readStringConstant() {
    return constantPool.getUTF8(readUnsignedShort());
  }
  private AttributeInfo readAttribute() {
    return new AttributeInfo(readStringConstant(), readBytesArrray());
  }
}
