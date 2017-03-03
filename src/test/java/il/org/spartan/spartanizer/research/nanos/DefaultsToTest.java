package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link DefaultsTo}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class DefaultsToTest {
  @Test public void a() {
    trimmingOf("return ¢ != null ? ¢ : \"\";")//
        .using(ConditionalExpression.class, new DefaultsTo())//
        .gives("return default¢(¢).to(\"\");")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("{B $ = t.tip(x); return $ != null ? $ : t2.tip(y);}")//
        .using(ConditionalExpression.class, new DefaultsTo())//
        .gives("return default¢(t.tip(x)).to(t2.tip(y));")//
        .using(ConditionalExpression.class, new DefaultsTo())//
        .stays();
  }
}
