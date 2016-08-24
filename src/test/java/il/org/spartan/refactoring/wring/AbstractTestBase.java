package il.org.spartan.refactoring.wring;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;
import org.junit.runners.Parameterized.*;

import il.org.spartan.refactoring.spartanizations.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "javadoc" }) //
public class AbstractTestBase {
  protected static Collection<Object[]> collect(final String[][] cases) {
    final Collection<Object[]> $ = new ArrayList<>(cases.length);
    for (final String[] c : cases) {
      if (c == null)
        break;
      $.add(c);
    }
    return $;
  }

  /** The name of the specific test for this transformation */
  @Parameter(0) public String name;
  /** Where the input text can be found */
  @Parameter(1) public String input;

  @Test public void peelableinput() {
    if (input != null)
      assertEquals(input, Wrap.STATEMENT_OR_SOMETHING_THAT_MAY_APPEAR_IN_A_METHOD.off(Wrap.STATEMENT_OR_SOMETHING_THAT_MAY_APPEAR_IN_A_METHOD.on(input)));
  }
}
