package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
/* Tests {@link FindFirst} and {@link ForLoop.FindFirst}
  @author Ori Marcovitch
 * @since Jan 18, 2017 */
@SuppressWarnings("static-method")
public class FindFirstTest {
  @Test public void a() {
    trimmingOf("for(Object i : is) if(i.isN()) return i; return null;")//
        .using(EnhancedForStatement.class, new FindFirst())//
        .gives("return is.stream().filter(i->i.isN()).findFirst().orElse(null);");
  }

  @Test public void b() {
    trimmingOf("for(Object i : is) if(i.isN()) return i; throw new None();")//
        .using(EnhancedForStatement.class, new FindFirst())//
        .gives("return is.stream().filter(i->i.isN()).findFirst().orElseThrow(()->new None());");
  }

  @Test public void c() {
    trimmingOf("for(Object i : is) if(i.isN()) {theChosen = i; break;}")//
        .using(EnhancedForStatement.class, new FindFirst())//
        .gives("theChosen=is.stream().filter(i->i.isN()).findFirst().orElse(theChosen);");
  }

  @Test public void d() {
    trimmingOf("for (EF $ : EF) if ($.getX() == x && $.getY() == y && $.getZ() == z) return $.gI(); return 0;")//
        .using(EnhancedForStatement.class, new FindFirst())//
        .gives("return EF.stream().filter($->$.getX()==x&&$.getY()==y&&$.getZ()==z).map($->$.gI()).findFirst().orElse(0);");
  }

  @Test public void e() {
    trimmingOf("for(Object i : is) if(i.isN()) return i; return 0;")//
        .using(EnhancedForStatement.class, new FindFirst())//
        .gives("return is.stream().filter(i->i.isN()).findFirst().orElse(0);");
  }

  @Test public void f() {
    trimmingOf(" for (final G $ : G.values()) if ($.clazz.isA(¢)) return $; return null;")//
        .using(EnhancedForStatement.class, new FindFirst())//
        .gives("return G.values().stream().filter($->$.clazz.isA(¢)).findFirst().orElse(null);");
  }

  @Test public void g() {
    trimmingOf("for (ASTNode $ = ¢; $ != null; $ = p($)) if (iz.m($)) return az.m($); return null;")//
        .using(ForStatement.class, new ForLoop.FindFirst())//
        .gives("return from(¢).step(($)->$!=null).to(($)->$=p($)).findFirst($->iz.m($)).map(($)->az.m($)).orElse(null);");
  }

  @Test public void h() {
    trimmingOf("for (ASTNode $ = ¢; $ != null; $ = p($)) if (iz.m($)) return az.m($); return null;")//
        .using(ForStatement.class, new ForEachInRange(), new ForLoop.FindFirst())//
        .gives("return from(¢).step(($)->$!=null).to(($)->$=p($)).findFirst($->iz.m($)).map(($)->az.m($)).orElse(null);");
  }

  @Test public void i() {
    trimmingOf("for(Object i : is) if(i.isN()) return i;")//
        .using(EnhancedForStatement.class, new FindFirst())//
        .gives("returnFirstIfAny(is).matches(i->i.isN());");
  }

  @Test public void j() {
    trimmingOf("for (ASTNode $ = ¢; $ != null; $ = p($)) if (iz.m($)) return az.m($);")//
        .using(ForStatement.class, new ForLoop.FindFirst())//
        .gives("returnFirstIfAny(from(¢).step(($)->$!=null).to(($)->$=p($))).matches($->iz.m($)).map(az.m($));");
  }

  @Test public void k() {
    trimmingOf("for (EF $ : EF) if ($.getX() == x && $.getY() == y && $.getZ() == z) return $.gI();")//
        .using(EnhancedForStatement.class, new FindFirst())//
        .gives("returnFirstIfAny(EF).matches($->$.getX()==x&&$.getY()==y&&$.getZ()==z).map($->$.gI());");
  }
}
