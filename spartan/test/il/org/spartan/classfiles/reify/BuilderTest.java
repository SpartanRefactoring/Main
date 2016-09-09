/**
 *
 */
package il.org.spartan.classfiles.reify;

import org.junit.*;

import il.org.spartan.classfiles.*;
import il.org.spartan.classfiles.reify.ClassInfo.*;

/** @author Yossi Gil
 * @since 25 November 2011 */
@SuppressWarnings("static-method") public class BuilderTest {
  @Test public void classInfoFromBuilder() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    assert null != new ClassInfo(b);
  }

  @Test public void constantPoolEntityBuilder() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    assert null != new ConstantPoolEntity(b);
  }

  @Test public void constantPoolEntityStrings() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    final ConstantPoolEntity c = new ConstantPoolEntity(b);
    System.out.println(c.getReferencedStrings());
  }

  @Test public void constantPoolEntityUTF8() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    assert null != new ConstantPoolEntity(b);
  }

  @Test public void other() {
    assert null != new ClassInfo.Builder(ClassFileTest.class);
  }

  @Test public void otherBuilderNoErrors() {
    assert !new Builder(CLASSFILES.open(ClassFileTest.class)).hasErrors();
  }

  @Test public void otherBuilderNotNull() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    assert null != b.go();
  }

  @Test public void otherMakeNotNull() {
    assert null != ClassInfo.make(ClassFileTest.class);
  }

  @Test public void self() {
    assert null != new ClassInfo.Builder(this.getClass());
  }

  @Test public void selfGo() {
    assert null != new ClassInfo.Builder(this.getClass()).go();
  }

  @Test public void selfMakeNotNull() {
    assert null != ClassInfo.make(this.getClass());
  }
}
