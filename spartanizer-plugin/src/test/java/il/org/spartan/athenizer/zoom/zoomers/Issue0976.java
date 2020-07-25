package il.org.spartan.athenizer.zoom.zoomers;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.bloatingOf;

import org.junit.Test;

import il.org.spartan.athenizer.zoomers.LongIfBloater;

/** Unit tests for {@link LongIfBloater}
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-09 */
@SuppressWarnings("static-method")
public class Issue0976 {
  @Test public void a() {
    bloatingOf("if(a==b && c==d) { t=5; }")//
        .gives("if(a==b) { if(c==d) t=5; }");
  }
  @Test public void a1() {
    bloatingOf("if(a==b && c==d) { t=5; }")//
        .gives("if(a==b) { if(c==d) t=5; }");
  }
  @Test public void b() {
    bloatingOf("if(a && b && c) { t=5; }")//
        .gives("if(a) { if(b && c) t=5; }")//
        .gives("if(a) { if(b && c) { t=5; } }")//
        .gives("if(a) { if(b) { if(c) t=5; } }")//
        .gives("if(a) { if(b) { if(c) { t=5; } } }");//
  }
  @Test public void c() {
    bloatingOf("if(a && b) { f(); } else { g(); }")//
        .gives("if(a) if(b) f(); else { g(); } else g();");
  }
  @Test public void e() {
    bloatingOf("if(a && b && c) { f(); } else { g(); }").stays();
  }
}
