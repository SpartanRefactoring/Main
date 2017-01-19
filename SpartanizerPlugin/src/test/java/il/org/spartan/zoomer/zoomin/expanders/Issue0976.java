package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.bloater.bloaters.BloatingTestUtilities.*;

import org.junit.*;

import il.org.spartan.bloater.bloaters.*;

/** Unit tests for {@link LongIfExpander}
 * @author tomerdragucki <tt>tomerd@campus.technion.ac.il</tt>
 * @since 2017-01-09 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0976 {
  @Test public void a() {
    bloatingOf("if(a==b && c==d) { t=5; }")//
        .gives("if(a==b) { if(c==d) t=5; }");
  }

  @Test public void a1() {
    bloatingOf("if(a==b && c==d) t=5;")//
        .gives("if(a==b && c==d) { t=5; }")//
        .gives("if(a==b) { if(c==d) t=5; }");
  }

  @Test public void b() {
    bloatingOf("if(a && b && c) { t=5; }")//
        .gives("if(a) { if(b && c) t=5; }")//
        .gives("if(a) { if(b && c) { t=5; } }").gives("if(a) { if(b) { if(c) t=5; } }");
  }

  @Test public void c() {
    bloatingOf("if(a && b) { f(); } else { g(); }")//
        .gives("if(a) if(b) f(); else { g(); } else { g(); }");
  }
}
