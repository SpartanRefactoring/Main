package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.research.nanos.*;

/** @author Ori Marcovitch
 * @since 2016 */
@Ignore
@SuppressWarnings("static-method")
public class FindFirstTest {
  @Test public void a() {
    trimmingOf("for(Object i : is) if(i.isNice()) return i; return null;")//
        .using(Block.class, new FindFirst())//
        .gives("return is.stream().findFirst(i -> i.isNice()).get();");
  }

  @Test public void b() {
    trimmingOf("for(Object i : is) if(i.isNice()) return i; throw new None();")//
        .using(Block.class, new FindFirst())//
        .gives("if(is.stream().anyMatch(i->i.isNice()))return is.stream().findFirst(i->i.isNice()).get();throw new None();");
  }

  @Test public void c() {
    trimmingOf("for(Object i : is) if(i.isNice()) {theChosen = i; break;}")//
        .using(Block.class, new FindFirst())//
        .gives("theChosen=is.stream().findFirst(i->i.isNice()).get();");
  }

  @Test public void d() {
    trimmingOf("for (BlockState $ : blocks) if ($.getX() == x && $.getY() == y && $.getZ() == z) return $.getTypeId(); return 0;")//
        .using(Block.class, new FindFirst())//
        .gives("return blocks.stream().findFirst($->$.getX()==x&&$.getY()==y&&$.getZ()==z).map($->$.getTypeId()).defaultTo(0).get();");
  }

  @Test public void e() {
    trimmingOf("for(Object i : is) if(i.isNice()) return i; return 0;")//
        .using(Block.class, new FindFirst())//
        .gives("return is.stream().findFirst(i->i.isNice()).defaultTo(0).get();");
  }
}
