package il.org.spartan.classfiles.reify;

import static org.junit.Assert.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.*;

import il.org.spartan.bench.*;
import il.org.spartan.classfiles.*;
import il.org.spartan.classfiles.reify.ClassInfo.*;
import il.org.spartan.external.*;
import il.org.spartan.files.visitors.*;
import il.org.spartan.files.visitors.FileSystemVisitor.*;
import il.org.spartan.files.visitors.FileSystemVisitor.Action.*;
import il.org.spartan.utils.*;
import fluent.ly.*;

@SuppressWarnings("static-method") public class ClassFileTest {
  private static final String GOOD_FILE_PATH = "bin/il/ac/technion/cs/ssdl/classfiles/reify/ClassFileTest.class";
  private static final String BAD_FILE_PATH = "asdfasdfaafasdfas";

  @Test public void allAccessLevels() {
    final TypedEntity[] methods = ClassInfo.make(AllAccessLevels.class).methods;
    assert methods[0].isPrivate();
    assert methods[1].isDefault();
    assert methods[2].isProtected();
    assert methods[3].isPublic();
  }

  @Test public void allAccessLevelsNegative() {
    final TypedEntity[] methods = ClassInfo.make(AllAccessLevels.class).methods;
    assert !methods[1].isPrivate();
    assert !methods[2].isPrivate();
    assert !methods[3].isPrivate();
    assert !methods[0].isDefault();
    assert !methods[2].isDefault();
    assert !methods[3].isDefault();
    assert !methods[0].isProtected();
    assert !methods[1].isProtected();
    assert !methods[3].isProtected();
    assert !methods[0].isPublic();
    assert !methods[1].isPublic();
    assert !methods[2].isPublic();
  }

  @Test public void allPrimitiveFieldsArePrimitive() {
    for (final TypedEntity f : ClassInfo.make(AllPrimitiveFields.class).fields)
      assert f.type.isPrimitive();
  }

  @Test public void allPrimitiveFieldsAttributes() {
    for (final FlaggedEntity ¢ : ClassInfo.make(AllPrimitiveFields.class).fields)
      assertEquals(0, ¢.attributes.length);
  }

  @Test public void allPrimitiveFieldsCount() {
    assertEquals(7, ClassInfo.make(AllPrimitiveFields.class).fields.length);
  }

  @Test public void allPrimitiveFieldsModifiers() {
    for (final FlaggedEntity ¢ : ClassInfo.make(AllPrimitiveFields.class).fields)
      assertEquals(Modifier.FINAL | Modifier.PUBLIC, ¢.flags);
  }

  @Test public void allPrimitiveFieldsNames() {
    final NamedEntity[] fields = ClassInfo.make(AllPrimitiveFields.class).fields;
    assertEquals("firstField", fields[0].name);
    assertEquals("secondField", fields[1].name);
    assertEquals("thirdField", fields[2].name);
    assertEquals("fourthField", fields[3].name);
    assertEquals("fifthField", fields[4].name);
    assertEquals("sixthField", fields[5].name);
  }

  @Test public void allPrimitiveFieldsTypes() {
    final TypedEntity[] fields = ClassInfo.make(AllPrimitiveFields.class).fields;
    assertEquals("byte", fields[0].type + "");
    assertEquals("short", fields[1].type + "");
    assertEquals("long", fields[2].type + "");
    assertEquals("float", fields[3].type + "");
    assertEquals("double", fields[4].type + "");
    assertEquals("char", fields[5].type + "");
    assertEquals("boolean", fields[6].type + "");
  }

  @Test public void annotationCounts() {
    final ClassInfo __ = ClassInfo.make(Annotation.class);
    assertEquals(0, __.constructors.length);
    assertEquals(1, __.methods.length);
    assertEquals(0, __.fields.length);
    assertEquals(0, __.initializers.length);
  }

  @Test public void annotationFlags() {
    assertEquals(0, ClassInfo.make(Annotation.class).flags & ~(Modifier.ABSTRACT | Modifier.INTERFACE | FlaggedEntity.ANNOTATION));
  }

  @Test public void annotationIsAnnotation() {
    assert ClassInfo.make(Annotation.class).isAnnotation();
  }

  @Test public void annotationIsDeprecated() {
    assert !ClassInfo.make(Annotation.class).isDeprecated();
  }

  @Test public void annotationIsEnum() {
    assert !ClassInfo.make(Annotation.class).isEnum();
  }

  @Test public void annotationIsSynthetic() {
    assert !ClassInfo.make(Annotation.class).isSynthetic();
  }

  @Test public void arrayField() {
    final TypedEntity field = ClassInfo.make(ArrayField.class).fields[0];
    assertEquals("fieldName", field.name);
    assertEquals("[[[[I", field.descriptor);
    assertEquals("int[][][][]", field.type + "");
  }

  @Test public void badFile() {
    assert new File(BAD_FILE_PATH) != null;
  }

  @Test(expected = FileNotFoundException.class) public void badFileInputStream() throws FileNotFoundException {
    assert new FileInputStream(new File(BAD_FILE_PATH)) != null;
  }

  @Test public void badInputStream() {
    assertNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void byteCodeCount() {
    class __ {
    }
    assert ClassInfo.make(__.class).methods[0].getCode().codes.length > 0;
  }

  @Test public void classInfoFromBadPath() {
    assertNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void classInfoFromBadPathCLASSFILE() {
    assertNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void classWithCapitalL() {
    final TypedEntity field = ClassInfo.make(ClassWithCapitalL.class).fields[0];
    assertEquals("fieldName", field.name);
    assertEquals("Lil/ac/technion/cs/ssdl/classfiles/reify/ClassFileTest$ClassWithCapitalL;", field.descriptor);
    assertEquals("il.org.spartan.classfiles.reify.ClassFileTest$ClassWithCapitalL", field.type + "");
  }

  @Test public void codeAttributeExists() {
    class _ {
    }
    assert ClassInfo.make(_.class).methods[0].getCode() != null;
  }

  @Test public void codeAttributeMaxLocals() {
    class _ {
    }
    assertEquals(1, ClassInfo.make(_.class).methods[0].getCode().maxLocals);
  }

  @Test public void codeAttributeMaxStack() {
    class _ {
    }
    assertEquals(0, ClassInfo.make(_.class).methods[0].getCode().maxStack);
  }

  @Test public void complexClassConstructorCounts() {
    assertEquals(5, ClassInfo.make(LargeClass.class).constructorsCount());
  }

  @Test public void complexClassCounts() {
    assertEquals(1, ClassInfo.make(LargeClass.class).initializersCount());
  }

  @Test public void complexClassFieldsCounts() {
    assertEquals(6, ClassInfo.make(LargeClass.class).fieldsCount());
  }

  @Test public void complexClassFieldsInterfaceCounts() {
    assertEquals(3, ClassInfo.make(LargeClass.class).interfacesCount());
  }

  @Test public void complexClassFileAbstraction() {
    assertEquals(Abstraction.PLAIN, ClassInfo.make(LargeClass.class).abstraction());
  }

  @Test public void complexClassFileKind() {
    assertEquals(Kind.CLASS, ClassInfo.make(LargeClass.class).kind());
  }

  @Test public void complexClassMethodCounts() {
    assertEquals(8, ClassInfo.make(LargeClass.class).methodCount());
  }

  @Test public void complexClassMethodNames() {
    final MethodInfo[] ms = ClassInfo.make(LargeClass.class).methods;
    int i = 0;
    assertEquals("method0", ms[i++].name);
    assertEquals("method1", ms[i++].name);
    assertEquals("method2", ms[i++].name);
    assertEquals("method3", ms[i++].name);
    assertEquals("method4", ms[i++].name);
    assertEquals("method5", ms[i++].name);
    assertEquals("method6", ms[i++].name);
    assertEquals("method7", ms[i++].name);
  }

  @Test public void complexMethod() {
    assertEquals("java.lang.Object[][][] (java.lang.Void, java.lang.Object, int, float, double[][])",
        ClassInfo.make(ComplexMethod.class).methods[0].type + "");
  }

  @Test public void dataInputStreamFromPath() throws FileNotFoundException {
    assert new DataInputStream(new FileInputStream(new File(GOOD_FILE_PATH))) != null;
  }

  @Test public void deprecatedClassIsDeprecated() {
    assert ClassInfo.make(DeprecatedClass.class).isDeprecated();
    assert ClassInfo.make(DeprecatedClass.class).constructors[0].isDeprecated();
  }

  @Test public void deprecatedClassIsSynthetic() {
    assert !ClassInfo.make(DeprecatedClass.class).isSynthetic();
    assert !ClassInfo.make(DeprecatedClass.class).constructors[0].isSynthetic();
  }

  @Test public void emptlyClassInteface() {
    final ClassInfo __ = ClassInfo.make(EmptyClass.class);
    assert !__.isAbstract();
    assert !__.isInterface();
    assert __.isClass();
  }

  @Test public void emptyClassConstructorExceptions() {
    assertEquals(0, ClassInfo.make(EmptyClass.class).constructors[0].exceptions.length);
  }

  @Test public void emptyClassDefaultConstructorAttributesLength() {
    assertEquals(1, ClassInfo.make(EmptyClass.class).constructors[0].attributes.length);
  }

  @Test public void emptyClassDefaultConstructorAttributesName() {
    assertEquals("Code", ClassInfo.make(EmptyClass.class).constructors[0].attributes[0].name);
  }

  @Test public void emptyClassDefaultConstructorFlags() {
    assertEquals(Modifier.PUBLIC, ClassInfo.make(EmptyClass.class).constructors[0].flags);
  }

  @Test public void emptyClassDefaultConstructorgFlags() {
    assertEquals(Modifier.PUBLIC, ClassInfo.make(EmptyClass.class).constructors[0].flags);
  }

  @Test public void emptyClassDefaultConstructorIsSynthetic() {
    assert !ClassInfo.make(EmptyClass.class).constructors[0].isSynthetic();
  }

  @Test public void emptyClassDefaultConstructorName() {
    assertEquals("", ClassInfo.make(EmptyClass.class).constructors[0].name);
  }

  @Test public void emptyClassDefaultConstructorType() {
    assertEquals("()", ClassInfo.make(EmptyClass.class).constructors[0].type + "");
  }

  @Test public void emptyClassDescriptor() {
    final TypedEntity constructor = ClassInfo.make(EmptyClass.class).constructors[0];
    assertEquals("", constructor.name);
    assertEquals("()V", constructor.descriptor);
  }

  @Test public void emptyClassDGetConstructors() {
    assert ClassInfo.make(EmptyClass.class).constructors != null;
  }

  @Test public void emptyClassFlags() {
    assertEquals(Modifier.PUBLIC | Modifier.SYNCHRONIZED, ClassInfo.make(EmptyClass.class).flags);
  }

  @Test public void emptyClassIsDeprecated() {
    assert !ClassInfo.make(EmptyClass.class).isDeprecated();
    assert !ClassInfo.make(EmptyClass.class).constructors[0].isDeprecated();
  }

  @Test public void emptyClassNFields() {
    assertEquals(0, ClassInfo.make(EmptyClass.class).fields.length);
  }

  @Test public void emptyClassNInterfaces() {
    assertEquals(0, ClassInfo.make(EmptyClass.class).interfaces.length);
  }

  @Test public void emptyClassNMethods() {
    assertEquals(0, ClassInfo.make(EmptyClass.class).methods.length);
  }

  @Test public void emptyClassOneMethodNFields() {
    assertEquals(0, ClassInfo.make(EmptyClassOneMethod.class).fields.length);
  }

  @Test public void emptyClassOneMethodNMethods() {
    assertEquals(1, ClassInfo.make(EmptyClassOneMethod.class).methods.length);
  }

  @Test public void enumClassFile() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    assert !__.isAbstract();
    assert !__.isInterface();
    assert !__.isStatic();
  }

  @Test public void enumClassFileIsAnnotation() {
    assert !ClassInfo.make(Enum.class).isAnnotation();
  }

  @Test public void enumClassFileIsEnum() {
    assert ClassInfo.make(Enum.class).isEnum();
  }

  @Test public void enumConstructorIsSynthetic() {
    assert ClassInfo.make(Enum.class).constructors[0].isSynthetic();
  }

  @Test public void enumConstructorsCounts() {
    assertEquals(1, ClassInfo.make(Enum.class).constructors.length);
  }

  @Test public void enumIsDeprecated() {
    assert !ClassInfo.make(Enum.class).isDeprecated();
    assert !ClassInfo.make(Enum.class).constructors[0].isDeprecated();
  }

  @Test public void enumIsSynthetic() {
    assert !ClassInfo.make(Enum.class).isSynthetic();
  }

  @Test public void enumMethod() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    assertEquals(1, __.methods.length);
    assertEquals(0, __.constructors.length);
    assertEquals(0, __.initializers.length);
    assert __.methods[0].isAbstract();
    assert __.methods[0].isPublic();
  }

  @Test public void enumMethodCounts() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    assertEquals(2, __.methods.length);
    assertEquals(0, __.initializers.length);
  }

  @Test public void enumMethodName() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    assertEquals("myMethod", __.methods[0].name);
    assertEquals("values", __.methods[1].name);
    assert __.methods[1].isSynthetic();
  }

  @Test public void fileFromPath() {
    assert new File(GOOD_FILE_PATH) != null;
  }

  @Test public void findSelf() {
    assert this.getClass() != null;
  }

  @Test public void InitializerConstructors() {
    final NamedEntity[] ctros = ClassInfo.make(Initializer.class).constructors;
    assertEquals(2, ctros.length);
    assertEquals("", ctros[0].name);
    assertEquals("", ctros[1].name);
  }

  @Test public void inputStreamFromPath() throws FileNotFoundException {
    assert new FileInputStream(new File(GOOD_FILE_PATH)) != null;
  }

  @Test public void interfaceClassFile() {
    final ClassInfo __ = ClassInfo.make(Interface.class);
    assert __.isAbstract();
    assert __.isInterface();
  }

  @Test public void interfaceIsDeprecated() {
    assert !ClassInfo.make(Interface.class).isDeprecated();
  }

  @Test public void interfaceIsSynthetic() {
    assert !ClassInfo.make(Interface.class).isSynthetic();
  }

  @Test public void interfaceMethod() {
    final ClassInfo __ = ClassInfo.make(Interface.class);
    assertEquals(1, __.methods.length);
    assertEquals(0, __.constructors.length);
    assertEquals(0, __.initializers.length);
    assert __.methods[0].isAbstract();
    assert __.methods[0].isPublic();
  }

  @Test public void interfaceSuperClassName() {
    assertEquals(Object.class.getCanonicalName(), ClassInfo.make(Serializable.class).superClass);
  }

  @Test public void intFieldName() {
    final TypedEntity field = ClassInfo.make(IntField.class).fields[0];
    assertEquals("fieldName", field.name);
    assertEquals("I", field.descriptor);
    assertEquals("int", field.type + "");
  }

  @Test public void isClass() {
    assert !ClassInfo.make(Annotation.class).isClass();
    assert !ClassInfo.make(Enum.class).isClass();
    assert !ClassInfo.make(Interface.class).isClass();
    assert ClassInfo.make(EmptyClass.class).isClass();
  }

  @Test public void meFlags() {
    assertEquals(Modifier.PUBLIC | Modifier.SYNCHRONIZED, ClassInfo.make(this.getClass()).flags);
  }

  @Test public void meHasCorrectInteface() {
    assertEquals(Serializable.class.getCanonicalName(), ClassInfo.make(this.getClass()).interfaces[0] + "");
  }

  @Test public void meHasOneInteface() {
    assertEquals(1, ClassInfo.make(this.getClass()).interfaces.length);
  }

  @Test public void meName() {
    assertEquals(this.getClass().getCanonicalName(), ClassInfo.make(this.getClass()).name);
  }

  @Test public void meSuperClassName() {
    assertEquals(Object.class.getCanonicalName(), ClassInfo.make(this.getClass()).superClass);
  }

  @Test public void methodExceptionsLength() {
    class _ {
    }
    assertEquals(2, ClassInfo.make(_.class).methods[0].exceptions.length);
  }

  @Test public void methodExceptionsName() {
    class _ {
    }
    assertEquals(IOException.class.getName(), ClassInfo.make(_.class).methods[0].exceptions[0] + "");
    assertEquals(ArrayIndexOutOfBoundsException.class.getName(), ClassInfo.make(_.class).methods[0].exceptions[1] + "");
  }

  @Test public void methodWithParameters() {
    final TypedEntity method = ClassInfo.make(MethodWithParametrs.class).methods[0];
    assertEquals("method", method.name);
    assertEquals("(III)Ljava/lang/Integer;", method.descriptor);
  }

  @Test public void nameOneField() {
    assertEquals("fieldName", ClassInfo.make(OneField.class).fields[0].name);
  }

  @Test public void namesTwoAnnotatedFields() {
    final NamedEntity[] fields = ClassInfo.make(TwoAnnotatedFields.class).fields;
    assertEquals("firstField", fields[0].name);
    assertEquals("secondField", fields[1].name);
  }

  @Test public void namesTwoFields() {
    final NamedEntity[] fields = ClassInfo.make(TwoFields.class).fields;
    assertEquals("firstField", fields[0].name);
    assertEquals("secondField", fields[1].name);
  }

  @Test public void nameTwoMethods() {
    final ClassInfo __ = ClassInfo.make(TwoMethods.class);
    assertEquals("", __.constructors[0].name);
    assertEquals("firstMethod", __.methods[0].name);
    assertEquals("secondMethod", __.methods[1].name);
  }

  @Test public void noByteCodeEmptyClassConstructor() {
    assertNull(ClassInfo.make(EmptyClass.class).constructors[0].code);
  }

  @Test public void noByteCodeEmptyConstructor() {
    class _ {
    }
    assertNull(ClassInfo.make(_.class).constructors[0].code);
  }

  @Test public void noByteCodeEmptyConstructorCallingSuper() {
    class _ {
    }
    assertNull(ClassInfo.make(_.class).constructors[0].code);
  }

  @Test public void noExceptionsCount() {
    class _ {
    }
    assertEquals(2, ClassInfo.make(_.class).methods[0].exceptions.length);
  }

  @Test public void objecClassCreate() {
    assert ClassInfo.make(Object.class) != null;
  }

  @Test public void objectFieldName() {
    final TypedEntity field = ClassInfo.make(ObjectField.class).fields[0];
    assertEquals("fieldName", field.name);
    assertEquals("Ljava/lang/Object;", field.descriptor);
    assertEquals("java.lang.Object", field.type + "");
  }

  @Test public void objectSuperClassName() {
    assertNull(ClassInfo.make(Object.class).superClass);
  }

  @Test public void omeMethoExceptions() {
    assert !ClassInfo.make(Annotation.class).isSynthetic();
  }

  @Test public void oneFieldFieldCount() {
    assertEquals(1, ClassInfo.make(OneField.class).fields.length);
  }

  @Test public void oneFieldFieldIsDeperecated() {
    assert ClassInfo.make(OneField.class).fields[0].isDeprecated();
  }

  @Test public void oneFieldFieldIsSynthetic() {
    assert !ClassInfo.make(OneField.class).fields[0].isSynthetic();
  }

  @Test public void oneFieldFlags() {
    assertEquals(Modifier.FINAL | Modifier.PUBLIC | FlaggedEntity.DEPRECATED, ClassInfo.make(OneField.class).fields[0].flags);
  }

  @Test public void oneFieldIsDeperecated() {
    assert !ClassInfo.make(OneField.class).isDeprecated();
  }

  @Test public void oneFieldIsSynthetic() {
    assert !ClassInfo.make(OneField.class).isSynthetic();
  }

  @Test public void oneMethodFlags() {
    assertEquals(Modifier.FINAL | Modifier.PUBLIC, ClassInfo.make(OneMethod.class).methods[0].flags);
  }

  @Test public void oneMethodLengths() {
    final ClassInfo __ = ClassInfo.make(OneMethod.class);
    assertEquals(1, __.constructors.length);
    assertEquals(1, __.methods.length);
    assertEquals(0, __.fields.length);
    assertEquals(0, __.initializers.length);
  }

  @Test public void oneMethodName() {
    final ClassInfo __ = ClassInfo.make(OneMethod.class);
    assertEquals("", __.constructors[0].name);
    assertEquals("methodName", __.methods[0].name);
  }

  @Test public void oneMethodSize() {
    assertEquals(1, ClassInfo.make(OneMethod.class).methods.length);
  }

  @Test public void oneStaticMethodSize() {
    assertEquals(1, ClassInfo.make(OneMethod.class).methods.length);
  }

  @Test public void openBadPath() {
    assertNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void openEmptyClass() {
    assert ClassInfo.make(EmptyClass.class) != null;
  }

  @Test public void openPath() {
    assert ClassInfo.make(GOOD_FILE_PATH) != null;
  }

  @Test public void openPathCLASSFILES() {
    assert ClassInfo.make(this.getClass()) != null;
  }

  @Test public void openPathCLASSFILESBadPath() {
    assertNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void openSelf() {
    assert CLASSFILES.open(this.getClass()) != null;
  }

  @Test public void openSelfNoErrors() {
    final Builder b = new ClassInfo.Builder(this.getClass());
    b.go();
    assert !b.hasErrors();
  }

  @Test public void parseAllPathMethods() {
    final Parser p = new Parser();
    p.parse(CLASSPATH.asArray());
    System.out.println(p);
  }

  @Test public void parseAllProjectMethods() {
    final Parser p = new Parser();
    p.parse(".");
    System.out.println(p);
  }

  @Test public void parseAllWorspace() {
    final Parser p = new Parser();
    p.parse("..");
    System.out.println(p);
  }

  @Test public void parseByteCodesSelfFirstMethod() {
    assert ClassInfo.make(this.getClass()).methods[0].getCode().instructionsCount() > 0;
  }

  @Test public void parseByteCodesTwoMethods() {
    class _ {
      boolean __(final int __) {
        return __ % 1 == 0 ? __(__ * __) : __(__ % 7) != ___(__ * __ % 3);
      }

      boolean ___(final int __) {
        return __ == 1 == __(__);
      }
    }
    assert ClassInfo.make(_.class).methods[0].getCode().instructionsCount() > ClassInfo.make(_.class).methods[1].getCode().instructionsCount();
  }

  @Test public void parseComplexMethod() {
    class ClassWithComplexMethod {
    }
    ClassInfo.make(ClassWithComplexMethod.class).methods[0].getCode().instructionsCount();
  }

  @Test public void parseSelfAllMethods() {
    for (final MethodInfo ¢ : ClassInfo.make(this.getClass()).methods)
      ¢.getCode().instructionsCount();
  }

  @Test public void parseTableSwitch() {
    class _ {
    }
    assertEquals("tableSwitch", ClassInfo.make(_.class).methods[0].name);
    assert ClassInfo.make(_.class).methods[0].getCode().instructionsCount() > 20;
  }

  @Test public void readFromDataInputStreamFromPath() throws IOException {
    final DataInputStream f = new DataInputStream(new FileInputStream(new File(GOOD_FILE_PATH)));
    assertEquals(ClassInfo.Builder.MAGIC, f.readInt());
    f.close();
  }

  @Test public void readFromNoExceptionsReaderFromBadPath() {
    assertEquals(0, new RobustReader(BAD_FILE_PATH).readInt());
  }

  @Test public void readFromNoExceptionsReaderFromGoodPath() {
    assertEquals(ClassInfo.Builder.MAGIC, new RobustReader(GOOD_FILE_PATH).readInt());
  }

  @Test public void selfMakeNotNull() {
    assert ClassInfo.make(this.getClass()) != null;
  }

  @Test public void selfSource() {
    assertEquals(this.getClass().getSimpleName() + ".java", ClassInfo.make(this.getClass()).source);
  }

  @Test public void selfSourceNotNul() {
    assert ClassInfo.make(this.getClass()).source != null;
  }

  @Test public void singleStaticInitilizerLengths() {
    final ClassInfo __ = ClassInfo.make(SingleStaticInitializer.class);
    assertEquals(1, __.constructors.length);
    assertEquals(0, __.methods.length);
    assertEquals(0, __.fields.length);
    assertEquals(1, __.initializers.length);
  }

  @Test public void staticInitializerIsPrivate() {
    assert ClassInfo.make(StaticInitializer.class).initializers[0].isDefault();
  }

  @Test public void staticInitializerIsStatic() {
    assert ClassInfo.make(StaticInitializer.class).initializers[0].isStatic();
  }

  @Test public void staticInitializerNames() {
    final ClassInfo __ = ClassInfo.make(StaticInitializer.class);
    assertEquals(0, __.methods.length);
    assertEquals("", __.initializers[0].name);
    assertEquals("", __.constructors[0].name);
    assertEquals("", __.constructors[1].name);
  }

  @Test public void staticInitializerType() {
    assertEquals("()", ClassInfo.make(StaticInitializer.class).initializers[0].type + "");
  }

  @Test public void twoFieldsAreNotPrimtive() {
    for (final TypedEntity f : ClassInfo.make(TwoFields.class).fields)
      assert !f.type.isPrimitive();
  }

  @Test public void twoFieldsFlags() {
    final FlaggedEntity[] fields = ClassInfo.make(TwoFields.class).fields;
    assertEquals(Modifier.FINAL | Modifier.STATIC, fields[0].flags);
    assertEquals(Modifier.FINAL | Modifier.PRIVATE, fields[1].flags);
  }

  @Test public void twoMethodLengths() {
    final ClassInfo __ = ClassInfo.make(OneMethod.class);
    assertEquals(1, __.constructors.length);
    assertEquals(1, __.methods.length);
    assertEquals(0, __.fields.length);
    assertEquals(0, __.initializers.length);
  }

  @Test public void twoMethodsCount() {
    assertEquals(2, ClassInfo.make(TwoMethods.class).methods.length);
  }

  @Test public void twoMethodsFlags() {
    final FlaggedEntity[] methods = ClassInfo.make(TwoMethods.class).methods;
    assertEquals(Modifier.ABSTRACT, methods[0].flags);
    assertEquals(Modifier.FINAL | Modifier.PUBLIC | Modifier.STATIC, methods[1].flags);
  }

  @Test public void voidMethod() {
    assertEquals("()V", ClassInfo.make(VoidMethod.class).methods[0].descriptor);
    assertEquals("void ()", ClassInfo.make(VoidMethod.class).methods[0].type + "");
  }

  public abstract static class AllAccessLevels {
    public abstract void f4();

    protected abstract void f3();

    void f2() {
      f1();
    }

    private void f1() {
      f1();
    }
  }

  public static class AllPrimitiveFields {
    public final byte firstField = (byte) hashCode();
    public final short secondField = (short) hashCode();
    public final long thirdField = (byte) hashCode();
    public final float fourthField = hashCode();
    public final double fifthField = hashCode();
    public final char sixthField = toString().charAt(0);
    public final boolean seventhField = hashCode() > 100;
  }

  public static class ArrayField {
    int[][][][] fieldName;
  }

  public static class ClassWithCapitalL {
    ClassWithCapitalL fieldName = new ClassWithCapitalL();
  }

  public static class ComplexMethod {
    public final Object[][][] m(final Void v, final Object a, final int b, final float c, final double[][] dss) {
      ___.unused(v, a, box.it(b), box.it(c));
      return new Object[dss.length][][];
    }
  }

  @Deprecated public static class DeprecatedClass {
    @Deprecated DeprecatedClass() {
    }
  }

  public static class EmptyClass {
    // Empty
  }

  public static class EmptyClassOneMethod {
    void empty() throws IOException {
      throw new IOException();
    }
  }

  public abstract static class Initializer {
    {
      System.getenv();
      System.getenv("PATH");
      for (final String key : System.getenv().keySet())
        if (key != null)
          System.getenv(key);
    }

    Initializer() {
    }

    Initializer(final String s) {
      System.getenv(s);
    }
  }

  public static class IntField {
    int fieldName = 5;
  }

  public abstract static class MethodWithParametrs {
    public MethodWithParametrs() {
    }

    public final Integer method(final int a, final int b, final int c) {
      return Integer.valueOf(a + b + c + hashCode());
    }
  }

  public static class ObjectField {
    Object fieldName = new Object();
  }

  public static class OneField {
    @Deprecated public final String fieldName = null;
  }

  public static class OneMethod {
    public final Object methodName() {
      return this;
    }
  }

  public static class OneStaticMethod {
    public static String methodName() {
      return null;
    }
  }

  public static class Parser {
    int nFiles;
    int nMethods;
    int nCodes;
    int nInstructions;
    Stopper s = new Stopper();

    @Override public String toString() {
      return String.format(
          "Processed %s files, consisting of %s methods with %s code blocks\n" + "comprising a total of %s bytecode instructions in %s.",
          Unit.INTEGER.format(nFiles), Unit.INTEGER.format(nMethods), Unit.INTEGER.format(nCodes), Unit.INTEGER.format(nInstructions),
          Unit.NANOSECONDS.format(s.time())) + "";
    }

    void parse(final ClassInfo ¢) {
      parse(¢.methods);
    }

    void parse(final MethodInfo[] is) {
      ++nFiles;
      for (final MethodInfo ¢ : is)
        parseMethod(¢);
    }

    void parse(final String... path) {
      try {
        new ClassFilesVisitor(path, new FileOnlyAction() {
          File zipFile;

          @Override public void visitFile(final File f) {
            try {
              parse(ClassInfo.make(f));
            } catch (final RuntimeException ¢) {
              System.out.println("\n** " + "" + f + ": " + ¢);
            }
          }

          @Override public void visitZip(final File zip) {
            zipFile = zip;
          }

          @Override public void visitZipEntry(final String entryName, final InputStream s) {
            try {
              parse(ClassInfo.make(s));
            } catch (final RuntimeException e) {
              System.out.println(zipFile + ":" + entryName);
            }
          }
        }).go();
      } catch (final IOException ¢) {
        ¢.printStackTrace();
      } catch (final StopTraversal ¢) {
        ¢.printStackTrace();
      }
    }

    private void parse(final CodeEntity ¢) {
      ++nCodes;
      nInstructions += ¢.instructionsCount();
    }

    private void parseMethod(final MethodInfo ¢) {
      ++nMethods;
      if (¢.getCode() != null)
        parse(¢.getCode());
    }
  }

  public abstract static class SingleStaticInitializer {
    static {
      System.getenv();
      System.getenv("PATH");
      for (final String key : System.getenv().keySet())
        if (key != null)
          System.getenv(key);
    }
  }

  public abstract static class StaticInitializer {
    static {
      System.getenv();
      System.getenv("PATH");
      for (final String key : System.getenv().keySet())
        if (key != null)
          System.getenv(key);
    }

    StaticInitializer() {
    }

    StaticInitializer(final String s) {
      System.getenv(s);
    }
  }

  public static class TwoAnnotatedFields {
    @Deprecated @External private static final String firstField = null;
    @Deprecated final String secondField = null;
  }

  public static class TwoFields {
    static final String firstField = null;
    private final String secondField = null;

    public String getSecondField() {
      return secondField;
    }
  }

  public abstract static class TwoMethods {
    public static String secondMethod() {
      return null;
    }

    abstract String firstMethod(int a, int b);
  }

  public static class VoidMethod {
    public final void voidMethod() {
      voidMethod();
    }
  }

  @interface Annotation {
    int f();
  }

  enum Enum {
    ;
    public void myMethod() {
      myMethod();
    }
  }

  interface Interface {
    void method0();
  }

  static class LargeClass implements Serializable, Cloneable, Interface {
    private static final long serialVersionUID = 1;
    private static int field1;
    static Random field4 = new Random();
    static {
      field4 = new Random(field4.nextLong());
      for (int ¢ = 1; ¢ < 10; ++¢)
        field4.nextDouble();
    }
    static Random field5 = new Random(field4.nextLong() + method3());
    static {
      for (int ¢ = 1; ¢ < 20; ++¢)
        field5.nextDouble();
    }
    static Random field6 = new Random(field5.nextLong());
    static {
      for (int ¢ = 1; ¢ < 20; ++¢)
        field4 = new Random(field6.nextLong());
    }

    static int method3() {
      return field5.nextInt();
    }

    static int method4() {
      return field5.nextInt();
    }

    static int method5() {
      return field5.nextInt();
    }

    static int method6() {
      return field5.nextInt();
    }

    static int method7() {
      return field6.nextInt();
    }

    private final int field2;
    private final int field3;

    public LargeClass() {
      this(12);
    }

    public LargeClass(final byte a) {
      this(12 + a);
    }

    public LargeClass(final double d) {
      field1 = 1;
      field2 = (int) d;
      field3 = field2 + 3;
    }

    public LargeClass(final int field1, final int field2, final int field3) {
      LargeClass.field1 = field1;
      this.field2 = field2;
      this.field3 = field3;
    }

    LargeClass(final int a) {
      field1 = field4.nextInt() + a;
      field2 = field5.nextInt() + field1;
      field3 = field6.nextInt() + field2;
    }

    @Override public void method0() {
      method1();
    }

    int method1() {
      return method2(11);
    }

    private int method2(final int ¢) {
      return ¢ * ¢ + field3;
    }
  }
}
