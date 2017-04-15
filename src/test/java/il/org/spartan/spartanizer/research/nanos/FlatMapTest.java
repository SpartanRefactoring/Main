package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link FlatMap}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-12 */
@SuppressWarnings("static-method")
public class FlatMapTest {
  @Test public void a() {
    trimminKof(
        "  for (AtmosphereResourceEventListener ¢ : willBeResumed ? listeners : rImpl.atmosphereResourceEventListener())  other.addAll(onBroadcast(e));")//
            .using(EnhancedForStatement.class, new ForEach(), new FlatMap())//
            .gives("other.addAll((willBeResumed?listeners:rImpl.atmosphereResourceEventListener()).stream().flatMap(¢->onBroadcast(e)));") //
            .gives("other.addAll((willBeResumed?listeners:rImpl.atmosphereResourceEventListener()).stream().flatMap(λ->onBroadcast(e)));") //
            .stays();
  }

  @Test public void b() {
    trimminKof(" for (AtmosphereResourceEventListener ¢ : rImpl)  other.addAll(onBroadcast(e));")//
        .using(EnhancedForStatement.class, new ForEach(), new FlatMap())//
        .gives("other.addAll((rImpl).stream().flatMap(¢->onBroadcast(e)));")//
        .gives("other.addAll(rImpl.stream().flatMap(λ->onBroadcast(e)));")//
        .stays();
  }

  @Test public void c() {
    trimminKof(" for (AtmosphereResourceEventListener ¢ : rImpl)  other.addAll(¢);")//
        .using(EnhancedForStatement.class, new ForEach(), new FlatMap())//
        .gives("other.addAll((rImpl).stream().flatMap(¢->¢));")//
        .gives("other.addAll(rImpl.stream().flatMap(λ->λ));")//
        .stays();
  }

  @Test public void d() {
    trimminKof(" for (AtmosphereResourceEventListener ¢ : very.complicated(expression))  other.addAll(¢);")//
        .using(EnhancedForStatement.class, new ForEach(), new FlatMap())//
        .gives("other.addAll((very.complicated(expression)).stream().flatMap(¢->¢));")//
        .gives("other.addAll(very.complicated(expression).stream().flatMap(λ->λ));")//
        .stays();
  }

  @Test public void e() {
    trimminKof(" for (A a : B) for(C c : a) other.add(c);")//
        .using(EnhancedForStatement.class, new ForEach(), new FlatMap())//
        .gives("other.addAll((B).stream().flatMap(a->a));")//
        .gives("other.addAll(B.stream().flatMap(λ->λ));")//
        .stays();
  }

  @Test public void f() {
    trimminKof(" for (A a : B.f.f()) for(C c : a) other.add(c);")//
        .using(EnhancedForStatement.class, new ForEach(), new FlatMap())//
        .gives("other.addAll((B.f.f()).stream().flatMap(a->a));")//
        .gives("other.addAll(B.f.f().stream().flatMap(λ->λ));")//
        .stays();
  }
}
