package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class ForEachTest {
  @Test public void a() {
    trimmingOf("for (  A ¢ : is? thiz : theReal)   if (life) justFantasy();")//
        .withTipper(EnhancedForStatement.class, new ForEach())//
        .gives("(is?thiz:theReal).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("  for (AtmosphereResourceEventListener ¢ : willBeResumed ? listeners : rImpl.atmosphereResourceEventListener())  ¢.onBroadcast(e);")//
        .withTipper(EnhancedForStatement.class, new ForEach())//
        .gives("(willBeResumed?listeners:rImpl.atmosphereResourceEventListener()).stream().forEach(¢->¢.onBroadcast(e));")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (  A ¢ : (B)c)   if (life) justFantasy();")//
        .withTipper(EnhancedForStatement.class, new ForEach())//
        .gives("((B)c).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .stays();
  }
}
