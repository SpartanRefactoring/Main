package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link ForEach}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2016-12-25 */
@SuppressWarnings("static-method")
public class ForEachTest {
  @Test public void a() {
    trimmingOf(" for (AtmosphereResourceEventListener ¢ : willBeResumed ? listeners : rImpl.atmosphereResourceEventListener())  ¢.onBroadcast(e);")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("(willBeResumed?listeners:rImpl.atmosphereResourceEventListener()).forEach(¢->¢.onBroadcast(e));")//
    ;
  }

  @Test public void b() {
    trimmingOf("for (Class i : is) try { " //
        + " l.add((A)f.newClassInstance(H.class,i));} " //
        + "catch (Throwable ¢) {" //
        + "logger.warn(\"\",¢);" //
        + "}")//
            .using(EnhancedForStatement.class, new ForEach())//
            .gives("is.forEach(i->{try{l.add((A)f.newClassInstance(H.class,i));}catch(Throwable ¢){{logger.warn(\"\",¢);}}});")//
            .gives("is.forEach(i->{try{l.add((A)f.newClassInstance(H.class,i));}catch(Throwable ¢){logger.warn(\"\",¢);}});")//
            .stays();
  }

  @Test public void c() {
    trimmingOf("for (Class<? extends BroadcastFilter> ¢ : bf) f.broadcasterFilters(f.newClassInstance(BroadcastFilter.class,b));")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("bf.forEach(¢->f.broadcasterFilters(f.newClassInstance(BroadcastFilter.class,b)));")//
    ;
  }

  @Test public void d() {
    trimmingOf(" for (final Statement k : ss)    $.append(k);")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("ss.forEach(k -> $.append(k));")//
    ;
  }

  @Test public void e() {
    trimmingOf("for (final ICompilationUnit ¢ : us)    scanCompilationUnit(¢, eclipse.newSubMonitor(progressMonitor));")//
        .using(EnhancedForStatement.class, new ForEach())//
        .gives("us.forEach(¢->scanCompilationUnit(¢,eclipse.newSubMonitor(progressMonitor)));")//
    ;
  }
}
