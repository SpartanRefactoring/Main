package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link ForEachInRange}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-12 */
@SuppressWarnings("static-method")
public class ForEachInRangeTest {
  @Test public void a() {
    trimmingOf("for (int ¢=0; ¢ < 4096; ++¢) whitespace.append(\" \");")//
        .using(new ForEachInRange(), ForStatement.class)//
        .gives("range.from(0).to(4096).forEach(¢->whitespace.append(\" \"));")//
        .gives("range.from(0).to(4096).forEach(λ->whitespace.append(\" \"));")//
        .stays();
  }
  @Test public void b() {
    trimmingOf("for (int ¢=0; ¢ < 2000; ++¢) whitespace.append(\" \");")//
        .using(new ForEachInRange(), ForStatement.class)//
        .gives("range.from(0).to(2000).forEach(¢->whitespace.append(\" \"));")//
        .gives("range.from(0).to(2000).forEach(λ->whitespace.append(\" \"));")//
        .stays();
  }
  @Test public void c() {
    trimmingOf("for (int ¢=0; ¢ < ls.size(); ++¢) $[¢]=ls.elementAt(¢);")//
        .using(new ForEachInRange(), ForStatement.class)//
        .gives("range.from(0).to(ls.size()).forEach(¢->$[¢]=ls.elementAt(¢));")//
        .gives("range.from(0).to(ls.size()).forEach(λ->$[λ]=ls.elementAt(λ));")//
        .stays();
  }
  @Test public void d() {
    trimmingOf("for (int ¢= thingy().f.g; ¢ < 7; ++¢) $[¢]=ls.elementAt(¢);")//
        .using(new ForEachInRange(), ForStatement.class)//
        .gives("range.from(thingy().f.g).to(7).forEach(¢->$[¢]=ls.elementAt(¢));")//
    ;
  }
}
