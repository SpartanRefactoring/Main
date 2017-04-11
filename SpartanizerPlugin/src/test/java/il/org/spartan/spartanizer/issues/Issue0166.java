package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for {@link ForParameterRenameToIt}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0166 {
  @Test public void dollar() {
    topDownTrimming("for(final int $:as)sum+=$;")//
        .stays();
  }

  @Test public void doubleUnderscore() {
    topDownTrimming("for(final int __:as)sum+=_;")//
        .stays();
  }

  @Test public void innerVariable() {
    topDownTrimming("for(final int i:as){int sum; f(sum+=i); ++x;}")//
        .stays();
  }

  @Test public void meaningfulName() {
    topDownTrimming("for(final String fileName: ss) {f(fileName); ++x;}")//
        .stays();
  }

  @Test public void singleUnderscore() {
    topDownTrimming("for(final int _:as)sum+=_;")//
        .gives("for(final int __:as)sum+=__;")//
        .stays();
  }

  @Test public void statementBlock() {
    topDownTrimming("for(final Statement s:as){f(s);g(s);sum+=i;}")//
        .gives("for(final Statement ¢:as){f(¢);g(¢);sum+=i;}")//
        .stays();
  }

  @Test public void string() {
    topDownTrimming("for(String s:as)sum+=s;")//
        .gives("for(String ¢:as)sum+=¢;")//
        .stays();
  }

  @Test public void unused() {
    topDownTrimming("for(final int i:as)f(sum+=j);")//
        .stays();
  }

  @Test public void vanilla() {
    topDownTrimming("for(final int i:as)sum+=i;")//
        .gives("for(final int ¢:as)sum+=¢;")//
        .stays();
  }

  @Test public void vanillaBlock() {
    topDownTrimming("for(final int i:as){++i; sum+=i;}")//
        .gives("for(final int ¢:as){++¢;sum+=¢;}")//
        .stays();
  }
}
