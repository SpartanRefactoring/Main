package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-28 */
@SuppressWarnings("static-method")
public class ExecuteWhenTest {
  @Test public void basic() {
    trimmingOf("if(x == 8) print(8);")//
        .using(IfStatement.class, new ExecuteWhen())//
        .gives("execute(() -> print(8)).when(x==8);")//
        .stays();
  }

  @Test public void comlicated() {
    trimmingOf("if(x == 8 && iz.Literal(lit) || bigDaddy(d)) a.b()._(f,g).f.x(8,g,h*p);")//
        .using(IfStatement.class, new ExecuteWhen())//
        .gives("execute(()->a.b()._(f,g).f.x(8,g,h*p)).when(x==8&&iz.Literal(lit)||bigDaddy(d));")//
        .stays();
  }
}
