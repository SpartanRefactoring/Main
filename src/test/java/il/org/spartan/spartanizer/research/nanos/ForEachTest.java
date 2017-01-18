package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class ForEachTest {
  @Test public void a() {
    trimmingOf("  for (AtmosphereResourceEventListener ¢ : willBeResumed ? listeners : rImpl.atmosphereResourceEventListener())  ¢.onBroadcast(e);")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("(willBeResumed?listeners:rImpl.atmosphereResourceEventListener()).forEach(¢->¢.onBroadcast(e));")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for (Entry<URI, CTOverride> entry : overrideContentType.entrySet()) {  types.getDefaultOrOverride().add(entry.getValue());}")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("for(Entry<URI,CTOverride>¢:overrideContentType.entrySet())types.getDefaultOrOverride().add(entry.getValue());")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (Class i : interceptors) try { " //
        + " l.add((AtmosphereInterceptor)f.newClassInstance(AtmosphereHandler.class,i));} " //
        + "catch (Throwable ¢) {" //
        + "logger.warn(\"\",¢);" //
        + "}")//
            .using(EnhancedForStatement.class, new ForEach())//
            .gives(
                "interceptors.forEach(i->{try{l.add((AtmosphereInterceptor)f.newClassInstance(AtmosphereHandler.class,i));}catch(Throwable ¢){{logger.warn(\"\",¢);}}});")//
            .gives(
                "interceptors.forEach(i->{try{l.add((AtmosphereInterceptor)f.newClassInstance(AtmosphereHandler.class,i));}catch(Throwable ¢){logger.warn(\"\",¢);}});")//
            .stays();
  }

  @Test public void d() {
    trimmingOf("for (Class<? extends BroadcastFilter> ¢ : bf) f.broadcasterFilters(f.newClassInstance(BroadcastFilter.class,b));")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("bf.forEach(¢->f.broadcasterFilters(f.newClassInstance(BroadcastFilter.class,b)));")//
        .stays();
  }
}
