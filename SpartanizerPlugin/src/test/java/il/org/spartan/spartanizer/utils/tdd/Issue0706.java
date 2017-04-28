package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import org.junit.*;

import fluent.ly.*;
import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Unit tests for issue #706
 * @author Sapir Bismot
 * @author Yaniv Levinsky
 * @since Jan 15, 2017 */
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0706 {
  @Test public void test00() {
    getAll2.stringVariables(null);
  }

  @Test public void test02() {
    assert getAll2.stringVariables(az.methodDeclaration(make.ast("void f(int n);"))).isEmpty();
  }

  @Test public void test03() {
    azzert.that(1, is(getAll2.stringVariables(az.methodDeclaration(make.ast("void f(String s);"))).size()));
  }

  @Test public void test04() {
    azzert.that(0, is(getAll2.stringVariables(az.methodDeclaration(make.ast("void f(int x, int y);"))).size()));
  }

  @Test public void test05() {
    azzert.that(1, is(getAll2.stringVariables(az.methodDeclaration(make.ast("void f(int x, String s1, double y);"))).size()));
  }

  @Test public void test06() {
    azzert.that(1, is(getAll2.stringVariables(az.methodDeclaration(make.ast("public String numToString(int x, String s1);"))).size()));
  }

  @Test public void test07() {
    azzert.that(2, is(getAll2.stringVariables(az.methodDeclaration(make.ast("public String stringCopy(String s1, String s2, int size);"))).size()));
  }

  @Test public void test08() {
    azzert.that("s1",
        is(the.headOf(getAll2.stringVariables(az.methodDeclaration(make.ast("public String stringCopy(String s1, String s2, int size);")))).getName()
            .getIdentifier()));
  }

  @Test public void test09() {
    azzert.that("s2", is(getAll2.stringVariables(az.methodDeclaration(make.ast("public String stringCopy(String s1, String s2, int size);"))).get(1)
        .getName().getIdentifier()));
  }
}
