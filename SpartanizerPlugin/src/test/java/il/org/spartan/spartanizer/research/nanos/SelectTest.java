package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.analyses.*;

/** @author Ori Marcovitch
 * @since Dec 17, 2016 */
@SuppressWarnings("static-method")
public class SelectTest {
  @Test public void a() {
    trimmingOf("for (final Expression ¢ : xs) if(¢.isNice() && awesomw(¢))  $.add(¢);")//
        .using(EnhancedForStatement.class, new Select())//
        .gives("$.addAll(xs.stream().filter(¢ -> ¢.isNice() && awesomw(¢)).collect(Collectors.toList()));")//
        .stays();
  }

  @Test public void b() {
    trimmingOf("for (final Expression ¢ : xs) if(¢.isNice() && awesomw(¢))  $.add(peel(¢));")//
        .using(EnhancedForStatement.class, new Select())//
        .gives("$.addAll(xs.stream().filter(¢ -> ¢.isNice() && awesomw(¢)).map(¢->peel(¢)).collect(Collectors.toList()));")//
        .stays();
  }

  @Test public void respect() {
    trimmingOf("for (final Expression ¢ : xs) if(¢.isNice() && awesomw(¢))  $.add(¢);")//
        .withTippers(EnhancedForStatement.class, new ForEach(), new Select(), new Aggregate())//
        .gives("$.addAll(xs.stream().filter(¢ -> ¢.isNice() && awesomw(¢)).collect(Collectors.toList()));")//
        .stays();
  }

  @Test public void test() {
    System.out.println((new SpartAnalyzer()).fixedPoint(
        "final List<Expression> operands = new ArrayList<>();  for (final Expression ¢ : hop.operands(flatten.of($))) operands.add(make.notOf(¢));"));
  }
}
