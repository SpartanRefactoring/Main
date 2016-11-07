package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Vivian Shehadeh
 * @author Ward Mattar
 * @since 16-11-06 */
public class Issue692 {
  @SuppressWarnings("static-method") @Test public void test0() {
    assertNull(getAll.invocations((MethodInvocation) null));
  }
  
  @SuppressWarnings("static-method") @Test public void test1() {
    assertEquals(getAll.invocations(az.methodInvocation(wizard.ast("example(1,2,3)"))).size(), 0);
  }
}