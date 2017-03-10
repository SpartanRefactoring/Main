package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link Singleton}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-28 */
@SuppressWarnings("static-method")
public class SingletonTest {
  @Test public void a() {
    trimmingOf("return collection.size() == 1;")//
        .using(InfixExpression.class, new Singleton())//
        .gives("return singleton(collection);")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("return 1 == collection.size();")//
        .using(InfixExpression.class, new Singleton())//
        .gives("return singleton(collection);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("return 1 == collection.size() && first(collection).isPretty;")//
        .using(InfixExpression.class, new Singleton())//
        .gives("return singleton(collection) && first(collection).isPretty;")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("return 1 == (really.complicated ? exp : re).ssion.size() && first(collection).isPretty;")//
        .using(InfixExpression.class, new Singleton())//
        .gives("return singleton((really.complicated ? exp : re).ssion) && first(collection).isPretty;")//
        .stays();
  }
}
