package il.org.spartan.spartanizer.wring;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.wring.TrimmerTestsUtils.*;

import java.util.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.spartanizations.*;

/** Unit tests for {@link NameYourClassHere}
 * @author TODO // Write your name here
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class SimplifyBlockTest {

  @Test public void complexEmpty0() {
    trimming("{;}").to("/* empty */    ");
  }

  @Test public void complexEmpty0A() {
    trimming("{}").to("/* empty */");
  }

  @Test public void complexEmpty0B() {
    trimming("{;}").to("/* empty */");
  }

  @Test public void complexEmpty0C() {
    trimming("{{;}}").to("/* empty */");
  }

  @Test public void complexEmpty0D() {
    trimming("{;;;{;;;}{;}}").to("/* empty */    ");
  }

  @Test public void complexEmpty1() {
    trimming("{;;{;{{}}}{}{};}").to("/* empty */ ");
  }

  @Test public void complexSingleton() {
    assertSimplifiesTo("{;{{;;return b; }}}", "return b;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void deeplyNestedReturn() {
    assertSimplifiesTo("{{{;return c;};;};}", "return c;", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void empty() {
    assertSimplifiesTo("{;;}", "", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void emptySimpler() {
    assertSimplifiesTo("{;}", "", new BlockSimplify(), Wrap.Statement);
  }
  @Test public void emptySimplest() {
    assertSimplifiesTo("{}", "", new BlockSimplify(), Wrap.Statement);
  }

  @Test public void expressionVsExpression() {
    trimming("6 - 7 < a * 3").to("-1 < 3 * a");
  }

  @Test public void literalVsLiteral() {
    trimming("if (a) return b; else c();").to("if(a)return b;c();");
  }

  @Test public void threeStatements() {
    assertSimplifiesTo("{i++;{{;;return b; }}j++;}", "i++;return b;j++;", new BlockSimplify(), Wrap.Statement);
  }
}
