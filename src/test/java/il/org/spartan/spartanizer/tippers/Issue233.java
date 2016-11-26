package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method")
public class Issue233 {
  @Test public void a() {
    trimmingOf("switch(x) {} int x=5;").gives("int x=5;").stays();
  }

  @Test public void b() {
    trimmingOf("switch(x) {} switch(x) {}int x=5;").gives("int x=5;").stays();
  }

  @Ignore public void c() {
    trimmingOf("switch(x) { default: k=5; }").gives("{k=5;}");
  }
  
  @Ignore public void d() {
    trimmingOf("switch(x) { default: k=5; break; }").gives("{k=5;}");
  }
}
