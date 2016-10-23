package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@Ignore //
@SuppressWarnings("static-method") //
public class ExecuteWhenTest {
  @Test public void basic() {
    trimmingOf("if(x==8)p(8);").withTipper(IfStatement.class, new ExecuteWhen())//
        .gives("execute((x)->p(8)).when(x==8);");
  }

  @Test public void complicated() {
    trimmingOf("if(x == 8 && iz.t(i) || b(d)) a.b()._(f,g).f.x(8,g,h*p);")//
        .withTipper(IfStatement.class, new ExecuteWhen())//
        .gives("execute((x)->a.b()._(f,g).f.x(8,g,h*p)).when(x == 8 && iz.t(i) || b(d));");
  }
}
