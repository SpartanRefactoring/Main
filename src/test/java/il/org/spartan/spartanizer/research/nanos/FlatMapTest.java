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
        "  for (AtmosphereResourceEventListener ¢ : willBeResumed ? listeners : rImpl.atmosphereResourceEventListener())  ¢.addAll(onBroadcast(e));")//
            .using(EnhancedForStatement.class, new FlatMap())//
            .gives("¢.addAll((willBeResumed?listeners:rImpl.atmosphereResourceEventListener()).stream().flatMap(¢->onBroadcast(e)));")//
    ;
  }

  @Test public void c() {
    trimmingOf("for (Class i : is) try { " //
        + " l.add((A)f.newClassInstance(H.class,i));} " //
        + "catch (Throwable ¢) {" //
        + "logger.warn(\"\",¢);" //
        + "}")//
            .using(EnhancedForStatement.class, new FlatMap())//
            .gives("is.FlatMap(i->{try{l.add((A)f.newClassInstance(H.class,i));}catch(Throwable ¢){{logger.warn(\"\",¢);}}});")//
            .gives("is.FlatMap(i->{try{l.add((A)f.newClassInstance(H.class,i));}catch(Throwable ¢){logger.warn(\"\",¢);}});")//
            .stays();
  }

  @Test public void d() {
    trimmingOf("for (Class<? extends BroadcastFilter> ¢ : bf) f.broadcasterFilters(f.newClassInstance(BroadcastFilter.class,b));")//
        .using(EnhancedForStatement.class, new FlatMap())//
        .gives("bf.FlatMap(¢->f.broadcasterFilters(f.newClassInstance(BroadcastFilter.class,b)));")//
    ;
  }

  @Test public void e() {
    trimmingOf("  for (final Statement k : ss)    $.append(k);")//
        .using(EnhancedForStatement.class, new FlatMap())//
        .gives("ss.FlatMap(k -> $.append(k));")//
    ;
  }

  @Test public void f() {
    trimmingOf("for (final ICompilationUnit ¢ : us)    scanCompilationUnit(¢, eclipse.newSubMonitor(progressMonitor));")//
        .using(EnhancedForStatement.class, new FlatMap())//
        .gives("us.FlatMap(¢->scanCompilationUnit(¢,eclipse.newSubMonitor(progressMonitor)));")//
    ;
  }
}
