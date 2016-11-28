package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Yuval Simon
 *  @since 2016-11-20 */
@SuppressWarnings("static-method")
public class Issue233 {
  @Test public void a() {
    trimmingOf("switch(x) {} int x=5;").gives("int x=5;").stays();
  }

  @Test public void b() {
    trimmingOf("switch(x) {} switch(x) {}int x=5;").gives("int x=5;").stays();
  }

  @Test public void c() {
    trimmingOf("switch(x) { default: k=5; }").gives("{k=5;}");
  }

  @Test public void d() {
    trimmingOf("switch(x) { default: k=5; break; }").gives("{k=5;}");
  }

  @Test public void e() {
    trimmingOf("switch(x) {} switch(x) { case a: }int x=5;").gives("int x=5;").stays();
  }

  @Test public void f() {
    trimmingOf("switch(x) {} switch(x) { case a: }int x=5;").gives("int x=5;").stays();
  }
}
