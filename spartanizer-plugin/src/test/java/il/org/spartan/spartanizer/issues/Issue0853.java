package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.trimmingOf;

import org.eclipse.jdt.core.dom.ForStatement;
import org.junit.Test;

import il.org.spartan.spartanizer.tippers.ReplaceForWithRange;

/** Tests for the GitHub issue thus numbered
 * @author Dan Abramovich
 * @since 28-11-2016 */
@SuppressWarnings("static-method")
public class Issue0853 {
  @Test public void test0() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){++x;}") //
        .using(new ReplaceForWithRange(), ForStatement.class).gives("for(Integer ¢ : range.from(3).to(10)){{++x;}}")//
        .gives("for(Integer ¢ : range.from(3).to(10))++x;")//
        .stays();
  }
  @Test public void test1() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){++x;¢+=x;++y;}") //
        .using(new ReplaceForWithRange(), ForStatement.class)//
        .stays();
  }
  @Test public void test2() {
    trimmingOf("for(int ¢ = 3; ¢ < 10; ++¢){++x;++¢;++y;}") //
        .using(new ReplaceForWithRange(), ForStatement.class)//
        .stays();
  }
  @Test public void test3() {
    trimmingOf("for(int ¢ = 10; ¢ > 5; --¢){++x;++y;}") //
        .using(new ReplaceForWithRange(), ForStatement.class).gives("for(Integer ¢ : range.from(10).step(-1).to(5)){{++x;++y;}}")//
        .gives("for(Integer ¢ : range.from(10).step(-1).to(5)){++x;++y;}") //
        .stays();
  }
  @Test public void test4() {
    trimmingOf("for(int ¢ = 10; ¢ > 5; ¢-=2){++x;++y;}") //
        .using(new ReplaceForWithRange(), ForStatement.class).gives("for(Integer ¢ : range.from(10).step(-2).to(5)){{++x;++y;}}")//
        .gives("for(Integer ¢ : range.from(10).step(-2).to(5)){++x;++y;}")//
        .stays();
  }
}
