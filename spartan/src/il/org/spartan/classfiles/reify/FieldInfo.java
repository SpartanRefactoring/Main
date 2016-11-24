package il.org.spartan.classfiles.reify;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since 21 November 2011 */
public final class FieldInfo extends TypedEntity {
  public FieldInfo(final ConstantPool constantPool, final int flags, final String name, @NotNull final String descriptor,
      final AttributeInfo[] attributes) {
    super(constantPool, flags, name, descriptor, attributes);
  }

  public FieldInfo(@NotNull final TypedEntity t) {
    super(t.constantPool, t.flags, t.name, t.descriptor, t.attributes);
  }
}
