package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** This is a unit test for {@link SwitchEmpty}
 * @author Yuval Simon
 * @since 2016-11-20 */
@Ignore
@SuppressWarnings("static-method")
public class Issue233 {
  // TODO: side effects and unignore {@link Version230} eliminateSwitch test
  @Test public void a() {
    trimmingOf("switch(x) {} int x=5; ++x;").gives("int x=5; ++x;").stays();
  }

  @Test public void b() {
    trimmingOf("switch(x) {} switch(x) {}int x=5; ++x;").gives("int x=5; ++x;").stays();
  }

  @Test public void c() {
    trimmingOf("switch(x) { default: k=5; }").gives("{k=5;}");
  }

  @Test public void d() {
    trimmingOf("switch(x) { default: k=5; break; }").gives("{k=5;}");
  }

  @Test public void e() {
    trimmingOf("switch(x) {} switch(x) { case a: }int x=5; ++x;").gives("int x=5; ++x;").stays();
  }

  @Test public void f() {
    trimmingOf("switch(x) {} switch(x) { case a: }int x=5; ++x;").gives("int x=5; ++x;").stays();
  }
}
