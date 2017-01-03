package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class ContainsTest {
  @Test public void a() {
    trimmingOf("for (final Object ¢ : os)  if (¢.equals(best))   return true; return false;")//
        .withTipper(Block.class, new Contains())//
        .gives("return collection(os).contains(best);")//
        .withTipper(Block.class, new Contains())//
        .stays();
  }

  @Test public void b() {
    trimmingOf("something1(); something2(); for (final Object ¢ : os)  if (¢.equals(best))   return true; return false;")//
        .withTipper(Block.class, new Contains())//
        .gives("something1(); something2(); return collection(os).contains(best);")//
        .withTipper(Block.class, new Contains())//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (Object ¢ : os)  if (best.equals(¢))   return true; return false;")//
        .withTipper(Block.class, new Contains())//
        .gives("return collection(os).contains(best);")//
        .withTipper(Block.class, new Contains())//
        .stays();
  }

  @Test public void d() {
    trimmingOf("for (Object ¢ : (B)bs)  if (best.equals(¢))   return true; return false;")//
        .withTipper(Block.class, new Contains())//
        .gives("return collection((B)bs).contains(best);")//
        .withTipper(Block.class, new Contains())//
        .stays();
  }

  @Test public void e() {
    trimmingOf("for (Object ¢ : col.le(c,(tio)n))  if (best.equals(¢))   return true; return false;")//
        .withTipper(Block.class, new Contains())//
        .gives("return collection(col.le(c,(tio)n)).contains(best);")//
        .withTipper(Block.class, new Contains())//
        .stays();
  }

  @Test public void f() {
    trimmingOf("for (Object ¢ : omg ? yes : no)  if (best.equals(¢))   return true; return false;")//
        .withTipper(Block.class, new Contains())//
        .gives("return collection(omg ? yes : no).contains(best);")//
        .withTipper(Block.class, new Contains())//
        .stays();
  }

  @Test public void g() {
    trimmingOf("for (final Object ¢ : os)  if (¢ == (best))   return true; return false;")//
        .withTipper(Block.class, new Contains())//
        .gives("return collection(os).contains((best));")//
        .withTipper(Block.class, new Contains())//
        .stays();
  }

  @Test public void h() {
    trimmingOf("for (final Object ¢ : os)  if (¢ == a + b)   return true; return false;")//
        .withTipper(Block.class, new Contains())//
        .gives("return collection(os).contains(a + b);")//
        .withTipper(Block.class, new Contains())//
        .stays();
  }

  @Test public void i() {
    trimmingOf("for (final Object ¢ : os)  if (¢ == (omg ? yes : no))   return true; return false;")//
        .withTipper(Block.class, new Contains())//
        .gives("return collection(os).contains((omg ? yes : no));")//
        .withTipper(Block.class, new Contains())//
        .stays();
  }
}
