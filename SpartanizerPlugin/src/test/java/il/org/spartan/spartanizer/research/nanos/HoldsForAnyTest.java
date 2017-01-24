package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class HoldsForAnyTest {
  @Test public void _a() {
    trimmingOf(" for (final UserDefinedTipper<Statement> ¢ : tippers) if (¢.canTip(s)) return true; return false;")
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return tippers.stream().anyMatch(¢ -> ¢.canTip(s));");
  }

  @Test public void a() {
    trimmingOf("for (final Object ¢ : os) if (¢.equals(best)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢.equals(best));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void b() {
    trimmingOf("something1(); something2(); for (final Object ¢ : os) if (¢.equals(best)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("something1();something2();return os.stream().anyMatch(¢->¢.equals(best));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void c() {
    trimmingOf("for (Object ¢ : os) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void d() {
    trimmingOf("for (Object ¢ : (B)bs) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return(B)bs.stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void e() {
    trimmingOf("for (Object ¢ : col.le(c,(tio)n)) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return col.le(c,(tio)n).stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void f() {
    trimmingOf("for (Object ¢ : omg ? yes : no) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return omg?yes:no.stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void g() {
    trimmingOf("for (final Object ¢ : os) if (¢ == (best)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢==(best));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void h() {
    trimmingOf("for (final Object ¢ : os) if (¢ == a + b) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢==a+b);")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void i() {
    trimmingOf("for (final Object ¢ : os) if (¢ == (omg ? yes : no)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢==(omg?yes:no));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  // TODO Ori Marchovitch
  @Ignore @Test public void j() {
    trimmingOf(" for (final Object ¢ : f.modifiers()) if (((Modifier) ¢).isFinal()) $ = true;")//
        .gives("return os.stream().anyMatch(¢->¢==(omg?yes:no));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }
}
