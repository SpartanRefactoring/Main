package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.research.nanos.*;
import il.org.spartan.research.nanos.deprecated.*;

/** Tests {@link Aggregate}
 * @author Ori Marcovitch
 * @since Jan 18, 2017 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("static-method")
public class AggregateTest {
  @Ignore @Test public void a() {
    trimminKof("for (int ¢ = 1; ¢ < arr.length; ++¢)  if (arr[¢] < min)   min = arr[¢];") //
        .using(new Aggregate(), EnhancedForStatement.class)//
        .gives("StatsAccumulator $=Create.a(StatsAccumulator.class).from(values);");
  }

  @Test public void b() {
    trimminKof("for (final Object ¢ : os)  if (¢.better(best))   best = ¢;")//
        .using(new Aggregate(), EnhancedForStatement.class)//
        .gives("best=os.stream().reduce((¢,best)->¢.better(best)?¢:best).get();");
  }

  @Test public void c() {
    trimminKof("int $ = 0; for(B d : bs) $ += f();")//
        .using(new Aggregate(), EnhancedForStatement.class)//
        .gives("int $ = 0; $+=bs.stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("int $ = 0 + bs.stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("bs.stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .gives("bs.stream().map(λ->f()).reduce((x,y)->x+y).get();")//
        .stays();
  }

  @Test public void d() {
    trimminKof("for(B d : (B)bs) $ += f();")//
        .using(new Aggregate(), EnhancedForStatement.class)//
        .gives("$ += ((B)bs).stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .using(new Aggregate(), EnhancedForStatement.class)//
        .stays();
  }

  @Test public void e() {
    trimminKof("for(B d : omg ? yes : no) $ += f();")//
        .using(new Aggregate(), EnhancedForStatement.class)//
        .gives("$+=(omg ? yes : no).stream().map(d->f()).reduce((x,y)->x+y).get();")//
        .using(new Aggregate(), EnhancedForStatement.class)//
        .stays();
  }

  @Test public void f() {
    trimminKof("for (final List<?> ¢ : implementation)    if (¢ != null)  $ += ¢.size();")//
        .using(new Aggregate(), EnhancedForStatement.class)//
        .gives("$+=implementation.stream().filter(¢->¢!=null).map(¢->¢.size()).reduce((x,y)->x+y).get();")//
        .gives("$+=implementation.stream().filter(λ->λ!=null).map(λ->λ.size()).reduce((x,y)->x+y).get();")//
        .stays();
  }

  @Test public void g() {
    trimminKof("int $ = init; for (final Statement ¢ : ss)    $ += base + horizontalComplexity(¢);")//
        .using(new Aggregate(), EnhancedForStatement.class)//
        .gives("int $=init;$+=ss.stream().map(¢->base+horizontalComplexity(¢)).reduce((x,y)->x+y).get();")//
        .gives("int $=init + ss.stream().map(¢->base+horizontalComplexity(¢)).reduce((x,y)->x+y).get();")//
    ;
  }

  @Test public void respect() {
    trimminKof("for (final Object ¢ : os)  if (¢.better(best))   best = ¢;")//
        .using(EnhancedForStatement.class, new ForEach(), new Select(), new Aggregate())//
        .gives("best=os.stream().reduce((¢,best)->¢.better(best)?¢:best).get();");
  }
}
