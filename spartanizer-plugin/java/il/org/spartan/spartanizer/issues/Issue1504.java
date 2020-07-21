package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** TODO dormaayn: document class
 * @author Dor
 * @since 2017-06-25 */
@SuppressWarnings("static-method")
public class Issue1504 {
  @Test public void test0() {
    trimmingOf("try {runnable.run();} catch (RuntimeException it) {throw new UncheckedExecutionException(it);} catch (Error it) {"
        + "throw new ExecutionError(it);} catch (Throwable it) {throw new UncheckedExecutionException(it);}").stays();
  }
}
