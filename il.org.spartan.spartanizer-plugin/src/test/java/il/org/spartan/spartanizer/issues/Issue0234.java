package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Sapir Bismot
 * @since 2016-11-21 */
@SuppressWarnings("static-method")
public class Issue0234 {
  @Test public void b$01() {
    trimmingOf("try { f(); } catch(Exception e) { return -1; }")//
        .stays();
  }
  @Test public void b$02() {
    trimmingOf("try { } catch(Exception e) { return -1; } return true;")//
        .gives("return true;");
  }
  @Test public void b$03() {
    trimmingOf("int a; try { } catch(Exception e) { return -1; }")//
        .gives("");
  }
  @Test public void b$04() {
    trimmingOf("int a=5; try { } catch(Exception e) { return -1; } finally { ++a; }")//
        .gives("int a=5; {++a;}")//
        .gives("int a=5; ++a;");
  }
}
