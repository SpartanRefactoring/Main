package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue166Test {
  @Test public void dollar() {
    trimming("for(final int $:as)sum+=$;").stays();
  }

  @Test public void doubleUnderscore() {
    trimming("for(final int __:as)sum+=_;").stays();
  }

  @Test public void innerVariable() {
    trimming("for(final int i:as){int sum; f(sum+=i);}").stays();
  }

  @Test public void meaningfulName() {
    trimming("for(final String fileName: ss) {f(fileName);}").stays();
  }

  @Test public void singleUnderscore() {
    trimming("for(final int _:as)sum+=_;").stays();
  }

  @Test public void string() {
    trimming("for(String s: as)sum+=s;").to("for(String ¢:as)sum+=¢;").stays();
  }

  @Test public void unused() {
    trimming("for(final int i:as)f(sum+=j);").stays();
  }

  @Test public void vanilla() {
    trimming("for(final int i:as)sum+=i;").to("for(final int ¢:as)sum+=¢;").stays();
  }
}
