package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

@SuppressWarnings("static-method") public class Issue687 {
  @Test public void testGetNull() {
    assertNull(getAll.names(null));
  }

  @Test public void testemptyBlock() {
    assertTrue(getAll.names((Block) wizard.ast("{}")).isEmpty());
  }
}
