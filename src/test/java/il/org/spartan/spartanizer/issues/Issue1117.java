package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** @author Yossi Gil
 * @since Jan 23, 2017 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1117 {
  @Test public void a() {
    trimminKof("(x)->x")//
        .using(LambdaExpression.class, new LambdaRemoveParenthesis()) //
        .gives("x->x")//
        .gives("λ->λ")//
        .stays()//
    ;
  }

  @Test public void b() {
    trimminKof("(final int x)->x")//
        .using(LambdaExpression.class, new LambdaRemoveParenthesis()) //
        .stays()//
    ;
  }

  @Test public void c() {
    trimminKof("(λ)->λ")//
        .gives("λ->λ")//
        .stays()//
    ;
  }
}
