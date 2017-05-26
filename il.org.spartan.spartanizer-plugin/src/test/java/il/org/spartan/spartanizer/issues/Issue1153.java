package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;
import il.org.spartan.spartanizer.traversal.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-03-23 */
@SuppressWarnings("static-method")
public class Issue1153 {
  @Test public void a() {
    Configurations.all();
  }
  @Test public void b() {
    Configurations.allClone();
  }
  @Test public void b1() {
    new FieldInitializedSerialVersionUIDToHexadecimal().hashCode();
  }
  @Test public void b2() {
    final Class<FieldDeclaration> c = FieldDeclaration.class;
    assert c != null;
    final FieldInitializedSerialVersionUIDToHexadecimal x = new FieldInitializedSerialVersionUIDToHexadecimal();
    assert x != null;
    final Configuration configuration = Configurations.allClone();
    assert configuration != null;
    configuration.add(c, x);
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
  @Test public void l() {
    trimmingOf("class A { private long serialVersionUID = -999L;}")//
        .gives("class A { private long serialVersionUID = -0x3E7;}")//
        .stays();
  }
  @Test public void m() {
    trimmingOf("class A { private long serialVersionUID = -066L;}")//
        .stays();
  }
  @Test public void n() {
    trimmingOf("class A { private long serialVersionUID = -9043350929840336722L;}")//
        .gives("class A { private long serialVersionUID = -0x7D806FC1C854EF52L;}")//
        .stays();
  }
  @Test public void o() {
    trimmingOf("class A { private long serialVersionUID = 9043350929840336722L;}")//
        .gives("class A { private long serialVersionUID = 0x7D806FC1C854EF52L;}")//
        .stays();
  }
  @Test public void o1() {
    trimmingOf("class A { private long serialVersionUID = -1472927802038098123L;}")//
        .gives("class A { private long serialVersionUID = -0x1470E408344718CBL;}")//
        .stays();
  }
  /** Introduced by Yossi on Fri-Mar-24-13:58:53-IDT-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void test_classAPrivateLonga12345677899L() {
    trimmingOf("class A { private long a = 12345677899L; }") //
        .stays() //
    ;
  }
}