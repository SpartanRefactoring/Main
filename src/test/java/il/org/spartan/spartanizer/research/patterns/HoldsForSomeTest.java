package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class HoldsForSomeTest {
  @Test public void _a() {
    trimmingOf(" for (final UserDefinedTipper<Statement> ¢ : tippers) if (¢.canTip(s)) return true; return false;")
        .withTipper(Block.class, new HoldsForSome()).gives("return tippers.stream().anyMatch(¢ -> ¢.canTip(s));");
  }

  @Test public void a() {
    trimmingOf("for (final Object ¢ : os)  if (¢.equals(best))   return true; return false;")//
        .withTipper(Block.class, new HoldsForSome())//
        .gives("return os.stream().anyMatch(¢->¢.equals(best));")//
        .withTipper(Block.class, new HoldsForSome())//
        .stays();
  }

  @Test public void b() {
    trimmingOf("something1(); something2(); for (final Object ¢ : os)  if (¢.equals(best))   return true; return false;")//
        .withTipper(Block.class, new HoldsForSome())//
        .gives("something1();something2();return os.stream().anyMatch(¢->¢.equals(best));")//
        .withTipper(Block.class, new HoldsForSome())//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (Object ¢ : os)  if (best.equals(¢))   return true; return false;")//
        .withTipper(Block.class, new HoldsForSome())//
        .gives("return os.stream().anyMatch(¢->best.equals(¢));")//
        .withTipper(Block.class, new HoldsForSome())//
        .stays();
  }

  @Test public void d() {
    trimmingOf("for (Object ¢ : (B)bs)  if (best.equals(¢))   return true; return false;")//
        .withTipper(Block.class, new HoldsForSome())//
        .gives("return(B)bs.stream().anyMatch(¢->best.equals(¢));")//
        .withTipper(Block.class, new HoldsForSome())//
        .stays();
  }

  @Test public void e() {
    trimmingOf("for (Object ¢ : col.le(c,(tio)n))  if (best.equals(¢))   return true; return false;")//
        .withTipper(Block.class, new HoldsForSome())//
        .gives("return col.le(c,(tio)n).stream().anyMatch(¢->best.equals(¢));")//
        .withTipper(Block.class, new HoldsForSome())//
        .stays();
  }

  @Test public void f() {
    trimmingOf("for (Object ¢ : omg ? yes : no)  if (best.equals(¢))   return true; return false;")//
        .withTipper(Block.class, new HoldsForSome())//
        .gives("return omg?yes:no.stream().anyMatch(¢->best.equals(¢));")//
        .withTipper(Block.class, new HoldsForSome())//
        .stays();
  }

  @Test public void g() {
    trimmingOf("for (final Object ¢ : os)  if (¢ == (best))   return true; return false;")//
        .withTipper(Block.class, new HoldsForSome())//
        .gives("return os.stream().anyMatch(¢->¢==(best));")//
        .withTipper(Block.class, new HoldsForSome())//
        .stays();
  }

  @Test public void h() {
    trimmingOf("for (final Object ¢ : os)  if (¢ == a + b)   return true; return false;")//
        .withTipper(Block.class, new HoldsForSome())//
        .gives("return os.stream().anyMatch(¢->¢==a+b);")//
        .withTipper(Block.class, new HoldsForSome())//
        .stays();
  }

  @Test public void i() {
    trimmingOf("for (final Object ¢ : os)  if (¢ == (omg ? yes : no))   return true; return false;")//
        .withTipper(Block.class, new HoldsForSome())//
        .gives("return os.stream().anyMatch(¢->¢==(omg?yes:no));")//
        .withTipper(Block.class, new HoldsForSome())//
        .stays();
  }
}
