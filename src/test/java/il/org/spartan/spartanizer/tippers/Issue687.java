package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** Tests for issue 687
 * @author Raviv Rachmiel
 * @author Kfir Marx
 * @since 2-11-2016 */
@SuppressWarnings("static-method")
public class Issue687 {
  private static void forceTypeAtCompileTime(@SuppressWarnings("unused") final List<Name> __) {
    assert true;
  }

  @Test public void testGetNull() {
    assertNull(getAll.names(null));
  }

  @Test public void testReturnType() {
    forceTypeAtCompileTime(getAll.names((Block) wizard.ast("{}")));
  }

  @Test public void testGetEmpty() {
    assertTrue(getAll.names((Block) wizard.ast("{}")).isEmpty());
  }

  @Test public void testGetOneNameSize() {
    assertTrue(getAll.names((Block) wizard.ast("{a=1+1;}")).size() == 1);
  }

  @Test public void testGetTwoNamesSize() {
    assertTrue(getAll.names((Block) wizard.ast("{a=1+1;b=2+2;}")).size() == 2);
  }

  @Test public void testCheckActualName() {
    assertTrue("a".equals(getAll.names((Block) wizard.ast("{a=1+1;}")).get(0) + ""));
  }

  @Test public void testCheckTwoNamesWithMoreThenOneLiteral() {
    final List<Name> names = getAll.names((Block) wizard.ast("{aba=1+1; ima = 787-9;}"));
    assertTrue("aba".equals(names.get(0) + "") && "ima".equals(names.get(1) + ""));
  }

  @Test public void testCheckNamesFineBlock() {
    // assuming we need to get all names in the block, including repetitions
    final List<Name> n = getAll.names((Block) wizard.ast("{a=1+1;b=2+3;System.out.println(a);c=2;c*=a;}"));
    assertTrue("a".equals(n.get(0) + ""));
    assertTrue("b".equals(n.get(1) + ""));
    assertTrue("System".equals(n.get(2) + ""));
    assertTrue("out".equals(n.get(3) + ""));
    assertTrue("println".equals(n.get(4) + ""));
    assertTrue("a".equals(n.get(5) + ""));
    assertTrue("c".equals(n.get(6) + ""));
    assertTrue("c".equals(n.get(7) + ""));
    assertTrue("a".equals(n.get(8) + ""));
  }
}
