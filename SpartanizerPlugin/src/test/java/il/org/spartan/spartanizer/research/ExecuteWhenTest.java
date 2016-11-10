package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
// TODO: Marco tests fail because we also add an import. Tests need to be
// changed, changes
// to trimmingOf might be needed... Or just use another testing method.
@Ignore @SuppressWarnings("static-method") public class ExecuteWhenTest {
  @Test public void basic() {
    trimmingOf("if(x == 8) print(8);").withTipper(IfStatement.class, new ExecuteWhen()).gives("execute(() -> print(8)).when(x==8);");
  }
  @Test public void comlicated() {
    trimmingOf("if(x == 8 && iz.Literal(lit) || bigDaddy(d)) a.b()._(f,g).f.x(8,g,h*p);").withTipper(IfStatement.class, new ExecuteWhen())
        .gives("execute((__)->a.b()._(f,g).f.x(8,g,h*p)).when(x == 8 && iz.Literal(lit) || bigDaddy(d));");
  }
}
