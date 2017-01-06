package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class ForEachTest {
  @Test public void a() {
    trimmingOf("for (  A ¢ : is? thiz : theReal)   if (life) justFantasy();")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("(is?thiz:theReal).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("  for (AtmosphereResourceEventListener ¢ : willBeResumed ? listeners : rImpl.atmosphereResourceEventListener())  ¢.onBroadcast(e);")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("(willBeResumed?listeners:rImpl.atmosphereResourceEventListener()).stream().forEach(¢->¢.onBroadcast(e));")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (  A ¢ : (B)c)   if (life) justFantasy();")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("((B)c).stream().filter(¢->life).forEach(¢->justFantasy());")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("for (Entry<URI, CTOverride> entry : overrideContentType.entrySet()) {  types.getDefaultOrOverride().add(entry.getValue());}")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("for(Entry<URI,CTOverride>¢:overrideContentType.entrySet())types.getDefaultOrOverride().add(entry.getValue());")//
        .stays();
  }
}
