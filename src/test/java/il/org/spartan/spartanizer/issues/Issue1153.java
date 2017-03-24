package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;
import org.junit.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tippers.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-23 */
@SuppressWarnings("static-method")
public class Issue1153 {
  @Test public void a() {
    Toolbox.defaultInstance();
  }

  @Test public void b() {
    Toolbox.freshCopyOfAllTippers();
  }

  @Test public void b1() {
    new FieldSerialVersionUIDToHexadecimal().hashCode();
  }

  @Test public void b2() {
    @NotNull final Class<FieldDeclaration> c = FieldDeclaration.class;
    assert c != null;
    @NotNull final FieldSerialVersionUIDToHexadecimal x = new FieldSerialVersionUIDToHexadecimal();
    assert x != null;
    @NotNull final Toolbox toolbox = new Toolbox();
    assert toolbox != null;
    toolbox.add(c, x);
  }

  @Test public void c() {
    trimmingOf("class A { private static final long serialVersionUID = 3405687738L;}")//
        .gives("class A { private static final long serialVersionUID = 0xCAFEABBAL;}")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("class A { private static final long serialVersionUID = 0x2;}")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("class A { private static final long serialVersionUID = 02;}")//
        .stays();
  }

  @Test public void f() {
    trimmingOf("class A { private static final long serialVersionUID = 1;}")//
        .stays();
  }

  @Test public void f1() {
    trimmingOf("class A { private static final long serialVersionUID = 2;}")//
        .stays();
  }

  @Test public void g() {
    trimmingOf("class A { private static final long serialVersionUID = 0x2;}")//
        .stays();
  }

  @Test public void h() {
    trimmingOf("class A { private static final long serialVersionUID = 0X2;}")//
        .stays();
  }

  @Test public void i() {
    trimmingOf("class A { private static final long serialVersionUID = 10;}")//
        .stays();
  }

  @Test public void j() {
    trimmingOf("class A { private static final long serialVersionUID = 0XDeadL;}")//
        .stays();
  }
@Test public void k() {
    trimmingOf("class A { private long serialVersionUID = 12345677899L;}")//
        .gives("class A { private long serialVersionUID = 0x2DFDC184BL;}")//
        .stays();
  }
/** Introduced by Yossi on Fri-Mar-24-13:58:53-IDT-2017 
(code automatically generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java')*/
@Test public void test_classAPrivateLonga12345677899L() {
   trimmingOf("class A { private long a = 12345677899L; }") //
  .stays() //
  ;
}
}