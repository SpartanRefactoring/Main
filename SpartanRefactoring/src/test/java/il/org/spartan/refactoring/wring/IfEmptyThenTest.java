package il.org.spartan.refactoring.wring;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.refactoring.utils.*;

@SuppressWarnings({ "javadoc", "static-method" }) //
public class IfEmptyThenTest {
  private static final IfEmptyThen WRING = new IfEmptyThen();
  private static final Statement INPUT = Into.s("{if (b) ; else ff();}");
  private static final IfStatement IF = Extract.firstIfStatement(INPUT);

  @Test public void eligible() {
    assertTrue(WRING.eligible(IF));
  }
  @Test public void emptyThen() {
    assertTrue(Is.vacuousThen(IF));
  }
  @Test public void extractFirstIf() {
    assertNotNull(IF);
  }
  @Test public void inputType() {
    assertThat(INPUT, instanceOf(Block.class));
  }
  @Test public void scopeIncludes() {
    assertTrue(WRING.scopeIncludes(IF));
  }
}
