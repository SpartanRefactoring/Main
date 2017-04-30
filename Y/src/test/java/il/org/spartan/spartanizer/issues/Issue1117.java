package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** @author Yossi Gil
 * @since Jan 23, 2017 */
//
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1117 {
  @Test public void a() {
    trimmingOf("(x)->x")//
        .using(new LambdaRemoveParenthesis(), LambdaExpression.class) //
        .gives("x->x")//
        .gives("λ->λ")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimmingOf("(final int x)->x")//
        .using(new LambdaRemoveParenthesis(), LambdaExpression.class) //
        .stays()//
    ;
  }

  @Test public void c() {
    trimmingOf("(λ)->λ")//
        .gives("λ->λ")//
        .stays()//
    ;
  }
}
