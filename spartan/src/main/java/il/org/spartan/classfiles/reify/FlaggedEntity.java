package il.org.spartan.classfiles.reify;

import java.lang.reflect.Modifier;

/** @author Yossi Gil
 * @since 21 November 2011 */
public abstract class FlaggedEntity extends AttributedEntity {
  public static final int BRIDGE = 0x00000040;
  public static final int VARARGS = 0x00000080;
  public static final int SYNTHETIC = 0x00001000;
  public static final int ANNOTATION = 0x00002000;
  public static final int ENUM = 0x00004000;
  public static final int DEPRECATED = 0x00008000;
  public final int flags;

  public FlaggedEntity(final int flags, final String name, final AttributeInfo[] attributes) {
    super(attributes, name);
    this.flags = flags | (hasNo("Synthetic") ? 0 : SYNTHETIC) | (hasNo("Deprecated") ? 0 : DEPRECATED);
  }
  public boolean isAbstract() {
    return Modifier.isAbstract(flags);
  }
  public boolean isDefault() {
    return !isPrivate() && !isPublic() && !isProtected();
  }
  public boolean isDeprecated() {
    return (flags & DEPRECATED) != 0;
  }
  public boolean isFinal() {
    return Modifier.isFinal(flags);
  }
  public boolean isNative() {
    return Modifier.isNative(flags);
  }
  public boolean isPrivate() {
    return Modifier.isPrivate(flags);
  }
  public boolean isProtected() {
    return Modifier.isProtected(flags);
  }
  public boolean isPublic() {
    return Modifier.isPublic(flags);
  }
  public boolean isStatic() {
    return Modifier.isStatic(flags);
  }
  public boolean isSynchronized() {
    return Modifier.isSynchronized(flags);
  }
  public boolean isSynthetic() {
    return (flags & SYNTHETIC) != 0;
  }
  public boolean isTransient() {
    return Modifier.isTransient(flags);
  }
  public boolean isVolatile() {
    return Modifier.isVolatile(flags);
  }
}
