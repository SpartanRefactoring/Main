package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class AggregateTest {
  @Ignore @Test public void a() {
    trimmingOf("for (int ¢ = 1; ¢ < arr.length; ++¢)  if (arr[¢] < min)   min = arr[¢];")
        .gives("StatsAccumulator $=Create.a(StatsAccumulator.class).from(values);");
  }

  @Test public void b() {
    trimmingOf("for (final Object ¢ : os)  if (¢.better(best))   best = ¢;")//
        .withTipper(EnhancedForStatement.class, new Aggregate())//
        .gives("best=os.stream().reduce((¢,best)->¢.better(best)?¢:best).get();");
  }

  @Test public void c() {
    trimmingOf("for(B d : bs) $ += f();")//
        .withTipper(EnhancedForStatement.class, new Aggregate())//
        .gives("$=bs.stream().map(d->f()).reduce((x,y)->x+y).get();").stays();
  }

  @Test public void d() {
    trimmingOf("for(B d : (B)bs) $ += f();"//
    )//
        .withTipper(EnhancedForStatement.class, new Aggregate())//
        .gives("$=((B)bs).stream().map(d->f()).reduce((x,y)->x+y).get();").stays();
  }

  @Test public void e() {
    trimmingOf("for(B d : omg ? yes : no) $ += f();"//
    )//
        .withTipper(EnhancedForStatement.class, new Aggregate())//
        .gives("$=(omg ? yes : no).stream().map(d->f()).reduce((x,y)->x+y).get();").stays();
  }
}
