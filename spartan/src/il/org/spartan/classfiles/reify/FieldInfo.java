package il.org.spartan.classfiles.reify;

/** @author Yossi Gil
 * @since 21 November 2011 */
public final class FieldInfo extends TypedEntity {
  public FieldInfo(final ConstantPool constantPool, final int flags, final String name, final String descriptor, final AttributeInfo[] attributes) {
    super(constantPool, flags, name, descriptor, attributes);
  }
  public FieldInfo(final TypedEntity t) {
    super(t.constantPool, t.flags, t.name, t.descriptor, t.attributes);
  }
}
