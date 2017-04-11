package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.nanos.deprecated.*;

/** TODO Ori Marcovitch please add a description
 * @author Ori Marcovitch
 * @since Dec 17, 2016 */
@SuppressWarnings("static-method")
public class SelectTest {
  @Test public void a() {
    trimminKof("for (final Expression ¢ : xs) if(¢.isNice() && awesomw(¢))  $.add(¢);")//
        .using(EnhancedForStatement.class, new Select())//
        .gives("$.addAll(xs.stream().filter(¢ -> ¢.isNice() && awesomw(¢)).collect(toList()));")//
    ;
  }

  @Test public void b() {
    trimminKof("for (final Expression ¢ : xs) if(¢.isNice() && awesomw(¢))  $.add(peel(¢));")//
        .using(EnhancedForStatement.class, new Select())//
        .gives("$.addAll(xs.stream().filter(¢ -> ¢.isNice() && awesomw(¢)).map(¢->peel(¢)).collect(toList()));")//
    ;
  }

  @Test public void respect() {
    trimminKof("for (final Expression ¢ : xs) if(¢.isNice() && awesomw(¢))  $.add(¢);")//
        .using(EnhancedForStatement.class, new ForEach(), new Select(), new Aggregate())//
        .gives("$.addAll(xs.stream().filter(¢ -> ¢.isNice() && awesomw(¢)).collect(toList()));")//
    ;
  }

  @Test public void test() {
    System.out.println(new Nanonizer().fixedPoint(
        "final List<Expression> operands = new ArrayList<>();  for (final Expression ¢ : hop.operands(flatten.of($))) operands.add(make.notOf(¢));"));
  }
}
