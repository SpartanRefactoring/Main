package il.org.spartan.spartanizer.ast.navigate;

import static org.junit.Assert.*;
import org.junit.*;
import il.org.spartan.spartanizer.engine.*;

/** Test for cantTip.java, see issue #822 for more details
 * @author Amit Ohayon
 * @author Yosef Raisman
 * @author Entony Lekhtman */
@SuppressWarnings({ "static-method" })
public class cantTipTest {
  @Test public void testRemoveoRedundantIfInFor() {
    assertFalse(cantTip.remvoeRedundantIf(findFirst.forStatement(into.s("{for(;;){if(true){}}}"))));
  }

  @Test public void testRemoveRedundantIfInWhile() {
    assertFalse(cantTip.remvoeRedundantIf(findFirst.whileStatement(into.s("{while(true){if(true){}}}"))));
  }

  @Test public void testNullPrecedingFragmentInTerminalScopeStatement() {
    assertTrue(cantTip.declarationInitializerStatementTerminatingScope(findFirst.forStatement(into.s("{for(;;){}}"))));
    assertTrue(cantTip.declarationInitializerStatementTerminatingScope(findFirst.whileStatement(into.s("{while(true){}}"))));
  }
}
