package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.research.nanos.deprecated.*;

/** Tests {@link Aggregate}
 * @author Ori Marcovitch
 * @since Jan 18, 2017 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class AggregateTest {
  @Ignore @Test public void a() {
    topDownTrimming("for (int ¢ = 1; ¢ < arr.length; ++¢)  if (arr[¢] < min)   min = arr[¢];") //
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("StatsAccumulator $=Create.a(StatsAccumulator.class).from(values);");
  }

  @Test public void b() {
    topDownTrimming("for (final Object ¢ : os)  if (¢.better(best))   best = ¢;")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("best=os.stream().reduce((¢,best)->¢.better(best)?¢:best).get();");
  }

  @Test public void c() {
    topDownTrimming("int $ = 0; for(B d : bs) $ += f();")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("int $ = 0; $+=bs.stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("int $ = 0 + bs.stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("bs.stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("bs.stream().map(λ->f()).reduce((x,y)->x+y).get();")//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("for(B d : (B)bs) $ += f();")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("$ += ((B)bs).stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .stays();
  }

  @Test public void e() {
    topDownTrimming("for(B d : omg ? yes : no) $ += f();")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("$+=(omg ? yes : no).stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .stays();
  }

  @Test public void f() {
    topDownTrimming("for (final List<?> ¢ : implementation)    if (¢ != null)  $ += ¢.size();")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("$+=implementation.stream().filter(¢->¢!=null).map(¢->¢.size()).reduce((x,y)->x+y).get();")//
        .gives("$+=implementation.stream().filter(λ->λ!=null).map(λ->λ.size()).reduce((x,y)->x+y).get();")//
        .stays();
  }

  @Test public void g() {
    topDownTrimming("int $ = init; for (final Statement ¢ : ss)    $ += base + horizontalComplexity(¢);")//
        .using(EnhancedForStatement.class, new Aggregate())//
        .gives("int $=init;$+=ss.stream().map(¢->base+horizontalComplexity(¢)).reduce((x,y)->x+y).get();")//
        .gives("int $=init + ss.stream().map(¢->base+horizontalComplexity(¢)).reduce((x,y)->x+y).get();")//
    ;
  }

  @Test public void respect() {
    topDownTrimming("for (final Object ¢ : os)  if (¢.better(best))   best = ¢;")//
        .using(EnhancedForStatement.class, new ForEach(), new Select(), new Aggregate())//
        .gives("best=os.stream().reduce((¢,best)->¢.better(best)?¢:best).get();");
  }
}
