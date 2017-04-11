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
    topDownTrimming("for (final UserDefinedTipper<Statement> ¢ : tippers) if (¢.canTip(s)) return true; return false;")
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return tippers.stream().anyMatch(¢ -> ¢.canTip(s));");
  }

  @Test public void a() {
    topDownTrimming("for (final Object ¢ : os) if (¢.equals(best)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢.equals(best));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void b() {
    topDownTrimming("something1(); something2(); for (final Object ¢ : os) if (¢.equals(best)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("something1();something2();return os.stream().anyMatch(¢->¢.equals(best));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void c() {
    topDownTrimming("for (Object ¢ : os) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void d() {
    topDownTrimming("for (Object ¢ : (B)bs) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return(B)bs.stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void e() {
    topDownTrimming("for (Object ¢ : col.le(c,(tio)n)) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return col.le(c,(tio)n).stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void f() {
    topDownTrimming("for (Object ¢ : omg ? yes : no) if (best.equals(¢)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return omg?yes:no.stream().anyMatch(¢->best.equals(¢));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void g() {
    topDownTrimming("for (final Object ¢ : os) if (¢ == (best)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢==(best));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void h() {
    topDownTrimming("for (final Object ¢ : os) if (¢ == a + b) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢==a+b);")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void i() {
    topDownTrimming("for (final Object ¢ : os) if (¢ == (omg ? yes : no)) return true; return false;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("return os.stream().anyMatch(¢->¢==(omg?yes:no));")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .stays();
  }

  @Test public void j() {
    topDownTrimming("for (final Object ¢ : f.modifiers()) if (((Modifier) ¢).isFinal()) $ = true;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("$=f.modifiers().stream().anyMatch(¢->((Modifier)¢).isFinal());")//
    ;
  }

  @Test public void k() {
    topDownTrimming("for (X x : Y) if (whatever) return true;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("returnIf(Y.stream().anyMatch(x -> whatever));")//
        .gives("returnIf(Y.stream().anyMatch(λ -> whatever));")//
        .stays();
  }

  @Test public void l() {
    topDownTrimming("for (X x : Y) if (whatever) $ = true;")//
        .using(EnhancedForStatement.class, new HoldsForAny())//
        .gives("$ = Y.stream().anyMatch(x -> whatever);")//
        .gives("$ = Y.stream().anyMatch(λ -> whatever);")//
        .stays();
  }
}
