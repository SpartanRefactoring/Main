package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link DefaultsTo}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class DefaultsToTest {
  @Test public void a() {
    trimmingOf("return ¢ != null ? ¢ : \"\";")//
        .using(new DefaultsTo(), ConditionalExpression.class)//
        .gives("return defaults(¢).to(\"\");")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("{B $ = t.tip(x); return $ != null ? $ : t2.tip(y);}")//
        .using(new DefaultsTo(), ConditionalExpression.class)//
        .gives("return defaults(t.tip(x)).to(t2.tip(y));")//
        .using(new DefaultsTo(), ConditionalExpression.class)//
        .stays();
  }
}
