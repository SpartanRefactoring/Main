package il.org.sparan.classfiles;

import static il.org.spartan.azzert.*;

import java.io.*;
import java.util.*;

import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.classfiles.*;
import il.org.spartan.classfiles.reify.*;

@SuppressWarnings("static-method") public class ClassProxyTest {
  private static final long serialVersionUID = 0x7556556FAE212ADEL;
  private final String stringSignature = "My String Signature";
  public final long publicInstanceLong = 123456789011L * serialVersionUID;
  public final float someFloat = 4.91F;
  public final double someDouble = 1.125;

  @Test public void testClassDollarForNameEntry() throws ClassNotFoundException {
    Class.forName("java.util.Map$Entry");
  }

  @Test public void testClassFileOfStringBuilder() throws IOException, ClassNotFoundException {
    for (@Nullable final String className : new ClassProxy<>(StringBuilder.class).getReferencedClasses())
      if (className != null)
        assert new ClassProxy<>(className) != null;
  }

  @Test public void testClassFileUsingMagic() throws IOException, ClassNotFoundException {
    @NotNull final int[] intConstants = new ClassProxy<>(ClassInfo.Builder.class).getReferencedInts();
    Arrays.sort(intConstants);
    assert Arrays.binarySearch(intConstants, 0xCAFEBABE) >= 0;
  }

  @Test(expected = ClassNotFoundException.class) public void testClassForNameEntry() throws ClassNotFoundException {
    Class.forName("java.util.Map.Entry");
  }

  @Test public void testClassForNameObject() throws ClassNotFoundException {
    Class.forName("java.lang.Object");
  }

  @Test(timeout = 200000) public void testClassName() {
    azzert.that(new ClassProxy<>(java.util.Map.Entry.class).className(), is("java.util.Map$Entry"));
  }

  @Test public void testClassNameAnonymousCLass() {
    azzert.that(new ClassProxy<Object>(new Object() {
      // Nothing to extend in this anonymous class.
    }.getClass()).className(), is(myName() + "$1"));
  }

  @Test public void testClassNameConstructor() {
    assert new ClassProxy<>("java.lang.Object") != null;
  }

  @Test public void testClassNameDoubleMemberCLass() {
    azzert.that(new ClassProxy<>(Inner1.Inner2.class).className(), is(myName() + "$Inner1$Inner2"));
  }

  @Test public void testClassNameLocalCLass() {
    class Local {
      Class<? extends Local> me() {
        return this.getClass();
      }
    }
    azzert.that(new ClassProxy<Local>(new Local().me()).className(), is(myName() + "$1Local"));
  }

  public void testClassNameMemberCLass() {
    azzert.that(new ClassProxy<>(Map.Entry.class).className(), is("java.util.Map$Entry"));
  }

  @Test public void testClassNameNormalCLass() {
    azzert.that(new ClassProxy<>(Object.class).className(), is("java.lang.Object"));
  }

  @Test public void testClassNameNormalMapCLass() {
    azzert.that(new ClassProxy<>(Map.class).className(), is("java.util.Map"));
  }

  @Test public void testClassNameStringCLass() {
    azzert.that(new ClassProxy<>(String.class).className(), is("java.lang.String"));
  }

  @Test public void testFindClassForNameObject() throws ClassNotFoundException {
    assert ClassProxy.findClass("java.lang.Object") != null;
  }

  @Test public void testFindClassMapEntry() throws ClassNotFoundException {
    assert ClassProxy.findClass("java.util.Map.Entry") != null;
  }

  @Test(expected = ClassNotFoundException.class) public void testFindSemicoloned() throws ClassNotFoundException {
    ClassProxy.findClass("java.lang.Object;");
  }

  @Test(expected = ClassNotFoundException.class) public void testForNameUnfound() throws ClassNotFoundException {
    assert Class.forName("java.util.Map.xxx") != null;
  }

  @Test(expected = ClassNotFoundException.class) public void testForNameUnfoundInProxy() throws ClassNotFoundException {
    assert ClassProxy.findClass("java.util.Map.xxx") != null;
  }

  @Test public void testGetReferencedClasses() throws IOException, ClassNotFoundException {
    assert new ClassProxy<>(Object.class).getReferencedClasses() != null;
  }

  @Test public void testGetReferencedClassesAllValidArrayList() throws IOException, ClassNotFoundException {
    for (final String className : new ClassProxy<>(ArrayList.class).getReferencedClasses())
      assert new ClassProxy<>(className) != null;
  }

  @Test public void testGetReferencedClassesAllValidObject() throws IOException, ClassNotFoundException {
    for (final String className : new ClassProxy<>(Object.class).getReferencedClasses())
      assert new ClassProxy<>(className) != null;
  }

  @Test public void testGetReferencedStrings() throws IOException, ClassNotFoundException {
    assert new ClassProxy<>(Object.class).getReferencedStrings() != null;
  }

  @Test public void testInvalidClassNameConstructor() {
    assert new ClassProxy<>("Invalid Class Name!") != null;
  }

  @Test(expected = ClassNotFoundException.class) public void testInvalidClassNameGetFields() throws SecurityException, ClassNotFoundException {
    new ClassProxy<>("Invalid Class Name!").getFields();
  }

  @Test(expected = ClassNotFoundException.class) public void testMakeSemicoloned() throws ClassNotFoundException {
    new ClassProxy<>("java.lang.Object;").getFields();
  }

  @Test public void testMeReferencingClassFileProxy() throws IOException, ClassNotFoundException {
    @NotNull final String[] referenced = new ClassProxy<>(ClassProxyTest.class).getReferencedClasses();
    Arrays.sort(referenced);
    assert Arrays.binarySearch(referenced, ClassProxy.class.getName()) >= 0;
  }

  @Test public void testMeUsingADouble() throws IOException, ClassNotFoundException {
    @NotNull final double[] doubles = new ClassProxy<>(ClassProxyTest.class).getReferencedDoubles();
    Arrays.sort(doubles);
    assert Arrays.binarySearch(doubles, someDouble) >= 0;
  }

  @Test public void testMeUsingAFloat() throws IOException, ClassNotFoundException {
    @NotNull final float[] floats = new ClassProxy<>(ClassProxyTest.class).getReferencedFloats();
    Arrays.sort(floats);
    assert Arrays.binarySearch(floats, someFloat) >= 0;
  }

  @Test public void testMeUsingDefaultLong() throws IOException, ClassNotFoundException {
    @NotNull final long[] longConstants = new ClassProxy<>(ClassProxyTest.class).getReferencedLongs();
    Arrays.sort(longConstants);
    assert Arrays.binarySearch(longConstants, serialVersionUID) >= 0;
  }

  @Test public void testMeUsingPublicInstanceLong() throws IOException, ClassNotFoundException {
    @NotNull final long[] longConstants = new ClassProxy<>(ClassProxyTest.class).getReferencedLongs();
    Arrays.sort(longConstants);
    assert Arrays.binarySearch(longConstants, publicInstanceLong) >= 0;
  }

  @Test public void testMeUsingSerialVersionUID() throws IOException, ClassNotFoundException {
    @NotNull final long[] longConstants = new ClassProxy<>(ClassProxyTest.class).getReferencedLongs();
    Arrays.sort(longConstants);
    assert Arrays.binarySearch(longConstants, serialVersionUID) >= 0;
  }

  @Test public void testMeUsingString() throws IOException, ClassNotFoundException {
    @NotNull final String[] stringConstants = new ClassProxy<>(ClassProxyTest.class).getReferencedStrings();
    Arrays.sort(stringConstants);
    assert Arrays.binarySearch(stringConstants, stringSignature) >= 0;
  }

  @Test public void testNameFormat() throws ClassNotFoundException {
    assert ClassProxy.findClass("java.util.Map.Entry") != null;
    assert ClassProxy.findClass("java.util.Map$Entry") != null;
  }

  @Test public void testNormalizeNameObject() {
    azzert.that(ClassProxy.normalizeClassName("java.lang.Object"), is("java.lang.Object"));
    azzert.that(ClassProxy.normalizeClassName("java.util.Map$Entry"), is("java.util.Map$Entry"));
    azzert.that(ClassProxy.normalizeClassName("java.util.Map.Entry"), is("java.util.Map$Entry"));
  }

  @Test public void testReflectiveConstructor() {
    assert new ClassProxy<>(Object.class) != null;
  }

  private String myName() {
    return this.getClass().getName();
  }

  static class Inner1 {
    static class Inner2 {
      int f;
    }
  }
}
