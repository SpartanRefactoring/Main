package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests in {@link HoldsForAll}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class HoldsForAllTest {
  @Test public void a() {
    trimmingOf("for (  Entry<?> λ : that.entrySet())   if (m.count(λ.getElement()) != λ.getCount())   return false;  return true;")
        .using(EnhancedForStatement.class, new HoldsForAll())
        .gives("return that.entrySet().stream().allMatch(λ -> !(m.count(λ.getElement()) != λ.getCount()));");
  }

  @Test public void b() {
    trimmingOf("for (  Entry<?> λ : that.entrySet())   if (λ != null)   return false;  return true;")//
        .using(IfStatement.class, new ExecuteUnless())//
        .using(EnhancedForStatement.class, new HoldsForAll())//
        .gives("return that.entrySet().stream().allMatch(λ -> !(λ != null));")//
        .gives("return that.entrySet().stream().allMatch(λ -> λ == null);")//
        .stays();
  }
}
