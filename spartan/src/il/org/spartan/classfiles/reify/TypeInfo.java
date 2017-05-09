package il.org.spartan.classfiles.reify;

import java.util.*;

import il.org.spartan.utils.*;

public abstract class TypeInfo {
  public static TypeInfo makeArrayOf(final TypeInfo ¢) {
    return new ArrayType(¢);
  }
  public static TypeInfo makeConstructor(final TypeInfo ¢) {
    return new ConstructorType(((MethodType) ¢).arguments);
  }
  public static TypeInfo makeInitializer(@SuppressWarnings("unused") final TypeInfo __) {
    return new InitializerType();
  }
  public static TypeInfo makeMethodType(final TypeInfo returnValue, final TypeInfo... arguments) {
    return new MethodType(returnValue, arguments);
  }
  public static TypeInfo makePrimitiveType(final String name) {
    return new AtomicType(true, name);
  }
  public static TypeInfo makeReferenceType(final String name) {
    return new AtomicType(false, name);
  }
  public abstract Collection<TypeInfo> components();
  public abstract boolean isPrimitive();
  @Override public abstract String toString();

  public static class ArrayType extends TypeInfo {
    private final TypeInfo inner;

    public ArrayType(final TypeInfo inner) {
      this.inner = inner;
    }
    @Override public Collection<TypeInfo> components() {
      return inner.components();
    }
    @Override public boolean isPrimitive() {
      return false;
    }
    @Override public String toString() {
      return inner + "[]";
    }
  }

  public static final class AtomicType extends TypeInfo {
    final String name;
    final boolean isPrimitive;

    AtomicType(final boolean isPrimitive, final String name) {
      this.name = name;
      this.isPrimitive = isPrimitive;
    }
    AtomicType(final String name) {
      this(true, name);
    }
    @Override public Collection<TypeInfo> components() {
      final ArrayList<TypeInfo> $ = new ArrayList<>(1);
      $.add(this);
      return $;
    }
    @Override public boolean isPrimitive() {
      return isPrimitive;
    }
    @Override public String toString() {
      return name;
    }
  }

  public static class ConstructorType extends InitializerType {
    public final TypeInfo[] arguments;

    public ConstructorType(final TypeInfo[] arguments) {
      this.arguments = arguments;
    }
    @Override public Collection<TypeInfo> components() {
      final List<TypeInfo> $ = new ArrayList<>();
      for (final TypeInfo a : arguments)
        $.addAll(a.components());
      return $;
    }
  }

  public static class InitializerType extends TypeInfo {
    @Override public Collection<TypeInfo> components() {
      return new ArrayList<>();
    }
    @Override public final boolean isPrimitive() {
      return false;
    }
    @Override public String toString() {
      return "()";
    }
  }

  public static class MethodType extends ConstructorType {
    public final TypeInfo returnValue;

    public MethodType(final TypeInfo returnValue, final TypeInfo[] arguments) {
      super(arguments);
      this.returnValue = returnValue;
    }
    @Override public Collection<TypeInfo> components() {
      final List<TypeInfo> $ = new ArrayList<>(returnValue.components());
      $.addAll(super.components());
      return $;
    }
    @Override public String toString() {
      return returnValue + " (" + Separate.by(arguments, ", ") + ")";
    }
  }
}