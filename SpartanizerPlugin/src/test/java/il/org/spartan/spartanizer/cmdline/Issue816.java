package il.org.spartan.spartanizer.cmdline;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runners.*;

/** Tests of {@link cmdline.TrimmerLog}
 * @author AnnaBel7
 * @author michalcohen
 * @since Nov 10, 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue816 {
  @Test public void a() {
    assertEquals(10, TrimmerLog.getMaxApplications());
  }
  @Test public void b() {
    assertEquals(20, TrimmerLog.getMaxTips());
  }
  @Test public void c() {
    assertEquals(30, TrimmerLog.getMaxVisitations());
  }
  @Test public void d() {
    TrimmerLog.setMaxApplications(50);
    assertEquals(50, TrimmerLog.getMaxApplications());
  }
  @Test public void e() {
    TrimmerLog.setMaxTips(50);
    assertEquals(50, TrimmerLog.getMaxTips());
  }
  @Test public void f() {
    TrimmerLog.setMaxVisitations(50);
    assertEquals(50, TrimmerLog.getMaxVisitations());
  }
}
