package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.deprecated.*;

/** @author Ori Marcovitch
 * @since 2016 */
@Ignore
//TODO: Ori Marco
@SuppressWarnings("static-method")
public class AggregateTest {
  @Ignore @Test public void a() {
    trimmingOf("for (int ¢ = 1; ¢ < arr.length; ++¢) if (arr[¢] < min) min = arr[¢];")
        .gives("StatsAccumulator $=Create.a(StatsAccumulator.class).from(values);");
  }

  @Test public void b() {
    trimmingOf("for (final Object ¢ : os) if (¢.better(best)) best = ¢;")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("best=os.stream().reduce((¢,best)->¢.better(best)?¢:best).get();");
  }

  @Test public void c() {
    trimmingOf("int $ = 0; for(B d : bs) $ += f();")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("int $ = 0; $+=bs.stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("int $ = 0 + bs.stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("int $ = bs.stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("int $ = bs.stream().map(¢->f()).reduce((x,y)->x+y).get();")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("for(B d : (B)bs) $ += f();")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("$ += ((B)bs).stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("$ += ((B)bs).stream().map(¢->f()).reduce((x,y)->x+y).get();")//
        .stays();
  }

  @Test public void e() {
    trimmingOf("for(B d : omg ? yes : no) $ += f();")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("$+=(omg ? yes : no).stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("$+=(omg ? yes : no).stream().map(¢->f()).reduce((x,y)->x+y).get();")//
        .stays();
  }

  @Test public void respect() {
    trimmingOf("for (final Object ¢ : os) if (¢.better(best)) best = ¢;")//
        .withTippers(EnhancedForStatement.class, new ForEach(), new Select(), new Aggregate())//
        .gives("best=os.stream().reduce((¢,best)->¢.better(best)?¢:best).get();");
  }

  @Test public void f() {
    trimmingOf("for (final List<?> ¢ : implementation) if (¢ != null) $ += ¢.size();"//
    )//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("$+=implementation.stream().filter(¢->¢!=null).map(¢->¢.size()).reduce((x,y)->x+y).get();")//
        .stays();
  }

  @Test public void g() {
    trimmingOf("int $ = init; for (final Statement ¢ : ss) $ += base + horizontalComplexity(¢);"//
    )//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("int $=init;$+=ss.stream().map(¢->base+horizontalComplexity(¢)).reduce((x,y)->x+y).get();")//
        .gives("int $=init + ss.stream().map(¢->base+horizontalComplexity(¢)).reduce((x,y)->x+y).get();")//
        .stays();
  }
}
