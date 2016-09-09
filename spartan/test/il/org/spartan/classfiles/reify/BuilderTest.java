/**
 *
 */
package il.org.spartan.classfiles.reify;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.classfiles.*;
import il.org.spartan.classfiles.reify.ClassInfo.*;

/** @author Yossi Gil
 * @since 25 November 2011 */
@SuppressWarnings("static-method") public class BuilderTest {
  @Test public void classInfoFromBuilder() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    assertNotNull(new ClassInfo(b));
  }

  @Test public void constantPoolEntityBuilder() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    assertNotNull(new ConstantPoolEntity(b));
  }

  @Test public void constantPoolEntityStrings() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    final ConstantPoolEntity c = new ConstantPoolEntity(b);
    System.out.println(c.getReferencedStrings());
  }

  @Test public void constantPoolEntityUTF8() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    assertNotNull(new ConstantPoolEntity(b));
  }

  @Test public void other() {
    assertNotNull(new ClassInfo.Builder(ClassFileTest.class));
  }

  @Test public void otherBuilderNoErrors() {
    assertFalse(new Builder(CLASSFILES.open(ClassFileTest.class)).hasErrors());
  }

  @Test public void otherBuilderNotNull() {
    final Builder b = new Builder(CLASSFILES.open(ClassFileTest.class));
    assertNotNull(b.go());
  }

  @Test public void otherMakeNotNull() {
    assertNotNull(ClassInfo.make(ClassFileTest.class));
  }

  @Test public void self() {
    assertNotNull(new ClassInfo.Builder(this.getClass()));
  }

  @Test public void selfGo() {
    assertNotNull(new ClassInfo.Builder(this.getClass()).go());
  }

  @Test public void selfMakeNotNull() {
    assertNotNull(ClassInfo.make(this.getClass()));
  }
}
