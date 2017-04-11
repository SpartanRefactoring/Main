package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;

/** @author Dor Ma'ayan
 * @since 2017-04-09 */
@SuppressWarnings("static-method")
public class Issue1157 {
  /** [[SuppressWarningsSpartan]] - see #1245 */
  @Test public void t1() {
    trimminKof("" //
        + "@Override public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> function) {" //
        + "  checkNotNull(key);" //
        + "  checkNotNull(function);" //
        + "  return compute(key, (k, oldValue) -> (oldValue == null) ? null : function.apply(k, oldValue));" //
        + "}").stays();
  }
}
