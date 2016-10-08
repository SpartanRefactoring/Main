package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/** 
 * @author Alex Kopzon
 * @since 2016 */
@SuppressWarnings("static-method") public class Issue428 {
  @Test public void a() {
    trimmingOf("func(int i){int j;System.out.println(j);System.out.println($);return j;}").stays();
  }
}
