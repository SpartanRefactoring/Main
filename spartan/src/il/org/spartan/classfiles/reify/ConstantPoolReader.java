/**
 *
 */
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
    for (int i = 0; i < $.length; i++)
      $[i] = readAttribute();
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
    for (int i = 0; i < $.length; i++)
      $[i] = readClassConstant();
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
    for (int i = 0; i < $.length; i++) {
      final int flags = readUnsignedShort(); // access flag
      final String memberName = readStringConstant(); // u2 name;
      final String descriptor = readStringConstant(); // u2 descriptionIndex;
      $[i] = new TypedEntity(constantPool, flags, memberName, descriptor, readAttributes());
    }
    return $;
  }

  public String readStringConstant() {
    return constantPool.getUTF8(readUnsignedShort());
  }

  private AttributeInfo readAttribute() {
    final String attributeName = readStringConstant();
    return new AttributeInfo(attributeName, readBytesArrray());
  }
}
