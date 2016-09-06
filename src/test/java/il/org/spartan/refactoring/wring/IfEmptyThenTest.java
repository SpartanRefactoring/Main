package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc", "static-method" }) //
public class IfEmptyThenTest {
  private static final Statement INPUT = into.s("{if (b) ; else ff();}");
  private static final IfStatement IF = extract.firstIfStatement(INPUT);
  private static final IfEmptyThen WRING = new IfEmptyThen();

  @Test public void eligible() {
    assert WRING.eligible(IF);
  }

  @Test public void emptyThen() {
    assert iz.vacuousThen(IF);
  }

  @Test public void extractFirstIf() {
    assert IF != null;
  }

  @Test public void inputType() {
    azzert.that(INPUT, instanceOf(Block.class));
  }

  @Test public void scopeIncludes() {
    assert WRING.scopeIncludes(IF);
  }
}
