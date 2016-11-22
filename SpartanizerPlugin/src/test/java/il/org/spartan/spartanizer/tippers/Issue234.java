package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** @author Sapir Bismot
 * @since 2016-11-21 */
@SuppressWarnings("static-method")
@Ignore
public class Issue234 {
  @Test public void b$01() {
    trimmingOf("try { f(); } catch(Exception e) { return -1; }").stays();
  }

  @Test public void b$02() {
    trimmingOf("try { } catch(Exception e) { return -1; } return true;").gives("return true;");
  }

  @Test public void b$03() {
    trimmingOf("int a; try { } catch(Exception e) { return -1; }").gives("int a;");
  }
  @Test public void b$04() {
    trimmingOf("int a=5; try { } catch(Exception e) { return -1; } finally { ++a; }").gives("int a=5; ++a;");
  }
}
