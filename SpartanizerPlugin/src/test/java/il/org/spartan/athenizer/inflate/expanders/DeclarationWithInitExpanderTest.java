package il.org.spartan.athenizer.inflate.expanders;

import org.junit.*;

import static il.org.spartan.athenizer.inflate.expanders.ExpanderTestUtils.*;

/** Unit tests for {@link DeclarationWithInitExpander}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class DeclarationWithInitExpanderTest {
  @Test public void a() {
    expansionOf("int a = 0;").gives("int a;" //
        + "a = 0;").stays();
  }

  @Test public void b() {
    expansionOf("int a = f();").gives("int a;" //
        + "a = f();").stays();
  }

  @Test public void c() {
    expansionOf("final String[] command = { \"/bin/bash\", \"-c\", shellCommand };").stays();
  }
  
  @Test public void d() {
    expansionOf("@SuppressWarnings(\"unchecked\") int a = f();").stays();
  }
}
