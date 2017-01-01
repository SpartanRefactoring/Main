package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class FindFirstTest {
  @Test public void a() {
    trimmingOf("for(Object i : is) if(i.isNice()) return i;").withTipper(Block.class, new FindFirst())
        .gives("return is.stream().findFirst(i -> i.isNice()).get();");
  }

  @Test public void b() {
    trimmingOf("for(Object i : is) if(i.isNice()) return i; throw new None();").withTipper(Block.class, new FindFirst())
        .gives("if(is.stream().anyMatch(i->i.isNice()))return is.stream().findFirst(i->i.isNice()).get();throw new None();");
  }
}
