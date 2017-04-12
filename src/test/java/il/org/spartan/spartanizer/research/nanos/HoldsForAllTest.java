package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** Tests in {@link HoldsForAll}
 * @author Ori Marcovitch */
@SuppressWarnings("static-method")
public class HoldsForAllTest {
  @Test public void a() {
    trimminKof("for (  Entry<?> λ : that.entrySet())   if (m.count(λ.getElement()) != λ.getCount())   return false;  return true;")//
        .using(new HoldsForAll(), EnhancedForStatement.class)//
        .gives("return that.entrySet().stream().allMatch(λ -> !(m.count(λ.getElement()) != λ.getCount()));");
  }

  @Test public void b() {
    trimminKof("for (  Entry<?> λ : that.entrySet())   if (λ != null)   return false;  return true;")//
        .using(IfStatement.class, new ExecuteUnless(), new NotNullAssumed())//
        .using(new HoldsForAll(), EnhancedForStatement.class)//
        .gives("return that.entrySet().stream().allMatch(λ -> !(λ != null));")//
        .gives("return that.entrySet().stream().allMatch(λ -> λ == null);")//
        .stays();
  }

  @Test public void c() {
    trimminKof("for (X x : Y) if (whatever) return false;")//
        .using(new HoldsForAll(), EnhancedForStatement.class)//
        .gives("returnIf(Y.stream().allMatch(x -> !(whatever)));")//
        .gives("returnIf(Y.stream().allMatch(λ -> !(whatever)));")//
        .stays();
  }

  @Test public void d() {
    trimminKof("for (X x : Y) if (whatever) $ = false;")//
        .using(new HoldsForAll(), EnhancedForStatement.class)//
        .gives("$ = Y.stream().allMatch(x -> !(whatever));")//
        .gives("$ = Y.stream().allMatch(λ -> !(whatever));")//
        .stays();
  }

  @Test public void e() {
    trimminKof(" for (BroadcasterCacheInspector ¢ : inspectors) if (!¢.inspect(m)) return false;")//
        .using(new HoldsForAll(), EnhancedForStatement.class)//
        .gives("returnIf(inspectors.stream().allMatch(¢->!(!¢.inspect(m))));") //
        .gives("returnIf(inspectors.stream().allMatch(λ->!(!λ.inspect(m))));") //
        .gives("returnIf(inspectors.stream().allMatch(λ->λ.inspect(m)));") //
        .stays();
  }
}
