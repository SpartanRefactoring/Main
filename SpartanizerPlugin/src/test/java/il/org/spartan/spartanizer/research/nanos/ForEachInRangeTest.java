package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO:  orimarco <tt>marcovitch.ori@gmail.com</tt>
 please add a description 
 @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-12 
 */

@SuppressWarnings("static-method")
public class ForEachInRangeTest {
  @Test public void a() {
    trimmingOf("for (int ¢=0; ¢ < 4096; ++¢) whitespace.append(\" \");")//
        .using(ForStatement.class, new ForEachInRange())//
        .gives("range.from(0).to(4096).forEach(¢->whitespace.append(\" \"));")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for (int ¢=0; ¢ < 2000; ++¢) whitespace.append(\" \");")//
        .using(ForStatement.class, new ForEachInRange())//
        .gives("range.from(0).to(2000).forEach(¢->whitespace.append(\" \"));")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (int ¢=0; ¢ < ls.size(); ++¢) $[¢]=ls.elementAt(¢);")//
        .using(ForStatement.class, new ForEachInRange())//
        .gives("range.from(0).to(ls.size()).forEach(¢->$[¢]=ls.elementAt(¢));")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("for (int ¢= thingy().f.g; ¢ < 7; ++¢) $[¢]=ls.elementAt(¢);")//
        .using(ForStatement.class, new ForEachInRange())//
        .gives("range.from(thingy().f.g).to(7).forEach(¢->$[¢]=ls.elementAt(¢));")//
        .stays();
  }
}

