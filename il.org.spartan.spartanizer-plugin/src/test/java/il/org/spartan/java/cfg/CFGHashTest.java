package il.org.spartan.java.cfg;

import org.junit.*;
import org.junit.runner.*;

/** TODO Roth: delete this file, an example
 * @author Ori Roth
 * @since 2017-10-03 */
@RunWith(HashRunner.class)
public class CFGHashTest {
  class C1 {
    public int i;
  }

  class C2 extends C1 {
    //
  }

  @Focus C2 c = new C2();

  @Test public void a() {
    c.i = 2;
  }
  @Test public void b() {
    //
  }
  @Test public void c() {
    c.i = 2;
  }
  @Test public void d() {
    c.i = 1;
  }
}
