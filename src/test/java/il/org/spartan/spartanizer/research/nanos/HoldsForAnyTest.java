package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** Tests {@link HoldsForAny}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-01-05 */
@SuppressWarnings("static-method")
public class HoldsForAnyTest {
  @Test public void _a() {
    trimminKof("for (final UserDefinedTipper<Statement> ¢ : tippers) if (¢.canTip(s)) return true; return false;")
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return tippers.stream().anyMatch(¢ -> ¢.canTip(s));");
  }

  @Test public void a() {
    trimminKof("for (final Object ¢ : os) if (¢.equals(best)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢.equals(best));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void b() {
    trimminKof("something1(); something2(); for (final Object ¢ : os) if (¢.equals(best)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("something1();something2();return os.stream().anyMatch(¢->¢.equals(best));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void c() {
    trimminKof("for (Object ¢ : os) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void d() {
    trimminKof("for (Object ¢ : (B)bs) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return(B)bs.stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void e() {
    trimminKof("for (Object ¢ : col.le(c,(tio)n)) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return col.le(c,(tio)n).stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void f() {
    trimminKof("for (Object ¢ : omg ? yes : no) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return omg?yes:no.stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void g() {
    trimminKof("for (final Object ¢ : os) if (¢ == (best)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢==(best));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void h() {
    trimminKof("for (final Object ¢ : os) if (¢ == a + b) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢==a+b);")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void i() {
    trimminKof("for (final Object ¢ : os) if (¢ == (omg ? yes : no)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢==(omg?yes:no));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void j() {
    trimminKof("for (final Object ¢ : f.modifiers()) if (((Modifier) ¢).isFinal()) $ = true;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("$=f.modifiers().stream().anyMatch(¢->((Modifier)¢).isFinal());")//
    ;
  }

  @Test public void k() {
    trimminKof("for (X x : Y) if (whatever) return true;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("returnIf(Y.stream().anyMatch(x -> whatever));")//
        .gives("returnIf(Y.stream().anyMatch(λ -> whatever));")//
        .stays();
  }

  @Test public void l() {
    trimminKof("for (X x : Y) if (whatever) $ = true;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("$ = Y.stream().anyMatch(x -> whatever);")//
        .gives("$ = Y.stream().anyMatch(λ -> whatever);")//
        .stays();
  }
}
