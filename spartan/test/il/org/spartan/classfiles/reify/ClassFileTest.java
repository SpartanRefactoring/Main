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
    final FlaggedEntity[] fields = ClassInfo.make(AllPrimitiveFields.class).fields;
    for (final FlaggedEntity f : fields)
      assertEquals(0, f.attributes.length);
  }

  @Test public void allPrimitiveFieldsCount() {
    final NamedEntity[] fields = ClassInfo.make(AllPrimitiveFields.class).fields;
    assertEquals(7, fields.length);
  }

  @Test public void allPrimitiveFieldsModifiers() {
    final FlaggedEntity[] fields = ClassInfo.make(AllPrimitiveFields.class).fields;
    for (final FlaggedEntity f : fields)
      assertEquals(Modifier.FINAL | Modifier.PUBLIC, f.flags);
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
    assertEquals("byte", fields[0].type.toString());
    assertEquals("short", fields[1].type.toString());
    assertEquals("long", fields[2].type.toString());
    assertEquals("float", fields[3].type.toString());
    assertEquals("double", fields[4].type.toString());
    assertEquals("char", fields[5].type.toString());
    assertEquals("boolean", fields[6].type.toString());
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
    assertEquals("int[][][][]", field.type.toString());
  }

  @Test public void badFile() {
    final File f = new File(BAD_FILE_PATH);
    assert null != f;
  }

  @Test(expected = FileNotFoundException.class) public void badFileInputStream() throws FileNotFoundException {
    final File f = new File(BAD_FILE_PATH);
    assert null != new FileInputStream(f);
  }

  @Test public void badInputStream() {
    assertNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void byteCodeCount() {
    class __ {
      // empty
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
    assertEquals("il.org.spartan.classfiles.reify.ClassFileTest$ClassWithCapitalL", field.type.toString());
  }

  @Test public void codeAttributeExists() {
    class __ {
    }
    assert null != ClassInfo.make(__.class).methods[0].getCode();
  }

  @Test public void codeAttributeMaxLocals() {
    class __ {
    }
    assertEquals(1, ClassInfo.make(__.class).methods[0].getCode().maxLocals);
  }

  @Test public void codeAttributeMaxStack() {
    class __ {
    }
    assertEquals(0, ClassInfo.make(__.class).methods[0].getCode().maxStack);
  }

  @Test public void complexClassConstructorCounts() {
    final ClassInfo c = ClassInfo.make(LargeClass.class);
    assertEquals(5, c.constructorsCount());
  }

  @Test public void complexClassCounts() {
    final ClassInfo c = ClassInfo.make(LargeClass.class);
    assertEquals(1, c.initializersCount());
  }

  @Test public void complexClassFieldsCounts() {
    final ClassInfo c = ClassInfo.make(LargeClass.class);
    assertEquals(6, c.fieldsCount());
  }

  @Test public void complexClassFieldsInterfaceCounts() {
    final ClassInfo c = ClassInfo.make(LargeClass.class);
    assertEquals(3, c.interfacesCount());
  }

  @Test public void complexClassFileAbstraction() {
    final ClassInfo c = ClassInfo.make(LargeClass.class);
    assertEquals(Abstraction.PLAIN, c.abstraction());
  }

  @Test public void complexClassFileKind() {
    final ClassInfo c = ClassInfo.make(LargeClass.class);
    assertEquals(Kind.CLASS, c.kind());
  }

  @Test public void complexClassMethodCounts() {
    final ClassInfo c = ClassInfo.make(LargeClass.class);
    assertEquals(8, c.methodCount());
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
        ClassInfo.make(ComplexMethod.class).methods[0].type.toString());
  }

  @Test public void dataInputStreamFromPath() throws FileNotFoundException {
    assert null != new DataInputStream(new FileInputStream(new File(GOOD_FILE_PATH)));
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
    assertEquals("()", ClassInfo.make(EmptyClass.class).constructors[0].type.toString());
  }

  @Test public void emptyClassDescriptor() {
    final TypedEntity constructor = ClassInfo.make(EmptyClass.class).constructors[0];
    assertEquals("", constructor.name);
    assertEquals("()V", constructor.descriptor);
  }

  @Test public void emptyClassDGetConstructors() {
    assert null != ClassInfo.make(EmptyClass.class).constructors;
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
    final ClassInfo __ = ClassInfo.make(Enum.class);
    assert !__.isAnnotation();
  }

  @Test public void enumClassFileIsEnum() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    assert __.isEnum();
  }

  @Test public void enumConstructorIsSynthetic() {
    assert ClassInfo.make(Enum.class).constructors[0].isSynthetic();
  }

  @Test public void enumConstructorsCounts() {
    final ClassInfo __ = ClassInfo.make(Enum.class);
    assertEquals(1, __.constructors.length);
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
    assert null != new File(GOOD_FILE_PATH);
  }

  @Test public void findSelf() {
    assert null != this.getClass();
  }

  @Test public void InitializerConstructors() {
    final NamedEntity[] ctros = ClassInfo.make(Initializer.class).constructors;
    assertEquals(2, ctros.length);
    assertEquals("", ctros[0].name);
    assertEquals("", ctros[1].name);
  }

  @Test public void inputStreamFromPath() throws FileNotFoundException {
    assert null != new FileInputStream(new File(GOOD_FILE_PATH));
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
    assertEquals("int", field.type.toString());
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
    final ClassInfo __ = ClassInfo.make(this.getClass());
    assertEquals(Serializable.class.getCanonicalName(), __.interfaces[0].toString());
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
    class __ {
    }
    assertEquals(2, ClassInfo.make(__.class).methods[0].exceptions.length);
  }

  @Test public void methodExceptionsName() {
    class __ {
    }
    assertEquals(IOException.class.getName(), ClassInfo.make(__.class).methods[0].exceptions[0].toString());
    assertEquals(ArrayIndexOutOfBoundsException.class.getName(), ClassInfo.make(__.class).methods[0].exceptions[1].toString());
  }

  @Test public void methodWithParameters() {
    final TypedEntity method = ClassInfo.make(MethodWithParametrs.class).methods[0];
    assertEquals("method", method.name);
    assertEquals("(III)Ljava/lang/Integer;", method.descriptor);
  }

  @Test public void nameOneField() {
    final ClassInfo __ = ClassInfo.make(OneField.class);
    assertEquals("fieldName", __.fields[0].name);
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
    class __ {
    }
    assertNull(ClassInfo.make(__.class).constructors[0].code);
  }

  @Test public void noByteCodeEmptyConstructorCallingSuper() {
    class __ {
    }
    assertNull(ClassInfo.make(__.class).constructors[0].code);
  }

  @Test public void noExceptionsCount() {
    class __ {
    }
    assertEquals(2, ClassInfo.make(__.class).methods[0].exceptions.length);
  }

  @Test public void objecClassCreate() {
    assert null != ClassInfo.make(Object.class);
  }

  @Test public void objectFieldName() {
    final TypedEntity field = ClassInfo.make(ObjectField.class).fields[0];
    assertEquals("fieldName", field.name);
    assertEquals("Ljava/lang/Object;", field.descriptor);
    assertEquals("java.lang.Object", field.type.toString());
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
    final int flags = ClassInfo.make(OneField.class).fields[0].flags;
    assertEquals(Modifier.FINAL | Modifier.PUBLIC | FlaggedEntity.DEPRECATED, flags);
  }

  @Test public void oneFieldIsDeperecated() {
    assert !ClassInfo.make(OneField.class).isDeprecated();
  }

  @Test public void oneFieldIsSynthetic() {
    assert !ClassInfo.make(OneField.class).isSynthetic();
  }

  @Test public void oneMethodFlags() {
    final int flags = ClassInfo.make(OneMethod.class).methods[0].flags;
    assertEquals(Modifier.FINAL | Modifier.PUBLIC, flags);
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
    assert null != ClassInfo.make(EmptyClass.class);
  }

  @Test public void openPath() {
    assert null != ClassInfo.make(GOOD_FILE_PATH);
  }

  @Test public void openPathCLASSFILES() {
    assert null != ClassInfo.make(this.getClass());
  }

  @Test public void openPathCLASSFILESBadPath() {
    assertNull(ClassInfo.make(BAD_FILE_PATH));
  }

  @Test public void openSelf() {
    assert null != CLASSFILES.open(this.getClass());
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
    final CodeEntity c = ClassInfo.make(this.getClass()).methods[0].getCode();
    assert 0 < c.instructionsCount();
  }

  @Test public void parseByteCodesTwoMethods() {
    class __ {
      boolean ____(final int __) {
        return __ % 1 == 0 ? ____(__ * __) : ____(__ % 7) != ______(__ * __ % 3);
      }

      boolean ______(final int __) {
        return __ == 1 == ____(__);
      }
    }
    final CodeEntity c0 = ClassInfo.make(__.class).methods[0].getCode();
    final CodeEntity c1 = ClassInfo.make(__.class).methods[1].getCode();
    assert c0.instructionsCount() > c1.instructionsCount();
  }

  @Test public void parseComplexMethod() {
    class ClassWithComplexMethod {
    }
    ClassInfo.make(ClassWithComplexMethod.class).methods[0].getCode().instructionsCount();
  }

  @Test public void parseSelfAllMethods() {
    for (final MethodInfo m : ClassInfo.make(this.getClass()).methods)
      m.getCode().instructionsCount();
  }

  @Test public void parseTableSwitch() {
    class __ {
    }
    assertEquals("tableSwitch", ClassInfo.make(__.class).methods[0].name);
    assert ClassInfo.make(__.class).methods[0].getCode().instructionsCount() > 20;
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
    assert null != ClassInfo.make(this.getClass());
  }

  @Test public void selfSource() {
    assertEquals(this.getClass().getSimpleName() + ".java", ClassInfo.make(this.getClass()).source);
  }

  @Test public void selfSourceNotNul() {
    assert null != ClassInfo.make(this.getClass()).source;
  }

  @Test public void singleStaticInitilizerLengths() {
    final ClassInfo __ = ClassInfo.make(SingleStaticInitializer.class);
    assertEquals(1, __.constructors.length);
    assertEquals(0, __.methods.length);
    assertEquals(0, __.fields.length);
    assertEquals(1, __.initializers.length);
  }

  @Test public void staticInitializerIsPrivate() {
    final InitializerInfo[] initializers = ClassInfo.make(StaticInitializer.class).initializers;
    assert initializers[0].isDefault();
  }

  @Test public void staticInitializerIsStatic() {
    final InitializerInfo[] initializers = ClassInfo.make(StaticInitializer.class).initializers;
    assert initializers[0].isStatic();
  }

  @Test public void staticInitializerNames() {
    final ClassInfo __ = ClassInfo.make(StaticInitializer.class);
    assertEquals(0, __.methods.length);
    assertEquals("", __.initializers[0].name);
    assertEquals("", __.constructors[0].name);
    assertEquals("", __.constructors[1].name);
  }

  @Test public void staticInitializerType() {
    final InitializerInfo[] initializers = ClassInfo.make(StaticInitializer.class).initializers;
    assertEquals("()", initializers[0].type.toString());
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
    assertEquals("void ()", ClassInfo.make(VoidMethod.class).methods[0].type.toString());
  }

  public static abstract class AllAccessLevels {
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
    int[][][][] fieldName = null;
  }

  public static class ClassWithCapitalL {
    ClassWithCapitalL fieldName = new ClassWithCapitalL();
  }

  public static class ComplexMethod {
    public final Object[][][] m(final Void v, final Object a, final int b, final float c, final double[][] d) {
      ___.unused(v, a, Box.it(b), Box.it(c));
      return new Object[d.length][][];
    }
  }

  @Deprecated public static class DeprecatedClass {
    @Deprecated DeprecatedClass() {
      super();
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

  public static abstract class Initializer {
    {
      System.getenv();
      System.getenv("PATH");
      for (final String key : System.getenv().keySet())
        if (key != null)
          System.getenv(key);
    }

    Initializer() {
      // Empty constructor
    }

    Initializer(final String s) {
      System.getenv(s);
    }
  }

  public static class IntField {
    int fieldName = 5;
  }

  public static abstract class MethodWithParametrs {
    public MethodWithParametrs() {
      super();
    }

    public final Integer method(final int a, final int b, final int c) {
      return new Integer(a + b + c + hashCode());
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
    public final static String methodName() {
      return null;
    }
  }

  public static class Parser {
    int nFiles = 0;
    int nMethods = 0;
    int nCodes = 0;
    int nInstructions = 0;
    Stopper s = new Stopper();

    @Override public String toString() {
      final long stop = s.time();
      return String
          .format(
              //
              "Processed %s files, consisting of %s methods with %s code blocks\n" + //
                  "comprising a total of %s bytecode instructions in %s.", //
              Unit.INTEGER.format(nFiles), //
              Unit.INTEGER.format(nMethods), //
              Unit.INTEGER.format(nCodes), //
              Unit.INTEGER.format(nInstructions), //
              Unit.NANOSECONDS.format(stop)) //
          .toString();
    }

    void parse(final ClassInfo c) {
      parse(c.methods);
    }

    void parse(final MethodInfo[] methods) {
      nFiles++;
      for (final MethodInfo m : methods)
        parseMethod(m);
    }

    void parse(final String... path) {
      try {
        new ClassFilesVisitor(path, new FileOnlyAction() {
          File zipFile = null;

          @Override public void visitFile(final File f) {
            try {
              parse(ClassInfo.make(f));
            } catch (final RuntimeException e) {
              System.out.println("\n** " + f.toString() + ": " + e);
            }
          }

          @Override public void visitZip(final File zip) {
            zipFile = zip;
          }

          @Override public void visitZipEntry(final String entryName, final InputStream stream) {
            try {
              parse(ClassInfo.make(stream));
            } catch (final RuntimeException e) {
              System.out.println(zipFile + ":" + entryName);
            }
          }
        }).go();
      } catch (final IOException e) {
        e.printStackTrace();
      } catch (final StopTraversal e) {
        e.printStackTrace();
      }
    }

    private void parse(final CodeEntity c) {
      nCodes++;
      nInstructions += c.instructionsCount();
    }

    private void parseMethod(final MethodInfo m) {
      nMethods++;
      if (m.getCode() != null)
        parse(m.getCode());
    }
  }

  public static abstract class SingleStaticInitializer {
    static {
      System.getenv();
      System.getenv("PATH");
      for (final String key : System.getenv().keySet())
        if (key != null)
          System.getenv(key);
    }
  }

  public static abstract class StaticInitializer {
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
    @External @Deprecated static private final String firstField = null;
    @Deprecated final String secondField = null;
  }

  public static class TwoFields {
    static final String firstField = null;
    private final String secondField = null;

    public String getSecondField() {
      return secondField;
    }
  }

  public static abstract class TwoMethods {
    public static final String secondMethod() {
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
    public void method0();
  }

  static class LargeClass implements Serializable, Cloneable, Interface {
    private static final long serialVersionUID = 1L;
    private static int field1;
    static Random field4 = new Random();
    static {
      field4 = new Random(field4.nextLong());
      for (int i = 1; i < 10; i++)
        field4.nextDouble();
    }
    static Random field5 = new Random(field4.nextLong() + method3());
    static {
      for (int i = 1; i < 20; i++)
        field5.nextDouble();
    }
    static Random field6 = new Random(field5.nextLong());
    static {
      for (int i = 1; i < 20; i++)
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

    private int method2(final int i) {
      return i * i + field3;
    }
  }
}
