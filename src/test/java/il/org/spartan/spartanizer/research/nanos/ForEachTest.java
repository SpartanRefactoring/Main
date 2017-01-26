package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** TODO: orimarco <tt>marcovitch.ori@gmail.com</tt> please add a description
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class ForEachTest {
  @Test public void a() {
    trimmingOf("  for (AtmosphereResourceEventListener ¢ : willBeResumed ? listeners : rImpl.atmosphereResourceEventListener())  ¢.onBroadcast(e);")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("(willBeResumed?listeners:rImpl.atmosphereResourceEventListener()).forEach(¢->¢.onBroadcast(e));")//
    ;
  }

  @Test public void b() {
    trimmingOf("for (Entry<U, O> entry : overrideContentType.entrySet()) {  types.getDefaultOrOverride().add(entry.getValue());}")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("for(Entry<U,O>¢:overrideContentType.entrySet())types.getDefaultOrOverride().add(entry.getValue());")//
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
    ;
  }

  @Test public void e() {
    trimmingOf("  for (final Statement k : ss)    $.append(k);")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("ss.forEach(k -> $.append(k));")//
    ;
  }

  @Test public void f() {
    trimmingOf("for (final ICompilationUnit ¢ : us)    scanCompilationUnit(¢, eclipse.newSubMonitor(progressMonitor));")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("us.forEach(¢->scanCompilationUnit(¢,eclipse.newSubMonitor(progressMonitor)));")//
    ;
  }
}
