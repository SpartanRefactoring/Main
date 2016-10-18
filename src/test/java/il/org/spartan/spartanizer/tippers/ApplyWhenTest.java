package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method") public class ApplyWhenTest {
  @Test public void basic() {
    trimmingOf("if(x == 8) print(8);").withTipper(IfStatement.class, new ApplyWhen()).gives("applyWhen(x==8, print(8));");
  }

  @Test public void comlicated() {
    trimmingOf("if(x == 8 && iz.Literal(lit) || bigDaddy(d)) a.b()._(f,g).f.x(8,g,h*p);").withTipper(IfStatement.class, new ApplyWhen())
        .gives("applyWhen(x == 8 && iz.Literal(lit) || bigDaddy(d), a.b()._(f,g).f.x(8,g,h*p));");
  }
}
