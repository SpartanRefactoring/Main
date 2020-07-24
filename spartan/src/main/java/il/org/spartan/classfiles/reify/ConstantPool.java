package il.org.spartan.classfiles.reify;

import static fluent.ly.box.*;
import static fluent.ly.unbox.*;

import java.util.*;

import fluent.ly.idiomatic;
import il.org.spartan.classfiles.*;
import il.org.spartan.collections.*;

/** A representation of an entry in the constant pool array.
 * @author Yossi Gil */
public final class ConstantPool {
  private static UTF8 asUTF8(final Constant $) {
    try {
      return (UTF8) $;
    } catch (final ClassCastException ¢) {
      throw new CorruptClassFile(¢);
    }
  }

  private final Constant[] pool;

  public ConstantPool(final RobustReader reader) {
    pool = new Constant[reader.readUnsignedShort()];
    for (int ¢ = 1; ¢ < pool.length; ++¢)
      if ((pool[¢] = readConstant(reader)).isDoubleLength())
        pool[++¢] = Empty.$$;
  }
  /** @param classIndex
   * @return name of class stored in this location */
  public String getClassName(final int classIndex) {
    return ((ClassConstant) pool[classIndex]).getClassName();
  }
  public FieldReference getFieldReference(final int classIndex) {
    return (FieldReference) pool[classIndex];
  }
  public MemberReference getMemberReference(final int classIndex) {
    return (MemberReference) pool[classIndex];
  }
  public String getPackage(final int classIndex) {
    return ((ClassConstant) pool[classIndex]).getPackage();
  }
  /** Which other classes does this class refer to?
   * @return an array with names of all classes that this class uses */
  public String[] getReferencedClasses() {
    final ArrayList<String> $ = new ArrayList<>();
    for (final Constant ¢ : pool)
      if (¢ instanceof ClassConstant)
        $.add(¢ + "");
    return $.toArray(new String[$.size()]);
  }
  public int[] getReferencedClassesIndices() {
    final IntsArray $ = new IntsArray();
    for (int ¢ = 0; ¢ < pool.length; ++¢)
      if (pool[¢] instanceof ClassConstant && pool[¢] != null)
        $.push(¢);
    return $.toArray();
  }
  /** Which <code><b>double</b></code>s are found in this class's constants'
   * pool?
   * @return an array with <code><b>double</b></code>s that this class uses. */
  public double[] getReferencedDoubles() {
    final ArrayList<Double> $ = new ArrayList<>();
    for (final Constant ¢ : pool)
      if (¢ instanceof DoubleLiteral)
        $.add(idiomatic.box(((DoubleLiteral) ¢).value));
    return it($.toArray(new Double[$.size()]));
  }
  /** Which <code><b>float</b></code>s are found in this class's constants'
   * pool?
   * @return an array with <code><b>float</b></code>s that this class uses. */
  public float[] getReferencedFloats() {
    final ArrayList<Float> $ = new ArrayList<>();
    for (final Constant ¢ : pool)
      if (¢ instanceof FloatLiteral)
        $.add(idiomatic.box(((FloatLiteral) ¢).value));
    return it($.toArray(new Float[$.size()]));
  }
  /** Which <code><b>int</b></code>s are found in this class's constants' pool?
   * @return an array with <code><b>int</b></code>s that this class uses. */
  public int[] getReferencedInts() {
    final ArrayList<Integer> $ = new ArrayList<>();
    for (final Constant ¢ : pool)
      if (¢ instanceof IntLiteral)
        $.add(idiomatic.box(((IntLiteral) ¢).value));
    return it($.toArray(new Integer[$.size()]));
  }
  /** Which <code><b>long</b></code>s are found in this class's constants' pool?
   * @return an array with <code><b>long</b></code>s that this class uses. */
  public long[] getReferencedLongs() {
    final ArrayList<Long> $ = new ArrayList<>();
    for (final Constant ¢ : pool)
      if (¢ instanceof LongLiteral)
        $.add(idiomatic.box(((LongLiteral) ¢).value));
    return it($.toArray(new Long[$.size()]));
  }
  public String[] getReferencedMethods() {
    final ArrayList<String> $ = new ArrayList<>();
    for (final Constant ¢ : pool)
      if (¢ instanceof MethodReference && !"<init>".equals(((MethodReference) ¢).getNameAndType().getName()))
        $.add(((MethodReference) ¢).getClassConstant().getClassName() + ":" + ((MethodReference) ¢).getNameAndType().getName());
    return $.toArray(new String[$.size()]);
  }
  /** Which {@link String}s does this class refer to?
   * @return an array with all {@link String}s that this class uses */
  public String[] getReferencedStrings() {
    final ArrayList<String> $ = new ArrayList<>();
    for (final Constant ¢ : pool)
      if (¢ instanceof StringConstant)
        $.add(¢ + "");
    return $.toArray(new String[$.size()]);
  }
  /** Which <code><b>UTF8</b></code>s are found in this class's constants' pool?
   * @return an array with <code><b>UTF8</b></code>s that this class uses. */
  public String[] getReferencedUTF8() {
    final ArrayList<String> $ = new ArrayList<>();
    for (final Constant ¢ : pool)
      if (¢ instanceof UTF8)
        $.add(((UTF8) ¢).value);
    return $.toArray(new String[$.size()]);
  }
  public String getShortClassName(final int classIndex) {
    return ((ClassConstant) pool[classIndex]).getShortClassName();
  }
  public String getUTF8(final int a) {
    return asUTF8(pool[a]).value;
  }
  ClassConstant getClassConstant(final int classIndex) {
    return (ClassConstant) pool[classIndex];
  }
  /** Read the next constant pool entry from a given stream
   * @param r
   * @return the next constant pool entry found in the given stream */
  private Constant readConstant(final RobustReader $) {
    final int//
    CONSTANT_UTF8 = 1, //
        CONSTANT_INTEGER = 3, //
        CONSTANT_FLOAT = 4, //
        CONSTANT_LONG = 5, //
        CONSTANT_DOUBLE = 6, //
        CONSTANT_CLASS = 7, //
        CONSTANT_STRING = 8, //
        CONSTANT_FIELDREF = 9, //
        CONSTANT_METHODREF = 10, //
        CONSTANT_INTERFACE_METHODREF = 11, //
        CONSTANT_NAME_AND_TYPE = 12; //
    int b;
    switch (b = $.readUnsignedByte()) {
      case CONSTANT_CLASS:
        return new ClassConstant($.readUnsignedShort());
      case CONSTANT_FIELDREF:
        return new FieldReference($.readUnsignedShort(), $.readUnsignedShort());
      case CONSTANT_METHODREF:
        return new MethodReference($.readUnsignedShort(), $.readUnsignedShort());
      case CONSTANT_INTERFACE_METHODREF:
        return new InterfaceMethodReference($.readUnsignedShort(), $.readUnsignedShort());
      case CONSTANT_STRING:
        return new StringConstant($.readUnsignedShort());
      case CONSTANT_INTEGER:
        return new IntLiteral($.readInt());
      case CONSTANT_FLOAT:
        return new FloatLiteral($.readFloat());
      case CONSTANT_LONG:
        return new LongLiteral($.readLong());
      case CONSTANT_DOUBLE:
        return new DoubleLiteral($.readDouble());
      case CONSTANT_NAME_AND_TYPE:
        return new NameAndTypeConstant($.readUnsignedShort(), $.readUnsignedShort());
      case CONSTANT_UTF8:
        return new UTF8($.readUTF());
      default:
        System.out.print("Unfamiliar field identifier = " + b);
        return null;
    }
  }

  /** A representation of a class constant in the constants' pool
   * @author Yossi Gil */
  public class ClassConstant extends Reference {
    /** Creates an instance for the specified index referring an {@link UTF8}
     * @param nameIndex Index into constant pool */
    public ClassConstant(final int nameIndex) {
      super(nameIndex);
    }
    /** What is the fully-qualified class name. In the case of an object array
     * only the class name of the object is returned.
     * @return fully-qualified class name in standard notation with '.'. */
    public String getClassName() {
      String $ = getUTF8(contentIndex);
      if ($ == null)
        return null;
      $ = $.replaceFirst("^[\\[]+L", "").replaceAll(";$", "");
      return $.startsWith("[") ? null : $.replace('/', '.').replace('$', '.');
    }
    public String getPackage() {
      String $ = getUTF8(contentIndex);
      if ($ == null)
        return null;
      $ = $.replaceFirst("^[\\[]+L", "").replaceAll(";$", "");
      if ($.startsWith("["))
        return null;
      final int i = $.lastIndexOf('/');
      return i == -1 ? "" : $.substring(0, i).replace('/', '.');
    }
    public String getShortClassName() {
      String $ = getUTF8(contentIndex);
      if ($ == null)
        return null;
      $ = $.replaceFirst("^[\\[]+L", "").replaceAll(";$", "");
      return $.startsWith("[") ? null : $.substring($.lastIndexOf('/') + 1).replace('$', '.');
    }
    @Override public String toString() {
      return getClassName();
    }
    @Override public String typeName() {
      return "CLASS";
    }
  }

  public abstract static class Constant {
    /** Does this entry occupy double space?
     * @return <code><b>true</b></code> <i>iff</i> this instance occupies two
     *         32-bit words */
    @SuppressWarnings("static-method") public boolean isDoubleLength() {
      return false;
    }
    /** What is the type of this constant?
     * @return a textual representation of the type of this instance */
    public abstract String typeName();
  }

  /** A representation of a <code><b>long</b></code> literal in the constants'
   * pool
   * @author Yossi Gil */
  public static final class DoubleLiteral extends Literal {
    /** Actual content of this instance */
    public final double value;

    /** Creates an instance for the specified <code><b>double</b></code> value.
     * @param value the value to be stored in this constants pool item */
    public DoubleLiteral(final double value) {
      this.value = value;
    }
    @Override public boolean isDoubleLength() {
      return true;
    }
    @Override public String toString() {
      return value + "";
    }
    @Override public String typeName() {
      return "DOUBLE";
    }
  }

  /** A representation of an unoccupied entry in the in memory representation of
   * the constants' pool table. Such an entry occurs whenever the previous entry
   * uses a double space on disk. The in-memory representation, which relies on
   * polymorphism, does not require allocation of multiple entries for one
   * constant, hence we have empty locations.
   * <p>
   * Implementation is a singleton realizing of the <b>Null Object</b> design
   * pattern
   * @author Yossi Gil */
  public static final class Empty extends Literal {
    /** Singleton instance */
    public static final Empty $$ = new Empty();

    /** A default, inaccessible constructor, forcing the singleton
     * restriction */
    private Empty() {
      // Forbid instantiation by clients
    }
    @Override public String typeName() {
      return "EMPTY";
    }
  }

  /** A representation of a field reference constant in the constants' pool
   * @author Yossi Gil */
  public final class FieldReference extends MemberReference {
    /** Instantiate with a given class name and name,type pair
     * @param classIndex in which class is this field defined?
     * @param nameAndTypeIndex what is the name and type of this field? */
    public FieldReference(final int classIndex, final int nameAndTypeIndex) {
      super(classIndex, nameAndTypeIndex);
    }
    @Override public String typeName() {
      return "FIELD";
    }
  }

  /** A representation of a <code><b>float</b></code> literal in the constants'
   * pool
   * @author Yossi Gil */
  public final class FloatLiteral extends Literal {
    /** Actual content of this instance */
    public final float value;

    /** Creates an instance for the specified <code><b>float</b></code> value.
     * @param value the value to be stored in this constants pool item */
    public FloatLiteral(final float value) {
      this.value = value;
    }
    @Override public String toString() {
      return "CONSTANT_Float: " + value;
    }
    @Override public String typeName() {
      return "DOUBLE";
    }
  }

  /** A representation of an interface method reference constant in the
   * constants' pool
   * @author Yossi Gil */
  public final class InterfaceMethodReference extends MemberReference {
    /** Instantiate with a given class name and name,type pair
     * @param classIndex in which class is this interface method defined?
     * @param nameAndTypeIndex what is the name and type of this interface
     *        method? */
    public InterfaceMethodReference(final int classIndex, final int nameAndTypeIndex) {
      super(classIndex, nameAndTypeIndex);
    }
    @Override public String typeName() {
      return "INTERFACE METHOD";
    }
  }

  /** A representation of an <code><b>int</b></code> literal in the constants'
   * pool
   * @author Yossi Gil */
  public static final class IntLiteral extends Literal {
    /** Actual content of this instance */
    public final int value;

    /** Creates an instance for the specified <code><b>int</b></code> value.
     * @param value the value to be stored in this constants pool item */
    public IntLiteral(final int value) {
      this.value = value;
    }
    @Override public String toString() {
      return value + "";
    }
    @Override public String typeName() {
      return "INTEGER";
    }
  }

  /** A representation of a literal in the constants' pool
   * @author Yossi Gil */
  public abstract static class Literal extends Constant {
    // Empty
  }

  /** A representation of a <code><b>long</b></code> literal in the constants'
   * pool
   * @author Yossi Gil */
  public final class LongLiteral extends Literal {
    /** Actual content of this instance */
    public final long value;

    /** Creates an instance for the specified <code><b>int</b></code> value.
     * @param value the value to be stored in this constants pool item */
    public LongLiteral(final long value) {
      this.value = value;
    }
    @Override public boolean isDoubleLength() {
      return true;
    }
    @Override public String toString() {
      return value + "";
    }
    @Override public String typeName() {
      return "DOUBLE";
    }
  }

  /** An representation of a reference to a class member in the constants' pool
   * @author Yossi Gil */
  public abstract class MemberReference extends Constant {
    /** Where is the class of this member stored? */
    public final int classIndex;
    /** Where are the name and type descriptor of this member stored? */
    public final int nameAndTypeIndex;

    /** Creates an instance for the specified class, name, and type. Constant
     * pool. Needed for resolving references.
     * @param classIndex index of {@link ClassConstant}.
     * @param nameAndTypeIndex index of {@link NameAndTypeConstant}. */
    public MemberReference(final int classIndex, final int nameAndTypeIndex) {
      this.classIndex = classIndex;
      this.nameAndTypeIndex = nameAndTypeIndex;
    }
    /** @return the class constant. */
    @SuppressWarnings("synthetic-access") //
    public ClassConstant getClassConstant() {
      return (ClassConstant) pool[classIndex];
    }
    /** @return the name-and-type constant. */
    @SuppressWarnings("synthetic-access") //
    public NameAndTypeConstant getNameAndType() {
      return (NameAndTypeConstant) pool[nameAndTypeIndex];
    }
    @Override public final String toString() {
      return "Class = " + getClassConstant().getClassName() + ", Name = " + getNameAndType().getName() + ", Descriptor = "
          + getNameAndType().getDescriptor();
    }
  }

  /** A representation of a method reference constant in the constants' pool
   * @author Yossi Gil */
  public final class MethodReference extends MemberReference {
    /** Instantiate with a given class name and name,type pair
     * @param classIndex in which class is this method defined?
     * @param nameAndTypeIndex what is the name and type of this method? */
    public MethodReference(final int classIndex, final int nameAndTypeIndex) {
      super(classIndex, nameAndTypeIndex);
    }
    @Override public String typeName() {
      return "METHOD";
    }
  }

  /** A representation of a name&type constant in the constants' pool
   * @author Yossi Gil */
  public class NameAndTypeConstant extends Reference {
    /** Where is the type descriptor of this entry stored */
    public final int descriptorIndex;

    /** Creates an instance for the specified name and type or method
     * descriptor.
     * @param nameIndex Index of the name in the pool.
     * @param descriptorIndex Index of the type or method descriptor in the
     *        pool. */
    public NameAndTypeConstant(final int nameIndex, final int descriptorIndex) {
      super(nameIndex);
      this.descriptorIndex = descriptorIndex;
    }
    /** @return the type or method descriptor. */
    public String getDescriptor() {
      return getUTF8(descriptorIndex);
    }
    /** @return the name. */
    public String getName() {
      return getUTF8(contentIndex);
    }
    @Override public String toString() {
      return getName() + ", " + getDescriptor();
    }
    @Override public String typeName() {
      return "NAME&TYPE";
    }
  }

  /** An entry in the constant pool which is represented as a reference to
   * another such entry.
   * @author Yossi Gil */
  public abstract class Reference extends Constant {
    /** Where is the content of this entry stored. */
    public final int contentIndex;

    /** Instantiate this class with a given constant pool reference
     * @param contentIndex where is the content of this entry stored in the
     *        constants' pool? */
    public Reference(final int contentIndex) {
      this.contentIndex = contentIndex;
    }
    @Override public String toString() {
      return getUTF8(contentIndex);
    }
    /** Return a UTF8 representation of a specific entry in the constants' pool
     * @param ¢ where to look for this entry?
     * @return a UTF8 representation of the content of this entry */
    @SuppressWarnings("synthetic-access") //
    final String getUTF8(final int ¢) {
      return !(pool[¢] instanceof UTF8) ? null : ((UTF8) pool[¢]).value;
    }
  }

  /** A representation of a String constant in the constants' pool
   * @author Yossi Gil */
  public class StringConstant extends Reference {
    /** Creates an instance for the specified index. Constant pool. Needed for
     * resolving the reference.
     * @param stringIndex index of an {@link UTF8} constant in the constants'
     *        pool */
    public StringConstant(final int stringIndex) {
      super(stringIndex);
    }
    @Override public String typeName() {
      return "STRING";
    }
  }

  /** A representation of a UTF8 literal in the constants' pool
   * @author Yossi Gil */
  public class UTF8 extends Literal {
    /** Actual content of this instance */
    public final String value;

    /** Creates an instance for the specified string.
     * @param string wrapped string. */
    public UTF8(final String string) {
      value = string;
    }
    @Override public String toString() {
      return value;
    }
    @Override public String typeName() {
      return "UTF8";
    }
  }
}
