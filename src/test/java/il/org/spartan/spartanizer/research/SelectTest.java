package il.org.spartan.spartanizer.research;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.patterns.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class SelectTest {
  @Test public void a() {
    trimmingOf("for (final Expression ¢ : xs) if(¢.isNice() && awesomw(¢))  $.add(¢);").withTipper(EnhancedForStatement.class, new Select())
        .gives("$.addAll(xs.stream().filter(¢ -> ¢.isNice() && awesomw(¢)).collect(Collectors.toList()));");
  }

  @Test public void b() {
    trimmingOf("for (final Expression ¢ : xs) if(¢.isNice() && awesomw(¢))  $.add(peel(¢));").withTipper(EnhancedForStatement.class, new Select())
        .gives("$.addAll(xs.stream().filter(¢ -> ¢.isNice() && awesomw(¢)).map(¢->peel(¢)).collect(Collectors.toList()));");
  }
}
