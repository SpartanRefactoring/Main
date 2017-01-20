package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

/** @author Ori Marcovitch
 * @since 2016 */
@SuppressWarnings("static-method")
public class FindFirstTest {
  @Test public void a() {
    trimmingOf("for(Object i : is) if(i.isNice()) return i; return null;")//
        .using(Block.class, new FindFirst())//
        .gives("return is.stream().filter(i->i.isNice()).findFirst().orElse(null);");
  }

  @Test public void b() {
    trimmingOf("for(Object i : is) if(i.isNice()) return i; throw new None();")//
        .using(Block.class, new FindFirst())//
        .gives("return is.stream().filter(i->i.isNice()).findFirst().orElseThrow(()->new None());");
  }

  @Test public void c() {
    trimmingOf("for(Object i : is) if(i.isNice()) {theChosen = i; break;}")//
        .using(Block.class, new FindFirst())//
        .gives("theChosen=is.stream().filter(i->i.isNice()).findFirst().orElse(theChosen);");
  }

  @Test public void d() {
    trimmingOf("for (BlockState $ : blocks) if ($.getX() == x && $.getY() == y && $.getZ() == z) return $.getTypeId(); return 0;")//
        .using(Block.class, new FindFirst())//
        .gives("return blocks.stream().filter($->$.getX()==x&&$.getY()==y&&$.getZ()==z).map($->$.getTypeId()).findFirst().orElse(0);");
  }

  @Test public void e() {
    trimmingOf("for(Object i : is) if(i.isNice()) return i; return 0;")//
        .using(Block.class, new FindFirst())//
        .gives("return is.stream().filter(i->i.isNice()).findFirst().orElse(0);");
  }

  @Test public void f() {
    trimmingOf(" for (final TipperGroup $ : TipperGroup.values())    if ($.clazz.isAssignableFrom(¢))      return $; return null;")//
        .using(Block.class, new FindFirst())//
        .gives("return TipperGroup.values().stream().filter($->$.clazz.isAssignableFrom(¢)).findFirst().orElse(null);");
  }
}
