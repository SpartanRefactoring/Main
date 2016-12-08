package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

@Ignore
@SuppressWarnings("static-method")
public class Issue909 {
  @Test public void b$03() {
    trimmingOf("int a; try { } catch(Exception e) { return -1; }").gives("try { } catch(Exception e) { return -1; }");
  }
}
