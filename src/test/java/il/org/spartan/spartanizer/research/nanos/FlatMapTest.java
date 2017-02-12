package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link FlatMap}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-12 */
@SuppressWarnings("static-method")
public class FlatMapTest {
  @Test public void a() {
    trimmingOf(
        "  for (AtmosphereResourceEventListener ¢ : willBeResumed ? listeners : rImpl.atmosphereResourceEventListener())  other.addAll(onBroadcast(e));")//
            .using(EnhancedForStatement.class, new FlatMap())//
            .stays();
  }

  @Test public void b() {
    trimmingOf("  for (AtmosphereResourceEventListener ¢ : rImpl)  other.addAll(onBroadcast(e));")//
        .using(EnhancedForStatement.class, new FlatMap())//
        .gives("other.addAll((rImpl).stream().flatMap(¢->onBroadcast(e)));")//
        .gives("other.addAll(rImpl.stream().flatMap(λ->onBroadcast(e)));")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("  for (AtmosphereResourceEventListener ¢ : rImpl)  other.addAll(¢);")//
        .using(EnhancedForStatement.class, new FlatMap())//
        .gives("other.addAll((rImpl).stream().flatMap(¢->¢));")//
        .gives("other.addAll(rImpl.stream().flatMap(λ->λ));")//
        .stays();
  }
  
  @Test public void d() {
    trimmingOf("  for (AtmosphereResourceEventListener ¢ : very.complicated(expression))  other.addAll(¢);")//
        .using(EnhancedForStatement.class, new FlatMap())//
        .gives("other.addAll((very.complicated(expression)).stream().flatMap(¢->¢));")//
        .gives("other.addAll(very.complicated(expression).stream().flatMap(λ->λ));")//
        .stays();
  }
}
