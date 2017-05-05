package il.org.spartan.classfiles.reify;

import org.junit.*;

import il.org.spartan.classfiles.*;
import il.org.spartan.classfiles.reify.ClassInfo.*;

/** @author Yossi Gil
 * @since 25 November 2011 */
@SuppressWarnings("static-method") public class BuilderTest {
  @Test public void classInfoFromBuilder() {
    assert new ClassInfo(new Builder(CLASSFILES.open(ClassFileTest.class))) != null;
  }

  @Test public void constantPoolEntityBuilder() {
    assert new ConstantPoolEntity(new Builder(CLASSFILES.open(ClassFileTest.class))) != null;
  }

  @Test public void constantPoolEntityStrings() {
    System.out.println(new ConstantPoolEntity(new Builder(CLASSFILES.open(ClassFileTest.class))).getReferencedStrings());
  }

  @Test public void constantPoolEntityUTF8() {
    assert new ConstantPoolEntity(new Builder(CLASSFILES.open(ClassFileTest.class))) != null;
  }

  @Test public void other() {
    assert new ClassInfo.Builder(ClassFileTest.class) != null;
  }

  @Test public void otherBuilderNoErrors() {
    assert !new Builder(CLASSFILES.open(ClassFileTest.class)).hasErrors();
  }

  @Test public void otherBuilderNonNull() {
    assert new Builder(CLASSFILES.open(ClassFileTest.class)).go() != null;
  }

  @Test public void otherMakeNonNull() {
    assert ClassInfo.make(ClassFileTest.class) != null;
  }

  @Test public void self() {
    assert new ClassInfo.Builder(this.getClass()) != null;
  }

  @Test public void selfGo() {
    assert new ClassInfo.Builder(this.getClass()).go() != null;
  }

  @Test public void selfMakeNonNull() {
    assert ClassInfo.make(this.getClass()) != null;
  }
}
