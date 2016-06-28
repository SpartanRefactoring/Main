package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.spartanizations.TESTUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import il.org.spartan.hamcrest.*;
import il.org.spartan.refactoring.utils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

@SuppressWarnings({ "javadoc", "static-method" })//
public class IfEmptyThenTest {
  private static final IfEmptyThen WRING = new IfEmptyThen();
  private static final Statement INPUT = Into.s("{if (b) ; else ff();}");
  private static final IfStatement IF = Extract.firstIfStatement(INPUT);

  @Test public void eligible() {
    JunitHamcrestWrappper.assertTrue(WRING.eligible(IF));
  }
  @Test public void emptyThen() {
    JunitHamcrestWrappper.assertTrue(Is.vacuousThen(IF));
  }
  @Test public void extractFirstIf() {
    assertThat(IF, notNullValue());
  }
  @Test public void inputType() {
    assertThat(INPUT, instanceOf(Block.class));
  }
  @Test public void scopeIncludes() {
    JunitHamcrestWrappper.assertTrue(WRING.scopeIncludes(IF));
  }
}
