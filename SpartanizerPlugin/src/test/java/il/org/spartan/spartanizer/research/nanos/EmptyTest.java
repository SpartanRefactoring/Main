package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link IsEmpty}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-01 */
@SuppressWarnings("static-method")
public class EmptyTest {
  @Test public void a() {
    trimmingOf("return collection.size() == 0;")//
        .using(InfixExpression.class, new IsEmpty())//
        .gives("return empty(collection);")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("return 0 == collection.size();")//
        .using(InfixExpression.class, new IsEmpty())//
        .gives("return empty(collection);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("return 0 == collection.size() && first(collection).isPretty;")//
        .using(InfixExpression.class, new IsEmpty())//
        .gives("return empty(collection) && first(collection).isPretty;")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("return 0 == (really.complicated ? exp : re).ssion.size() && first(collection).isPretty;")//
        .using(InfixExpression.class, new IsEmpty())//
        .gives("return empty((really.complicated ? exp : re).ssion) && first(collection).isPretty;")//
        .stays();
  }
}
