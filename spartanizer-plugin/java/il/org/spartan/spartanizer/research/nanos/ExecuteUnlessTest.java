package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link ExecuteUnless}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-28 */
@SuppressWarnings("static-method")
public class ExecuteUnlessTest {
  @Test public void a() {
    trimmingOf("if(x == 8) print(8);")//
        .using(new ExecuteUnless(), IfStatement.class)//
        .gives("execute(() -> print(8)).when(x==8);")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("if(x == 8 && iz.Literal(lit) || bigDaddy(d)) a.b()._(f,g).f.x(8,g,h*p);")//
        .using(new ExecuteUnless(), IfStatement.class)//
        .gives("execute(()->a.b()._(f,g).f.x(8,g,h*p)).when(x==8&&iz.Literal(lit)||bigDaddy(d));")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("for (final E constant : cs) if(Y) $.add(constant);")//
        .using(new ExecuteUnless(), IfStatement.class)//
        .stays();
  }
}
