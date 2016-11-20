package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@SuppressWarnings("static-method") public class Issue233 {
  @Ignore public void a() {
    trimmingOf("switch(x) {}").gives("").stays();
  }
  @Ignore public void b() {
    trimmingOf("switch(x) {} switch(x) {}").gives("").stays();
  }

  // not sure if need to implement the below tipper on this issue
  @Ignore public void c() {
    trimmingOf("switch(x) { default: k=5; break; }").gives("k=5").stays();
  }
}
