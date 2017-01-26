package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since Jan 23, 2017 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1119 {
  @Test public void a() {
    trimmingOf("(x)->x")//
        .using(LambdaExpression.class, new LambdaRemoveParenthesis()) //
        .gives("x->x")//
        .using(LambdaExpression.class, new LambdaRemoveParenthesis()) //
        .stays()//
    ;
  }
}
