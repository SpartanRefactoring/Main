package il.org.spartan.spartanizer.ast.navigate;

import org.junit.*;

import il.org.spartan.spartanizer.engine.*;

/** Test for cantTip.java, see issue #822 for more details
 * @author Amit Ohayon
 * @author Yosef Raisman
 * @author Entony Lekhtman
 * @since Nov 14, 2016 */
@SuppressWarnings("static-method")
public class cantTipTest {
  @Test public void testNullPrecedingFragmentInTerminalScopeStatement() {
    assert cantTip.declarationInitializerStatementTerminatingScope(findFirst.forStatement(parse.s("{for(;;){}}")));
    assert cantTip.declarationInitializerStatementTerminatingScope(findFirst.whileStatement(parse.s("{while(true){}}")));
  }
  @Test public void testRemoveoRedundantIfInFor() {
    assert !cantTip.removeRedundantIf(findFirst.forStatement(parse.s("{for(;;){if(true){}}}")));
  }
  @Test public void testRemoveRedundantIfInWhile() {
    assert !cantTip.remvoeRedundantIf(findFirst.whileStatement(parse.s("{while(true){if(true){}}}")));
  }
}
