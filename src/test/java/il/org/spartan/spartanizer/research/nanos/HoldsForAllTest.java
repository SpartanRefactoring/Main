package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests in {@link HoldsForAll}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class HoldsForAllTest {
  @Test public void a() {
    trimmingOf("for (  Entry<?> λ : that.entrySet())   if (m.count(λ.getElement()) != λ.getCount())   return false;  return true;")//
        .using(EnhancedForStatement.class, new HoldsForAll())//
        .gives("return that.entrySet().stream().allMatch(λ -> !(m.count(λ.getElement()) != λ.getCount()));");
  }

  @Test public void b() {
    trimmingOf("for (  Entry<?> λ : that.entrySet())   if (λ != null)   return false;  return true;")//
        .using(IfStatement.class, new ExecuteUnless(), new NotNullOrReturn())//
        .using(EnhancedForStatement.class, new HoldsForAll())//
        .gives("return that.entrySet().stream().allMatch(λ -> !(λ != null));")//
        .gives("return that.entrySet().stream().allMatch(λ -> λ == null);")//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (X x : Y) if (whatever) return false;")//
        .using(EnhancedForStatement.class, new HoldsForAll())//
        .gives("returnIf(Y.stream().allMatch(x -> !(whatever)));")//
        .gives("returnIf(Y.stream().allMatch(λ -> !(whatever)));")//
        .stays();
  }

  @Test public void d() {
    trimmingOf("for (X x : Y) if (whatever) $ = false;")//
        .using(EnhancedForStatement.class, new HoldsForAll())//
        .gives("$ = Y.stream().allMatch(x -> !(whatever));")//
        .gives("$ = Y.stream().allMatch(λ -> !(whatever));")//
        .stays();
  }
}
