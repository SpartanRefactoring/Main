package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-08 */
@SuppressWarnings("static-method")
public class ForEachFilteredTest {
  @Test public void a() {
    trimmingOf("for (  A ¢ : is? thiz : theReal)   if (life) justFantasy();")//
        .using(EnhancedForStatement.class, new ForEachFiltered())//
        .gives("(is?thiz:theReal).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for (  A ¢ : (B)c)   if (life) justFantasy();")//
        .using(EnhancedForStatement.class, new ForEachFiltered())//
        .gives("((B)c).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .stays();
  }
}
