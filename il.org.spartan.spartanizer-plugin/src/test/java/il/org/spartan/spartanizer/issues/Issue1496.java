package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Test case for bug in exclusion
 * manager({@link IfStatementBlockSequencerBlockSameSequencer},
 * {@link IfEmptyThen}
 * @author Yuval Simon
 * @since 2017-06-17 */
@SuppressWarnings("static-method")
public class Issue1496 {
  @Test public void t1() {
    trimmingOf("if(base == 0) {return true;}long a = f(b,c,d);if(a==1){}else {   while(a > 1) g();}return true;")
        .gives("if(base==0){}else{long a=f(b,c,d);if(a==1){}   else{while(a>1)g();}}return true;");
  }
}
