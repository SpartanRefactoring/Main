package il.org.spartan.classfiles.reify;

import java.io.*;

import org.jetbrains.annotations.*;

public class AttributeInfo {
  public final String name;
  final byte[] data;

  public AttributeInfo(final String name, final byte[] data) {
    this.name = name;
    this.data = data;
  }

  @NotNull final ConstantPoolReader reader(final ConstantPool ¢) {
    return new ConstantPoolReader(new DataInputStream(new ByteArrayInputStream(data)), ¢);
  }
}