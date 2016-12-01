package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Raviv Rachmiel
 * @since 22-11-2016 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue466 {
  @Test public void TestDoubleUnderscore() {
    trimmingOf("@SuppressWarnings(\"unused\") public void check__(Object... os) {  }")//
    .gives("@SuppressWarnings(\"unused\") public void check__(Object... __) {  }")//
    .stays();
  }
  
  @Test public void TestDoubleUnderscoreInside() {
    trimmingOf("public void check__(@SuppressWarnings(\"unused\")  Object... os) {  }")//
    .gives("public void check__(@SuppressWarnings(\"unused\")  Object... __) {  }")//
    .stays();
  }
  
  @Test public void TestDoesUseShouldntChange() {
    trimmingOf("@SuppressWarnings(\"unused\") public void check__(Object... ¢) { ¢.get(0);  }")//
    .stays();
  }
  
  @Test public void TestDoesUseShouldntChange2() {
    trimmingOf("public void check__(@SuppressWarnings(\"unused\") Object... ¢) { ¢.get(0);  }")//
    .stays();
  }
  
} 